package com.marine.io.data;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import com.marine.Logging;
import com.marine.util.Position;

public class ByteData implements Iterable<Byte> {
	
	protected List<Byte> bytes;
	
	protected int readerPos;
	
	public ByteData(List<Byte> list) {
		this.bytes = list;
	}
	
	public ByteData(byte[] bytes) {
		this.bytes = new ArrayList<Byte>();
		
		for(byte b : bytes)
			this.bytes.add(b);
	}
	
	public ByteData() {
		this.bytes = new ArrayList<Byte>();
	}
	public void writeByte(byte... b) {
		for(byte by : b)
			bytes.add(by);
	}
	
	public boolean readBoolean() {	
		if(readByte() == 0)
			return false;
		else
			return true;
	}
	
	public byte readByte() {
		
		if(!hasBytes()) {
			Logging.getLogger().error("ByteData object is empty");
			return 0;
		}
		
		if(!canReadAnother()) {
			Logging.getLogger().error("ByteData ran out of bytes");
			new Exception().printStackTrace();
			return 0;
		}
		
		byte b = bytes.get(readerPos);
		readerPos++;
		return b;
	}
	
	public short readShort() {
		if(bytes.size() < 2)
			return 0;
		
		return (short)((readByte() << 8) | (readByte() & 0xff));
	}
	
	public int readUnsignedShort() {
		if(bytes.size() < 2)
			return 0;
		
		return (((readByte() & 0xff) << 8) | (readByte() & 0xff));
	}
	
	public int readInt() {
		
		if(bytes.size() < 4)
			return 0;		
		
		return  (((readByte() & 0xff) << 24) | ((readByte() & 0xff) << 16) |
				  ((readByte() & 0xff) << 8) | (readByte() & 0xff));
		
	}
	
	public long readLong() {
		if(bytes.size() < 8)
			return 0;		
		return  (((long)(readByte() & 0xff) << 56) |
				  ((long)(readByte() & 0xff) << 48) |
				  ((long)(readByte() & 0xff) << 40) |
				  ((long)(readByte() & 0xff) << 32) |
				  ((long)(readByte() & 0xff) << 24) |
				  ((long)(readByte() & 0xff) << 16) |
				  ((long)(readByte() & 0xff) <<  8) |
				  ((long)(readByte() & 0xff)));
	}
	
	public float readFloat() {
		return Float.intBitsToFloat(readInt());
	}
	
	public double readDouble() {
		return Double.longBitsToDouble(readLong());
	}
	
	public char readChar() { // To give control of character readings in updates
		return readCharacter();
	}
	
	private char readCharacter() {
		if(bytes.size() < 2)
			return 0;
		
		return (char)((readByte() << 8) | (readByte() & 0xff));
	};
	
	public byte[] readAllBytes() {
		byte[] a = new byte[bytes.size()];
		int i = 0;
		for(byte b : bytes) {
			a[i] = b;
			readerPos++;
			i++;
		}
		
		return a;
 	}
	
	
	public byte[] getBytes() {
		byte[] a = new byte[bytes.size()];
		int i = 0;
		for(byte b : bytes) {
			a[i] = b;
			i++;
		}
		
		return a;
	}
	

	public int readVarInt(){
        int out = 0;
        int bytes = 0;
        byte in;
        while (true) {
            in = readByte();
            out |= (in & 0x7F) << (bytes++ * 7);
            if ((in & 0x80) != 0x80) {
                break;
            }
        }
        return out;
    }
	
	public ByteData subData(int a, int b) {
		return new ByteData(this.bytes.subList(a, b));
	}
	
	public ByteData readData(int l) {
		int x = 0;
		ByteData data = new ByteData();
		
		while(l > x) {
			data.writeByte(readByte());
			x++;
		}
		
		return data;
	}
	
	public byte[] read(byte... input) {
		int i = 0;
		while(i < input.length) {
			input[i] = readByte();
			i++;
		}
		return input;
	}
	
