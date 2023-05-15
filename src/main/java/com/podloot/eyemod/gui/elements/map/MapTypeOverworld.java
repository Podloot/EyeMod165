package com.podloot.eyemod.gui.elements.map;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MapTypeOverworld extends MapType {
	
	public MapTypeOverworld(World world) {
		super(world);
	}
	
	public MapType set(BlockPos mapPos) {
		this.setRange(0, isUnderground(mapPos) ? mapPos.getY() + 4 : Math.max(world.getSeaLevel(), mapPos.getY()) + 40, 70, 20);
		return super.set(mapPos);
	}
	
	public boolean isUnderground(BlockPos mapPos) {
		return mapPos.getY() < (world.getSeaLevel()-8) && (mapPos.getY()+2 < this.getHeight(mapPos.getX(), mapPos.getZ()));
	}


}
