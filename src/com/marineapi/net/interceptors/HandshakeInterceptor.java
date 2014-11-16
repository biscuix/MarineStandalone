package com.marineapi.net.interceptors;

import com.marineapi.Logging;
import com.marineapi.net.Client;
import com.marineapi.net.data.ByteData;
import com.marineapi.net.handshake.ClientHandshake;


public class HandshakeInterceptor implements PacketInterceptor {

	@Override
	public boolean intercept(ByteData data, Client c) {
		
		int id = data.readVarInt();
		
		if(id==0x00) {
			ClientHandshake packet = new ClientHandshake();
			packet.readFromBytes(data);
			
			Logging.getLogger().info("Handshake from client: " + c.getAdress().toString());
			
			c.setState(packet.getState());
			
			return true;
		}
		
		
		
		return false;
	}
}