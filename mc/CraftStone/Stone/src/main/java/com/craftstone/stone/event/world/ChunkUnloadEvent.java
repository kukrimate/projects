package com.craftstone.stone.event.world;

import com.craftstone.stone.event.Cancellable;
import com.craftstone.stone.world.Chunk;

/**
 * Called when a chunk got unloaded
 * @author kmate
 *
 */
public class ChunkUnloadEvent extends ChunkEventBase implements Cancellable {

	private boolean isCancelled = false;
	
	public ChunkUnloadEvent(Chunk chunk) {
		super(chunk);
	}
	
	/**
	 * Cancells this event
	 */
	@Override
	public void cancel() {
		this.isCancelled = true;
	}

	/**
	 * Returns was this event cancelled
	 */
	@Override
	public boolean wasCanclled() {
		return isCancelled;
	}
	
}
