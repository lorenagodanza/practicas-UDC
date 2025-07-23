#include <stdio.h>
#include "math.h"
#include "time.h"
#include <sys/time.h>

int fib1(int n){
    if (n<2)
        return n;
    else
        return (fib1(n-1) + fib1(n-2));

}

int fib2(int n){
    int i= 1;
    int j= 0;
    int k;
    for(k = 1; k<= n; k++){
        j= i+j;
        i=j-i;
    }
    return j;
}


int fib3(int n){
    int i=1;
    int j=0;
    int k=0;
    int h=1;
    int t=0;

    while (n>0){
        if((n%2) != 0){
            t= j*h;
            j= (i*h) + (j*k) + t;
            i = (i*k) + t;
        }
        t = h*h;
        h= (2*k*h) + t;
        k= (k*k) +t;
        n= n / 2;
    }
    return j;
}

void test(){
    int n;

    printf("n\tfib1(n)\tfib2(n)\tfib3(n)\n\n");

    for(n=0; n<10; n++){
        printf("%d\t%d\t%d\t%d\n", n, fib1(n), fib2(n), fib3(n));
    }

}


double tiempo_actual(){  //obtenemos la hora actual (en microsegundos)

    struct timeval time;
    if(gettimeofday(&time, NULL)<0){
        return 0.0;
    }return (time.tv_usec + time.tv_sec * 1000000.0);

}

void tiempo_fib1(){
    double t1, t2, t;
    double c_sub, c_aj, c_sobr;
    int k;
    int n;

    printf("\nfib1");
    printf("\n\tn\t\t\t  t\t\t\t t/c1\t\t\t t/c2\t\t\tt/c3\n"); 
   

    for(n=2; n<64; n=2*n){ //calculo de Fibonacci en 2,4,8,16 y 32
        t1= tiempo_actual();
        fib1(n);
        t2= tiempo_actual();

        t=t2-t1;
        if(t<500){
            t1= tiempo_actual();

            for(k=0; k<100000; k++){
                fib1(n);
            }

            t2=tiempo_actual();
            t= (t2-t1)/k;

       }

       //Calculamos las cotas

       c_sub= t/ pow(1.1, (double)n);//subestimada
       c_aj= t/pow(((1+ sqrt(5))/2), n); //ajustada
       c_sobr= t/ pow(2, n); //sobrestimada
       printf("\t%d         \t%15.6f    \t%15.6f    \t%15.6f    \t%15.8f\n", n, t, c_sub, c_aj, c_sobr);

       

   }

}


void tiempo_fib2(){
    double t1, t2, t;
    double c_sub, c_aj, c_sobr;
    int k;
    int n;

    printf("\nfib2");
    printf("\n\tn\t\t\t  t\t\t\t t/c1\t\t\t t/c2\t\t\tt/c3\n"); 
    
    for(n=1000; n<100000000; n=10*n){ 
    //calculo de Fibonacci en 1.000, 10.000, 100.000, 1.000.000 y 10.000.000
        t1= tiempo_actual();
        fib2(n);
        t2= tiempo_actual();

        t=t2-t1;
        if(t<500){
            t1= tiempo_actual();

            for(k=0; k<10000; k++){
                fib2(n);
            }

            t2=tiempo_actual();
            t= (t2-t1)/k;
            printf ("*");
        }else printf (" ");

        //Calculamos las cotas

        c_sub= t/ pow(n,0.8);//subestimada
        c_aj= t/n;  //ajustada
        c_sobr= t/(n* log(n)); //sobrestimada

        printf("\t%d    \t%15.6f    \t%15.6f    \t%15.6f    \t%15.8f\n", n, t, c_sub, c_aj, c_sobr);

    }

}

void tiempo_fib3(){
    double t1, t2, t;
    double c_sub, c_aj, c_sobr;
    int k;
    int n;

    printf("\nfib3");
    printf("\n\tn\t\t\t  t\t\t\t t/c1\t\t\t t/c2\t\t\tt/c3\n");  
    
    for(n=1000; n<100000000; n=10*n){ 
    //calculo de Fibonacci en 1.000, 10.000, 100.000, 1.000.000 y 10.000.000
        t1= tiempo_actual();
        fib3(n);
        t2= tiempo_actual();

        t=t2-t1;
        if(t<500){
            t1= tiempo_actual();

            for(k=0; k<10000; k++){
                fib3(n);
            }

            t2=tiempo_actual();
            t= (t2-t1)/k;

        }

        //Calculamos las cotas

        c_sub= t/ sqrt(log(n));//subestimada
        c_aj= t/ log(n); //ajustada
        c_sobr= t/pow(n,0.5); //sobrestimada

        printf("\t%d    \t%15.6f    \t%15.6f    \t%15.6f    \t%15.8f\n", n, t, c_sub, c_aj, c_sobr);

    }

}

int main() {
    test();

    int i;
    
    
    printf("\n\n");

    for(i=0; i<1; i++){
        tiempo_fib1();
    }
    
    printf("\n\n");

   for(i=0; i<1; i++){
        tiempo_fib2();
    }

    printf("\n\n");
    
    
    for(i=0; i<1; i++){
    	tiempo_fib3();
    }
  
    printf("\n\n");

}
