package main

import (
	"crypto/aes"
	"crypto/cipher"
	"crypto/rand"
	"fmt"
	"io"
	"io/ioutil"
	"log"
	"os"
)

func main() {
	info := ParseFile("info.txt")
	WriteCryptPass("config.txt", crypt(info["pass"]))
}

func crypt(pass string) (newpass string) {
	text := []byte(pass)
	key := []byte("okaylet'strytomake32bytesphrase!")

	c, err := aes.NewCipher(key)
	if err != nil {
		log.Fatal(err)
	}

	gcm, err := cipher.NewGCM(c)
	if err != nil {
		log.Fatal(err)
	}

	nonce := make([]byte, gcm.NonceSize())
	if _, err = io.ReadFull(rand.Reader, nonce); err != nil {
		fmt.Println(err)
	}

	return string(gcm.Seal(nonce, nonce, text, nil))
}

func WriteCryptPass(filename, pass string) {
	file, err := os.Create(filename)
	if err != nil {
		log.Fatal(err)
	}
	defer file.Close()

	file.WriteString(pass)
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