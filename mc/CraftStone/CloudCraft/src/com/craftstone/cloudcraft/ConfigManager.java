package com.craftstone.cloudcraft;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class ConfigManager {
	private Properties config;
	private File configFile;
	
	public ConfigManager(File configFile) {
		this.configFile = configFile;
		if (!configFile.exists()) {
			createConfig();
		}

		InputStream stream = null;
		
		try {
			stream = new FileInputStream(configFile);
		} catch (FileNotFoundException e) {
			createConfig();
		}
		
		this.config = new Properties();
		try {
			this.config.load(stream);
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void createConfig() {
		try {
			configFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getString(String name, String defaultValue) {
		String str = config.getProperty(name, defaultValue);
		saveConfig();
		return str;
	}
	
	public void setString(String name, String value) {
		config.setProperty(name, value);
		saveConfig();
	}
	
	public void saveConfig() {
		OutputStream fs = null;
		try {
			fs = new FileOutputStream(configFile);
			this.config.store(fs, "");
		} catch (IOException e) {
			createConfig();
			saveConfig();
		}
	}
}
