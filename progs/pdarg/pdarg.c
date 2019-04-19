#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include "pdarg.h"

/* internal structures */
struct pdarg {
	char **aliases;
	char *description;
	bool required;
	char *storage;
};

struct pdflag {
	char **aliases;
	char *description;
	bool present;
};

/* internal variables */
pdarg_exit_callback exit_func;
int sizeof_flags = 0, sizeof_args = 0;
struct pdflag *flags;
struct pdarg *args;

int free_counter = 0;
void **free_list;

static void add_free_pointer(void *pointer)
{
	free_list = realloc(free_list, ++free_counter * sizeof(void*));
	free_list[free_counter - 1] = pointer;
}

void pdarg_init(pdarg_exit_callback exit_callback, char *program_description)
{
	flags = malloc(sizeof(struct pdflag));
	args = malloc(sizeof(struct pdarg));
	exit_func = exit_callback;
	free_list = malloc(sizeof(void*));
	add_free_pointer(flags);
	add_free_pointer(args);
}

void pdarg_add_arg(char **aliases, char *description, bool required)
{
	args = realloc(args, ++sizeof_args * sizeof(struct pdarg));
	args[sizeof_args - 1].aliases = aliases;
	args[sizeof_args - 1].description = description;
	args[sizeof_args - 1].required = required;
	args[sizeof_args - 1].storage = NULL;
}

void pdarg_add_flag(char **aliases, char *description)
{
	flags = realloc(flags, ++sizeof_flags * sizeof(struct pdflag));
	flags[sizeof_flags - 1].aliases = aliases;
	flags[sizeof_flags - 1].description = description;
	flags[sizeof_flags - 1].present = false;
}

void pdarg_parse(int argc, char **argv)
{
	bool arg_value = false;
	struct pdarg *arg_pointer;

	for (int i = 0; i < argc; ++i) {
		char *current = argv[i];

		if (arg_value) {
			(*arg_pointer).storage = current;
			arg_value = false;
			continue;
		}

		for (int j = 0; j < sizeof_args; ++j) {
			struct pdarg *current_arg = args + j;
			char **aliases = current_arg->aliases;
			while(*aliases) {
				if (strcmp(current, *aliases) == 0) {
					arg_value = true;
					arg_pointer = current_arg;
				}
				++aliases;
			}
		}

		for (int j = 0; j < sizeof_flags; ++j) {
			struct pdflag *current_flag = flags + j;
			char **aliases = current_flag->aliases;
			while(*aliases) {
				if (strcmp(current, *aliases) == 0) {
					current_flag->present = true;
				}
				++aliases;
			}
		}
	}

	bool is_exit = false;
	for (int i = 0; i < sizeof_args; ++i) {
		struct pdarg current_arg = args[i];
		if (current_arg.required && current_arg.storage == NULL) {
			printf("Required argument %s is not present!\n", current_arg.aliases[0]);
			is_exit = true;
		}
	}

	if (is_exit) {
		exit_func();
		exit(EXIT_FAILURE);
	}
}

bool pdarg_is_flag_present(char *flag_first_alias)
{
	for (int i = 0; i < sizeof_flags; ++i) {
		if ((strcmp(flag_first_alias, flags[i].aliases[0]) == 0) && flags[i].present) {
			return true;
		}
	}

	return false;
}

char *pdarg_get_arg_value(char *arg_first_alias)
{
	for (int i = 0; i < sizeof_args; ++i) {
		if (strcmp(arg_first_alias, args[i].aliases[0]) == 0) {
			return args[i].storage;
		}
	}
}

char *pdarg_generate_help(void)
{

}

char *pdarg_generate_usage(void)
{

}

void pdarg_fini(void)
{
	for (int i = 0; i < free_counter; ++i) {
		free(free_list[i]);
	}
}
