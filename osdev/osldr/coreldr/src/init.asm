; Copyright (c) 2018, Máté Kukri
; This file is part of osldr and is distributed under the ISC license.
; Check license.txt for more details.

; coreldr initialization code

; real-mode
section .real
bits 16

; real-mode entry point
global _real_start
_real_start:
cli

; load the GDT
mov ax, 0x1000
mov ds, ax
lgdt [gdtr]

; enable protected-mode
mov eax, cr0
or al, 1
mov cr0, eax

; jump to protected-mode
jmp dword 8:_pm_init

; GDT
gdt:
dd 0, 0
db 0xff, 0xff, 0, 0, 0, 10011010b, 11001111b, 0
db 0xff, 0xff, 0, 0, 0, 10010010b, 11001111b, 0
gdtr:
dw gdtr - gdt - 1
dd gdt + 0x10000

; protected-mode
section .text
bits 32

_pm_init:
; setup protected-mode data segments
mov ax, 0x10
mov ds, ax
mov es, ax
mov ss, ax
mov fs, ax
mov gs, ax

; protected mode stack
mov esp, 0x80000

; give control to 'C' code
extern ldrmain
call ldrmain

; hang here if 'C' code returns
.1: hlt
jmp .1
