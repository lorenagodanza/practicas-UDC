//Marta Vidal Lopez    -->  marta.vidal.lopez@udc.es
//Lorena Godon Danza   -->  lorena.godon@udc.es
//GRUPO: 3.1


#include <stdio.h>
#include <stdbool.h>
#include <math.h>
#include <time.h>
#include <sys/time.h>
#include <stdlib.h>

#define UMBRAL 100

//algoritmo ordenacion por insercion

void ord_ins (int v [], int n){
    int i, j, x;

    for(i =1; i<n ; i++){
        x = v[i];
        j = i-1;
        while (j>=0 && v[j]>x){
            v[j+1] = v[j];
            j = j-1;
        }

        v[j+1]= x;
        
    }
}


void muestraVector (int v[], int n){ //Imprime el vector por pantalla
    int i;

    for(i =0; i<n; i++){

        printf ("%d  ", v[i]);
    }
}


void inicializarSemilla(){ //Inicializa la semilla 
                        //para generar numeros pseudoaleatorios
    
    srand(time(NULL));
}

void aleatorio(int v [], int n) { //Rellena un vector con numeros
                                    // pseudoaleatorios entre -n y +n
    int i; 
    int m=2*n+1;
    for (i=0; i < n; i++){
        v[i] = (rand() % m) - n;
    }
}


void ascendente(int v [], int n) { //Rellena un vector con numeros 
                                //oredenados de forma ascendente de 0 a n-1
    int i;
    for (i=0; i < n; i++){
        v[i] = i;
    }
}


void descendente(int v[], int n){ //Rellena un vector con numeros 
                                    //ordenados de forma descendente de n a 1
    int i;
    for (i = n; i>0; i--){
        v[n-i]=i;
    }
}


bool isOrdered (int v[], int n){ //Comprueba si el vector esta ordenado
    int i;
    int x = v[0];

    for(i = 0; i < n; i++){
        if(v[i] < x){
            return false;
        }

        x = v[i];
    }
    return true;
}


void intercambiar (int *a, int *b){
    int aux = *a;
    *a =*b;
    *b = aux;
}



void rapida_aux(int v[], int izq, int der){
    int x, i, j;
    int pivote;
    if(izq+UMBRAL <= der){
        x= (rand () % (der - izq +1))+ izq; 
        pivote= v[x];
        intercambiar( &(v[izq]), &(v[x]));
        i= izq +1;
        j= der;

        while (i<=j){
            while (i <= der && v[i]<pivote){
                i= i + 1;

            }
            while(v[j]>pivote){
                j = j-1;
            
            }
            
            if(i<=j){
                intercambiar(&(v[i]), &(v[j]));
                i= i+1;
                j=j-1;
               
            }
  
        }
        intercambiar(&(v[izq]), &(v[j]));
        rapida_aux(v, izq, j-1);
        rapida_aux(v, j+1, der);

    }

}


void ord_rapida (int v[], int n){
    rapida_aux(v, 0, n-1);
    if(UMBRAL > 1){
        ord_ins(v, n);
    }
    
}


void test (int v[],int n){
    int i, j;
    char* sortNames [] = {"por Inserción", "Rapida"};
    char* vNames [] = {"aleatoria", "descendente", "ascendente"};

    void (*initialize[3]) (int[], int) = {aleatorio, descendente, ascendente};

    void (*sort[2]) (int[], int) = {ord_ins, ord_rapida};

    printf("\nTEST PARA COMPROBAR EL CORRECTO FUNCIONAMIENTO DE LOS ALGORITMOS\n");

    for (i = 0; i < 2 ; i++) {

        for (j = 0; j < 3 ; j++) {

            printf("\nInicialización %s\n", vNames[j]);
            initialize[j](v, n);
            muestraVector(v, n);

            printf("%s\n", isOrdered(v, n)? "\tEl vector está ordenado": 
            "\tEl vector no está ordenado");

            printf("\nOrdenación %s\n", sortNames[i]);
            sort[i](v, n);
            muestraVector(v, n);

            printf("%s\n", isOrdered(v, n)? "\tEl vector está ordenado":
             "\tEl vector no está ordenado");
             
            printf("\n");
        }

    }

    printf("\nFIN DEL TEST\n\n");
    
}

