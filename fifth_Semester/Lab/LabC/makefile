


myshell: myshell.o LineParser.o
	gcc -o myshell myshell.o LineParser.o

mypipeline: mypipeline.o
	gcc -o mypipeline mypipeline.o

mypipeline.o: mypipeline.c
	gcc -c -g -Wall mypipeline.c



myshell.o: myshell.c LineParser.h
	gcc -c -g -Wall myshell.c

LineParser.o: LineParser.c LineParser.h
	gcc -c -g -Wall LineParser.c

clean:
	rm mypipeline myshell  *.o