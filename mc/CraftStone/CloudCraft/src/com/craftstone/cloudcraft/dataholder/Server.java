package com.craftstone.cloudcraft.dataholder;

public class Server {
	private String ip;
	private int port;
	private String name;
	
	public Server(String ip, int port, String name) {
		this.ip = ip;
		this.port = port;
		this.name = name;
	}

	public String getIp() {
		return ip;
	}

	public int getPort() {
		return port;
	}

	public String getName() {
		return name;
	}
	
}
