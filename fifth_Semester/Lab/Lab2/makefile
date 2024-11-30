myshell: myshell.o LineParser.o
	gcc -o myshell myshell.o LineParser.o

lopper: lopper.o
	gcc -o lopper lopper.o

lopper.o: lopper.c
	gcc -c -g -Wall lopper.c

myshell.o: myshell.c LineParser.h
	gcc -c -g -Wall myshell.c

LineParser.o: LineParser.c LineParser.h
	gcc -c -g -Wall LineParser.c

clean:
	rm myshell lopper *.o