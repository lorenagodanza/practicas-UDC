
type 'a g_tree =
  Gt of 'a * 'a g_tree list
;;

let rec size = function 
    Gt (_, []) -> 1
  | Gt (r, h::t) -> size h + size (Gt (r, t))
;;


let rec height = function
  Gt(x,[])->1
  |Gt(_,lista)->1+(List.fold_left (max) 1 (List.map height lista));;
  

 let rec leaves=function
   Gt(x,[])->[x]
   |Gt(_,lista)->List.fold_left (@) [] (List.map leaves lista);;
   

let rec mirror=function
  Gt(x,[])->Gt(x,[])
  |Gt(x,lista)->Gt(x,List.map mirror (List.rev lista));; 
  
  
let rec preorder=function
  Gt(x,[])->[x]
  |Gt(x,lista)->List.fold_left (@) [x] (List.map preorder lista);;
  
  
let rec postorder=function
  Gt(x,[])->[x]
  |Gt(x,lista)->List.fold_right (@) (List.map postorder lista)[x];;  
  
 
 

