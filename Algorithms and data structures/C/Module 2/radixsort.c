#include <stdio.h>
#include <stdlib.h>

union Int32 {
    int x;
    unsigned char bytes[4];
};

void RadixSort(int q, int m, int n,
               union Int32 *Arr);

void DistributionSort(int m, int n,
                      union Int32 *Arr,
                      int number);

int main()
{
    int n;
    scanf("%d", &n);
    union Int32 Arr[n];
    for (int i = 0; i < n; i++)
    {
        scanf("%d", &Arr[i].x);
    }
    RadixSort(4, 256, n, Arr);
    for (int i = 0; i < n; i++)
    {
        printf("%d ", Arr[i].x);
    }
    return 0;
}

void DistributionSort(int m, int n,
                      union Int32 *Arr,
                      int number)
{
    int k;
    int count[m];
    for (int i = 0; i < m; i++)
    {
        count[i] = 0;
    }
    int j = 0;
    while (j < n)
    {
        if (number == 3)
        {
            k = (Arr[j].bytes[number] + 128) % 256;
        }
        else
        {
            k = Arr[j].bytes[number];
        }
        count[k]++;
        j++;
    }
    int i = 1;
    while (i < m)
    {
        count[i] = count[i] + count[i-1];
        i++;
    }
    union Int32 D[n];
    j = n - 1;
    while (j >= 0)
    {
        if (number == 3)
        {
            k = (Arr[j].bytes[number] + 128) % 256;
        }
        else
        {
            k = Arr[j].bytes[number];
        }
        count[k]--;
        D[count[k]] = Arr[j];
        j--;
    }
    for (int i = 0; i < n; i++)
    {
        Arr[i] = D[i];
    }
}

void RadixSort(int q, int m, int n,
               union Int32 *Arr)
{
    int i = 0;
    while (i < q)
    {
        DistributionSort(m, n, Arr, i);
        i++;
    }
}