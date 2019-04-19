#ifndef DISASM_H
#define DISASM_H

struct ctx;

// Returns available byte count
typedef size_t (*AVAL)(struct ctx *it);

// Returns the next byte
typedef uint8_t (*NEXB)(struct ctx *it);
// Returns the next two bytes as a little-endian word
typedef uint16_t (*NEXW)(struct ctx *it);

struct ctx {
	AVAL aval;
	NEXB nexb;
	NEXW nexw;
};

int disasm86(struct ctx *ctx);

#endif
