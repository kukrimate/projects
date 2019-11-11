/* SHA1 implementation */
#include <stdio.h>
#include <string.h>
#include <stddef.h>
#include <stdint.h>
#include "sha1.h"

void print_uint32(uint32_t n)
{
	printf("%08x\n", n);
}

int main(int argc, char **argv)
{
	FILE *source;
	struct sha1_ctx ctx;
	uint8_t buffer[64];
	uint8_t md[20];
	size_t i;

	if (argc < 2) {
		source = stdin;
	} else {
		source = fopen(argv[1], "rb");
		if (!source) {
			perror("fopen()");
			return 1;
		}
	}

	sha1_init(&ctx);

	while(i = fread(buffer, 1, 64, source)) {
		sha1_update(&ctx, buffer, i);
	}

	sha1_final(&ctx, md);

	for (i = 0; i < 20; ++i) {
		printf("%02x", md[i]);
	}
	printf("\n");

	return 0;
}
