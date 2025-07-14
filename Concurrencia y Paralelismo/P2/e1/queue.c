#include <stdlib.h>
#include <threads.h>
#include <stdbool.h>

// circular array
typedef struct _queue {
    int size;
    int used;
    int first;
    void **data;
    mtx_t lock;
    cnd_t not_full;
    cnd_t not_empty;
    bool no_more_files;
    
} _queue;

#include "queue.h"


queue q_create(int size) {
    queue q = malloc(sizeof(_queue));

    q->size  = size;
    q->used  = 0;
    q->first = 0;
    q->data  = malloc(size * sizeof(void *));
    q->no_more_files=false;
    
    mtx_init(&q->lock, mtx_plain);
    cnd_init(&q->not_full);
    cnd_init(&q->not_empty);

    return q;
}
void q_set_finished(queue q) {
    mtx_lock(&q->lock);
    q->no_more_files = true;
    cnd_broadcast(&q->not_empty);
    mtx_unlock(&q->lock);
}


int q_elements(queue q) {
    mtx_lock(&q->lock);
    int used = q->used;
    mtx_unlock(&q->lock);
    return used;
}

int q_insert(queue q, void *elem) {
    mtx_lock(&q->lock);
    while(q->used == q->size) {
        cnd_wait(&q->not_full, &q->lock);
    }

    q->data[(q->first + q->used) % q->size] = elem;
    q->used++;
    if(q->used==1){
        cnd_broadcast(&q->not_empty);
    }
    
    mtx_unlock(&q->lock);

    return 0;
}

void *q_remove(queue q) {
    mtx_lock(&q->lock);
    while(q->used == 0 && q->no_more_files == false) {
        cnd_wait(&q->not_empty, &q->lock);
    }

    if(q->used == 0 && q->no_more_files == false) {
        mtx_unlock(&q->lock);
        return NULL;
    }

    void *res = q->data[q->first];

    q->first = (q->first + 1) % q->size;
    q->used--;
    if(q->used == q->size - 1) {
        cnd_broadcast(&q->not_full);
    }
    
    mtx_unlock(&q->lock);

    return res;
}

void q_destroy(queue q) {
    free(q->data);
    mtx_destroy(&q->lock);
    cnd_destroy(&q->not_full);
    cnd_destroy(&q->not_empty);
    free(q);
}