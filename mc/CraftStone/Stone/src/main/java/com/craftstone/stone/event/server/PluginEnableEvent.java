package com.craftstone.stone.event.server;

import com.craftstone.stone.plugin.BasePlugin;

/**
 * An event called when a plugin is enabled
 * @author kmate
 *
 */
public class PluginEnableEvent extends PluginEventBase {
	public PluginEnableEvent(BasePlugin plugin) {
		super(plugin);
	}
}
