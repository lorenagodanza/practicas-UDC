let n = int_of_string(Sys.argv.(1));;

let is_prime m = 
      let rec check_from i =
            i>= m ||
            (m mod i <> 0 && check_from (i+1))
      in check_from 2;;
           
let rec next_prime n= 
      if (is_prime (n+1)) then n+1
      else next_prime(n+1);;
      
let rec last_prime_to n = 
      if (is_prime n) then n
      else last_prime_to(n-1);;
      
let is_prime2 m = 
      let rec check_from i =
            float_of_int(i)>= sqrt(float_of_int(m)) ||
            (m mod i <> 0 && check_from (i+1))
      in check_from 2;; 
      
(*print_string("next prime = ");
print_int(next_prime(n+1));
print_endline("");
print_string("last prime = ");
print_int(last_prime_to n);
print_endline("")*)