double tiempo_actual(){  //obtenemos la hora actual (en microsegundos)

    struct timeval time;
    if(gettimeofday(&time, NULL)<0){
        return 0.0;
    }return (time.tv_usec + time.tv_sec * 1000000.0);

}


void tiempo_insAleatorio (int v[]){//Calcula cuanto tarda la ordenacion
                        // por insercion con inicializacion aleatoria
    double t1, t2, t, ta, tb;
    double c_sub, c_aj, c_sobr;
    int k, n;

    printf("\nORDENACION POR INSERCION *****inicializacion aleatoria*****\n");
    printf("%19s%24s%26s%23s%23s%24s\n", "n", "t","t/c1","t/c2","t/c3","k");
     
    for(n = 500; n < 64000; n = n*2){
        aleatorio(v, n);
        t1= tiempo_actual();
        ord_ins(v, n);
        t2= tiempo_actual();
        t = t2 - t1;

        if(t < 500){ //si el tiempo es menor de 500 microsegundos,
                        // hace la media de repetirlo k veces
            t1 = tiempo_actual();
            for(k = 0; k < 1000; k++){
                aleatorio(v, n);
                ord_ins(v,n);
            }
            t2= tiempo_actual();
            ta= tiempo_actual();
            for (k = 0; k < 1000; k++){
                aleatorio(v, n);
            }
            tb = tiempo_actual();
        
            t= ((t2-t1) - (tb-ta))/ k;
        }
        c_sub = t/pow((double)n, 1.8); //cota subestimada 
        c_aj = t/pow((double)n, 1.99);  //cota ajustada
        c_sobr = t/ pow((double)n,2.2);  //cota sobreestimada

        printf("\t%12d    \t%15.6f    \t%15.6f    \t%15.9f    \t%15.10f    \t%12d\n", 
        n, t, c_sub, c_aj, c_sobr, t<500? k: 1);  
    }
}


void tiempo_insAscendente (int v[]){//Calcula cuanto tarda la ordenacion
                                // por insercion con inicializacion ascendente
    double t1, t2, t, ta, tb,c_sub, c_aj, c_sobr;
    int k,n;

    printf("\nORDENACION POR INSERCION *****inicializacion ascendente*****\n");
    printf("%19s%24s%26s%23s%23s%24s\n", "n", "t","t/c1","t/c2","t/c3","k");
     
    for(n = 500; n <= 64000; n = n*2){
        ascendente(v, n);
        t1= tiempo_actual();
        ord_ins(v, n);
        t2= tiempo_actual();
        t = t2 - t1;

        if(t < 500){ //si el tiempo es menor de 500 microsegundos,
                    // hace la media de repetirlo k veces
            t1 = tiempo_actual();
            for(k = 0; k < 1000; k++){
                ascendente(v, n);
                ord_ins(v,n);
            
            }
            t2= tiempo_actual();
            ta=tiempo_actual();
            for (k = 0; k < 1000; k++){
                ascendente(v, n);
            }
            tb = tiempo_actual();
           
            t= ((t2-t1) - (tb-ta))/ k;
            
        }
        c_sub = t/pow((double)n, 0.8);//cota subestimada 
        c_aj = t/pow((double)n, 1.01);  //cota ajustada
        c_sobr = t/ pow((double)n,1.2);  //cota sobreestimada

        printf("\t%12d    \t%15.6f    \t%15.6f    \t%15.9f    \t%15.10f    \t%12d\n", 
        n, t, c_sub, c_aj, c_sobr, t<500? k: 1); 
    }  
}



