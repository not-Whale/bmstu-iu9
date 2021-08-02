#include <iostream>
#include <vector>
#include <map>
#include <fstream>
#include <algorithm>
#include <ctime>

using namespace std;

vector<string> calcBigrams(string word);
vector<string> unity(vector<string>& a, vector<string>& b);
vector<string> cross(vector<string>& a, vector<string>& b);
double similarity(vector<string>& a, vector<string>& b);

int main() {
    unsigned int start = clock();
    map<string, pair<vector<string>, int> > dictionary;
    ifstream dic ("count_big.txt");
    while (dic) {
        string word;
        int rate;
        dic >> word >> rate;
        dictionary[word] = make_pair(calcBigrams(word), rate);
    }
    dic.close();
    do {
        string word, ans;
        cin >> word;
        vector<string> w = calcBigrams(word);
        ans = word;
        double sim = -1;
        int num = 0;
        for (auto i = dictionary.begin(); i != dictionary.end(); i++) {
            double sim1 = similarity(w, i->second.first);
            auto buf = i->second.second;
            if (sim1 > sim) {
                sim = sim1;
                num = buf;
                ans = i->first;
            } else if (sim1 == sim) {
                if (i->second.second > num) {
                    sim = sim1;
                    num = buf;
                    ans = i->first;
                } else if (i->second.second == num) {
                    if (i->first < ans) {
                        sim = sim1;
                        num = buf;
                        ans = i->first;
                    }
                }
            }
        }
        if (cin) {
            cout << ans << endl;
        }
    } while (cin);
    return 0;
}

vector<string> calcBigrams(string word) {
    int n = word.size();
    vector<string> w;
    if (n == 1) {
        w.push_back(word);
    } else {
        char bi[3] = { 0 };
        for (int i = 0; i < n - 1; i++) {
            bi[0] = word[i];
            bi[1] = word[i + 1];
            w.emplace_back(bi);
        }
    }
    sort(w.begin(), w.end());
    return w;
}

vector<string> unity(vector<string>& a, vector<string>& b) {
    vector<string> res;
    for (string x : a) {
        bool contains = false;
        for (string y : res) {
            if (y == x) {
                contains = true;
                break;
            }
        }
        if (!contains) {
            res.push_back(x);
        }
    }
    for (string x : b) {
        bool contains = false;
        for (string y : res) {
            if (y == x) {
                contains = true;
                break;
            }
        }
        if (!contains) {
            res.push_back(x);
        }
    }
    return res;
}

vector<string> cross(vector<string>& a, vector<string>& b) {
    vector<string> res;
    for (string x : a) {
        bool contains = false;
        bool added = false;
        for (string y : b) {
            if (y == x) {
                contains = true;
                for (string z : res) {
                    if (z == x) {
                        added = true;
                        break;
                    }
                }
                break;
            }
        }
        if ((contains) && (!added)) {
            res.push_back(x);
        }
    }
    return res;
}

double similarity(vector<string>& a, vector<string>& b) {
    vector<string> v1;
    set_intersection(a.begin(), a.end(), b.begin(), b.end(), inserter(v1, v1.begin()));
    double c1 = v1.size();
    v1.clear();
    if (c1 == 0) {
        return 0;
    }
    double c3 = (double)a.size() + (double)b.size() - c1;
    return c1 / c3;
}