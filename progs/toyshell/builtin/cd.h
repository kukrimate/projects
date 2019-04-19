/*
 * toyshell (c) 2016 Kukri Máté
 * cd.h is part of toyshell.
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
void builtin_cd(char **args) {
	char *path;
	
	if (args[1]) {
		path = args[1];
	} else {
		if ((path = getenv("HOME")) == NULL) {
			fprintf(stderr, "cd: Please specify a directory or set $HOME.\n");
			return;
		}
	}
	
	if (chdir(path) != 0) {
		perror("cd");
	}
}
