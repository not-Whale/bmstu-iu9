;(define x 5)

;(let ((y (delay (/ 1 x))))
;  (and (not (zero? x)) (force y)))

(define-syntax my-if
  (syntax-rules ()
    ((my-if cond arg1 arg2)
     (let ((x (delay arg1))
           (y (delay arg2)))
       (or
        (and cond (force x))
        (and (not cond) (force y)))))))
