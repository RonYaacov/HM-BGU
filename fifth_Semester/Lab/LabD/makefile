TARGET = program
SRC = task0.asm

all:
	nasm -f elf32 $(SRC) -o task0.o
		gcc -m32 task0.o -o $(TARGET)

clean:
	rm -f task0.o $(TARGET)