#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <dirent.h>
#include <stdlib.h>
#include <unistd.h>
#include <stdio.h>
#include <string.h>
#include <errno.h>
#include <openssl/evp.h>
#include <threads.h>
#include "options.h"
#include "queue.h"


#define MAX_PATH 1024
#define BLOCK_SIZE (10*1024*1024)
#define MAX_LINE_LENGTH (MAX_PATH * 2)


struct file_md5 {
    char *file;
    unsigned char *hash;
    unsigned int hash_size;
};

struct read_files_args{
    char *dir;
    queue q;
};

void get_entries(char *dir, queue q);


void print_hash(struct file_md5 *md5) {
    for(int i = 0; i < md5->hash_size; i++) {
        printf("%02hhx", md5->hash[i]);
    }
}


void read_hash_file(char *file, char *dir, queue q) {
    FILE *fp;
    char line[MAX_LINE_LENGTH];
    char *file_name, *hash;
    int hash_len;
    
    if((fp = fopen(file, "r")) == NULL) {
        
        printf("Could not open %s : %s\n", file, strerror(errno));
        exit(0);
    }

    while(fgets(line, MAX_LINE_LENGTH, fp) != NULL) {
        char *field_break;
        struct file_md5 *md5 = malloc(sizeof(struct file_md5));

        if((field_break = strstr(line, ": ")) == NULL) {
            printf("Malformed md5 file\n");
            exit(0);
        }
        *field_break = '\0';

        file_name = line;
        hash      = field_break + 2;
        hash_len  = strlen(hash);

        md5->file      = malloc(strlen(file_name) + strlen(dir) + 2);
        sprintf(md5->file, "%s/%s", dir, file_name);
        md5->hash      = malloc(hash_len / 2);
        md5->hash_size = hash_len / 2;


        for(int i = 0; i < hash_len; i+=2)
            sscanf(hash + i, "%02hhx", &md5->hash[i / 2]);

        q_insert(q, md5);
    }

    fclose(fp);
}


void sum_file(struct file_md5 *md5) {
    EVP_MD_CTX *mdctx;
    int nbytes;
    FILE *fp;
    char *buf;
    if((fp = fopen(md5->file, "r")) == NULL) {
        printf("aqui\n");
        printf("Could not open %s %s \n", md5->file, strerror(errno));
        return;
    }

    buf = malloc(BLOCK_SIZE);
    const EVP_MD *md = EVP_get_digestbyname("md5");

    mdctx = EVP_MD_CTX_create();
    EVP_DigestInit_ex(mdctx, md, NULL);

    while((nbytes = fread(buf, 1, BLOCK_SIZE, fp)) >0)
        EVP_DigestUpdate(mdctx, buf, nbytes);

    md5->hash = malloc(EVP_MAX_MD_SIZE);
    EVP_DigestFinal_ex(mdctx, md5->hash, &md5->hash_size);

    EVP_MD_CTX_destroy(mdctx);
    free(buf);
    fclose(fp);
}


void recurse(char *entry, void *arg) {
    queue q = * (queue *) arg;
    struct stat st;

    stat(entry, &st);

    if(S_ISDIR(st.st_mode))
        get_entries(entry, q);
}


void add_files(char *entry, void *arg) {
    queue q = * (queue *) arg;
    struct stat st;

    stat(entry, &st);

    if(S_ISREG(st.st_mode))
        q_insert(q, strdup(entry));
}


void walk_dir(char *dir, void (*action)(char *entry, void *arg), void *arg) {
    DIR *d;
    struct dirent *ent;
    char full_path[MAX_PATH];

    if((d = opendir(dir)) == NULL) {
        printf("Could not open dir %s\n", dir);
        return;
    }

    while((ent = readdir(d)) != NULL) {
        if(strcmp(ent->d_name, ".") == 0 || strcmp(ent->d_name, "..") ==0)
            continue;

        snprintf(full_path, MAX_PATH, "%s/%s", dir, ent->d_name);

        action(full_path, arg);
    }

    closedir(d);
}


void get_entries(char *dir, queue q) {
    walk_dir(dir, add_files, &q);
    walk_dir(dir, recurse, &q);
}


