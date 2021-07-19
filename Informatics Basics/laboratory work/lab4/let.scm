;(use-syntax (ice-9 syncase))

(define-syntax my-let
  (syntax-rules ()
    
    ((my-let ((x1 value1) (x2 value2) ...) expr1 expr2 ...)
     ((lambda (x1 x2 ...) expr1 expr2 ...) value1 value2 ...))))

(define-syntax my-let*
  (syntax-rules ()
    
    ((my-let* ((x1 value1) (x2 value2) ...) expr1 expr2 ...)
     (my-let ((x1 value1))
             (my-let* ((x2 value2) ...) x1 expr2 ...)))
    
    ((my-let* () expr1 expr2 ...)
     (begin expr1 expr2 ...))))

