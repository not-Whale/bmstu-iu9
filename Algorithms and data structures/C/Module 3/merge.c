#include <stdio.h>
#include <stdlib.h>

struct PriorityElem
{
    int pos;
    int value;
    int arr_size;
    int arr_num;
};

void Heapify(struct PriorityElem *A, int i, int n);
void BuildHeap(struct PriorityElem *A, int n);

int main()
{
    int n;
    int m = 0;
    scanf("%d", &n);
    int counter = n;
    struct PriorityElem *A;
    A = (struct PriorityElem*)malloc(n*sizeof(struct PriorityElem));
    int max = 0;
    for (int i = 0; i < n; i++)
    {
        scanf("%d", &A[i].arr_size);
        if (max < A[i].arr_size)
        {
            max = A[i].arr_size;
        }
        m += A[i].arr_size;
    }
    int Matrix[n][max];
    for (int i = 0; i < n; i++)
    {
        for (int j = 0; j < A[i].arr_size; j++)
        {
            scanf("%d", &Matrix[i][j]);
        }
        if (A[i].arr_size != 0)
        {
            A[i].value = Matrix[i][0]; //A[i].Arr[0];
        }
        else
        {
            A[i].value = 2145000000; //2 147 483 648;
        }
        A[i].pos = 0;
        A[i].arr_num = i;
    }
    BuildHeap(A, n);
    for (int i = 0; i < m; i++)
    {
        printf("%d ", A[0].value);
        if (A[0].pos != A[0].arr_size - 1)
        {
            A[0].pos++;
            A[0].value = Matrix[A[0].arr_num][A[0].pos];  // A[0].Arr[A[0].pos];
            Heapify(A, 0, counter);
        }
        else
        {
            counter--;
            if (counter != 0)
            {
                A[0] = A[counter];
                Heapify(A, 0, counter);
            }
        }
    }
    free(A);
    return 0;
}

void BuildHeap(struct PriorityElem *A, int n)
{
    int i = (n/2)-1;
    while (i >= 0)
    {
        Heapify(A, i, n);
        i--;
    }
}

void Heapify(struct PriorityElem *A, int i, int n)
{
    while (1)
    {
        int l = 2*i + 1;
        int r = 2*i + 2;
        int j = i;
        if ((l < n) && (A[i].value > A[l].value))
        {
            i = l;
        }
        if ((r < n) && (A[i].value > A[r].value))
        {
            i = r;
        }
        if (i == j)
        {
            break;
        }
        struct PriorityElem B;
        B = A[i];
        A[i] = A[j];
        A[j] = B;
    }
}