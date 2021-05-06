#include  <stdio.h>

int array[] = { 1, 2, 30, 45, 50, 51, 55, 60 };
const int k = 51;

int compare(unsigned long i)
{
        if (array[i] == k) return 0;
        if (array[i]  < k) return -1;
        return 1;
}

unsigned long binsearch(unsigned long, int (*)(unsigned long));

int main(int argc, char  **argv)
{
        printf("%lu\n", binsearch(8, compare));
        return 0;
}

unsigned long binsearch(unsigned long nel, int (*compare)(unsigned long i))
{
    char flag = 0;
    unsigned long a = 0, b = nel, c;
    while ((a<=b) && (flag != 1))
    {
        c = (a+b)/2;
        //printf ("a = %lu, b = %lu, c = %lu\n", a, b, c);
        if (compare (c) == 0) { flag = 1; };
        if (compare (c) == 1) { b = c - 1; }
        else { a = c + 1; };
    }
    if (flag) { return c; }
    else { return nel; };
} 