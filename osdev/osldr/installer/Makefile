include ../config.mk

install_ldr: install_ldr.c mbrcode.h
	$(HOSTCC) $< -o $@

mbrcode.h: mbrldr.bin
	../scripts/bin2arr -a mbrcode $^ > $@

mbrldr.bin: mbrldr.asm
	$(NASM) -f bin $^ -o $@

.PHONY: clean
clean:
	rm -f install_ldr mbrcode.h mbrldr.bin
