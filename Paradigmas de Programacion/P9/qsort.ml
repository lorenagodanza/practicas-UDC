(*Lorena Godón Danza
Grupo 3.1*)

(*El rendimiento no seŕa bueno en listas muy grandes, con @ podría desbordar la pila *)


		
let randomlist x n=
  let rec aux lista n=
    if n>0 then aux (Random.int(x)::lista) (n-1)
    else lista
  in aux [] n;;
	
let primeros n=
  let rec aux n l=
    if n=0 then l 
    else aux (n-1) (n::l)
  in aux n [];;
	
let l=randomlist 2000 100000;;
let l1=[];;

let crono f x y=
  let t = Sys.time () in
  let _ = f x y in
    (Sys.time() -. t);;
	

let rec qsort1 ord = function
  [] -> []
  | h::t -> let after, before = List.partition (ord h) t 
  in qsort1 ord before @ h :: qsort1 ord after;;
  
  
let rec qsort2 ord =
  let append' l1 l2 = List.rev_append (List.rev l1) l2 in function
    [] -> []
    | h::t -> let after, before = List.partition (ord h) t in append' (qsort2 ord before) (h :: qsort2 ord after);;
    
(*    
let tiempo_desor=
  List.map(crono qsort1 (<)) (List.map (randomlist 100) [1000;10000;100000]);;


val tiempo_desor : float list =
  [0.00375700000000023238; 0.0384560000000000457; 4.55385299999999837]
*)

(*
let tiempo_desor2=
    List.map(crono qsort2 (<)) (List.map (randomlist 100) [1000;10000;1000000]);;



val tiempo_desor2 : float list =
  [0.00405800000000056116; 0.0447550000000003223; 4.490641] *)


(*let tiempo_or=
List.map(crono qsort1 (<)) (List.map (primeros) [1000;100000]);;

val tiempo_or : float list = [0.0404350000000004428; 3.900397] *)

(*Desborda con 100000*)

(*let tiempo_or2=
  List.map(crono qsort2 (<)) (List.map (primeros) [1000;10000]);;
  
  : float list = [0.0354130000000001388; 3.83834299999999917]*)

  
(*En la recursividad terminal la pila no se desborda con listas grandes, pero se hacen más operaciones de modo que resulta más lenta. Usando quicksort las listas ya ordenadas son peores de ordenar porque una de las particiones estará vacía*)


(*Qsort2 es más lenta (aprox 12%) ya que como no usamos @ usamos rev_append despues de rev y hay que hacer mas operaciones *)






























