#include <string>
#include <vector>
#include <unordered_set>
#include <map>

using namespace std;

void get_tokens(
        // [ВХОД] Текстовая строка.
        const string &s,
        // [ВХОД] Множество символов-разделителей.
        const unordered_set<char> &delimiters,
        // [ВЫХОД] Последовательность слов.
        vector<string> &tokens) {
    int i = 0;
    int n = s.length();
    string buf = "";
    while (i < n) {
        char x = s.at(i);
        if (delimiters.count(x) != 0) {
            if (!buf.empty()) {
                tokens.push_back(buf);
                buf = "";
            }
            i++;
        } else {
            x = (char)tolower(x);
            buf += x;
            i++;
        }
    }
    if (!buf.empty()) {
        tokens.push_back(buf);
        buf = "";
    }
}

void get_type_freq(
        // [ВХОД] Последовательность слов.
        const vector<string> &tokens,
        // [ВЫХОД] Частотный словарь
        // (ключи -- слова, значения -- количества вхождений).
        map<string, int> &freqdi) {
    for (string x : tokens) {
        if (freqdi.count(x) == 0) {
            freqdi[x] = 1;
        } else {
            freqdi[x]++;
        }
    }
}

void get_types(
        // [ВХОД] Последовательность слов.
        const vector<string> &tokens,
        // [ВЫХОД] Список уникальных слов.
        vector<string> &wtypes) {
    map<string, int> ws;
    get_type_freq(tokens, ws);
    for (auto p : ws) {
        wtypes.push_back(p.first);
    }
}

void get_x_length_words(
        // [ВХОД] Список уникальных слов.
        const vector<string> &wtypes,
        // [ВХОД] Минимальная длина слова.
        int x,
        // [ВЫХОД] Список слов, длина которых не меньше x.
        vector<string> &words) {
    for (auto p : wtypes) {
        if (p.length() >= x) {
            words.push_back(p);
        }
    }
}

void get_x_freq_words(
        // [ВХОД] Частотный словарь
        const map<string, int> &freqdi,
        // [ВХОД] Минимальное количество вхождений.
        int x,
        // [ВЫХОД] Список слов, встречающихся не меньше x раз.
        vector<string> &words) {
    for (auto p : freqdi) {
        if (p.second >= x) {
            words.push_back(p.first);
        }
    }
}

void get_words_by_length_dict(
        // [ВХОД] Список уникальных слов.
        const vector<string> &wtypes,
        // [ВЫХОД] Словарь распределения слов по длинам.
        map<int, vector<string> > &lengthdi) {
    for (auto p : wtypes) {
        int n = p.length();
        lengthdi[n].push_back(p);
    }
}