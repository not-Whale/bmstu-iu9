package main

import (
	"fmt"
	"golang.org/x/net/html"
	"log"
	"net/http"
)

func getChildren(node *html.Node) []*html.Node {
	var children []*html.Node
	for c := node.FirstChild; c != nil; c = c.NextSibling {
		children = append(children, c)
	}
	return children
}

func getChildrenRec(node *html.Node) []*html.Node {
	var children []*html.Node
	return childRec(node, children)
}

func childRec(node *html.Node, children []*html.Node) []*html.Node {
	for c := node.FirstChild; c != nil; c = c.NextSibling {
		if c.Data != "aside" && c.Data != "style" {
			children = append(children, c)
			if c.FirstChild != nil {
				children = childRec(c, children)
			}
		}
	}
	return children
}

func getAttr(node *html.Node, key string) string {
	for _, attr := range node.Attr {
		if attr.Key == key {
			return attr.Val
		}
	}
	return ""
}

func lookAttr(node *html.Node) {
	for _, attr := range node.Attr {
		fmt.Println(attr.Key + ": " + attr.Val)
	}
}

func parsePic(sec *html.Node) *Item {
	items := getChildren(sec)
	if len(items) > 0 {
		var item *Item
		for _, it := range items {
			if getAttr(it, "class") == "info g-date item__info" {
				if item == nil {
					item = &Item {
						Time : it.FirstChild.FirstChild.FirstChild.Data,
					}
				} else {
					item.Time = it.FirstChild.FirstChild.FirstChild.Data
				}
				continue
			}
			if getAttr(it, "class") == "js-dh picture" {
				if item == nil {
					item = &Item {
						Ref : getAttr(it, "href"),
						Pic : getAttr(it.FirstChild, "src"),
					}
				} else {
					item.Ref = getAttr(it, "href")
					item.Pic = getAttr(it.FirstChild, "src")
				}
				continue
			}
			if getAttr(it, "class") == "titles" {
				if item == nil {
					item = &Item {
						Title : it.FirstChild.FirstChild.FirstChild.FirstChild.Data +
							".\n" + it.LastChild.FirstChild.Data,
					}
				} else {
					item.Title = it.FirstChild.FirstChild.FirstChild.FirstChild.Data +
						".\n" + it.LastChild.FirstChild.Data
				}
				continue
			}
		}
		return item
	}
	return nil
}

func parseText(sec *html.Node) *Item {
	items := getChildren(sec)
	if len(items) > 0 {
		var item *Item
		for _, it := range items {
			if getAttr(it, "class") == "info g-date item__info" {
				if item == nil {
					item = &Item {
						Time : it.FirstChild.FirstChild.FirstChild.Data,
					}
				} else {
					item.Time = it.FirstChild.FirstChild.FirstChild.Data
				}
				continue
			}
			if getAttr(it, "class") == "titles" {
				if item == nil {
					item = &Item {
						Title: it.FirstChild.FirstChild.FirstChild.FirstChild.Data,
						Ref : getAttr(it.FirstChild.FirstChild, "href"),
					}
				} else {
					item.Title = it.FirstChild.FirstChild.FirstChild.FirstChild.Data
					item.Ref = getAttr(it.FirstChild.FirstChild, "href")
				}
			}
		}
		return item
	}
	return nil
}

func downloadSnippets() []*Item {
	fmt.Println("Sending request to lenta.ru...")

	if response, err := http.Get("https://lenta.ru/rubrics/world/"); err != nil {
		log.Fatal("Request to lenta.ru failed: ", err)
	} else {
		defer response.Body.Close()
		status := response.StatusCode
		fmt.Println("Got response from lenta.ru:", "status", status)
		if status == http.StatusOK {
			if doc, err := html.Parse(response.Body); err != nil {
				fmt.Println("Invalid HTML from lenta.ru:", "error", err)
			} else {
				fmt.Println("HTML from lenta.ru parsed successfully!")
				fmt.Println()
				return searchSnippet(doc)
			}
		}
	}
	return nil
}

