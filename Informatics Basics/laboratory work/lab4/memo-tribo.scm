;(define memo-factorial
;  (let ((memo '() ))
;    (lambda (n)
;      (let ((pair (assoc n memo)))
;        (if pair
;            (cdr pair)
;            (let ((n-value
;                   (if (< n 2)
;                       1
;                       (* (memo-factorial (- n 1)) n))))
;              (set! memo (cons (cons n n-value) memo)) n-value))))))


(define memo-tribo
  (let ((memo '() ))
    (lambda (n)
      (let ((pair (assoc n memo)))
        (if pair
            (cdr pair)
            (let ((n-value (if (< n 2)
                               0
                               (if (= n 2)
                                   1
                                   (+ (memo-tribo (- n 1))
                                      (memo-tribo (- n 2))
                                      (memo-tribo (- n 3)))))))
              (set! memo (cons (cons n n-value) memo))
              n-value))))))
