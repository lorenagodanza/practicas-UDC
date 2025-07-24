let f n=
	if n mod 2=0 then n/2 
	else 3*n+1;;
	
(*FUNCION ORBIT *)
let rec orbit n=
	if n=1 then "1"
	else (string_of_int n)^" , "^ (orbit(f n));;	
	
(*FUNCION LENGTH *)	
let rec length n=
	if n=1 then 0
	else 1+length(f n);; 
	
(*FUNCIÓN TOP *)	
let rec top n=
	if(n=1) then 1
	else max n(top(f n));;
	
(*FUNCION LENGTH'N'TOP *)			
let rec length'n'top n=
	if(n=1) then (0,1)
	else let(cnt,maximo)=length'n'top(f n)
	in (cnt+1,max n maximo);;
	

(*FUNCION LONGEST *)	
let rec longest_in m n=
	let lm=length m in 
		if m=n then m,lm
		else let resto,lr=longest_in (m+1) n in 
			if(lm)>=(lr) then (m,lm) 
				else (resto,lr);;
			
(*FUNCION HIGHEST_IN *)				
let rec highest_in m n=
	let hm=top m in
		if m=n then m,hm
		else let resto,hr=highest_in (m+1) n in
			if(hm)>=(hr) then(m,hm)
			else (resto,hr);;			
						
						
													
