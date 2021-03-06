package com.marine.net.play.clientbound;

import java.io.IOException;
import java.io.OutputStream;

import com.marine.io.data.ByteData;
import com.marine.net.Packet;
import com.marine.net.PacketOutputStream;
import com.marine.net.States;
import com.marine.util.Location;

public class PlayerPositionPacket extends Packet {

	final Location l;
	public PlayerPositionPacket(Location l) {
		this.l = l;
	}
	@Override
	public int getID() {
		return 0x04;
	}
	@Override
	public void writeToStream(PacketOutputStream stream) throws IOException {
		ByteData d = new ByteData();
		d.writeVarInt(getID());
		
		d.writeDouble(l.getX());
		d.writeDouble(l.getY());
		d.writeDouble(l.getZ());
		
		d.writeBoolean(true);
		
		d.writePacketPrefix();
		
		stream.write(getID(), d.getBytes());
	}
	@Override
	public void readFromBytes(ByteData input) {}
	@Override
	public States getPacketState() {
		return States.INGAME;
	}

	
}
