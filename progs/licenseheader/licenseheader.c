/* licenseheader.c -- Automatic license header checker
 *
 * Copyright (C) 2016 Kukri Máté
 * All rights reserved.
 *
 * This software may be modified and distributed under the terms
 * of the MIT license.  See the LICENSE file for details.
 */

#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>

/* for file basenames */
#include <libgen.h>

#define USAGE "licenseheader --filename <file to check> --template <template filename> --program-name <program name> --copyright-holder <copyright holder> --copyright-year <copyright year> [--description] <description text>"

char *filename = "shell_launch.h";
char *template = NULL;
char *program_name = NULL;
char *description = NULL;
char *copyright_holder = NULL;
char *copyright_year = NULL;

int main(int argc, char *argv[]) {
	/* parse arguments */
	for (int i = 0; i < argc; ++i) {
			if (argv[i+1] == NULL) {
				continue;
			}
			
			if (strcmp("--template", argv[i+1]) == 0) {
				if (argv[i+2] == NULL) { 
					fprintf(stderr, "%s\n", USAGE); 
					exit(EXIT_FAILURE);
				}
				template = argv[i+2];
				++i;
				continue;
			}
			
			if (strcmp("--program-name", argv[i+1]) == 0) {
				if (argv[i+2] == NULL) { 
					fprintf(stderr, "%s\n", USAGE); 
					exit(EXIT_FAILURE);
				}
				program_name = argv[i+2];
				++i;
				continue;
			}
			
			if (strcmp("--copyright-holder", argv[i+1]) == 0) {
				if (argv[i+2] == NULL) { 
					fprintf(stderr, "%s\n", USAGE); 
					exit(EXIT_FAILURE);
				}
				copyright_holder = argv[i+2];
				++i;
				continue;
			}
			
			if (strcmp("--copyright-year", argv[i+1]) == 0) {
				if (argv[i+2] == NULL) { 
					fprintf(stderr, "%s\n", USAGE); 
					exit(EXIT_FAILURE);
				}
				copyright_year = argv[i+2];
				++i;
				continue;
			}
			
			if (strcmp("--filename", argv[i+1]) == 0) {
				if (argv[i+2] == NULL) { 
					fprintf(stderr, "%s\n", USAGE); 
					exit(EXIT_FAILURE);
				}
				filename = argv[i+2];
				++i;
				continue;
			}
			
			if (strcmp("--description", argv[i+1]) == 0) {
				if (argv[i+2] == NULL) { 
					fprintf(stderr, "%s\n", USAGE); 
					exit(EXIT_FAILURE);
				}
				description = argv[i+2];
				++i;
				continue;
			}
	}
	
	if (template == NULL || program_name == NULL || copyright_holder == NULL || copyright_year == NULL || filename == NULL) {
		fprintf(stderr, "%s\n", USAGE); 
		exit(EXIT_FAILURE);
	}
	
	/* Open and read template */
	FILE *fp = fopen(template, "r");
	if (fp == NULL) {
		perror("Failed opening template");
		exit(EXIT_FAILURE);
	}
	
	int alloc_size = 1024;
	char *template_text = malloc(alloc_size);
	bool check_next = false;
	int ch;
	int i = 0;
	while ((ch = fgetc(fp)) != EOF) {
		if (i == alloc_size) {
			alloc_size *=2;
			template_text = realloc(template_text, alloc_size);
		}
		
		if (check_next) {
			check_next = false;
		
			if (ch == 'n') {
				sprintf(template_text+i, program_name);
				i+=strlen(program_name);
				continue;				
			} else if (ch == 'y') {
				sprintf(template_text+i, copyright_year);
				i+=strlen(copyright_year);
				continue;
			} else if (ch == 'h') {
				sprintf(template_text+i, copyright_holder);
				i+=strlen(copyright_holder);
				continue;
			} else if (ch == 'f') {
				sprintf(template_text+i, basename(filename));
				i+=strlen(basename(filename));
				continue;
			} else if (ch == 'd') {
				if (description == NULL) {
					fprintf(stderr, "This template requires file description. Re-run with --description");
					exit(EXIT_FAILURE);
				}
				
				sprintf(template_text+i, description);
				i+=strlen(description);
			}
		}
		
		if (ch == '%') {
			check_next = true;	
			continue; 
		}
		
		template_text[i] = ch;
		++i;
	}
	template_text[i] = '\0';
	
	fclose(fp);
	
	/* check and/or insert header */
	FILE *fp2 = fopen(filename, "r+");
	if (fp2 == NULL) {
		perror("Failed opening file");
		exit(EXIT_FAILURE);
	}
	
	/* read the current content of the file */
	alloc_size = 1024;
	char *text = malloc(alloc_size);
	i = 0;
	while ((ch = fgetc(fp2)) != EOF) {
		if (alloc_size == i) {
			alloc_size *= 2;
			text = realloc(text, alloc_size);
		}
		text[i] = ch;
		++i;
	}
	text[i] = '\0';
	
	/* Seek to the beginning of the file */
	fseek(fp2, 0, SEEK_SET);
	
	/* Write the header */
	if (strstr(text, template_text) == NULL) {
		fwrite(template_text, 1, strlen(template_text), fp2);
	}
	
	/* Write back the content */
	fwrite(text, 1, strlen(text), fp2);
	
	fclose(fp2);
}
