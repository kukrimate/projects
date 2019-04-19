package com.craftstone.stone.event.weather;

import com.craftstone.stone.event.EventBase;
import com.craftstone.stone.world.World;

public class WeatherEventBase implements EventBase {
	private World world;
	
	public WeatherEventBase(World world) {
		this.world = world;
	}

	/**
	 * Returns the world affected by this event
	 * @return the world
	 */
	public World getWorld() {
		return world;
	}
}
