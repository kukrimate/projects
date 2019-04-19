/*
 * Copyright (c) 2018, Máté Kukri
 * This file is part of osldr and is distributed under the ISC license.
 * Check license.txt for more details.
 */
#ifndef LDRSTD_H
#define LDRSTD_H

/* compiler freestanding headers */
#include <stdint.h>
#include <stddef.h>
#include <stdarg.h>
#include <stdbool.h>

/* defined in the linker script */
extern int end_of_binary;

/* debug console */
typedef struct dbg_con dbg_con_t;
struct dbg_con {
	void (*output_string)(dbg_con_t *this, char *string);
};

void debug_add_console(dbg_con_t *con);
void debug_print(char *format, ...);

/* dynamic memory allocation */
void *balloc(size_t nbytes);
void bfree(void *ptr);

/* string functions */
void *memcpy(void *dest, void *src, size_t n);
void *memset(void *mem, uint8_t byte, size_t n);
char *strcpy(char *dest, char *src);
size_t strlen(char *str);
char *strrev(char *s);

#endif
