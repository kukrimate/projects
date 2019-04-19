package com.craftstone.stone.event.world;

import com.craftstone.stone.world.World;

/**
 * Called when a world is saved
 * @author kmate
 *
 */
public class WorldSaveEvent extends WorldEventBase {
	public WorldSaveEvent(World world) {
		super(world);
	}
}
