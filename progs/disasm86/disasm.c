#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <errno.h>
#include <unistd.h>
#include "disasm.h"

static void reg8(uint8_t n)
{
	switch (n) {
	case 0:
		printf("al");
		break;
	case 1:
		printf("cl");
		break;
	case 2:
		printf("dl");
		break;
	case 3:
		printf("bl");
		break;
	case 4:
		printf("ah");
		break;
	case 5:
		printf("ch");
		break;
	case 6:
		printf("dh");
		break;
	case 7:
		printf("bh");
		break;
	default:
		printf("invalid reg8");
		break;
	}
}

static void reg16(uint8_t n)
{
	switch (n) {
	case 0:
		printf("ax");
		break;
	case 1:
		printf("cx");
		break;
	case 2:
		printf("dx");
		break;
	case 3:
		printf("bx");
		break;
	case 4:
		printf("sp");
		break;
	case 5:
		printf("bp");
		break;
	case 6:
		printf("si");
		break;
	case 7:
		printf("di");
		break;
	default:
		printf("invalid reg8");
		break;
	}
}

static void sr(uint8_t n)
{
	switch (n) {
	case 0:
		printf("es");
		break;
	case 1:
		printf("cs");
		break;
	case 2:
		printf("ss");
		break;
	case 3:
		printf("ds");
		break;
	default:
		printf("invalid sr");
		break;
	}
}

#define MOD(n) (n >> 6 & 3)
#define REG(n) (n >> 3 & 7)
#define RM(n) (n & 7)

static void modrm(uint8_t n, int word, struct ctx* ctx)
{
	uint8_t mod = MOD(n);
	uint8_t rm = RM(n);

	// Register
	if (mod == 3) {
		if (word)
			reg16(rm);
		else
			reg8(rm);
		return;
	}

	if (word)
		printf("word [");
	else
		printf("byte [");

	// Direct address
	if (!mod && rm == 6) {
		printf("%04x]", ctx->nexw(ctx));
		return;
	}

	switch (rm) {
	case 0:
		printf("bx + si");
		break;
	case 1:
		printf("bx + di");
		break;
	case 2:
		printf("bp + si");
		break;
	case 3:
		printf("bp + di");
		break;
	case 4:
		printf("si");
		break;
	case 5:
		printf("di");
		break;
	case 6:
		printf("bp");
		break;
	case 7:
		printf("bx");
		break;
	default:
		printf("invalid rm");
		break;
	}

	switch (mod) {
	case 0:
		printf("]");
		break;
	case 1:
		printf(" + %02x]", ctx->nexb(ctx));
		break;
	case 3:
		printf(" + %04x]", ctx->nexw(ctx));
		break;
	default:
		printf(" + invalid mod]");
		break;
	}
}

// add r/m8, r8
void op_00(struct ctx *ctx, uint8_t b)
{
	printf("add ");
	uint8_t b2 = ctx->nexb(ctx);
	modrm(b2, 0, ctx);
	printf(", ");
	reg8(REG(b2));
}
// add r/m16, r16
void op_01(struct ctx *ctx, uint8_t b)
{
	printf("add ");
	uint8_t b2 = ctx->nexb(ctx);
	modrm(b2, 1, ctx);
	printf(", ");
	reg16(REG(b2));
}
// add r8, r/m8
void op_02(struct ctx *ctx, uint8_t b)
{
	printf("add ");
	uint8_t b2 = ctx->nexb(ctx);
	reg8(REG(b2));
	printf(", ");
	modrm(b2, 0, ctx);
}
// add r16, r/m16
void op_03(struct ctx *ctx, uint8_t b)
{
	printf("add ");
	uint8_t b2 = ctx->nexb(ctx);
	reg16(REG(b2));
	printf(", ");
	modrm(b2, 1, ctx);
}
// add al, imm8
void op_04(struct ctx *ctx, uint8_t b)
{
	printf("add al, %02x", ctx->nexb(ctx));
}
// add ax, imm16
void op_05(struct ctx *ctx, uint8_t b)
{
	printf("add ax, %04x", ctx->nexw(ctx));
}

