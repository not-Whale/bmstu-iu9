#include <stdio.h>

int array[] = {
        1000000000,
        2000000000,
        3000000000,
        4000000000,
        5000000000
};

void revarray(void*, unsigned long, unsigned long);

int main(int argc, char **argv)
{
        revarray(array, 5, sizeof(int));

        int i;
        for (i = 0; i < 5; i++) {
                printf("%d\n", array[i]);
        }

        return 0;
}

void revarray(void *base, unsigned long nel, unsigned long width)
{
    for (int i=0; i<nel/2; i++)
    {
        char x;
        for (int j=0; j<width; j++)
        {
            x = *((char*)base + i*width + j);
            *((char*)base + i*width + j) = *((char*)base + (nel-1-i)*width + j);
            *((char*)base + (nel-1-i)*width + j) = x;
        }
    }
}