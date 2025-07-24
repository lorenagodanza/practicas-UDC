(*Lorena Godón Danza 
Grupo 3.1 *)

(*1*)
let to0from n=
	let rec aux n acum=
		if n<0 then List.rev acum
		else aux(n-1) (n::acum)
	in aux n [];;	
	
(*2*)
let fromto m n=
	let rec aux m n acum=
		if m>n then acum
		else aux (m) (n-1) (n::acum)
	in aux m n [];;	

(*3*)	
let incseg l=
	let rec aux l sol=match l with
		[]->List.rev sol
		|h1::t->aux t ((h1+(List.hd sol))::sol)
	in aux (List.tl l) [List.hd l];;	
	

(*4*)
let rec append l1 l2=match (l1,l2) with
	([],l2)->l2
	|(h::t,l2)->h::(append t l2);;
 

let remove elemento lista=
	let rec aux elemento sol lista = match lista with
		[]->List.rev sol
		|h::t->if(elemento=h) then append (List.rev sol) t
			else (aux elemento (h::sol) t)
	in aux elemento [] lista;;		

(*5*)	
let compress lista=
	let rec aux lista sol=match lista with
		h1::h2::t->if h1=h2 then aux (h2::t)sol
				else aux (h2::t)(h1::sol)
		|_->append lista sol
	in aux (List.rev lista)[];;				
