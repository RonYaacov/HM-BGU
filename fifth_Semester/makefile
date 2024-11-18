all: target_dirs main

target_dirs:
	mkdir -p target bin

main: main.o
	gcc -g -Wall -m32 -o bin/main target/main.o

main.o: main.c
	gcc -g -Wall -m32 -c -o target/main.o main.c

clean:
	rm -rf target bin