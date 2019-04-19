[![Build Status](https://travis-ci.org/CraftStone/CraftStone.svg)](https://travis-ci.org/CraftStone/CraftStone)

CraftStone
==========
Minecraft server implementation for the Stone API. 

Features:
- Plugin Loading: Done
- Basic API Features: Partial
- Command System: Done
- Event System: Partial, Currently Worked On Status: [Status](https://docs.google.com/document/d/1xlF2Sxv7Hp25piTU3E9xjhuWiOCAnnE_tWe2Ikvid-c/edit?usp=sharing)
- Scheduling: Not worked on yet

Distribution
============
Provided in form off diff patches to avoid copyrighted mojang code.

Compiling
=========
- If you are a linux user make sure you have theese two apt packages installed: dos2unix, p7zip-full
- First you will need mcp mappings that you can download from here: [Download](http://www.mediafire.com/download/56xoalz89957n7o/mcp910-pre1.zip)
- Extract the conf folder contents to mappings
- Than run `prepare.bat` and your finally built gradle project will be in the `CraftStone` folder
- Then go into that folder than run `gradle`
- And you will get the final jar in the `build/libs` folder

Licensing
=========
Everything is licensed under MIT inside this repository except for the tools in the tools folder
