/*
 * toyshell (c) 2016 Kukri Máté
 * shell_launch.h is part of toyshell.
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
int execute_command(char **args) {
	pid_t pid, wpid;
	int status;

	pid = fork();
	if (pid == 0) {
		// Child process
    	if (execvp(args[0], args) == -1) {
      		fprintf(stderr, "tsh: %s: %s\n", args[0], strerror(errno));
    	}
    	exit(EXIT_FAILURE);
  	} else if (pid < 0) {
    	// Error forking
    	perror("tsh");
  	} else {
    	// Parent process
    	do {
    	  wpid = waitpid(pid, &status, WUNTRACED);
    	} while (!WIFEXITED(status) && !WIFSIGNALED(status));
  	}

  	return 1;
}