func downloadNews(address string) *Item {
	fmt.Println("Sending request to " + address + "...")

	if response, err := http.Get(address); err != nil {
		log.Fatal("Request to " + address + " failed: ", err)
	} else {
		defer response.Body.Close()
		status := response.StatusCode
		fmt.Println("Got response from " + address + ":", "status", status)
		if status == http.StatusOK {
			if doc, err := html.Parse(response.Body); err != nil {
				fmt.Println("Invalid HTML from " + address + ":", "error", err)
			} else {
				fmt.Println("HTML from " + address + " parsed successfully!")
				return searchNews(doc)
			}
		}
	}
	return nil
}

type Item struct {
	Ref, Time, Title, Pic, Body string
}

func isElem(node *html.Node, tag string) bool {
	return node != nil && node.Type == html.ElementNode && node.Data == tag
}

func isDiv(node *html.Node, class string) bool {
	return isElem(node, "div") && getAttr(node, "class") == class
}

func searchSnippet(node *html.Node) []*Item {
	var res []*Item
	if isDiv(node, "span4") {
		var items []*Item
		if isElem(node.FirstChild, "section") &&
			getAttr(node.FirstChild, "class") == "b-longgrid-column" {
			for c := node.FirstChild.FirstChild; c != nil; c = c.NextSibling {
				if getAttr(c, "class") == "item article" {
					item := parsePic(c)
					items = append(items, item)
				}
				if getAttr(c, "class") == "item news b-tabloid__topic_news" {
					item := parseText(c)
					items = append(items, item)
				}
			}
			res = append(res, items...)
		}
	}
	for c := node.FirstChild; c != nil; c = c.NextSibling {
		if items := searchSnippet(c); items != nil {
			res = append(res, items...)
		}
	}
	return res
}

func searchNews(node *html.Node) *Item {
	var res *Item
	fmt.Println("SEARCH NEWS")

	// g-application js-root
	// b-topic-layout js-topic
	// b-topic-layout__content clearfix
	// b-topic-layout__left js-topic__content

	items := getChildren(node.LastChild.LastChild)
	for _, item := range items {
		if getAttr(item, "class") == "g-application js-root" {
			node = item
			break
		}
	}

	items = getChildren(node)
	for _, item := range items {
		if getAttr(item, "class") == "b-topic-layout js-topic" {
			node = item.FirstChild
			break
		}
	}

	items = getChildren(node)
	for _, item := range items {
		if getAttr(item, "class") == "b-topic-layout__content clearfix" {
			node = item
			break
		}
	}

	items = getChildren(node)
	for _, item := range items {
		if getAttr(item, "class") == "b-topic-layout__left js-topic__content" {
			node = item
			break
		}
	}

	header := node.FirstChild.FirstChild.FirstChild.FirstChild

	items = getChildren(header)
	for _, item := range items {
		//fmt.Println(getAttr(item, "class"))
		if getAttr(item, "class") == "g-date" {
			//fmt.Println("item.Data = " + item.FirstChild.Data)
			if res == nil {
				res = &Item {
					Time: item.FirstChild.Data,
				}
			} else {
				res.Time = item.FirstChild.Data
			}
		}
		if getAttr(item, "class") == "b-topic__title" {
			//fmt.Println("item.Data = " + item.FirstChild.Data)
			if res == nil {
				res = &Item {
					Title: item.FirstChild.Data,
				}
			} else {
				res.Title = item.FirstChild.Data
			}
		}
		if getAttr(item, "class") == "b-topic__rightcol" {
			//fmt.Println("item.Data = " + item.FirstChild.Data)
			if res == nil {
				res = &Item {
					Title: item.FirstChild.Data,
				}
			} else {
				res.Title += ". " + item.FirstChild.Data
			}
		}
		if getAttr(item, "class") == "b-topic__title-image" {
			//fmt.Println("item.Data = " + getAttr(item.FirstChild, "src"))
			if res == nil {
				res = &Item {
					Pic : getAttr(item.FirstChild, "src"),
				}
			} else {
				res.Pic = getAttr(item.FirstChild, "src")
			}
		}
	}

	items = getChildren(node.FirstChild.FirstChild)
	for _, item := range items {
		if getAttr(item, "class") == "b-text clearfix js-topic__text" {
			node = item
		}
	}

	bd := ""
	tab := false
	h := false
	items = getChildrenRec(node)
	for _, item := range items {

		if item.Data == "a" {
			continue
		}

		if item.Data == "h1" || item.Data == "h2" || item.Data == "h3" ||
			item.Data == "h4" || item.Data == "h5" || item.Data == "h6" {
			bd += "<br><h3>"
			h = true
			continue
		}

		if item.Data == "p" {
			bd += "<br>"
			tab = true
			continue
		}

		if item.Data == "div" {
			continue
		}

		bd += item.Data

		if tab {
			tab = false
		}

		if h {
			bd += "</h3>"
			h = false
		}
	}

	//fmt.Println(bd)

	if res == nil {
		res = &Item {
			Body: bd,
		}
	} else {
		res.Body = bd
	}

	return res
}

