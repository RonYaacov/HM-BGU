(* hw1.ml
 * Handling infix expressions with percents:
 *
 *   x + y %
 *   x - y %
 *   x * y %
 *
 * Programmer: Mayer Goldberg, 2024
 *)

#use "pc.ml";;

type binop = Add | Sub | Mul | Div | Mod | Pow | AddPer | SubPer | PerOf;;

type expr =
  | Num of int
  | Var of string
  | BinOp of binop * expr * expr
  | Deref of expr * expr
  | Call of expr * expr list;;

type args_or_index = Args of expr list | Index of expr;;



module type INFIX_PARSER = sig
  val nt_expr : expr PC.parser
end;; (* module type INFIX_PARSER *)

module InfixParser : INFIX_PARSER = struct
open PC;;

let nt_digit = const (fun ch -> '0' <= ch && ch <= '9');;

let nt_lower_case = const (fun ch -> 'a' <= ch && ch <= 'z');;

let nt_upper_case = const (fun ch -> 'A' <= ch && ch <= 'Z');;

let nt_plus = const (fun ch -> ch = '+');;

let nt_minus = const (fun ch -> ch = '-');;

let nt_times = const (fun ch -> ch = '*');;

let nt_div = const (fun ch -> ch = '/');;

let nt_mod = const (fun ch -> ch = '%');;

let nt_pow = const (fun ch -> ch = '^');;

let nt_lparen = const (fun ch -> ch = '(');;

let nt_rparen = const (fun ch -> ch = ')');;

let nt_lbracket = const (fun ch -> ch = '[');;

let nt_rbracket = const (fun ch -> ch = ']');;

let nt_comma = const (fun ch -> ch = ',');;

let nt_colon = const (fun ch -> ch = ':');;

let nt_semicolon = const (fun ch -> ch = ';');;

let nt_eq = const (fun ch -> ch = '=');;

let nt_per = const (fun ch -> ch = '%');;

let nt_add = caten nt_plus nt_per;;

let nt_sub = caten nt_minus nt_per;;


let maybeify nt none_value = 
  pack (maybe nt) (function
  | None -> none_value 
  | Some x -> x);;

  

  let nt_var = 
    let nt1 = range_ci 'a' 'z' in
    let nt2 = range '0' '9' in
    let nt3 = disj nt1 nt2 in
    let nt2 = range '_' '_' in
    let nt3 = disj nt3 nt2 in
    let nt2 = range '$' '$' in
    let nt3 = disj nt3 nt2 in
    let nt1 =  caten nt1 (star nt3) in
  let nt1 = pack nt1 (fun (c1, cs) -> string_of_list (c1 :: cs)) in
  let nt1 = pack nt1 (fun name -> Var name) in
  nt1;; 


let nt_add_per = caten nt_plus nt_per;;


let nt_sub_per = caten nt_minus nt_per;;
 
let int_of_digit_char = 
  let delta = int_of_char '0' in
  fun c -> int_of_char c - delta;;

let nt_optional_is_pus = 
  let nt1 = pack (char '+') (fun _ -> true) in
  let nt2 = pack (char '-') (fun _ -> false) in
  let nt1 = maybeify(disj nt1 nt2) true in
  nt1;;
  
let nt_digit_0_9 = pack( range '0' '9') int_of_digit_char;;
let nt_int  = 
  let nt1 = pack (plus nt_digit)
  (fun digits -> List.fold_left(
    fun number digit -> 10 * number + (int_of_digit_char digit)) 0 digits) in
    let nt1 = caten nt_optional_is_pus nt1 in
    let nt1 = pack nt1 (fun (is_pos, num) -> if is_pos then (num) else (-num)) in
    nt1;;

let nt_number = pack nt_int (fun num -> num);;

let nt_num_var = 
  let nt1 = pack nt_number (fun num -> Num num) in
  disj nt_var nt1;;

let nt_dec = 
  let nt1 = nt_var in
  let nt2 = caten nt1 (char '[')in
  let nt3 = caten nt2 nt_num_var in
  let nt4 = caten nt3 (char ']')in
  pack nt4 (fun (((var,_), x),_) -> Deref(var, x));;

let nt_neg = 
  let nt1  = (char '(')in 
  let nt1 = caten nt1 (char '-') in
  let nt1 = caten nt1 nt_num_var in
  let nt1 = caten nt1 (char ')') in
  pack nt1 (fun (((_, _),num), _) -> BinOp(Sub, Num 0, num));;

let nt_invert = 
  let nt1  = (char '(')in 
  let nt1 = caten nt1 (char '/') in
  let nt1 = caten nt1 nt_num_var in
  let nt1 = caten nt1 (char ')') in
  pack nt1 (fun (((_, _),num), _) -> BinOp(Div, Num 1, num));;

let string_of_char c = String.make 1 c;;



let string_of_binop = function
| Add -> "+"
| Sub -> "-"
| Mul -> "*"
| Div -> "/"
| Mod -> "mod"
| Pow -> "^"
| AddPer -> "+%"
| SubPer -> "-%"
| PerOf -> "%of";;


