
open G_tree;;

let rec breadth_first = function
    Gt (x, []) -> [x]
  | Gt (x, (Gt (y, t2))::t1) -> x :: breadth_first (Gt (y, t1@t2));;

let breadth_first_t gt =
   let rec aux a lista = match a with
      Gt(x,[])->List.rev(x::lista)
      |Gt(x,(Gt(y,t2))::t1)-> aux (Gt (y,List.rev_append (List.rev t1) t2)) (x::lista)
   in aux gt [];;   
      
                    
let t2 = let rec hacer t n=
  if n<=0 then t else hacer (Gt(n,[t])) (n-1)
  in hacer (Gt(300000,[]))300000;;

