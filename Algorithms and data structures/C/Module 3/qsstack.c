#include <stdio.h>
#include <stdlib.h>

struct Task
{
    int low;
    int high;
};

struct Stack
{
    int cap;
    int top;
    struct Task *T;
};

void QuickSort(int *A, int low, int high);
int Partition(int *A, int low, int high);

void Push(struct Stack *S, int low, int high);
struct Task Pop(struct Stack *S);

void Swap(int *A, int i, int j);


int main()
{
    int n;
    scanf("%d", &n);
    int A[n];
    for (int i = 0; i < n; i++)
    {
        scanf("%d", &A[i]);
    }
    QuickSort(A, 0, n-1);
    for (int i = 0; i < n; i++)
    {
        printf("%d ", A[i]);
    }
    return 0;
}

void QuickSort(int *A, int low, int high)
{
    struct Stack *S = (struct Stack*)malloc(sizeof(struct Stack));
    S->cap = 256;
    S->top = 0;
    S->T = (struct Task*)malloc(S->cap*sizeof(struct Task));

    struct Task t;
    t.low = low;
    t.high = high;

    Push(S, low, high);
    while(S->top > 0)
    {
        t = Pop(S);
        if (t.high <= t.low)
        {
            continue;
        }
        int i = Partition(A, t.low, t.high);
        if (i - t.low > t.high - i)
        {
            Push(S, t.low, i - 1);
            Push(S, i + 1, t.high);
        }
        else
        {
            Push(S, i + 1, t.high);
            Push(S, t.low, i - 1);
        }
    }
    free(S->T);
    free(S);
}

int Partition(int *A, int low, int high)
{
    int i = low;
    int j = low;
    while (j < high)
    {
        if (A[j] < A[high])
        {
            Swap(A, i, j);
            i++;
        }
        j++;
    }
    Swap(A, i, high);
    return i;
}

void Push(struct Stack *S, int low, int high)
{
    S->T[S->top].low = low;
    S->T[S->top].high = high;
    S->top++;
}

struct Task Pop(struct Stack *S)
{
    struct Task Out;
    S->top--;
    Out.low = S->T[S->top].low;
    Out.high = S->T[S->top].high;
    return Out;
}

void Swap(int *A, int i, int j)
{
    int c;
    c = A[i];
    A[i] = A[j];
    A[j] = c;
}