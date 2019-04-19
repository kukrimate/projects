package com.craftstone.stone.command;

/**
 * An object that can send commands like a player
 * @author kmate
 *
 */
public interface CommandSender {
	/**
	 * Returns the name of the sender
	 * @return
	 */
	public String getName();

	/**
	 * Sends a message to the sender
	 * @param message
	 */
	public void sendMessage(String message);
}