let rec string_of_expr = function
| Num n -> string_of_int n
| Var v -> v
| BinOp (op, e1, e2) ->
  let op_str = match op with
  | Add -> "+"
  | Sub -> "-"
  | Mul -> "*"
  | Div -> "/"
  | Mod -> "mod"
  | Pow -> "^"
  | AddPer -> "+%"
  | SubPer -> "-%"
  | PerOf -> "%of"
  in
  "(" ^ string_of_expr e1 ^ " " ^ op_str ^ " " ^ string_of_expr e2 ^ ")"
  | Deref (e1, e2) -> string_of_expr e1 ^ "[" ^ string_of_expr e2 ^ "]"
  | Call (e, args) ->
    string_of_expr e ^ "(" ^ String.concat ", " (List.map string_of_expr args) ^ ")"
    
            
let rec string_of_binop_exprlst = function
      | [] -> ""
      | [(binop, expr)] -> string_of_binop binop ^ " " ^ string_of_expr expr
      | (binop, expr) :: rest -> string_of_binop binop ^ " " ^ string_of_expr expr ^ "; " ^ string_of_binop_exprlst rest;;
let make_nt_spaced_out nt = 
  let nt1 = star nt_whitespace in
  let nt1 = pack (caten nt1 (caten nt nt1)) (fun (_, (e, _)) -> e) in
  nt1;;


let nt_arg =
  let nt1 = nt_num_var in
  let nt2 = star (caten (make_nt_spaced_out (char ',')) nt_num_var) in
  let nt1 = caten nt1 nt2 in
  let nt1 = pack nt1 (fun (arg, args) -> arg :: List.map (fun (_, arg) -> arg) args) in
  let nt1 = disj nt1 (pack nt_epsilon (fun _ -> [])) in
  pack nt1 (fun args -> args);;

(*  *)

let nt_call = 
  let nt1 = nt_var in
  let nt2 = caten nt1 (char '(') in
  let nt3 = caten nt2 (nt_arg) in
  let nt4 = caten nt3 (char ')') in
  pack nt4 (fun (((var, _), args), _) -> Call(var, args));;


let make_nt_paren lparen rparen nt = 
  let nt1 = make_nt_spaced_out (char lparen) in
  let nt2 = make_nt_spaced_out (char rparen) in
  let nt1 = caten nt1 (caten nt nt2) in
  let nt1 = pack nt1 (fun (_, (e, _)) -> e) in
  nt1;;

let rec reverse_list lst =
  match lst with
  | [] -> []
  | hd :: tl -> (reverse_list tl) @ [hd];;

(* ((expr*char)*int)*char *)


let rec nt_expr str = nt_expr0 str
  and nt_expr0 str = 
    let nt1 = pack (char '+') (fun _ -> Add) in
    let nt2 = pack (char '-') (fun _ -> Sub) in
    let nt1 = disj nt1 nt2 in
    let nt1 = star (caten nt1 nt_expr1) in
    let nt1 = pack (caten nt_expr1 nt1) (fun (expr1, binop_exprlst) -> 
      List.fold_left (fun expr1 (binop, expr1') -> BinOp(binop, expr1, expr1')) expr1 binop_exprlst) in
    let nt1 = make_nt_spaced_out nt1 in
    nt1 str

  and nt_expr1 str = 
    let nt1 = pack (char '*') (fun _ -> Mul) in
    let nt2 = pack (char '/') (fun _ -> Div) in
    let nt3 = pack (word "mod") (fun _ -> Mod) in
    let nt1 = disj_list [nt1; nt2; nt3] in
    let nt1 = star (caten nt1 nt_expr2) in 
    let nt1  = pack (caten nt_expr2 nt1) (fun (expr2, binop_exprlst) -> 
      List.fold_left (fun expr2 (binop, expr2') -> BinOp(binop, expr2, expr2')) expr2 binop_exprlst) in
    let nt1 = make_nt_spaced_out nt1 in
    nt1 str
  and nt_expr2 str = 
      let nt_expo = pack (char '^') (fun _ -> Pow) in
      let nt1 = pack (caten nt_expr3 nt_expo) (fun (x, _) -> x) in
      let nt1 = star nt1 in
      let nt1 = caten nt1 nt_expr3 in
      let nt1 = pack nt1 (fun (es, e) -> List.fold_right (fun curr acc -> BinOp (Pow, curr,acc))es e)in
      let nt1 = make_nt_spaced_out nt1 in
      nt1 str
      
  and nt_expr3 str = 
    let nt1 = pack nt_number (fun num -> Num num) in
    let nt1 = disj_list [
      nt1;
      nt_call;  
      nt_dec;
      nt_var;
      nt_invert;
      nt_neg;
      nt_paren] in
    let nt1 = make_nt_spaced_out nt1 in
    nt1 str

  and nt_paren str = 
    disj_list [make_nt_paren '(' ')' nt_expr;
              make_nt_paren '[' ']' nt_expr;
              make_nt_paren '{' '}' nt_expr  
    ] str

  


  end;; (* module InfixParser *)

open InfixParser;;
