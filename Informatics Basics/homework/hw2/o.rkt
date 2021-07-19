(define (f x) (* x 2))
(define (g x) (* x 3))
(define (h x) (- x))

(define (o . fs)
  (lambda (x)
    (define (iter r xs)
      (if (null? xs)
          r
          (iter ((car xs) r) (cdr xs))))
    (iter x (reverse fs))))

((o f g h) 1)
((o f g) 1)
((o h) 1)
((o) 1)
