package main

import (
	"bufio"
	"fmt"
	"log"
	"net"
	"os"
)

func main() {
	conn, err := net.Dial("tcp", "localhost:9607")
	if err != nil{
		log.Println("Connection error: ", err.Error())
	}
	for {
		in := bufio.NewReader(os.Stdin)
		fmt.Printf("Write your command:\n")
		msg,_ := in.ReadString('\n')
		fmt.Fprintf(conn, msg + "\n")
		answ, err := bufio.NewReader(conn).ReadString('\n')
		if err != nil {
			log.Fatal(err.Error())
		}
		fmt.Println(answ)
	}
}