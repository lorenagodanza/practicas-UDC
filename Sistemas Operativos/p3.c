
//Lorena Godón Danza 
//Marta Vidal López
//GRUPO: 3.1

//LIBRERÍAS
#include <libgen.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <time.h>
#include <sys/utsname.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <dirent.h>
#include <pwd.h>
#include <grp.h>
#include <sys/ipc.h>
#include <sys/shm.h>
#include <errno.h>
#include <sys/mman.h>
#include <sys/resource.h>
#include <sys/wait.h>
#include "list.h"
//#include "list.c"
#include "listaMem.h"
//#include "listMem.c"
#include "ProcList.h"
//#include "ProcList.c"

//CONSTANTES Y VARIABLES
#define MAX_INPUT_SIZE 1024
#define MAX_TOKENS 64
#define MAXNAME 256
#define TAMANO 2048
#define MAXVAR 256


extern char **environ;
extern char **_environ;
char ** arg3env=NULL;
tList list;
tListM LMem;
tListP Lproc;
int global1=1,global2=2,global3=3;

//FUNCIONES PO
int autores(char *tokens[]);
int pid(char *tokens[]);
int carpeta(char *tokens[]);
int fecha(char *tokens[]);
int hist(char *tokens[]);
int command(char *tokens[]);
int infosis(char *tokens[]);
int ayuda(char *tokens[]);

//FUNCIONES P1
int create(char *tokens[]);
int delete(char *tokens[]);
int deltree(char *tokens[]);
int statt(char *tokens[]);
int listdir(char *tokens[]);


//FUNCIONES P2
int allocate(char *tokens[]);
int dealloc(char *tokens[]);
int io (char *tokens[]);
int volcarmem(char *tokens[]);
int memfill (char *tokens[]);
int memory(char *tokens[]);
int recurse(char *tokens[]);

//FUNCIONES AUXILIARES
int process_input(char *tokens[]);
int split_string(char * cadena, char * trozos[]);
int isNumber(char *num);
int fin(char *tokens[]);
char LetraTF (mode_t m);
void borrar_directorios(char *directorio);
char * ConvierteModo2 (mode_t m);
void tiempo(time_t t,int i);
void imprimirInfoFile(int listar_largo, int link, int acc, char* nombre);
int EsDirectorio( char *dir);
void printDir(int listar_largo, int link, int acc, int hid, int rec, char* ruta);
void printSubDirs(int listar_largo, int link, int acc, int hid, int rec, char* ruta);

//AUXILIARES P2
void imprimir_malloc(tListM M);
void imprimir_shared(tListM M);
void imprimir_mmap(tListM M);
void mallocc(char *tokens[]);
void * ObtenerMemoriaShmget (key_t clave, size_t tam);
void sharedd(char *tokens[]);
void do_AllocateCreateshared (char *tr[]);
void * MapearFichero (char * fichero, int protection);
void do_AllocateMmap(char *arg[]);
void FreeMalloc(char *tokens[]);
void FreeShared(char *tokens[]);
void FreeMmap(char *tokens[]);
ssize_t LeerFichero (char *f, void *p, size_t cont);
ssize_t EscribirFichero (char *f, void *p, size_t cont,int overwrite);
void do_I_O_read (char *ar[]);
void do_I_O_write (char *ar[],int overwrite);
void print_byte(void *ptr, int n);
void memfill_aux(void *ptr, size_t cnt, unsigned char caracter);
void Recursiva (int n);



int split_string(char * cadena, char * trozos[]){
    int i=1;
    if ((trozos[0]=strtok(cadena," \n\t"))==NULL)
        return 0;
    while ((trozos[i]=strtok(NULL," \n\t"))!=NULL)
        i++;
    return i;
}

int isNumber(char *num){
    for(int i=0;i<strlen(num);i++){
        if(num[i]<48||num[i]>57){//numeros en ASCII
            if(num[i]!=45){//quitamos el -
                return 0;
            }
        }
    }
    return 1;
}

int fin(char *tokens[]){
    return 1;    

}

char LetraTF (mode_t m){
    switch (m&S_IFMT) { /*and bit a bit con los bits de formato,0170000 */
        case S_IFSOCK: return 's'; /*socket */
        case S_IFLNK: return 'l'; /*symbolic link*/
        case S_IFREG: return '-'; /* fichero normal*/
        case S_IFBLK: return 'b'; /*block device*/
        case S_IFDIR: return 'd'; /*directorio */
        case S_IFCHR: return 'c'; /*char device*/
        case S_IFIFO: return 'p'; /*pipe*/
        default: return '?'; /*desconocido, no deberia aparecer*/
    }
}

void borrar_directorios(char *directorio){
    char ruta[MAX_INPUT_SIZE];
    DIR *dir;
    struct dirent *rdir;
    struct stat st;
    getcwd(ruta,MAX_INPUT_SIZE);
    dir= opendir(directorio);
    if(dir==NULL){
        perror( "error" );
    }else{
        if(chdir(directorio)==0){
            while((rdir=readdir(dir))!=NULL){
                if(strncmp(rdir->d_name,".",2)!=0 && strncmp(rdir->d_name,"..",3)!=0){
                    if(lstat(rdir->d_name,&st)==-1){
                        perror( "error" );
                    }else{
                        if(EsDirectorio(rdir->d_name)){
                            borrar_directorios(rdir->d_name);
                        }else if(S_ISREG (st.st_mode) || S_ISLNK (st.st_mode)){
                            if(unlink(rdir->d_name)!=0){
                                perror( "error" );
                            }
                        }

                    }
                }
                }    
                chdir(ruta);
                if(rmdir(directorio)!=0){
                    perror( "error" );
                }
                closedir(dir);
        }else{
            perror( "error" );
        }
        
        chdir(ruta);

        }   
    
}


int EsDirectorio( char *dir){
    struct stat st;
    if(lstat(dir, &st)!=-1) {
        if(S_ISDIR(st.st_mode))
            return 1;
        else return 0;
    }
    else return 0;


}

char * ConvierteModo2 (mode_t m){
static char permisos[12];
strcpy (permisos,"---------- ");
permisos[0]=LetraTF(m);//mira el tipo de fichero
if (m&S_IRUSR) permisos[1]='r'; /*propietario*/
if (m&S_IWUSR) permisos[2]='w';
if (m&S_IXUSR) permisos[3]='x';
if (m&S_IRGRP) permisos[4]='r'; /*grupo*/
if (m&S_IWGRP) permisos[5]='w';
if (m&S_IXGRP) permisos[6]='x';
if (m&S_IROTH) permisos[7]='r'; /*resto*/
if (m&S_IWOTH) permisos[8]='w';
if (m&S_IXOTH) permisos[9]='x';
if (m&S_ISUID) permisos[3]='s'; /*setuid, setgid y stickybit*/
if (m&S_ISGID) permisos[6]='s';
if (m&S_ISVTX) permisos[9]='t';
return (permisos);
}

