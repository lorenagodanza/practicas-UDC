//LORENA GODÓN DANZA  lorena.godon@udc.es
//MARTA VIDAL LÓPEZ   marta.vidal.lopez@udc.es

#include<stdio.h>
#include<stdlib.h>
#include<sys/time.h>
#include<time.h>
#include<math.h>
#include<string.h>
#include<stdbool.h>

typedef struct{
    int n;
    double time;
    double sub;
    double ajus;
    double sobre;
    double ajusteo;
} info;
 


/************FUNCIONES AUXILIARES*************/

double microsegundos(){  //obtenemos la hora actual (en microsegundos)

    struct timeval time;
    if(gettimeofday(&time, NULL)<0){
        return 0.0;
    }return (time.tv_usec + time.tv_sec * 1000000.0);

}

void inicializar_semilla (){//Inicializa la semilla 
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


/****************************************************/


struct nodo {
    int elem;
    int num_repeticiones;
    struct nodo *izq, *der;
};

typedef struct nodo *posicion;
typedef struct nodo *arbol;

static struct nodo *crearnodo (int e){

    struct nodo *p = malloc (sizeof(struct nodo));

    if (p==NULL){
        printf("memoria agotada\n");
        exit (EXIT_FAILURE);
    }
    
    p->elem = e;
    p->num_repeticiones = 1;
    p->izq = NULL;
    p->der = NULL;
    return p;    
}

arbol insertar (int e , arbol a){
    if(a == NULL){
        return crearnodo(e);
    }else if (e < a->elem){
        a->izq = insertar(e, a->izq);
    }else if (e > a->elem){
        a->der = insertar(e, a->der);
    }else{
        a->num_repeticiones++;
    }

    return a;
}

arbol creararbol(){
    return NULL;
}


int esArbolVacio(arbol a){
    if(a == NULL){
        return 1;
    }else return 0;
}


posicion buscar (int e, arbol a){
    posicion p;

    if (esArbolVacio(a)){
        p = NULL;
    }else if (e == a->elem){
        p = a;
    }else if (e < a->elem){
        p = buscar(e, a->izq);
    }else if (e > a->elem){
        p = buscar(e, a->der);
    }

   return p;
}


arbol eliminarArbol(arbol a){
   
    if(a!=NULL){
        eliminarArbol(a->izq);
        eliminarArbol(a->der);
        free(a);
        a=NULL;
    }
    return a;
}


posicion hijoIzquierdo(arbol a){
    posicion p;
    if (esArbolVacio(a)){
      return(p = NULL);
   }else{
      return a->izq;
   }   

}

posicion hijoDerecho(arbol a){
    posicion p;
    if (esArbolVacio(a)){
      return(p = NULL);
   }else{
      return a->der;
   }  
}

int elemento (posicion p){
    return p->elem;
}


int numeroRepeticiones(posicion p){
    return p->num_repeticiones;
}


static int max (int a, int b){
    if (a>b){
        return a;
    }else return b;
}


int altura(arbol a){
    if (a == NULL){
        return (-1);
    }else {
        return 1 + max(altura(a->izq), altura(a->der));
    }
}


void viusualizar(arbol a){
    if(!esArbolVacio(a)){
        if(a->izq != NULL){
            printf("(");
            viusualizar(a->izq);
            printf(")");
        }

        printf("%d", a->elem);

        if(a->der != NULL){
            printf("(");
            viusualizar(a->der);
            printf(")");
        }
    }else printf ("Arbol vacio: ()\n");
}



void test(){
    arbol a, pos;
    int i, j;
    int v[] = {3, 1, 2, 5, 4, 5};

    printf("***********TEST***********\n");
    a = creararbol();
    viusualizar(a);
    printf("Altura del arbol: %d\n\n", altura(a));

    for(j=0; j<6; j++){
        a=insertar(v[j], a);
        printf("Inserto un %d\n", v[j]);
    }
    printf("\n");
    printf("Arbol actual: ");
    viusualizar(a); printf("\n");
    printf("Altura del arbol: %d\n\n", altura(a));

    for(i=1; i<=6; i++){
        pos = buscar(i, a);
        if(pos!=NULL){
            printf("Busco %d y encuentro %d repedito: %d veces\n", i, 
            pos->elem, pos->num_repeticiones);
        }else printf("Busco %d y no encuentro nada\n", i);
    }

    printf("\n"); 
    printf("Borro todos los nodos liberando la memoria:\n");
    a= eliminarArbol(a);
    viusualizar(a);
    printf("Altura del arbol: %d\n", altura(a));
    printf("\n**********FIN DEL TEST**************\n\n");
}



void medirTiempos(double vTiempos[8][3]){
    double t_ini, t_fin, t1, t2;
    int n, i, j, k, v[256000];
    n=8000; j=1; i=1;
    arbol tree;
    tree= creararbol();

    while((i<8) && (n<=256000)){
        aleatorio(v,n);
        t_ini = microsegundos();
        for(k=0; k<n; k++){
            tree= insertar(v[k], tree);
        }
        t_fin = microsegundos();
        t1 = t_fin - t_ini;
        if(t1<500){
            n=n*2;
            tree = eliminarArbol(tree);
            continue;
        }

        t_ini = microsegundos();
        for(k=0; k<n; k++){
            buscar(v[k], tree);
        }
        t_fin = microsegundos();
        t2 = t_fin - t_ini;
        if(t2<500){
            n=n*2;
            tree = eliminarArbol(tree);
            continue;
        }

        //se insertan los tiempos en la tabla
        printf("%10i %10i %10i\n", n, (int) t1, (int) t2);
        vTiempos[j][0] = n; vTiempos[j][1] = t1; vTiempos[j][2] = t2;
        ++j; n=n*2; tree= eliminarArbol(tree); i++;

    }
    vTiempos[0][0] = i;
}

void medicion_insercion(double v[8][3]){
    int i;
    double c_Ins_Sub, c_Ins_Aj, c_Ins_Sobre;
    printf("\n\nInsercion de n elementos\n\n");
    printf("%10s %15s %15s %15s %15s\n", "n", "t(n)", "t(n)/n", "t(n)/n^1.18",
      "t(n)/n^1.5");

    
    for(i=1; i<v[0][0]; i++){
        c_Ins_Sub = pow((int) v[i][0], 1); 
        c_Ins_Aj = pow((int) v[i][0], 1.18); 
        c_Ins_Sobre = pow((int) v[i][0], 1.5);

        printf("   %7d %15.3f %15.6f %15.6f %15.6f\n", (int) v[i][0], v[i][1], 
        (int) v[i][1]/c_Ins_Sub, (int) v[i][1]/c_Ins_Aj,
        (int) v[i][1]/c_Ins_Sobre);
    }
}


void medicion_busqueda(double v[8][3]){
    int i;
    double c_Bus_Sub, c_Bus_Aj, c_Bus_Sobre;
    printf("\n\nBusqueda de n elementos\n\n");
    printf("%10s %15s %15s %15s %15s\n", "n", "t(n)", "t(n)/n", "t(n)/n^1.2",
      "t(n)/n^1.5");

   

    for(i=1; i<v[0][0]; i++){
        c_Bus_Sub = pow((int) v[i][0], 1); 
        c_Bus_Aj = pow((int) v[i][0], 1.2); 
        c_Bus_Sobre = pow((int) v[i][0], 1.5);
        printf("   %7d %15.3f %15.6f %15.6f %15.6f\n", (int) v[i][0], v[i][2], 
        (int) v[i][2]/c_Bus_Sub, (int) v[i][2]/c_Bus_Aj,
        (int) v[i][2]/c_Bus_Sobre);
    }
}

int main(){
    inicializar_semilla();
    
    test();
    double v[8][3];
    

    for(int i=0; i<3; i++){
        printf("MEDICION DE TIEMPOS\n\n");
        printf("%10s %10s %10s\n", "n", "t_ins(n)", "t_bus(n)");
        medirTiempos(v);
        medicion_insercion(v);
        medicion_busqueda(v);
        printf("\n\n");
    }

    return 0;
}
