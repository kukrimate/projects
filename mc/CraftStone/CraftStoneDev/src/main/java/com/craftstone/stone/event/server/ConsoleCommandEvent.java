package com.craftstone.stone.event.server;

import com.craftstone.stone.event.Cancellable;

/**
 * Event called when a console command is sent
 * @author kmate
 *
 */
public class ConsoleCommandEvent extends ServerEventBase implements Cancellable {
	private boolean wasCancalled = false;
	private String command;
	private String[] arguments;
	
	public ConsoleCommandEvent(String command, String[] arguments) {
		this.command = command;
		this.arguments = arguments;
	}

	/**
	 * Returns the command
	 * @return the command
	 */
	public String getCommand() {
		return command;
	}

	/**
	 * Returns the command arguments
	 * @return the arguments
	 */
	public String[] getArguments() {
		return arguments;
	}

	/**
	 * Cancels the event
	 */
	@Override
	public void cancel() {
		wasCancalled = true;
	}

	/**
	 * Checks the event was cancelled or not
	 */
	@Override
	public boolean wasCanclled() {
		return wasCancalled;
	}
	
}
