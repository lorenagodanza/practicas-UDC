//Adrián Méndez Barrera y Rubén Fernández Farelo.
//Grupo 2.3
#include <stdio.h>
#include <stdbool.h>
#include <stdlib.h>
#include <time.h>
#include <sys/time.h>
#include <math.h>
#define UMBRAL 1



void inicializar_semilla() {
srand(time(NULL));
}

double microsegundos() {

    struct timeval t;
    if (gettimeofday(&t, NULL) < 0 )
        return 0.0;
    return (t.tv_usec + t.tv_sec * 1000000.0);
}


void aleatorio(int v [], int n) {
    int i, m = 2 * n + 1;
    for (i = 0; i < n; i++)
        v[i] = (rand() % m) - n;
}

void ascendente(int v [], int n) {
    int i;
    for (i = 0; i < n; i++)
        v[i] = i;
}


void descendente(int v[], int n)
{
    int i;
    for (i = 0; i < n; i++)
        v[i] = n - i - 1;
}

bool ordenado(int v [], int n){
    int cont=0,i;
    for (i = 0; i < n - 1; i++){
        if (v[i] <= v [i+1]){
            cont++;
        }
    }if (cont == n-1){
        return true;
    }else return false;
}

void ord_ins(int v [], int n){ 
    int i,j,x;
    for (i = 1; i < n; i++){
        x = v[i];
        j = i - 1;
        while (j >= 0 && v[j] > x){
            v[j+1] = v[j];
            j = j - 1;
        } v[j+1] = x;
    }
}

void printArray(int v [], int n){
    int i;
    for(i = 0; i < n; i++){
        printf("[%d]",v[i]);
    }
}
void ordenarInsComprobar(int v [], int n){

    printf("\n\nOrdenacion por Insercion:\n");
    ord_ins(v,n);
    printArray(v,n);
    printf("\nOrdenado? %d",ordenado(v,n));
}


double * medicion1(void (*inicializar)(int [],int)){
    double t1,t2,t3,t4;
    static double t[6];
    int n = 1000,i,j,h=0;
    int *V;
    for (i = 500; i <= 32000 ; i = i*2) {
    	V = (int *) malloc(i * sizeof(int));
        inicializar(V,i);
        t1 = microsegundos();
        ord_ins(V,i);
        t2 = microsegundos();
        t[h] = t2 - t1;
   
        if (t[h] < 500){
            t1 = microsegundos();
            for (j = 0; j < n; j++){
                inicializar(V,i);
                ord_ins(V,i);
            }
            t2 = microsegundos();
            t3=t2-t1;
            t1 = microsegundos();

            for (j = 0; j < n; j++){
                inicializar(V,i);
            }
            t2 = microsegundos();
            t4= t2-t1;
            t[h] = (t3-t4)/n;
        }else
            printf(" ");
        h++;
        free(V);
    }
    return t;
        
      
}

void medicionAscendente(){
    
     int i,h=0;
    double x,y,z,*t;
    t = medicion1(ascendente);
    
    printf("\nMedicion ascendente (insercion):\n");
    printf("\n                                 C.Subestimada    C.Ajustada    "
           " C.Sobrestimada");
    printf("\n            n         t(n)       t(n)/n^0.8       t(n)/n^1   "
           "    t(n)/n^1.1\n");
    printf("------------------------------------------------------------------"
           "-----------------\n");

    for(i = 500; i <= 32000; i = i*2){
        
        x = t[h] / pow(i,0.5);
        y = t[h] / pow(i,1);
        z = t[h] / pow(i,1.5);
        
        if(t[h] < 500)
             printf(">%12d%16.6f%15.6f%15.6f%15.6f\n", i, t[h], x, y, z);
        else
            printf(" %12d%16.6f%15.6f%15.6f%15.6f\n", i, t[h], x, y, z);
        h++;
    }
    printf("\n>:tiempo promedio (en μs) de 1000 ejecuciones del algoritmo\n");

}


void medicionDescendente(){

   int i,h=0;
    double x,y,z,*t;
    t = medicion1(descendente);
    
    printf("\nMedicion descendente (insercion):\n");
    printf("\n                                 C.Subestimada    C.Ajustada    "
           " C.Sobrestimada");
    printf("\n            n         t(n)       t(n)/n^1.8       t(n)/n^2   "
           "    t(n)/n^2.2\n");
    printf("------------------------------------------------------------------"
           "-----------------\n");

    for(i = 500; i <= 32000; i = i*2){
        
        x = t[h] / pow(i,1.8);
        y = t[h] / pow(i,2);
        z = t[h] / pow(i,2.2);
        
        if(t[h] < 500)
             printf(">%12d%16.6f%15.6f%15.6f%15.6f\n", i, t[h], x, y, z);
        else
            printf(" %12d%16.6f%15.6f%15.6f%15.6f\n", i, t[h], x, y, z);
        h++;
    }
    printf("\n>:tiempo promedio (en μs) de 1000 ejecuciones del algoritmo\n");
}

