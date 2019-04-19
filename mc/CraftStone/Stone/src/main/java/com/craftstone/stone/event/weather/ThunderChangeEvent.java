package com.craftstone.stone.event.weather;

import com.craftstone.stone.event.Cancellable;
import com.craftstone.stone.world.World;

public class ThunderChangeEvent extends WeatherEventBase implements Cancellable {
	private EnumWeather newWeather;
	private boolean iscancel = false;
	
	public ThunderChangeEvent(World world, EnumWeather changedTo) {
		super(world);
		this.newWeather = changedTo;
	}

	@Override
	public void cancel() {
		iscancel = true;
	}

	@Override
	public boolean wasCanclled() {
		return iscancel;
	}

	public EnumWeather getNewWeather() {
		return newWeather;
	}
}
