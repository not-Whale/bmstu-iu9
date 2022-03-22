package main

import (
	"golang.org/x/crypto/ssh"
	"io"
	"log"
	"os"
	"time"
)

func main() {

	// считывание данных сервера
	//fmt.Println("Enter host name:")
	//host := input.Gets()
	//fmt.Println("Enter port:")
	//port := input.Gets()

	// считывание логина и пароля
	//fmt.Println("Enter your login:")
	//login := input.Gets()
	//fmt.Println("Enter your password")
	//pass := input.Gets()

	// для удобства отладки задам явно эти данные

	host := "lab2.posevin.com"
	port := "22"

	address := host + ":" + port

	login := "rezepin"
	pass := "kvant2915"

	// создание конфига для клиента
	// логин, авторизация по паролю { пароль }
	// игнор колбека ключа
	// вывод баннера в os.Stderr
	// таймаут 5 секунд
	config := &ssh.ClientConfig{
		User: login,
		Auth: []ssh.AuthMethod {
			ssh.Password(pass),
		},
		HostKeyCallback: ssh.InsecureIgnoreHostKey(),
		BannerCallback: ssh.BannerDisplayStderr(),
		Timeout: 5 * time.Second,
	}

	// стучимся на сервер, создаем клиент
	client, err := ssh.Dial("tcp", address, config)
	if err != nil {
		log.Fatal("Failed to dial: ", err)
	}
	defer client.Close()

	// в случае установленного соединения
	// создаем новую сессию на сервере
	session, err := client.NewSession()
	if err != nil {
		log.Fatal("Failed to create a session: ", err)
	}
	defer session.Close()

	// отключение повторения ввода
	// установка скорости отправки и получения
	// ну, input и output speed
	modes := ssh.TerminalModes{
		ssh.ECHO: 0,
		ssh.TTY_OP_ISPEED: 14400,
		ssh.TTY_OP_OSPEED: 14400,
	}

	// создание псевдо-терминала (чтобы вывод был форматированный)
	err = session.RequestPty("xterm", 100, 100, modes)
	if err != nil {
		log.Fatal("Failed to create Pty: ", err)
	}

	// синхронизация потоков ввода
	stdin, err := session.StdinPipe()
	if err != nil {
		log.Fatal("Failed to Pipe StdIn: ", err)
	}
	go io.Copy(stdin, os.Stdin)

	// синхронизация потоков вывода
	stdout, err := session.StdoutPipe()
	if err != nil {
		log.Fatal("Failed to Pipe StdOut: ", err)
	}
	go io.Copy(os.Stdout, stdout)

	// синхронизация потоков вывода ошибок
	stderr, err := session.StderrPipe()
	if err != nil {
		log.Fatal("Failed to Pipe StrErr: ", err)
	}
	go io.Copy(os.Stderr, stderr)

	// запуск оболочки на сервере
	// и уже внутри можно работать, как удобно
	err = session.Shell()
	if err != nil {
		log.Fatal("Failed to create Shell session: ", err)
	}

	// есть другой вариант выполнения одной команды
	// но есть ли смысл выполнять только одну команду?
	// err = session.Run("ls")
	// if err != nil {
	//	 log.Fatal("Failed to run a command: ", err)
	// }

	// запускаем режим ожидания, чтобы работать
	// с оболочкой и не "вылетать" сразу
	// завершение будет только в случае ошибки обработки
	// запроса / команды
	err = session.Wait()
	if err != nil {
		log.Fatal(err)
	}
}