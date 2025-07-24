(*Lorena Godón Danza 3.1 *)

(*REMOVE*)
let rec remove elemento lista= match lista with
	[]->[]
	|h::t->if(elemento=h) then t
	else h::(remove elemento t);;

(*REMOVE_ALL*)
let rec remove_all elemento lista =match lista with
	[]->[]
	|h::t->if(elemento=h) then remove_all elemento t
	else h::(remove_all elemento t);;

(*LDIF*)
let rec ldif l1 l2=match l2 with
[]->l1
|h::t->ldif(remove_all h l1)t;;

(*LPROD*)
let lprod l1 l2=
	let rec aux l1 l2 sol=match (l1,l2) with
		([],_)|(_,[])->sol
		|(h::[],h2::t2)->aux[h] t2 ([(h,h2)]@sol)
		|(h1::t1,t2)->aux t1 t2 ((aux [h1] t2 sol))
		in aux l1 l2 [];;

(*DIVIDE*)
let rec divide lista=
	let rec sp lista l1 l2= match lista with
	[]->(List.rev l1,List.rev l2)
	|(x::y::t)->sp t(x::l1)(y::l2)
	|x::[]->(List.rev(x::l1),List.rev l2)
	in sp lista [][];;
	
	
	
	