func rec(node *html.Node) {
	items := getChildren(node)
	for _, item := range items {
		fmt.Println(item)
	}
}

func Handler(w http.ResponseWriter, r *http.Request) {
	r.ParseForm()
	fmt.Println("form", r.Form)
	fmt.Println("path", r.URL.Path)
	fmt.Println("url_long", r.Form["url_long"])
	fmt.Println()

	w.Write([]byte("<h1>News!</h1>"))

	items := downloadSnippets()

	for _, item := range items {
		if item != nil {
			if item.Pic != "" {
				if item.Title != "" {
					w.Write([]byte("<h2>" + item.Title + "</h2>"))
				} else {
					fmt.Println("TITLE is empty")
					continue
				}
				if item.Pic != "" {
					w.Write([]byte("<img src=\"" + item.Pic + "\">"))
				}
				// вот тут в странице со сниппетами делаю адресацию с передачей ключ-значения
				if item.Ref != "" {
					w.Write([]byte("<h4><a href=\"/news?address=" + item.Ref + "\"> Источник </a></h3>"))
				}
				if item.Body != "" {
					w.Write([]byte("<div>" + item.Body + "</div>"))
				}
				if item.Time != "" {
					w.Write([]byte("<h4> Published at: " + item.Time + "</h3>"))
				}
			}
		}
	}
}

func NewsHandler(w http.ResponseWriter, r *http.Request)  {
	r.ParseForm()
	fmt.Println("form", r.Form)
	fmt.Println("path", r.URL.Path)
	fmt.Println("url_long", r.Form["url_long"])
	fmt.Println()

	// вот тут считываю из запроса нужный мне адрес
	url := "https://lenta.ru" + r.Form["address"][0]

	item := downloadNews(url)

	w.Write([]byte("<h1> NEWS! </h1>"))

	if item.Title != "" {
		w.Write([]byte("<h2>" + item.Title + "</h2>"))
	} else {
		fmt.Println("TITLE is empty")
		return
	}
	if item.Pic != "" {
		w.Write([]byte("<img src=\"" + item.Pic + "\">"))
	}
	if item.Ref != "" {
		w.Write([]byte("<h4>Источник: <a href=\"/news?address=" + item.Ref +
			"\"> http://lenta.ru" + item.Ref + "</a></h3>"))
	}
	if item.Time != "" {
		w.Write([]byte("<h4> Published at: " + item.Time + "</h3>"))
	}
	if item.Body != "" {
		w.Write([]byte("<p>" + item.Body + "</p>"))
	}
}

func main() {
	http.HandleFunc("/", Handler)
	http.HandleFunc("/news", NewsHandler)
	err := http.ListenAndServe(":9607", nil)
	if err != nil {
		log.Fatal(err)
	}
}