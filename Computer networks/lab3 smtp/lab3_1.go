package main

import (
	"crypto/aes"
	"crypto/cipher"
	"crypto/tls"
	"fmt"
	"github.com/skorobogatov/input"
	"net/smtp"
	"io/ioutil"
	"log"
	"os"
)

func main() {

	info := ParseFile("info.txt")

	login := info["login"]
	pass := decrypt(ScanCryptFile("config.txt"))
	host := info["host"]

	auth := smtp.PlainAuth("", login, pass, host)

	fmt.Println("Enter receiver address: ")
	receiver := input.Gets()

	fmt.Println("Enter subject: ")
	subject := input.Gets()

	fmt.Println("Enter your message: ")
	body := input.Gets()

	message := []byte ("To: " + receiver + "\r\n"+
		"From: " + login + "\r\n" +
		"Subject: " + subject + "\r\n" +
		"\r\n" +
		body + "\r\n")

	config := &tls.Config {
		InsecureSkipVerify: true,
		ServerName: host,
	}

	conn, err := tls.Dial("tcp", host + ":465", config)
	if err != nil {
		log.Fatal(err)
	}

	client, err := smtp.NewClient(conn, host)
	if err != nil {
		log.Fatal(err)
	}

	if err = client.Auth(auth); err != nil {
		log.Fatal(err)
	}

	if err = client.Mail(login); err != nil {
		log.Fatal(err)
	}

	if err = client.Rcpt(receiver); err != nil {
		log.Fatal(err)
	}

	wc, err := client.Data()
	if err != nil {
		log.Fatal(err)
	}

	_, err = wc.Write(message)
	if err != nil {
		log.Fatal(err)
	}

	err = wc.Close()
	if err != nil {
		log.Fatal(err)
	}

	err = client.Quit()
	if err != nil {
		log.Fatal(err)
	}
}

func ParseFile(filename string) (info map[string]string) {
	info = make(map[string]string)
	file, err := os.Open(filename)
	if err != nil {
		log.Fatal(err)
	}
	data, err := ioutil.ReadAll(file)
	if err != nil {
		log.Fatal(err)
	}
	name := ""
	value := ""
	checkname := true
	for _, symbol := range data {
		if symbol == ' ' || symbol == '\r' || symbol == '\n' || symbol == '\t' {
			//fmt.Println("Space Symbol")
			continue
		} else if symbol == ':' {
			//fmt.Println(": Symbol")
			checkname = false
		} else if symbol == ';' {
			//fmt.Println("; Symbol")
			checkname = true
			//fmt.Println("name = " + name + ", value = " + value)
			info[name] = value
			//fmt.Println("info[" + name + "] = " + info[name])
			name = ""
			value = ""
		} else if checkname {
			//fmt.Println("Name Symbol")
			name += string(symbol)
			//fmt.Println("name = " + name)
		} else {
			//fmt.Println("Value Symbol")
			value += string(symbol)
			//fmt.Println("value = " + value)
		}
	}
	return
}

func decrypt(pass string) string  {
	text := []byte(pass)
	key := []byte("okaylet'strytomake32bytesphrase!")

	c, err := aes.NewCipher(key)
	if err != nil {
		log.Fatal("93", err)
	}

	gcm, err := cipher.NewGCM(c)
	if err != nil {
		log.Fatal("98", err)
	}

	if len(text) < gcm.NonceSize() {
		log.Fatal("length error!")
	}

	nonce, text := text[:gcm.NonceSize()], text[gcm.NonceSize():]
	decryptpass, err := gcm.Open(nil, nonce, text, nil)
	if err != nil {
		log.Fatal("108 string - ", err)
	}

	return string(decryptpass)
}

func ScanCryptFile(filename string) string {
	file, err := os.Open(filename)
	if err != nil {
		log.Fatal(err)
	}
	defer file.Close()

	data, err := ioutil.ReadAll(file)
	if err != nil {
		log.Fatal(err)
	}

	return string(data)
}
