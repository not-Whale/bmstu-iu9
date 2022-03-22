package main

import (
	"fmt"
	"io/ioutil"
	"log"
	"net/http"
	"strings"
)

var (
	LOCAL_HREF = "href=\"http://localhost:9607?url="
	LOCAL_SRC = "src=\"http://localhost:9607?url="
)

func Handler(w http.ResponseWriter, r *http.Request) {
	url := r.FormValue("url")
	if url == "" {
		http.ServeFile(w, r, "index.html")
	} else {

		client := &http.Client{}

		req, err := http.NewRequest("GET", url, nil)
		if err != nil {
			log.Fatal(err)
		}

		resp, err := client.Do(req)
		if err != nil {
			log.Fatal(err)
		}

		old_body, err := ioutil.ReadAll(resp.Body)
		if err != nil {
			log.Fatal(err)
		}

		new_body := strings.Replace(string(old_body), "%", "%%", -1)
		if resp.Header.Get("Content-Type") == "text/http" {
			new_body = strings.Replace(new_body, "href=\"h", LOCAL_HREF + "h", -1)
			new_body = strings.Replace(new_body, "href=\"//", LOCAL_HREF + "http://", -1)
			new_body = strings.Replace(new_body, "href=\"/", LOCAL_HREF + url + "/", -1)
			new_body = strings.Replace(new_body, "src=\"h", LOCAL_SRC + "h", -1)
			new_body = strings.Replace(new_body, "src=\"//", LOCAL_SRC + "http://", -1)
			new_body = strings.Replace(new_body, "src=\"/", LOCAL_SRC + url + "/", -1)
		} else {
			new_body = strings.Replace(new_body, "http://", "http://localhost:9607?url=http://", -1)
			new_body = strings.Replace(new_body, "https://", "http://localhost:9607?url=https://", -1)
		}

		w.Header().Set("Content-Type", resp.Header.Get("Content-Type"))

		fmt.Fprintf(w, new_body)
	}
}

func main() {
	http.HandleFunc("/", Handler)
	log.Fatal(http.ListenAndServe("localhost:9607", nil))
}