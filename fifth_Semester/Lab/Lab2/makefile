myshell: myshell.o LineParser.o
	gcc -o myshell myshell.o LineParser.o

myshell.o: myshell.c LineParser.h
	gcc -c -g -Wall myshell.c

LineParser.o: LineParser.c LineParser.h
	gcc -c -g -Wall LineParser.c

clean:
	rm myshell myshell.o LineParser.o