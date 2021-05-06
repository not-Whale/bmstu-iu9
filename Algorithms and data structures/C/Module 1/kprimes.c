#include <stdio.h>
#include <stdlib.h>

int main()
{
    short k;
    int n;
    scanf ("%hd %d", &k, &n);
    char arr[n+1];
    for (int i=0; i<=n; i++)
    {
        arr[i] = 1;
    }
    for (int j=2; j*j<=n; j++)
    {
        for (int k=j*j; k<=n; k+=j)
        {
            arr[k] = arr[j] + arr[k/j];
        }
    }
    for (int i=2; i<=n; i++)
    {
        if (arr[i] == k) printf ("%d ", i);
    }
    return 0;
}