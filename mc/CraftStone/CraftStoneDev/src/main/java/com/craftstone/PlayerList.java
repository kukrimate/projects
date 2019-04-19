package com.craftstone;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.common.collect.Lists;

//Holds a list of player names stored in a file
public class PlayerList {
	private final File file;
	private List<String> players = Lists.newArrayList();
	
	public PlayerList(File file) {
		this.file = file;
		if (!file.exists()) 
		{
			try 
			{
				file.createNewFile();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			} 
		}
		
		load();
	}
	
	public List<String> getPlayers() {
		return this.players;
	}
	
	public void addPlayer(String player) {
		if (!isPlayerOnList(player)) {
			players.add(player);
		}
	}
	
	public void removePlayer(String player) {
		if (isPlayerOnList(player)) {
			players.remove(player);
		}
	}
	
	public boolean isPlayerOnList(String player) {
		for (String str : players) {
			if (str.equalsIgnoreCase(player)) {
				return true;
			}
		}
		
		return false;
	}
	
	public void save() {
		try {
			file.delete();
			file.createNewFile();
			FileUtils.writeLines(file, players);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void load() {
		try {
			players = FileUtils.readLines(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
