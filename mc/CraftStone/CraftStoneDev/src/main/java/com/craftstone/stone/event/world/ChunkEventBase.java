package com.craftstone.stone.event.world;

import com.craftstone.stone.world.Chunk;

/**
 * An event base file for chunk events
 * @author kmate
 *
 */
public class ChunkEventBase extends WorldEventBase {
	private Chunk chunk;
	
	public ChunkEventBase(Chunk chunk) {
		super(chunk.getWorld());
		this.chunk = chunk;
	}

	/**
	 * Returns the chunk affected by this event
	 * @return the affected chunk
	 */
	public Chunk getChunk() {
		return chunk;
	}
}