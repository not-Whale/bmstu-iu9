package main

import (
	"fmt"
	"golang.org/x/net/html"
	"net/http"
)

// Функция обходит от первого дочернего элемента
// до последнего и возвращает массив подчиненных элементов
func getChildren(node *html.Node) []*html.Node {
	var children []*html.Node
	// Полход по дочерним и добавление их в массив
	for c := node.FirstChild; c != nil; c = c.NextSibling {
		children = append(children, c)
	}
	return children
}

// Перебирает атрибуты тега и возвращает значение
// искомого атрибута
func getAttr(node *html.Node, key string) string {
	// Проход по атрибутам и ретурн в случае
	// совпадение текущего и искомого
	for _, attr := range node.Attr {
		if attr.Key == key {
			return attr.Val
		}
	}
	return ""
}

// Содержится ли текст внутри тега
func isText(node *html.Node) bool {
	return node != nil && node.Type == html.TextNode
}

// Является ли тег <div> / <a> / <h1> и Data == заданному тегу
func isElem(node *html.Node, tag string) bool {
	return node != nil && node.Type == html.ElementNode && node.Data == tag
}

// Является ли тег <div>
func isDiv(node *html.Node, class string) bool {
	return isElem(node, "div") && getAttr(node, "class") == class
}

// Разбор тега <a> в блоке "Главные новости"
func readItem(item *html.Node) *Item {
	// Проверка на <a>
	if a := item.FirstChild; isElem(a, "a") {
		// Если это <a>, то берем ее дочерние элементы
		cs := getChildren(a)
		if isText(cs[0]) {
			return &Item{
				Ref:   getAttr(a, "href"),
				Title: cs[0].Data,
			}
		} else if cs := getChildren(a); len(cs) == 2 && isElem(cs[0], "time") && isText(cs[1]) {
			return &Item{
				Ref:   getAttr(a, "href"),
				Time:  getAttr(cs[0], "title"),
				Title: cs[1].Data,
			}
		}
	}
	return nil
}

// Структура для хранения данных
type Item struct {
	Ref, Time, Title string
}

func downloadNews() []*Item {
	fmt.Println("Sending request to lenta.ru...")
	// Подключение к ленте
	if response, err := http.Get("http://lenta.ru"); err != nil {
		// Не получилось - вывести ошибку
		fmt.Errorf("Request to lenta.ru failed: ", err)
	} else {
		// Иначе отправляем defer на закрытие запроса
		defer response.Body.Close()
		// Считываем статус подключения
		status := response.StatusCode
		fmt.Println("Got response from lenta.ru:", "status", status)
		// Если все хорошо, то
		if status == http.StatusOK {
			// Парсим Body Html-я
			if doc, err := html.Parse(response.Body); err != nil {
				fmt.Println("Invalid HTML from lenta.ru:", "error", err)
			} else {
				// Запускаем функцию search от результата парсинга
				// и возвращаем результат ее работы
				fmt.Println("HTML from lenta.ru parsed successfully!")
				return search(doc)
			}
		}
	}
	// Иначе возврат nil
	return nil
}

// Поиск нужного блока новостей и составление нужной структуры
func search(node *html.Node) []*Item {
	// Создание массива айтемов
	var res []*Item
	// Если блок класса b-yellow-box__wrap или span4
	if isDiv(node, "b-yellow-box__wrap") || isDiv(node, "span4") {
		// Создание буферного массива айтемов
		var items []*Item
		// Для каждого дочернего элемента, пока не ni;
		for c := node.FirstChild; c != nil; c = c.NextSibling {
			// Если блок класса item
			if isDiv(c, "item") {
				// Читаем блок и если он ненулевой,
				// То добавляем его в массив айтемов
				if item := readItem(c); item != nil {
					items = append(items, item)
				}
			}
		}
		// Сливаем в результирующий набранные значения
		res = append(res, items...)
	}
	// Если не <div>, то проходимся по дочерним элементам
	for c := node.FirstChild; c != nil; c = c.NextSibling {
		// Делаем search по этому элементу
		// и если результат ненулевой, то добавляем его
		// в результуруюзий массив
		if items := search(c); items != nil {
			res = append(res, items...)
		}
	}
	// Возврат того, что насобирали по странице
	return res
}

func Handler(w http.ResponseWriter, r *http.Request)  {
	fmt.Println("Listen on localhost:0607...")

	w.Write([]byte("<h1>News!</h1>"))

	// Начало загрузки заголовков новостей
	fmt.Println("Download started...")
	items := downloadNews()
	fmt.Println("Got all news!")

	// Проход по всем айтемам
	// И вывод новостей по принципу:
	// Если есть название - выводим и проверяем
	// На дату и ссылку на новость
	// Все, что есть, выводим, остальное пропускаем
	// Если нет названия, то ничего не выводим
	for i, item := range items {
		fmt.Println("\nItem", i + 1, ":")
		if item.Title != "" {
			fmt.Println("TITLE: " + item.Title)
			w.Write([]byte("<h2>" + item.Title + "</h2>"))
		} else {
			fmt.Println("TITLE is empty")
			continue
		}
		if item.Ref != "" {
			fmt.Println("REF: " + item.Ref)
			w.Write([]byte("<h4>Источник: <a href=\"http://lenta.ru" + item.Ref + "\">http://lenta.ru" + item.Ref + "</a></h3>"))
		} else {
			fmt.Println("REF is empty")
		}
		if item.Time != "" {
			fmt.Println("TIME: " + item.Time)
			w.Write([]byte("<h4> Published: " + item.Time + "</h3>"))
		} else {
			fmt.Println("TIME is empty")
		}
	}
}

func main() {
	http.HandleFunc("/", Handler)
	http.ListenAndServe("localhost:9606", nil)
}