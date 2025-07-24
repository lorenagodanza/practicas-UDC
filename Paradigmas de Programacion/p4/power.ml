let rec power x y=
	if y=1
	then x
	else x*power x(y-1);;
	
let rec power' x y=
	if y=1
	then x
	else if(y mod 2=0)
	then power'(x*x)(y/2)
	else x*power'(x*x)(y/2);;	
	
(* Es más eficiente power' porque requiere menos operaciones recursiva, ya que cada llamada divide entre 2 el exponente. Aún así, requiere hacer una múltiplicación y una división entera y una comprobación, de este modo, en exponentes muy pequeños no valdría la pena usar power' frente a power *)

let rec powerf x y=
	 if y=1
	 then x
	 else x*.powerf x(y-1);;	
