#include <stdlib.h>
#include <string.h>
#include <stdio.h>

int split(char *s, char ***words);

#define INITIAL_SIZE 128

char *getstring()
{
    char *s;
    int flag, len = 0, size = INITIAL_SIZE;

    s = (char*)malloc(INITIAL_SIZE);
    if (s == NULL) return NULL;

    for (;;) {
        if (fgets(s+len, size-len, stdin) == NULL) {
            free(s);
            return NULL;
        }

        len += (int)strlen(s+len);
        if (s[len-1] == '\n') break;

        char *new_s = (char*)malloc(size *= 2);
        if (new_s == NULL) {
            free(s);
            return NULL;
        }

        memcpy(new_s, s, len);
        free(s);
        s = new_s;
    }

    s[len-1] = 0;
    return s;
}

void printword(char *s)
{
    printf("\"");
    for (;;) {
        char c = *s++;
        switch (c) {
        case 0:
            printf("\"\n");
            return;
        case '\a':
            printf("\\a");
            break;
        case '\b':
            printf("\\b");
            break;
        case '\f':
            printf("\\f");
            break;
        case '\n':
            printf("\\n");
            break;
        case '\r':
            printf("\\r");
            break;
        case '\t':
            printf("\\t");
            break;
        case '\v':
            printf("\\v");
            break;
        case '\\':
            printf("\\\\");
            break;
        case '\"':
            printf("\\\"");
            break;
        default:
            printf(c >= 0x20 && c <= 0x7E ? "%c" : "\\x%02x", c);
        }
    }
}

int main()
{
    char *s = getstring();
    if (s == NULL) return 1;

    char **words;
    int n = split(s, &words);
    free(s);

    for (int i = 0; i < n; i++) printword(words[i]);

    for (int i = 0; i < n; i++) free(words[i]);
    free(words);
    return 0;
}

int split(char *s, char ***words)
{
    int k = 0;
    int length = 0, maxlength = 0;
    char chr = s[0];
    while (1)
    {
        if (chr != 32) { break; };
        k++;
        chr = s[k];
    }
    int h = k;
    int flag=0, counter=0;
    while (chr != 0)
    {
        if (chr != 32)
        {
            length ++;
            if (length > maxlength) { maxlength = length; };
            if (flag == 1) { counter++; };
            flag = 0;
        }
        else
        {
            if (length > maxlength) { maxlength = length; };
            length = 0;
            flag = 1;
        }
        k++;
        chr = s[k];
    }
    counter ++;
    //printf ("max length is %d\n", maxlength);
    *words=(char**)malloc(counter*sizeof(char*));
    for (int i=0; i<counter; i++)
    {
        *(*words+i)=(char*)calloc(maxlength+1, sizeof(char));
    }
    chr = s[h];
    flag=0;
    int l=0, u=0;
    while (chr != 0)
    {
        if (chr != 32)
        {
            if (flag == 1) { l++; };
            flag = 0;
            *(*(*words+l)+u) = chr;
            u++;
        }
        else
        {
            flag = 1;
            u=0;
        }
        h++;
        chr = s[h];
    }
    return counter;
}