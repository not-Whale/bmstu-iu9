#include <stdio.h>
#include <stdlib.h>

int main()
{
    short a, b, c = 1;
    long n;
    scanf ("%ld", &n);
    scanf ("%hd", &a);
    if (n==1)
    {
        if (a==1)
        {
            printf ("0 1 ");
            return 0;
        }
        else
        {
            printf ("1 ");
            return 0;
        }
    }
    scanf ("%hd", &b);
    int i = 3;
    if ((a==0) & (b==0))
    {
        printf ("1 0 ");
    }
    else if ((a==1) & (b==0))
    {
        printf ("0 ");
    }
    else if ((a==0) & (b==1))
    {
        printf ("0 0 ");
        a = 1;
        if (n>2) scanf ("%hd ", &b);
        i++;
    }
    if (n==2) printf ("%hd", a);
    while (i<=n)
    {
        scanf ("%hd", &b);
        if ((a==1) & (b==1))
        {
            printf ("0 0 ");
            i += 2;
            if (i>n)
            {
                printf ("1 ");
            }
            else scanf ("%hd", &b);
        }
        else if ((a==1) & (b==0) & (c==1))
        {
            printf ("%hd ", a);
            printf ("%hd ", b);
            a = b;
            c = 0;
            i++;
        }
        else
        {
            printf ("%hd ", b);
            a = b;
            i++;
        }
    }
    return 0;
}