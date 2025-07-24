let randomlist x n=
  let rec aux lista n =
    if n>0
    then aux (Random.int(x)::lista) (n-1)
    else lista
  in aux [] n;;
  

let rec divide l = match l with
    h1::h2::t -> let t1, t2 = divide t in (h1::t1, h2::t2)
  | _ -> l, [];;
  
let rec merge = function
    [], l | l, [] -> l
  | h1::t1, h2::t2 -> if h1 <= h2 then h1 :: merge (t1, h2::t2)
      else h2 :: merge (h1::t1, t2);;
      
      
let rec msort1 l = match l with
    [] | _::[] -> l
  | _ -> let l1, l2 = divide l in
        merge (msort1 l1, msort1 l2);;

let l=randomlist 10 10;;
msort1 l;;



let divide' l =
  let rec aux l t1 t2=
    match l with
        h1::h2::t -> aux t (h1::t1) (h2::t2)
      | _ -> (List.rev_append l t1), t2
  in aux l [] [];;


let  merge' f (l1 ,l2) =
  let rec aux l1 l2 sol=match l1,l2 with
      [], l | l, [] -> (List.rev_append  sol l)
    | h1::t1, h2::t2 -> 
        if f h1 h2 
        then aux  t1 (h2::t2) (h1::sol)
        else aux (h1::t1) t2 (h2::sol)
  in aux l1 l2 [];;
  
  
(*msort2 es un 60% más lento que msort1 *)
  
let rec msort2 f l = match l with
    [] | _::[] -> l
  | _ -> let l1, l2 = divide' l in
        merge' f (msort2  f l1, msort2 f l2);;
let l=randomlist 10 10;;

msort2 (<) l;;

let l2=randomlist 2000 10000000;;        


(* Al manejar listas muy grandes se puede desbordar la pila*)
