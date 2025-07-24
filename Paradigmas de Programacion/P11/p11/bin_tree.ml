
type 'a bin_tree =
    Empty
  | Node of 'a * 'a bin_tree * 'a bin_tree
;;

let rec map_tree f = function
  Empty->Empty
  |Node(x,l,r)->Node(f x,map_tree f l, map_tree f r);;

let rec fold_tree f a = function
    Empty -> a
  | Node (x, l, r) -> f x (fold_tree f a l) (fold_tree f a r)
;;

let sum t =
   let aux a b c = a+b+c in
   fold_tree aux 0 t;;

let prod t =  
   let aux a b c = a*.b*.c in
   fold_tree aux 1.0 t;;

let size t = 
  let aux a b c=1+b+c in
  fold_tree aux 0 t;;

let height t = 
  let aux x hijoIzq hijoDcho= 1+(max hijoIzq hijoDcho) in
  fold_tree aux 0 t;;

let inorder t = 
   fold_tree (fun a b c->b@[a]@c) [] t;;

let mirror t = fold_tree(fun a b c -> Node(a,c,b)) Empty t;;

