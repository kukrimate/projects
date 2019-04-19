package com.craftstone;

import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;

import com.craftstone.stone.math.VectorDouble;

public class CraftBlock implements com.craftstone.stone.block.Block {

	Block block;
	BlockPos pos;

	public CraftBlock(Block block, BlockPos pos) {
		this.block = block;
		this.pos = pos;
	}

	@Override
	public String getID() {
		return block.getID();
	}

	@Override
	public VectorDouble getPosition() {
		return new VectorDouble(pos.getX(), pos.getY(), pos.getZ());
	}

	@Override
	public String getName() {
		return block.getUnlocalizedName();
	}
}
