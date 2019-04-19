package com.craftstone.stone.event.server;

import java.util.List;

import com.craftstone.stone.entity.Player;

/**
 * This event is called when the player list is fetched from the server (1.8+)
 * @author kmate
 *
 */
public class PingRequestEvent extends ServerEventBase {
	private String motd;
	private int currPlayerCount;
	private int slotNumber;
	private List<Player> players;
	
	public PingRequestEvent(String motdText, int currPlayerCount, int slotNumber, List<Player> playersToDisplay) {
		if (currPlayerCount < 0) {
			System.out.println("WARNING: Negative player count: internal error!");
		}
		this.motd = motdText;
		this.currPlayerCount = currPlayerCount;
		this.slotNumber = slotNumber;
		this.players = playersToDisplay;
	}

	/**
	 * Returns the motd of the server
	 * @return the motd
	 */
	public String getMotd() {
		return motd;
	}

	/**
	 * Returns the current number of players on the server
	 * @return the current player count
	 */
	public int getCurrentPlayerCount() {
		return currPlayerCount;
	}

	/**
	 * Returns the max number of players 
	 * @return
	 */
	public int getSlotNumber() {
		return slotNumber;
	}

	/**
	 * Returns the list a player will be displayed in this ping (It can be changed)
	 * @return the list
	 */
	public List<Player> getPlayersToDisplay() {
		return players;
	}
}
