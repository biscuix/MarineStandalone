package com.marine.world.generators;

import com.marine.world.Chunk;
import com.marine.world.World;

public abstract class WorldGenerator {
	
	protected World world;
	
	public WorldGenerator(World w) {
		this.world = w;
	}
	
	public abstract Chunk generateChunk(int x, int y);
}