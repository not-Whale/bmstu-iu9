package main

import (
	_ "fmt"
	filedriver "github.com/goftp/file-driver"
	"github.com/goftp/server"
	"log"
)

func main() {
	factory := &filedriver.FileDriverFactory {
		RootPath: "/home/rezepin/Рабочий стол/Никита/Компьютерные сети/lab1/storage",
		Perm:     server.NewSimplePerm("admin", "base"),
	}

	opts := &server.ServerOpts {
		Factory:  factory,
		Port:     2121,
		Hostname: "localhost",
		Auth:     &server.SimpleAuth{ Name: "admin", Password: "1234" },
	}

	log.Printf("Starting ftp server on %v:%v", opts.Hostname, opts.Port)

	ftp := server.NewServer(opts)
	err := ftp.ListenAndServe()

	if err != nil {
		log.Fatal(err)
	}
}