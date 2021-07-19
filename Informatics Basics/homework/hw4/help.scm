(define (new-list name pos value lng)
  (define (iter in out counter)
    (if (null? in)
        out
        (if (equal? pos (- lng counter -1))
            (iter (cdr in) (cons value out) (+ counter 1))
            (iter (cdr in) (cons (car in) out) (+ counter 1)))))
  (iter (reverse name) '() 2))

(define (find-num elem lst)
  (define (iter num xs)
    (if (equal? elem (car xs))
        num
        (iter (+ num 1) (cdr xs))))
  (iter 2 lst))

(define str->sym string->symbol)
(define sym->str symbol->string)

(define-syntax show
  (syntax-rules ()
    ((_ expr)
     (begin
       (display 'expr)
       (display " => ")
       expr))))

(define-syntax show*
  (syntax-rules ()
    ((_ expr)
     (begin
       (display 'expr)
       (newline)))))