void tiempo(time_t t,int i){
    struct tm fecha=*localtime(&t);
    if(i==1){
        printf("%04d/%02d/%02d-%02d:%02d ",fecha.tm_year+1900,fecha.tm_mon+1, fecha.tm_mday,  fecha.tm_hour, fecha.tm_min);
    }else if(i==2){
      printf("%s", ctime(&t));  
    }else if (i==3){
        printf("%04d/%02d/%02d %02d:%02d:%02d ",fecha.tm_year+1900,fecha.tm_mon+1, fecha.tm_mday,  fecha.tm_hour, fecha.tm_min, fecha.tm_sec);
    }
    

}

void imprimirInfoFile(int listar_largo, int link, int acc, char* nombre){
    struct stat st;
    struct passwd *usr;
    struct group *grp;
    char lk[5000];
    char* perm;
    if(listar_largo){
        if(lstat(nombre,&st)==-1){
            perror( "error" );
        }else{
            usr=getpwuid(st.st_uid);
            if(usr!=NULL){
                grp=getgrgid(st.st_gid);
                if(grp!=NULL){
                    if(acc){//damos formato a las fechas
                        tiempo(st.st_atime,1);
                    }else{
                        tiempo(st.st_mtime,1);
                    } 
                    perm=ConvierteModo2(st.st_mode);                               
                    printf("%ld (%ld) %s %s %s %ld %s", st.st_nlink, st.st_ino, usr->pw_name, grp->gr_name, perm, st.st_size, nombre);
                    
                    if(link){
                        if(S_ISLNK(st.st_mode)){
                            if(readlink(nombre,lk,2000)!=-1){
                                printf("-> %s",lk);
                            }else{
                                perror( "error" );
                            }
                        }
                    } 
                }else{
                    perror( "error" );
                }
            }else{
                perror( "error" );
            }  
        printf("\n");
        }
    }else{
        if(lstat(nombre,&st)==-1){
            perror( "error" );
        }else{
            printf("%ld %s\n",st.st_size ,nombre);
        }
    
    }

}

    

void printDir(int listar_largo, int link, int acc, int hid, int rec, char* ruta){
    DIR *d;
    struct dirent *dire;
    char path[MAX_INPUT_SIZE];

    d = opendir(ruta);

    if (d) {
        if(rec==2){
            printSubDirs(listar_largo, link, acc, hid, rec, ruta);
        }

        printf("************%s\n", ruta);
        while ((dire = readdir(d)) != NULL) {
            if (hid || dire->d_name[0]!='.'){
                sprintf(path,"%s/%s",ruta,dire->d_name);
                imprimirInfoFile(listar_largo, link, acc, path);
            }
        }

        if(rec==1){
            printSubDirs(listar_largo, link, acc, hid, rec, ruta);
        }
    }
    closedir(d);
}

void printSubDirs(int listar_largo, int link, int acc, int hid, int rec, char* ruta){

    DIR *d2;
    struct dirent *dire2;
    char path[MAX_INPUT_SIZE];

    d2 = opendir(ruta);

    if (d2) {
        while ((dire2 = readdir(d2)) != NULL) {

            sprintf(path,"%s/%s",ruta,dire2->d_name);

            if((strcmp(dire2->d_name,".")!=0 && strcmp(dire2->d_name,"..")!=0) && (EsDirectorio(path)) && (hid || dire2->d_name[0]!='.')){
                printDir(listar_largo, link, acc,hid,rec, path);
            }
        }
        closedir(d2);
    }
}


//CODIGO P2
void imprimir_malloc(tListM M){
    tItemM item;
    for(tPosM p=M; p!=NULL; p=nextM(p,M)){
        item=getItemM(p,M);
        if(item.tipo==MALLOC){
            printf("%p %ld \tmalloc\t",item.dir_malloc,item.tam);
            tiempo(item.hora,2);
        }
            
            
        
    }
}

void imprimir_shared(tListM M){
    tItemM item;
    for(tPosM p=M; p!=NULL; p=nextM(p,M)){
        item=getItemM(p,M);
        if(item.tipo==SHARED){
            printf("%p %ld \tshared\t",item.dir_malloc,item.tam);
            tiempo(item.hora,2);
        }
            
            
        
    }
}

void imprimir_mmap(tListM M){
    tItemM item;
    for(tPosM p=M; p!=NULL; p=nextM(p,M)){
        item=getItemM(p,M);
        if(item.tipo==MMAP){
            printf("%p %ld \tmmap\t",item.dir_malloc,item.tam);
            tiempo(item.hora,2);
        }
            
            
        
    }
}

void mallocc(char *tokens[]){
    tItemM item;
    unsigned long int tamano;
    if(tokens[1]==NULL){
        printf("******Lista de bloques asignados malloc para el proceso %d\n",getpid());
        imprimir_malloc(LMem);
    }else{
        tamano=(unsigned long int) strtoul(tokens[1], NULL, 10);//convertimos o string a un numero en decimal
        item.dir_malloc=malloc(tamano);
    if(tamano==0){//si strtoul devolve 0 significa que non se puido convertir
        printf("No se asignan bloques de 0 bytes\n");
    }else{
        strcpy(item.nome_ficheiro,"");
        item.key=0;
        item.tipo=MALLOC;
        item.tam=tamano;
        insertItemM(item,NULL,&LMem);
        printf("Asignados %ld bytes en %p\n", item.tam,item.dir_malloc);
     }

    }
       
}
void * ObtenerMemoriaShmget (key_t clave, size_t tam)
{
    void * p;
    int aux,id,flags=0777;
    struct shmid_ds s;
    tItemM item;

    if (tam)     /*tam distito de 0 indica crear */
        flags=flags | IPC_CREAT | IPC_EXCL;
    if (clave==IPC_PRIVATE)  /*no nos vale*/
        {errno=EINVAL; return NULL;}
    if ((id=shmget(clave, tam, flags))==-1)
        return (NULL);
    if ((p=shmat(id,NULL,0))==(void*) -1){
        aux=errno;
        if (tam)
             shmctl(id,IPC_RMID,NULL);
        errno=aux;
        return (NULL);
    }
    shmctl (id,IPC_STAT,&s);
    item.dir_malloc=p;
    item.key=clave;
    strcpy(item.nome_ficheiro,"");
    item.tam=s.shm_segsz;
    item.tipo=SHARED;
    insertItemM(item,NULL,&LMem);

    return (p);
}



