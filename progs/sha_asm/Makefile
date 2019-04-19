sha1sum: sha1sum.c sha1.c sha1_amd64.o
	cc -O2 -ansi $^ -o $@

%.o: %.asm
	nasm -f elf64 $^ -o $@

clean:
	rm -f sha1sum
