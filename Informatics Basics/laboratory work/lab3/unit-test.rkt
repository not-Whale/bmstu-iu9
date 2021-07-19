(define-syntax test
  (syntax-rules ()
    ((_ expression result)
     (list (quote expression) result))))

(define (run-test the-test)
  (let ((expression (car the-test))
        (expected (cadr the-test)))
    (write expression)
    (let* ((value (eval expression
                        (interaction-environment)))
           (status (equal? value expected)))
      (if status
          (display "  ok\n")
          (begin
            (display "  FAIL\n")
            (display "     Result:")
            (write value)
            (newline)
            (display "     Expected:")
            (write expected)
            (newline)))
      status)))

(define (and-fold result xs)
  (if (null? xs)
      result
      (and-fold (and result (car xs)) (cdr xs))))

(define (run-tests the-tests)
  (and-fold #t (map run-test the-tests)))

#| (define tests1
    (list (test (* 2 2) 4)
          (test (* 3 3) 10.5)
          (test (/ 1 0) 22)
          (test (+ 1 1) 2))) 

(define tests2
    (list (test (* 2 2) 4)
          (test (* 3 3) 10.5)
          (test (/ 1 10) 0.1)
          (test (+ 1 1) 2)))

(define tests3
    (list (test (* 2 2) 4)
          (test (* 3 3) 10.5)
          (test (/ 1 10) 1/10)
          (test (+ 1 1) 2)))

(define tests4
    (list (test (* 2 2) 4)
          (test (* 3 3) 9)
          (test (/ 1 10) 1/10)
          (test (+ 1 1) 2)))      |#