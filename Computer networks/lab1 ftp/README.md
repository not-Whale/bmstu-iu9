## Лабораторная работа №1
### Задания
##### Часть 1
1. Реализовать ftp-клиент и запустить его на сервере 185.20.227.83 или 185.20.226.174.
2. Протестировать соединение go ftp клиента с установленным на сервере students.yss.su ftp сервером.
3. Реализовать следующие функции:
     * загрузку файла go ftp клиентом на ftp сервер;
     * скачивание файла go ftp клиентом с ftp сервера;
     * создание директории go ftp клиентом на ftp сервере;
     * удаление go ftp клиентом файла на ftp сервере;
     * получение содержимого директории на ftp сервере с помощью go ftp клиента.

##### Часть 2
1. Реализовать ftp сервер на языке GO с применением указанных пакетов и запустить его на сервере 185.20.227.83 или 185.20.226.174.
2. Протестировать соединение ftp клиента (для тестирования можно использовать консольную версию ftp клиента используемой операционной системы или FileZilla, WinSCP и д.р.) с реализованным ftp сервером. Параметры доступа к ftp серверу могут быть заданы в коде программы или во внешнем конфигурационном файле.
3. Ftp сервер должен обладать следующими функциями:
     * поддерживать авторизацию клиента на ftp сервере;
     * передавать клиенту список содержимого заданной директории ftp сервера по запросу;
     * позволять клиенту скачивать файлы из заданной директории ftp сервера по запросу;
     * позволять клиенту загружать файлы в заданную директорию ftp сервера по запросу;
     * позволять клиенту создавать директории на ftp сервере по запросу;
     * позволять клиенту удалять директории на ftp сервере по запросу.

##### Часть 3
1. Запустить go ftp сервер на одном из серверов, далее запустить go ftp клиент на другом сервере либо на одном и том же сервере, но обращаясь к localhost.
2. Разработать клиент-серверное приложение для автоматизации передачи данных с одного сервера на другой с использованием go ftp клиента и go ftp сервера. Необходимо автоматизировать выполнение следующих функций с одного сервера на другой по ftp протоколу:
    * создание директории на удаленном go ftp сервере из go ftp клиента;
    * удаление директории на удаленном go ftp сервере из go ftp клиента;
    * передачу файла на удаленный go ftp сервер из текущей директории с go ftp клиента в заданную директорию go ftp сервера;
    * прием файла с удаленного go ftp сервера из заданной директории на go ftp клиент в текущую директорию.