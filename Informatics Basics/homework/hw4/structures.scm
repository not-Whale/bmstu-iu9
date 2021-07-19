(load "./help.scm")

(define-syntax define-struct
  (syntax-rules ()
    ((_ in-name in-fields)
     (let ((name 'in-name)
           (fields 'in-fields))

       (begin
         
         (eval `(define (,(str->sym (string-append "make-" (sym->str name)))
                         ,@fields)
                  (list "structure" ',name ,@fields))
               (interaction-environment))

         (eval `(define (,(str->sym (string-append (sym->str name) "?"))
                         structure)
                  (and
                   (list? structure)
                   (equal? "structure" (car structure))
                   (equal? 'in-name (cadr structure))))
               (interaction-environment))
         
         (define (struct-ref field)
           (eval `(define (,(str->sym (string-append (sym->str name) "-" (sym->str field)))
                           structure)
                    (list-ref structure (find-num ',field 'in-fields)))
                 (interaction-environment)))

         (define (struct-set! field)
           (eval `(define-syntax ,(str->sym (string-append "set-" (sym->str name) "-" (sym->str field) "!"))
                    (syntax-rules ()
                      ((_ structure value)

                         (set! structure (new-list structure
                                                   (find-num ',field 'in-fields)
                                                   value
                                                   (length structure)))
                         #|(new-list structure
                                   (find-num ',field 'in-fields)
                                   value
                                   (length structure))) |#
                       )))
                 (interaction-environment)))
         
         (for-each struct-ref fields)
         (for-each struct-set! fields))))))


(define-struct cube (length width height))
(define example (make-cube 1 2 3))

(show (cube? example))

(newline)

(show (cube-length example))
(show (cube-width example))
(show (cube-height example))

(newline)
;(display example)

(show* (set-cube-length! example 30))
(show* (set-cube-width! example 20))
(show* (set-cube-height! example 10))

(newline)
;(display example)

(show (cube-length example))
(show (cube-width example))
(show (cube-height example))


#| (define-struct pos (row col))
   (define p (make-pos 1 2))
   (pos? p)
   (pos-row p)
   (pos-col p)
   p
   (set-pos-row! p 3) 
   (set-pos-col! p 4)
   p
   (pos-row p)
   (pos-col p)
|#

