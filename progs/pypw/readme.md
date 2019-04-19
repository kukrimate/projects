Original readme kept for reference. This was my attempt at writing a POC
password manager. It seems to work, but I'm not really sure it is actually
secure, so I wouldn't recommend using it.
=================================================
# pypw
Uber-simple password manager written in Python.
Look at `db_format.txt` for database file spec.
Features:
- Human readable decrypted file format (JSON)
- AES256 encryption
- HMAC-SHA256 authentication
- PBKDF2 key derivation
