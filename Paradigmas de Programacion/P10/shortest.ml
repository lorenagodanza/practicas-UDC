let trees =
[(1,1); (1,3); (1,6); (1,11); (1,12); (1,15); (1,16); (2,1); (2,15); (3,6);
(3,7); (3,9); (3,12); (4,3); (4,12); (4,15); (5,1); (6,3); (6,7); (6,9);
(6,13); (6,14); (6,16); (7,3); (7,5); (7,16); (8,10); (8,11); (8,13);
(8,16); (9,1); (9,3); (9,13); (10,6); (10,9); (10,10); (11,16); (12,1);
(12,4); (12,6); (12,13); (13,11); (13,13); (14,1); (14,7); (14,9); (15,2);
(15,4); (15,6); (15,7); (15,13); (15,16); (16,4); (16,11); (16,13);
(16,16)];;

let get_valores (i,j) trees n=
   let rec aux pos sol=
      if(pos=List.length trees) then sol
      else 
         let (a,b)=(List.nth trees pos) in 
           if(a,b)<>(i,j) && (((abs(a-i)<=n && b=j)|| (a=i && abs (b-j)<=n))) then aux (pos+1) ((a,b)::sol)
           else aux (pos+1) sol
         in aux 0 [];;
         
let eliminar_visitados visitados lista=
   let rec aux l sol= match l with 
       []->sol
       |h::t->if  List.mem h visitados then aux t sol
              else aux t (h::sol)
    in aux lista [];;


let get_posibilidades  visitados trees (i,j) n=
   eliminar_visitados visitados (get_valores (i,j) trees n);;
   

let shortest_tour m n trees salto=
  let rec aux pila=
    if pila=[] then raise Not_found 
    else 
      let visitados, (x,y) = List.hd pila, List.hd (List.hd pila) in if (x,y) =(m,n) then List.rev visitados
      else let posibilidades=get_posibilidades visitados trees (x,y) salto in 
          let pila2=(List.map(fun x->x::visitados) posibilidades)@(List.tl pila) in
             aux (List.sort (fun x y -> (List.length x)-(List.length y)) pila2)
             in aux [[(1,1)]];;
