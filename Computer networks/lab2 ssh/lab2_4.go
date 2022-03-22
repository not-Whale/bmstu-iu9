package main

import (
	"bytes"
	"golang.org/x/crypto/ssh"
	"fmt"
	"log"
	"os"
	"strings"
	"time"
	"github.com/skorobogatov/input"
)

func main() {
	// считываем хосты
	hosts := os.Args[1:]

	// считывание логина и пароля
	//fmt.Println("Enter your login:")
	//login := input.Gets()
	//fmt.Println("Enter your password")
	//pass := input.Gets()

	// для удобства отладки задам явно эти данные
	login := "rezepin"
	pass := "kvant2915"

	for {
		fmt.Println("Enter your command:")
		// считывание команды
		cmd := input.Gets()

		// выход из цикла
		if strings.ToLower(cmd) == "close" {
			break
		}

		// создаем канал строк, чтобы распараллелить
		// выполнение команд на различных серверах
		// пусть будет 5 потоков
		// (то есть можно обработать до 5 соединений)
		results := make(chan string, 5)

		// пусть выполнение команд на серверах должно выполниться
		// в течение 15 секунд
		timeout := time.After(25 * time.Second)

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

		// проходим по адресам и запускаем goroutine команду
		for _, hostname := range hosts {
			go func (hostname string) {
				results <- runCommand(cmd, hostname, config)
			} (hostname)
		}

		// проходим по массиву ответов
		for i := 0; i < len(hosts); i++ {
			select {
			// если есть ответ, то выведем ответ
			case res := <-results:
				{
					fmt.Print(res)
				}
			// а если время вышло то грустим
			// но несильно
			case <-timeout:
				{
					fmt.Println("Timed out!")
					return
				}
			}
		}
	}
}

func runCommand(cmd, address string, config *ssh.ClientConfig) string {
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

	// создание потока вывода
	var Stdout bytes.Buffer
	session.Stdout = &Stdout

	// выполнение команды
	err = session.Run(cmd)
	// если произошла ошибка, то вывод ее
	if err != nil {
		log.Fatal("Failed to run a command: ", err)
	}

	// иначе возвращаем ответ сервера
	return address + ": " + Stdout.String()
}