void check(struct options opt) {
    queue in_q;
    struct file_md5 *md5_in, md5_file;

    in_q  = q_create(opt.queue_size);

    read_hash_file(opt.file, opt.dir, in_q);

    while((md5_in = q_remove(in_q))) {
        md5_file.file = md5_in->file;

        sum_file(&md5_file);

        if(memcmp(md5_file.hash, md5_in->hash, md5_file.hash_size)!=0) {
            printf("File %s doesn't match.\nFound:    ", md5_file.file);
            print_hash(&md5_file);
            printf("\nExpected: ");
            print_hash(md5_in);
            printf("\n");
        }

        free(md5_file.hash);

        free(md5_in->file);
        free(md5_in->hash);
        free(md5_in);
    }

    q_destroy(in_q);
}

void *get_entries_thread(void *arg){
    struct read_files_args *args=arg;
    get_entries(args->dir,args->q);
    q_set_finished(args->q);
    return NULL;
}


struct sum_files_args{
    queue in_q;
    queue out_q;
    int *counter;
    mtx_t *mutex;
};



void *sum_files_thread(void *arg) {
    struct sum_files_args *args = arg;
    struct file_md5 *md5;
    char *ent;
    

    while((ent = q_remove(args->in_q)) != NULL) {
        md5 = malloc(sizeof(struct file_md5));

        md5->file = ent;
        sum_file(md5);

        q_insert(args->out_q, md5);
        
    }
        
        mtx_lock(args->mutex);
        (*args->counter)--;
        if (*args->counter == 0) {
        q_set_finished(args->out_q);
        }
        mtx_unlock(args->mutex);
        return NULL;
    }

struct write_files_args{
    FILE *file;
    queue q;
    int dirname_len;
     
};    

void *write_files_thread(void *arg) {
    struct write_files_args *args = (struct write_files_args *)arg;
    FILE *out = args->file;
    queue out_q = args->q;
    int dirname_len = args->dirname_len;
    struct file_md5 *md5;

    while((md5 = q_remove(out_q)) != NULL) {
        fprintf(out, "%s: ", md5->file + dirname_len);

        for(int i = 0; i < md5->hash_size; i++)
            fprintf(out, "%02hhx", md5->hash[i]);
        fprintf(out, "\n");

        free(md5->file);
        free(md5->hash);
        free(md5);
    }

    return NULL;
}

void sum(struct options opt) {
    queue in_q, out_q;
    FILE *out;
    struct file_md5 *md5;
    int dirname_len;
    int *counter = &(opt.num_threads);
    mtx_t mutex;
    mtx_init(&mutex, mtx_plain);

    in_q = q_create(opt.queue_size);
    out_q = q_create(1);
    struct read_files_args args = {opt.dir, in_q};

    thrd_t thread_entries;
    thrd_create(&thread_entries, (void *) get_entries_thread, &args);

    // Crear threads para calcular hash de cada archivo
    thrd_t threads[opt.num_threads];
    struct sum_files_args sum_args = {in_q, out_q, counter, &mutex};
    for(int i = 0; i < opt.num_threads; i++) {
        thrd_create(&threads[i], (void *) sum_files_thread, &sum_args);
    }

    // Abrir archivo de salida
    if((out = fopen(opt.file, "w")) == NULL) {
        printf("Could not open output file\n");
        exit(0);
    }

    dirname_len = strlen(opt.dir) + 1; // length of dir + /

    // Separar la escritura de los hashes en su propio thread
    struct write_files_args write_args = {out, out_q, dirname_len};
    thrd_t thread_write_files;
    thrd_create(&thread_write_files, (void *) write_files_thread, &write_args);

    // Esperar a que todos los threads de cálculo de hashes terminen
    for(int i = 0; i < opt.num_threads; i++) {
        thrd_join(threads[i], NULL);
    }

    // Esperar a que el thread de lectura de archivos termine
    thrd_join(thread_entries, NULL);

    // Esperar a que el thread de escritura de hashes termine
    thrd_join(thread_write_files, NULL);

    fclose(out);
    q_destroy(in_q);
    q_destroy(out_q);
    mtx_destroy(&mutex);
}



int main(int argc, char *argv[]) {

    struct options opt;

    opt.num_threads = 5;
    opt.queue_size  = 1000;
    opt.check       = true;
    opt.file        = NULL;
    opt.dir         = NULL;

    read_options (argc, argv, &opt);

    if(opt.check)
        check(opt);
    else
        sum(opt);

}
