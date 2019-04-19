package com.craftstone.stone.event;

/**
 * An event that can be cancelled
 * @author kmate
 *
 */
public interface Cancellable {
	/**
	 * Cancels the event
	 */
	public void cancel();
	
	/**
	 * Checks is the event was cancelled
	 * @return the flag
	 */
	public boolean wasCanclled();
}
