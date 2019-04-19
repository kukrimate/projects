package com.craftstone.world;

import java.util.Iterator;

import com.craftstone.stone.world.Enviroment;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.WorldServer;

public class Teleporter {
	private WorldManager worldManager;
	private MinecraftServer server;
	
	public Teleporter(WorldManager worldManager) {
		this.worldManager = worldManager;
		this.server = MinecraftServer.getServer();
	}
	
	public void transferEntityToWorld(Entity entity, WorldServer newWorld, boolean createPortal) 
	{
		WorldServer oldWorld = entity.worldObj.asWorldServer();
		
		
		BlockPos newSpawn = newWorld.getSpawnPoint();
		double entityX = newSpawn.getX();
		double entityZ = newSpawn.getY();
		double entityY = newSpawn.getZ();
		float entityYaw = entity.rotationYaw;
		float entityPitch = entity.rotationPitch;
		
	    if(entity.isEntityAlive()) 
	    {
	    	oldWorld.theProfiler.startSection("moving");
			    entity.setLocationAndAngles(entityX, entityY, entityZ, entityYaw, entityPitch);
			    newWorld.spawnEntityInWorld(entity);
			    oldWorld.updateEntityWithOptionalForce(entity, false);
	    	oldWorld.theProfiler.endSection();
	    }
		
	    if (createPortal) {
			 oldWorld.theProfiler.startSection("placing");
		         if(entity.isEntityAlive()) {
		        	 newWorld.getDefaultTeleporter().createPortal(entity, entityYaw);
		         }
	         oldWorld.theProfiler.endSection();
	    }
	    
		/*if (!newWorld.getEnv().equals(Enviroment.END)) {
			oldWorld.theProfiler.startSection("placing");
		         entityX = (double)MathHelper.clamp_int((int)entityX, -29999872, 29999872);
		         entityZ = (double)MathHelper.clamp_int((int)entityZ, -29999872, 29999872);
		         if(entity.isEntityAlive()) {
		        	entity.setLocationAndAngles(entityX, entityY, entityZ, entityYaw, entityPitch);
		        	newWorld.getDefaultTeleporter().createPortal(entity, entityYaw);
		            newWorld.spawnEntityInWorld(entity);
		            newWorld.updateEntityWithOptionalForce(entity, false);
		         }

		    oldWorld.theProfiler.endSection();
		}*/
		
		entity.setWorld(newWorld);
	}
	
	public void takePlayerToWorld(EntityPlayerMP player, WorldServer world, boolean createPortal) {
		WorldServer currentWorld = player.worldObj.asWorldServer();
		
		if (world.equals(currentWorld)) { return; }
		
		player.setWorld(world);
		//Packet was here
		currentWorld.removePlayerEntityDangerously(player);
		player.isDead = false;

		transferEntityToWorld(player, world, createPortal);
		
		player.playerNetServerHandler.sendPacket(new S07PacketRespawn(world.getEnv().getMagicValue(), world.getDifficulty(), world.getWorldInfo().getTerrainType(), player.theItemInWorldManager.getGameType()));
		
		server.getConfigurationManager().preparePlayer(player, world);
		player.playerNetServerHandler.setPlayerLocation(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
		player.theItemInWorldManager.setWorld(world);
		
		server.getConfigurationManager().updateTimeAndWeatherForPlayer(player, world);
		server.getConfigurationManager().syncPlayerInventory(player);
		Iterator<PotionEffect> effects = player.getActivePotionEffects().iterator();
		
		while (effects.hasNext()) {
			PotionEffect currentEffect = effects.next();
			player.playerNetServerHandler.sendPacket(new S1DPacketEntityEffect(player.getEntityId(), currentEffect));
		}
	}
	
	public void takePlayerToNether(EntityPlayerMP player) {
		WorldServer nether = worldManager.getLoadedWorlds().get(1);
		takePlayerToWorld(player, nether, true);
	}
	
	public void takePlayerToEnd(EntityPlayerMP player) {
		WorldServer end = worldManager.getLoadedWorlds().get(2);
		takePlayerToWorld(player, end, false);
	}
}
