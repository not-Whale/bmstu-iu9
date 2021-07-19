#include <stdio.h>
#include <stdlib.h>

int SegmentTree_Query(int *D, int n, int l, int r);
int query(int *D, int l, int r, int a, int b, int x);

void SegmentTree_Build(int *In, int *D, int n);
void build(int *In, int a, int b, int *D, int x);

void SegmentTree_Update(int i, int v, int *D, int n);
void update(int i, int v, int a, int b, int *T, int x);

int max(int a, int b)
{
    return (a > b) ? a : b;
}


int main()
{
    int nT;
    scanf ("%d", &nT);
    int T[nT];
    for (int i = 0; i < nT; i++)
    {
        scanf("%d", &T[i]);
    }
    int *D = (int*)malloc(4*nT*sizeof(int));
    SegmentTree_Build(T, D, nT);
    int nOpp;
    scanf("%d ", &nOpp);
    for (int j = 0; j < nOpp; j++)
    {
        char Opp[4];
        for (int i = 0; i < 4; i++)
        {
            scanf("%c", &Opp[i]);
        }
        if (Opp[0] == 77)
        {
            int l, r;
            scanf("%d%d", &l, &r);
            int m;
            m = SegmentTree_Query(D, nT, l, r);
            printf ("%d\n", m);
        }
        else if (Opp[0] == 85)
        {
            int i, v;
            scanf("%d%d", &i, &v);
            SegmentTree_Update(i, v, D, nT);
        }
        if (j != nOpp-1) scanf("\n");
    }
    free(D);
    return 0;
}

int SegmentTree_Query(int *D, int n, int l, int r)
{
    int v;
    v = query(D, l, r, 0, n - 1, 0);
    return v;
}

int query(int *D, int l, int r, int a, int b, int x)
{
    int v;
    if ((l == a) && (r == b))
    {
        v = D[x];
    }
    else
    {
        int m;
        m = (a+b)/2;
        if (r <= m)
        {
            v = query(D, l, r, a, m, 2*x+1);
        }
        else if (l > m)
        {
            v = query(D, l, r, m+1, b, 2*x+2);
        }
        else
        {
            v = max(query(D, l, m, a, m, 2*x+1), query(D, m+1, r, m+1, b, 2*x+2));
        }
    }
    return v;
}

void SegmentTree_Build(int *In, int *D, int n)
{
    build(In, 0, n - 1, D, 0);
}

void build(int *In, int a, int b, int *D, int x)
{
    if (a == b)
    {
        D[x] = In[a];
    }
    else
    {
        int m;
        m = (a+b)/2;
        build(In, a, m, D, 2*x+1);
        build(In, m+1, b, D, 2*x+2);
        D[x] = max(D[2*x+1], D[2*x+2]);
    }
}

void SegmentTree_Update(int i, int v, int *D, int n)
{
    update(i, v, 0, n-1, D, 0);
}

void update(int i, int v, int a, int b, int *D, int x)
{
    if (a == b)
    {
        D[x] = v;
    }
    else
    {
        int m;
        m = (a+b)/2;
        if (i <= m)
        {
            update(i, v, a, m, D, 2*x+1);
        }
        else
        {
            update(i, v, m+1, b, D, 2*x+2);
        }
        D[x] = max(D[2*x+1], D[2*x+2]);
    }
}