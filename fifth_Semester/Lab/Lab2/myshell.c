# include "linux/limits.h"
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>
#include <string.h>
#include "LineParser.h"
#include <signal.h>

int execute(cmdLine *pCmdLine);
void logger(char *msg);
int cd(cmdLine *pCmdLine);
int sendSignal(cmdLine *pCmdLine);



int isDebug = 0;
char logMsg[256];





int main(int argc, char *argv[]){
    char path[PATH_MAX];
    char max_input[2048];
    cmdLine *line;
    int exit_code = 0;
    for(int i = 0; i<argc; i++){
        if(strcmp(argv[i], "-d") == 0){
            isDebug = 1;
        }
    }
    while(1){
        if(getcwd(path, PATH_MAX) == NULL){
            perror("Error getting current working directory");
            exit_code = 1;
            break;
        }
        printf("Current working directory: %s\n", path);
        fgets(max_input, 2048, stdin);
        line = parseCmdLines(max_input);
        if(line == NULL){
           continue;
        }
        if(strcmp(line->arguments[0], "quit") == 0){
            exit_code = 0;
            break;
        }
        int exeResult = execute(line);
        if(exeResult == 1 || exeResult == -1){
            exit_code = exeResult;
            break;
        }
    }
    freeCmdLines(line);
    return exit_code;
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
        if(execvp(pCmdLine->arguments[0], pCmdLine->arguments) == -1){
            perror("Error executing command");
            _exit(1);
        }
    }
    else{
        snprintf(logMsg, sizeof(logMsg), "PID: %d\nExecuting command: %s", pid, pCmdLine->arguments[0]);
        logger(logMsg);
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


void logger(char *msg){
    if(isDebug){
        printf("%s\n", msg);
    }
}