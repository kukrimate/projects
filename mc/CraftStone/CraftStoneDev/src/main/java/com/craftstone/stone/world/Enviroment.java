package com.craftstone.stone.world;

public enum Enviroment {
	OVERWORLD,
	NETHER,
	END;
	
	/**
	 * Returns String version of this enviroment
	 */
	@Override
	public String toString() {
		if (this.equals(Enviroment.OVERWORLD)) {
			return "OVERWORLD";
		} else if (this.equals(Enviroment.NETHER)) {
			return "NETHER";
		} else if (this.equals(Enviroment.END)) {
			return "END";
		}
		
		return "UNKNOWN";
	}
	
	/**
	 * Magic value only use in implementation!
	 * @return
	 */
	@Deprecated
	public int getMagicValue() {
		if (this.equals(Enviroment.OVERWORLD)) {
			return 0;
		} else if (this.equals(Enviroment.NETHER)) {
			return -1;
		} else if (this.equals(Enviroment.END)) {
			return 1;
		}
		
		return 0;
	}
}