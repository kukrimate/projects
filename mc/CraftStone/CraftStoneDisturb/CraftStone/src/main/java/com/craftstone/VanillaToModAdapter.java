package com.craftstone;

import net.minecraft.world.WorldType;
import com.craftstone.stone.world.Enviroment;

public class VanillaToModAdapter {
	public static Enviroment dimToEnv(int dim) {
		if (dim == 0) {
			return Enviroment.OVERWORLD;
		} else if (dim == -1) {
			return Enviroment.NETHER;
		} else {
			return Enviroment.END;
		}
	}
	
	public static com.craftstone.stone.world.EnumWorldType worldTypeAdapter(WorldType typeIn) {
		if (typeIn.equals(WorldType.DEFAULT)) {
			return com.craftstone.stone.world.EnumWorldType.NORMAL;
		} else if(typeIn.equals(WorldType.DEFAULT_1_1)) {
			return com.craftstone.stone.world.EnumWorldType.OLD;
		} else if(typeIn.equals(WorldType.FLAT)) {
			return com.craftstone.stone.world.EnumWorldType.FLAT;
		} else if(typeIn.equals(WorldType.AMPLIFIED)) {
			return com.craftstone.stone.world.EnumWorldType.AMPLIFIED;
		} else if(typeIn.equals(WorldType.CUSTOMIZED)) {
			return com.craftstone.stone.world.EnumWorldType.CUSTOMIZED;
		} else if(typeIn.equals(WorldType.DEBUG_WORLD)) {
			return com.craftstone.stone.world.EnumWorldType.DEBUG;
		} else if(typeIn.equals(WorldType.LARGE_BIOMES)) {
			return com.craftstone.stone.world.EnumWorldType.LARGE_BIOMES;
		}
		
		return com.craftstone.stone.world.EnumWorldType.NORMAL;
	}
}
