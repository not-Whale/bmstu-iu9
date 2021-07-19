(define (count x xs)
  (define (iter ys cnt)
    (if (null? ys)
        cnt
        (if (equal? x (car ys))
            (iter (cdr ys) (+ cnt 1))
            (iter (cdr ys) cnt))))
    (iter xs 0))

(count 'a '(a b c a))
(count 'b '(a c d))
(count 'a '())