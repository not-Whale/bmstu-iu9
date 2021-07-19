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

;================================================;

(define-syntax define-data
  (syntax-rules ()
    ((_ in-name in-structs)
     (let ((name `in-name)
           (structs `in-structs))


       (define (make-assoc struct)
         (cons ',(car struct) ',(cdr struct)))
       
       (for-each make-assoc structs)

       
       (define (make-struct struct)
         (eval `(define (,(car struct) ,@(cdr struct))
                  (list ',(car struct) ,@(cdr struct)))
               (interaction-environment)))
       
       (for-each make-struct structs)

       
       (define (make-pred)
         (eval `(define (,(string->symbol (string-append
                                           (symbol->string name)
                                           "?"))
                         question)
                  (and
                   (list? question)
                   (assoc (car question) ',structs)
                   (list? question)))
               (interaction-environment)))

       (make-pred)
       
       ))))

(define-syntax match
  (syntax-rules ()
    ((match f) 0)
    ((match f ((type args ...) expr) other ...)
     (if (equal? 'type (car f))
         (apply (lambda (args ...) expr) (cdr f))
         (match f other ...)))))


;========================================================;


(define-data figure ((square a)
                     (rectangle a b)
                     (triangle a b c)
                     (circle r)))

(define s (square 10))
(define r (rectangle 10 20))
(define t (triangle 10 20 30))
(define c (circle 10))

(show (and (figure? s)
           (figure? r)
           (figure? t)
           (figure? c)))

(define pi (acos -1))

(define (perim f)
  (match f 
    ((square a)       (* 4 a))
    ((rectangle a b)  (* 2 (+ a b)))
    ((triangle a b c) (+ a b c))
    ((circle r)       (* 2 pi r))))

(show (perim s))
(show (perim r))
(show (perim t))
(show (perim c))