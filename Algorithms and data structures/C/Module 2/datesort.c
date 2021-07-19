#include <stdio.h>
#include <stdlib.h>

struct Date {
    int Day, Month, Year;
    } dt[1000];

void DistibutionSort(int (*key)(int i, int l, int n, struct Date *M),
                     int m, int n, int i,
                     struct Date *S,
                     int l);

void RadixSort(int (*key)(int i, int l, int n, struct Date *M),
               int q, int m, int n,
               struct Date *S,
               int l);

int key(int i, int l, int n, struct Date *M);

void SetStruct(struct Date *S, struct Date *D, int m);

void PrintStruct(struct Date *D, int m);

int main()
{
    int m;
    scanf("%d", &m);

    for (int i = 0; i < m; i ++)
    {
        scanf("%d %d %d", &dt[i].Year, &dt[i].Month, &dt[i].Day);
    }
    RadixSort(key, 2, m, 10, dt, 3);
    RadixSort(key, 2, m, 10, dt, 2);
    RadixSort(key, 4, m, 10, dt, 1);
    PrintStruct(dt, m);
    return 0;
}

void DistibutionSort(int (*key)(int i, int l, int n, struct Date *M),
                     int m, int n, int i,
                     struct Date *S,
                     int l)
{
    struct Date D[m];
    int count[n];
    for (int h = 0; h < n; h++)
    {
        count[h] = 0;
    }
    int j = 0;
    while (j < m)
    {
        int k;
        k = key(i, l, j, S);
        count[k] = count[k] + 1;
        j++;
    }
    int b = 1;
    while (b < n)
    {
        count[b] = count[b] + count[b-1];
        b++;
    }
    j = m - 1;
    while (j >= 0)
    {
        int k;
        k = key(i, l, j, S);
        int v = count[k] - 1;
        count[k] = v;
        D[v].Year = S[j].Year;
        D[v].Month = S[j].Month;
        D[v].Day = S[j].Day;
        j--;
    }
    SetStruct(S, D, m);
}

void RadixSort(int (*key)(int i, int l, int n, struct Date *M),
               int q, int m, int n,
               struct Date *S,
               int l)
{
    struct Date D[m];
    SetStruct(D, S, m);
    int i= 0;
    while (i < q)
    {
        DistibutionSort(key, m, n, i, D, l);
        SetStruct(S, D, m);
        i++;
    }
}

int key(int i, int l, int n, struct Date *M)
{
    int k = 1;
    int elem;
    if (l == 1) { elem = M[n].Year; };
    if (l == 2) { elem = M[n].Month; };
    if (l == 3) { elem = M[n].Day; };

    for (int j = 0; j < i; j++)
    {
        k *= 10;
    }
    int m = (elem/k)%10;
    return m;
}

void PrintStruct(struct Date *D, int m)
{
    for (int i = 0; i < m; i++)
    {
        printf("%d %d %d\n", D[i].Year, D[i].Month, D[i].Day);
    }
}

void SetStruct(struct Date *S, struct Date *D, int m)
{
    for (int i = 0; i < m; i++)
    {
        S[i].Year = D[i].Year;
        S[i].Month = D[i].Month;
        S[i].Day = D[i].Day;
    }
}