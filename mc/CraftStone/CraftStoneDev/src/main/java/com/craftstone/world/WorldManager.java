package com.craftstone.world;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldServerMulti;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldSettings.GameType;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.storage.AnvilSaveHandler;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.craftstone.stone.world.Enviroment;
import com.google.common.base.Optional;

public class WorldManager {
	public static final File WORLDS_DIR = new File("worlds");
	
	private final String defaultWorldName;
	private final WorldType defaultWorldType;
	private final long defaultWorldSeed;
	private Logger logger = LogManager.getLogger();
	private MinecraftServer server = MinecraftServer.getServer();
	private List<WorldServer> loadedWorlds;
	private int spawnWorld;
	
	public WorldManager(String name, WorldType type, long seed) 
	{
		this.defaultWorldName = name;
		this.defaultWorldType = type;
		this.defaultWorldSeed = seed;
		this.spawnWorld = 0;
		loadedWorlds = new ArrayList<WorldServer>();
		loadDefaultWorlds();
	}
	
	//Public stuff
	public List<WorldServer> getLoadedWorlds() {
		return loadedWorlds;
	}
	
	public void unloadWorld(String name) {
		WorldServer world = worldByName(name).get();
		if (world == null) { return; }
		
		if (world.playerEntities.size() == 0) {
			this.loadedWorlds.remove(world);
		}
	}
 	
	public void setSpawnWorld(String name) {
		WorldServer world = worldByName(name).get();
		if (world == null) { return; }
		spawnWorld = getWorldNumber(world);
	}
	
	@Nullable
	public Optional<WorldServer> worldByName(String name) {
		for (WorldServer world : loadedWorlds) {
			if (world.getWorldName().equals(name)) {
				return Optional.of(world);
			}
		}
		
		return Optional.absent();
	}
	
	public WorldServer getSpawnWorld() {
		return loadedWorlds.get(spawnWorld);
	}
	
	public void addNewWorld(String name, WorldType type, Enviroment env, long seed, GameType gameType)
	{
    	WorldSettings worldSettings = new WorldSettings(seed, gameType, server.canStructuresSpawn(), server.isHardcore(), defaultWorldType);
    	worldSettings.setWorldName(name);
		ISaveHandler saveHandler = new AnvilSaveHandler(WORLDS_DIR, name, true);
        WorldInfo worldInfo = saveHandler.loadWorldInfo();
    	if (worldInfo == null) {
    		worldInfo = new WorldInfo(worldSettings, name);
    	}
    	
    	WorldServer world = (WorldServer) new WorldServer(server, saveHandler, worldInfo, server.theProfiler, env).init();
    	world.initialize(worldSettings);
    	world.addWorldAccess(new net.minecraft.world.WorldManager(server, world));
    	
    	this.loadedWorlds.add(world);
	}
	
	public void saveAllWorlds(boolean displayMessage) 
	{
        if(this.loadedWorlds != null) {
        	
			if (displayMessage)
			{
	            logger.info("Saving worlds");
			}
			
            for (WorldServer world : loadedWorlds) {
            	saveWorld(world, displayMessage);
            }
         }
	}
	
	public long getDefaultWorldSeed() {
		return defaultWorldSeed;
	}

	//Private stuff
	private void loadDefaultWorlds() 
	{
		this.convertMapIfNeeded(defaultWorldName);
	    int worldCount = 3;
	      
	    for (int i = 0; i < worldCount; i++) 
	    {
	    	Enviroment env = Enviroment.OVERWORLD;
	    	String worldName = defaultWorldName;
	    	  
	    	if (i == 1) 
	    	{
	    		env = Enviroment.NETHER;
	    	} 
	    	else if (i == 2) 
	    	{
	    		env = Enviroment.END;
	    	}
	    	  
	    	worldName = env == Enviroment.NETHER ? worldName + "_nether" : (env == Enviroment.END ? worldName + "_the_end"  : worldName);
	    	  
	    	WorldSettings worldSettings = new WorldSettings(defaultWorldSeed, server.getGameType(), server.canStructuresSpawn(), server.isHardcore(), defaultWorldType);
	    	worldSettings.setWorldName(worldName);
	    	  
	    	ISaveHandler saveHandler = new AnvilSaveHandler(WORLDS_DIR, worldName, true);
	        WorldInfo worldInfo = saveHandler.loadWorldInfo();
	    	if (worldInfo == null) {
	    		worldInfo = new WorldInfo(worldSettings, worldName);
	    	}
	    	  
	    	WorldServer currentWorld = null;
	    		  
	    	if (env == Enviroment.OVERWORLD) 
	    	{
	    		currentWorld = (WorldServer) new WorldServer(server, saveHandler, worldInfo, server.theProfiler, env).init();
	    	} 
	    	else 
	    	{
	    		currentWorld = (WorldServer) new WorldServer(server, saveHandler, worldInfo, server.theProfiler, env).init();
	    	}

	    	currentWorld.initialize(worldSettings);
	    	currentWorld.addWorldAccess(new net.minecraft.world.WorldManager(server, currentWorld));
	    	  
	    	loadedWorlds.add(currentWorld);
	      }

	      server.getConfigurationManager().setPlayerManager(this.loadedWorlds.toArray(new WorldServer[this.loadedWorlds.size()]));
	      setDifficultyForAllWorlds(server.getDifficulty());
	      prepareWorld(loadedWorlds.get(0));
	   }
	
