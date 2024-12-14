#include <stdio.h>  
#include <stdlib.h>



typedef struct virus {
unsigned short SigSize;
char virusName[16];
unsigned char* sig;
} virus;

virus* readVirus(FILE*);
void printVirus(virus* virus, FILE* output);
int isLittleEndian(FILE *file);
int isBigEndian(FILE *file);

int main(int argc, char **argv) {
    FILE* file = fopen(argv[1], "r");
    if(!(file)) {
        fclose(file);
        fprintf(stderr, "Error opening file\n");
        return 1;
    }
    int isBig = isBigEndian(file);
    int isLittle = isLittleEndian(file);
    if(isBig == 0 && isLittle == 0) {
        fclose(file);
        fprintf(stderr, "Error reading file\n");
        return 1;
    }
    virus* virus = readVirus(file);
    while (virus != NULL) {
        printVirus(virus, stdout);
        free(virus->sig);
        free(virus);
        virus = readVirus(file);
    }
    fclose(file);
    return 0;
}

virus* readVirus(FILE* file) {
    virus* virus = (struct virus*)malloc(sizeof(struct virus));
    if(fread(virus, 1, 18, file) != 18) {
        free(virus);
        return NULL;
    }
    virus->sig = (unsigned char*)malloc(virus->SigSize);
    if(fread(virus->sig, 1, virus->SigSize, file) != virus->SigSize) {
        free(virus->sig);
        free(virus);
        return NULL;
    }
    return virus;
}

void printVirus(virus* virus, FILE* output){

}