void sharedd(char *tokens[]){
    key_t cl;
    size_t tam;
    void *p;
    if (tokens[0]==NULL || tokens[1]==NULL) {
        printf("******Lista de bloques asignados shared para el proceso %d\n",getpid());
		imprimir_shared(LMem);
		return;
    }else{
        cl=(key_t)  strtoul(tokens[0],NULL,10);
        tam=0;
        if ((p=ObtenerMemoriaShmget(cl,tam))!=NULL)
		    printf ("Memoria compartida de clave %lu en %p\n",(unsigned long) cl, p);
        else
		    printf ("Imposible asignar memoria compartida clave %lu:%s\n",(unsigned long) cl,strerror(errno));
           
        }
}

void do_AllocateCreateshared (char *tr[])

{
   key_t cl;
   size_t tam;
   void *p;

   if (tr[0]==NULL || tr[1]==NULL) {
        printf("******Lista de bloques asignados shared para el proceso %d\n",getpid());
		imprimir_shared(LMem);
		return;
   }
  
   cl=(key_t)  strtoul(tr[0],NULL,10);
   tam=(size_t) strtoul(tr[1],NULL,10);
   if (tam==0) {
	printf ("No se asignan bloques de 0 bytes\n");
	return;
   }

   if ((p=ObtenerMemoriaShmget(cl,tam))!=NULL)
		printf ("Asignados %lu bytes en %p\n",(unsigned long) tam, p);
   else
		printf ("Imposible asignar memoria compartida clave %lu:%s\n",(unsigned long) cl,strerror(errno));
}

void * MapearFichero (char * fichero, int protection)
{
    
    int df, map=MAP_PRIVATE,modo=O_RDONLY;
    struct stat s;
    void *p;
    tItemM item;

    if (protection&PROT_WRITE)
          modo=O_RDWR;
    if (stat(fichero,&s)==-1 || (df=open(fichero, modo))==-1)
          return NULL;
    if ((p=mmap (NULL,s.st_size, protection,map,df,0))==MAP_FAILED)
           return NULL;
    item.dir_malloc=p;
    strcpy(item.nome_ficheiro,fichero);
    item.key=df;
    item.tam=s.st_size;
    item.tipo=MMAP;
    insertItemM(item,NULL,&LMem);
    return p;
}

void do_AllocateMmap(char *arg[])
{ 
     char *perm;
     void *p;
     int protection=0;
     
     if (arg[0]==NULL)
            {printf("******Lista de bloques asignados mmap para el proceso %d\n",getpid());
                imprimir_mmap(LMem); return;}      
     if ((perm=arg[1])!=NULL && strlen(perm)<4) {
            if (strchr(perm,'r')!=NULL) protection|=PROT_READ;
            if (strchr(perm,'w')!=NULL) protection|=PROT_WRITE;
            if (strchr(perm,'x')!=NULL) protection|=PROT_EXEC;
     }
     if ((p=MapearFichero(arg[0],protection))==NULL)
             perror ("Imposible mapear fichero");
     else
             printf ("fichero %s mapeado en %p\n", arg[0], p);
}



void FreeMalloc(char *tokens[]){
    tPosM p;
    tItemM items;
    size_t tam;
    if(tokens[0]==NULL){
        printf("******Lista de bloques asignados malloc para el proceso %d\n",getpid());
        imprimir_malloc(LMem);
    }else{
        tam=strtol(tokens[0],NULL,10);
        for(p=firstM(LMem);p!=NULL;p=nextM(p,LMem)){
            items=getItemM(p,LMem);
            if(items.tipo==MALLOC){
                if(items.tam==tam){
                    free(items.dir_malloc); //address is freed
                    deleteAtPositionM(p,&LMem); //item is deleted from the list
                    
                    }
               
            }
}
        
        
    }

}


void FreeShared(char *tokens[]){
    tItemM items;
    tPosM p;
    key_t key;

    if(tokens[0]==NULL){
        printf("******Lista de bloques asignados shared para el proceso %d\n",getpid());
        imprimir_shared(LMem);
    }else{
        key=(key_t) atoi(tokens[0]);
        p=firstM(LMem);
        for(p=firstM(LMem);p!=NULL;p=nextM(p,LMem)){
            items=getItemM(p,LMem);
            if(items.tipo==SHARED){
                if(items.key==key){
                   if(shmdt(items.dir_malloc)==0){
                        deleteAtPositionM(p,&LMem); 
                    }
                
                }
            }
}
    }

}

void FreeMmap(char *tokens[]){
    tItemM items;
    tPosM p;
    
    if(tokens[0]==NULL){
        printf("******Lista de bloques asignados mmap para el proceso %d\n",getpid());
        imprimir_mmap(LMem);

    }else{
        for(p=firstM(LMem);p!=NULL;p=nextM(p,LMem)){
            items=getItemM(p,LMem);
            if(items.tipo==MMAP){
                if(strcmp(items.nome_ficheiro,tokens[0])==0){
                    if(munmap(items.dir_malloc,items.tam)==-1){
                        perror("no se puede hacer el unmap");
                    }
                    deleteAtPositionM(p,&LMem);
                }
            }
        }
        
    }
}



int autores(char *tokens[]) {
    if(tokens[0]==NULL){
        printf("Lorena Godón Danza: lorena.godon\n");
        printf("Marta Vidal López: marta.vidal.lopez\n");
    }else{
        if(strcmp(tokens[0],"-l")==0){
            printf("lorena.godon\n");
            printf("marta.vidal.lopez\n");
        }else if(strcmp(tokens[0],"-n")==0){
            printf("Lorena Godón Danza\n");
            printf("Marta Vidal López\n");
        }else{
        perror("ERROR");
    }
    }
    return 0;
}

int pid(char *tokens[]){
    if(tokens[0]==NULL){
        printf("Pid del shell:%d\n",getpid());
    }else{
        if (strcmp(tokens[0],"-p")==0){
            printf("Pid del padre del shell:%d\n",getppid());
        }else{
            perror("Error");
        } 
    
    }
    return 0;
}

int carpeta(char *tokens[]){
    char dir[MAX_INPUT_SIZE];
    char *aux=tokens[0];
    getcwd(dir,MAX_INPUT_SIZE);

    if(tokens[0]==NULL){
        printf("%s\n",dir);
    }else{
       if(chdir(aux)==-1){
            perror("ERROR");
            
    }
    getcwd(dir,MAX_INPUT_SIZE);
        
    }
    return 0;
}