// push es
void op_06(struct ctx *ctx, uint8_t b)
{
	printf("push es");
}
// pop es
void op_07(struct ctx *ctx, uint8_t b)
{
	printf("pop es");
}

// or r/m8, r8
void op_08(struct ctx *ctx, uint8_t b)
{
	printf("or ");
	uint8_t b2 = ctx->nexb(ctx);
	modrm(b2, 0, ctx);
	printf(", ");
	reg8(REG(b2));
}
// or r/m16, r16
void op_09(struct ctx *ctx, uint8_t b)
{
	printf("or ");
	uint8_t b2 = ctx->nexb(ctx);
	modrm(b2, 1, ctx);
	printf(", ");
	reg16(REG(b2));
}
// or r8, r/m8
void op_0a(struct ctx *ctx, uint8_t b)
{
	printf("or ");
	uint8_t b2 = ctx->nexb(ctx);
	reg8(REG(b2));
	printf(", ");
	modrm(b2, 0, ctx);
}
// or r16, r/m16
void op_0b(struct ctx *ctx, uint8_t b)
{
	printf("or ");
	uint8_t b2 = ctx->nexb(ctx);
	reg16(REG(b2));
	printf(", ");
	modrm(b2, 1, ctx);
}
// or al, imm8
void op_0c(struct ctx *ctx, uint8_t b)
{
	printf("or al, %02x", ctx->nexb(ctx));
}
// or ax, imm16
void op_0d(struct ctx *ctx, uint8_t b)
{
	printf("or ax, %04x", ctx->nexw(ctx));
}

// push cs
void op_0e(struct ctx *ctx, uint8_t b)
{
	printf("push cs");
}
// pop cs NOTE: this is 8088/8086 only
void op_0f(struct ctx *ctx, uint8_t b)
{
	printf("pop cs");
}

// adc r/m8, r8
void op_10(struct ctx *ctx, uint8_t b)
{
	printf("adc ");
	uint8_t b2 = ctx->nexb(ctx);
	modrm(b2, 0, ctx);
	printf(", ");
	reg8(REG(b2));
}
// adc r/m16, r16
void op_11(struct ctx *ctx, uint8_t b)
{
	printf("adc ");
	uint8_t b2 = ctx->nexb(ctx);
	modrm(b2, 1, ctx);
	printf(", ");
	reg16(REG(b2));
}
// adc r8, r/m8
void op_12(struct ctx *ctx, uint8_t b)
{
	printf("adc ");
	uint8_t b2 = ctx->nexb(ctx);
	reg8(REG(b2));
	printf(", ");
	modrm(b2, 0, ctx);
}
// adc r16, r/m16
void op_13(struct ctx *ctx, uint8_t b)
{
	printf("adc ");
	uint8_t b2 = ctx->nexb(ctx);
	reg16(REG(b2));
	printf(", ");
	modrm(b2, 1, ctx);
}
// adc al, imm8
void op_14(struct ctx *ctx, uint8_t b)
{
	printf("adc al, %02x", ctx->nexb(ctx));
}
// adc ax, imm16
void op_15(struct ctx *ctx, uint8_t b)
{
	printf("adc ax, %04x", ctx->nexw(ctx));
}

// push ss
void op_16(struct ctx *ctx, uint8_t b)
{
	printf("push ss");
}
// pop ss
void op_17(struct ctx *ctx, uint8_t b)
{
	printf("pop cs");
}

