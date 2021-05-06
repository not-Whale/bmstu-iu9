#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int main(int argc, char* argv[])
{
    if (argc != 4) { printf ("Usage: frame <height> <width> <text>"); }
    else
    {
        unsigned long length, width, strl;
        length = atoi(argv[2]);
        width = atoi (argv[1]);
        strl = strlen(argv[3]);
        if ((strl > length-2) || (width < 3))
        {
            printf ("Error");
        }
        else
        {
            for (int i=0; i<length; i++)
            {
                printf ("*");
            }
            printf ("\n");
            int j=1;
            int k;
            if (width%2) { k = width/2; }
            else { k = width/2 - 1; };
            for (j; j<k; j++)
            {
                printf ("*");
                for (int k=0; k<length-2; k++)
                {
                    printf (" ");
                }
                printf ("*\n");
            }
            printf ("*");
            int l=1;
            for (l; l<(length-strl)/2; l++)
            {
                printf (" ");
            }
            printf ("%s", argv[3]);
            for (l+strl; l<length-strl-1; l++)
            {
                printf (" ");
            }
            printf ("*\n");
            for (j; j<width-2; j++)
            {
                printf ("*");
                for (int k=0; k<length-2; k++)
                {
                    printf (" ");
                }
                printf ("*\n");
            }
            for (int i=0; i<length; i++)
            {
                printf ("*");
            }
        }
    }
    return 0;
}