int fecha(char *tokens[]){
    time_t fecha;
    fecha=time(NULL);
    struct tm fecha_local = *localtime(&fecha);
    if(tokens[0]==NULL){
        printf("%d:%d:%d\n", fecha_local.tm_hour, fecha_local.tm_min, fecha_local.tm_sec);
        printf("%d/%d/%d\n", fecha_local.tm_mday,fecha_local.tm_mon+1,fecha_local.tm_year+1900);
        
    } else{
        if((strcmp(tokens[0],"-d")==0)){
            printf("%d/%d/%d\n", fecha_local.tm_mday,fecha_local.tm_mon+1,fecha_local.tm_year+1900);
        }else if((strcmp(tokens[0],"-h")==0)){
            printf("%d:%d:%d\n", fecha_local.tm_hour, fecha_local.tm_min, fecha_local.tm_sec);
        }else{
            perror("ERROR");
        }
    }
    return 0;

}

int hist(char *tokens[]){
    int n;
    tItemL d;
    tPosL p;
    if(tokens[0]==NULL){
        for(p=last(list);p!=NULL;p=previous(p,list)){
            d=getItem(p);
            printf("%d->%s\n",d.num,d.comm);
        }
    }else{
        if(strcmp(tokens[0],"-c")==0){
            removeList(&list);
        }else if(!(isNumber(tokens[0]))){
            printf("Error\n");
           
            }else{
                n=atoi(tokens[0]);
        p=last(list);
        d=getItem(p);
        if(n<0){
            perror("ERROR");
        }else{
            for(int cnt=0;cnt<n && isValid(p);cnt++){
                d=getItem(p);
               	p=previous(p,list);
                printf("%d->%s\n",d.num,d.comm);
              
            	
            }
        }
            }
        
    
    }
   return 0; 
}

int command(char *tokens[]){
    int n;
    tItemL d;
    tPosL p;
 
    if(!(isNumber(tokens[0]))){
        printf("error\n");
    }else{
        n=atoi(tokens[0]);
        p=last(list);
        d=getItem(p);
        if(n<0){
            perror("ERROR");
        }else{
            for(int cnt=0;cnt<n && isValid(p);cnt++){
                d=getItem(p);
               	p=previous(p,list);
              
                
               
                
        }
        if(isValid(p)){
        	 d=getItem(p);
        	printf("%d->%s\n",d.num,d.comm);
        }else{
        	printf("error\n");
        }
        
        
        }
        
    }
    return 0;
    
}

int infosis(char *tokens[]){
    struct utsname uts;
    if(uname(&uts)<0){
        perror("ERROR");

    }else{
        if(tokens[0]==NULL){
            printf("sysname: %s\n",uts.sysname);
            printf("Nodename: %s\n",uts.nodename);
            printf("Release: %s\n",uts.release);
            printf("Version: %s\n",uts.version);
            printf("Machine: %s\n",uts.machine);
        }else{
            perror("ERROR\n");
        }
        
    }
    return 0;
}

int create(char *tokens[]){
    //falla si queremos crear un fichero co mismo nombre que un directorio
    int fd;
    
    if(tokens[0]==NULL){ //imprime ruta
        carpeta(tokens);
    }else{
        if(strcmp(tokens[0],"-f")!=0){ //se crea directorio
            if(mkdir(tokens[0],0700)==-1){ //si no tenemos permiso
                perror("error");
            }
        }else if(strcmp(tokens[0],"-f")==0){ //se crea fichero
            fd=open(tokens[1],O_CREAT|O_EXCL ,0700); //creamos fichero
            if(fd==-1){
                perror("error");
            } else close(fd);
    }
    }
    
    return 0;
}

int delete(char *tokens[]){
 
    char dir[MAX_INPUT_SIZE];
    getcwd(dir,MAX_INPUT_SIZE);
    struct stat st;
    lstat(tokens[0],&st);
    if(tokens[0]==NULL){
        printf("%s\n",dir);
    }else{
    	if(EsDirectorio(tokens[0])){
    		if(rmdir(tokens[0])!=0){
            		perror("error");
        	}
    	
    	}else{
    		if(S_ISREG (st.st_mode) || S_ISLNK (st.st_mode)){
            		if(unlink(tokens[0])!=0){
                		perror("error");
                }
            }
    	}
        }
    
    return 0;
    
}

int deltree(char *tokens[]){
    char dir[MAX_INPUT_SIZE];
    getcwd(dir,MAX_INPUT_SIZE);
    struct stat st;

    if(tokens[0]==NULL){
         printf("%s\n",dir);
      
    }else{
            if(lstat(tokens[0],&st)==-1){
                perror( "error" );
            }else{
                if(EsDirectorio(tokens[0])){
                    if(strncmp(tokens[0],".\0",2)!=0 && strncmp(tokens[0],"..\0",3)!=0){
                        borrar_directorios(tokens[0]);
                    }
                }else{
                    if(unlink(tokens[0])!=0){
                        perror( "error" );
                    }
                }

                
            }
    }
    return 0;
}


int statt(char *tokens[]){
    //stat sale como nombre reservado
    int listar_largo=0, link=0, acc=0;
    int i=0;
    if(tokens[0]==NULL){
        carpeta(tokens);
    }else{
        while (tokens[i]!=NULL){
            if(strcmp(tokens[i],"-long")==0){
                listar_largo=1;
            } else if (strcmp(tokens[i],"-link")==0){
                link=1;
            } else if (strcmp(tokens[i],"-acc")==0){
                acc=1;
            } else if(strcmp(tokens[i],"-long")!=0 || strcmp(tokens[i],"-link")!=0 || strcmp(tokens[i],"-acc")!=0){
                imprimirInfoFile(listar_largo,link,acc,tokens[i]);
            }else{
                break;
            }   
             i++;             
        }
       
        
    }
    return 0;
    }
    
int listdir(char *tokens[]){
    int listar_largo=0, link=0, acc=0, hid=0;
    int i=0,rec=0;
    if(tokens[0]==NULL){
        carpeta(tokens);
    }else{
         while (tokens[i]!=NULL){
            if((strcmp(tokens[i],"-long")==0) && !listar_largo){
            listar_largo=1;
        } else if ((strcmp(tokens[i],"-link")==0) && link==0){
            link=1;
        } else if ((strcmp(tokens[i],"-acc")==0) && acc==0){
            acc=1;
        } else if ((strcmp(tokens[i],"-hid")==0) && hid==0){
            hid=1;
        } else if ((strcmp(tokens[i],"-reca")==0) && rec==0){
            rec=1;
        } else if ((strcmp(tokens[i],"-recb")==0) && rec==0){
            rec=2;
        } else if (strcmp(tokens[i],"-long")!=0 || strcmp(tokens[i],"-link")!=0 || strcmp(tokens[i],"-acc")!=0||strcmp(tokens[i],"-reca")!=0||strcmp(tokens[i],"-recb")!=0){
            if(!EsDirectorio(tokens[i])){
                imprimirInfoFile(listar_largo,link,acc,tokens[i]);
            }else{
                printDir(listar_largo, link, acc, hid, rec, tokens[i]);

            }
            
            
        }else{
            break;
        }
        i++;
    }
     

    
    }



    return 0;
}
       
