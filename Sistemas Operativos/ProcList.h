#ifndef __PROCESOS_LIST_
#define __PROCESOS_LIST_


#include <stdio.h>
#include <stdbool.h>
#include <stdlib.h>
#include <signal.h>
#include <time.h>
#include <sys/types.h>
#include <errno.h>
#include <string.h>
#include <sys/wait.h>
#include <unistd.h>
#include <sys/resource.h>
#include <sys/time.h>
#include <pwd.h>
#include <sys/utsname.h>
#define TAM 512
#define VACIA 0
#define ERR -1
#define TERM 1
#define ACTIVO 0
#define PARADO 1
#define TERMINADO 2
#define SIGNAL 3
#define LNULL NULL

typedef struct tNodeP *tPosP;
typedef int elemento;
typedef struct{
    pid_t pid;
    int prioridad;
    uid_t user;
    char comando[TAM];
    time_t hora;
    int estado;
    int senal;
}tItemP;
struct tNodeP {
    tItemP data;
    tPosP next;
};
typedef tPosP tListP;

struct SEN{
    char *nombre;
    int senal;
};




void createEmptyListP (tListP*);
bool insertItemP(tItemP, tPosP, tListP*);
void updateItemP(tItemP, tPosP, tListP*);
void deleteAtPositionP(tPosP, tListP*);
bool isEmptyListP(tListP);
tItemP getItemP(tPosP, tListP);
tPosP firstP(tListP);
tPosP lastP(tListP);
tPosP previousP(tPosP, tListP);
tPosP nextP(tPosP, tListP);
bool createNodeP(tPosP *p);
void deleteListP(tListP *L);
void listar_item(tItemP item);
void borrarsig(tListP *P);
void borrartem(tListP *P);
tPosP deleteNext(tPosP p, tListP *L,int borrar);
void updateItemP(tItemP d, tPosP p, tListP *L);
#endif
