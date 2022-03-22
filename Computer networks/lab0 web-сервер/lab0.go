package main

import (
	"fmt"	   // пакет для работы с потоками ввода/вывода
	"github.com/mmcdole/gofeed" // пакет для синтаксического рабора RSS-страниц
	"log"      // пакет для логирования
	"net/http" // пакет для поддержки HTTP протокола
	"sort"	   // пакет для сортировки
	"strings"  // пакет для работы со строками
)

func HomeRouterHandler(w http.ResponseWriter, r *http.Request) {
	r.ParseForm()
	// вывод отладочной информации в консоль
	fmt.Println("form:", r.Form)
	fmt.Println("path:", r.URL.Path)
	fmt.Println("url_long:", r.Form["url_long"])
	// для разделения
	fmt.Println()

	// отправка страницы на сторону пользователя
	fmt.Fprintf(w,
		`<p> Тут можно посмотреть новости :-) </p>
				<a href="http://blagnews.ru"> BlagNews </a>
				<br></br>
				<a href="http://www.rssboard.org"> RssBoard </a>
				<br></br>
				Оставить только один ресурс:
				<br></br>
				<a href="/blag"> BlagNews Rss </a>
				<br></br>
				<a href="/board"> RssBoard Rss </a>
				<br></br>
				<a href="/lenta"> Lenta Rss </a>
				<br></br>
				<form method="POST">
					<input type="text" name="news" placeholder="enter link">
					<button type="submit"> Get some news! </button>
				</form>`)
	// вывод ленты новостей с сортировкой по дате публикации
	printAllNews(w)
}

func printContent(w http.ResponseWriter, news string) {
	// если название ресурса непусто
	if len(news) != 0 {
		// генерация парсера
		fp := gofeed.NewParser()
		// разбор RSS-ресурса по ссылке
		feed, err := fp.ParseURL(news)
		// вывод информации в случае ошибки
		if err != nil {
			log.Fatal("Open URL: ", err)
		}
		// вывод новостей
		for _, item := range feed.Items {
			fmt.Fprintf(w,
				`<h1>`+item.Title+`</h1>`+
					`<h3> <a href="`+item.Link+`"> Источник </a> </h3>`+
					item.Description)
		}
	}
}

func printAllNews(w http.ResponseWriter) {
	// создание нового парсера
	fp := gofeed.NewParser()
	// разбор RSS-ресурсов
	blag, _ := fp.ParseURL("http://blagnews.ru/rss_vk.xml")
	board, _ := fp.ParseURL("http://www.rssboard.org/files/sample-rss-2.xml")
	lenta, _ := fp.ParseURL("https://lenta.ru/rss")
	// создание массива из Items
	all_sourses := blag.Items
	all_sourses = append(all_sourses, lenta.Items...)
	all_sourses = append(all_sourses, board.Items...)
	// сортировка массива по дате
	sort.SliceStable(all_sourses,
		func (i, j int) bool {
			// определние года, месяца, дня, времени публикации

			yearI := all_sourses[i].Published[12:16]
			yearJ := all_sourses[j].Published[12:16]

			monthI := all_sourses[i].Published[8:11]
			monthJ := all_sourses[j].Published[8:11]

			dayI := all_sourses[i].Published[5:7]
			dayJ := all_sourses[j].Published[5:7]

			timeI := all_sourses[i].Published[17:25]
			timeJ := all_sourses[j].Published[17:25]

			// перевод месяца из строки в число

			monthI = calculateMonth(monthI)
			monthJ = calculateMonth(monthJ)

			// гениальнейшее сравнение методом "я дурачок"

			if yearI > yearJ {
				return true
			} else if yearI == yearJ {
				if monthI > monthJ {
					return true
				} else if monthI == monthJ {
					if dayI > dayJ {
						return true
					} else if dayI == dayJ {
						if timeI >= timeJ {
							return true
						}
					}
				}
			}
			return false
		})
	// вывод новостей
	for _, item := range all_sourses {
		fmt.Fprintf(w,
			`<h1>`+ item.Title+`</h1>`+
				`<p> Published: ` + item.Published + `</p>` +
				`<h3> <a href="` + item.Link + `"> Источник </a> </h3>` +
				item.Description)
	}
}

func calculateMonth(month string) string {
	// замена месяца-строки на число методом "я дурачок"
	if strings.ToLower(month) == "jan" {
		return "01"
	}
	if strings.ToLower(month) == "feb" {
		return "02"
	}
	if strings.ToLower(month) == "mar" {
		return "03"
	}
	if strings.ToLower(month) == "apr" {
		return "04"
	}
	if strings.ToLower(month) == "may" {
		return "05"
	}
	if strings.ToLower(month) == "jun" {
		return "06"
	}
	if strings.ToLower(month) == "jul" {
		return "07"
	}
	if strings.ToLower(month) == "aug" {
		return "08"
	}
	if strings.ToLower(month) == "sep" {
		return "09"
	}
	if strings.ToLower(month) == "oct" {
		return "10"
	}
	if strings.ToLower(month) == "nov" {
		return "11"
	}
	if strings.ToLower(month) == "dec" {
		return "12"
	}
	return ""
}

func BlagRouterHandler (w http.ResponseWriter, r *http.Request) {
	printContent(w, "http://blagnews.ru/rss_vk.xml")
}

func BoardRouterHandler (w http.ResponseWriter, r *http.Request) {
	printContent(w, "http://www.rssboard.org/files/sample-rss-2.xml")
}

func LentaRouterHandler (w http.ResponseWriter, r *http.Request) {
	printContent(w, "https://lenta.ru/rss")
}

func main() {
	// создание роутеров для относительной адресации
	http.HandleFunc("/", HomeRouterHandler)
	http.HandleFunc("/blag", BlagRouterHandler)
	http.HandleFunc("/board", BoardRouterHandler)
	http.HandleFunc("/lenta", LentaRouterHandler)
	// слушаем порт
	err := http.ListenAndServe(":0607", nil)
	// вывод информации в случае ошибки
	if err != nil {
		log.Fatal("ListenAndServe: ", err)
	}
}