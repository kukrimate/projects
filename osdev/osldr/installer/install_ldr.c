/*
 * Copyright (c) 2018, Máté Kukri
 * This file is part of osldr and is distributed under the ISC license.
 * Check license.txt for more details.
 */
/* osldr installer program, currently only supports MBR detection */
#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <limits.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <sys/sendfile.h>
#include <fcntl.h>
#include <unistd.h>

#include "mbrcode.h"

struct mbr_part {
	uint8_t active_flag;
	uint8_t start_CHS[3];
	uint8_t system_id;
	uint8_t end_CHS[3];
	uint32_t start_LBA;
	uint32_t total_sectors;
} __attribute__((packed));

/* make sure we do not overwrite the partition table */
#if 440 < MBRCODE_LEN
#error MBR binary must be less than 440 bytes
#endif

/* Size limits */
#define CORE_MAX 458752

/* MBR magic number */
#define MBR_MAGIC 0xAA55

static int install_mbr(int devfd, uint64_t core_lba, uint16_t core_sectors)
{
	*((uint64_t *) (mbrcode + 5)) = core_lba;
	*((uint16_t *) (mbrcode + 13)) = core_sectors;

	if (-1 == pwrite(devfd, mbrcode, MBRCODE_LEN, 0))
		return -1;

	const uint16_t magic = MBR_MAGIC;
	if (-1 == pwrite(devfd, &magic, 2, 510))
		return -1;

	return 0;
}

static int copy_to(int tgtfd, int srcfd, long target_offset, size_t src_count)
{
	uint8_t buffer[1024];
	if (-1 == lseek(srcfd, 0, SEEK_SET))
		return -1;
	if (-1 == lseek(tgtfd, target_offset, SEEK_SET))
		return -1;

	while (src_count) {
		ssize_t cnt = read(srcfd, buffer, 1024);
		if (-1 == cnt)
			return -1;
		if (-1 == write(tgtfd, buffer, cnt))
			return -1;

		src_count -= cnt;
	}

	return 0;
}

int main(int argc, char **argv)
{
	int exit_code = EXIT_SUCCESS;

	if (argc < 3) {
		fprintf(stderr, "Usage: install_ldr <device> <core_binary>\n");
		exit_code = EXIT_FAILURE;
		goto exit;
	}

	int corefd = open(argv[2], O_RDONLY);
	if (-1 == corefd) {
		perror("open(core_binary)");
		exit_code = EXIT_FAILURE;
		goto exit;
	}

	struct stat corefd_stat;
	if (-1 == fstat(corefd, &corefd_stat)) {
		perror("stat(core_binary)");
		exit_code = EXIT_FAILURE;
		goto cleanup_corefd;
	}

	if (corefd_stat.st_size > CORE_MAX) {
		fprintf(stderr, "core binary must be less than %d bytes\n", CORE_MAX);
		exit_code = EXIT_FAILURE;
		goto cleanup_corefd;
	}

	int devfd = open(argv[1], O_RDWR);
	if (-1 == devfd) {
		perror("open(device)");
		exit_code = EXIT_FAILURE;
		goto cleanup_corefd;
	}

	uint8_t buffer[512];
	if (-1 == pread(devfd, buffer, 512, 0)) {
		perror("read(device)");
		exit_code = EXIT_FAILURE;
		goto cleanup_devfd;
	}

	long first_partition_offset = LONG_MAX;
	int valid_table = 0;

	struct mbr_part *current_partition = (void *) (buffer + 446);
	for (int i = 0; i < 4; ++i, ++current_partition) {
		long current_offset = current_partition->start_LBA * 512;
		if (current_partition->system_id) {
			valid_table = 1;
			if (current_offset < first_partition_offset)
				first_partition_offset = current_offset;
		}
	}

	if (!valid_table) {
		printf("Warning: device does not contain a valid fdisk partition table\n");
	} else if (corefd_stat.st_size > (first_partition_offset - 512)) {
		fprintf(stderr, "Error: the core binary does not fit\n");
		exit_code = EXIT_FAILURE;
		goto cleanup_devfd;
	}

	if (-1 == install_mbr(devfd, 1, (corefd_stat.st_size + 511) / 512)) {
		perror("Failed to install MBR");
		exit_code = EXIT_FAILURE;
		goto cleanup_devfd;
	}

	if (-1 == copy_to(devfd, corefd, 512, corefd_stat.st_size)) {
		perror("Failed to install core binary");
		exit_code = EXIT_FAILURE;
		goto cleanup_devfd;
	}

cleanup_devfd:
	if (-1 == close(devfd)) {
		perror("close(device)");
		exit_code = EXIT_FAILURE;
	}
cleanup_corefd:
	if (-1 == close(corefd)) {
		perror("close(core_binary)");
		exit_code = EXIT_FAILURE;
	}
exit:
	return exit_code;
}
