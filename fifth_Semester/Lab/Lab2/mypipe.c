#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>
#include <sys/wait.h>




int main(int argc, char *argv[]){
    int pipeChannel[2];
    int pid;
    char buffer[256];
    if(pipe(pipeChannel) == -1){
        perror("Error creating pipe");
        exit(1);
    }
    pid = fork();
    if(pid == -1){
        perror("Error forking");
        exit(1);
    }
    if(pid == 0){
        close(pipeChannel[0]);
        char *msg = "Hello";
        write(pipeChannel[1], msg, strlen(msg)+1);
        close(pipeChannel[1]);
        _exit(0);
    }
    close(pipeChannel[1]);
    int status;
    waitpid(pid, &status, 0);
    read(pipeChannel[0], buffer, sizeof(buffer));
    printf("Message from child: %s\n", buffer);
    close(pipeChannel[0]);
    return 0;
}