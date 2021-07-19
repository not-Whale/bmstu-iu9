(define (my-range a b d)
  (define (iter count)
    (if (>= count b)
        '()
        (cons count (iter (+ count d)))))
  (iter a))

(define (my-flatten xs)
  (if (null? xs)
      '()
      (if (pair? xs)
          (append (my-flatten (car xs)) (my-flatten (cdr xs)))
          (list xs))))

(define (my-element? x xs)
  (define (iter ys count)
    (if (null? ys)
        (> count 0)
        (if (equal? (car ys) x)
            (iter (cdr ys) (+ count 1))
            (iter (cdr ys) count))))
  (iter xs 0))

(define (my-filter pred? xs)
  (define (iter ys)
    (if (null? ys)
        '()
        (if (pred? (car ys))
            (cons (car ys) (iter (cdr ys)))
            (iter (cdr ys)))))
  (iter xs))

(define (my-fold-left op xs)
  (define (iter ys r)
    (if (null? (cdr ys))
        r
        (iter (cdr ys) (op r (car (cdr ys))))))
  (iter xs (car xs)))

(define (my-fold-right op xs)
  (define (iter ys r)
    (if (null? (cdr ys))
        r
        (iter (cdr ys) (op (car (cdr ys)) r))))
  (iter (reverse xs) (car (reverse xs))))

(my-range 0 11 3)
(my-range 1 15 4)
(my-range 2 8 1)
(my-range 5 8 2)

(my-flatten '((1) 2 (3 (4 5)) 6))

(my-element? 1 '(3 2 1))
(my-element? 4 '(3 2 1))

(my-filter odd? (my-range 0 10 1))
(my-filter (lambda (x) (= (remainder x 3) 0)) (my-range 0 13 1))

(my-fold-left quotient '(16 2 2 2 2))
(my-fold-left * '(2 2 2))
(my-fold-left / '(16 2 2))

(my-fold-right expt '(2 3 4))