;(load "./unit-test.scm")

(define (derivative expr)
  (or 
   (and (list? expr)
        (or
         (and (= (length expr) 4) (equal? (list-ref expr 0) '*) (der-f*g*h expr))
         (and (= (length expr) 3) (or (and (equal? (list-ref expr 0) '*) (der-f*g expr))
                                      (and (equal? (list-ref expr 0) '/) (der-f/g expr))
                                      (and (equal? (list-ref expr 0) '+) (der-f+g expr))
                                      (and (equal? (list-ref expr 0) '-) (der-f-g expr))
                                      (and (equal? (list-ref expr 0) 'expt) (number? (list-ref expr 2)) (der-x^n expr))
                                      (and (equal? (list-ref expr 0) 'expt) (number? (list-ref expr 1)) (der-a^x expr))))
         (and (= (length expr) 2) (or (and (equal? (list-ref expr 0) '-) (der-minus*x expr))
                                      (and (equal? (list-ref expr 0) 'exp) (der-e^x expr))
                                      (and (equal? (list-ref expr 0) 'sin) (der-sinx expr))
                                      (and (equal? (list-ref expr 0) 'cos) (der-cosx expr))
                                      (and (equal? (list-ref expr 0) 'tg) (der-tgx expr))
                                      (and (equal? (list-ref expr 0) 'ctg) (der-ctgx expr))
                                      (and (equal? (list-ref expr 0) 'log) (der-lnx expr))))))      
   (and (number? expr) (der-const expr))
   (and (symbol? expr) (der-x expr))))

(define (der-f*g*h expr)
  (define f (list-ref expr 1))
  (define g (list-ref expr 2))
  (define h (list-ref expr 3))
  `(+ (* ,(derivative f) ,g ,h) (* ,f ,(derivative g) ,h) (* ,f ,g ,(derivative h))))

(define (der-f*g expr)
  (define f (list-ref expr 1))
  (define g (list-ref expr 2))
  `(+ (* ,(derivative f) ,g) (* ,f ,(derivative g))))

(define (der-f/g expr)
  (define f (list-ref expr 1))
  (define g (list-ref expr 2))
  `(/ (- (* ,(derivative f) ,g) (* ,f ,(derivative g)))
      (expt ,g 2)))

(define (der-f+g expr)
  (define f (list-ref expr 1))
  (define g (list-ref expr 2))
  `(+ ,(derivative f) ,(derivative g)))

(define (der-f-g expr)
  (define f (list-ref expr 1))
  (define g (list-ref expr 2))
  `(- ,(derivative f) ,(derivative g)))

(define (der-x^n expr)
  (define x (list-ref expr 1))
  (define n (list-ref expr 2))
  `(* ,n (expt ,x ,(- n 1))))

(define (der-a^x expr)
  (define x (list-ref expr 2))
  (define a (list-ref expr 1))
  `(* (expt ,a ,x) (log ,a)))

(define (der-minus*x expr)
  (define x (list-ref expr 1))
  `(* (-1) ,(derivative x)))

(define (der-e^x expr)
  (define x (list-ref expr 1))
  `(* ,expr ,(derivative x)))

(define (der-sinx expr)
  (define x (list-ref expr 1))
  `(* (cos ,x) ,(derivative x)))

(define (der-cosx expr)
  (define x (list-ref expr 1))
  `(* (- (sin ,x)) ,(derivative x)))

(define (der-tgx expr)
  (define x (list-ref expr 1))
  `(/ ,(derivative x) ((expt cos 2) ,x)))

(define (der-ctgx expr)
  (define x (list-ref expr 1))
  `(/ ,(derivative x) (- ((expt sin 2) ,x))))

(define (der-lnx expr)
  (define x (list-ref expr 1))
  `(/ ,(derivative x) ,x))

(define (der-const expr) `0)

(define (der-x expr) `1)

#| (define test1
  (list (test (derivative 2) 0)
        (test (derivative 'x) 1)
        (test (derivative '(- x)) '(* (-1) 1))
        (test (derivative '(* 1 x)) '(+ (* 0 x) (* 1 1)))
        (test (derivative '(* -1 x)) '(+ (* 0 x) (* -1 1)))
        (test (derivative '(* -4 x)) '(+ (* 0 x) (* -4 1)))
        (test (derivative '(* 10 x)) '(+ (* 0 x) (* 10 1)))
        (test (derivative '(expt x 10)) '(* 10 (expt x 9))) 
        (test (derivative '(* 2 (expt x 5))) '(+ (* 0 (expt x 5)) (* 2 (* 5 (expt x 4)))))
        (test (derivative '(expt x -2)) '(* -2 (expt x -3)))
        (test (derivative '(expt 5 x)) '(* (expt 5 x) (log 5)))
        (test (derivative '(cos x)) '(* (- (sin x)) 1))
        (test (derivative '(sin x)) '(* (cos x) 1))
        (test (derivative '(tg x)) '(/ 1 ((expt cos 2) x)))
        (test (derivative '(ctg x)) '(/ 1 (- ((expt sin 2) x))))
        (test (derivative '(exp x)) '(* (exp x) 1))
        (test (derivative '(* 2 (exp x))) '(+ (* 0 (exp x)) (* 2 (* (exp x) 1))))
        (test (derivative '(* 2 (exp 2x))) '(+ (* 0 (exp 2x)) (* 2 (* (exp 2x) 1))))
        (test (derivative '(log x)) '(/ 1 x))
        (test (derivative '(* 3 (log x))) '(+ (* 0 (log x)) (* 3 (/ 1 x))))

        (test (derivative '(+ (expt x 3) (expt x 2))) '(+ (* 3 (expt x 2)) (* 2 (expt x 1))))
        
        (test (derivative '(- (* 2 (expt x 3)) (* 2 (expt x 2))))
              '(- (+ (* 0 (expt x 3)) (* 2 (* 3 (expt x 2)))) (+ (* 0 (expt x 2)) (* 2 (* 2 (expt x 1))))))

        (test (derivative '(/ 3 x))
              '(/ (- (* 0 x) (* 3 1)) (expt x 2)))
        
        (test (derivative '(/ 3 (* 2 (expt x 2))))
              '(/ (- (* 0 (* 2 (expt x 2)))
                     (* 3 (+ (* 0 (expt x 2)) (* 2 (* 2 (expt x 1))))))
                  (expt (* 2 (expt x 2)) 2))) 
        
        (test (derivative '(* 2 (sin x) (cos x)))
              '(+ (* 0 (sin x) (cos x))
                  (* 2 (* (cos x) 1) (cos x))
                  (* 2 (sin x) (* (- (sin x)) 1))))
        
        (test (derivative '(* (* 2 (exp x)) (sin x) (cos x)))
              '(+ (* (+ (* 0 (exp x)) (* 2 (* (exp x) 1))) (sin x) (cos x))
                  (* (* 2 (exp x)) (* (cos x) 1) (cos x))
                  (* (* 2 (exp x)) (sin x) (* (- (sin x)) 1))))
        
        (test (derivative '(sin (* 2 x)))
              '(* (cos (* 2 x)) (+ (* 0 x) (* 2 1))))
        
        (test (derivative '(cos (* 2 (expt x 2))))
              '(* (- (sin (* 2 (expt x 2)))) (+ (* 0 (expt x 2)) (* 2 (* 2 (expt x 1))))))
        
        (test (derivative '(sin (log (expt x 2))))
              '(* (cos (log (expt x 2))) (/ (* 2 (expt x 1)) (expt x 2))))
        
        (test (derivative '(+ (sin (* 2 x)) (cos (* 2 (expt x 2)))))
              '(+ (* (cos (* 2 x)) (+ (* 0 x) (* 2 1))) (* (- (sin (* 2 (expt x 2)))) (+ (* 0 (expt x 2)) (* 2 (* 2 (expt x 1)))))))

        (test (derivative '(* (sin (* 2 x)) (cos (* 2 (expt x 2)))))
              '(+ (* (* (cos (* 2 x)) (+ (* 0 x) (* 2 1))) (cos (* 2 (expt x 2)))) (* (sin (* 2 x)) (* (- (sin (* 2 (expt x 2)))) (+ (* 0 (expt x 2)) (* 2 (* 2 (expt x 1))))))))))


(run-tests test1) |#
