#include <linux/limits.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>
#include <string.h>
#include <fcntl.h>
#include "LineParser.h"
#include <signal.h>
#define MAX_LOG_MSG_SIZE 256
#define MAX_CHILDREN 256
#define MAX_INPUT_SIZE 2048

int execute(cmdLine *pCmdLine);
int executePipe(cmdLine *pCmdLine, int pipe_index);
int execute_single_process(cmdLine *pCmdLine);
void logger(char *msg);
int cd(cmdLine *pCmdLine);
int sendSignal(cmdLine *pCmdLine);
int reDirect(cmdLine *pCmdLine);
int checkRedirect(cmdLine *pCmdLine);


int isDebug = 0;
char logMsg[MAX_LOG_MSG_SIZE];
pid_t child_pids[MAX_CHILDREN];
int pipeChannels[MAX_CHILDREN][2];
int child_count = 0;

void cleanup() {
    for (int i = 0; i < child_count; i++) {
        kill(child_pids[i], SIGTERM);
    }
}


int main(int argc, char *argv[]){
    char path[PATH_MAX];
    char max_input[MAX_INPUT_SIZE];
    cmdLine *current_line;
    int quit = 0;
    int exit_code = 0;
    int current_pipe_index = 0;
    for(int i = 0; i<argc; i++){
        if(strcmp(argv[i], "-d") == 0){
            isDebug = 1;
        }
    }
    atexit(cleanup);
    while(1){
        if(getcwd(path, PATH_MAX) == NULL){
            perror("Error getting current working directory");
            exit_code = 1;
            break;
        }
        printf("Current working directory: %s\n", path);
        fgets(max_input, sizeof(max_input), stdin);
        current_line = parseCmdLines(max_input);
        while(current_line != NULL){
            if(strcmp(current_line->arguments[0], "quit") == 0){
                exit_code = 0;
                quit = 1;
                break;
            }
            if(!checkRedirect(current_line)){
                exit_code = 1;
                quit = 1;
                break;
            }
            if(!current_line->next){
                int exeResult = execute(current_line);
                if(exeResult == 1 || exeResult == -1){
                    exit_code = 1;
                    quit = 1;
                    break;
                }    
            }
            else{
                int exeResult = executePipe(current_line, current_pipe_index);
                if(exeResult == 1 || exeResult == -1){
                    exit_code = 1;
                    quit = 1;
                    break;
                }
                current_line = current_line->next;
                current_pipe_index++;
            }
            current_line = current_line->next;
        }
        if(quit){
            break;
        }
    }
    freeCmdLines(current_line);
    return exit_code;
}


int executePipe(cmdLine *pCmdLine, int pipe_index){
    int pid1;
    int pid2;
    int *pipeChannel = pipeChannels[pipe_index];
    if(pipe(pipeChannel) == -1){
        perror("Error creating pipe");
        return 1;
    }
    pid1 = fork();
    if(pid1 == -1){
        perror("Error forking");
        return 1;
    }
    if(pid1 == 0){
        if(dup2(pipeChannel[1], STDOUT_FILENO) == -1){
            perror("Error duplicating file descriptor");
            return 1;
        }
        close(pipeChannel[1]);
        close(pipeChannel[0]);
        int exeResult = execute_single_process(pCmdLine);
        if( exeResult == 1 || exeResult == -1){
            perror("Error executing command");
            return 1;
        }
        return 0;
    }
    pid2 = fork();
    if(pid2 == -1){
        perror("Error forking");
        return 1;
    }
    if(pid2 == 0){
        if(dup2(pipeChannel[0], STDIN_FILENO) == -1){
            perror("Error duplicating file descriptor");
            return 1;
        }
        close(pipeChannel[0]);
        close(pipeChannel[1]);
        int exeResult = execute_single_process(pCmdLine->next);
        if(exeResult == 1 || exeResult == -1){
            return 1;
        }
        return 0;
    }
    close(pipeChannel[0]);
    close(pipeChannel[1]);
    child_pids[child_count++] = pid1;
    child_pids[child_count++] = pid2;
    if(pCmdLine->blocking){
        int status;
        waitpid(pid2, &status, 0);
        waitpid(pid1, &status, 0);
    }
    return 0;
}

