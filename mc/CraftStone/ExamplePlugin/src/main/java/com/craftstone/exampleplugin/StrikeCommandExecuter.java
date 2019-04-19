package com.craftstone.exampleplugin;

import com.craftstone.stone.command.Command;
import com.craftstone.stone.command.CommandExecuter;
import com.craftstone.stone.command.CommandSender;
import com.craftstone.stone.entity.Player;
import com.craftstone.stone.plugin.BasePlugin;
import com.craftstone.stone.world.World;

public class StrikeCommandExecuter implements CommandExecuter {
	private BasePlugin plugin;
	
	public StrikeCommandExecuter(BasePlugin plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public BasePlugin getPlugin() {
		return this.plugin;
	}

	@Override
	public boolean onCommand(Command arg0, CommandSender arg1, String[] arg2) {
		if (!(arg1 instanceof Player)) {
			arg1.sendMessage("You are'nt a player!");
			return true;
		}
		
		Player player = (Player)arg1;
		World world = player.getWorldOfEntity();
		world.strikeLightning((int)player.getPosition().getX(), (int)player.getPosition().getY(), (int)player.getPosition().getZ());
		arg1.sendMessage("Test Lightning");
		return true;
	}
}
