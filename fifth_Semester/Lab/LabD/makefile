TARGET = program
TARGET0 = task0
SRC = multi.asm
SRC0 = task0.asm

all: 
	nasm -g -f elf -w+all $(SRC) -o multi.o
	gcc -m32 -g -Wall -o $(TARGET) multi.o


task0: $(SRC0)
	nasm -g -f elf -w+all $(SRC0) -o task0.o
	gcc -m32 -g -Wall -o $(TARGET0) task0.o


clean:
	rm -f task0.o multi.o $(TARGET) $(TARGET0)
