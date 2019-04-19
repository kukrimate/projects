package com.craftstone.cloudcraft;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.craftstone.cloudcraft.dataholder.Server;

public class ServerDeamon extends Thread {
	private ServerSocket server;
	private Server defaultServer;
	public volatile static boolean EXIT = false;
	
	public ServerDeamon(ServerSocket server, Server defaultServer) {
		this.server = server;
		this.defaultServer = defaultServer;
	}
	
	public void run() {
		while (!EXIT) {
			try {
				Socket sock = server.accept();
	            ClientThread clientThread = new ClientThread(sock, defaultServer); 
	            clientThread.start(); 
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}