/*
 * toyshell (c) 2016 Kukri Máté
 * splitline.h is part of toyshell.
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
char **splitline(char *str) {
	char **buffer = malloc(sizeof(char*));
	int counter = 0;
	char *pool = calloc(sizeof(char), 256);

	char ch;
	bool last_was_space = true;
	bool watch_out_for_multipart = false;
	bool print = true;
	for (int i = 0; i < strlen(str); ++i) {
		ch = str[i];
		
		if (ch == ' ') {
			if (str[i-1] == '"' && str[i-2] != '\\') {
				watch_out_for_multipart = false;
			}
		
			if (watch_out_for_multipart) {
				sprintf(pool + strlen(pool), "%c", ch);
				continue;
			}
			
			if (str[i-1] == '\\' && str[i-2] != '\\') { /* support multipart args with \ */
				sprintf(pool + strlen(pool), "%c", ch);
				continue;
			}
			
			if (!last_was_space) { /* new argument */	
				/* save argument */
				pool = realloc(pool, strlen(pool) + 1); /* resize pool to match the size of string */
				pool[strlen(pool)] = 0; /* null terminate the string */
				buffer[counter] = pool;
				buffer = realloc(buffer, (++counter + 1) * sizeof(char*));
						
				/* clear pool */
				pool = calloc(sizeof(char), 256);
			}
			
			last_was_space = true;
			continue;
		}
		
		if (last_was_space) {
			if (ch != ' ') {
				last_was_space = false;
			}
			
			if (ch == '"' && str[i-1] != '\\') { /* start of multipart argument */
				watch_out_for_multipart = true;
			}
		}
		
		if ((ch == '\\' && str[i-1] != '\\') || (ch == '"' && str[i-1] != '\\')) {
			print = false;
		}
		
		if (print) {
			sprintf(pool + strlen(pool), "%c", ch);
		} 
		
		print = true; 
	}
	
	/* save our last arg */
	pool = realloc(pool, strlen(pool) + 1); /* resize pool to match the size of string */
	pool[strlen(pool)] = 0; /* null terminate the string */
	buffer[counter] = pool;
	/* null terminate the array */
	buffer = realloc(buffer, (++counter + 1) * sizeof(char*));
	buffer[counter] = NULL;
	
	return buffer;
}
