#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>
#include <sys/wait.h>



int main(int argc, char *argv[]){
    int pipeChannel[2];
    int pid1;
    int pid2;
    if(pipe(pipeChannel) == -1){
        perror("Error creating pipe");
        exit(1);
    }
    printf("(parent_process>forking…)\n");
    pid1 = fork();
    if(pid1 == -1){
        perror("Error forking");
        exit(1);
    }
    if(pid1 == 0){
        printf("(child1>redirecting stdout to the write end of the pipe…)\n");
        if((dup2(pipeChannel[1],STDOUT_FILENO) == -1)){
            perror("Error duplicating file descriptor");
            exit(1);
        }
        printf( "(parent_process>closing the write end of the pipe…)\n");
        close(pipeChannel[1]);
        close(pipeChannel[0]);
        printf("(child1>going to execute cmd: …)\n");
        execlp("ls", "ls", "-l", NULL);
    }
    printf("(parent_process>created process with id: %d)\n", pid1);
    printf("(parent_process>forking…)\n");
    pid2 = fork();
    if(pid2 == -1){
        perror("Error forking");
        exit(1);
    }
    if(pid2 == 0){

        printf("(child2>redirecting stdin to the read end of the pipe…)\n");
        if((dup2(pipeChannel[0], STDIN_FILENO) == -1)){
            perror("Error duplicating file descriptor");
            exit(1);
        }
        printf("(parent_process>closing the read end of the pipe…)\n");
        close(pipeChannel[0]);
        close(pipeChannel[1]);
        printf("(child2>going to execute cmd: …)\n");
        execlp("tail", "tail", "-n", "2", NULL);
    }
    printf("(parent_process>created process with id: %d)\n", pid2);
    close(pipeChannel[0]);
    close(pipeChannel[1]);
    int status;
    printf("(parent_process>waiting for child processes to terminate…)\n");
    waitpid(pid1, &status, 0);
    waitpid(pid2, &status, 0);
    printf("(parent_process>exiting…)\n");
    exit(0);
}