void tiempo_insDescendente (int v[]){  //Calcula cuanto tarda la ordenacion
                            // por insercion con inicializacion descendente
    double t1, t2, t, ta, tb, c_sub, c_aj, c_sobr;
    int k, n;
    
    printf("\nORDENACION POR INSERCION *****inicializacion descendente*****\n");
    printf("%19s%24s%26s%23s%23s%24s\n", "n", "t","t/c1","t/c2","t/c3","k");
     
    for(n = 500; n < 64000; n = n*2){
        descendente(v, n);
        t1= tiempo_actual();
        ord_ins(v, n);
        t2= tiempo_actual();
        t = t2 - t1;

        if(t < 500){  //se hace la media de repetirlo k veces
            t1 = tiempo_actual();
            for(k = 0; k < 1000; k++){
                descendente(v, n);
                ord_ins(v,n);
            }
            t2= tiempo_actual();

            ta=tiempo_actual();
            for (k = 0; k < 1000; k++){
                descendente(v, n);
            }
            tb = tiempo_actual();
        
            t= ((t2-t1) - (tb-ta))/ k;
        }
        c_sub = t/pow((double)n, 1.8); //cota subestimada 
        c_aj = t/pow((double)n, 2);  //cota ajustada
        c_sobr = t/ pow((double)n,2.2);  //cota sobreestimada

        printf("\t%12d    \t%15.6f    \t%15.6f    \t%15.9f    \t%15.10f    \t%12d\n",
         n, t, c_sub, c_aj, c_sobr, t<500? k: 1); 
    }
}




void tiempo_QuickAscendente(int v[]){
    double t1, t2, t, ta, tb, c_sub, c_aj, c_sobr;
    int k,n;
    printf("\nORDENACION RAPIDA *******inicializacion ascendente*******\n");
    printf("%19s%24s%26s%23s%23s%24s%7s\n", "n", "t","t/c1","t/c2","t/c3","k","U");
    for(n = 500; n < 64000; n = n*2){
        ascendente(v, n);
        t1= tiempo_actual();
        ord_rapida(v, n);
        t2= tiempo_actual();
        t = t2 - t1;
        if(t < 500){ //se hace la media de repetirlo k veces
            t1 = tiempo_actual();
            for(k = 0; k < 1000; k++){
                ascendente(v, n);
                ord_rapida(v,n);
            }
            t2= tiempo_actual();
            ta=tiempo_actual();
            for (k = 0; k < 1000; k++){
                ascendente(v, n);
            }
            tb = tiempo_actual();
            t= ((t2-t1) - (tb-ta))/ k;
        }
        if(UMBRAL==1){
            c_sub = t/pow((double)n, 0.90); //cota subestimada 
            c_aj = t/pow((double)n, 1.093);  //cota ajustada 
            c_sobr = t/ pow((double)n, 1.30);  //cota sobreestimada
        }else if (UMBRAL==10){
            c_sub = t/pow((double)n, 0.81); //cota subestimada 
            c_aj = t/(pow((double)n, 1.09));  //cota ajustada 
            c_sobr = t/ pow((double)n, 1.22);  //cota sobreestimada
        }else if (UMBRAL==100){
            c_sub = t/pow((double)n, 0.98); //cota subestimada 
            c_aj = t/pow((double)n, 1.18);  //cota ajustada (cte= 0.0019)
            c_sobr = t/ pow((double)n, 1.38);  //cota sobreestimada
        }
        printf("\t%12d    \t%15.6f    \t%15.6f    \t%15.9f    \t%15.10f    \t%12d   \t%d\n",
         n, t, c_sub, c_aj, c_sobr, t<500? k: 1, UMBRAL); 
    }
}