int allocate(char *tokens[]){
    if(tokens[0]==NULL){
        printf("******Lista de bloques asignados para el proceso %d\n",getpid()); //se non nos introducen valor
        imprimir_malloc(LMem);
        imprimir_shared(LMem);
        imprimir_mmap(LMem);
    
    
    }else if(strcmp(tokens[0],"-malloc")==0){
        mallocc(tokens);
    }else if(strcmp(tokens[0],"-shared")==0){
        sharedd(tokens+1);
        
    }else if(strcmp(tokens[0],"-createshared")==0){
        do_AllocateCreateshared(tokens+1);
        
    }else if(strcmp(tokens[0],"-mmap")==0){
        do_AllocateMmap(tokens+1);
        
    }
        
    return 0;
}    

int dealloc(char *tokens[]){
    if(tokens[0]==NULL){
        printf("******Lista de bloques asignados para el proceso %d\n",getpid()); //se non nos introducen valor
        imprimir_malloc(LMem);
        imprimir_shared(LMem);
        imprimir_mmap(LMem);
    
    
    }else if(strcmp(tokens[0],"-malloc")==0){
        FreeMalloc(tokens+1);
        
    }else if(strcmp(tokens[0],"-shared")==0){
        FreeShared(tokens+1);
        
    }else if(strcmp(tokens[0],"-mmap")==0){
        FreeMmap(tokens+1);
          
} 
return 0; 
}


ssize_t LeerFichero (char *f, void *p, size_t cont)
{
   struct stat s;
   ssize_t  n;  
   int df,aux;

   if (stat (f,&s)==-1 || (df=open(f,O_RDONLY))==-1)
	return -1;     
   if (cont==-1)   /* si pasamos -1 como bytes a leer lo leemos entero*/
	cont=s.st_size;
   if ((n=read(df,p,cont))==-1){
	aux=errno;
	close(df);
	errno=aux;
	return -1;
   }
   close (df);
   return n;
}

ssize_t EscribirFichero (char *f, void *p, size_t cont,int overwrite)
{
   ssize_t  n;
   int df,aux, flags=O_CREAT | O_EXCL | O_WRONLY;

   if (overwrite)
	flags=O_CREAT | O_WRONLY | O_TRUNC;

   if ((df=open(f,flags,0777))==-1)
	return -1;

   if ((n=write(df,p,cont))==-1){
	aux=errno;
	close(df);
	errno=aux;
	return -1;
   }
   close (df);
   return n;
}

void do_I_O_read (char *ar[])
{
   void *p;
   size_t cont=-1;
   ssize_t n;
   if (ar[0]==NULL || ar[1]==NULL){
	printf ("faltan parametros\n");
	return;
   }
   if (ar[2]!=NULL)
	cont=(size_t) atoll(ar[2]);
    p=(void *)strtol(ar[1],NULL,16);

   if ((n=LeerFichero(ar[0],p,cont))==-1)
	perror ("Imposible leer fichero");
   else
	printf ("leidos %lld bytes de %s en %p\n",(long long) n,ar[0],p);
}

void do_I_O_write (char *ar[],int overwrite)
{
   void *p;
   size_t cont=-1;
   ssize_t n;
   if (ar[0]==NULL || ar[1]==NULL){
	printf ("faltan parametros\n");
	return;
   }
	cont=(size_t) atoll(ar[2]);
    p=(void *)strtol(ar[1],NULL,16);

   if ((n=EscribirFichero(ar[0],p,cont,overwrite))==-1)
	perror ("Imposible leer fichero");
   else
	printf ("Escritos %lld bytes de %s en %p\n",(long long) n,ar[0],p);
}

int io (char *tokens[]){
    if(tokens[0]==NULL){
        printf("uso: e-s [read|write] ......\n");
    }else if(strcmp(tokens[0],"read")==0){
        do_I_O_read(tokens+1);
    }else if(strcmp(tokens[0],"write")==0){
        if(strcmp(tokens[1],"-o")==0){
            do_I_O_write(tokens+2,1);
        }else{
            do_I_O_write(tokens+1,0);
        }
    }
    return 0;
}


void print_byte(void *ptr, int n){
    unsigned char *char_ptr = (unsigned char *) ptr;
    int i, j, z;


    for(z = 0; z < n; z += 25) {
        for (i = 0; i < 25 && i + z < n; i++) {
            if(char_ptr[z+i] == 10)
                printf("\\n ");
            else if ((char_ptr[z+i]<32 || char_ptr[z+i]>126)){
                 printf("   ");
            }else printf(" %c ", char_ptr[z + i]);
        }
                
        printf("\n");
        for (j = 0; j < 25 && j + z < n; j++) {
            printf("%02x ", char_ptr[z +j]);
        }
        printf("\n");
    }


} 

int volcarmem(char *tokens[]){
    void *addr;
    int cont = 25;

    if(tokens[0] == NULL){
        printf("Direccion no especificada\n");
        return 0;
    }else{
        addr = (void*) strtol(tokens[0], NULL, 16);
        if(tokens[1] != NULL){
            cont = atoi (tokens[1]);
        }
        printf("Volcando %d bytes desde la direccion %p\n", cont, addr);
        print_byte(addr, cont);
    } 
    return 0;
    
    
}


void memfill_aux(void *ptr, size_t cnt, unsigned char caracter){
    size_t j;
    unsigned char *address = (unsigned char*) ptr;
    

    for(j=0; j<cnt; j++){
        address[j] = caracter;
    }     
}

int memfill (char *tokens[]){
    
    unsigned char caracter;
    size_t cnt;

    void * ptr;

    if(tokens[0]==NULL){
        printf("Direccion de memoria no especificada\n");
        return 0;
    }else{
        ptr= (void*) strtol(tokens[0], NULL, 16);

        if(tokens[1]==NULL){
            cnt=128;
        }else{
             cnt = atoi(tokens[1]);
        }

        if(tokens[1]!=NULL  &&  tokens[2]!=NULL){
            if (tokens[2][0]=='\'' && tokens[2][2]=='\''){
                caracter = (unsigned char) tokens[2][1];
            }else if (tokens[2][0]=='0' && tokens[2][1]=='x'){
                caracter = (unsigned char) strtol(tokens[2], NULL, 16);
            }else{
                caracter = (unsigned char) strtol(tokens[2], NULL, 10);
            }
        }else caracter = (unsigned char) 65;
    }

    memfill_aux(ptr, cnt, caracter);


    printf("LLenando %ld bytes de memoria con el byte ",cnt);
    if(caracter==10){
        printf("\\n");
    }else printf("%c", caracter);
    printf("(%02x) a partir de la direccion %p\n", caracter, ptr); 

    return 0;

    
}


