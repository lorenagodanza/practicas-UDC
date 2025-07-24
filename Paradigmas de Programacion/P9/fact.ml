let rec fact = function
    0 -> 1
  | n -> n * fact (n - 1)
;;


let mensaje= if (Array.length Sys.argv)=2
  then 
    try string_of_int (fact (int_of_string(Sys.argv.(1)))) with
      |_->"fact: argumento inválido"
  else ("fact: número de argumentos inválido")
in print_endline mensaje
;;


