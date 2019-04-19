/*
 * Copyright (c) 2018, Máté Kukri
 * This file is part of osldr and is distributed under the ISC license.
 * Check license.txt for more details.
 */
/* bootloader dynamic memory allocator */
#include <ldrstd.h>

typedef struct mem_hdr
{
	size_t size;
	bool free;
	struct mem_hdr *prev;
} mem_hdr_t;

/* free memory base */
static void *freebase = &end_of_binary;
/* last memory block */
static mem_hdr_t *last = NULL;

static mem_hdr_t *allocate_block(size_t nbytes)
{
	mem_hdr_t *block = last;

	/* re-use freed blocks */
	for (block = last; block; block = block->prev) {
		if (block->free && nbytes <= block->size)
			return block;
	}

	/* allocate a new block */
	block = freebase;
	freebase += sizeof(mem_hdr_t) + nbytes;
	block->size = nbytes;
	block->free = false;
	block->prev = last;
	last = block;
	return block;
}

static void free_block(mem_hdr_t *block)
{
	if (block == last) {
		last = block->prev;
		freebase = block;
	} else {
		block->free = true;
	}
}

void *balloc(size_t nbytes)
{
	void *block;

	block = allocate_block(nbytes);
	if (!block)
		return NULL;
	return block + sizeof(mem_hdr_t);
}

void bfree(void *ptr)
{
	free_block(ptr - sizeof(mem_hdr_t));
}
