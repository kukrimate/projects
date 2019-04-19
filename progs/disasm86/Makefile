.PHONY: all
all: disasm86

OBJ = main.o disasm.o

disasm86: $(OBJ)
	$(CC) $(LDFLAGS) $(OBJ) -o disasm86

.PHONY: clean
clean:
	rm -f $(OBJ) disasm86
