/*
 * Copyright (c) 2018, Máté Kukri
 * This file is part of osldr and is distributed under the ISC license.
 * Check license.txt for more details.
 */
/* VGA text-mode driver */
#include <ldrstd.h>
#include <drv/vgatext.h>

/* video mode settings */
static uint16_t *video_memory = (uint16_t *) 0xb8000;
static size_t rows = 25;
static size_t columns = 80;

/* current terminal state */
static size_t current_row = 0;
static size_t current_column = 0;
static uint8_t current_color = VGA_COLOR(VGA_DARK_GRAY, VGA_WHITE);

static void memsetw(uint16_t *mem, uint16_t word, size_t n)
{
	while (n--)
		*mem++ = word;
}

void vga_clearscreen()
{
	current_column = 0;
	current_row = 0;

	memsetw(video_memory, VGA_ENTRY(current_color, ' '), rows * columns);
}

/* scroll the entire text-mode screen up by a line */
static void vga_scrollscreen()
{
	memcpy(video_memory, video_memory + columns, (rows - 1) * columns * 2);
	memsetw(video_memory + (rows - 1) * columns, VGA_ENTRY(current_color, ' '), columns);
}

/* start a new line */
static void vga_newline()
{
	if (++current_row == rows) {
		vga_scrollscreen();
		--current_row;
	}
	current_column = 0;
}

void vga_print_char(char ch)
{
	switch (ch) {
	case '\r':
		break;
	case '\n':
		vga_newline();
		break;
	default:
		video_memory[current_row * columns + current_column] = VGA_ENTRY(current_color, ch);
		if (++current_column == columns)
			vga_newline();
		break;
	}
}

void vga_print_string(char *string)
{
	for (; *string; ++string)
		vga_print_char(*string);
}

/* vga console support */

void vgacon_output_string(dbg_con_t *this, char *string)
{
	vga_print_string(string);
}

dbg_con_t *setup_vga_console()
{
	dbg_con_t *con = balloc(sizeof(dbg_con_t));
	/* if (!con)
		panic(); */
	con->output_string = vgacon_output_string;

	vga_clearscreen();
	return con;
}
