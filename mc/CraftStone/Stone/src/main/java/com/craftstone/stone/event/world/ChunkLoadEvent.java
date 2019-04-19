package com.craftstone.stone.event.world;

import com.craftstone.stone.world.Chunk;

/**
 * Called when a chunk is loaded
 * @author kmate
 *
 */
public class ChunkLoadEvent extends ChunkEventBase {
	private boolean isNew;
	
	public ChunkLoadEvent(Chunk chunk, boolean isNewlyCreated) {
		super(chunk);
		this.isNew = isNewlyCreated;
	}

	/**
	 * Returns is the chunk is new or not
	 * @return
	 */
	public boolean isNew() {
		return isNew;
	}
}
