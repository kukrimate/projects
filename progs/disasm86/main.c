#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <fcntl.h>
#include <unistd.h>
#include "disasm.h"

struct ctx_fd {
	struct ctx ctx;
	int fd;
	size_t off, len, aval;
};

static size_t aval_fd(struct ctx *ctx)
{
	struct ctx_fd *ctx_fd = (struct ctx_fd *) ctx;
	return ctx_fd->aval;
}

static uint8_t nexb_fd(struct ctx *ctx)
{
	struct ctx_fd *ctx_fd = (struct ctx_fd *) ctx;
	if (!ctx_fd->aval)
		return 0;

	uint8_t b;
	if (-1 == pread(ctx_fd->fd, &b, 1, ctx_fd->off))
		return 0;

	++ctx_fd->off;
	--ctx_fd->aval;

	return b;
}

static uint16_t nexw_fd(struct ctx *ctx)
{
	struct ctx_fd *ctx_fd = (struct ctx_fd *) ctx;
	if (ctx_fd->aval < 2)
		return 0;

	uint16_t w;
	if (-1 == pread(ctx_fd->fd, &w, 2, ctx_fd->off))
		return 0;

	ctx_fd->off += 2;
	ctx_fd->aval -= 2;

	return w;
}

struct ctx_fd *init_ctx_fd(struct ctx_fd *ctx, int fd,
	size_t off, size_t len)
{
	ctx->ctx.aval = aval_fd;
	ctx->ctx.nexb = nexb_fd;
	ctx->ctx.nexw = nexw_fd;

	ctx->fd = fd;
	ctx->off = off;
	ctx->len = len;
	ctx->aval = len;
	return ctx;
}

int main()
{
	int fd = open("test.bin", O_RDONLY);
	if (-1 == fd) {
		perror("open()");
		goto err1;
	}

	struct ctx_fd ctx;
	disasm86((struct ctx *) init_ctx_fd(&ctx, fd, 0, lseek(fd, 0, SEEK_END)));

	if (-1 == close(fd))
		goto err1;
	return 0;
err2:
	close(fd);
err1:
	return 1;
}
