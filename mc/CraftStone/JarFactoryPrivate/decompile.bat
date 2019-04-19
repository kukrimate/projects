@echo off
set map=%CD%\mappings
set source=%CD%\source
set tools=%CD%\tools
set jar=%CD%\jar
mkdir temp
mkdir source
java -jar tools\specialsource.jar -i jar\minecraft_server.jar -m mappings/ -o temp\minecraft_server_remapped.jar --kill-source
copy %tools%\7za.exe temp\7za.exe
cd temp
set curDir=%CD%
mkdir extract
cd extract
%curDir%\7za x %curDir%\minecraft_server_remapped.jar
cd %curDir%
set curDir=%CD%\extract
mkdir repack
cd repack
xcopy %curDir%\net net /e /i /h
..\7za a repack.jar *
copy repack.jar ..\repack.jar
cd..
RMDIR extract /S /Q
RMDIR repack /S /Q
del 7za.exe
del minecraft_server_remapped.jar
cd..
copy %tools%\fernflower.jar temp\fernflower.jar
copy %tools%\7za.exe temp\7za.exe
copy %tools%\mcinjector.jar temp\mcinjector.jar
copy %tools%\patch.exe temp\patch.exe
copy mappings\joined.srg temp\injector.srg
copy mappings\exceptor.json temp\exceptor.json
copy patches\server.patch temp\server.patch
cd temp
java -jar mcinjector.jar --jarIn repack.jar --mapIn injector.srg --jarOut repack_ff_in.jar --jsonIn exceptor.json
del repack.jar
java -jar fernflower.jar -din=1 -rbr=0 -dgs=1 -asc=1 -log=WARN repack_ff_in.jar source
del repack_ff_in.jar
del fernflower.jar
del mcinjector.jar
del injector.srg
del exceptor.json
copy source\repack_ff_in.jar repack_source.jar
RMDIR source /S /Q
mkdir sourceTemp
cd sourceTemp
..\7za x ..\repack_source.jar
..\patch -p1 < ..\server.patch
cd..
del 7za.exe
del patch.exe
del server.patch
xcopy sourceTemp\net ..\source\net /e /i /h
RMDIR sourceTemp /S /Q
cd..
RMDIR temp /S /Q
PAUSE