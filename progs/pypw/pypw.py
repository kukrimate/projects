#!/usr/bin/python3

# Crypto modules
from Crypto.Cipher import AES
from Crypto.Hash import HMAC
from Crypto.Hash import SHA256
from Crypto.Protocol import KDF

import argparse
import os
import json

parser = argparse.ArgumentParser(description='Python password manager')
parser.add_argument('-d', metavar='database', required=True, help='Password database file')
parser.add_argument('-c', action='store_true', help='Create a new database')
args = parser.parse_args()

def derive_encryption_key(password, salt):
	# Derive a 32-byte key for AES256
	# Use HMAC-SHA256 as the pseudorandom function
	return KDF.PBKDF2(password, salt, dkLen=32, count=1000, prf=lambda p,s: HMAC.new(p,s,SHA256).digest())

def get_random_bytes(count):
	# Use the OS for random generation
	return os.urandom(count)

def compute_hmac_sha256(key, data):
	return HMAC.new(key, data, digestmod=SHA256).digest()

def pad_to_block(data, block_size):
	arr = bytearray(data)
	pad_bytes = block_size - len(arr) % block_size
	for i in range(pad_bytes):
		arr.append(0)
	return bytes(arr)

class PasswordDatabase:
	# Just create an uninitialized database object
	def __init__(self):
		self.initialized = False
		self.unlocked = False

	def create_new(self, password):
		if self.initialized:
			raise Exception('This database is already initialized')

		# Generate a new random salt and derive the encryption key from the password
		self.salt = get_random_bytes(16)
		self.key = derive_encryption_key(password, self.salt)
		# Generate the AES IV
		self.iv = get_random_bytes(16)

		# A new database is obviously empty
		self.data = {}

		# New databases are both initialized and unlocked by default
		self.initialized = True
		self.unlocked = True

		return self

	def read_from_file(self, path):
		if self.initialized:
			raise Exception('This database is already initialized')

		with open(path, 'rb') as f:
			magic = f.read(4)
			if magic.decode('ascii') != 'pypw':
				raise('Invalid password database')
			self.salt = f.read(16)
			self.iv = f.read(16)
			self.hmac = f.read(32)
			self.encrypted_data = f.read()
		self.initialized = True

		return self

	def unlock(self, password):
		if not self.initialized:
			raise Exception('Can not unlock an uninitialized database')
		if self.unlocked:
			return	

		# Compute the HMAC
		hmac_buffer = bytearray()
		hmac_buffer.extend('pypw'.encode('ascii'))
		hmac_buffer.extend(self.salt)
		hmac_buffer.extend(self.iv)
		hmac_buffer.extend(bytearray(32))
		hmac_buffer.extend(self.encrypted_data)

		# Derive the decryption key
		self.key = derive_encryption_key(password, self.salt)

		computed_hmac = HMAC.new(self.key, hmac_buffer, digestmod=SHA256).digest()
		if computed_hmac != self.hmac:
			raise Exception('Failed to authenticate the database')
		# Decrypt and parse the data
		raw_data = AES.new(self.key, AES.MODE_CBC, self.iv).decrypt(self.encrypted_data)
		self.data = json.loads(raw_data.decode('utf-8').rstrip('\0'))

		self.unlocked = True

	def write_to_file(self, path):
		if not self.unlocked:
			raise Exception('Can only write an unlocked database to file')
		with open(path, 'wb') as f:
			# Encrypt the data
			raw_data = pad_to_block(json.dumps(self.data).encode('utf-8'), 16)
			encrypted_data = AES.new(self.key, AES.MODE_CBC, self.iv).encrypt(raw_data)

			f.write('pypw'.encode('ascii'))
			f.write(self.salt)
			f.write(self.iv)

			# Compute the HMAC
			hmac_buffer = bytearray()
			hmac_buffer.extend('pypw'.encode('ascii'))
			hmac_buffer.extend(self.salt)
			hmac_buffer.extend(self.iv)
			hmac_buffer.extend(bytearray(32))
			hmac_buffer.extend(encrypted_data)

			# Write the HMAC to file
			f.write(HMAC.new(self.key, hmac_buffer, digestmod=SHA256).digest())

			# Write the encrypted data
			f.write(encrypted_data)

	def get_entry(self, entry):
		if not self.unlocked:
			raise Exception('The database must be unlocked to do this')
		if entry not in self.data:
			raise Exception('Requested entry does not exist')
		return self.data[entry]

	def set_entry(self, entry, data):
		if not self.unlocked:
			raise Exception('The database must be unlocked to do this')
		self.data[entry] = data

if args.c:
	print('Creating new database')
	passwd_str = input('New password: ')
	db = PasswordDatabase().create_new(passwd_str)
	db.write_to_file('test.pwdb')
else:
	print('Using existing database')
	db = PasswordDatabase().read_from_file('test.pwdb')
	passwd_str = input('Type password to unlock database: ')
	db.unlock(passwd_str)
	try:
		print(db.get_entry('google.com'))
	except:
		db.set_entry('google.com', {'username': 'mymail@gmail.com', 'password': 'very secure'})
		db.write_to_file('test.pwdb')
