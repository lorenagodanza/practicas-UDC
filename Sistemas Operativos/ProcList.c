#include "ProcList.h"

void createEmptyListP(tListP *L){
    *L= LNULL;
}
bool createNodeP(tPosP *p){
    *p= malloc(sizeof(struct tNodeP));
    return *p != NULL;
}
bool insertItemP(tItemP d, tPosP p, tListP* L){

    tPosP q,r;
    if(!createNodeP(&q)){
        return false;//si no hay memoria suficiente
    }else{
        q->data = d;
        q->next =LNULL;
        if(*L == LNULL){
            *L =q;
        }else if(p==LNULL){//insertar al final 
            for(r=*L; r->next!= LNULL; r=r->next);
            r->next = q;
        }else if(p==*L){ //insertar al principio
            q->next =p;
            *L = q;
        }else{
            q->data =p->data;
            p->data = d;
            q->next = p->next;
            p->next = q;
        }
        return true;
    }
}

bool isEmptyListP(tListP L){
    return(L==LNULL);
}
tItemP getItemP(tPosP p, tListP L){
    return p->data;
}
tPosP firstP(tListP L){
    return L;
}
tPosP lastP(tListP L){
    tPosP p;
    for(p=L;p->next != LNULL;p = p->next);
    return p;

}
tPosP previousP(tPosP p, tListP L){
    tPosP q;
    if(p==L) { //caso de la 1era posición
        return LNULL;
    }else{
        //se recorre la lista con la variable "p"
        for(q=L;q->next!=p;q=q->next);
        return q;
    }
}
tPosP nextP(tPosP p, tListP L){
    return p->next;
}
void deleteAtPositionP(tPosP p, tListP *L){
    tPosP  q;
    if(p==*L) {//borrar 1er elemento
        *L = (*L)->next;
    }else if(p->next == LNULL){ //borrar último elemento
        for(q=*L;q->next!=p;q=q->next);
        q->next = LNULL;
    }else{//borrar en intermedio
        q=p->next;
        p->data = q->data;
        p->next = q->next;
        p=q;
    }
    free(p);
}
void deleteListP(tListP *L){
    tPosP p;
    while(*L != LNULL) {
        p = *L;
        *L = (*L)->next;
        free(p);
    }
}


void updateItemP(tItemP d, tPosP p, tListP *L){
    p ->data = d;

}
