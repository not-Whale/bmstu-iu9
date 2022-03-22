package main

import (
	"bufio"
	"fmt"
	"log"
	"net"
	"strconv"
	"strings"
)

func main() {
	listener, err := net.Listen("tcp", "localhost:9607")
	if err != nil {
		log.Fatal("Unable to listen: ", err.Error())
	}
	for {
		conn, err := listener.Accept()
		if err != nil {
			continue
		}
		go Handler(conn)
	}
}

func Handler(conn net.Conn) {
	defer conn.Close()
	N := -1
	var v1 [100]int
	var v2 [100]int
	for {
		msg, err := bufio.NewReader(conn).ReadString('\n')
		if err != nil {
			log.Fatal(err)
		}
		strings.ToLower(msg)
		fmt.Println("message = " + msg)
		msg = msg[:len(msg) - 1]
		if strings.Contains(msg, "setv") {
			fmt.Println("setv detected")
			if len(strings.Split(msg, " ")) < N + 2 {
				conn.Write([]byte("Error: " + strconv.Itoa(N+1) + " args expected\n"))
				continue
			}
			if N < 0 {
				conn.Write([]byte("Error: N don't set!\n"))
				continue
			}
			num := strings.Split(msg, " ")[1]
			fmt.Println("num = " + num)
			if num == "1" {
				fmt.Println("N = ", N)
				for i := 0; i < N; i++ {
					value, err := strconv.Atoi(strings.Split(msg, " ")[i+2])
					if err != nil {
						conn.Write([]byte("Error: " + err.Error() + "\n"))
						continue
					}
					v1[i] = value
					fmt.Println("v1[", i, "] = ", v1[i])
				}
			} else if num == "2" {
				fmt.Println("N = ", N)
				for i := 0; i < N; i++ {
					value, err := strconv.Atoi(strings.Split(msg, " ")[i+2])
					if err != nil {
						conn.Write([]byte("Error: " + err.Error() + "\n"))
						continue
					}
					v2[i] = value
					fmt.Println("v2[", i, "] = ", v2[i])
				}
			} else {
				conn.Write([]byte("Error: vector number is out of range\n"))
			}
			conn.Write([]byte("v"+num+" set\n"))
		} else if strings.Contains(msg, "setn") {
			fmt.Println("setn detected")
			if len(strings.Split(msg, " ")) < 2 {
				conn.Write([]byte("Error: N expected, but not detected\n"))
				continue
			}
			value, err := strconv.Atoi(strings.Split(msg, " ")[1])
			if err != nil {
				conn.Write([]byte("Error: " + err.Error() + "\n"))
				continue
			}
			N = value
			fmt.Println("N =", N)
			conn.Write([]byte("N set\n"))
		} else if strings.Contains(msg, "calc") {
			fmt.Println("calculate detected")
			if N == -1 {
				conn.Write([]byte("Error: N don't set!\n"))
				continue
			}
			res := 0
			for i := 0; i < N; i++ {
				fmt.Println("v1[", i, "] = ", v1[i])
				fmt.Println("v2[", i, "] = ", v2[i])
				res += v1[i] * v2[i]
				fmt.Println("res =", res)
			}
			fmt.Println("res = ", res)
			r := strconv.Itoa(res)
			conn.Write([]byte(r + "\n"))
		} else {
			fmt.Println("unknown command")
			conn.Write([]byte("unknown command \"" + msg + "\"\n"))
		}
 	}
}

// conn.Close()
// return