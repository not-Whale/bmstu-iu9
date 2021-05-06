#include <stdio.h>
#include <stdlib.h>

int main()
{
    long n, a, b, x, y;
    scanf ("%ld", &n);
    scanf ("%ld %ld", &x, &y);
    for (int i=1; i<n; i++)
    {
        scanf ("%ld %ld", &a, &b);
        if (a > (y+1))
        {
            printf ("%ld %ld\n", x, y);
            x = a;
            y = b;
        }
        else if (b >= (y+1))
        {
            y = b;
        }
    }
    printf ("%ld %ld", x, y);
    return 0;
}
