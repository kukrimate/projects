package com.craftstone.cloudcraft;
/** 
 * This program is an example from the book "Internet 
 * programming with Java" by Svetlin Nakov. It is freeware. 
 * For more information: http://www.nakov.com/books/inetjava/ 
 */ 
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

import com.craftstone.cloudcraft.dataholder.Server;
 
/** 
 * TCPForwardServer is a simple TCP bridging software that 
 * allows a TCP port on some host to be transparently forwarded 
 * to some other TCP port on some other host. TCPForwardServer 
 * continuously accepts client connections on the listening TCP 
 * port (source port) and starts a thread (ClientThread) that 
 * connects to the destination host and starts forwarding the 
 * data between the client socket and destination socket. 
 */ 
public class CloudCraftServer {
	public static volatile boolean TERMINATE = false;
	
	private ConfigManager config = new ConfigManager(new File("config.ini"));
	private int port = 25565;
	private int serverCount = 3;
	private List<Server> servers = new ArrayList<Server>();
	private ServerSocket serverSocket;
	
	@SuppressWarnings("deprecation")
	public CloudCraftServer() throws IOException {
		loadConfig();
        serverSocket = new ServerSocket(port); 
		Thread t = new ConsoleScanner();
		Thread t2 = new ServerDeamon(serverSocket, servers.get(0));
		t.start();
		t2.start();
		while (!TERMINATE) {}
		t.stop();
		t2.stop();
        serverSocket.close();
        config.saveConfig();
        System.exit(0);
	}
	
    private void loadConfig() {
    	this.port = Integer.valueOf(this.config.getString("port", String.valueOf(port)));
    	this.serverCount = Integer.valueOf(this.config.getString("serverCount", String.valueOf(serverCount)));
    	for (int i = 0; i < serverCount; i++) {
    		String str = "server" + i+1;
    		String currIp = config.getString(str + "-ip", "127.0.0.1");
    		int currPort = Integer.valueOf(this.config.getString(str + "-port", String.valueOf(port + i + 1)));
    		String currName = config.getString(str + "-name", str);
    		this.servers.add(new Server(currIp, currPort, currName));
    	}
    }
    
    public static void main(String[] args) throws IOException {
    	new CloudCraftServer();
    }
}