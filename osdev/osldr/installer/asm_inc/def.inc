; Copyright (c) 2018, Máté Kukri
; This file is part of osldr and is distributed under the ISC license.
; Check license.txt for more details.

; MBR code helper definitions

; magic number of the coreldr
k_coreldr_magic equ 0x45524f43

; coreldr header structure
struc coreldr_hdr
.magic        resd 1
.entry_offset resw 1
.size:
endstruc

; size of sector as far as the BIOS is concerned
k_bios_sector_size equ 0x200

; load address of the MBR
k_mbr_load_address equ 0x7c00

; bootloader stack top
k_bootloader_stack equ k_mbr_load_address

; load segment of the coreldr
k_coreldr_segment  equ 0x1000


; print a string
;
%macro print 1
mov si, %1
call print_string
%endmacro

;
; catch an error after a helper call
;
%macro catcherr 2
test ax, ax
jz %%end
print %1
jmp %2
%%end:
%endmacro
