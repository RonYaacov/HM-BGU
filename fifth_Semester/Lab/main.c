
#include <stdio.h>
#include <stdbool.h>
#include <string.h>

FILE *infile;
FILE *outfile;

char *encoding_string = "0";
int encoding_index = 0;
bool is_plus = true;


char *encode(char *input){
    int j = 0;
    while (input[j] != '\n' && input[j] != EOF && input[j] != '\0'){
        int encoding = (int)(encoding_string[encoding_index] - '0');
        if(!is_plus){
            encoding = -encoding;
        }    
        input[j] = input[j] + encoding;
        encoding_index++;
        if(encoding_string[encoding_index] == '\0'){
            encoding_index = 0;
        }
        j++;
    }
    return input;
}

void program_loop(){
    char c;
    while((c = fgetc(infile)) != EOF){
        fputs(encode(&c), stdout);
    }
}

void print_command_line(int argc, char *argv[]){
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
        if(strncmp(argv[i], "-E", 2) == 0){
            encoding_string = argv[i] + 2;
            is_plus = false;
        }
        else if(strncmp(argv[i], "+E", 2) == 0){
            encoding_string = argv[i] + 2;
        }
        if(debug){
            fprintf(outfile, "%s\n",argv[i]);
        }
    }
}


int main(int argc, char *argv[]) {    
    infile = stdin;
    outfile = stderr;
    print_command_line(argc, argv);
    program_loop();
    return 0;
}