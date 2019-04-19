PREFIX=/usr/local

.PHONY: all
all: ;

.PHONY: install
install: mkpkg mkpkg.conf
	install -D -g root -o root -m 755 mkpkg $(PREFIX)/bin/mkpkg
	install -D -g root -o root -m 755 mkpkg.conf $(PREFIX)/etc/mkpkg.conf
