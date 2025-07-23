#ifndef __MEMORIA_H_
#define __MEMORIA_H_
#include <stdio.h>
#include <string.h>
#include <stdbool.h>
#include <stdlib.h>
#define LNULL NULL
#define MALLOC 0
#define MMAP 1
#define SHARED 2

typedef struct tNodeM *tPosM; 
typedef int elemento;
typedef struct{
    void* dir_malloc;
    int key;
    char nome_ficheiro[256];
    unsigned long int tam;
    time_t hora;
    int tipo;
}tItemM;
struct tNodeM {
    tItemM data;
    tPosM next;
};
typedef tPosM tListM;

//CABECERAS
void createEmptyListM (tListM*);
bool insertItemM(tItemM, tPosM, tListM*);
void deleteAtPositionM(tPosM, tListM*);
bool isEmptyListM(tListM);
tItemM getItemM(tPosM, tListM);
tPosM firstM(tListM);
tPosM lastM(tListM);
tPosM previousM(tPosM, tListM);
tPosM nextM(tPosM, tListM);
bool createNodeM(tPosM *p);
void deleteListM(tListM *L);


#endif
