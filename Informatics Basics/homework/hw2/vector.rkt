(define (make-multi-vector sizes . fill)
  (define vec (make-vector (+ 1 (apply * sizes))
                           (if (null? fill)
                               fill
                               (car fill))))
  (vector-set! vec 0 (cons 'multivector sizes))
  vec)
                 
(define (multi-vector?  m)
  (and (vector?  m)
       (list? (vector-ref m 0))
       (equal? 'multivector (car (vector-ref m 0)))))

(define (get-index m indices)  
  (define (iter l ind) 
    (if (null? (cdr ind)) 
        (car ind) 
        (+ (* (car ind) (apply * (cdr l))) (iter (cdr l) (cdr ind))))) 
  (iter (list-tail (vector-ref m 0) 1) indices)) 

(define (multi-vector-ref m indices) 
  (vector-ref m (+ 1 (get-index m indices))))

(define (multi-vector-set!  m indices x)
  (vector-set!  m (+ 1 (get-index m indices)) x))



#| (define m (make-multi-vector '(11 12 9 16)))
(multi-vector? m)

(multi-vector-set! m '(10 7 6 12) 'test)
(multi-vector-ref m '(10 7 6 12))

(define m (make-multi-vector '(3 5 7) -1))
(multi-vector-ref m '(0 0 0)) |#

(define m (make-multi-vector '(3 4) -1))
(multi-vector-set! m '(1 2) 1)
(multi-vector-set! m '(2 1) 2)
(multi-vector-ref m '(1 2))
(multi-vector-ref m '(2 1))