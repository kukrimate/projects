.PHONY: all
all:
	make -C coreldr/
	make -C installer/

test_hdd.img:
	truncate -s 200M $@

.PHONY: run
run: all test_hdd.img
	installer/install_ldr test_hdd.img coreldr/coreldr.bin
	qemu-system-x86_64 -hda test_hdd.img -serial stdio

.PHONY: clean
clean:
	make -C coreldr/ clean
	make -C installer/ clean
