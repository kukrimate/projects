package com.craftstone.stone.event;

/**
 * Event priority
 * @author kmate
 *
 */
public enum EventPriority {
	/**
	 * That goes for events with low priority.
	 */
	LOW,
	/**
	 * This is for all below_normal events.
	 */
	BELOW_NORMAL,
	/**
	 * Just the portion for normal events NOTE: this is the default one
	 */
	NORMAL,
	/**
	 * This is for below_high ones,
	 */
	BELOW_HIGH,
	/**
	 * This is for high events.
	 */
	HIGH,
	/**
	 * This is for events need to executed before all others.
	 */
	HIGHEST;
}