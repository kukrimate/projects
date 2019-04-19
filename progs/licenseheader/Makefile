PREFIX=/usr/local

all:
	cc -std=c99 -pedantic -D_XOPEN_SOURCE=700 -o licenseheader licenseheader.c
	
install: all
	install -D -m 775 licenseheader $(PREFIX)/bin/licenseheader
	install -D -m 775 $(shell find ./templates) $(PREFIX)/share/licenseheader/templates/
	
clean:
	rm -f licenseheader
