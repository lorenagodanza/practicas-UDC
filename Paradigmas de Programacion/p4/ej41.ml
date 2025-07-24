let rec sum_cifras n=
	if n=0
	then 0
	else (n mod 10)+sum_cifras(n/10);;
let rec num_cifras n=
	if (abs n)<10
	then 1
	else 1+num_cifras((abs n)/10);;
let rec exp10 n= 
	if n=0
	then 1
	else 10*exp10(n-1);;
let rec reverse n=
	if n<10
	then n
	else ((n mod 10)*(exp10(num_cifras(n/10))))+reverse (n/10);;
let rec palindromo str=
	let len=String.length str in 
		 if len<=1 then true
		 else if str.[0]=str.[len-1]
		 	then palindromo(String.sub str 1(len-2))
		 	else false;;	
