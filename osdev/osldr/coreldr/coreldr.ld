/*
 * Copyright (c) 2018, Máté Kukri
 * This file is part of osldr and is distributed under the ISC license.
 * Check license.txt for more details.
 */
/* coreldr linker script */
OUTPUT_FORMAT(binary)

/* header magic */
HDR_MAGIC = 0x45524f43;
HDR_ENTRY = _real_start;

/* protected-mode addres */
LOAD_ADDR = 0x10000;
/* real-mode segment */
LOAD_SEGM = LOAD_ADDR >> 4;

SECTIONS
{
	/* real-mode code and data */
	.hdr 0 : AT(LOAD_ADDR)
	{
		LONG  (HDR_MAGIC);
		SHORT (HDR_ENTRY);
	}

	.real :
	{
		*(.real)
	}

	/* protected-mode code */
	pm_start = LOAD_ADDR + SIZEOF(.hdr) + SIZEOF(.real);

	.text pm_start : AT(pm_start)
	{
		*(.text)
	}

	/* protected-mode data */
	.rodata :
	{
		*(.rodata)
	}

	.data :
	{
		*(.data)
	}

	.bss :
	{
		*(COMMON)
		*(.bss)
	}

	end_of_binary = .;

	/* discard unneeded sections */
	/DISCARD/ : { *(*) }
}
