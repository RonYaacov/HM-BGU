#include <stdio.h>
#include <unistd.h>
#include <sys/syscall.h>
#include <signal.h>
#include <string.h>

void handler(int sig)
{
	printf("\nRecieved Signal : %s\n", strsignal(sig));
	if (sig == SIGTSTP)
	{
		signal(SIGTSTP, SIG_DFL);
        raise(SIGTSTP);
        signal(SIGTSTP, handler);
	}
	else if (sig == SIGCONT)
	{
		signal(SIGCONT, SIG_DFL);
        raise(SIGCONT);
        signal(SIGCONT, handler);
	}
    else if (sig == SIGINT){
        signal(SIGINT, SIG_DFL);
        raise(SIGINT);
		signal(SIGCONT, handler);

    }
    else{
        signal(sig, SIG_DFL);
        raise(sig);
    }
}

int main(int argc, char **argv)
{

	printf("Starting the program\n");
	signal(SIGINT, handler);
	signal(SIGTSTP, handler);
	signal(SIGCONT, handler);

	while (1)
	{
		sleep(1);
		
	}

	return 0;
}