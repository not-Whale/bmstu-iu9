#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int len[100];
int num[100];
int n;

void csort(char *src, char *dest);

int main()
{
    char string[1000] = { 0 };
    char x;
    int line = 0;
    int sym = 0;
    int flag = 1;
    int length = 0;
    scanf ("%c ", &x);
    if (x == 32) { scanf ("%c", &x); };
    while (x != 10)
    {
        if (x != 32)
        {
            if (flag == 1)
            {
                num[line] = sym;
            }
            string[sym] = x;
            length++;
            flag = 0;
        }
        else
        {
            if (flag == 0)
            {
                len[line] = length;
                line ++;
                length = 0;
            }
            flag = 1;
        }
        sym++;
        scanf ("%c", &x);
    }
    len[line] = length;
    line++;

    n = line;
    char bufer[1000] = { 0 };
    csort(string, bufer);

    for (int i = 0; i < sym; i++)
    {
        printf ("%c", bufer[i]);
    }

    return 0;
}

void csort(char *src, char *dest)
{
    int count[100] = { 0 };
    int j = 0;
    while (j < n-1)
    {
        int i = j+1;
        while (i < n)
        {
            if (len[i] < len[j])
            {
                count[j]++;
            }
            else
            {
                count[i]++;
            }
            i++;
        }
        j++;
    }

    int pos = 0;
    for (int h = 0; h < n; h++)
    {
        for (int b = 0; b < n; b++)
        {
            if (count[b] == h)
            {
                for (int g = 0; g < len[b]; g++)
                {
                    dest[pos] = src[g+num[b]];
                    pos++;
                }
                dest[pos] = 32;
                pos++;
            }
        }
    }
}