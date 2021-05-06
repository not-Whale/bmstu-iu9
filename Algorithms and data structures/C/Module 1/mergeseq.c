#include <stdio.h>
#include <stdlib.h>

int main()
{
    int n1, n2;
    scanf ("%d", &n1);
    int *a = (int*)malloc(n1*sizeof(int));
    for (int i=0; i<n1; i++)
    {
        scanf ("%d", &a[i]);
    }
    scanf ("%d", &n2);
    int *b = (int*)malloc(n2*sizeof(int));
    for (int j=0; j<n2; j++)
    {
        scanf ("%d", &b[j]);
    }
    int i=0, j=0;
    while ((i<n1) && (j<n2))
    {
        if (a[i] < b[j])
        {
            printf ("%d ", a[i]);
            i++;
        }
        else
        {
            printf ("%d ", b[j]);
            j++;
        }
    }
    while (i<n1)
    {
        printf ("%d ", a[i]);
        i++;
    }
    while (j<n2)
    {
        printf ("%d ", b[j]);
        j++;
    }
    free (a);
    free (b);
    return 0;
}
