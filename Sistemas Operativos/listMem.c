#include "listaMem.h"

void createEmptyListM(tListM *L){
    *L= LNULL;
}
bool createNodeM(tPosM *p){


    *p= malloc(sizeof(struct tNodeM));
    return *p != NULL;
}
bool insertItemM(tItemM d, tPosM p, tListM* L){

    tPosM q,r;
    if(!createNodeM(&q)){
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

bool isEmptyListM(tListM L){
    return(L==LNULL);
}
tItemM getItemM(tPosM p, tListM L){
    return p->data;
}
tPosM  firstM(tListM L){
    return L;
}
tPosM lastM(tListM L){
    tPosM p;
    for(p=L;p->next != LNULL;p = p->next);
    return p;

}
tPosM previousM(tPosM p, tListM L){
    tPosM q;
    if(p==L) { //caso de la 1era posición
        return LNULL;
    }else{
        //se recorre la lista con la variable "p"
        for(q=L;q->next!=p;q=q->next);
        return q;
    }
}
tPosM nextM(tPosM p, tListM L){
    return p->next;
}
void deleteAtPositionM(tPosM p, tListM *L){
    tPosM  q;
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
void deleteListM(tListM *L){
    tPosM p;
    while(*L != LNULL) {
        p = *L;
        *L = (*L)->next;
        free(p);
    }
}





