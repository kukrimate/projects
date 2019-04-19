package com.craftstone.stone.event.server;

import com.craftstone.stone.plugin.BasePlugin;

/**
 * An event called when a plugin is reloaded
 * @author kmate
 *
 */
public class PluginReloadEvent extends PluginEventBase {
	public PluginReloadEvent(BasePlugin plugin) {
		super(plugin);
	}
}
