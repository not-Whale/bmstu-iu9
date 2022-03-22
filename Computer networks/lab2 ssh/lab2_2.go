package main

import (
	"fmt"
	"github.com/gliderlabs/ssh"
	"golang.org/x/crypto/ssh/terminal"
	"log"
	"os/exec"
	"strings"
)

func main() {
	// устанавливаем обработчик
	ssh.Handle(Handler)

	// запускаем слушатель
	fmt.Println("Starting server on localhost:2222...")
	log.Fatal(ssh.ListenAndServe("localhost:2222", nil))
}

func Handler(s ssh.Session) {

	// проверка есть ли у нас команда от s
	command := s.Command()
	if len(command) != 0 {
		// создаение новой составной команды
		cmd := exec.Command(command[0], command[1:]...)
		// выполнение
		res, err := cmd.Output()
		// если ошибка то вывод ошибки
		if err != nil {
			s.Write([]byte("Error: " + err.Error() + "\n"))
		} else {
			// иначе вывод результата
			s.Write(res)
		}
		return
	}

	// иначе создание нового терминала
	term := terminal.NewTerminal(s, "> ")
	for {
		// чтение строки и вывод отладки в консоль на сервере
		line, err := term.ReadLine()
		if err != nil {
			break
		}
		fmt.Println("Command: " + line)

		var cmd *exec.Cmd
		// разбиение строки в последовательность
		// команда аргумент1 аргумент2 ...
		arr := strings.Split(line, " ")
		if len(arr) > 1 {
			// если аргументы есть
			// то создание команды с аргументами
			cmd = exec.Command(arr[0], arr[1:]...)
		} else {
			// если аргументов нет
			// то создание команды
			cmd = exec.Command(line)
		}

		// выполнение
		res, err := cmd.Output()
		// если ошибка то вывод ошибки
		if err != nil {
			term.Write([]byte("Error: " + err.Error() + "\n"))
		} else {
			// иначе вывод результата
			term.Write(res)
		}
	}
	// терминал закрыт (отладка)
	fmt.Println("Terminal closed!")
}