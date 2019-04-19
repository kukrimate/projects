package com.craftstone.stone.event.world;

import com.craftstone.stone.world.World;

/**
 * Called when a world is loaded
 * @author kmate
 *
 */
public class WorldLoadEvent extends WorldEventBase {
	public WorldLoadEvent(World world) {
		super(world);
	}
}
