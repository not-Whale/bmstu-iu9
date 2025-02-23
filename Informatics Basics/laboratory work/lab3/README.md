# Лабораторная работа №3

## Цели работы

+   На практике ознакомиться с системой типов языка Scheme.
+   На практике ознакомиться с юнит-тестированием. 
+   Разработать свои средства отладки программ на языке Scheme.
+   На практике ознакомиться со средствами метапрограммирования языка Scheme.

## Задания

1.  Реализуйте макрос `trace` для трассировки. Трассировка — способ отладки, при котором отслеживаются значения переменных или выражений на каждом шаге выполнения программы. Необходимость и вывести значение в консоль, и вернуть его в программу нередко требует существенной модификации кода, что может стать источником дополнительных ошибок. Реализуйте макрос, который позволяет ценой небольшой вставки, не нарушающей декларативность кода, выполнить и вывод значения в консоль с комментарием в виде текста выражения, которое было вычислено, и возврат его значения в программу.

    Код без трассировки:
    
    ```scheme
    (define (zip . xss)
      (if (or (null? xss)
              (null? (car xss))) ; Надо отслеживать значение (car xss) здесь...
          '()
          (cons (map car xss)
                (apply zip (map cdr xss))))) ; ...и значение xss здесь.
    ```
    
    Код с трассировкой:
    
    ```scheme
    (load "trace.scm")

    (define (zip . xss)
      (if (or (null? xss)
              (null? (trace-ex (car xss)))) ; Здесь...
          '()
          (cons (map car xss)
                (apply zip (map cdr (trace-ex xss)))))) ; ... и здесь
    ```
    
    Консоль:
    
    ```nohighlight
    > (zip '(1 2 3) '(one two three))
    (car xss) => (1 2 3)
    xss => ((1 2 3) (one two three))
    (car xss) => (2 3)
    xss => ((2 3) (two three))
    (car xss) => (3)
    xss => ((3) (three))
    (car xss) => ()
    ((1 one) (2 two) (3 three))
    ```
    
    Вычисление значения выражения осуществляется после вывода цитаты этого выражения в консоль. Таким образом, в случае аварийного завершения программы из-за невозможности вычисления значения, вы всегда сможете определить, в каком выражении возникает ошибка.
    
    **Проследите, чтобы выражение вычислялось ровно один раз**, в противном случае можно получить неверный результат работы программы.

    В дальнейшем используйте этот макрос при отладке своих программ на языке Scheme.
    
2.  Юнит-тестирование — способ проверки корректности отдельных относительно независимых частей программы. При таком подходе для каждой функции (процедуры) пишется набор тестов — пар "выражение — значение, которое должно получиться". Процесс тестирования заключается в вычислении выражений тестов и автоматизированном сопоставлении результата вычислений с ожидаемым результатом. При несовпадении выдается сообщение об ошибках.

    Реализуйте свой каркас для юнит-тестирования. Пусть каркас включает следующие компоненты:

    +   Макрос `test` — конструктор теста вида `(выражение ожидаемый-результат)`.
    
    +   Процедуру `run-test`, выполняющую отдельный тест. Если вычисленный результат совпадает с ожидаемым, то в консоль выводятся выражение и признак того, что тест пройден. В противном случае выводится выражение, признак того, что тест не пройден, а также ожидаемый и фактический результаты. Функция возвращает `#t`, если тест пройден и `#f` в противном случае. Вывод цитаты выражения в консоль должен выполняться до вычисления его значения, чтобы при аварийном завершении программы последним в консоль было бы выведено выражение, в котором произошла ошибка.
    
    +   Процедуру `run-tests`, выполняющую серию тестов, переданную ей в виде списка. Эта процедура должна выполнять все тесты в списке и возвращает `#t`, если все они были успешными, в противном случае процедура возвращает `#f`. 
    
    Какой предикат вы будете использовать для сравнения ожидаемого результата с фактическим? Почему?

    Пример:
    
    ```scheme
    ; Пример процедуры с ошибкой
    ; 
    (define (signum x)
      (cond
        ((< x 0) -1)
        ((= x 0)  1) ; Ошибка здесь!
        (else     1)))

    ; Загружаем каркас
    ;
    (load "unit-test.scm")

    ; Определяем список тестов
    ;
    (define the-tests
      (list (test (signum -2) -1)
            (test (signum  0)  0)
            (test (signum  2)  1)))

    ; Выполняем тесты
    ;
    (run-tests the-tests)
    ```
    
    Пример результата в консоли:

    ```nohighlight
    (signum -2) ok
    (signum 0) FAIL
      Expected: 0
      Returned: 1
    (signum 2) ok
    #f
    ```
    
    Используйте разработанные вами средства отладки для выполнения следующих заданий этой лабораторной работы и последующих домашних заданий.
    
3.  Реализуйте процедуру доступа к произвольному элементу последовательности (правильного списка, вектора или строки) по индексу. Пусть процедура возвращает `#f` если получение элемента не возможно. Примеры применения процедуры:

    ```scheme
    (ref '(1 2 3) 1) ⇒ 2
    (ref #(1 2 3) 1) ⇒ 2
    (ref "123" 1)    ⇒ #\2
    (ref "123" 3)    ⇒ #f
    ```
    
    Реализуйте процедуру "вставки" произвольного элемента в последовательность, в позицию с заданным индексом (процедура возвращает новую последовательность). Пусть процедура возвращает `#f` если вставка не может быть выполнена. Примеры применения процедуры:
    
    ```scheme
    (ref '(1 2 3) 1 0)   ⇒ (1 0 2 3)
    (ref #(1 2 3) 1 0)   ⇒ #(1 0 2 3)
    (ref #(1 2 3) 1 #\0) ⇒ #(1 #\0 2 3)
    (ref "123" 1 #\0)    ⇒ "1023"
    (ref "123" 1 0)      ⇒ #f
    (ref "123" 3 #\4)    ⇒ "1234"
    (ref "123" 5 #\4)    ⇒ #f
    ```
    
    Попробуйте предусмотреть все возможные варианты.
    
    **Примечание.** Результатом выполнения задания должно быть **одно** определение процедуры `ref`. Алгоритм её работы должен определяться числом аргументов и их типами.

4.  Разработайте наборы юнит-тестов и используйте эти тесты для разработки процедуры, выполняющей разложение на множители.

    Реализуйте процедуру `factorize`, выполняющую разложение многочленов
вида <i>a</i><sup>2</sup>−<i>b</i><sup>2</sup>, 
<i>a</i><sup>3</sup>−<i>b</i><sup>3</sup> 
и <i>a</i><sup>3</sup>+<i>b</i><sup>3</sup> по формулам. 

    Пусть процедура принимает единственный аргумент — выражение на языке Scheme, которое следует разложить на множители, и возвращает преобразованное выражение. Возведение в степень в исходных выражениях пусть будет реализовано с помощью встроенной процедуры expt. Получаемое выражение должно быть пригодно для выполнения в среде интерпретатора с помощью встроенной процедуры eval. Упрощение выражений не требуется.

    Примеры вызова процедуры:

    ```
    (factorize '(- (expt x 2) (expt y 2))) 
      ⇒ (* (- x y) (+ x y))
  
    (factorize '(- (expt (+ first 1) 2) (expt (- second 1) 2)))
      ⇒ (* (- (+ first 1) (- second 1))
             (+ (+ first 1) (- second 1)))
             
    (eval (list (list 'lambda 
                          '(x y) 
                          (factorize '(- (expt x 2) (expt y 2))))
                    1 2)
              (interaction-environment))
      ⇒ -3
    ```