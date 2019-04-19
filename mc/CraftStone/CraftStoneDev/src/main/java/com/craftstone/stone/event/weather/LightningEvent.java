package com.craftstone.stone.event.weather;

import com.craftstone.stone.event.Cancellable;
import com.craftstone.stone.math.VectorDouble;
import com.craftstone.stone.world.World;

/**
 * An event when lightning is sent out
 * @author kmate
 *
 */
public class LightningEvent extends WeatherEventBase implements Cancellable {
	private boolean wasCancelled = false;
	private VectorDouble position;
	
	public LightningEvent(World world, VectorDouble position) {
		super(world);
		this.position = position;
	}

	/**
	 * Cancels this lightning
	 */
	@Override
	public void cancel() {
		wasCancelled = true;
	}

	/**
	 * Checks was the event cancelled
	 */
	@Override
	public boolean wasCanclled() {
		return wasCancelled;
	}

	/**
	 * Returns the position of the lightning
	 * @return the position
	 */
	public VectorDouble getPosition() {
		return position;
	}
}
