(define (delete x xs)
  (define (iter ys)
    (if (null? ys)
        '()
        (if (equal? x (car ys))
            (iter (cdr ys))
            (cons (car ys) (iter (cdr ys))))))
  (iter xs))

(define (list->set xs)
  (define (iter ys)
    (if (null? ys)
        '()
        (cons (car ys) (iter (delete (car ys) ys)))))
  (iter xs))

(define (set? xs)
  (equal? xs (list->set xs)))

(define (union xs ys)
  (list->set (append xs ys)))

(define (plus x xs)
  (define (iter r count ys)
    (if (null? ys)
        (if (> count 0)
            r
            '())
        (if (equal? x (car ys))
            (iter x (+ count 1) (cdr ys))
            (iter x count (cdr ys)))))
  (iter x 0 xs))

(define (intersection xs ys)
  (define (iter ss)
    (if (null? ss)
        '()
        (if (null? (plus (car ss) xs))
            (iter (cdr ss))
            (cons (plus (car ss) xs) (iter (cdr ss))))))
  (iter ys))

(define (difference xs ys)
  (define (iter cs ss)
    (if (null? ss)
        cs
        (iter (delete (car ss) cs) (cdr ss))))
  (iter xs ys))

(define (symmetric-difference xs ys)
  (difference (union xs ys) (intersection xs ys)))

(define (set-eq? xs ys)
  (equal? (union xs ys) xs))

(list->set '(1 1 2 3))

(set? '(1 2 3))
(set? '(1 2 3 3))

(union '(1 2 3) '(2 3 4))

(intersection '(1 2 3) '(2 3 4))

(difference '(1 2 3 4 5) '(2 3))

(symmetric-difference '(1 2 3 4) '(3 4 5 6))

(set-eq? '(1 2 3) '(3 2 1))
(set-eq? '(1 2) '(1 3))
