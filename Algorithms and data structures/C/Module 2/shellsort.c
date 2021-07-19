#include <stdlib.h> 
#include <stdio.h> 
 
int *array; 

int fib[] = { 	0, 1, 2, 3, 5, 8, 13, 21, 34,
                55, 89, 144, 233, 377, 610, 987,
                1597, 2584, 4181, 6765, 10946, 17711,
                28657, 46368, 75025, 121393, 196418,
                317811, 514229, 832040, 1346269, 2178309,
                3524578, 5702887, 9227465, 14930352,
                24157817, 39088169, 63245986, 102334155 };
 
int compare(unsigned long i, unsigned long j) 
{ 
        if (i <= j) { 
                printf("COMPARE␣%d␣%d\n", i, j); 
        } else { 
                printf("COMPARE␣%d␣%d\n", j, i); 
        } 
 
        if (array[i] == array[j]) return 0; 
        return array[i] < array[j] ? -1 : 1; 
} 
 
void swap(unsigned long i, unsigned long j) 
{ 
        if (i <= j) { 
                printf("SWAP␣%d␣%d\n", i, j); 
        } else { 
                printf("SWAP␣%d␣%d\n", j, i); 
        } 
 
        int t = array[i]; 
        array[i] = array[j]; 
        array[j] = t; 
} 
 
void shellsort(unsigned long, 
        int (*)(unsigned long, unsigned long), 
        void (*)(unsigned long, unsigned long)); 
 
int main(int argc, char **argv) 
{ 
        int i, n; 
        scanf("%d", &n); 
 
        array = (int*)malloc(n * sizeof(int)); 
        for (i = 0; i < n; i++) scanf("%d", array+i); 
 
        shellsort(n, compare, swap); 
        for (i = 0; i < n; i++) printf("%d␣", array[i]); 
        printf("\n"); 
 
        free(array); 
        return 0; 
}

int fibo_num (unsigned long nel)
{
    int num = 1;
    int i = 2;
    while ((i<nel) && (fib[i]<nel))
    {
        num = i;
        i++;
    }
    return num;
}

void shellsort(unsigned long nel,
        int (*compare)(unsigned long i, unsigned long j),
        void (*swap)(unsigned long i, unsigned long j))
{
    int number = fibo_num(nel);
    int step = fib[number];
    while (step>=1)
    {
        int j = step;
        while (j < nel)
        {
            int loc = j - step;
            while ((loc>=0) && (compare(loc+step, loc) < 0))
            {
                swap (loc+step, loc);
                loc -= step;
            }
            j++;
        }
        number--;
        step = fib[number];
    }
}