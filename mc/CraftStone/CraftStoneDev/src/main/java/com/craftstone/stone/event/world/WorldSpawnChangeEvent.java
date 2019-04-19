package com.craftstone.stone.event.world;

import com.craftstone.stone.math.VectorDouble;
import com.craftstone.stone.world.World;

/**
 * Called when the spawn location of the world is changeds
 * @author kmate
 *
 */
public class WorldSpawnChangeEvent extends WorldEventBase {
	private VectorDouble prevLoc;
	
	public WorldSpawnChangeEvent(World world, VectorDouble prevLoc) {
		super(world);
		this.prevLoc = prevLoc;
	}

	/**
	 * The previous spawn location
	 * @return
	 */
	public VectorDouble getPreviousLocation() {
		return prevLoc;
	}
}