(define (string-trim s)
  (list->string (ox s)))
  
(define (ox xs)
  (define (oo ys)
    (if (null? ys)
        '()
        (if (or (equal? (car ys) #\tab)
                (equal? (car ys) #\newline))
        (oo (cdr ys))
        (cons (car ys) (oo (cdr ys))))))
  (oo (string->list xs)))

(define (string-trim-left s)
  (list->string (lox s)))

(define (string-trim-right s)
  (list->string (reverse (lox (list->string (reverse (string->list s)))))))

(define (lox xs)
  (define (oo ys)
    (if (not (equal? (car ys) (or #\tab #\newline)))
        ys
        (oo (cdr ys))))
  (oo (string->list xs)))

(define (pox count xs)
  (define (iter x ys)
    (if (= x count)
        '()
        (cons (car ys) (iter (+ x 1) (cdr ys)))))
  (iter 0 xs))

(define (string-prefix? cs ss)
  (equal? (string->list cs) (pox (length (string->list cs)) (string->list ss))))

(define (string-suffix? cs ss)
  (equal? (string->list cs) (reverse (pox (length (string->list cs)) (reverse (string->list ss))))))

(define (string-infix? a b)
  (define (iter x count vs)
    (if (> count (- (length (string->list b)) (length (string->list a))))
        (> x 0)
        (if (string-prefix? a (list->string vs))
            (iter (+ x 1) (+ count 1) (cdr vs))
            (iter x (+ count 1) (cdr vs)))))
  (iter 0 0 (string->list b)))

(define (string-split str sep)
  (if (not (string-infix? sep str))
      (list str)
      (map list->string (oz (string->list str) (string->list sep)))))

(define (oz xs sep)
  (if (null? xs)
      xs
      (if (< (length xs) (length sep))
          (list xs)
          (if (not (equal? (reverse (list-tail (reverse xs) (- (length xs) (length sep)))) sep))
              (cons (reverse (list-tail (reverse xs) (-(length xs) 1))) (oz (cdr xs) sep))
              (oz (list-tail xs (length sep)) sep)))))

(string-trim-left  "\t\tabc def\t\t")
(string-trim-right "\t\tabc def\t\t")
(string-trim       "\t\tabc def\n")

(string-prefix? "abc" "abcdef") 
(string-prefix? "bcd" "abcdef")
(string-suffix? "def" "abcdef")
(string-suffix? "bcd" "abcdef")

(string-infix? "def" "abcdefgh")
(string-infix? "abc" "abcdefgh")
(string-infix? "fgh" "abcdefgh")
(string-infix? "ijk" "abcdefgh")

(string-split "x;y;z" ";")
(string-split "x-->y-->z" "-->")
