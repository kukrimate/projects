; Copyright (c) 2018, Máté Kukri
; This file is part of osldr and is distributed under the ISC license.
; Check license.txt for more details.

; MBR code helper functions

;
; print an ascii string to the screen
; si: string address in memory
;
print_string:
push ax
push si
pushf
.next_char:
lodsb
test al, al
jz .done
mov ah, 0xe
int 0x10
jmp .next_char
.done:
popf
pop si
pop ax
ret

;
; check if LBA interrupts are supported
; dl: must contain the drive number
; return: ax = 0 if supported
;
check_lba_support:
push bx
push cx
push dx
pushf

; call the BIOS installation check
mov ah, 0x41
mov bx, 0x55aa
int 0x13

; error
jc .fail

; not installed
cmp bx, 0xaa55
jne .fail

; LBA not supported
and cx, 1
jz .fail

; success
xor ax, ax
jmp .done

; fail
.fail:
xor ax, ax
inc al

.done:
popf
pop dx
pop cx
pop bx
ret

;
; Read sectors from disk using LBA
; dl    = drive number
; bx    = sector count
; eax   = LBA
; es:di = target memory buffer
; return ax = 0 on success
;
read_lba:
push si
pushf

; DAP
push dword 0
push dword eax ; 64-bit LBA
push es
push di        ; buffer
push bx        ; sector count
push 0x10

; call the BIOS
mov si, sp
mov ah, 0x42
int 0x13

; disk read failed
jc .fail

.sucess:
xor ax, ax
jmp .done

.fail:
xor ax, ax
inc al

.done:
add sp, 0x10 ; get rid of the DAP
popf
pop si
ret
