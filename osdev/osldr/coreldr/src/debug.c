/*
 * Copyright (c) 2018, Máté Kukri
 * This file is part of osldr and is distributed under the ISC license.
 * Check license.txt for more details.
 */
/* debug console */
#include <ldrstd.h>
#include <drv/vgatext.h>

static const char *num_lookup = "0123456789abcdefghijklmopqrstuvwxyz";

static size_t ptoa(uintptr_t ptr, char *s)
{
	char *p;

	p = s;
	*p++ = '0';
	*p++ = 'x';

	for (int i = sizeof(p) * 8; i; i -= 4) {
		*p++ = num_lookup[(ptr >> (i - 4)) & 0xf];
	}
	*p = '\0';
	return p - s;
}

static size_t utoa(unsigned n, char *s, int base)
{
	char *p;

	p = s;
	do {
		*p++ = num_lookup[n % base];
	} while (n /= base, n);
	*p = '\0';

	strrev(s);
	return p - s;
}

static size_t itoa(int n, char *s, int base)
{
	char *p;

	p = s;
	if (n < 0) {
		*p++ = '-';
		n *= -1;	
	}
	p += utoa((unsigned) n, p, base);

	return p - s;
}

static size_t copy_string(char *d, char *s)
{
	char *p = d;
	for (; *s; ++s) {
		*p++ = *s;
	}
	return p - d;
}

static dbg_con_t *con_arr[10] = { 0 };

void debug_add_console(dbg_con_t *con)
{
	for (size_t i = 0; i < 10; ++i) {
		if (!con_arr[i]) {
			con_arr[i] = con;
			con_arr[i + 1] = NULL;
			break;
		}
	}
}

void debug_print(char *format, ...)
{
	char buffer[100];
	va_list ap;

	va_start(ap, format);

	char *p = buffer;
	for (; *format; ++format) {
		/* just a regular character */
		if (*format != '%') {
			*p++ = *format;
			continue;
		}

		/* check format */
		switch (*++format) {
		case 'p':
			p += ptoa(va_arg(ap, uintptr_t), p);
			break;
		case 'd':
			p += itoa(va_arg(ap, int), p, 10);
			break;
		case 'u':
			p += utoa(va_arg(ap, unsigned), p, 10);
			break;
		case 'x':
			p += utoa(va_arg(ap, unsigned), p, 16);
			break;
		case 's':
			p += copy_string(p, va_arg(ap, char *));
			break;
		case 'c':
			*p++ = (char) va_arg(ap, int);
			break;
		default:
			*p++ = '?';
			break;
		}
	}
	*p = '\0';

	va_end(ap);

	for (dbg_con_t **cur = con_arr; *cur; ++cur)
		(*cur)->output_string(*cur, buffer);
}