	public byte[] readBytes(int amt) {
		byte[] r = new byte[amt];
		int i = 0;
		while(amt > i) {
			r[i] = readByte();
		}
		
		return r;
	}
	
	public boolean hasBytes() {
		return !bytes.isEmpty();
	}
	
	public boolean canReadAnother() {
		return getRemainingBytes() > 0;
	}
	
	public int remainingBytes() {
		return bytes.size() - readerPos;
	}
	
	public String readUTF8() {
		int l = readVarInt();
        if (l>= Short.MAX_VALUE) {
        	Logging.getLogger().error("Tried to read String greater then max value!");
        }
		byte[] data = new byte[l];
		data = read(data);
		return new String(data, StandardCharsets.UTF_8);
	}
	
	public String readUTF8Short() {
		int l = readShort();
		byte[] data = new byte[l];
		data = read(data);
		return new String(data, StandardCharsets.UTF_8);
	}
	
	public void writeToStream(OutputStream stream) {
		try {
			stream.write(getBytes());
		} catch (IOException e) {
		}
	}
	
	public int getReaderPos() {
		return readerPos;
	}
	
	public int getRemainingBytes() {
		return bytes.size() - readerPos;
	}
	
	public int getLength() {
		return bytes.size();
	}
	
	public void writeUTF8(String v) {
		final byte[] bytes = v.getBytes(StandardCharsets.UTF_8);
        if (bytes.length >= Short.MAX_VALUE) {
        	Logging.getLogger().error("Tried to write String greater then max value!");
        }
        // Write the string's length
        writeVarInt(bytes.length);
        writeend(bytes);
	}
	
	public void writeUTF8Short(String v) {
		final byte[] bytes = v.getBytes(StandardCharsets.UTF_8);
        writeShort((short) bytes.length);
        writeend(bytes);
	}
	
	public void writeend(byte... v) {
		for(byte b : v)
			bytes.add(b);
	}
	
	public void write(int pos, byte... v) {
		ArrayList<Byte> bl = new ArrayList<Byte>();
		for(byte b : v)
			bl.add(b);
		bytes.addAll(pos, bl);
	}
	
	public void writeBoolean(boolean v) {
		writeend(ByteEncoder.writeBoolean(v));
	}
	public void writeShort(short v) {
		writeend(ByteEncoder.writeShort(v));
	}
	public void writeInt(int v) {
		writeend(ByteEncoder.writeInt(v));
	}
	public void writeLong(long v) {
		writeend(ByteEncoder.writeLong(v));
	}
	public void writeFloat(float v) {
		writeend(ByteEncoder.writeFloat(v));
	}
	public void writeDouble(double v) {
		writeend(ByteEncoder.writeDouble(v));
	}
	public void writeVarInt(int v) {
		writeend(ByteEncoder.writeVarInt(v));
	}
	
	public boolean writeObj(Object obj) {
		if(obj instanceof Byte) {
			writeByte((byte) obj); return true; } else
			if(obj instanceof Short) {
				writeShort((short) obj); return true; } else
				if(obj instanceof Integer) {
					writeInt((int) obj); return true; } else
					if(obj instanceof Long) {
						writeLong((long) obj); return true; } else
						if(obj instanceof Float) {
							writeFloat((float) obj); return true; } else
								if(obj instanceof Double) {
									writeDouble((double) obj); return true; } else
										return false;
		
	}

	public void writeVarInt(int pos, int v) {
		write(pos,ByteEncoder.writeVarInt(v));
	}
	
	public void writePosition(Position pos) {
		writeend(ByteEncoder.writeLong(pos.encode()));
	}
	
	public void writePacketPrefix() {
		int l = bytes.size();
		write(0, ByteEncoder.writeVarInt(l));
	}

	@Override
	public Iterator<Byte> iterator() {
		return bytes.iterator();
	}

	public void writeUUID(UUID uuid) {
		writeLong(uuid.getMostSignificantBits());
        writeLong(uuid.getLeastSignificantBits());
	}
}