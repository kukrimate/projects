# toyshell (c) 2016 Kukri Máté
# Makefile is part of toyshell.
#
# toyshell is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.

# toyshell is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
# 
# You should have received a copy of the GNU General Public License
# along with toyshell.  If not, see <http://www.gnu.org/licenses/>.
include config.mk

TARGET=tsh

SRC = $(shell ls -a *.c)
OBJ = $(addsuffix .o, $(basename $(SRC)))

all: $(OBJ)
	gcc -o $(TARGET) $(OBJ) -lreadline

update_notice:
	find -iregex ".*\.\(c\|h\)" -exec licenseheader --filename {} --template templates/gplv3_c.txt --program-name toyshell --copyright-year 2016 --copyright-holder "Kukri Máté" \;
	find -iregex ".*\(mk\|Makefile\)" -exec licenseheader --filename {} --template templates/gplv3_make.txt --program-name toyshell --copyright-year 2016 --copyright-holder "Kukri Máté" \;

cleanbuild:
	make clean
	make

%.o: %.c
	$(CC) -c $(CFLAGS) $^
	
install: all
	install -D -m 775 tsh $(PREFIX)/bin/tsh

clean:
	rm -f $(TARGET)
	rm -f $(OBJ)
