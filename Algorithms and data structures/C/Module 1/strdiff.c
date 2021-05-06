#include  <stdio.h>

int strdiff(char *a, char *b);

int main(int argc, char **argv)
{
        char s1[1000], s2[1000];
        gets(s1);
        gets(s2);
        printf("%d\n", strdiff(s1, s2));

        return 0;
}

int strdiff(char *a, char *b)
{
    int i=0;
    do
    {
        if (a[i] == b[i])
        {
            i++;
        }
        else
        {
            int k=0;
            while (k<8)
            {
                if ((a[i] & (1 <<k)) == (b[i] & (1 << k)))
                {
                    k++;
                }
                else return (i*8+k);
            }
        }
    } while ((a[i] != 0) || (b[i] != 0));
    return -1;
} 