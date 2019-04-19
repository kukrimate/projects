# All tools
.PHONY: all
all: fileball;

# File packing utility
fileball: fileball.c
	$(CC) $(CFLAGS) $^ -o $@

.PHONY: clean
clean:
	rm fileball
