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

struct args {
    int id;
    int iterations;
    int delay;
    mtx_t *mutex;
    struct array *array;
    int *counter;
    mtx_t *counter_mutex;
};

struct thread_info {
    thrd_t thread;
    struct args args;
};

void apply_delay(int delay) {
    for (int i = 0; i < delay * DELAY_SCALE; i++); // waste time
}
int aumentar_una(int * iter,mtx_t * mutex) {
  int temp;
  mtx_lock(mutex);
  temp =(*iter)++;
  mtx_unlock(mutex);
  return temp;
}
void *increment(void *parametros) {
    int pos, val;
    struct args *args = parametros;
    while (aumentar_una(args->counter,args->counter_mutex) < args->iterations) {
        pos = rand() % args->array->size;
        mtx_lock(&args->mutex[pos]);
        printf("%d increasing position %d\n", args->id, pos);
        val = args->array->arr[pos];
        apply_delay(args->delay);
        val++;
        apply_delay(args->delay);
        args->array->arr[pos] = val;
        mtx_unlock(&args->mutex[pos]);
        apply_delay(args->delay);
    
    }

    return 0;
}

void *transfer(void *parametros) {
    int pos1, pos2, val1, val2;
    struct args *args = parametros;
    for(int i = 0; i < args->iterations; i++) {
        pos1 = rand() % args->array->size;
        pos2 = rand() % args->array->size;
        while (pos2 == pos1) {
            pos2 = rand() % args->array->size;
        }
        if (pos1 > pos2) { // para que sea mayor que n
            mtx_lock(&args->mutex[pos2]);
            mtx_lock(&args->mutex[pos1]);
        }
        else {
            mtx_lock(&args->mutex[pos1]);
            mtx_lock(&args->mutex[pos2]);
        }
        printf("%d transferring from %d to %d\n", args->id, pos1, pos2);
        val1 = args->array->arr[pos1];
        apply_delay(args->delay);
        val2 = args->array->arr[pos2];
        apply_delay(args->delay);
        val1--;
        apply_delay(args->delay);
        val2++;
        args->array->arr[pos1] = val1;
        args->array->arr[pos2] = val2;
        mtx_unlock(&args->mutex[pos1]);
        mtx_unlock(&args->mutex[pos2]);
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
    mtx_t *mutex;
    srand(time(NULL));
    // Default values for the options
    opt.num_threads  = 5;
    opt.size         = 10;
    opt.iterations   = 100;
    opt.delay        = 1000;
    opt.delay        = 1000;
    read_options(argc, argv, &opt);
    arr.size = opt.size;
    arr.arr  = malloc(arr.size * sizeof(int));
    memset(arr.arr, 0, arr.size * sizeof(int));
    mutex = malloc(arr.size * sizeof(mtx_t));
    for (int i = 0; i < arr.size; i++) {
        mtx_init(&mutex[i], mtx_plain);
    }
    threads = (struct thread_info*)malloc(2*opt.num_threads * sizeof(struct thread_info));
    int counter = 0;
    mtx_t counter_mutex;
    mtx_init(&counter_mutex, mtx_plain);
    int counter2 = 0;
    mtx_t counter_mutex2;
    mtx_init(&counter_mutex2, mtx_plain);

    for (int i = 0; i < opt.num_threads; i++) {
        threads[2 * i].args = (struct args) {
            i, opt.iterations, opt.delay, mutex, &arr, &counter, &counter_mutex
        };
        threads[2 * i + 1].args = (struct args) {
            i, opt.iterations, opt.delay, mutex, &arr, &counter2, &counter_mutex2
        };

        thrd_create(&threads[2 * i].thread, (void*) increment, &threads[2 * i].args);
        thrd_create(&threads[2 * i + 1].thread, (void*) transfer, &threads[2 * i + 1].args);
    }

    // Wait for all threads to finish
    for (int i = 0; i < 2 * opt.num_threads; i++) {
        thrd_join(threads[i].thread, NULL);
    }
    print_array(arr);

    for (int i = 0; i < arr.size; i++) {
        mtx_destroy(&mutex[i]);
    }
    free(arr.arr);
    free(threads);
    mtx_destroy(&counter_mutex);
    return 0;
}