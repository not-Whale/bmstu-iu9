#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int num[100];
int len[100];
int as[100];

int compare(int a, int b);

void HeapSort(int *base, int n,
              int (*compare)(int a, int b));

void Heapify(int *base, int i, int n,
             int (*compare)(int a, int b));

void BuildHeap(int *base, int n,
               int (*compare)(int a, int b));

int main()
{
    int n;
    char chr;
    char str[1000] = { 0 };
    int sym = 0;
    int flag = 1;
    int length = 0;
    scanf ("%d\n", &n);
    scanf("%c", &chr);
    for (int i = 0; i < n; i++)
    {
        while (chr != 10)
        {
            if (flag == 1) { num[i] = sym; };
            if (chr == 97) { as[i]++; };
            str[sym] = chr;
            length++;
            flag = 0;
            sym++;
            scanf ("%c", &chr);
        }
        len[i] = length;
        length = 0;
        flag = 1;
        if (i != n-1)
        {
            scanf ("%c", &chr);
            str[sym] = 32;
        }
        sym++;
    }

    int base[n];
    for (int i=0; i < n; i++)
    {
        base[i] = i;
        //printf ("Base[%d] = %d\n", i, base[i]);
    }

    HeapSort(base, n, compare);

    for (int i = 0; i < n; i++)
    {
        for (int j = 0; j < len[base[i]]; j++)
        {
            printf ("%c", str[j+num[base[i]]]);
        }
        if (i != n-1) printf(" ");
    }

    return 0;
}

int compare(int a, int b)
{
    int res = as[a]-as[b];
    return res;
}

void HeapSort(int *base, int n,
              int (*compare)(int a, int b))
{
    BuildHeap(base, n, compare);
    int i = n - 1;
    while (i > 0)
    {
        int c;
        c = base[0];
        base[0] = base[i];
        base[i] = c;

        Heapify(base, 0, i, compare);
        i--;
    }
}

void Heapify(int *base, int i, int n,
             int (*compare)(int a, int b))
{
    while (1)
    {
        int l = 2*i + 1;
        int r = 2*i + 2;
        int j = i;
        if ((l < n) && ((compare(base[i], base[l])) < 0))
        {
            i = l;
        }
        if ((r < n) && ((compare(base[i], base[r])) < 0))
        {
            i = r;
        }
        if (i == j)
        {
            break;
        }

        int c;
        c = base[i];
        base[i] = base[j];
        base[j] = c;
    }
}

void BuildHeap(int *base, int n,
               int (*compare)(int a, int b))
{
    int i = (n/2)-1;
    while (i >= 0)
    {
        Heapify(base, i, n, compare);
        i--;
    }
}