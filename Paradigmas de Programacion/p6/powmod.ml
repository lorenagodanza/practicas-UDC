(*Lorena Godón Danza 
GRUPO 3.1 *)

let rec power' x y=
	if y=1
	then x
	else if(y mod 2=0)
	then power'(x*x)(y/2)
	else x*power'(x*x)(y/2);;
	
let powmod m b e = power' b e mod m;;

let rec powmod m b e=
	if e=0 then 1
	else if e=1 then b mod m
	else 
		if e mod 2=0 then (powmod m b (e/2)*(powmod m b(e/2)))mod m
		else (powmod m b (e/2)*(powmod m b ((e/2)+1))mod m);;




