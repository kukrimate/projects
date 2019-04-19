; Copyright (c) 2018, Máté Kukri
; This file is part of osldr and is distributed under the ISC license.
; Check license.txt for more details.

; master boot record for loading coreldr.bin from disk

%include 'asm_inc/def.inc'

; load address
org k_mbr_load_address
; 16-bit mode
bits 16

; some quirky BIOSes jump to 0x7c0:0 => fix this
jmp 0:mbr_start

; these will be set by the install program
coreldr_lba : dd 0, 0
coreldr_size: dw 0

; code start
mbr_start:
cli
xor ax, ax
mov ds, ax
mov es, ax
mov ss, ax
mov sp, k_bootloader_stack
sti

call check_lba_support
catcherr err_lba_not_supported, .fail

mov bx, word [coreldr_size]
mov eax, dword [coreldr_lba]
push k_coreldr_segment
pop es
xor di, di
call read_lba
catcherr err_disk_read_error, .fail

cmp dword [es:coreldr_hdr.magic], 'CORE'
je .magic_ok

print err_broken_coreldr
jmp .fail

.magic_ok:
push es
push word [es:coreldr_hdr.entry_offset]
retf

.fail: cli
.1: hlt
jmp .1

%include 'asm_inc/func.inc'

; strings
err_lba_not_supported db 'Error: bios does not support LBA',0xd,0xa,0
err_disk_read_error   db 'Error: disk read failed',0xd,0xa,0
err_broken_coreldr    db 'Error: invalid coreldr.bin',0xd,0xa,0
