#include <stdio.h>

int main()
{
    char a;
    unsigned long s1 = 0, s2 = 0, x = 1;
    scanf ("%c", &a);
    while (a != 32)
    {
        if (a>=65 && a<=90)
        {
            s1 = s1 | (x << (a-65));
        }
        else
        {
            s1 = s1 | (x << (a-71));
        }
        scanf ("%c", &a);
    }
    scanf ("%c", &a);
    while (a != 10)
    {
        if (a>=65 && a<=90)
        {
            s2 = s2 | (x << (a-65));
        }
        else
        {
            s2 = s2 | (x << (a-71));
        }
        scanf ("%c", &a);
    }
    s1 = s1 & s2;
    for (int i=0; i<26; i++)
    {
        if (s1 & (x << i))
        {
            printf ("%c", i + 65);
        }
    }
    for (int i=26; i<52; i++)
    {
        if (s1 & (x << i))
        {
            printf ("%c", i + 71);
        }
    };
    return 0;
}