/*
 * Copyright (c) 2018, Máté Kukri
 * This file is part of osldr and is distributed under the ISC license.
 * Check license.txt for more details.
 */
#include <ldrstd.h>
#include <drv/serport.h>
#include <drv/vgatext.h>

void ldrmain()
{
	debug_print("No console active, should appear nowhere!\n");

	/* add serial console */
	serport_setup(0x3f8, 9600, SER_BITS_8);
	debug_add_console(setup_serial_console(0x3f8));

	debug_print("This should only appear on serial!\n");

	/* add VGA console */
	debug_add_console(setup_vga_console());

	debug_print("This should appear on both VGA and serial!\n");
}
