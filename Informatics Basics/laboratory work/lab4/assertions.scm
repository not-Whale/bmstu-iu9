;(define (mapp xs)
;  (call-with-current-continuation
;   (lambda (escape)
;     (map (lambda (x)
;            (if (zero? x)
;                (escape #f)
;                (/ 1 x))) xs))))


(define start-environment 1)

(define (use-assertions)
  (call-with-current-continuation
   (lambda (c)
     (set! start-environment c))))

(use-assertions)

(define-syntax assert
  (syntax-rules ()
    ((assert cond)
     (if (not cond)
         (begin
           (display "FAILED: ")
           (display (quote cond))
           (start-environment)
           )))))

(define (1/x x)
  (assert (not (zero? x)))
  (/ 1 x))

(map 1/x '(1 2 3 5 7))
(map 1/x '(-2 -1 0 1 2))
