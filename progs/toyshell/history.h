/*
 * toyshell (c) 2016 Kukri Máté
 * history.h is part of toyshell.
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
char **command_history = NULL;
size_t command_history_len = 0;

int history_pointer = 0; /* 0 = oldest */

void flush_history() {
	/* flush command history to file */
	char filename[256];
	sprintf(filename, "%s/%s", getenv("HOME"), ".tsh_history");
	FILE *fp = fopen(filename, "w+");
	if (fp == NULL) {
		perror("tsh: Error opening command history");
		exit(EXIT_FAILURE);
	}
		
	for (int i = 0; i < command_history_len; ++i) {
		fprintf(fp, "%s\n", command_history[i]);
	}
		
	fclose(fp);
}

void read_history() {
	char filename[256];
	sprintf(filename, "%s/%s", getenv("HOME"), ".tsh_history");
	FILE *fp = fopen(filename, "r");
	if (fp == NULL) {
		perror("tsh: Error opening commandhistory");
		return;
	}
	
	char *buffer;
	while ((buffer = readline_script(fp)) != NULL) {
		if (buffer[0] == '\n') { /* we don't need this line */
			continue;
		}
		++command_history_len;
		if (command_history == NULL) {
			command_history = malloc(sizeof(char*));
		} else {
			command_history = realloc(command_history, sizeof(char*) * command_history_len);
		}
		
		command_history[command_history_len-1] = strdup(buffer);
		
		free(buffer);
	}
	
	history_pointer = command_history_len - 1;
}

/* handlers for arrow key presses */
int key_up() {
	if (command_history == NULL) {
		return 0;
	}
	
	if (history_pointer == 0) { /* We are at the oldest command */
		rl_replace_line("",0);
		rl_insert_text(command_history[history_pointer]);
		return 0;
	}
	--history_pointer;
	rl_replace_line("",0);
	rl_insert_text(command_history[history_pointer]);
}

int key_down() {
	if (command_history == NULL) {
		return 0;
	}

	if (history_pointer == command_history_len - 1) { /* We are already at the newest command */
		rl_replace_line("",0);
		rl_insert_text(command_history[history_pointer]);
		return 0;
	}
	++history_pointer;
	rl_replace_line("",0);
	rl_insert_text(command_history[history_pointer]);
}
