(define (my-odd x)
  (= (remainder x 2) 1))

(define (my-even x)
  (= (remainder x 2) 0))

(my-odd 10)
(my-odd 11)
(my-even 10)
(my-even 11)