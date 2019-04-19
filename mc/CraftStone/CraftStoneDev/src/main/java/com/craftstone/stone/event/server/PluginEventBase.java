package com.craftstone.stone.event.server;

import com.craftstone.stone.plugin.BasePlugin;

/**
 * Holds any plugin based events
 * @author kmate
 *
 */
public class PluginEventBase {
	private BasePlugin plugin;
	
	public PluginEventBase(BasePlugin plugin) {
		this.plugin = plugin;
	}

	/**
	 * Returns the plugin affected by this event.
	 * @return
	 */
	public BasePlugin getPlugin() {
		return plugin;
	}
}	