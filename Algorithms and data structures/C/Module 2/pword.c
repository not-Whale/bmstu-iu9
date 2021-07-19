#include <stdio.h>
#include <stdlib.h>
#include <string.h>

void Prefix(char *S, int *pi, int nS);
int AllPref(char *ST, int nS, int nST);

int main(int argc, char *argv[])
{
    int nS = strlen(argv[1]);
    int nT = strlen(argv[2]);
    int nST = nS + nT;
    char ST[nST];
    int i = 0;
    while (i < nS)
    {
        ST[i] = argv[1][i];
        i++;
    }
    i = 0;
    while (i < nT)
    {
        ST[i+nS] = argv[2][i];
        i++;
    }
    int flag;
    flag = AllPref(ST, nS, nST);
    if (flag)
    {
        printf("yes");
    }
    else
    {
        printf("no");
    }
    return 0;
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

int AllPref(char *ST, int nS, int nST)
{
    int piST[nST];
    Prefix(ST, piST, nST);
    int counter = 0;
    for (int j = nS; j < nST; j++)
    {
        if (piST[j] != 0)
        {
            counter++;
        }
        else
        {
            return 0;
        }
    }
    return counter;
}