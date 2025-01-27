(define free_var 2)

(define tail_lambda
    (lambda (x f) (f x))
)
(define arg_lambda
    (lambda (x) x)
)

(define free_var_lambda (lambda () free_var))

(define free_fact (letrec ((factorial 
          (lambda (n)
            (if (= n 0)
                1
                (* n (factorial (- n 1)))))))
  (factorial 5)))