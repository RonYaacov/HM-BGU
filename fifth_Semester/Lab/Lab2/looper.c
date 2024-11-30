#include <stdio.h>
#include <unistd.h>
#include <sys/syscall.h>
#include <signal.h>
#include <string.h>

void handler(int sig)
{
    printf("\nReceived Signal: %s\n", strsignal(sig));
    if (sig == SIGTSTP) {
        signal(SIGTSTP, SIG_DFL);  
        raise(SIGTSTP);            
    
    } else if (sig == SIGCONT) {
		signal(SIGCONT, SIG_DFL);
		raise(SIGCONT);

    } else if (sig == SIGINT) {
        signal(SIGINT, SIG_DFL);   
        raise(SIGINT);            
    }
}

int main(int argc, char **argv)
{
    printf("Starting the program\n");

    while (1) {
        sleep(1);
		signal(SIGINT, handler);
		signal(SIGTSTP, handler);
		signal(SIGCONT, handler);

    }

    return 0;
}