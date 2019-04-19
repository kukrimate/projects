package com.craftstone.plugin;

import java.util.List;
import java.util.Map;

import net.minecraft.server.MinecraftServer;

import com.craftstone.stone.Server;
import com.craftstone.stone.command.CommandExecuter;
import com.craftstone.stone.event.Listener;
import com.craftstone.stone.event.server.PluginDisableEvent;
import com.craftstone.stone.event.server.PluginEnableEvent;
import com.craftstone.stone.event.server.PluginReloadEvent;
import com.craftstone.stone.plugin.BasePlugin;
import com.craftstone.stone.plugin.PluginManager;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class CraftPluginManager implements PluginManager {

	private PluginLoader loader;
	
	public Map<BasePlugin, List<RawCommand>> commandMap;
	public Map<String, RawCommand> commands;
	public Map<RawCommand, List<CommandExecuter>> commandExecMap;
	
	public Map<BasePlugin, List<Listener>> listenerMap;
	
	public CraftPluginManager(Server server) {
		this.listenerMap = Maps.newHashMap();
		
		this.commandMap = Maps.newHashMap();
		this.commandExecMap = Maps.newHashMap();
		this.commands = Maps.newHashMap();
		
		this.loader = new PluginLoader(server);
		for (BasePlugin plugin : loader.getPluginInstances()) {
			List<RawCommand> commands = loader.commandMap.get(plugin);
			this.commandMap.put(plugin, commands);
			for (RawCommand command : commands) {
				List<CommandExecuter> list = Lists.newArrayList();
				list.add(plugin);
				this.commandExecMap.put(command, list);
				this.commands.put(command.getName(), command);
			}
		}
	}
	
	private BasePlugin getCommandOwner(RawCommand command) {
		return (BasePlugin)(this.commandExecMap.get(command).get(0));
	}
	
	public String getCommandOwner(String str) {
		try {
			BasePlugin plugin = null;
			RawCommand command = commands.get(str);
			CommandExecuter exec = commandExecMap.get(command).get(0);
			plugin = (BasePlugin)exec;
			
			if (plugin == null) {
				return null;
			}
			
			return plugin.getName();
		} catch(Exception e) {
			return null;
		}
	}
	
	@Override
	public void registerCommandExecuter(CommandExecuter executer, String command) {
		RawCommand rawCommand = commands.get(command);
		if (rawCommand == null) {
			return;
		}
		
		if (!executer.getPlugin().equals(getCommandOwner(rawCommand))) {
			return;
		}
		
		this.commandExecMap.get(rawCommand).add(executer);
	}
	
	@Override
	public Optional<BasePlugin> getPlugin(String name) {
		for (BasePlugin plugin : loader.getPluginInstances()) {
			if (plugin.getName().equals(name)) {
				return Optional.of(plugin);
			} else continue;
		}
		
		return Optional.absent();
	}
	
	public void enablePlugins() {
		for (BasePlugin plugin : loader.getPluginInstances()) {
			MinecraftServer.getServer().callEvent(new PluginEnableEvent(plugin));
			plugin.onEnable();
		}
	}
	
	public void disablePlugins() {
		for (BasePlugin plugin : loader.getPluginInstances()) {
			MinecraftServer.getServer().callEvent(new PluginDisableEvent(plugin));
			plugin.onDisable();
		}
	}
	
	public void reloadPlugins() {
		for (BasePlugin plugin : loader.getPluginInstances()) {
			MinecraftServer.getServer().callEvent(new PluginReloadEvent(plugin));
			plugin.onReload();
		}
	}

	@Override
	public void registerListener(Listener listener, BasePlugin plugin) {
		if (this.listenerMap.get(plugin) != null) {
			this.listenerMap.get(plugin).add(listener);
		} else { 
			List<Listener> listeners = Lists.newArrayList();
			listeners.add(listener);
			this.listenerMap.put(plugin, listeners);
		}
	}

	public Map<BasePlugin, List<Listener>> getListenerMap() {
		return listenerMap;
	}
}