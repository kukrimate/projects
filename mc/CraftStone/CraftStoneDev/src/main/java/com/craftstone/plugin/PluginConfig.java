package com.craftstone.plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;

import com.google.common.collect.Lists;

public class PluginConfig {
	
	private JarFile pluginJar;
	private XMLConfiguration config;
	
	public PluginConfig(File pluginJar) {
		try {
			this.pluginJar = new JarFile(pluginJar);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		JarEntry theConfig = null;
		
		Enumeration<JarEntry> entries = this.pluginJar.entries();
		
		while (entries.hasMoreElements()) {
			JarEntry entry = entries.nextElement();
			if (entry.getName().equals("plugin.xml")) {
				theConfig = entry;
			} else continue;
		}
		
		if (theConfig == null) {
			return;
		}
		
		try {
			InputStream is = this.pluginJar.getInputStream(theConfig);
			config = new XMLConfiguration();
			config.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}		
	}
	
	public String getMainClass() {
		return config.getString("main-class");
	}
	
	public String getPluginName() {
		return config.getString("name");
	}
	
	public String getPluginVersion() {
		return config.getString("version");
	}
	
	public List<RawCommand> getCommands() {
		List<RawCommand> commands = Lists.newArrayList();
		HierarchicalConfiguration commandsConfig = config.configurationAt("commands");
		List<String> it = getCommandNames(commandsConfig.getKeys());
		for (String str : it) {
			//System.out.println(str);
			HierarchicalConfiguration command = commandsConfig.configurationAt(str);
			String aliases = command.getString("aliases");
			String[] strs = aliases.split(";");
			commands.add(new RawCommand(command.getString("name"), strs, command.getString("description"), command.getString("usage"), command.getString("permission"), command.getString("permissionMessage")));
		}
		
		return commands;
	}
	
	private List<String> getCommandNames(Iterator<String> it) {
		List<String> strs = Lists.newArrayList();
		String prevNode = null;
		while (it.hasNext()) {
			String str = it.next().split("\\.")[0];
			if (prevNode == null) {
				prevNode = str;
				continue;
			} else if (prevNode.equals(str)) {
				continue;
			} else if (!prevNode.equals(str)) {
				strs.add(prevNode);
				prevNode = str;
				continue;
			}
		}
		strs.add(prevNode); //Add last node
		
		return strs;
	}
}