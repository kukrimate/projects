package com.craftstone.plugin;

import java.io.File;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import net.minecraft.server.MinecraftServer;

import org.apache.logging.log4j.Logger;

import com.craftstone.stone.Server;
import com.craftstone.stone.plugin.BasePlugin;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class PluginLoader {
	private Logger logger = MinecraftServer.getServer().getLogger();
	private List<File> plugins;
	private List<BasePlugin> pluginInstances = Lists.newArrayList();
	private Server server;
	public Map<BasePlugin, List<RawCommand>> commandMap = Maps.newHashMap();
	
	public PluginLoader(Server server) {
		this.server = server;
		List<File> plugins = collectPlugins().orNull();
		if (plugins == null) {
			logger.info(MinecraftServer.LOG_PREFIX + "No plugins found!");
			return;
		}
		for (File f : plugins) {
			logger.info(MinecraftServer.LOG_PREFIX + "Plugin found: " + f.getName());
		}
		this.plugins = plugins;
		loadPlugins();
	}
	
	public List<BasePlugin> getPluginInstances() {
		return pluginInstances;
	}

	@Nullable
	public Optional<List<File>> collectPlugins() { 
		File pluginsFolder = new File("." + File.separator + "plugins" + File.separator);
		if (!pluginsFolder.isDirectory()) {
			pluginsFolder.mkdirs();
			return Optional.absent();
		}
		
		List<File> plugins = Lists.newArrayList();
		String[] filesInPluginsFolder = pluginsFolder.list();
		
		for (String str : filesInPluginsFolder) {
			File f = new File (pluginsFolder + File.separator + str);
			if (f.isDirectory()) {
				continue;
			} else {
				plugins.add(f);
			}
		}
		
		if (plugins.size() > 0)
			return Optional.of(plugins);
		else
			return Optional.absent();
	}
	
	@SuppressWarnings({ "deprecation", "rawtypes" })
	public void loadPlugins() {
		for (File pluginFile : plugins) {
			URL[] pluginJar = null;
			try {
				PluginConfig config = new PluginConfig(pluginFile);
				pluginJar = new URL[] { pluginFile.toURL() };
				URLClassLoader child = new URLClassLoader(pluginJar, this.getClass().getClassLoader());
				Class classToLoad = Class.forName (config.getMainClass(), true, child);
				
				Object instance = classToLoad.newInstance();
				
				Field name = null;
				Field version = null;
				Field server = null;
				Field dataFolder = null;
				
				try {
					name = classToLoad.getSuperclass().getDeclaredField("name");
					version = classToLoad.getSuperclass().getDeclaredField("version");
					server = classToLoad.getSuperclass().getDeclaredField("server");
					dataFolder = classToLoad.getSuperclass().getDeclaredField("dataFolder");
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				}
				name.setAccessible(true);
				version.setAccessible(true);
				server.setAccessible(true);
				dataFolder.setAccessible(true);
				
				name.set(instance, config.getPluginName());
				version.set(instance, config.getPluginVersion());
				
				server.set(instance, this.server);
				
				File dataFolderFile = new File("." + File.separator + "plugins" + File.separator + config.getPluginName() + File.separator);
				
				if (!dataFolderFile.isDirectory())
					dataFolderFile.mkdirs();
				
				dataFolder.set(instance, dataFolderFile);
				
				name.setAccessible(false);
				version.setAccessible(false);
				server.setAccessible(false);
				dataFolder.setAccessible(false);

				BasePlugin pluginInstance = (BasePlugin)instance;
				pluginInstances.add(pluginInstance);
				
				commandMap.put(pluginInstance, config.getCommands());
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} 
		}
	}
}
