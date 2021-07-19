#include <stdio.h>
#include <stdlib.h>
 
int crossing[10][10];
int str[10][100];
int str_len[10];
int del[10];
int min;
int number;
 
void cross (int i, int j);
void min_sstr (int str_num, int length, int counter);
 
int main()
{
    scanf ("%d\n", &number);
    if (number == 0) { return 0; };
    char chr;
    scanf ("%c", &chr);
    int sym = 0;
    for (int line = 0; line < number; line++)
    {
       while ( (chr != 10) )
        {
            str[line][sym] = chr;
            sym ++;
            scanf ("%c", &chr);
        }
        scanf ("%c", &chr);
        str_len[line] = sym;
        sym = 0;
    }
    str_len[number] = sym;
   
    for (int i = 0; i < number; i++)
    {
        for (int j = 0; j < number; j++)
        {
            cross(i, j);
        }
    }
    
    if (number == 1)
    {
        min = str_len[0];
    }
    else
    {
        min = 100000;
        for (int n = 0; n < number; n++)
        {
            min_sstr(n, str_len[n], 0);
        }
    }
 
    printf ("%d", min);
 
    return 0;
}
 
 
void cross(int i, int j)
{
    int x, y;
    int flag = 0;
    int l = 1;
    while ( (flag == 0) && (l < str_len[i]) )
    {
        y = 0;
        x = l;
        while( (str[i][x] == str[j][y]) && (y < str_len[j]) && (x < str_len[i]) )
        {
            x++;
            y++;
        }
        if (x == str_len[i])
        {
            crossing[i][j] = str_len[i] - l;
            flag = 1;
        }
        l++;
    }
}
 
void min_sstr (int str_num, int length, int counter)
{
    int flag = 0;
    if (counter == number-1)
    {
        if (length < min)
        {
            min = length;
        }
    }
    else
    {
        for (int i = 0; i < number; i++)
        {
            del[counter] = str_num;
            for (int j = 0; j <= counter; j++)
            {
                if (del[j] == i)
                {
                    flag = 1;
                    break;
                }
            }
            if (flag != 1)
            {
                min_sstr(i, length+str_len[i]-crossing[str_num][i], counter+1);
            }
            flag = 0;
        }
    }
}