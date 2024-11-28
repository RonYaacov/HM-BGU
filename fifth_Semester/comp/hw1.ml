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

let maybeify nt none_value = 
  pack (maybe nt) (function
  | None -> none_value 
  | Some x -> x);;

  let nt_letter = range_ci 'a' 'z';;
  let nt_var = 
    let nt1 = nt_letter in
    let nt2 = range '0' '9' in
    let nt3 = disj nt1 nt2 in
    let nt2 = range '_' '_' in
    let nt3 = disj nt3 nt2 in
    let nt2 = range '$' '$' in
    let nt3 = disj nt3 nt2 in
    let nt1 =  caten nt1 (star nt3) in
  let nt1 = pack nt1 (fun (c1, cs) -> string_of_list (c1 :: cs)) in
  let nt1 = pack nt1 (fun name -> Var name) in
  let nt1 = diff nt1 (word "mod") in
  nt1;; 

  let make_nt_spaced_out nt = 
    let nt1 = star nt_whitespace in
    let nt1 = pack (caten nt1 (caten nt nt1)) (fun (_, (e, _)) -> e) in
    nt1;;
  
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

  
let make_nt_paren lparen rparen nt = 
  let nt1 = make_nt_spaced_out (char lparen) in
  let nt2 = make_nt_spaced_out (char rparen) in
  let nt1 = caten nt1 (caten nt nt2) in
  let nt1 = pack nt1 (fun (_, (e, _)) -> e) in
  nt1;;

let rec nt_expr str = nt_expr_add_sub str

  
  and nt_expr_add_sub str = 
    let nt1 = pack (char '+') (fun _ -> Add) in
    let nt2 = pack (char '-') (fun _ -> Sub) in
    let nt1 = disj nt1 nt2 in
    let nt1 = star (caten nt1 nt_expr_mul_div_mod) in
    let nt1 = pack (caten nt_expr_mul_div_mod nt1) (fun (expr1, binop_exprlst) -> 
      List.fold_left (fun expr1 (binop, expr1') -> BinOp(binop, expr1, expr1')) expr1 binop_exprlst) in
    let nt1 = make_nt_spaced_out nt1 in
    nt1 str

  and nt_expr_mul_div_mod str = 
    let nt1 = pack (char '*') (fun _ -> Mul) in
    let nt2 = pack (char '/') (fun _ -> Div) in
    let nt3 = pack (not_followed_by (word "mod")(nt_letter)) (fun _ -> Mod) in
    let nt1 = disj_list [nt1; nt2; nt3] in
    let nt1 = star (caten nt1 nt_expr_per) in 
    let nt1  = pack (caten nt_expr_per nt1) (fun (expr2, binop_exprlst) -> 
      List.fold_left (fun expr2 (binop, expr2') -> BinOp(binop, expr2, expr2')) expr2 binop_exprlst) in
    let nt1 = make_nt_spaced_out nt1 in
    nt1 str

  and nt_expr_per str = 
    let nt2 = pack (char '+') (fun _ -> AddPer) in
    let nt3 = pack (char '-') (fun _ -> SubPer) in
    let nt1 = pack (char '*') (fun _ -> PerOf) in
    let nt1 = disj_list [nt1; nt2; nt3] in
    let nt2 = caten nt_expr_pow (char '%') in
    let nt2 = star (caten (make_nt_spaced_out nt1) nt2) in 
    let nt2  = pack (caten nt_expr_pow nt2) (fun (expr3, binop_exprlst) -> 
      List.fold_left (fun expr3 (binop, (expr3', _)) -> BinOp(binop, expr3, expr3')) expr3 binop_exprlst) in
    nt2 str
    
  and nt_expr_pow str = 
    let nt_expo = pack (char '^') (fun _ -> Pow) in
    let nt1 = pack (caten nt_expr_call_der nt_expo) (fun (x, _) -> x) in
    let nt1 = star nt1 in
    let nt1 = caten nt1 nt_expr_call_der in
    let nt1 = pack nt1 (fun (es, e) -> List.fold_right (fun curr acc -> BinOp (Pow, curr,acc))es e)in
    let nt1 = make_nt_spaced_out nt1 in
    nt1 str
  
  and nt_expr_call_der str = 
    let nt1 = disj nt_call nt_deref in
    let nt1 = caten nt_expr_last (star nt1) in
    let nt1 = pack nt1 (fun (rand, rator) -> List.fold_left (fun acc op -> op acc) rand rator) in
    let nt1 = make_nt_spaced_out nt1 in
    nt1 str
      
  and nt_expr_last str = 
    let nt1 = pack nt_number (fun num -> Num num) in
    let nt1 = disj_list [
      nt1;
      nt_var;
      nt_neg;
      nt_invert;
      nt_paren] in
    let nt1 = make_nt_spaced_out nt1 in
    nt1 str

    and nt_neg str=
      let nt1 = caten (char '-') nt_expr in
      let nt1 = make_nt_paren '('')' nt1 in
      let nt1 = pack nt1 (fun (_, var) -> BinOp(Sub, Num 0, var)) in
      nt1 str

    and nt_invert str=
      let nt1 = caten (char '/') nt_expr in
      let nt1 = make_nt_paren '('')' nt1 in
      let nt1 = pack nt1 (fun (_, var) -> BinOp(Div, Num 1, var)) in
      nt1 str

    and nt_deref str = 
      let nt1 =  make_nt_paren '['']' nt_expr in
      let nt1 = pack nt1 (fun second first -> Deref(first, second)) in
      nt1 str

    
    and nt_call str = 
      let nt1 = make_nt_paren '('')' nt_arg in
      let nt1 = pack nt1 (fun args name -> Call(name, args)) in
      nt1 str

    and nt_arg str =
      let nt1 = nt_expr in
      let nt2 = star (caten (make_nt_spaced_out (char ',')) nt_expr) in
      let nt1 = caten nt1 nt2 in
      let nt1 = pack nt1 (fun (arg, args) -> arg :: List.map (fun (_, arg) -> arg) args) in
      let nt1 = disj nt1 (pack nt_epsilon (fun _ -> [])) in
      pack nt1 (fun args -> args) str
    
  and nt_paren str = 
    disj_list [make_nt_paren '(' ')' nt_expr;
              make_nt_paren '[' ']' nt_expr;
              make_nt_paren '{' '}' nt_expr  
    ] str

    end;; (* module InfixParser *)

open InfixParser;;
