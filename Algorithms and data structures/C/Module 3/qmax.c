#include <stdio.h>
#include <stdlib.h>

int main()
{
    int *B = (int*)malloc(4*sizeof(int));
    int cap = 4;
    int top1 = 0;
    int top2 = 3;
    int n;
    scanf("%d", &n);
    for (int i = 0; i < n; i++)
    {
        char Opp[6];
        scanf("%s", &Opp);
        if ((Opp[0] == 69) && (Opp[1] == 77)) // E.M.PTY
        {
            if ((top1 == 0) && (top2 == cap-1))
            {
                printf("true\n");
            }
            else
            {
                printf("false\n");
            }
        }
        else if ((Opp[0] == 69) && (Opp[1] == 78)) // E.N.Q
        {
            int x;
            scanf("%d", &x);
            if (top1 >= top2)
            {
                int c = cap;
                cap *= 2;
                int *A = (int*)malloc(cap*sizeof(int));
                for (int h = 0; h < top1; h++)
                {
                    A[h] = B[h];
                }
                for (int g = c-1; g > top2; g--)
                {
                    A[c+g] = B[g];
                }

                top2 += c;

                B = (int*)realloc(B, cap*sizeof(int));
                for (int h = 0; h < top1; h++)
                {
                    B[h] = A[h];
                }
                for (int g = cap-1; g > top2; g--)
                {
                    B[g] = A[g];
                }
                free(A);
            }
            B[top1] = x;
            top1++;
            B[top1] = x;
            int f = 2;
            while ((top1-f >= 1) && (x > B[top1-f]))
            {
                B[top1-f] = x;
                f += 2;
            }
            int help = cap - 1;
            if (top2 != cap - 1)
            {
                while ((help > top2 + 1) && (x > B[help]))
                {
                    B[help] = x;
                    help -= 2;
                }
            }
            top1++;
        }
        else if (Opp[0] == 68) // D.EQ
        {
            if (top2 == cap-1)
            {
                int *C = (int*)malloc(top1*sizeof(int));
                for (int j = 0; j < top1; j++)
                {
                    C[j] = B[j];
                }
                while (top1 > 0)
                {
                    top1--;
                    B[top2] = C[top1];
                    top2--;
                }
                free(C);
            }
            top2 += 2;
            printf("%d\n", B[top2 - 1]);
        }
        else if (Opp[0] == 77) // M.AX
        {
            if (top2 == cap-1)
            {
                int *C = (int*)malloc(top1*sizeof(int));
                for (int j = 0; j < top1; j++)
                {
                    C[j] = B[j];
                }
                while (top1 > 0)
                {
                    top1--;
                    B[top2] = C[top1];
                    top2--;
                }
                free(C);
            }
            printf("%d\n", B[top2 + 2]);
        }
    }
    free(B);
    return 0;
} 