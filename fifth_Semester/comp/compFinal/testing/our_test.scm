(define free_var 2)

(define tail_lambda
    (lambda (x f) (f x))
)
(define arg_lambda
    (lambda (x) x)
)

(define free_var_lambda (lambda () free_var))

(tail_lambda 5 arg_lambda)

(define test_+ (lambda (x y) (+ x y)))

(define function_for_map  (lambda (x) x ))
(map function_for_map '(1 2 3))
(apply list '(1 2 3 4))
(or #t #f)
(+ 9 1)
(or)

