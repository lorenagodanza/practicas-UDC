
(*HD*)
let hd lista=match lista with 
	[]->raise(Failure "hd")
	|(h::_)->h;; 
	
(*TL*)

let tl lista=match lista with 
	[]->raise(Failure "tl")
	|(_::t)->t;;
	
(*LENGTH*)
let rec length lista=match lista with
	[]->0
	|_::t->1+length t;;	
	
(*COMPARE_LENGTHS*)
let rec compare_lengths l1 l2 =match (l1,l2) with 
	([],[])->0
	|([],_)->(-1)		
	|(_,[])->1
	|(_::t1,_::t2)->compare_lengths t1 t2;;
	
(*NTH*)
let nth lista n=
	if (n<0) then raise(Invalid_argument "nth")
	else let rec aux=function
		([],_)->raise(Failure "nth")
		|(h::_,0)->h
		|(h::t,n)->aux(t,n-1)
	in aux(lista,n);;	
	 
	 
(*APPEND*)

let rec append l1 l2 =match (l1,l2) with
	([],l2)->l2
	|(h::t,l2)->h::(append t l2);;
	
(*FIND*)

let rec find funcionBooleana lista=match lista with
	[]->raise(Not_found)
	|h::t->if funcionBooleana h then h
	else (find funcionBooleana t);; 
	

(*FOR_ALL*)
let rec for_all funcionBooleana lista=match lista with
	[]->true
	|h::t->(funcionBooleana h)&&(for_all funcionBooleana t);;
	

(*EXISTS*)
let rec exists funcionBooleana lista=match lista with
 	[]->false  
  	|h::t->if funcionBooleana h then true
  	else(exists funcionBooleana t);;

(*MEM*)

let rec mem elemento lista=match lista with
  	[]->false
  	|h::t->if elemento=h then true
  	else(mem elemento t);;
  	
(*FILTER*)
let rec filter funcionCondicion lista=match lista with
  	[]->[]
  	|h::t->if funcionCondicion h then h::filter funcionCondicion t
  	else filter funcionCondicion t;;  

(*FIND_ALL*)
let find_all=filter;;
	
(*PARTITION *)
	  
let partition f1 l=
	let f2 h=not(f1 h)
	in (filter f1 l, filter f2 l);;

(*SPLIT*)

let rec split lista=match lista with
	[]->([],[])
	| h::t -> (fst(h):: (fst(split t)) ,snd(h):: (snd(split t)));;
  
  
 (*COMBINE *) 
let rec combine l1 l2 =match (l1,l2) with
  ([],[])->[]
  |(h1::t1,h2::t2)->(h1,h2)::combine t1 t2
  |(_,_)->raise(Invalid_argument "combine");;


(*INIT*)
let init x f=
	if(x<0) then invalid_arg"init"
	else let rec aux x f sol=
		if x=1 then (f 0)::sol
		else aux (x-1) f ((f(x-1))::sol)
		in aux x f [];;		
		
(*REV*)

let rev lista=
	let rec aux acc= function
		[]->acc
		|h::t->aux(h::acc)t in aux[] lista;;	
		
(*REV_APPEND*)
let rec rev_append l1 l2=match(l1,l2) with
	([],l2)->l2
	|(h::t,l2)->rev_append t(h::l2)
	
(*CONCAT*)
let rec concat= function
	[]->[]
	|h::t->append h (concat t);;
				
				
(*FLATTEN*)
let flatten=concat;;

(*MAP*)
let rec map funcion lista=match lista with
	[]->[]
	|h::t->(funcion h)::(map funcion t);;
	
(*REV_MAP*)	
let rev_map f l=rev(map f l);;
	
	
(*MAP2*)
let rec map2 funcion l1 l2 =match (l1,l2) with
	([],[])->[]
	|([],_)|(_,[])->raise(Invalid_argument "map2")
	|(h1::t1,h2::t2)->(funcion h1 h2)::(map2 funcion t1 t2);;
	
(*FOLD_LEFT*)	
	
let rec fold_left funcion inicial lista=match lista with 
 	[]->inicial                                              
  	|h::t->fold_left funcion(funcion inicial h)t;;      
	
(*FOLD_RIGHT*)
let rec fold_right funcion lista inicial=match lista with 
 	[]->inicial
  	|h::t->funcion h (fold_right funcion t inicial);;
			
	
	
	
	
	
	
	
	
	
	
	
			
		 
