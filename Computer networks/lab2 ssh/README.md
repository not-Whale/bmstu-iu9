## Лабораторная работа №2
### Задания
##### Часть 1
1. Реализовать ssh-клиент.
2. Реализовать создание и удаление директории go-ssh-клиентом на ssh-сервере;
3. Реализовать удаление go-ssh-клиентом файла на ssh-сервере;
4. Реализовать получение содержимого директории на ssh-сервере с помощью go-ssh-клиента.
5. Протестировать соединение go-ssh-клиента с использованием своей учетной записи на сервере lab2.posevin.com.

##### Часть 2
1. Реализовать ssh-сервер;
2. Реализовать авторизацию клиента на ssh-сервере;
3. Реализовать передачу клиенту список содержимого заданной директории ssh-сервера по запросу;
4. Реализовать поддержку создания клиентом директории на ssh-сервере по запросу;
5. Реализовать поддержку удаления клиентом директории на ssh-сервере по запросу.
6. Протестировать соединение ssh-клиента (для тестирования можно использовать консольную версию ssh-клиента) с реализованным ssh-сервером.

##### Часть 3
1. Разработать клиент-серверное приложение для автоматизации передачи данных с одного сервера на другой с использованием go-ssh-клиента и go-ssh-сервера. Необходимо автоматизировать выполнение следующих функций с одного сервера на другой по ssh протоколу:
    * создание директории на удаленном go-ssh-сервере из go-ssh-клиента;
    * удаление директории на удаленном go-ssh-сервере из go-ssh-клиента;
2. Выполнить удаленно приложение на ssh-сервере посредством ssh-клиента.

##### Часть 4
1. Разработать go-ssh-клиент, устанавливающий соединение с несколькими ssh-серверами и параллельно выполняющий на них одну и ту же команду (с ограничением по максимальному времени всех команд) из списка:
    * создание директории;
    * удаление директории;
    * удаление файла;
    * получение содержимого директории.
2. Протестировать работу с двумя серверами: локальным из части 2 и lab2.posevin.com.