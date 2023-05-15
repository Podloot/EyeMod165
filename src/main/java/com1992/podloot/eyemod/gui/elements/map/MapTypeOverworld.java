package com.podloot.eyemod.gui.elements.map;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class MapTypeOverworld extends MapType {
	
	public MapTypeOverworld(Level world) {
		super(world);
	}
	
	public MapType set(BlockPos mapPos) {
		this.setRange(world.getMinBuildHeight(), isUnderground(mapPos) ? mapPos.getY() + 4 : Math.max(world.getSeaLevel(), mapPos.getY()) + 40, 70, 20);
		return super.set(mapPos);
	}
	
	public boolean isUnderground(BlockPos mapPos) {
		return mapPos.getY() < (world.getSeaLevel()-8) && (mapPos.getY()+2 < this.getHeight(mapPos.getX(), mapPos.getZ()));
	}


}
