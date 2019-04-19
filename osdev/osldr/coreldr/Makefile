include ../config.mk

# Objects
OBJ = src/init.o src/ldrmain.o src/alloc.o src/debug.o src/util.o \
	src/drv/serport.o src/drv/vgatext.o

coreldr.bin: $(OBJ)
	$(LD) -nostdlib -T coreldr.ld $^ -o $@

.PHONY: clean
clean:
	rm -f $(OBJ) *.bin

%.o: %.c
	$(CC) -Iinc/ -ffreestanding -c $^ -o $@

%.o: %.asm
	$(NASM) -Iinc/ -f elf32 $^ -o $@