void medicionAleatorio(){


   int i,h=0;
    double x,y,z,*t;
    t = medicion1(aleatorio);
    
    printf("\nMedicion aleatoria (insercion):\n");
    printf("\n                                 C.Subestimada    C.Ajustada    "
           " C.Sobrestimada");
    printf("\n            n         t(n)       t(n)/n^1.8       t(n)/n^2   "
           "    t(n)/n^2.2\n");
    printf("------------------------------------------------------------------"
           "-----------------\n");

    for(i = 500; i <= 32000; i = i*2){
        
        x = t[h] / pow(i,1.8);
        y = t[h] / pow(i,2);
        z = t[h] / pow(i,2.2);
        
        if(t[h] < 500)
             printf(">%12d%16.6f%15.6f%15.6f%15.6f\n", i, t[h], x, y, z);
        else
            printf(" %12d%16.6f%15.6f%15.6f%15.6f\n", i, t[h], x, y, z);
        h++;
    }
    printf("\n>:tiempo promedio (en μs) de 1000 ejecuciones del algoritmo\n");
}




int num_aleatorio(int izq, int der){
    //return min + rand() / (RAND_MAX / (max - min + 1) +1);
    return (rand () % (der - izq +1))+ izq;
}
/*
void intercambio(int v[],int i, int j){

    int aux; 
    aux = v[i];
    v[i] = v[j];
    v[j] = aux;
}
*/


void intercambio(int i, int j, int v[]) {
    int aux;

    // se intercambian los valores "i" y "j" en el vector "v" 

    aux = v[j];
    v[j] = v[i];
    v[i] = aux;
}

  
void mediana3(int v[], int i, int j) {
    int k;

    if (i >= j) {
        return;
    }

    k = (i + j) / 2;

    if (v[k] > v[j]) {
        intercambio(j, k, v);
    }
    if (v[k] > v[i]) {
        intercambio(i, k, v);
    }
    if (v[i] > v[j]) {
        intercambio(i, j, v);
    }
}

void rapida_aux(int v[], int izq, int der) {
    int i, j, pivote;

    if (izq + UMBRAL <= der) {
        mediana3(v, izq, der);

        pivote = v[izq];
        i = izq;
        j = der;

        do {
            do i++; while (v[i] < pivote);
            do j--; while (v[j] > pivote);
            intercambio(i, j, v);
        } while (j > i);
        intercambio(i, j, v);
        intercambio(izq, j, v);
        rapida_aux(v, izq, j - 1);
        rapida_aux(v, j + 1, der);
    }
}

void ord_rapida(int v[], int n) {
    rapida_aux(v, 0, n - 1);
    if (UMBRAL > 1) {
        ord_ins(v, n);
    }
}


/*
void rapida_aux(int v[], int izq, int der){

    int i=0, j=0,x;
    int pivote;

    if (izq + UMBRAL <= der){
        x = num_aleatorio(izq,der);// mediana3
        pivote = v[x];
        intercambio(v,izq,x);
        i = izq + 1;
        j = der;
        while(i <= j){
            while(i <= der && v[i] < pivote){
                i++;
            }
            while(v[j] > pivote){
                j--;
            }
            if(i <= j){
                intercambio(v,i,j);
                i++;
                j--;
            }
        }
        intercambio(v,izq,j);
        rapida_aux(v,izq,j-1);
        rapida_aux(v,j+1,der);
    }
}




void ord_rapida (int v[], int n){

    rapida_aux(v,0,n-1);
    if (UMBRAL > 1){
        ord_ins (v,n);
    }
}
*/
double * medicion(void (*inicializar)(int [],int)){
    double t1,t2,t3,t4;
    static double t[6];
    int n = 1000,i,j,h=0;
    int *V;
    for (i = 8000; i <= 1024000 ; i = i*2) {
    	V = (int *) malloc(i * sizeof(int));
        inicializar(V,i);
        t1 = microsegundos();
        ord_rapida(V,i);
        t2 = microsegundos();
        t[h] = t2 - t1;
        
        if (t[h] < 500){
            t1 = microsegundos();
            for (j = 0; j < n; j++){
                inicializar(V,i);
                ord_rapida(V,i);
            }
            t2 = microsegundos();
            t3=t2-t1;
            t1 = microsegundos();

            for (j = 0; j < n; j++){
                inicializar(V,i);
            }
            t2 = microsegundos();
            t4= t2-t1;
            t[h] = (t3-t4)/n;
        }else
            printf(" ");
        h++;
        free(V);
    }
    return t;
}

void cabecera(char s[], double a, double b, double c){


    printf("\nMedicion %s (UMBRAL %d):\n",s,UMBRAL);
    printf("\n                                 C.Subestimada    C.Ajustada    "
           " C.Sobrestimada");
    printf("\n            n        t(n)        t(n)/tn^%.2f     t(n)/n^%.2f "
          "   t(n)/n^%.2f \n",a,b,c);
    printf("------------------------------------------------------------------"
          "-----------------\n");
           
              
}