// sbb r/m8, r8
void op_18(struct ctx *ctx, uint8_t b)
{
	printf("sbb ");
	uint8_t b2 = ctx->nexb(ctx);
	modrm(b2, 0, ctx);
	printf(", ");
	reg8(REG(b2));
}
// sbb r/m16, r16
void op_19(struct ctx *ctx, uint8_t b)
{
	printf("sbb ");
	uint8_t b2 = ctx->nexb(ctx);
	modrm(b2, 1, ctx);
	printf(", ");
	reg16(REG(b2));
}
// sbb r8, r/m8
void op_1a(struct ctx *ctx, uint8_t b)
{
	printf("sbb ");
	uint8_t b2 = ctx->nexb(ctx);
	reg8(REG(b2));
	printf(", ");
	modrm(b2, 0, ctx);
}
// sbb r16, r/m16
void op_1b(struct ctx *ctx, uint8_t b)
{
	printf("sbb ");
	uint8_t b2 = ctx->nexb(ctx);
	reg16(REG(b2));
	printf(", ");
	modrm(b2, 1, ctx);
}
// sbb al, imm8
void op_1c(struct ctx *ctx, uint8_t b)
{
	printf("sbb al, %02x", ctx->nexb(ctx));
}
// sbb ax, imm16
void op_1d(struct ctx *ctx, uint8_t b)
{
	printf("sbb ax, %04x", ctx->nexw(ctx));
}

// push ds
void op_1e(struct ctx *ctx, uint8_t b)
{
	printf("push ds");
}
// pop ds
void op_1f(struct ctx *ctx, uint8_t b)
{
	printf("pop ds");
}

// and r/m8, r8
void op_20(struct ctx *ctx, uint8_t b)
{
	printf("and ");
	uint8_t b2 = ctx->nexb(ctx);
	modrm(b2, 0, ctx);
	printf(", ");
	reg8(REG(b2));
}
// and r/m16, r16
void op_21(struct ctx *ctx, uint8_t b)
{
	printf("and ");
	uint8_t b2 = ctx->nexb(ctx);
	modrm(b2, 1, ctx);
	printf(", ");
	reg16(REG(b2));
}
// and r8, r/m8
void op_22(struct ctx *ctx, uint8_t b)
{
	printf("and ");
	uint8_t b2 = ctx->nexb(ctx);
	reg8(REG(b2));
	printf(", ");
	modrm(b2, 0, ctx);
}
// and r16, r/m16
void op_23(struct ctx *ctx, uint8_t b)
{
	printf("and ");
	uint8_t b2 = ctx->nexb(ctx);
	reg16(REG(b2));
	printf(", ");
	modrm(b2, 1, ctx);
}
// and al, imm8
void op_24(struct ctx *ctx, uint8_t b)
{
	printf("and al, %02x", ctx->nexb(ctx));
}
// and ax, imm16
void op_25(struct ctx *ctx, uint8_t b)
{
	printf("and ax, %04x", ctx->nexw(ctx));
}

// Override ES:
void op_26(struct ctx *ctx, uint8_t b)
{
	printf("ES: ");
}

// daa
void op_27(struct ctx *ctx, uint8_t b)
{
	printf("daa");
}

// sub r/m8, r8
void op_28(struct ctx *ctx, uint8_t b)
{
	printf("sub ");
	uint8_t b2 = ctx->nexb(ctx);
	modrm(b2, 0, ctx);
	printf(", ");
	reg8(REG(b2));
}
// sub r/m16, r16
void op_29(struct ctx *ctx, uint8_t b)
{
	printf("sub ");
	uint8_t b2 = ctx->nexb(ctx);
	modrm(b2, 1, ctx);
	printf(", ");
	reg16(REG(b2));
}
// sub r8, r/m8
void op_2a(struct ctx *ctx, uint8_t b)
{
	printf("sub ");
	uint8_t b2 = ctx->nexb(ctx);
	reg8(REG(b2));
	printf(", ");
	modrm(b2, 0, ctx);
}
// sub r16, r/m16
void op_2b(struct ctx *ctx, uint8_t b)
{
	printf("sub ");
	uint8_t b2 = ctx->nexb(ctx);
	reg16(REG(b2));
	printf(", ");
	modrm(b2, 1, ctx);
}
// sub al, imm8
void op_2c(struct ctx *ctx, uint8_t b)
{
	printf("sub al, %02x", ctx->nexb(ctx));
}
// sub ax, imm16
void op_2d(struct ctx *ctx, uint8_t b)
{
	printf("sub ax, %04x", ctx->nexw(ctx));
}

