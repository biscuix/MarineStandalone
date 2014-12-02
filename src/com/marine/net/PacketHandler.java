package com.marine.net;

import com.marine.StandaloneServer;
import com.marine.io.data.ByteData;
import com.marine.net.interceptors.HandshakeInterceptor;
import com.marine.net.interceptors.LoginInterceptor;
import com.marine.net.interceptors.PacketInterceptor;
import com.marine.net.interceptors.StatusInterceptor;


public class PacketHandler implements PacketInterceptor {
	
	HandshakeInterceptor handshake;
	StatusInterceptor status;
	LoginInterceptor login;
	
	public PacketHandler(StandaloneServer server) {
		handshake = new HandshakeInterceptor();
		status = new StatusInterceptor();
		login = new LoginInterceptor(server);
	}
	
	public boolean intercept(ByteData data, Client c) {
		if(c.getState().equals(States.HANDSHAKE))
			return handshake.intercept(data, c);
		else
		if(c.getState().equals(States.INTRODUCE))
			return status.intercept(data, c);
		else
		if(c.getState().equals(States.LOGIN))
			return login.intercept(data, c);
				
		
		return false;
		
	}
}
