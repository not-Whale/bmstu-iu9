(define-syntax trace-ex
  (syntax-rules ()
    ((_ expr)
     (begin
       (display 'expr)
       (display " => ")
       (display expr)
       (newline)
       expr))))

(define (zip . xss)
  (if (or (null? xss)
          (null? (trace-ex (car xss))))
      '()
      (cons (map car xss)
            (apply zip (map cdr (trace-ex xss))))))