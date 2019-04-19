package com.craftstone.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldSettings.GameType;
import net.minecraft.world.WorldType;
import net.minecraft.world.WorldServer;

import com.craftstone.stone.world.Enviroment;
import com.craftstone.world.WorldManager;

//World Managing
public class CommandWorld extends CommandBase {

	public CommandWorld() {
		super("stone.world", "You don't have permission to use this", "This command is for managing worlds!");
	}
	
	@Override
	public String getCommandName() {
		return "world";
	}

	@Override
	public String getCommandUsage(ICommandSender var1) {
		return "/world <create|remove>";
	}

	@Override
	public void processCommand(ICommandSender var1, String[] var2) throws CommandException {
		WorldManager manager = MinecraftServer.getServer().getWorldManager();
		if (manager == null) {
			return;
		}
		
		if (var2.length < 1) {
			throw new WrongUsageException(getCommandUsage(var1));
		}
		
		String commandLabel = var2[0];
		
		if (commandLabel.equalsIgnoreCase("create")) {
			if (var2.length < 3) {
				throw new WrongUsageException("/world create <name> <NORMAL|NETHER|END>");
			}
			
			if (!manager.worldByName(var2[1]).isPresent()) {
				manager.addNewWorld(var2[1], WorldType.DEFAULT, Enviroment.OVERWORLD, manager.getDefaultWorldSeed(), GameType.SURVIVAL);
				var1.sendMessage("World '" + var2[1] + "' created successfully!");
			} else {
				var1.sendMessage("World '" + var2[1] + "' already exists!");
			}
		}
		
		if (commandLabel.equalsIgnoreCase("remove")) {
			if (var2.length < 2) {
				throw new WrongUsageException("/world remove <name> ");
			}
			
			if (manager.worldByName(var2[1]).isPresent()) {
				manager.unloadWorld(var2[1]);
				var1.sendMessage("World '" + var2[1] + "' removed successfully!");
			} else {
				var1.sendMessage("World '" + var2[1] + "' not exists!");
			}
		}
		
		if (commandLabel.equalsIgnoreCase("tp")) {
			if (var2.length < 2) {
				throw new WrongUsageException("/world tp <name> ");
			} else if (!(var1 instanceof EntityPlayerMP)) { 
				throw new WrongUsageException("This command can't sent from console!");
			}
			
			if (manager.worldByName(var2[1]).isPresent()) {
				System.out.println(manager.worldByName(var2[1]).get().getWorldName());
				EntityPlayerMP player = (EntityPlayerMP)var1;
				WorldServer newWorld = manager.worldByName(var2[1]).get();
				MinecraftServer.getServer().getTeleporter().takePlayerToWorld(player, newWorld, false);
				var1.sendMessage("Teleport successfull!");
			} else {
				var1.sendMessage("World '" + var2[1] + "' not exists can't teleport!");
			}
		}
	}
	
	@Override
	   public boolean isVanillaCommand() { //CraftStone
		   return false;
	   }

}