int execute_single_process(cmdLine *pCmdLine){
    if(strcmp(pCmdLine->arguments[0],"cd") == 0){
        return cd(pCmdLine);
    }
    int sendSignalResult = sendSignal(pCmdLine);
    if(sendSignalResult != 1){
        return sendSignalResult;
    }
    if(reDirect(pCmdLine) == 1){
        return 1;
    }
    if(execvp(pCmdLine->arguments[0], pCmdLine->arguments) == -1){
            perror("Error executing command");
            return 1;
    }
    return 0;

}

int execute(cmdLine *pCmdLine){
    if(strcmp(pCmdLine->arguments[0],"cd") == 0){
        return cd(pCmdLine);
    }
    int sendSignalResult = sendSignal(pCmdLine);
    if(sendSignalResult != 1){
        return sendSignalResult;
    }
    
    int pid = fork();
    if(pid == -1){
        perror("Error forking");
        return 1;
    }
    if(pid == 0){
        if(reDirect(pCmdLine) == 1){
            _exit(1);
        }
        if(setvbuf(stdout, NULL, _IONBF, 0) == -1){
            perror("Error setting buffer");
            _exit(1);
        }
        if(execvp(pCmdLine->arguments[0], pCmdLine->arguments) == -1){
            perror("Error executing command");
            _exit(1);
        }
        close(STDIN_FILENO);
        close(STDOUT_FILENO);
    }
    else{
        snprintf(logMsg, sizeof(logMsg), "PID: %d\nExecuting command: %s", pid, pCmdLine->arguments[0]);
        logger(logMsg);
        child_pids[child_count++] = pid;
        if(pCmdLine->blocking){
            int status;
            waitpid(pid, &status, 0);
        }
    }
    return 0;
}

int cd(cmdLine *pCmdLine){
    if(pCmdLine->argCount != 2){
        snprintf(logMsg, sizeof(logMsg), "cd: wrong number of arguments\nexpected 1 argument got %d", pCmdLine->argCount - 1);
        logger(logMsg);
        return 0;
    }
    if(chdir(pCmdLine->arguments[1]) == -1){
        snprintf(logMsg, sizeof(logMsg), "Error changing directory to: %s", pCmdLine->arguments[1]);
        logger(logMsg);
        return 0;
    }
    return 0;
}

int sendSignal(cmdLine *pCmdLine){
    if(strcmp(pCmdLine->arguments[0],"stop") == 0){
        return kill(atoi(pCmdLine->arguments[1]), SIGTSTP);
    }
    else if(strcmp(pCmdLine->arguments[0],"wake") == 0){
        return kill(atoi(pCmdLine->arguments[1]), SIGCONT);
        
    }
    else if(strcmp(pCmdLine->arguments[0],"term") == 0){
        return kill(atoi(pCmdLine->arguments[1]), SIGINT);
    }
    return 1;
}

int reDirect(cmdLine *pCmdLine){
    if(pCmdLine == NULL || (pCmdLine->inputRedirect == NULL && pCmdLine->outputRedirect == NULL)){
        return 0;
    }
    int file[2] = {STDIN_FILENO, STDOUT_FILENO};
    char *reDirections[2] = {(char *)pCmdLine->inputRedirect, (char *)pCmdLine->outputRedirect};
    int reDirectionTypes[2] = {O_RDONLY, O_WRONLY | O_CREAT | O_TRUNC};
    for(int i = 0; i<2; i++){
        if(reDirections[i] != NULL){
            int newFile = open(reDirections[i], reDirectionTypes[i], 0644);
            if(newFile == -1){
                close(newFile);
                perror("Error opening file");
                return 1;
            }
            if(dup2(newFile, file[i]) == -1){
                perror("Error redirecting");
                return 1;
            }
            close(newFile);
        }
    }
    return 0;
}

int checkRedirect(cmdLine *pCmdLine){
    if(!pCmdLine->next){
        return 1;
    }        
    if(pCmdLine->outputRedirect != NULL){
        perror("Error: output redirect in pipe");
        return 0;
    }
    if(pCmdLine->next->inputRedirect != NULL){
        perror("Error: input redirect in pipe");
        return 0;
    }
    return 1;
}


void logger(char *msg){
    if(isDebug){
        printf("%s\n", msg);
        fflush(stdout);
    }
}