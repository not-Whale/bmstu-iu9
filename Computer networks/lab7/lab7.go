package main

import (
	"io/ioutil"
	"log"
	"net/http"
)

func Proxy(w http.ResponseWriter, r *http.Request) {
	r.RequestURI = ""

	response, err := http.DefaultClient.Do(r)
	if err != nil {
		log.Fatal("Response error:", err)
	}

	body, err := ioutil.ReadAll(response.Body)
	if err != nil {
		log.Fatal("Can't read body:", err)
	}

	for i, h := range response.Header {
		w.Header().Add(i, h[0])
	}

	log.Println("Status: " + response.Status)
	log.Println("Method: " + r.Method)
	log.Println("URL: " + r.URL.String())

	w.WriteHeader(response.StatusCode)
	w.Write(body)
}

func main() {
	http.HandleFunc("/", Proxy)
	log.Fatal(http.ListenAndServe("localhost:9607", nil))
}