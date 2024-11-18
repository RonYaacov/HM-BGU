
#include <stdio.h>
#include <stdbool.h>
#include <string.h>

FILE *infile;
FILE *outfile;

char *encoder(char *input){
    return input;
}

void program_loop(){
    char c;
    while((c = fgetc(infile)) != EOF){
        fputs(encoder(&c), stdout);
    }
}


int main(int argc, char *argv[]) {    
    infile = stdin;
    outfile = stderr;
    bool debug = true;
    for(int i=1; i<argc; i++){
        if(strcmp(argv[i], "+D") == 0){
            debug = true;        
            continue;
        }
        if(strcmp(argv[i], "-D") == 0){
            debug = false;
            continue;
        }
        if(debug){
            fprintf(outfile, "%s\n",argv[i]);
        }
    }
    program_loop();

    return 0;
}