	private void prepareWorld(WorldServer world) 
	{
		int percentage = 0;
		logger.info("Preparing spawn region for '" + world.getWorldName() + "'");
		BlockPos var8 = world.getSpawnPoint();
		long var9 = MinecraftServer.getCurrentTimeMillis();

		for(int var11 = -192; var11 <= 192 && server.isServerRunning(); var11 += 16) 
		{
			for(int var12 = -192; var12 <= 192 && server.isServerRunning(); var12 += 16) 
			{
			    long var13 = MinecraftServer.getCurrentTimeMillis();
				if(var13 - var9 > 1000L) {
				      this.outputTaskPrecentage("Preparing world spawn", percentage * 100 / 625);
				      var9 = var13;
				}
				++percentage;
				world.theChunkProviderServer.loadChunk(var8.getX() + var11 >> 4, var8.getZ() + var12 >> 4);
			}
		}
	}
	
	private void outputTaskPrecentage(String var1, int var2) 
	{
		 logger.info(var1 + ": " + var2 + "%");
	}
	
	private void convertMapIfNeeded(String var1) 
	{
		if(server.getActiveAnvilConverter().isOldMapFormat(var1)) 
		{
			logger.info("Converting map!");
			server.getActiveAnvilConverter().convertMapFormat(var1, new IProgressUpdate() {
				private long startTime = System.currentTimeMillis();

			    public void displaySavingString(String var1) {}
			    
			    public void setLoadingProgress(int var1) {
				    if(System.currentTimeMillis() - this.startTime >= 1000L) {
				         this.startTime = System.currentTimeMillis();
				         logger.info("Converting... " + var1 + "%");
				    }
			    }
			    
		        public void displayLoadingString(String var1) {}
		    });
		}
	}
	
	private void saveWorld(WorldServer world, boolean displayMessage) 
	{
		if(world != null) 
		{
			if (displayMessage)
			{
				logger.info("Saving chunks for level \'" + world.getWorldInfo().getWorldName() + "\'/" + world.provider.getDimensionName());
			}
			
		    try 
		    {
		    	world.saveAllChunks(true, (IProgressUpdate)null);
		    } 
		    catch (MinecraftException var7) 
		    {
		    	logger.warn(var7.getMessage());
		    }
		    world.flush();
		}
	}
	
	private int getWorldNumber(WorldServer world) {
		if (world != null) {
			return loadedWorlds.indexOf(world);
		}
		
		return -1;
	}
	
	   public void setDifficultyForAllWorlds(EnumDifficulty var1) {
		      for(WorldServer world : loadedWorlds) {
		         if(world != null) {
		            if(world.getWorldInfo().isHardcoreModeEnabled()) {
		            	world.getWorldInfo().setDifficulty(EnumDifficulty.HARD);
		            	world.setAllowedSpawnTypes(true, true);
		            } else if(server.isSinglePlayer()) {
		            	world.getWorldInfo().setDifficulty(var1);
		            	world.setAllowedSpawnTypes(world.getDifficulty() != EnumDifficulty.PEACEFUL, true);
		            } else {
		            	world.getWorldInfo().setDifficulty(var1);
		            	world.setAllowedSpawnTypes(server.allowSpawnMonsters(), server.canSpawnAnimals);
		            }
		         }
		      }

		   }
}
