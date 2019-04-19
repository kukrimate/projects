package com.craftstone.stone.entity;

import com.craftstone.stone.command.CommandSender;
import com.craftstone.stone.math.VectorDouble;
import com.craftstone.stone.world.Chunk;
import com.craftstone.stone.world.World;

/**
 * Holds an entity
 * @author kmate
 *
 */
public interface Entity extends CommandSender {
	/**
	 * Returns the world of the entity
	 * @return the world
	 */
	public World getWorldOfEntity();
	
	/**
	 * Returns the chunk of this entity
	 * @return the chunk
	 */
	public Chunk getChunk();
	
	/**
	 * Teleports this entity to a new world
	 * @param newWorld the new world
	 */
	public void teleportTo(World newWorld);
	
	/**
	 * Returns the position of this entity
	 * @return the position
	 */
	public VectorDouble getPosition();
	
	/**
	 * Sets the position of this entity
	 * @param position the new position
	 */
	public void setPosition(VectorDouble position);
	
	/**
	 * Returns the fall distance of this entity
	 * @return the fall distance
	 */
	public float getFallDistance();
	
	/**
	 * Sets the fall distance of this entity
	 * @param fallDistance the float distance
	 */
	public void setFallDistance(float fallDistance);
	
	/**
	 * Returns the width of this entity
	 * @return the width
	 */
	public float getWidth();
	
	/**
	 * Returns the height of this entity
	 * @return the height
	 */
	public float getHeight();
	
	/**
	 * Returns the stepHeight of this entity
	 * @return the stepHeight
	 */
	public float getStepHeight();
	
	/**
	 * Sets the stepHeight of this entity
	 * @param height the new step height
	 */
	public void setStepHeight(float height);
	
	/**
	 * Sets the fire resistance of this entity
	 * @param resistence
	 */
	public void setFireResistance(int resistence);
	
	/**
	 * Returns the fire resistance of this entity
	 * @return the fire resistance
	 */
	public int getFireResistance();
	
	/**
	 * Returns is this entity is in the portal
	 * @return the flag
	 */
	public boolean isInPortal();
}