int memory(char *tokens[]){
    static int st1,st2,st3;
    int loc1,loc2,loc3;

    if(tokens[0]==NULL||strcmp(tokens[0],"-all")==0){
        printf("Locales: %p %p %p\n",&loc1,&loc2,&loc3);
        printf("Globales: %p %p %p\n",&global1,&global2,&global3);
        printf("Estáticas: %p %p %p\n",&st1,&st2,&st3);
        printf("Funciones programa: %p %p %p\n",autores,pid,fecha); 
        printf("Funciones librería: %p %p %p\n",printf,strcmp,strtol); 
        
    }else if(strcmp(tokens[0],"-funcs")==0){
        printf("Funciones programa: %p %p %p\n",autores,pid,fecha); 
        printf("Funciones librería: %p %p %p\n",printf,strcmp,strtol); 
    }else if(strcmp(tokens[0],"-blocks")==0){
        printf("******Lista de bloques asignados shared para el proceso %d\n",getpid());
        imprimir_malloc(LMem);
        imprimir_shared(LMem);
        imprimir_mmap(LMem);
    }else if(strcmp(tokens[0],"vars")==0){
        printf("Locales: %p %p %p\n",&loc1,&loc2,&loc3);
        printf("Globales: %p %p %p\n",&global1,&global2,&global3);
        printf("Estáticas: %p %p %p\n",&st1,&st2,&st3);
    }else if(strcmp(tokens[0],"-all")!=0||strcmp(tokens[0],"-funcs")!=0||strcmp(tokens[0],"-blocks")!=0 || strcmp(tokens[0],"vars")!=0){
        printf("Opcion %s no contemplada\n",tokens[0]);
    }
    return 0;
}

void Recursiva (int n)
{
  char automatico[TAMANO];
  static char estatico[TAMANO];

  printf ("parametro:%3d(%p) array %p, arr estatico %p\n",n,&n,automatico, estatico);

  if (n>0)
    Recursiva(n-1);
}


int recurse(char *tokens[]){
    
    if(tokens[0]!=NULL){
        int n=atoi(tokens[0]);
        Recursiva(n);
    }
    return 0;
}

int priority (char *tokens[]){
    pid_t pid;
    int prio;
    if(tokens[0]==NULL){
        pid=getpid();
        if((prio=getpriority(PRIO_PROCESS,pid))==-1){
            perror("error");
        }else{
            printf("Prioridad del proceso %d es %d\n",pid, prio);
        }
    }else if(tokens[1]==NULL){
        pid=strtol(tokens[0],NULL,10);
        if((prio=getpriority(PRIO_PROCESS,pid))==-1){
            perror("error");
        }else{
            printf("Prioridad del proceso %d es %d\n", pid, prio);
        }
    }else{
        //convertimos a decimal pid e prioridad
        pid=strtol(tokens[0],NULL,10);
        prio=strtol(tokens[1],NULL,10);
        if(setpriority(PRIO_PROCESS,pid,prio)==-1){
            perror("error");
        }
    }
    return 0;
}

int BuscarVariable (char * var, char *e[])  /*busca una variable en el entorno que se le pasa como parámetro*/
{
  int pos=0;
  char aux[MAXVAR];
  
  strcpy (aux,var);
  strcat (aux,"=");
  
  while (e[pos]!=NULL)
    if (!strncmp(e[pos],aux,strlen(aux)))
      return (pos);
    else 
      pos++;
  errno=ENOENT;   /*no hay tal variable*/
  return(-1);
}


void showEntorno(char *nombre,char **env){
    for(int i=0;env[i]!=NULL;i++){
        printf ("%p->%s[%d]=(%p) %s\n", &env[i],nombre, i,env[i],env[i]);
    }
}


int showenv(char *tokens[]){
     if(tokens[0]==NULL){
        showEntorno("main arg3",arg3env);
     }else if(strcmp(tokens[0],"-environ")==0){
        showEntorno("environ",environ);
     }else if(strcmp(tokens[0],"-addr")==0){
        printf("environ:\t%p (almacenado en %p)\n",environ,&environ);
        printf("main arg3:\t%p (almacenado en %p)\n",arg3env,&arg3env);
     }
     return 0; 
}


int showvar(char *tokens[]){
    int pos;
    char *cadena;
    if(tokens[0]==NULL){
        showEntorno("main arg3",arg3env);
    }else{
        if((pos=BuscarVariable(tokens[0],arg3env))==-1){
        }else{
           printf("Con arg3 main: %s(%p) @%p\n",arg3env[pos],arg3env[pos], &arg3env[pos]);
        }
        if((pos=BuscarVariable (tokens[0],environ))==-1){
            perror("error");
        }else{
            printf("Con environ: %s(%p) @%p\n", environ[pos], environ[pos], &environ);
        }
        if((cadena=getenv(tokens[0]))==NULL){
            perror("error");
        }else{
            printf("Con getenv: %s(%p)\n",cadena,cadena);
        }


    }
return 0;
}

int forkk(char *tokens[]){
    pid_t pid;
    if((pid=fork()==0)){
        printf("ejecutando proceso %d\n",getpid());
    }else if (pid!=-1){
        waitpid(pid,NULL,0);
    }

    
    return 0;
}


int CambiarVariable(char * var, char * valor, char *e[]) /*cambia una variable en el entorno que se le pasa como parámetro*/
{                                                        /*lo hace directamente, no usa putenv*/
  int pos;
  char *aux;
   
  if ((pos=BuscarVariable(var,e))==-1)
    return(-1);
 
  if ((aux=(char *)malloc(strlen(var)+strlen(valor)+2))==NULL)
	return -1;
  strcpy(aux,var);
  strcat(aux,"=");
  strcat(aux,valor);
  e[pos]=aux;
  return (pos);
}

int changevar(char *tokens[]){
    char cadena [8192];

    if (tokens[0]==NULL||tokens[1]==NULL||tokens[2]==NULL){
       printf("Uso: cambiarvar [-a|-e|-p] var valor\n"); 
    }else{
        if((strcmp(tokens[0],"-p")!=0)|| (strcmp(tokens[0],"-a")!=0) || (strcmp(tokens[0],"-e")!=0)){
            printf("Uso: cambiarvar [-a|-e|-p] var valor\n"); 
        }else{
            sprintf(cadena,"%s=%s",tokens[1],tokens[2]);
            if(strcmp(tokens[0],"-a")==0){
                if(CambiarVariable(tokens[1],tokens[2],arg3env)==-1){
                perror("error");
                }
            }else if(strcmp(tokens[0],"-e")==0){
                if(CambiarVariable(tokens[1],tokens[2],environ)==-1){
                perror("error");
                }
            }else if(strcmp(tokens[0],"-p")==0){
                if(putenv(cadena)!=0){
                    perror("error");
                }
            }
        }
        
    }
    return 0;
}

