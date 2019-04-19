package com.craftstone.stone.event.world;

import com.craftstone.stone.event.EventBase;
import com.craftstone.stone.world.World;

/**
 * Event base file for world based events
 * @author kmate
 *
 */
public class WorldEventBase implements EventBase {
	private World world;
	
	public WorldEventBase(World world) {
		this.world = world;
	}

	/**
	 * Returns the world affected by this event
	 * @return the world
	 */
	public World getWorld() {
		return world;
	}
}