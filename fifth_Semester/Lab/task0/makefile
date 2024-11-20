all: target_dirs main

target_dirs:
	mkdir -p target bin

main: main.o numbers.o add.o
	gcc -g -Wall -m32 -o bin/main target/main.o target/numbers.o target/add.o

main.o: main.c
	gcc -g -Wall -m32 -c -o target/main.o main.c

numbers.o: numbers.c
	gcc -g -Wall -m32 -c -o target/numbers.o numbers.c

add.o: add.s
	nasm -g -f elf -w+all -o target/add.o add.s

clean:
	rm -rf target bin