#include <stdio.h>
#include <netdb.h>
#include <netinet/in.h>
#include <stdlib.h>
#include <string.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <unistd.h>

#define MAX 80
#define PORT 8095
#define SA struct sockaddr

void handle_request(char *request, char *result) {
    sprintf(result, "%u", strlen(request) - 1);
}

void handle_client(int sockfd)
{
    char buff[MAX];
    char result[MAX];
    int n;
    for (;;) {
        bzero(buff, MAX);
        read(sockfd, buff, sizeof(buff));
        handle_request(buff, result);
        write(sockfd, result, sizeof(buff));
    }
}

int create_socket() {
    int sockfd = socket(AF_INET, SOCK_STREAM, 0);
    if (sockfd == -1) {
        printf("socket creation failed...\n");
        exit(0);
    }
    else
        printf("Socket successfully created..\n");
    return sockfd;
}

struct sockaddr_in create_servaddr() {
    struct sockaddr_in servaddr;
    bzero(&servaddr, sizeof(servaddr));
    servaddr.sin_family = AF_INET;
    servaddr.sin_addr.s_addr = htonl(INADDR_ANY);
    servaddr.sin_port = htons(PORT);
    return servaddr;
}

void bind_socket(int sockfd, struct sockaddr_in servaddr) {
    if ((bind(sockfd, (SA*)&servaddr, sizeof(servaddr))) != 0) {
        printf("socket bind failed...\n");
        exit(0);
    }
    else
        printf("Socket successfully binded..\n");
}

void listen_socket(int sockfd) {
    if ((listen(sockfd, 5)) != 0) {
        printf("Listen failed...\n");
        exit(0);
    }
    else
        printf("Server listening..\n");
}

int accept_connection(int sockfd) {
    struct sockaddr_in cli;
    int len = sizeof(cli);
    int connfd = accept(sockfd, (SA *)&cli, &len);
    if (connfd < 0) {
        printf("server acccept failed...\n");
        exit(0);
    } else
        printf("server acccept the client...\n");
    return connfd;

}

int main()
{
    int sockfd = create_socket();
    struct sockaddr_in servaddr = create_servaddr();
    bind_socket(sockfd, servaddr);
    listen_socket(sockfd);
    int connfd = accept_connection(sockfd);
    handle_client(connfd);
    close(sockfd);
}