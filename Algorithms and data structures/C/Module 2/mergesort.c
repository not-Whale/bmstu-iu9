#include <stdio.h>
#include <stdlib.h>

void Merge (int (*compare)(int a, int b, int *base),
            int k, int l, int m,
            int *base,
            void (*swap)(int a, int b, int *base));

void MergeSort(int (*compare)(int a, int b, int *base),
               int n,
               int *base,
               void (*swap)(int a, int b, int *base));

void MergeSortRec(int (*compare)(int a, int b, int *base),
                  int low, int high,
                  int *base,
                  void (*swap)(int a, int b, int *base));

void InsertSort(int (*compare)(int a, int b, int *base),
                int low, int high, int *base,
                void (*swap)(int a, int b, int *base));
                
int compare(int a, int b, int *base);

void swap(int a, int b, int *base);


int main()
{
    int num;
    scanf("%d", &num);
    int base[num];
    for (int i = 0; i < num; i++)
    {
        scanf("%d", &base[i]);
    }

    MergeSortRec(compare, 0, num-1, base, swap);

    for (int j = 0; j < num; j++)
    {
        printf ("%d", base[j]);
        if (j != num-1) { printf (" "); };
    }
    return 0;
}

void Merge(int (*compare)(int a, int b, int *base),
            int k, int l, int m,
            int *base,
            void (*swap)(int a, int b, int *base))
{
    int T[m - k + 1];
    int i = k;
    l++;
    int j = l;
    int h = 0;
    while (h < m - k + 1)
    {
        if ((j <= m) && ((i == l) || (compare(j, i, base) < 0)))
        {
            T[h] = base[j];
            j++;
        }
        else
        {
            T[h] = base[i];
            i++;
        }
        h++;
    }
    for (int p = 0; p < h; p++)
    {
        base[k+p] = T[p];
    }
}

void MergeSort(int (*compare)(int a, int b, int *base),
               int n,
               int *base,
               void (*swap)(int a, int b, int *base))
{
    MergeSortRec(compare, 0, n-1, base, swap);
}

void MergeSortRec(int (*compare)(int a, int b, int *base),
                  int low, int high,
                  int *base,
                  void (*swap)(int a, int b, int *base))
{
    if (high-low+1 >= 5)
    {
        int med = (high+low)/2;
        MergeSortRec(compare, low, med, base, swap);
        MergeSortRec(compare, med+1, high, base, swap);
        Merge(compare, low, med, high, base, swap);
    }
    else
    {
        InsertSort(compare, low, high, base, swap);
    }
}

void InsertSort(int (*compare)(int a, int b, int *base),
                int low, int high, int *base,
                void (*swap)(int a, int b, int *base))
{
    int i = low+1;
    while (i <= high)
    {
        int loc = i - 1;
        while ((loc>=low) && (compare(loc, loc+1, base) > 0))
        {
            int c = base[loc+1];
            base[loc+1] = base[loc];
            base[loc] = c;
            loc--;
        }
        i++;
    }
}

int compare(int a, int b, int *base)
{
    return (abs(base[a]) - abs(base[b]));
}

void swap(int a, int b, int *base)
{
    int c;
    c = base[a];
    base[a] = base[b];
    base[b] = c;
}