# include "linux/limits.h"
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>


int main(int argc, char *argv[])
{
    char path[PATH_MAX];
    if(getcwd(path, PATH_MAX) == NULL)
    {
        perror("Error getting current working directory");
        return 1;
    }
    printf("Current working directory: %s\n", path);
    return 0;
}