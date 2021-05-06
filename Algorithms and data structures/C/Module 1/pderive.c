#include <stdio.h>
#include <stdlib.h>

long fact (long a, short b)
{
    long c = 1;
    while (b>0)
    {
        c *= a;
        if (a>2)
            a--;
        else return c;
        b--;
    }
    return c;
}

int main()
{
    long x0, n, d, res = 0;
    short k;
    scanf ("%ld %hd %ld", &n, &k, &x0);
    int i;
    for (i=0; i<n-k; i++)
    {
       scanf ("%ld", &d);
       res = (res + d * fact(n-i, k))*x0;
    }
    scanf ("%ld", &d);
    res += d * fact(n-i, k);
    printf ("%ld", res);
    return 0;
}