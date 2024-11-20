
#include <stdio.h>
#include <stdbool.h>
#include <stdlib.h>

FILE *infile;
FILE *outfile;

char *encoding_string = "0";
int encoding_index = 0;
bool is_plus = true;

char wrap_around(char old_input, char new_input, int delta){
    if (new_input > 'Z' && old_input <= 'Z'){
        new_input = new_input - delta;
    }
    else if (new_input < 'A' && old_input >= 'A'){
        new_input = new_input + delta;
    }
    else if(new_input > 'z' && old_input <= 'z'){
        new_input = new_input - delta;
    }
    else if(new_input < 'a' && old_input >= 'a'){
        new_input = new_input + delta;
    }
    else if(new_input > '9' && old_input <= '9'){
        new_input = new_input - delta;
    }
    else if(new_input < '0' && old_input >= '0'){
        new_input = new_input + delta;
    }
    return new_input;
}

int get_delta(char input){
    if(input >= 'A' && input <= 'Z'){
        return ('Z'-'A') + 1;
    }
    if(input >= 'a' && input <= 'z'){
        return ('z'-'a') + 1;
    }
    if(input >= '0' && input <= '9'){
        return ('9'-'0') + 1;
    }
    return -1;
}

char *encode(char *input){
    int j = 0;
    while (input[j] != EOF && input[j] != '\0'){
        int delta = get_delta(input[j]);
        if(delta != -1){
            int encoding = (int)(encoding_string[encoding_index] - '0');
            if(!is_plus){
                encoding = -encoding;
            }
            char new_input = input[j] + encoding;
            input[j] = wrap_around(input[j], new_input, delta);
        }
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
        fputs(encode(&c), outfile);
    }
}

int handle_command_line_args(int argc, char *argv[]){
    bool debug = true;
    for(int i=1; i<argc; i++){
        if(argv[i][0] == '-' && argv[i][1] == 'i'){
            infile = fopen(argv[i] + 2, "r");
            if(infile == NULL){
                fprintf(stderr, "Error: Could not open file %s\n", argv[i] + 2);
                exit(1);
            }
        }
        else if(argv[i][0] ==  '-' && argv[i][1] == 'o'){
            outfile = fopen(argv[i] + 2, "w");
            if(outfile == NULL){
                fprintf(stderr, "Error: Could not open file %s\n", argv[i] + 2);
                exit(1);
            }
        }
        if(argv[i][0] ==  '-' && argv[i][1] == 'E'){
            encoding_string = argv[i] + 2;
            is_plus = false;
        }
        else if(argv[i][0] ==  '+' && argv[i][1] == 'E'){
            encoding_string = argv[i] + 2;
        }
        if(argv[i][0] ==  '+' && argv[i][1] == 'D'){
            debug = true;        
            continue;
        }
        else if(argv[i][0] ==  '-' && argv[i][1] == 'D'){
            debug = false;
            continue;
        }
        if(debug){
            fprintf(outfile, "%s\n",argv[i]);
        }
    }
    return 0;
}


int main(int argc, char *argv[]) {
    infile = stdin;
    outfile = stderr;
    handle_command_line_args(argc, argv);
    program_loop();
    if(infile != stdin){
        fclose(infile);
    }
    if(outfile != stderr){
        fclose(outfile);
    }
    return 0;
}