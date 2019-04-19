package com.craftstone.stone.plugin;

import java.io.File;
import java.io.IOException;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.io.FileUtils;

import com.craftstone.stone.Server;
import com.craftstone.stone.command.Command;
import com.craftstone.stone.command.CommandSender;

public class JavaPlugin implements BasePlugin {

	private String name;
	private String version;
	private Server server;
	private File dataFolder;
	private XMLConfiguration config;
	
	public JavaPlugin() {}
	
	@Override
	public boolean onCommand(Command command, CommandSender sender, String[] args) { return true; }

	@Override
	public void onEnable() {}

	@Override
	public void onDisable() {}

	@Override
	public void onReload() {}

	@Override
	public File getDataFolder() {
		return dataFolder;
	}

	@Override
	public String getVersion() {
		return version;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Server getServer() {
		return server;
	}
	
	public XMLConfiguration getConfig() {
		if (config == null) {
			File configFile = new File(getDataFolder() + File.separator + "config.xml");
			if (!configFile.isFile())
				saveDefaultConfig();
				
			try {
				config = new XMLConfiguration(configFile);
			} catch (ConfigurationException e) {
				e.printStackTrace();
			}
		}
		return config;
	}
	
	public void saveConfig() {
		try {
			config.save();
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	public void saveDefaultConfig() {
		String defaultConfig = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><config></config>";
		try {
			FileUtils.writeStringToFile(new File(getDataFolder() + File.separator + "config.xml"), defaultConfig);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public BasePlugin getPlugin() {
		return this;
	}

}
