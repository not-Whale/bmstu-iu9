package main

import (
	"fmt"
	"github.com/jlaffaye/ftp"
	"github.com/skorobogatov/input"
	"log"
	"strings"
)

func main() {
	// объявление необходимых переменных
	var command string
	var host, port string
	var login, pass string
	var client *ftp.ServerConn

	// обнуление последней папки
	lastdir := ""
	lastdir += "a"
	lastdir = "/"

	// сбор информации о втором сервере
	fmt.Println("Enter host name:")
	host = input.Gets()

	fmt.Println("Enter port:")
	port = input.Gets()

	fmt.Println("Enter your login:")
	login = input.Gets()

	fmt.Println("Enter your password:")
	pass = input.Gets()

	// вывод в консоль иноформации о втором сервере
	fmt.Println("--- Server 2: " + host + ":" + port)
	fmt.Println("--- Login: " + login)
	fmt.Println("--- Pass: " + pass)

	// проверка подключения, авторизации и выхода
	path := host + ":" + port
	quit(connect(path, login, pass))

	// подключение к localhost
	client = connectLocal()

	for {
		// считывание команды
		fmt.Println("Enter your command:")
		command = input.Gets()
		// проверка на соответствие
		switch command {
		// создание папки
		case "mkdir": {
			// переход в последнюю директорию на удаленном сервере
			client.Quit()
			client = connect(path, login, pass)
			err := client.ChangeDir(lastdir)
			if err != nil {
				log.Fatal("Error (client.ChangeDir): " + err.Error())
			}

			// считывание названия новой директории
			fmt.Println("Enter directory name:")
			dir := input.Gets()

			// проверка на наличие / в начале названия директории
			if dir[0] != '/' {
				dir = "/" + dir
			}
			dirpath := lastdir + dir

			// создание новой директории
			err = client.MakeDir(dirpath)
			if err != nil {
				log.Fatal("Error (client.MakeDir): " + err.Error())
			}

			// выход
			client.Quit()
		}

		case "rmdir": {
			// переход в последнюю директорию на удаленном сервере
			client.Quit()
			client = connect(path, login, pass)
			err := client.ChangeDir(lastdir)
			if err != nil {
				log.Fatal("Error (client.ChangeDir): " + err.Error())
			}

			// считывание названия директории для удаления
			fmt.Println("Enter directory name:")
			dir := input.Gets()

			// проверка на наличие / в начале названия директории
			if dir[0] != '/' {
				dir= "/" + dir
			}
			dirpath := lastdir + dir

			// удаление директории
			err = client.RemoveDir(dirpath)
			if err != nil {
				log.Fatal("Error (client.RemoveDir): " + err.Error())
			}

			// выход
			client.Quit()
		}

		case "dir": {
			// вывод последней директории
			fmt.Println("Current directory: " + lastdir)
		}

		case "cd": {
			// переход в последнюю директорию на удаленном сервере
			client.Quit()
			client = connect(path, login, pass)
			err := client.ChangeDir(lastdir)
			if err != nil {
				log.Fatal("Error (client.ChangeDir): " + err.Error())
			}

			// считывание пути для перемещения
			fmt.Println("Enter path:")
			cd := input.Gets()

			// проверка на наличие / в начале названия директории
			if cd[0] != '/' {
				cd = "/" + cd
			}
			dirpath := lastdir + cd

			// переход в новую папку
			err = client.ChangeDir(dirpath)
			if err != nil {
				log.Fatal("Error (client.ChangeDir): " + err.Error())
			}

			// изменение буферной переменной для хранения последней директории
			lastdir, _ = client.CurrentDir()

			// выход
			client.Quit()
		}

		case "ls": {
			// переход в последнюю директорию на удаленном сервере
			client.Quit()
			client = connect(path, login, pass)
			err := client.ChangeDir(lastdir)
			if err != nil {
				log.Fatal("Error (client.ChangeDir): " + err.Error())
			}

			// вызов обхода
			dirLookup(lastdir, client)

			// выход
			client.Quit()
		}

		case "delete": {
			// переход в последнюю директорию на удаленном сервере
			client.Quit()
			client = connect(path, login, pass)
			err := client.ChangeDir(lastdir)
			if err != nil {
				log.Fatal("Error (client.ChangeDir): " + err.Error())
			}

			// считывание названия файла для удаления
			fmt.Println("Enter file name:")
			file := input.Gets()

			// проверка на наличие / в начале названия файла
			if file[0] != '/' {
				file = "/" + file
			}
			filepath := lastdir + file

			// удаление файла
			err = client.Delete(filepath)
			if err != nil {
				log.Fatal("Error (client.Delete): " + err.Error())
			}

			// выход
			client.Quit()
		}

		case "get": {
			// переход в последнюю директорию на удаленном сервере
			client.Quit()
			client = connect(path, login, pass)
			err := client.ChangeDir(lastdir)
			if err != nil {
				log.Fatal("Error (finding last directory): " + err.Error())
			}

			// считывание название искомого файла
			fmt.Println("Enter file name:")
			filename := input.Gets()

			// проверка на наличие / в начале названия файла
			if filename[0] != '/' {
				filename = "/" + filename
			}
			filepath := lastdir + filename

			// получение искомого файла
			file, err := client.Retr(filepath)
			if err != nil {
				log.Fatal("Error (client.Retr): " + err.Error())
			}

			// переход в директорию для сохраненных файлов на локальном сервере
			client.Quit()
			client = connectLocal()
			err = client.ChangeDir("/download")
			if err != nil {
				log.Fatal("Error (client.ChangeDir): " + err.Error())
			}

			// формирование названия конечного файла
			filepath = "/download" + filename

			// запись файла в хранилище
			client.Stor(filepath, file)

			// выход
			client.Quit()
		}

		case "send": {
			// подключение к локальному хранилищу
			client.Quit()
			client = connectLocal()

			// считывание названия искомого файла
			fmt.Println("Enter file name:")
			filename := input.Gets()

			// проверка на наличие / в начале названия файла
			if filename[0] != '/' {
				filename = "/" + filename
			}

			// получение искомого файла из локального хранилища
			file, err := client.Retr(filename)
			if err != nil {
				log.Fatal()
			}

			// переход в последнюю директорию на удаленном сервере
			client.Quit()
			client = connect(path, login, pass)
			err = client.ChangeDir(lastdir)
			if err != nil {
				fmt.Println("Error (finding last directory): " + err.Error())
			}

			// создание новой директории для загрузки
			client.MakeDir(lastdir + "/down")

			// запись конечного файла в новую директорию
			client.Stor(lastdir + "/down" + filename, file)

			// выход
			client.Quit()
		}

		case "bye": {
			client.Quit()
			fmt.Println("Goodbye!")
			return
		}

		default: {
			fmt.Println("Error: unknown command")
		}
		}
	}
}

