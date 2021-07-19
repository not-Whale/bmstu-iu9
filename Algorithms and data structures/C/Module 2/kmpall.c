#include <stdio.h>
#include <stdlib.h>
#include <string.h>

void KMPSubst(char *S, char *T);

void Prefix(char *S, int *pi, int nS);


int main(int argc, char* argv[])
{
    char S[1000] = { 0 };
    char T[1000] = { 0 };

    int nS = strlen(argv[1]);
    int nT = strlen(argv[2]);
    int i = 0;
    while (i < nS)
    {
        S[i] = argv[1][i];
        i++;
    }
    i = 0;
    while (i < nT)
    {
        T[i] = argv[2][i];
        i++;
    }
    KMPSubst(S, T);
    return 0;
}

void KMPSubst(char *S, char *T)
{
    int nS = strlen(S);
    int nT = strlen(T);
    int pi[nS];
    Prefix(S, pi, nS);
    int q = 0;
    int k = 0;
    while (k < nT)
    {
        while ((q > 0) && (S[q] != T[k]))
        {
            q = pi[q-1];
        }
        if (S[q] == T[k])
        {
            q++;
        }
        if (q == nS)
        {
            k = k - nS + 1;
            printf ("%d\n", k);
            q = 0;
        }
        k++;
    }
}

void Prefix(char *S, int *pi, int nS)
{
    int t = 0;
    pi[0] = 0;
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
        pi[i]=t;
        i++;
    }
}