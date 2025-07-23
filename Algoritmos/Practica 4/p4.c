//LORENA GODÓN DANZA  lorena.godon@udc.es
//MARTA VIDAL LÓPEZ   marta.vidal.lopez@udc.es

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <math.h>
#include <stdbool.h>
#include <sys/time.h>


#define TAM 512000

typedef struct {
    int vector [TAM];
    int ultimo;
}monticulo;


//funciones auxiliares para los vectores

double microsegundos(){  //obtenemos la hora actual (en microsegundos)

    struct timeval time;
    if(gettimeofday(&time, NULL)<0){
        return 0.0;
    }return (time.tv_usec + time.tv_sec * 1000000.0);

}

void inicializar_semilla(){ 
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



//FUNCIONES REFERIDAS AL MONTICULO

void inicializar_monticulo (monticulo *m){
    m->ultimo = -1;
}

int monticulo_vacio (monticulo *m){
    if(m->ultimo == -1){
        return 1;
    }else return 0;
}


//intercambia dos posiciones del monticulo
void intercambiar (int* a, int* b) {
	// Intercambia dous valores enteiros recibidos por referencia 
	int aux = *a;
	*a = *b;
	*b = aux;
}



void flotar (monticulo *m, int i){
    while((i>0) && (m->vector[(i-1)/2] < m->vector[i])){
        intercambiar(&(m->vector[(i-1)/2]), &(m->vector[i]));
        i = (i-1)/2;
    }
}





void hundir(monticulo *m, int i){
    int hizq;
    int hder;
    int j;

    do{
        hizq = 2*i +1;
        hder = 2*i +2;
        j = i;

        if((hder <= m->ultimo) && (m->vector[hder] > m->vector[i])){
            i = hder;
        }

        if((hizq <= m->ultimo) && (m->vector[hizq] > m->vector[i])){
            i = hizq;
        }

        intercambiar (&(m->vector[j]), &(m->vector[i]));
    
    
    }while (j!=i); //Si j=i el nodo alcanzo su posicion final

}

int consultar_mayor ( monticulo *m) {
	/* Devolve o maior (a raiz) do monticulo. */
	int x;
	if (monticulo_vacio(m)) {
		printf("Error: Monticulo vacio\n");
        exit(EXIT_FAILURE);
	} else {
		x = m->vector[0];
	}
	return x;
}

int eliminar_mayor(monticulo *m){
    int x;
    if(monticulo_vacio(m)){
        printf("Error: Monticulo vacio\n");
        exit(EXIT_FAILURE);
    }else{
        x = m->vector[0];
        m->vector[0] = m->vector[m->ultimo];
        m->ultimo = m->ultimo - 1;

        if(m->ultimo > -1){
            hundir(m, 0);
        }

        return x;
    }
}


void crear_monticulo (int v[], int n, monticulo *m){
    int i;
    inicializar_monticulo(m);

    for (i = 0; i < n; i++){
        m->vector[i] = v[i];
    }

    m->ultimo = n - 1;

    for(i = m->ultimo/2; i >=0; i--){
        hundir(m, i);
    }

}




int is_Sorted(int v[], int n){
    int i;

    for (i = 0; i < (n-1); i++){  //recorremos el vector
        if(v[i] > v[(i+1)]){  //comprobamos si el siguiente elemento es menor
            return 0;
        }
    }

    return 1;  

}



void ord_monticulo(int v[], int n){
    int i;
    monticulo m;

    crear_monticulo(v, n, &m);
    
    for (i = n -1; i > -1; i--){
        v[i] = eliminar_mayor(&m);
    }

}


void print_vector(int v[], int n){
    int i;
    for(i = 0; i<n; i++){
        printf("%d  ", v[i]);
    }
    printf("\n");
}

void mostrar_mont(monticulo *m) {
    int i;
    if(monticulo_vacio(m)){
        printf("Monticulo vacio: no se puede eliminar\n");
    }else{
        for (i= 0; i <= m->ultimo; i++)
		printf("[%d] ",m->vector[i]);

    printf("\n");
    }

}





void testInicializarMonticulo(){
    monticulo m;

    printf("\n\n************************************\n\n");
    printf("\nTest inicializar monticulo\n\n");
    inicializar_monticulo(&m);


    if(monticulo_vacio(&m)){
        printf("Monticulo inicializado correctamente\n");
        printf("\n*****TEST SUPERADO*****\n\n");
    }else {
        printf("*************TEST NO SUPERADO***************\n");
    }

}




void test_validar(monticulo *m){
	
    int N = 6;
    int v[6] = {7,10,14,23,68,12};
    int i;

    printf("\nTEST CREACION Y ELIMINACION MONTICULOS\n\n");

    printf("Dado el vector:\n");
    print_vector(v, 6);
    printf("\n");
    printf("Se crea el montículo:\n");
	crear_monticulo(v,N,m);
    mostrar_mont(m);
    printf("\n");
    
    for(i=0; i<N; i++){
        printf("\nEliminamos el mayor (%d) y resulta el" 
        "siguiente monticulo:\n",consultar_mayor(m));
        eliminar_mayor(m);
        mostrar_mont(m);
    }
    
	
    printf("\n\n");
    
}

void test_ordenacion(){

    int n= 10, v[n];

    printf("\nTEST DE ORDENACION POR MONTICULOS\n\n");
   
    printf("Inicializado ascendentemente\n");
    ascendente(v,n);
    print_vector(v,n);
    printf("Ordenado? %d\n", is_Sorted(v,n));
    ord_monticulo(v, n);
    print_vector(v,n); 
    printf("Ordenado? %d\n\n", is_Sorted(v,n));

    printf("Inicializado descendentemente\n");
    descendente(v,n);
    print_vector(v,n);
    printf("Ordenado? %d\n", is_Sorted(v,n));
    ord_monticulo(v, n);
    print_vector(v,n); 
    printf("Ordenado? %d\n\n", is_Sorted(v,n));

    printf("Inicializado aleatoriamente\n");
    aleatorio(v,n);
    print_vector(v,n);
    printf("Ordenado? %d\n", is_Sorted(v,n));
    ord_monticulo(v, n);
    print_vector(v,n); 
    printf("Ordenado? %d\n\n", is_Sorted(v,n));
}




void tiempos_creacion (int n,void inicializacion(int v[], int n)){
	int v[n];			
	double ta,tb,t;
	int k;
	monticulo m;

	inicializar_monticulo(&m);
	inicializacion(v,n);
	ta = microsegundos();
	crear_monticulo(v,n,&m);
	tb = microsegundos();
	t=tb-ta;
	if (t<500){
		ta = microsegundos();	
		for(k= 0;k<1000;k++){
			inicializacion(v,n);
		    crear_monticulo(v,n,&m);
		}
		tb = microsegundos();
		t=tb-ta;
		ta = microsegundos();
		for(k= 0;k<1000;k++){
			inicializacion(v,n);
		}
		tb = microsegundos();
		t= (t-(tb-ta))/k;	
		printf("*");
	}else printf(" ");
	printf("\t%10d    \t%12.5f    \t%11.9f    \t%11.9f    \t%11.10f\n",
                n, t, t/pow(n, 0.8), t/n, t/pow(n, 1.2));

}

void tiempos_ordenacion(int n,void inicializacion(int v[], int n)){
	int v[n];			
	double ta,tb,t;
	int k;
	
	inicializacion(v,n);
	ta = microsegundos();
	ord_monticulo(v,n);
	tb = microsegundos();
	t=tb-ta;
	if (t<500){
		ta = microsegundos();	
		for(k= 0;k<1000;k++){
			inicializacion(v,n);
		    ord_monticulo(v,n);
		}
		tb = microsegundos();
		t=tb-ta;
		ta = microsegundos();
		for(k= 0;k<1000;k++){
			inicializacion(v,n);
		}
		tb = microsegundos();
		t= (t-(tb-ta))/k;	
		printf("*");
	}else printf(" ");
	printf("\t%10d    \t%12.5f    \t%11.9f    \t%11.9f    \t%11.10f\n",
                n, t, t/pow(n, 0.93), t/(n*log(n)), t/pow(n,1.6));

}

void mostrar_tablas(){
    int i;
    
    printf("\nMedicion tiempos Crear Monticulo\n\n");
    printf("%18s%14s%25s%14s%20s\n", "n", "t","t/n^0.8","t/n","t/n^1.2\n");
    for (i=500;i<=512000;i=i*2){
		tiempos_creacion(i,ascendente);
	}
 
    printf("\nMedicion tiempos Ordenar Monticulo Ascendente\n\n");
    printf("%18s%14s%27s%15s%16s\n", 
    "n", "t","t(n)/n^0.93","t/n*log(n)","t/n^1.6\n");
    for (i=500;i<=512000;i=i*2){
		tiempos_ordenacion(i,ascendente);
	}  

   printf("\nMedicion tiempos Ordenar Monticulo Descendente\n\n");
    printf("%18s%14s%27s%15s%16s\n",
     "n", "t","t(n)/n^0.93","t/*log(n)","t/n^1.6\n");
    for (i=500;i<=512000;i=i*2){
		tiempos_ordenacion(i,descendente);
	}

    printf("\nMedicion tiempos Ordenar Monticulo Aleatorio\n\n");
    printf("%18s%14s%27s%15s%16s\n", 
    "n", "t","t(n)/n^0.93","t/n*log(n)","t/n^1.6\n");
    for (i=500;i<=512000;i=i*2){
		tiempos_ordenacion(i,aleatorio);
	}  

    printf("\n");
}

int main(){
    monticulo m;
    int i;
    inicializar_semilla();

    testInicializarMonticulo();
    test_validar(&m);
    test_ordenacion();
    
    for(i=0; i<1; i++){
    mostrar_tablas();
    }

   return 0;
}
