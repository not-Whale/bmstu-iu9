#include <stdio.h>
#include <stdlib.h>

int SegmentTree_Query(int *D, int n, int l, int r);
int query(int *D, int l, int r, int a, int b, int x);

void SegmentTree_Build(int *In, int *D, int n);
void build(int *In, int a, int b, int *D, int x, int nT);

void SegmentTree_Update(int *In, int i, int *D, int n);
void update(int *In, int i, int a, int b, int *T, int x, int nT);

int peak(int i, int *In, int n)
{
    if (n == 1)
    {
        return 1;
    }
    else if ((i == 0) && (In[i] >= In[i+1]))
    {
        return 1;
    }
    else if ((i == n-1) && (In[i] >= In[i-1]))
    {
        return 1;
    }
    else if ((i > 0) && (i < n - 1) &&
             (In[i] >= In[i+1]) &&
             (In[i] >= In[i-1]))
    {
        return 1;
    }
    return 0;
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
        char Opp[5];
        Opp[4] = 0;
        int i = 0;
        char x;
        scanf("%c", &x);
        while (x != 32)
        {
            Opp[i] = x;
            scanf("%c", &x);
            i++;
        }
        if (Opp[0] == 80)
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
            T[i] = v;
            SegmentTree_Update(T, i, D, nT);
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
            v = query(D, l, m, a, m, 2*x+1) + query(D, m+1, r, m+1, b, 2*x+2);
        }
    }
    return v;
}

void SegmentTree_Build(int *In, int *D, int n)
{
    build(In, 0, n - 1, D, 0, n);
}

void build(int *In, int a, int b, int *D, int x, int nT)
{
    if (a == b)
    {
        D[x] = peak(a, In, nT);
    }
    else
    {
        int m;
        m = (a+b)/2;
        build(In, a, m, D, 2*x+1, nT);
        build(In, m+1, b, D, 2*x+2, nT);
        D[x] = D[2*x+1] + D[2*x+2];
    }
}

void SegmentTree_Update(int *In, int i, int *D, int n)
{
    update(In, i, 0, n-1, D, 0, n);
    if (i < n-1)
    {
        update(In, i+1, 0, n-1, D, 0, n);
    }
    if (i > 0)
    {
        update(In, i-1, 0, n-1, D, 0, n);
    }
}

void update(int *In, int i, int a, int b, int *D, int x, int nT)
{
    if (a == b)
    {
        D[x] = peak(i, In, nT);
    }
    else
    {
        int m;
        m = (a+b)/2;
        if (i <= m)
        {
            update(In, i, a, m, D, 2*x+1, nT);
        }
        else
        {
            update(In, i, m+1, b, D, 2*x+2, nT);
        }
        D[x] = D[2*x+1] + D[2*x+2];
    }
}