package com.craftstone.exampleplugin;

import com.craftstone.stone.plugin.JavaPlugin;

public class ExamplePlugin extends JavaPlugin {
	public void onEnable() {
		getServer().sendMessage("TESTPLUGIN Enabled!");
		getServer().getPluginManager().registerCommandExecuter(new StrikeCommandExecuter(this), "strike");
		getServer().getPluginManager().registerListener(new TestPluginListener(this), this);
	}
	
	public void onDisable() {
		getServer().sendMessage("TESTPLUGIN Disabled!");
	}
	
	public void onReload() {
		getServer().sendMessage("TESTPLUGIN Reloaded!");
	}
}
