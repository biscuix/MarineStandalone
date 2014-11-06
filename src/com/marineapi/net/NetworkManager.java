package com.marineapi.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.marineapi.Logging;

public class NetworkManager {
	public List<Client> connectedClients;
	
	public ServerSocket server;
	
	private ConnectionThread connector;
	

	public NetworkManager(int port) {
		connectedClients = new ArrayList<Client>();
		try {
			server = new ServerSocket(port, 5); //Port and num "queued" connections 
		} catch (IOException e) {
			Logging.getLogger().fatal("Port binding failed, perhaps allready in use");
		}
		connector = new ConnectionThread(5, this);
		connector.start(); // Permitt Connections
	}

	
	
	public List<Client> getClientList() {
		return connectedClients;
	}
	
	public Client[] getClients() {
		return (Client[]) connectedClients.toArray();
	}
	
	public void broadcastPacket(Packet p) {
		for(Client c : connectedClients)
			c.sendPacket(p);
	}

	public void connect(Socket accept) { 
		Client c = new Client(this, accept);
		Logging.getLogger().log("Client: " + accept.getInetAddress().toString() + " connected");
		connectedClients.add(c);
	}
}
