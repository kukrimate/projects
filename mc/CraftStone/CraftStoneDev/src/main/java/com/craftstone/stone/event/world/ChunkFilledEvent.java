package com.craftstone.stone.event.world;

import com.craftstone.stone.world.Chunk;

/**
 * Called when a chunk is filled with blocks (generation)
 * @author kmate
 *
 */
public class ChunkFilledEvent extends ChunkEventBase {
	public ChunkFilledEvent(Chunk chunk) {
		super(chunk);
	}
}
