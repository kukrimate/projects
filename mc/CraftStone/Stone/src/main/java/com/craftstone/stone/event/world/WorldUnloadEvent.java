package com.craftstone.stone.event.world;

import com.craftstone.stone.event.Cancellable;
import com.craftstone.stone.world.World;

/**
 * Called when a world is unloaded
 * @author kmate
 *
 */
public class WorldUnloadEvent extends WorldEventBase implements Cancellable {
	private boolean cancelFlag = false;
	
	public WorldUnloadEvent(World world) {
		super(world);
	}

	@Override
	public void cancel() {
		cancelFlag = true;
	}

	@Override
	public boolean wasCanclled() {
		return cancelFlag;
	}

}
