let g1 n=
	if(n>=0) 
	then if (n mod 2=0) 
		then true 
		else (n mod 2=(-1)) 
	else false;;
	
	
let g2 n=(function true->(function true->true|false->(n mod 2=(-1)))(n mod 2=0)|false->false)(n>0);;
