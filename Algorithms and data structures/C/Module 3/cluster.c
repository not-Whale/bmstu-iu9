#include <stdio.h>
#include <stdlib.h>

struct PriorityElem
{
    int start;
    int end;
    int sum;
};

struct PriorityElem *InitQueue(int n);

void swap(struct PriorityElem *A, int i, int j);
int compare(struct PriorityElem *A, int i, int j);

void Insert(struct PriorityElem *A, int start, int end, int sum, int *count);
int ExtractMin(struct PriorityElem *A, int *count);

void Heapify(struct PriorityElem *A, int i, int n);
void BuildHeap(struct PriorityElem *A, int n);

int main()
{
    int n;
    int m;
    scanf("%d", &n);
    scanf("%d", &m);
    if (n > m)
    {
        n = m;
    }
    int count = n;
    int start;
    int end;
    int sum;
    struct PriorityElem *A = InitQueue(n);
    for (int i = 0; i < n; i++)
    {
        scanf("%d %d", &start, &end);
        A[i].start = start;
        A[i].end = end;
        A[i].sum = start + end;
    }
    BuildHeap(A, n);
    for (int i = n; i < m; i++)
    {
        scanf("%d %d", &start, &end);
        int last = ExtractMin(A, &count);
        if (last > start)
        {
            start = last;
        }
        sum = start + end;
        Insert(A, start, end, sum, &count);
    }
    int max = 0;
    for (int i = 0; i < n; i++)
    {

        if (i == 0)
        {
            max = A[0].sum;
        }
        if (A[i].sum > max)
        {
            max = A[i].sum;
        }

        //printf("A[%d].sum = %d\n", i, A[i].sum);
    }
    printf("%d\n", max);
    free(A);
    return 0;
}

struct PriorityElem *InitQueue(int n)
{
    struct PriorityElem *Out = (struct PriorityElem*)malloc(n*sizeof(struct PriorityElem));
    return Out;
}

void Insert(struct PriorityElem *A, int start, int end, int sum, int *count)
{
    int i = (*count);
    (*count) = i + 1;
    A[i].start = start;
    A[i].end = end;
    A[i].sum = sum;
    while ((i > 0) && (compare(A, i, (i-1)/2)))
    {
        swap(A, (i-1)/2, i);
        i = (i-1)/2;
    }
}

void swap(struct PriorityElem *A, int i, int j)
{
    struct PriorityElem B;
    B = A[i];
    A[i] = A[j];
    A[j] = B;
}

int compare(struct PriorityElem *A, int i, int j)
{
    return (A[i].sum < A[j].sum);
}

int ExtractMin(struct PriorityElem *A, int *count)
{
    int res;
    int c;
    res = A[0].sum;
    (*count) = (*count) - 1;
    c = (*count);
    if (c > 0)
    {
        A[0] = A[c];
        Heapify(A, 0, *count);
    }
    return res;
}

void Heapify(struct PriorityElem *A, int i, int n)
{
    while (1)
    {
        int l = 2*i + 1;
        int r = 2*i + 2;
        int j = i;
        if ((l < n) && (compare(A, l, i)))
        {
            i = l;
        }
        if ((r < n) && (compare(A, r, i)))
        {
            i = r;
        }
        if (i == j)
        {
            break;
        }
        swap(A, i, j);
    }
}

void BuildHeap(struct PriorityElem *A, int n)
{
    int i = (n/2) - 1;
    while (i >= 0)
    {
        Heapify(A, i, n);
        i--;
    }
}