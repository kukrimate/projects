/*
 * Copyright (c) 2018, Máté Kukri
 * This file is part of osldr and is distributed under the ISC license.
 * Check license.txt for more details.
 */
#ifndef SERPORT_H
#define SERPORT_H

/* stop bits */
enum
{
	SER_STOP_SINGLE = 0,
	SER_STOP_DOUBLE = 4
};

/* parity type */
enum
{
	SER_PARITY_NO    = 0,
	SER_PARITY_ODD   = 8,
	SER_PARITY_EVEN  = 0x18,
	SER_PARITY_MARK  = 0x28,
	SER_PARITY_SPACE = 0x38
};

/* count of data bits */
enum
{
	SER_BITS_5 = 0,
	SER_BITS_6 = 1,
	SER_BITS_7 = 2,
	SER_BITS_8 = 3
};

void serport_setup(uint16_t io_addr, uint32_t baud_rate, uint8_t flags);
void serport_write_string(uint16_t io_addr, char *string);
char serport_read_char(uint16_t io_addr);

dbg_con_t *setup_serial_console(uint16_t io_addr);

#endif
