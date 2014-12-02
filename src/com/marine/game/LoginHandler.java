package com.marine.game;

import com.marine.net.Client;
import com.marine.net.States;
import com.marine.net.login.LoginSucessPacket;
import com.marine.player.*;
import com.marine.util.Location;
import com.marine.util.Position;
import com.marine.util.UUIDHandler;
import com.marine.world.World;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class LoginHandler {

	private Map<UUID, IPlayer> loggingInClients;
	
	private final PlayerManager players;
	
	private Location spawnLocation;
	
	public LoginHandler(PlayerManager playerManager, World w, Position spawnLocation) {
		this.spawnLocation = new Location(spawnLocation, w);
	
		players = playerManager;
		loggingInClients = Collections.synchronizedMap(new ConcurrentHashMap<UUID, IPlayer>());
	}
	
	public class LoginResponse {
		public final IPlayer player;
		public final String response;
		
		public LoginResponse(IPlayer p) {
			player = p;
			response = null;
		}
		
		public LoginResponse(String responseString) {
			player = null;
			response = responseString;
		}
		
		public boolean succeed() {
			return player !=  null;
		}
		
	}
	
	public LoginResponse preJoin(String name, Client c) { // Returns null if login succeded, otherwise makes LoginInterceptor drop the client
		UUID uuid = UUIDHandler.getUUID(name); //UUID.randomUUID(); //TODO: Retrive from Mojang
		
		if(players.isPlayerOnline(name))
			return new LoginResponse("Failed to login player is allready connected.");
		if(players.isPlayerOnline(uuid))
			return new LoginResponse("Failed to login player is allready connected.");
		
		//TODO: Check if player is banned incase they are drop them.
		
		synchronized(loggingInClients) {
			loggingInClients.put(uuid, new AbstractPlayer(players.getServer(), new PlayerID(name, uuid), c, new PlayerAbilites(false, false, false, 10, 10), spawnLocation));
		}
		
		return null;
	}
	

	public void passPlayer(UUID player) { //TODO: Encryption
		Player p = players.passFromLogin(loggingInClients.get(player));
		
		p.getClient().sendPacket(new LoginSucessPacket(p));
		p.getClient().setState(States.INGAME);
		
		loggingInClients.remove(player); // Remove from loginin process
		
		players.joinGame(p);
	}

}

