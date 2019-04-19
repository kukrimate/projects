package com.craftstone;

import java.io.File;
import java.io.IOException;

import net.minecraft.server.MinecraftServer;

import org.apache.commons.io.FileUtils;

public class MotdFile {
	
	File path;
	String defaultMotd;
	
	public MotdFile(File path, String defaultMotd) {
		this.path = path;
		this.defaultMotd = defaultMotd;
	}
	
	public String readMotd() {
		String str = null;
		try {
			str = FileUtils.readFileToString(path);
			if (str == null || str.length() < 1) {
				makeNewMotdFile();
				str = FileUtils.readFileToString(path);
			}
		} catch (IOException e) {
			MinecraftServer.getServer().getLogger().info("Error reading motd from file generating new!");
			makeNewMotdFile();
			try {
				str = FileUtils.readFileToString(path);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
		return str;
	}
	
	private void makeNewMotdFile() {
		if (this.path.isFile()) {
			this.path.delete();
		}
		try {
			this.path.createNewFile();
			FileUtils.writeStringToFile(path, this.defaultMotd);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