// Override CS:
void op_2e(struct ctx *ctx, uint8_t b)
{
	printf("CS: ");
}

// das
void op_2f(struct ctx *ctx, uint8_t b)
{
	printf("das");
}

// xor r/m8, r8
void op_30(struct ctx *ctx, uint8_t b)
{
	printf("xor ");
	uint8_t b2 = ctx->nexb(ctx);
	modrm(b2, 0, ctx);
	printf(", ");
	reg8(REG(b2));
}
// xor r/m16, r16
void op_31(struct ctx *ctx, uint8_t b)
{
	printf("xor ");
	uint8_t b2 = ctx->nexb(ctx);
	modrm(b2, 1, ctx);
	printf(", ");
	reg16(REG(b2));
}
// xor r8, r/m8
void op_32(struct ctx *ctx, uint8_t b)
{
	printf("xor ");
	uint8_t b2 = ctx->nexb(ctx);
	reg8(REG(b2));
	printf(", ");
	modrm(b2, 0, ctx);
}
// xor r16, r/m16
void op_33(struct ctx *ctx, uint8_t b)
{
	printf("xor ");
	uint8_t b2 = ctx->nexb(ctx);
	reg16(REG(b2));
	printf(", ");
	modrm(b2, 1, ctx);
}
// xor al, imm8
void op_34(struct ctx *ctx, uint8_t b)
{
	printf("xor al, %02x", ctx->nexb(ctx));
}
// xor ax, imm16
void op_35(struct ctx *ctx, uint8_t b)
{
	printf("xor ax, %04x", ctx->nexw(ctx));
}

// Override SS:
void op_36(struct ctx *ctx, uint8_t b)
{
	printf("SS: ");
}

// aaa
void op_37(struct ctx *ctx, uint8_t b)
{
	printf("aaa");
}

// cmp r/m8, r8
void op_38(struct ctx *ctx, uint8_t b)
{
	printf("cmp ");
	uint8_t b2 = ctx->nexb(ctx);
	modrm(b2, 0, ctx);
	printf(", ");
	reg8(REG(b2));
}
// cmp r/m16, r16
void op_39(struct ctx *ctx, uint8_t b)
{
	printf("cmp ");
	uint8_t b2 = ctx->nexb(ctx);
	modrm(b2, 1, ctx);
	printf(", ");
	reg16(REG(b2));
}
// cmp r8, r/m8
void op_3a(struct ctx *ctx, uint8_t b)
{
	printf("cmp ");
	uint8_t b2 = ctx->nexb(ctx);
	reg8(REG(b2));
	printf(", ");
	modrm(b2, 0, ctx);
}
// cmp r16, r/m16
void op_3b(struct ctx *ctx, uint8_t b)
{
	printf("cmp ");
	uint8_t b2 = ctx->nexb(ctx);
	reg16(REG(b2));
	printf(", ");
	modrm(b2, 1, ctx);
}
// cmp al, imm8
void op_3c(struct ctx *ctx, uint8_t b)
{
	printf("cmp al, %02x", ctx->nexb(ctx));
}
// cmp ax, imm16
void op_3d(struct ctx *ctx, uint8_t b)
{
	printf("cmp ax, %04x", ctx->nexw(ctx));
}

// Override DS:
void op_3e(struct ctx *ctx, uint8_t b)
{
	printf("DS: ");
}

// aas
void op_3f(struct ctx *ctx, uint8_t b)
{
	printf("aas");
}

