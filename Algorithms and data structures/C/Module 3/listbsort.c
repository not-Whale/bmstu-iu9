#include <stdio.h>
#include <stdlib.h>
#include <string.h>

struct Elem
{
    struct Elem *next;
    char *word;
};

void bsort(struct Elem *list);
int ListLength(struct Elem *list);

struct Elem *InitEmptyList();

void swap(struct Elem *a, struct Elem *b);
int compare(struct Elem *a, struct Elem *b);

int main()
{
    struct Elem *head = InitEmptyList();
    struct Elem *tail = head;
    struct Elem *current;
    char *Input = (char*)malloc(65536*sizeof(char));
    char *Word;
    gets(Input);
    Word = strtok(Input, " ");
    while (Word != NULL)
    {
        current = InitEmptyList();
        current->word = Word;
        if (head->next == NULL)
        {
            head->next = current;
        }
        else
        {
            tail->next = current;
        }
        tail = current;
        Word = strtok(NULL, " ");
    }
    bsort(head);
    struct Elem *d = head->next;
    while (d != NULL)
    {
        printf("%s ", d->word);
        tail = d;
        d = d->next;
        free(tail);
    }
    free(Input);
    free(head);
    return 0;
}

struct Elem* InitEmptyList()
{
    struct Elem *Out = (struct Elem*)malloc(sizeof(struct Elem));
    Out->next = NULL;
    Out->word = NULL;
    return Out;
}

void swap(struct Elem *a, struct Elem *b)
{
    char *c;
    c = a->word;
    a->word = b->word;
    b->word = c;
}

int compare(struct Elem *a, struct Elem *b)
{
    return (strlen(a->word) - strlen(b->word));
}

int ListLength(struct Elem *list)
{
    struct Elem *Help;
    Help = list;
    int res = 0;
    while (Help != NULL)
    {
        res++;
        Help = Help->next;
    }
    return res;
}

void bsort(struct Elem *list)
{
    struct Elem *l;
    int t = ListLength(list->next) - 1;
    while (t > 0)
    {
        int bound = t;
        int i = 0;
        t = 0;
        l = list->next;
        while (i < bound)
        {
            if (compare(l, l->next) > 0)
            {
                swap(l, l->next);
                t = i;
            }
            l = l->next;
            i++;
        }
    }
}