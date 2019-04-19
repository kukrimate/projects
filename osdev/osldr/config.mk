# GNU tools
TGT           := i386-elf-
export CC     := $(TGT)gcc
export LD     := $(TGT)ld

# Netwide Assembler
export NASM   := nasm

# Host tools
export HOSTCC := cc
