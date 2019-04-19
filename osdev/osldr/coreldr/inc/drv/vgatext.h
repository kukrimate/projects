/*
 * Copyright (c) 2018, Máté Kukri
 * This file is part of osldr and is distributed under the ISC license.
 * Check license.txt for more details.
 */
#ifndef VGATEXT_H
#define VGATEXT_H

/* VGA text-mode colors */
enum
{
	VGA_BLACK         = 0,
	VGA_BLUE          = 1,
	VGA_GREEN         = 2,
	VGA_CYAN          = 3,
	VGA_RED           = 4,
	VGA_MAGENTA       = 5,
	VGA_BROWN         = 6,
	VGA_LIGHT_GRAY    = 7,
	VGA_DARK_GRAY     = 8,
	VGA_LIGHT_BLUE    = 9,
	VGA_LIGHT_GREEN   = 10,
	VGA_LIGHT_CYAN    = 11,
	VGA_LIGHT_RED     = 12,
	VGA_LIGHT_MAGENTA = 13,
	VGA_YELLOW        = 14,
	VGA_WHITE         = 15
};

#define VGA_COLOR(bg_color, fg_color) (bg_color << 4 & 0xf0 | fg_color & 0xf)
#define VGA_ENTRY(color, ch) (color << 8 | ch)

void vga_clearscreen();
void vga_print_char(char ch);
void vga_print_string(char *string);

dbg_con_t *setup_vga_console();

#endif