void tiempo_QuickDescendente(int v[]){
    double t1, t2, t, ta, tb, c_sub, c_aj, c_sobr;
    int k,n;
    printf("\nORDENACION RAPIDA *******inicializacion descendente*******\n");
    printf("%19s%24s%26s%23s%23s%24s%7s\n", "n", "t","t/c1","t/c2","t/c3","k","U");
    for(n = 500; n < 64000; n = n*2){
        descendente(v, n);
        t1= tiempo_actual();
        ord_rapida(v, n);
        t2= tiempo_actual();
        t = t2 - t1;
        if(t < 500){ //se hace la media de repetirlo k veces
            t1 = tiempo_actual();
            for(k = 0; k < 1000; k++){
                descendente(v, n);
                ord_rapida(v,n);
            }
            t2= tiempo_actual();
            ta=tiempo_actual();
            for (k = 0; k < 1000; k++){
                descendente(v, n);
            }
            tb = tiempo_actual();
            t= ((t2-t1) - (tb-ta))/ k;
        }
        if(UMBRAL==1){
            c_sub = t/pow((double)n, 0.91); //cota subestimada 
            c_aj = t/pow((double)n, 1.072);  //cota ajustada
            c_sobr = t/ pow((double)n, 1.31);  //cota sobreestimada
        }else if (UMBRAL==10){
            c_sub = t/pow((double)n, 0.81); //cota subestimada 
            c_aj = t/pow((double)n, 1.077); //cota ajustada 
            c_sobr = t/ pow((double)n, 1.21);  //cota sobreestimada
        }else if (UMBRAL==100){
            c_sub = t/pow((double)n, 0.97); //cota subestimada 
            c_aj = t/pow((double)n, 1.097);  //cota ajustada
            c_sobr = t/ pow((double)n, 1.37);  //cota sobreestimada
        }
        printf("\t%12d    \t%15.6f    \t%15.6f    \t%15.9f    \t%15.10f    \t%12d   \t%d\n",
        n, t, c_sub, c_aj, c_sobr, t<500? k: 1, UMBRAL); 
    }
}



void tiempo_QuickAleatorio(int v[]){
    double t1, t2, t, ta, tb, c_sub, c_aj, c_sobr;
    int k,n;
    printf("\nORDENACION RAPIDA *******inicializacion aleatoria*******\n");
    printf("%19s%24s%26s%23s%23s%24s%7s\n", "n", "t","t/c1","t/c2","t/c3","k","U");
    for(n = 500; n < 64000; n = n*2){
        aleatorio(v, n);
        t1= tiempo_actual();
        ord_rapida(v, n);
        t2= tiempo_actual();
        t = t2 - t1;
        if(t < 500){  //se hace la media de repetirlo k veces
            t1 = tiempo_actual();
            for(k = 0; k < 1000; k++){
                aleatorio(v, n);
                ord_rapida(v,n);
            }
            t2= tiempo_actual();
            ta=tiempo_actual();
            for (k = 0; k < 1000; k++){
                aleatorio(v, n);
            }
            tb = tiempo_actual();
            t= ((t2-t1) - (tb-ta))/ k;
        }
        if(UMBRAL==1){
            c_sub = t/pow((double)n, 0.90); //cota subestimada 
            c_aj = t/pow((double)n, 1.10);  //cota ajustada 
            c_sobr = t/ pow((double)n, 1.30);  //cota sobreestimada
        }else if (UMBRAL==10){
            c_sub = t/pow((double)n, 0.81); //cota subestimada 
            c_aj = t/pow((double)n, 1.132);  //cota ajustada 
            c_sobr = t/ pow((double)n, 1.21);  //cota sobreestimada
        }else if (UMBRAL==100){
            c_sub = t/pow((double)n, 0.91); //cota subestimada 
            c_aj = t/pow((double)n, 1.139);  //cota ajustada 
            c_sobr = t/ pow((double)n, 1.31);  //cota sobreestimada
        }
        printf("\t%12d    \t%15.6f    \t%15.6f    \t%15.9f    \t%15.10f    \t%12d   \t%d\n",
         n, t, c_sub, c_aj, c_sobr, t<500? k: 1, UMBRAL); 
    }
}


/*-----------------------------------------------------------------*/




int main (){
    int n = 15;
    int v [64000];

    inicializarSemilla();
    test(v,n);

    for(int i = 0; i < 3; i++){
        tiempo_insAleatorio(v);
    }
    

    for(int i = 0; i < 1; i++){
        tiempo_insAscendente(v);
    }

    for(int i = 0; i < 1; i++){
        tiempo_insDescendente(v);
    }  

   
    for(int i = 0; i < 1; i++){
        tiempo_QuickAscendente(v);
    }

    for(int i = 0; i < 1; i++){
        tiempo_QuickDescendente(v);
    }

    for(int i = 0; i < 2; i++){
        tiempo_QuickAleatorio(v);
    }  
}
