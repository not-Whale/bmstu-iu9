#include <stdio.h>
#include <stdlib.h>
#include <math.h>

int main()
{
    int n;
    int l = 0;
    int r = 0;
    int start = 0;
    float sum = 0;
    float maxsum = 0;
    int a, b;
    char x;
    scanf ("%d", &n);
    int i = 0;
    while (i < n)
    {
        scanf("%d%c%d", &a, &x, &b);
        if (i == 0) { maxsum = maxsum + logf(a) - logf(b); };

        sum = sum + logf(a) - logf(b);
        if (sum > maxsum)
        {
            maxsum = sum;
            l = start;
            r = i;
        }
        i++;
        if (sum < 0)
        {
            sum = 0;
            start = i;
        }
    }
    printf("%d %d", l, r);
}