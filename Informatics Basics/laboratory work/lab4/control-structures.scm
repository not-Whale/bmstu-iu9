(define-syntax when
  (syntax-rules ()
    ((when cond expr . exprs)
     (if cond
         (begin
           expr . exprs)))))

(define-syntax unless
  (syntax-rules ()
    ((unless cond expr . exprs)
     (when (not cond) expr . exprs))))

(define-syntax for
  (syntax-rules (in as)
    ((for xs as x expr . exprs)
     (for-each (lambda (x) (begin expr . exprs)) xs))
    ((for x in xs expr . exprs)
     (for xs as x expr . exprs))))

(define-syntax while
  (syntax-rules ()
    ((while cond expr . exprs)
     (letrec ((iter (lambda ()
                      (if cond
                          (begin (begin expr . exprs) (iter))))))
       (iter)))))

(define-syntax repeat
  (syntax-rules (until)
    ((repeat (expr . exprs) until cond)
     (letrec ((iter (lambda ()
                      (begin
                        (begin expr . exprs)
                        (if (not cond)
                            (iter))))))
       (iter)))))

