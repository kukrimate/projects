/*
 * toyshell (c) 2016 Kukri Máté
 * main.c is part of toyshell.
 *
 * toyshell is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * toyshell is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with toyshell.  If not, see <http://www.gnu.org/licenses/>.
 */
#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>
#include <errno.h>

#include <sys/wait.h>
#include <unistd.h>

/* GNU readline */
#include <readline/readline.h>

// Config

// Modules
#include "shell_config.h"
#include "readline_script.h"
#include "splitline.h"
#include "history.h"
#include "shell_launch.h"

// Builtins
#include "builtin/cd.h"

// Prototypes
void run_script(char *filename);
void shell_loop(void);
void execute(char **args);
void setup_readline_bindingns();

int main(int argc, char *argv[]) {
	// Read the config
	readconfig();
	
	if (argc > 1) {
		// Running a script
		run_script(argv[1]);
	} else {
		// Running normally
		read_history();
		setup_readline_bindingns();
		shell_loop();
	}
}

void setup_readline_bindingns() {
	/* keybindings for history */
	rl_bind_keyseq("\033[A", key_up);
	rl_bind_keyseq("\033[B", key_down);
}

void run_script(char *filename) {
	FILE *fp = fopen(filename, "r");
	if (!fp) {
		perror("tsh");
		exit(EXIT_FAILURE);	
	}
	
	char *line;
	while ((line = readline_script(fp)) != NULL) {
		if (line[0] == '\n') { continue; } // Empty line process the next
		if (line[0] == '#') { continue; } // Comment skip it
		
		char **args = splitline(line);
		execute(args);
		
		free(line);
		free(args);
	}
	
	fclose(fp);
}

void shell_loop(void) {
	char *buffer;
	char **args;
	
	for(;;) {
		//printf("$ ");
		buffer = readline("$ ");//readline(stdin);
		
		if (buffer[0] == '\n') { // user pressed enter goto a new line
			continue;
		}
		
		/* here we save the command to the histore */
		++command_history_len;
		if (command_history == NULL) {
			command_history = malloc(sizeof(char*));
		} else {
			command_history = realloc(command_history, sizeof(char*) * command_history_len);
		}
		command_history[command_history_len-1] = strdup(buffer);
		history_pointer = command_history_len-1;
		
		args = splitline(buffer);
		
		execute(args);
		
		free(buffer);
		free(args);
	}
}

void execute(char **args) {
	if (strcmp(args[0], "cd") == 0) {
		builtin_cd(args);
	} else if (strcmp(args[0], "exit") == 0) {
		flush_history();
		exit(EXIT_SUCCESS);
	} else {
		execute_command(args);
	}
}
