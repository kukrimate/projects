/*
 * toyshell (c) 2016 Kukri Máté
 * readline_script.h is part of toyshell.
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
#define UP "\033[A"
#define DOWN "\033[B"
#define LEFT "\033[D"
#define RIGHT "\033[C"

char * readline_script(FILE *fp) {
	int ch, i = 0, buff_len = 256;
	char *buffer = malloc(buff_len * sizeof(char));
	bool space_flag = true;
	
	while ((ch = fgetc(fp)) != '\n' && ch != EOF) {
		if (buff_len == i) {
			buff_len *= 2;
			char *newbuff = realloc(buffer, buff_len);
			if (newbuff == NULL) {
				perror("tsh");
				free(buffer);
				exit(EXIT_FAILURE);
			} else {
				buffer = newbuff;
			}
		}
		
		if (ch != ' ') {
			space_flag = false;
		}
		
		buffer[i] = ch;
		++i;
	}
	
	if ((ch == '\n' && i == 0) || (ch == '\n' && space_flag)) { // Return a newline character for the handler
		return (char[1][1]) { { '\n' } };
	}
	
	if (ch == EOF) { // Return NULL if end of file
		return NULL;
	}
	
	// Null terminate the string
	buffer = realloc(buffer, sizeof(char) * i);
	buffer[i] = 0;
	
	return buffer;
}
