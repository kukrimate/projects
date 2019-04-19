package com.craftstone.exampleplugin;

import com.craftstone.stone.event.EventHandler;
import com.craftstone.stone.event.Listener;
import com.craftstone.stone.event.weather.LightningEvent;
import com.craftstone.stone.plugin.BasePlugin;

public class TestPluginListener implements Listener {
	BasePlugin plugin;
	
	public TestPluginListener(BasePlugin plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onLightning(LightningEvent event) {
		plugin.getServer().sendMessage("Lightning at: " + event.getPosition().getX() + " " + event.getPosition().getY() + " " + event.getPosition().getZ());
	}
}
