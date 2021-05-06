#include <stdio.h>

int main()
{
    int n, m, x;
    scanf ("%d %d", &m, &n);
    int min[n], max[m], maxl, minl, nmax, nmin, flag = 1;
    scanf ("%d", &x);
    min [0] = x;
    max [0] = x;
    if ((m==1) && (n==1)) { printf ("0 0"); }
    else
    {
        for (int i=1; i<n; i++)
        {
            scanf ("%d", &x);
            min[i%n] = x;
            if (x>max[i/n]) max [i/n] = x;
        }
        for (int j=n; j<n*m; j++)
        {
            scanf ("%d", &x);
            if (j%n == 0) { max[j/n] = x; }
            if (x<min[j%n]) { min[j%n]=x; }
            if (x>max[j/n]) { max[j/n]=x; }
        }
        minl = min[0];
        nmin = 0;
        for (int i=1; i<n; i++)
        {
            if (min[i] > minl) { minl = min[i]; nmin = i; flag = 1; }
            else if (min[i] == minl) { flag = 0; };
        }
        maxl = max[0];
        nmax = 0;
        for (int j=1; j<m; j++)
        {
            if (max[j] < maxl) { maxl = max[j]; nmax = j; flag = 1; }
            else if (max[j] == maxl) { flag = 0; };
        }
        if ((minl == maxl) && (flag == 1)) printf ("%d %d", nmax, nmin);
        else printf ("none");
    }
    return 0;
}