// inc ax
void op_40(struct ctx *ctx, uint8_t b)
{
	printf("inc ax");
}
// inc cx
void op_41(struct ctx *ctx, uint8_t b)
{
	printf("inc cx");
}
// inc dx
void op_42(struct ctx *ctx, uint8_t b)
{
	printf("inc dx");
}
// inc bx
void op_43(struct ctx *ctx, uint8_t b)
{
	printf("inc bx");
}
// inc sp
void op_44(struct ctx *ctx, uint8_t b)
{
	printf("inc sp");
}
// inc bp
void op_45(struct ctx *ctx, uint8_t b)
{
	printf("inc bp");
}
// inc si
void op_46(struct ctx *ctx, uint8_t b)
{
	printf("inc si");
}
// inc di
void op_47(struct ctx *ctx, uint8_t b)
{
	printf("inc di");
}
// dec ax
void op_48(struct ctx *ctx, uint8_t b)
{
	printf("dec ax");
}
// dec cx
void op_49(struct ctx *ctx, uint8_t b)
{
	printf("dec cx");
}
// dec dx
void op_4a(struct ctx *ctx, uint8_t b)
{
	printf("dec dx");
}
// dec bx
void op_4b(struct ctx *ctx, uint8_t b)
{
	printf("dec bx");
}
// dec sp
void op_4c(struct ctx *ctx, uint8_t b)
{
	printf("dec sp");
}
// dec bp
void op_4d(struct ctx *ctx, uint8_t b)
{
	printf("dec bp");
}
// dec si
void op_4e(struct ctx *ctx, uint8_t b)
{
	printf("dec si");
}
// dec di
void op_4f(struct ctx *ctx, uint8_t b)
{
	printf("dec di");
}

// push ax
void op_50(struct ctx *ctx, uint8_t b)
{
	printf("push ax");
}
// push cx
void op_51(struct ctx *ctx, uint8_t b)
{
	printf("push cx");
}
// push dx
void op_52(struct ctx *ctx, uint8_t b)
{
	printf("push dx");
}
// push bx
void op_53(struct ctx *ctx, uint8_t b)
{
	printf("push bx");
}
// push sp
void op_54(struct ctx *ctx, uint8_t b)
{
	printf("push sp");
}
// push bp
void op_55(struct ctx *ctx, uint8_t b)
{
	printf("push bp");
}
// push si
void op_56(struct ctx *ctx, uint8_t b)
{
	printf("push si");
}
// push di
void op_57(struct ctx *ctx, uint8_t b)
{
	printf("push di");
}
// pop ax
void op_58(struct ctx *ctx, uint8_t b)
{
	printf("pop ax");
}
// pop cx
void op_59(struct ctx *ctx, uint8_t b)
{
	printf("pop cx");
}
// pop dx
void op_5a(struct ctx *ctx, uint8_t b)
{
	printf("pop dx");
}
// pop bx
void op_5b(struct ctx *ctx, uint8_t b)
{
	printf("pop bx");
}
// pop sp
void op_5c(struct ctx *ctx, uint8_t b)
{
	printf("pop sp");
}
// pop bp
void op_5d(struct ctx *ctx, uint8_t b)
{
	printf("pop bp");
}
// pop si
void op_5e(struct ctx *ctx, uint8_t b)
{
	printf("pop si");
}
// pop di
void op_5f(struct ctx *ctx, uint8_t b)
{
	printf("pop di");
}

// Unused
void op_6x(struct ctx *ctx, uint8_t b)
{
	printf("unused %02x\n", b);
}

// jx short-label
void op_7x(struct ctx *ctx, uint8_t b)
{
	char *op;

	switch (b) {
	case 0x70: // jo
		op = "jo";
		break;
	case 0x71: // jno
		op = "jno";
		break;
	case 0x72: // jb/jnae/jc
		op = "jc";
		break;
	case 0x73: // jnb/jae/jnc
		op = "jnc";
		break;
	case 0x74: // je/jz
		op = "jz";
		break;
	case 0x75: // jne/jnz
		op = "jnz";
		break;
	case 0x76: // jbe/jna
		op = "jna";
		break;
	case 0x77: // jnbe/ja
		op = "ja";
		break;
	case 0x78: // js
		op = "js";
		break;
	case 0x79: // jns
		op = "jns";
		break;
	case 0x7a: // jp/jpe
		op = "jp";
		break;
	case 0x7b: // jnp/jpo
		op = "jnp";
		break;
	case 0x7c: // jl/jnge
		op = "jl";
		break;
	case 0x7d: // jnl/jge
		op = "jnl";
		break;
	case 0x7e: // jle/jng
		op = "jng";
		break;
	case 0x7f: // jnle/jg
		op = "jg";
		break;
	}
	printf("%s %02x", op, ctx->nexb(ctx));
}

