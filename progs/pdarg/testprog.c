#include <stdio.h>
#include <stdbool.h>
#include "pdarg.h"

void exit_callback(void)
{
	printf("%s\n", "PDARG EXIT CALLBACK");
}

int main(int argc, char **argv)
{
	pdarg_init(exit_callback, "This is a simple test program for pdarg.");
	pdarg_add_arg((char*[]){ "--input", "-i", NULL }, "The input file to read.", true);
	pdarg_add_flag((char*[]){ "--silent", "-s", NULL }, "Run silently.");
	pdarg_parse(argc, argv);

	if (pdarg_is_flag_present("--silent")) {
		printf("%s\n", "Running silently!");
	}

	char *input_file = pdarg_get_arg_value("--input");
	if (input_file != NULL) {
		printf("Input file: %s\n", input_file);
	}

	pdarg_fini();
	return 0;
}
