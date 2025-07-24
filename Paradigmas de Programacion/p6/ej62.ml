(*Lorena Godón Danza 
GRUPO 3.1 *)


(*CURRY Y UNCURRY *)

let curry f x y = f (x,y);;
(* val curry : ('a * 'b -> 'c) -> 'a -> 'b -> 'c = <fun> *)

let uncurry f(x,y)= (f x)y;;

uncurry(+);;
(* - : int * int -> int = <fun> *)

let sum=(uncurry(+));;
(* val sum : int * int -> int = <fun> *)

(* sum 1;; *)
(* Error: espera tipo int*int *)

 sum (2,1);;
 (*  - : int = 3 *)
 
 let g = curry (function p->2*fst p + 3*snd p);;
 
(* val g : int -> int -> int = <fun> *)

(* g(2,5) *)
(* Error: This expression has type 'a * 'b but an expression was expected of type int*)


let h=g 2;;
(* val h : int -> int = <fun> *)

h 1, h 2 , h 3;;
(* - : int * int * int = (7, 10, 13) *)


(* COMPOSICION *)

let comp f g x= f(g x);;
let f=let square x = x * x in comp square((+)1);;
(* - : int * int * int = (4, 9, 16) *)



(*POLIMORFISMO *)

let i x=x;;
let j (x,y)=x;;
let k (x,y)=y;;
let l x=[x];;
(*Hay infinitas funciones para cada uno de estos tipos *)