void ejecAux(char *tokens[],int prio,int valor) {
    if (prio){
        if  ((setpriority(PRIO_PROCESS,getpid(),valor))==-1){
            perror("error");
        }
    }    

    if (execvp(tokens[0],tokens)==-1){ 
        perror ("error");
    }
}

int execute (char *tokens[]){
    int i;
    char aux1[TAM];
    for(i=0;tokens[i+1]!=NULL;i++);
    strcpy(aux1,tokens[i]);
    //comprobamos si ten @
    if(strchr(aux1,'@')!=NULL){
        int prio = (int) strtol(aux1+1,NULL,10);
        tokens[i]=NULL; //@ a nulo
        ejecAux(tokens,1,prio); 
        
    }else{
        ejecAux(tokens,0,0);
    }
    

    return 0;
}

char * username (uid_t uid)
{
    struct passwd *p;
    if ((p=getpwuid(uid))==NULL)
        return ("????????");
    return p->pw_name;
}



void actualizar_list() {
    tItemP item;
    tPosP pos;
    int id, estado;
    if(isEmptyListP(Lproc)){
        return;
    }else{
        pos=firstP(Lproc);
    while (pos != NULL) {
        item=getItemP(pos,Lproc);
        if (item.estado != TERMINADO) {item.prioridad = getpriority(PRIO_PROCESS, item.pid);}
        id = waitpid(item.pid, &estado, WNOHANG | WUNTRACED | WCONTINUED);
        if (id == item.pid) {
            if (WIFEXITED(estado)) {
                item.estado = TERMINADO;
                item.senal = WEXITSTATUS(estado);
               
            } else if (WIFSIGNALED(estado)) {
                item.estado = SIGNAL;
                item.senal = WTERMSIG(estado);
              
            } else if (WIFSTOPPED(estado)) {
                item.estado = PARADO;
                item.senal = WSTOPSIG(estado);
            } else if (WIFCONTINUED(estado)) {
                item.estado = ACTIVO;
                item.senal = 0;

            }
        }
        updateItemP(item,pos,&Lproc);
        pos=nextP(pos,Lproc);
    }
    
    }
    
}



void listar_p(){
    tItemP item;
    tPosP pos;
    if(isEmptyListP(Lproc)){
        return ;
    }else{
        pos=firstP(Lproc);
        while (pos != NULL) {
        item=getItemP(pos,Lproc);
            printf("%d \t%s p=%d\t",item.pid,username(item.user),getpriority(PRIO_PROCESS,item.pid));
            tiempo(item.hora,3);
            if(item.estado==TERMINADO){
                printf("TERMINADO ");
            }else if(item.estado==PARADO){
                printf("PARADO ");
            }else if(item.estado==SIGNAL){
                printf("SIGNAL ");
            }else{
                printf("ACTIVO ");
            }
            printf("(%03d) %s\n",item.senal, item.comando);
            
            pos=nextP(pos,Lproc);
    }
    }
    
}

int listjobs(char *tokens[]){
    actualizar_list();
    listar_p();
    return 0;
}

int deleteJobs(int term, int sig){

    tPosP pos;
    tItemP item;

    if(isEmptyListP(Lproc)){
        return 0;
    }else{
        pos = firstP(Lproc);
        while (nextP(pos,Lproc)!=NULL){
            pos=nextP(pos,Lproc);
            item = getItemP(pos,Lproc);
            if ((term && item.estado==TERMINADO) || (sig && item.estado==SIGNAL)){
                pos=nextP(pos,Lproc);
                deleteAtPositionP(pos,&Lproc);
            } else pos = nextP(pos,Lproc);
        }

    pos = firstP(Lproc);
    item = getItemP(pos,Lproc);
    if ((term && item.estado==TERMINADO) || (sig && item.estado==SIGNAL)){
        deleteAtPositionP(pos,&Lproc);
    }
    }
        
    
    return 0;

}

int deljobs(char *tokens[]){
    if(tokens[0]==NULL){
        listjobs(tokens);
    }else if(strcmp(tokens[0],"-term")==0){
        deleteJobs(1,0);
    }else if(strcmp(tokens[0],"-sig")==0){
        deleteJobs(0,1);
    }
    return 0;
}

tPosP findjob(pid_t pid){

    tPosP pos;
    tItemP item;

    pos= firstP(Lproc);

    while (pos!=NULL){
        item= getItemP(pos,Lproc);

        if(item.pid==pid) return pos;

        pos= nextP(pos,Lproc);
    }

    return NULL;
}



void primPlano(pid_t pid){
    int estado, senal;
    if (waitpid (pid,&estado,0)==pid){
        if (WIFEXITED(estado)){
            senal = WEXITSTATUS(estado);
            printf("Proceso %d terminado normalmente. Valor devuelto %d\n",pid,senal);
        }
    }

}


int job(char *tokens[]){
    pid_t pid;
    tPosP pos;
    if(tokens[0]==NULL){
        listjobs(tokens); 
    }else if(strcmp(tokens[0],"-fg")==0 && tokens[1]!=NULL){ //si meten la opcion -fg y un numero
        pid = (int) strtol(tokens[1],NULL,10);
        if((pos=findjob(pid))!=NULL){
            primPlano(pid);
            deleteAtPositionP(pos,&Lproc);
        }
    }else if(strcmp(tokens[0],"-fg")==0 && tokens[1]==NULL){
        listjobs(tokens);
    }else{
  
        pid = (int) strtol(tokens[0],NULL,10);
        if ((pos = findjob(pid))!=NULL){
            listjobs(tokens);
        }else{
            listjobs(tokens);
        }
            
    }
    return 0;
}

