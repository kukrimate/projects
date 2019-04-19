package com.craftstone.stone.world;

import java.util.List;

import com.craftstone.stone.block.Block;
import com.craftstone.stone.scoreboard.Scoreboard;
import com.craftstone.stone.world.biome.Biome;

/**
 * Holds a world
 * @author kmate
 *
 */
public interface World {
	public String getWorldName();
	
	public Enviroment getEnviroment();
	
	public EnumWorldType getType();
	
	public Block getBlockAt(int x, int y, int z);
	
	public Biome getBiomeAtCoords(int x, int y, int z);
	
	public Chunk getChunkAt(int x, int z);
	
	public Scoreboard getWorldScoreboard();
	
	public boolean isMainWorld();
	
	public long getWorldTime();
	
	public void setWorldTime(long time);
	
	public void addTime(long timeToAdd);
	
	public List<Chunk> getChunks();
	
	public void setRaining(boolean state, double duration);
	
	public void setThudering(boolean state, double duration);
	
	public void strikeLightning(int x, int y, int z);
	
	public boolean isRaining();
	
	public boolean isThundering();
}
