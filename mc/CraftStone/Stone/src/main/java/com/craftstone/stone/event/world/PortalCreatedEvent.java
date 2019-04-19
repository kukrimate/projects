package com.craftstone.stone.event.world;

import java.util.List;

import com.craftstone.stone.block.Block;
import com.craftstone.stone.event.Cancellable;
import com.craftstone.stone.world.World;

/**
 * Called when a nether or end portal is created
 * @author kmate
 *
 */
public class PortalCreatedEvent extends WorldEventBase implements Cancellable {
	private List<Block> blocks;
	private boolean isCanclled = false;
	
	public PortalCreatedEvent(World world, List<Block> blocks) {
		super(world);
		this.blocks = blocks;
	}
	
	@Override
	public void cancel() {
		isCanclled = true;
	}
	
	@Override
	public boolean wasCanclled() {
		return isCanclled;
	}
	
	/**
	 * Returns the blocks of the portal
	 * @return the blocks
	 */
	public List<Block> getBlocks() {
		return blocks;
	}
}
