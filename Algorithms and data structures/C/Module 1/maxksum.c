#include <stdio.h>
#include <stdlib.h>

int main()
{
    int x, k, n, num;
    long sum=0, maxsum=0;
    scanf ("%d %d", &n, &k);
    int arr[k];
    for (int i=0; i<k; i++)
    {
        scanf ("%d", &arr[i]);
        sum += arr[i];
    };
    num = 0;
    maxsum = sum;
    for (int i=k; i<n; i++)
    {
        scanf ("%d", &x);
        sum = sum - arr[i%k] + x;
        arr[i%k]=x;
        if (sum > maxsum)
        {
            maxsum = sum;
            num = i-k+1;
        }
    }
    printf ("%d", num);
    return 0;
}