#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include "options.h"
#include <threads.h>


#define DELAY_SCALE 1000


struct array {
    int size;
    int *arr;
    
    
};


struct args{
    int id;
    int iterations;
    int delay;
    mtx_t *mutex;
    struct array *array;
};

struct thread_info{
    thrd_t thread;
    struct args args;

};

void apply_delay(int delay) {
    for(int i = 0; i < delay * DELAY_SCALE; i++); // waste time
}


void *increment(void *parametros)
{
    int pos, val;
    struct args *args=parametros;

    for(int i = 0; i < args->iterations; i++) {
        pos = rand() % args->array->size;
        mtx_lock(args->mutex);
        printf("%d increasing position %d\n", args->id,pos);
        val = args->array->arr[pos];
        apply_delay(args->delay);
        val ++;
        apply_delay(args->delay);
        args->array->arr[pos] = val;
        mtx_unlock(args->mutex);
        apply_delay(args->delay);
    }

    return 0;
}


void print_array(struct array arr) {
    int total = 0;
    for(int i = 0; i < arr.size; i++) {
        total += arr.arr[i];
        printf("%d ", arr.arr[i]);
    }

    printf("\nTotal: %d\n", total);
}


int main (int argc, char **argv)
{
    struct options       opt;
    struct array         arr;
    struct thread_info *threads;
    mtx_t mutex;

    srand(time(NULL));

    // Default values for the options
    opt.num_threads  = 5;
    opt.size         = 10;
    opt.iterations   = 100;
    opt.delay        = 1000;

    read_options(argc, argv, &opt);

    arr.size = opt.size;
    arr.arr  = malloc(arr.size * sizeof(int));

    memset(arr.arr, 0, arr.size * sizeof(int));
    mtx_init(&mutex, mtx_plain);
    threads = (struct thread_info*)malloc(opt.num_threads * sizeof(struct thread_info));

//inicializar
    for (int i=0;i<opt.num_threads;i++){
        threads[i].args=(struct args){
            i,opt.iterations,opt.delay,&mutex,&arr
        };
        thrd_create(&threads[i].thread,(void*)increment,&threads[i].args);
    }
    // Wait for all threads to finish
    for (int i = 0; i < opt.num_threads; i++) {
        thrd_join(threads[i].thread, NULL);
    }


    print_array(arr);
    mtx_destroy(&mutex);
    free(arr.arr);
    free(threads);


    return 0;
}