struct cmd {
    char *cmd_name;
    int (*cmd_fun)(char *tokens[]);
    char *cmd_help;
} cmds [] = {
    {"autores", autores, "\nImrpime los nombres y correos de los autores del programa.\n autores -l imprime solo los correos.\n autores -n imprime solo los nombres\n"},
    {"pid",pid, "\nImprime el pid del proceso en ejecucion del shell.\npid -p imprime el pid del padre del proceso\n"},
    {"carpeta",carpeta, "\nImprime la carpeta del directorio donde esra ubicado el shell.\nCambia de la carpeta actual a la carpeta invodada\n"},
    {"fecha",fecha, "\nImprime la fecha y la hora\nfehca -d imprime la fehca actual en formato DD/MM/YYYY.\nfecha -h imprime la hora actual del sistema en formato hh:mm:ss.\n"},
    {"hist", hist, "Muestra/borra el historial de comandos ejecutados por el shell con su numero en el orden.\nhist -c limpia  (vacía) el historial.\nhist -N imprime los primeros N comandos ejecutados.\n"},
    {"infosis",infosis, "\nImprime la informacion de la maquina en la cual se está ejecutando el shell.\n"},
    {"command",command, "\nRepite el comando numero N del historial\n"},
    {"fin",fin, "\nTermina la ejecución del shell\n"},
    {"salir",fin, "\nTermina la ejecución del shell\n"},
    {"bye",fin, "\nTermina la ejecución del shell\n"},
    {"ayuda",ayuda, "\nImprime la lista de comandos válidos.\nayuda cmd proporciona informacion sobre el comando introducido"},
    {"create",create,"\ncreate [-f] [name]	Crea un directorio o un fichero (-f)"},
    {"delete",delete,"\nDelete [name1 name2 ..]	Borra ficheros o directorios vacios"},
    {"deltree",deltree,"\n Deltree [name1 name2 ..]	Borra ficheros o directorios no vacios recursivamente"},
    {"stat",statt,"\nstat [-long][-link][-acc] name1 name2 ..	lista ficheros  \n-long: listado largo \n-acc: acesstime \n-link: si es enlace simbolico, el path contenido\n"},
    {"list",listdir," \nlist [-reca] [-recb] [-hid][-long][-link][-acc] n1 n2 ..	lista contenidos de directorios \n-hid: incluye los ficheros ocultos \n-reca: recursivo (antes) \n-recb: recursivo (despues) \tresto parametros como stat\n"},
    {"allocate",allocate,"\n allocate [-malloc|-shared|-createshared|-mmap]... Asigna un bloque de memoria\n-malloc tam: asigna un bloque malloc de tamano tam\n-createshared cl tam: asigna (creando) el bloque de memoria compartida de clave cl y tamano tam\n-shared cl: asigna el bloque de memoria compartida (ya existente) de clave cl\n-mmap fich perm: mapea el fichero fich, perm son los permisos\n"},
    {"dealloc",dealloc ,"dealloc [-malloc|-shared|-delkey|-mmap|addr]..	Desasigna un bloque de memoria \n-malloc tam: desasigna el bloque malloc de tamano tam\n-shared cl: desasigna (desmapea) el bloque de memoria compartida de clave cl\n-delkey cl: elimina del sistema (sin desmapear) la clave de memoria cl\n-mmap fich: desmapea el fichero mapeado fich\naddr: desasigna el bloque de memoria en la direccion addr\n"},
    {"i-o",io,"\ni-o [read|write] [-o] fiche addr cont \nread fich addr cont: Lee cont bytes desde fich a addr\nwrite [-o] fich addr cont: Escribe cont bytes desde addr a fich. -o para sobreescribir\naddr es una direccion de memoria\n"},
    {"memory",memory,"\nmemory [-blocks|-funcs|-vars|-all|-pmap] ..	Muestra muestra detalles de la memoria del proceso\n-blocks: los bloques de memoria asignados\n-funcs: las direcciones de las funciones\n-vars: las direcciones de las variables\n:-all: todo\n-pmap: muestra la salida del comando pmap(o similar)\n"},
    {"recurse",recurse,"\nrecurse [n]	Invoca a la funcion recursiva n veces\n"},
    {"memfill",memfill,"\nmemfill addr cont byte 	Llena la memoria a partir de addr con byte\n"},
    {"fillmem",memfill,"\nfillmem addr cont byte 	Llena la memoria a partir de addr con byte\n"},
    {"llenarmem",memfill,"\nllenarmem addr cont byte 	Llena la memoria a partir de addr con byte\n"},
    {"volcarmem",volcarmem,"\n volcarmem addr cont 	Vuelca en pantallas los contenidos (cont bytes) de la posicion de memoria addr\n"},
    {"memdump",volcarmem ,"\nmemdump addr cont 	Vuelca en pantallas los contenidos (cont bytes) de la posicion de memoria addr\n"},
    {"priority",priority},
    {"showvar",showvar},
    {"changevar",changevar},
    {"fork",forkk},
    {"showenv",showenv},
    {"execute",execute},
    {"listjobs",listjobs},
    {"job",job},
    {"deljobs",deljobs},
    {NULL, NULL},
};



int ayuda(char *tokens[]){
 
    if(tokens[0]==NULL){
        printf("LISTA DE COMANDOS:\n");
        for(int i=0; cmds[i].cmd_name != NULL; i++) {
            printf("%s\n",cmds[i].cmd_name);  
        }
    }else{
        for(int i=0; cmds[i].cmd_name != NULL; i++) {
                if(strcmp(tokens[0], cmds[i].cmd_name) == 0){
                    printf("%s",cmds[i].cmd_help);
                }   
        }
    }
    return 0;
}






void fgAux(char *tokens[],int prio,int valor)  {

    pid_t pid;
    if ((pid=fork())==0){
        if (execvp(tokens[0],tokens)==-1)
            perror ("error");
    }
    waitpid (pid,NULL,0);
}

void backAux(char *tokens[]) {
    tItemP item;
    int pid;

    if ((pid=fork())==0){
        if (execvp(tokens[0],tokens)==-1){
            perror ("error");
            exit(0);
        }


    } else{
        item.user=getuid();
        item.pid=pid;
        item.hora=time(NULL);
        strcpy(item.comando,tokens[0]);
        item.estado=ACTIVO;
        insertItemP(item,NULL,&Lproc);
    }


}

void ejecutarComando(char *tokens[]){
    int i;
    //recorremos tokens hasta o final 
    for (i = 0; tokens[i+1]!=NULL ; i++);

    if(!strcmp(tokens[i],"&")){//ejecucion en segundo plano
        tokens[i]=NULL; //eliminamos o &
        backAux(tokens);
    }else
        fgAux(tokens,0,0);
}

int process_input(char *tokens[]) {
    int i;
    int encontrado=0;
    for(i=0; cmds[i].cmd_name != NULL; i++) {
        if(strcmp(tokens[0], cmds[i].cmd_name) == 0) {
            return (cmds[i].cmd_fun(tokens+1));
            encontrado=1;
        }
    }
    //si o input non e ningun comando dos da lista
    if(encontrado==0){
        ejecutarComando(tokens);
    }
    return 0;
}

int main(int argc,char *argv[],char **env) {
    char input[MAX_INPUT_SIZE];
    char *tokens[MAX_TOKENS];
    char d[MAX_INPUT_SIZE];
    int finish=0;
    arg3env=env;
    createEmptyList(&list);

    while(!finish) {
        printf("> ");
        fgets(input, MAX_INPUT_SIZE, stdin);
        strcpy(d,input);
        insertItem(d,&list);
        split_string(input, tokens);
        finish=process_input(tokens);
        
    }
}
