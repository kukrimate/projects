/*
 * Copyright (c) 2018, Máté Kukri
 * This file is part of osldr and is distributed under the ISC license.
 * Check license.txt for more details.
 */
/* interrupt-less serial port driver */
#include <ldrstd.h>
#include <x86_io.h>
#include <drv/serport.h>

enum
{
	INT_ENABLE = 1,
	INT_IDENT = 2, /* read only */
	FIFO_CONTROL = 2, /* write only */
	LINE_CONTROL = 3,
	MODEM_CONTROL = 4,
	LINE_STATUS = 5,
	MODEM_STATUS = 6,
	SCRATCH = 7
};

/* these should not be user settable */
#define LC_DLAB_EN  0x80
#define LC_BREAK_EN 0x40

void serport_setup(uint16_t io_addr, uint32_t baud_rate, uint8_t flags)
{
	uint16_t divisor;

	if (115200 < baud_rate)
		return;
	divisor = 115200 / baud_rate;

	/* disable interrupts */
	outb(io_addr + INT_ENABLE, 0);

	/* enable divisor latch */
	outb(io_addr + LINE_CONTROL, LC_DLAB_EN);
	/* write divisor */
	outb(io_addr, divisor & 0xff);
	outb(io_addr + 1, divisor >> 8 & 0xff);

	/* setup flags */
	outb(io_addr + LINE_CONTROL, flags & 0x3f);
	/* disable newer controller's FIFO */
	outb(io_addr + FIFO_CONTROL, 0);
}

void serport_write_string(uint16_t io_addr, char *str)
{
	for (; *str; ++str) {
		while (!(inb(io_addr + LINE_STATUS) & 0x20));
		outb(io_addr, *str);
	}
}

char serport_read_char(uint16_t io_addr)
{
	while (!(inb(io_addr + LINE_STATUS) & 1));
	return inb(io_addr);
}

/* serial debug support */

typedef struct ser_con {
	dbg_con_t base;
	uint16_t io_addr;
} ser_con_t;

void sercon_output_string(dbg_con_t *this, char *string)
{
	ser_con_t *con = (ser_con_t *) this;
	serport_write_string(con->io_addr, string);
}

dbg_con_t *setup_serial_console(uint16_t io_addr)
{
	ser_con_t *con = balloc(sizeof(ser_con_t));
	/*if (!con)
		panic();*/

	con->base.output_string = sercon_output_string;
	con->io_addr = io_addr;

	return (dbg_con_t *) con;
}
