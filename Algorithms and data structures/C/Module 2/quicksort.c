#include <stdio.h>
#include <stdlib.h>

int Partition(int low, int high, int *P,
              int (*compare)(int a, int b, int *P),
              void (*swap)(int a, int b, int *P));

void QuickSort(int n, int m, int *P,
               int (*compare)(int a, int b, int *P),
               void (*swap)(int a, int b, int *P));

void QuickSortRec(int low, int high, int m, int *P,
                  int (*compare)(int a, int b, int *P),
                  void (*swap)(int a, int b, int *P));

void SelectSort(int low, int high, int *P,
                int (*compare)(int a, int b, int *P),
                void (*swap)(int a, int b, int *P));

int compare(int a, int b, int *P)
{
    return P[a]-P[b];
}

void swap(int a, int b, int *P)
{
    int c;
    c = P[a];
    P[a] = P[b];
    P[b] = c;
}

int main()
{
    int n, m;
    scanf("%d %d", &n, &m);
    int P[n];
    for (int i = 0; i < n; i++)
    {
        scanf("%d", &P[i]);
    }
    QuickSort(n, m, P, compare, swap);

    for (int i = 0; i < n; i++)
    {
        printf ("%d ", P[i]);
    }
    return 0;
}

int Partition(int low, int high, int *P,
              int (*compare)(int a, int b, int *P),
              void (*swap)(int a, int b, int *P))
{
    int i = low;
    int j = low;
    while (j < high)
    {
        if (compare(j, high, P) < 0)
        {
            swap(i, j, P);
            i++;
        }
        j++;
    }
    swap(i, high, P);
    return i;
}

void QuickSort(int n, int m, int *P,
               int (*compare)(int a, int b, int *P),
               void (*swap)(int a, int b, int *P))
{
    QuickSortRec(0, n-1, m, P, compare, swap);
}

void QuickSortRec(int low, int high, int m, int *P,
                  int (*compare)(int a, int b, int *P),
                  void (*swap)(int a, int b, int *P))
{
    int q;
    while (low < high)
    {
        if (high - low + 1 < m)
        {
            SelectSort(low, high, P, compare, swap);
            break;
        }
        q = Partition(low, high, P, compare, swap);
        if ((q - low) < (high - q))
        {
            QuickSortRec(low, q - 1, m, P, compare, swap);
            low = q + 1;
        }
        else
        {
            QuickSortRec(q + 1, high, m, P, compare, swap);
            high = q - 1;
        }
    }
}

void SelectSort(int low, int high, int *P,
                int (*compare)(int a, int b, int *P),
                void (*swap)(int a, int b, int *P))
{
    int j = high;
    while (j > low)
    {
        int k = j;
        int i = j - 1;
        while (i >= low)
        {
            if (compare(k, i, P) < 0)
            {
                k = i;
            }
            i--;
        }
        swap(j, k, P);
        j--;
    }
}