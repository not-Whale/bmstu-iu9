(define (my-gcd a b)
  (define (iter a b)
    (if (= a b)
        a
        (if (> a b) (iter (- a b) b) (iter (- b a) a))))
  (iter a b))

(define (my-lcm a b)
  (/ (* a b) (my-gcd a b)))

(define (prime? x)
  (define (iter a b)
    (if (= a 0)
        (= b 1)
        (if (= (remainder x a) 0)
            (iter (- a 1) (+ b 1))
            (iter (- a 1) b))))
  (iter (- x 1) 0))

(my-gcd 3542 2464)
(my-lcm 3 4)
(prime? 11)
(prime? 12)
