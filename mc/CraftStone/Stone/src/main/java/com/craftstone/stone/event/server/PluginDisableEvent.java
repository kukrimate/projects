package com.craftstone.stone.event.server;

import com.craftstone.stone.plugin.BasePlugin;

/**
 * An event called when a plugin is disabled
 * @author kmate
 *
 */
public class PluginDisableEvent extends PluginEventBase {
	public PluginDisableEvent(BasePlugin plugin) {
		super(plugin);
	}
}