void complejidad_asc(double sub, double aj, double sobr){

     int i,h=0;
    double x,y,z,*t;
    t = medicion(ascendente);


    cabecera("ascendente quicksort",sub,aj,sobr);

    for(i = 8000; i <= 1024000; i = i*2){
        
        x = t[h] / pow(i,sub);
        y = t[h] / pow(i,aj);
        z = t[h] / pow(i,sobr);
        
        if(t[h] < 500)
             printf(">%12d%16.6f%15.6f%15.6f%15.6f\n", i, t[h], x, y, z);
        else
            printf(" %12d%16.6f%15.6f%15.6f%15.6f\n", i, t[h], x, y, z);
        h++;
    }
    printf("\n>:tiempo promedio (en μs) de 1000 ejecuciones del algoritmo\n");

}


void complejidad_des(double sub, double aj, double sobr){

    int i,h=0;
    double x,y,z,*t;
    t = medicion(descendente);

    cabecera("descendente quicksort",sub,aj,sobr);

    for(i = 8000; i <= 1024000; i = i*2){
        
        x = t[h] / pow(i,sub);
        y = t[h] / pow(i,aj);
        z = t[h] / pow(i,sobr);

        if(t[h]<500)
             printf(">%12d%16.6f%15.6f%15.6f%15.6f\n", i, t[h], x, y, z);
        else
            printf(" %12d%16.6f%15.6f%15.6f%15.6f\n", i, t[h], x, y, z);
        h++;
    }
    printf("\n>:tiempo promedio (en μs) de 1000 ejecuciones del algoritmo\n");

}


void complejidad_al(double sub, double aj, double sobr){

    int i,h=0;
    double x,y,z,*t;
    t = medicion(aleatorio);

    cabecera("aleatoria quicksort",sub,aj,sobr);

    for(i = 8000; i <= 1024000; i = i*2){
        
        x = t[h] / pow(i,sub);
        y = t[h] / pow(i,aj);
        z = t[h] / pow(i,sobr);

        if(t[h] < 500)
             printf(">%12d%16.6f%15.6f%15.6f%15.6f\n", i, t[h], x, y, z);
        else
            printf(" %12d%16.6f%15.6f%15.6f%15.6f\n", i, t[h], x, y, z);
        h++;
    }
    printf("\n>:tiempo promedio (en μs) de 1000 ejecuciones del algoritmo\n");

}


void cotas(){

    switch(UMBRAL){

        case 1:
            complejidad_asc(0.8, 1, 1.1); 
            complejidad_des(0.9, 1.06, 1.13);
            complejidad_al(1, 1.07, 1.13);
            break;

        case 10:
            complejidad_asc(1, 1.1, 1.17); 
            complejidad_des(0.9, 1.08, 1.15);
            complejidad_al(0.9, 1.08, 1.19);
            break;

        case 100:
            complejidad_asc(0.98, 1.12, 1.22);
            complejidad_des(0.98, 1.11, 1.23);
            complejidad_al(0.98, 1.07, 1.19);
            break;

        default:
            printf("Umbral no implementado\n");
    }
}




void printComprobar(int v [], int n){
    printArray(v,n);
    printf("\nOrdenado? %d",ordenado(v,n));
}

void test(){
    int n=10,v[n];
    printf("INSERCION:\n");
    printf("Inicializacion aleatoria:\n");
    aleatorio(v,n);
    printComprobar(v,n);
    ordenarInsComprobar(v,n);

    printf("\n\n\nInicializacion descendente:\n");
    descendente(v,n);
    printComprobar(v,n);
    ordenarInsComprobar(v,n);

    printf("\n\n\nInicializacion ascendente:\n");
    ascendente(v,n);
    printComprobar(v,n);
    printf("\n\n");
    
    printf("QUICKSORT:\n");
    printf("Inicializacion aleatoria:\n");
    n = num_aleatorio(5,9);
    aleatorio(v,n);
    printComprobar(v,n); 
    printf("\n");  
    ord_rapida(v,n);
    printf("Ordenacion rapida:\n");
    printComprobar(v,n);

    printf("\n\nInicializacion ascendente:\n");
    n = num_aleatorio(5,9);
    ascendente(v,n);
    printComprobar(v,n);

    n = num_aleatorio(5,9);
    printf("\n\nInicializacion descendente:\n");
    descendente(v,n);
    printComprobar(v,n);
    printf("\n");
    ord_rapida(v,n);
    printf("Ordenacion rapida:\n");
    printComprobar(v,n);
    printf("\n"); 
  
}
 
int main(){
    int i ;
    inicializar_semilla(); 
    
  test();
  for(i=0;i < 3;i++){
    medicionAscendente();
     }
  for(i=0;i < 3;i++){
   medicionDescendente();
    }
  for(i=0;i < 3;i++){
  medicionAleatorio(); 
    }
  for(i=0;i < 3;i++){//Se imprimen las tres mediciones y despues se repiten, no como los for de insercion
   cotas(); 
    }
}
