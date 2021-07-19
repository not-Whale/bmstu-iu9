(define endl #\newline)
 
(define-syntax cout
  (syntax-rules (<<)
    ((cout << x)
     (display x))
    ((cout << x . other)
     (begin
       (display x)
       (cout . other)))))
 
(cout << "a = " << 1 << endl << "b = " << 2 << endl)
