## Лабораторная работа №8
### Задания
1. Реализовать протокол вычисления скалярного произведения двух n-мерных векторов.
2. Описать протокол.

| Запрос                                                                                       | Ответ сервера                        | Описание ответа                                                                                                   |
|----------------------------------------------------------------------------------------------|--------------------------------------|-------------------------------------------------------------------------------------------------------------------|
| setn N (Число N - целое неотрицательное, передается через пробел после setn)                 | "N set"                              | Установить число N - количество измерений                                                                         |
| setn                                                                                         | Error: N expected, but not detected  | Если не введено число N, вывод ошибки                                                                             |
| setv M $c_{1}$ $c_{2}$ ... $c_{N}$ (Коэффициенты $c_{i}$ вводятся через проблем, M = 1 или 2)| "y {M} set"                          | Установить координаты вектора M как $c_{i}, i = 1, 2, ..., N$                                                     |
| setv M $c_{1}$ $c_{2}$ ... $c_{L}$ (L < N)                                                   | Error: {N + 1} args expected         | Если число аргументов не соответствует требуемому (N координат + номер вектора = N + 1 аргумент), то вывод ошибки |
| setv M (M > 2)                                                                               | Error: vector number is out of range | Если M это не 1 или 2, вывод ошибки                                                                               |
| calc                                                                                         | { Скалярное произведение }           |                                                                                                                   |

