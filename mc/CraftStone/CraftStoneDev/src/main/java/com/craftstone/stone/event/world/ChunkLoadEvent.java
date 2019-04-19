package com.craftstone.stone.event.world;

import com.craftstone.stone.world.Chunk;

/**
 * Called when a chunk is loaded
 * @author kmate
 *
 */
public class ChunkLoadEvent extends ChunkEventBase {
	public ChunkLoadEvent(Chunk chunk) {
		super(chunk);
	}
}
