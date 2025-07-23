#include <stdlib.h>
#include "list.h"


void createEmptyList(tList *L) {
    *L=NULL;
}

bool isEmptyList(tList L){
    return (L==NULL);
}

tPosL first(tList L){
    return L;
}
tPosL last(tList L){
    tPosL p;
    for(p=L;p->next!=NULL;p=p->next);
    return p;
}

tPosL next(tPosL p,tList L){

    return p->next;
}

tPosL previous(tPosL p,tList L){
    tPosL q;
    if(p==L)
        return NULL;
    else{
        for(q=L;q->next!=p;q=q->next);
        return q;
    }
}

bool createNode(tPosL *p){
    
    *p=malloc(sizeof(struct tNode));
    return *p!=NULL;
}

bool insertItem(char *d,tList *L){
    tPosL p;
    int num;
    if(!createNode(&p)){
        return false;
    }else{
        strcpy(p->data.comm,d);
        p->next=NULL;
        if(isEmptyList(*L)){
            p->data.num=0;
            *L=p;
        }else{
            num=first(*L)->data.num;
            p->data.num=(num+1);
            p->next=*L;
            *L=p;
        }
    }
    return true;
    

}

void removeList(tList *L){
    tPosL p;
    while(!(isEmptyList(*L))){
        p=*L;
        *L=(*L)->next;
        free(p);
    }
}


tItemL getItem(tPosL p){
    return p->data;
}


bool isValid(tPosL p){
    if(p!=NULL){
        return true;
    }else{
        return false;
    }
}


