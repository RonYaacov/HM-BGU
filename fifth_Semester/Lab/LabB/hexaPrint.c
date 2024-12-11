#include <stdio.h>
#include <stdlib.h>
#include <string.h>

void printHex(char *buffer, int length){
    for(int i = 0; i<length; i++){
        printf("%x ", buffer[i]);
    }
    printf("\n");
}

int main(int cargs, char *args[]){
    if(cargs < 2){
        printf("Please provide a file name\n");
        return 1;
    }
    FILE *file = fopen(args[1], "r");
    if(file == NULL){
        printf("Error opening file\n");
        return 1;
    }
    char buffer[100];
    size_t bytesToRead = fread(buffer, 1, sizeof(buffer), file);
    if(bytesToRead > 0){
        printHex(buffer, bytesToRead);
    }
    fclose(file);
    return 0;
}