void op_8x(struct ctx *ctx, uint8_t b)
{

}

// xchg ax, ax/nop
void op_90(struct ctx *ctx, uint8_t b)
{
	printf("nop");
}
// xchg ax, cx
void op_91(struct ctx *ctx, uint8_t b)
{
	printf("xchg ax, cx");
}
// xchg ax, dx
void op_92(struct ctx *ctx, uint8_t b)
{
	printf("xchg ax, dx");
}
// xchg ax, bx
void op_93(struct ctx *ctx, uint8_t b)
{
	printf("xchg ax, bx");
}
// xchg ax, sp
void op_94(struct ctx *ctx, uint8_t b)
{
	printf("xchg ax, sp");
}
// xchg ax, bp
void op_95(struct ctx *ctx, uint8_t b)
{
	printf("xchg ax, bp");
}
// xchg ax, si
void op_96(struct ctx *ctx, uint8_t b)
{
	printf("xchg ax, si");
}
// xchg ax, di
void op_97(struct ctx *ctx, uint8_t b)
{
	printf("xchg ax, di");
}

// cbw
void op_98(struct ctx *ctx, uint8_t b)
{
	printf("cbw");
}
// cwd
void op_99(struct ctx *ctx, uint8_t b)
{
	printf("cwd");
}

// callf segm:offs
void op_9a(struct ctx *ctx, uint8_t b)
{
	uint16_t offs = ctx->nexw(ctx);
	uint16_t segm = ctx->nexw(ctx);
	printf("callf %04x:%04x", segm, offs);
}

// wait
void op_9b(struct ctx *ctx, uint8_t b)
{
	printf("wait");
}

// pushf
void op_9c(struct ctx *ctx, uint8_t b)
{
	printf("pushf");
}
// popf
void op_9d(struct ctx *ctx, uint8_t b)
{
	printf("popf");
}

// sahf
void op_9e(struct ctx *ctx, uint8_t b)
{
	printf("sahf");
}
// lahf
void op_9f(struct ctx *ctx, uint8_t b)
{
	printf("lahf");
}

// mov al, m8
void op_a0(struct ctx *ctx, uint8_t b)
{
	printf("mov al, byte [%04x]", ctx->nexw(ctx));
}
// mov ax, m16
void op_a1(struct ctx *ctx, uint8_t b)
{
	printf("mov ax, word [%04x]", ctx->nexw(ctx));
}
// mov m8, al
void op_a2(struct ctx *ctx, uint8_t b)
{
	printf("mov byte [%04x], al", ctx->nexw(ctx));
}
// mov m16, al
void op_a3(struct ctx *ctx, uint8_t b)
{
	printf("mov word [%04x], ax", ctx->nexw(ctx));
}

void op_a4(struct ctx *ctx, uint8_t b)
{
	printf("movsb");
}
void op_a5(struct ctx *ctx, uint8_t b)
{
	printf("movsw");
}
void op_a6(struct ctx *ctx, uint8_t b)
{
	printf("cmpsb");
}
void op_a7(struct ctx *ctx, uint8_t b)
{
	printf("cmpsw");
}

// test al, imm8
void op_a8(struct ctx *ctx, uint8_t b)
{
	printf("test al, %02x", ctx->nexb(ctx));
}
// test ax, imm16
void op_a9(struct ctx *ctx, uint8_t b)
{
	printf("test ax, %04x", ctx->nexw(ctx));
}

void op_aa(struct ctx *ctx, uint8_t b)
{
	printf("stosb");
}
void op_ab(struct ctx *ctx, uint8_t b)
{
	printf("stosw");
}
void op_ac(struct ctx *ctx, uint8_t b)
{
	printf("lodsb");
}
void op_ad(struct ctx *ctx, uint8_t b)
{
	printf("lodsw");
}
void op_ae(struct ctx *ctx, uint8_t b)
{
	printf("scasb");
}
void op_af(struct ctx *ctx, uint8_t b)
{
	printf("scasw");
}

