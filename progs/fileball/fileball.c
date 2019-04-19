#include <stdio.h>
#include <string.h>
#include <stdlib.h>

#include <sys/sendfile.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>

// Usage message
#define USAGE	"Usage: fileball [flags] ...\n" \
		"Flags:\n" \
		"\tv: Verbose mode\n" \
		"\tu: Unpack mode\n" \
		"\t\tfilball u <archives>...\n" \
		"\tp: Pack mode\n" \
		"\t\tfileball p <dest> <files>...\n"


// Flags
int fverbose = 0, funpack = 0, fpack = 0;

/* Entrty format used by fileball
filename length: 32-bit unsigned (length of filename incl. nul)
filename: nul-terminated string of characters
uid: 32-bit unsigned
gid: 32-bit unsigned
filemode: 32-bit unsigned
filesize: 64-bit unsigned
file contents
*/

// Archive a file
int archive_file(int archivefd, int filefd, char *filename)
{
	struct stat filestat;
	if (fstat(filefd, &filestat) == -1)
		goto err;

	unsigned buffer4;
	unsigned long long buffer8;
	// Filename
	buffer4 = strlen(filename) + 1;
	if (write(archivefd, &buffer4, 4) == -1)
		goto err;
	if (write(archivefd, filename, strlen(filename) + 1) == -1)
		goto err;
	
	// Metadata
	buffer4 = filestat.st_uid;
	if (write(archivefd, &buffer4, 4) == -1)
		goto err;
	buffer4 = filestat.st_gid;
	if (write(archivefd, &buffer4, 4) == -1)
		goto err;
	buffer4 = filestat.st_mode;
	if (write(archivefd, &buffer4, 4) == -1)
		goto err;
	
	// File contents
	buffer8 = filestat.st_size;
	if (write(archivefd, &buffer8, 8) == -1)
		goto err;
	if (sendfile(archivefd, filefd, NULL, filestat.st_size) == -1)
		goto err;

	return 0;
err:
	return -1;
}

void print_usage()
{
	fprintf(stderr, "%s", USAGE);
	fflush(stderr);
	exit(EXIT_FAILURE);
}

void pack(int argc, char **argv)
{
	if (argc < 2)
		print_usage();
	
	char *destfile = *argv;
	
	if (fverbose)
		printf("Destination file: %s\n", destfile);
	int destfd = open(destfile, O_CREAT | O_TRUNC | O_WRONLY, 0644);
	if (destfd == -1) {
		perror("Error opening destination");
		close(destfd);
		exit(EXIT_FAILURE);
	}

	// Write magic number
	if (write(destfd, "FILEBALL", 8) == -1) {
		perror("Error writing magic number");
		close(destfd);
		exit(EXIT_FAILURE);
	}

	--argc;
	while (argc) {
		if (fverbose) {
			printf("Adding file: %s\n", *++argv);
		}
		int filefd = open(*argv, O_RDONLY);
		if (filefd == -1) {
			perror("Error opening file");
			close(destfd);
			exit(EXIT_FAILURE);
		}
		if (archive_file(destfd, filefd, *argv) == -1) {
			perror("Error archiving file");
			close(filefd);
			close(destfd);
			exit(EXIT_FAILURE);
		}

		close(filefd);
		--argc;
	}

	close(destfd);
	exit(EXIT_SUCCESS);

}

int main(int argc, char **argv)
{
	// Remove program name from the argument
	--argc;
	++argv;

	// Print usage
	if (argc < 2) {
		print_usage();
	}

	// Parse flags
	for (; **argv; ++*argv) {
	switch (**argv) {
	case 'v':
		fverbose = 1;
		break;
	case 'p':
		fpack = 1;
		break;
	case 'u':
		funpack = 1;
		break;	
	default:
		fprintf(stderr, "Unknown flag %c", **argv);
		exit(EXIT_FAILURE);
	}
	}

	// Check modes
	if (!(fpack ^ funpack)) {
		fprintf(stderr, "One and only one mode must be set");
		exit(EXIT_FAILURE);
	}

	// Print header message in verbose mode	
	if (fverbose) {
		printf("Running in verbose mode\n");
		printf("fileball: file packaging utility\nCopyright (c) Mate Kukri 2017\n");
	}

	if (fpack)
		pack(--argc, ++argv);
	else
		goto unpack;
unpack:
	--argc;
	if (argc < 1) {
		print_usage();
	}

	while (argc) {
		++argv;
		if (fverbose) {
			printf("Found archive %s\n", *argv);
		}

		int archivefd = open(*argv, O_RDONLY);
		if (archivefd == -1) {
			perror("Error opening archive");
			exit(EXIT_FAILURE);
		}

		// Check the magic number
		char magic[8];
		if (read(archivefd, magic, 8) != 8) {
			perror("Error reading magic number");
			close(archivefd);
			exit(EXIT_FAILURE);
		}

		if (strncmp(magic, "FILEBALL", 8) != 0) {
			fprintf(stderr, "Invalid magic number\n");
			close(archivefd);
			exit(EXIT_FAILURE);
		}

		int errval;
		unsigned filename_length;
		while ((errval = read(archivefd, &filename_length, 4)) == 4) {
			char *filename = malloc(filename_length);
			if (filename == NULL) {
				abort();
			
			}

			if (errval = read(archivefd, filename, filename_length) != filename_length) {
				printf("Errval: %d\n", errval);
				perror("Error reading filename");
				close(archivefd);
				exit(EXIT_FAILURE);
			}
			if (fverbose) {
				printf("Unpacking file %s\n", filename);
			}

			// Read file metadata
			unsigned uid, gid, filemode;
			if (read(archivefd, &uid, 4) != 4) {
				perror("Error reading file metadata");
				close(archivefd);
				exit(EXIT_FAILURE);
			}
			if (read(archivefd, &gid, 4) != 4) {
				perror("Error reading file metadata");
				close(archivefd);
				exit(EXIT_FAILURE);
			}
			if (read(archivefd, &filemode, 4) != 4) {
				perror("Error reading file metadata");
				close(archivefd);
				exit(EXIT_FAILURE);
			}
			
			// File size
			long long filesize;
			if (read(archivefd, &filesize, 8) != 8) {
				perror("Error reading filesize");
				close(archivefd);
				exit(EXIT_FAILURE);
			}
				
			// Create file
			mode_t oldmask = umask(0);
			int filefd = open(filename, O_CREAT | O_WRONLY | O_TRUNC, filemode);
			umask(oldmask);
			if (filefd == -1) {
				perror("Error opening file");
				close(archivefd);
				exit(EXIT_FAILURE);
			}

			if (sendfile(filefd, archivefd, NULL, filesize) != filesize) {
				perror("Error writing file data");
				close(filefd);
				close(archivefd);
				exit(EXIT_FAILURE);
			}

			close(filefd);
			free(filename);
		}
		if (errval != 0) {
			perror("Error reading filename length");
			close(archivefd);
			exit(EXIT_FAILURE);
		}
		
		close(archivefd);
		--argc;
	}

	exit(EXIT_SUCCESS);

}
