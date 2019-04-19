# toyshell (c) 2016 Kukri Máté
# config.mk is part of toyshell.
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
PREFIX=/usr/local

CC=cc

CFLAGS=-std=c99 -pedantic -D_XOPEN_SOURCE=700
