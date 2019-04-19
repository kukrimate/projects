/*
 * Copyright (c) 2018, Máté Kukri
 * This file is part of osldr and is distributed under the ISC license.
 * Check license.txt for more details.
 */
/* utility functions */
#include <ldrstd.h>

void *memcpy(void *dest, void *src, size_t n)
{
	for (size_t i = 0; i < n; ++i)
		((uint8_t *) dest)[i] = ((uint8_t *) src)[i];
	return dest;
}

void *memset(void *mem, uint8_t byte, size_t n)
{
	for (void *p = mem; n; --n, ++p)
		*(uint8_t *) p = byte;
}

char *strcpy(char *dest, char *src)
{
	char *p = dest;
	for (; *src; ++p, ++src)
		*p = *src;
	*p = '\0';
	return dest;
}

size_t strlen(char *str)
{
	char *p = str;
	for (; *p; ++p);
	return p - str;
}

char *strrev(char *s)
{
	char *p, *ep;
	char t;
	
	for (p = s, ep = s + strlen(s) - 1; ep > p; ++p, --ep) {
		t = *p;
		*p = *ep;
		*ep = t;
	}	
	return s;
}
