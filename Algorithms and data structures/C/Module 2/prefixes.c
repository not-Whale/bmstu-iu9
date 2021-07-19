#include <stdio.h>
#include <stdlib.h>
#include <string.h>

void Prefix(char* S, int *pi, int nS);
int gcd(int a, int b);
void FindAndPrint(char *S, int *pi, int nS);

int main(int argc, char* argv[])
{
    int nS = strlen(argv[1]);
    char S[nS];
    for (int i = 0; i < nS; i++)
    {
        S[i] = argv[1][i];
    }
    int pi[nS];
    Prefix(S, pi, nS);
    FindAndPrint(S, pi, nS);
    return 0;
}

int gcd(int a, int b)
{
    int c, d;
    c = a%b;
    d = b%a;
    if (c == 0)
    {
        return d;
    } 
    else if (d == 0)
    {
        return c;
    } 
    else if (c > d)
    {
        gcd(a, d);
    } 
    else
    {
        gcd(c, b);
    }
}

void Prefix(char* S, int *pi, int nS)
{
    int t = 0;
    for (int i = 0; i < nS; i++)
    {
        pi[i] = 0;
    }
    int i = 1;
    while (i < nS)
    {
        while ((t > 0) && (S[t] != S[i]))
        {
            t = pi[t-1];
        }
        if (S[t] == S[i])
        {
            t++;
        }
        pi[i] = t;
        i++;
    }
}

void FindAndPrint(char *S, int *pi, int nS)
{
    int num;
    int flag = 1;
    for (int i = 0; i < nS; i++)
    {
        num = i + 1;
        if (pi[i] != 0)
        {
            int q = gcd(num, pi[i]);
            flag = 0;
            for (int j = 0; j < i - q; j++)
            {
                if (S[j] != S[j+q])
                {
                    flag = 1;
                }
            }
            if (flag == 0)
            {
                printf("%d %d\n", num, num/gcd(num, pi[i]));
            }
        }
    }
}