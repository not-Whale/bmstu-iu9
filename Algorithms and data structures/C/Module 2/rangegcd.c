#include <stdio.h>
#include <stdlib.h>
#include <math.h>

int gcd(int a, int b);
void SparseTable_Build(int *D, int** ST, int n);
int SparseTable_Query(int** ST, int l, int r);


int main()
{
    int n;
    scanf("%d", &n);
    int* D = (int*)malloc(n*sizeof(int));
    for (int i = 0; i < n; i++)
    {
        scanf("%d", &D[i]);
    }
    int b = log2(n) + 1;
    int** ST = (int**)malloc(n*sizeof(int*));
    for (int i = 0; i < n; i++)
    {
        ST[i] = (int*)malloc(b*sizeof(int));
    }
    SparseTable_Build(D, ST, n);
    int m;
    scanf("%d", &m);
    for (int i = 0; i < m; i++)
    {
        int l, r;
        scanf("%d%d", &l, &r);
        int res;
        res = SparseTable_Query(ST, l, r);
        printf("%d\n", res);
    }
    for (int i = 0; i < n; i++)
    {
        free(ST[i]);
    }
    free(ST);
    free(D);
    return 0;
}

int gcd(int a, int b)
{
    if (a == 0)
    {
        return b;
    }
    else if (b == 0)
    {
        return a;
    }
    else if (a == b)
    {
        return a;
    }
    else if ((a == 1) || (b == 1))
    {
        return 1;
    }
    else if (a > b)
    {
        return gcd(a%b, b);
    }
    else
    {
        return gcd(a, b%a);
    }
}

void ComputeLogs(int m, int *lg)
{
    int i = 1;
    int j = 0;
    while (i <= m)
    {
        int x = 1 << i;
        while (j < x)
        {
            lg[j] = i - 1;
            j++;
        }
        i++;
    }
}

void SparseTable_Build(int *D,
                       //int *lg,
                       int** ST, int n)
{
    int m = log2(n) + 1;
    int i = 0;
    while (i < n)
    {
        ST[i][0] = abs(D[i]);
        i++;
    }
    int j = 1;
    int x = 1;
    while (j < m)
    {
        i = 0;
        x = x << 1;
        while (i <= n - x)
        {
            int y = x >> 1;
            ST[i][j] = gcd(ST[i][j-1], ST[i+y][j-1]);
            i++;
        }
        j++;
    }
}

int SparseTable_Query(int** ST, int l, int r
                      //, int *lg
                      )
{
    int j = log2(r - l + 1);
    int p = 1 << j;
    return gcd(ST[l][j], ST[r - p + 1][j]);
}