#ifndef __LIST_H__
#define __LIST_H__
#define SIZE 100
#include <stdlib.h>
#include <stdio.h>
#include <stdbool.h>
#include <string.h>

//Definición de tipos
typedef struct tNode *tPosL;
typedef struct{
    char comm[SIZE];
    int num;
}tItemL;

struct tNode{
    tItemL data;
    tPosL next;
};


typedef tPosL tList;

//funciones
void createEmptyList(tList *L);
bool isEmptyList(tList L);
tPosL first(tList L);
tPosL last(tList L);
tPosL next(tPosL p,tList L);
tPosL previous(tPosL p,tList L);
bool isValid(tPosL p);
bool insertItem(char* d,tList *L);
tItemL getItem(tPosL p);
void removeList(tList *L);




#endif
