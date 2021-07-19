#include <stdio.h>
#include <stdlib.h>

int main()
{
    int st[30] = { 1 };
    for (int i=1; i<30; i++)
    {
        st[i] = st[i-1]*2;
    }
    int n, sum, counter = 0;
    scanf ("%d", &n);
    int arr[n];
    for (int i=0; i<n; i++)
    {
        scanf ("%d", &arr[i]);
    }
    int m = st[n];
    for (int C = 1; C < m; C++)
    {
        sum = 0;
        for (int j = 0; j < n; j++)
        {
            if (C & (1 << j)) { sum += arr[j]; };
        }
        for (int l = 0; l < 30; l++)
        {
            if (sum == st[l]) { counter++; };
        }
    }
    printf ("%d", counter);
    return 0;
}