
let n = int_of_string(Sys.argv.(1));;


let rec fib n= 
      if n<2 then n
      else 
            fib(n-1) + fib(n-2) 

let rec mostrar m= 
      if m <=n then begin
       (print_int(fib m); print_endline(""); mostrar (m +1))
       end
      else ();;

mostrar 0;



