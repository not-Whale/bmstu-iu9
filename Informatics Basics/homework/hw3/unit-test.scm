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
            (newline)
            (newline)))
      status)))

(define (and-fold result xs)
  (if (null? xs)
      result
      (and-fold (and result (car xs)) (cdr xs))))

(define (run-tests the-tests)
  (and-fold #t (map run-test the-tests)))