void op_bx(struct ctx *ctx, uint8_t b)
{
	printf("mov ");
	if (b < 0xb8) {
		reg8(b - 0xb0);
		printf(", %02x", ctx->nexb(ctx));
	} else {
		reg16(b - 0xb8);
		printf(", %04x", ctx->nexw(ctx));
	}
}

void op_c0(struct ctx *ctx, uint8_t b)
{
	printf("invalid c0");
}
void op_c1(struct ctx *ctx, uint8_t b)
{
	printf("invalid c1");
}

void op_c2(struct ctx *ctx, uint8_t b) {}
void op_c3(struct ctx *ctx, uint8_t b) {}
void op_c4(struct ctx *ctx, uint8_t b) {}
void op_c5(struct ctx *ctx, uint8_t b) {}
void op_c6(struct ctx *ctx, uint8_t b) {}
void op_c7(struct ctx *ctx, uint8_t b) {}
void op_c8(struct ctx *ctx, uint8_t b) {}
void op_c9(struct ctx *ctx, uint8_t b) {}
void op_ca(struct ctx *ctx, uint8_t b) {}
void op_cb(struct ctx *ctx, uint8_t b) {}
void op_cc(struct ctx *ctx, uint8_t b) {}
void op_cd(struct ctx *ctx, uint8_t b) {}
void op_ce(struct ctx *ctx, uint8_t b) {}
void op_cf(struct ctx *ctx, uint8_t b) {}
void op_d0(struct ctx *ctx, uint8_t b) {}
void op_d1(struct ctx *ctx, uint8_t b) {}
void op_d2(struct ctx *ctx, uint8_t b) {}
void op_d3(struct ctx *ctx, uint8_t b) {}
void op_d4(struct ctx *ctx, uint8_t b) {}
void op_d5(struct ctx *ctx, uint8_t b) {}
void op_d6(struct ctx *ctx, uint8_t b) {}
void op_d7(struct ctx *ctx, uint8_t b) {}
void op_d8(struct ctx *ctx, uint8_t b) {}
void op_d9(struct ctx *ctx, uint8_t b) {}
void op_da(struct ctx *ctx, uint8_t b) {}
void op_db(struct ctx *ctx, uint8_t b) {}
void op_dc(struct ctx *ctx, uint8_t b) {}
void op_dd(struct ctx *ctx, uint8_t b) {}
void op_de(struct ctx *ctx, uint8_t b) {}
void op_df(struct ctx *ctx, uint8_t b) {}
void op_e0(struct ctx *ctx, uint8_t b) {}
void op_e1(struct ctx *ctx, uint8_t b) {}
void op_e2(struct ctx *ctx, uint8_t b) {}
void op_e3(struct ctx *ctx, uint8_t b) {}
void op_e4(struct ctx *ctx, uint8_t b) {}
void op_e5(struct ctx *ctx, uint8_t b) {}
void op_e6(struct ctx *ctx, uint8_t b) {}
void op_e7(struct ctx *ctx, uint8_t b) {}
void op_e8(struct ctx *ctx, uint8_t b) {}
void op_e9(struct ctx *ctx, uint8_t b) {}
void op_ea(struct ctx *ctx, uint8_t b) {}
void op_eb(struct ctx *ctx, uint8_t b) {}
void op_ec(struct ctx *ctx, uint8_t b) {}
void op_ed(struct ctx *ctx, uint8_t b) {}
void op_ee(struct ctx *ctx, uint8_t b) {}
void op_ef(struct ctx *ctx, uint8_t b) {}
void op_f0(struct ctx *ctx, uint8_t b) {}
void op_f1(struct ctx *ctx, uint8_t b) {}
void op_f2(struct ctx *ctx, uint8_t b) {}
void op_f3(struct ctx *ctx, uint8_t b) {}
void op_f4(struct ctx *ctx, uint8_t b) {}
void op_f5(struct ctx *ctx, uint8_t b) {}
void op_f6(struct ctx *ctx, uint8_t b) {}
void op_f7(struct ctx *ctx, uint8_t b) {}
void op_f8(struct ctx *ctx, uint8_t b) {}
void op_f9(struct ctx *ctx, uint8_t b) {}
void op_fa(struct ctx *ctx, uint8_t b) {}
void op_fb(struct ctx *ctx, uint8_t b) {}
void op_fc(struct ctx *ctx, uint8_t b) {}
void op_fd(struct ctx *ctx, uint8_t b) {}
void op_fe(struct ctx *ctx, uint8_t b) {}
void op_ff(struct ctx *ctx, uint8_t b) {}

