(define-syntax lazy-cons
  (syntax-rules ()
    ((lazy-cons first second) (cons first (delay second)))))

(define lazy-car car)

(define (lazy-cdr p)
  (force (cdr p)))

;=============================================================;

(define (lazy-head xs k)
  (if (zero? (- k 1))
      (cons (lazy-car xs) '())
      (cons (car xs) (lazy-head (lazy-cdr xs) (- k 1)))))
      
(define (lazy-ref xs k)
  (if (zero? k)
      (lazy-car xs)
      (lazy-ref (lazy-cdr xs) (- k 1))))

;=============================================================;

(define (generate-natural start)
  (define (next num)
    (lazy-cons num (next (+ num 1))))
  (next start))

(define (naturals num)
  (generate-natural num))

;=============================================================;

(define (generate-factorial)
  (define (next n n!)
    (define n+1 (+ n 1))
    (lazy-cons n! (next n+1 (* n! n+1))))
  (next 0 1))

(define factorial (generate-factorial))

;=============================================================;

(define-syntax show
  (syntax-rules ()
    ((_ expr)
     (begin
       (display 'expr)
       (display " => ")
       expr))))

(show (lazy-ref factorial 4))
(show (lazy-ref factorial 5))
(show (lazy-ref factorial 6))
(show (display (lazy-head (naturals 1) 10)))
(newline)
(show (display (lazy-head (naturals 15) 6)))
(newline)
(show (display (lazy-head (naturals 101) 1)))

;=============================================================;