// подключение к локальному хранилищу
func connectLocal() *ftp.ServerConn {
	return connect("localhost:2121", "admin", "1234")
}

func connect(path, login, pass string) *ftp.ServerConn {
	// постучались
	client, err := ftp.Dial(path)
	if err != nil {
		log.Fatal("Error (connection): " + err.Error())
	}
	// залогинились
	err = client.Login(login, pass)
	if err != nil {
		log.Fatal("Error (login): " + err.Error())
	}
	// возврат соединения
	return client
}

// выход
func quit(client *ftp.ServerConn) {
	err := client.Quit()
	if err != nil {
		log.Fatal("Error (exit): " + err.Error())
	}
}

// обход
func dirLookup(path string, connection *ftp.ServerConn) {
	fmt.Print("+──" + path)
	dirRec(path, connection, 1)
	fmt.Println()
}

// рекурсивная составляющая
func dirRec(path string, connection *ftp.ServerConn, depth int) {
	entries, err := connection.List(path)
	if err != nil {
		fmt.Println("error in func:", err)
		return
	}
	for _, a := range entries {
		fmt.Println()
		fmt.Print("+")
		for i := 0; i < depth; i++ {
			fmt.Print("   ")
		}
		fmt.Print("└──" + a.Name)
		if strings.Compare(a.Type.String(), "folder") == 0 && a.Name != "." && a.Name != ".."  {
			dirRec(path + "/" + a.Name, connection, depth + 1)
		}
	}
}