// Opcode jump table
typedef void (*jmp_fn)(struct ctx *ctx, uint8_t b);
static jmp_fn jmp_tbl[] = {
op_00, op_01, op_02, op_03, op_04, op_05, op_06, op_07,
op_08, op_09, op_0a, op_0b, op_0c, op_0d, op_0e, op_0f,
op_10, op_11, op_12, op_13, op_14, op_15, op_16, op_17,
op_18, op_19, op_1a, op_1b, op_1c, op_1d, op_1e, op_1f,
op_20, op_21, op_22, op_23, op_24, op_25, op_26, op_27,
op_28, op_29, op_2a, op_2b, op_2c, op_2d, op_2e, op_2f,
op_30, op_31, op_32, op_33, op_34, op_35, op_36, op_37,
op_38, op_39, op_3a, op_3b, op_3c, op_3d, op_3e, op_3f,
op_40, op_41, op_42, op_43, op_44, op_45, op_46, op_47,
op_48, op_49, op_4a, op_4b, op_4c, op_4d, op_4e, op_4f,
op_50, op_51, op_52, op_53, op_54, op_55, op_56, op_57,
op_58, op_59, op_5a, op_5b, op_5c, op_5d, op_5e, op_5f,
op_6x, op_6x, op_6x, op_6x, op_6x, op_6x, op_6x, op_6x,
op_6x, op_6x, op_6x, op_6x, op_6x, op_6x, op_6x, op_6x,
op_7x, op_7x, op_7x, op_7x, op_7x, op_7x, op_7x, op_7x,
op_7x, op_7x, op_7x, op_7x, op_7x, op_7x, op_7x, op_7x,
op_8x, op_8x, op_8x, op_8x, op_8x, op_8x, op_8x, op_8x,
op_8x, op_8x, op_8x, op_8x, op_8x, op_8x, op_8x, op_8x,
op_90, op_91, op_92, op_93, op_94, op_95, op_96, op_97,
op_98, op_99, op_9a, op_9b, op_9c, op_9d, op_9e, op_9f,
op_a0, op_a1, op_a2, op_a3, op_a4, op_a5, op_a6, op_a7,
op_a8, op_a9, op_aa, op_ab, op_ac, op_ad, op_ae, op_af,
op_bx, op_bx, op_bx, op_bx, op_bx, op_bx, op_bx, op_bx,
op_bx, op_bx, op_bx, op_bx, op_bx, op_bx, op_bx, op_bx,
op_c0, op_c1, op_c2, op_c3, op_c4, op_c5, op_c6, op_c7,
op_c8, op_c9, op_ca, op_cb, op_cc, op_cd, op_ce, op_cf,
op_d0, op_d1, op_d2, op_d3, op_d4, op_d5, op_d6, op_d7,
op_d8, op_d9, op_da, op_db, op_dc, op_dd, op_de, op_df,
op_e0, op_e1, op_e2, op_e3, op_e4, op_e5, op_e6, op_e7,
op_e8, op_e9, op_ea, op_eb, op_ec, op_ed, op_ee, op_ef,
op_f0, op_f1, op_f2, op_f3, op_f4, op_f5, op_f6, op_f7,
op_f8, op_f9, op_fa, op_fb, op_fc, op_fd, op_fe, op_ff,
};

int disasm86(struct ctx *ctx)
{
	while (ctx->aval(ctx)) {
		uint8_t b = ctx->nexb(ctx);
		jmp_tbl[b](ctx, b);
		printf("\n");
	}
}
