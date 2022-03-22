package main

import (
	"bytes"
	"fmt"
	"github.com/skorobogatov/input"
	"golang.org/x/crypto/ssh"
	"log"
	"strings"
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

	// делаем псевдо-терминал с новой сессией для каждой команды
	for {
		// создание сесси
		s, err := client.NewSession()
		if err != nil {
			log.Fatal("Failed to create a session: ", err)
		}

		// чтение команды и проверка на выход
		cmd := input.Gets()
		if strings.ToLower(cmd) == "exit" {
			fmt.Println("Terminal closed!")
			break
		}

		// создание потока вывода
		var Stdout bytes.Buffer
		s.Stdout = &Stdout

		// выполнение команды
		err = s.Run(cmd)
		// и проверка на ошибки при выполнении
		if err != nil {
			// вывод ошибки
			fmt.Println("Error: ", err)
		} else if len(Stdout.String()) != 0 {
			// если ошибок нет и есть что вывести
			// то вывод
			fmt.Print(Stdout.String())
		}

		// закрытие сессии
		s.Close()
	}
}