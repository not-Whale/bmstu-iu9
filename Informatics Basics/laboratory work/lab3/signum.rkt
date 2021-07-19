(load "./unit-test.rkt")

(define (signum x)
  (cond
    ((< x 0) -1)
    ((= x 0)  1)
    (else     1)))

(define the-tests
  (list (test (signum -2) -1)
        (test (signum  0)  0)
        (test (signum  2)  1)))

(run-tests the-tests)