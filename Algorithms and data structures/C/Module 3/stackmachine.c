#include <stdio.h>
#include <stdlib.h>

int max(int a, int b)
{
    return (a > b) ? a : b;
}

int min(int a, int b)
{
    return (a < b) ? a : b;
}

int main()
{
    int* A = (int*)malloc(4*sizeof(int));
    int cap = 4;
    int top = 0;
    int n;
    scanf("%d", &n);
    for (int i = 0; i < n; i++)
    {
        char Opp[6];
        scanf("%s", Opp);
        if ((Opp[0] == 83) && (Opp[1] == 87))        // SWAP
        {
            int c;
            c = A[top-1];
            A[top-1] = A[top-2];
            A[top-2] = c;
        }
        else if (Opp[0] == 67)                       // CONST
        {
            int x;
            scanf("%d", &x);
            if (top == cap)
            {
                cap *= 2;
                A = (int*)realloc(A, cap*sizeof(int));
            }
            A[top] = x;
            top++;
        }
        else if (Opp[0] == 65)                      // ADD
        {
            top--;
            A[top-1] = A[top] + A[top-1];
        }
        else if ((Opp[0] == 83) && (Opp[1] == 85))  // SUB
        {
            top--;
            A[top-1] = A[top] - A[top-1];
        }
        else if ((Opp[0] == 77) && (Opp[1] == 85))  // MUL
        {
            top--;
            A[top-1] = A[top] * A[top-1];
        }
        else if ((Opp[0] == 68) && (Opp[1] == 73))  // DIV
        {
            top--;
            A[top-1] = A[top] / A[top-1];
        }
        else if ((Opp[0] == 77) && (Opp[1] == 65))  // MAX
        {
            top--;
            A[top-1] = max(A[top-1], A[top]);
        }
        else if ((Opp[0] == 77) && (Opp[1] == 73))  // MIN
        {
            top--;
            A[top-1] = min(A[top-1], A[top]);
        }
        else if (Opp[0] == 78)                      // NEG
        {
            A[top-1] = (-1)*A[top-1];
        }
        else if ((Opp[0] = 68) && (Opp[1] == 85))   // DUP
        {
            if (top == cap)
            {
                cap *= 2;
                A = (int*)realloc(A, cap*sizeof(int));
            }
            A[top] = A[top-1];
            top++;
        }
    }
    for (int i = 0; i < top; i++)
    {
        printf("%d\n", A[i]);
    }
    free(A);
    return 0;
} 