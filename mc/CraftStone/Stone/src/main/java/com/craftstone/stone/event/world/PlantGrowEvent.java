package com.craftstone.stone.event.world;

import java.util.List;

import com.craftstone.stone.block.Block;
import com.craftstone.stone.event.Cancellable;
import com.craftstone.stone.world.World;

/**
 * Called when a plant, mushroom etc is growed
 * @author kmate
 *
 */
public class PlantGrowEvent extends WorldEventBase implements Cancellable {
	private Block plant;
	private List<Block> growedState;
	private boolean wasCancelled = false;
	
	public PlantGrowEvent(World world, Block plant, List<Block> growedState) {
		super(world);
		this.growedState = growedState;
		this.plant = plant;
	}

	@Override
	public void cancel() {
		wasCancelled = true;
	}

	@Override
	public boolean wasCanclled() {
		return wasCancelled;
	}
	
	public List<Block> getGrowedState() {
		return growedState;
	}

	public Block getPlant() {
		return plant;
	}
}