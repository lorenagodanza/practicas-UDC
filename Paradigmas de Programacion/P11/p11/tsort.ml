
open Bin_tree;;

let rec insert_tree ord x = function
  Empty->Node(x,Empty,Empty)
  |Node(a,izq,dch)->if ord x a then Node(a,insert_tree ord x izq,dch)
                               else Node(a,izq,insert_tree ord x dch);;


let tsort ord l =
  inorder (List.fold_left (fun a x -> insert_tree ord x a) Empty l)
;;
