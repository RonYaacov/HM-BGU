(define free_var 2)

(define tail_lambda
    (lambda (x f) (f x))
)
(define arg_lambda
    (lambda (x) x)
)

(define free_var_lambda (lambda () free_var))

(tail_lambda 5 arg_lambda)

(+ 6 3)

