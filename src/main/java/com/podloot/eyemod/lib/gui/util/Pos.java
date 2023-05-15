package com.podloot.eyemod.lib.gui.util;

import com.podloot.eyemod.lib.gui.EyeLib;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class Pos {
	
	String name = "undefined";
	BlockPos pos;
	ResourceLocation world;
	
	public Pos() {
		
	}
	
	public Pos(BlockPos pos, ResourceLocation world) {
		this.pos = pos;
		this.world = world;
	}

	public Pos(BlockPos pos, ResourceLocation world, String name) {
		this(pos, world);
		this.name = name;
	}
	
	
	public Pos fromString(String nbt) {
		if(nbt.contains("|")) {
			String[] i = nbt.split("\\|");
			if(i.length >= 4) {
				BlockPos pos = new BlockPos(EyeLib.getInt(i[0]), EyeLib.getInt(i[1]), EyeLib.getInt(i[2]));
				ResourceLocation dim = new ResourceLocation(i[3]);
				this.pos = pos;
				this.world = dim;
				if(i.length == 5) name = i[4];
				return this;
			}
		}
		return null;
	}
	
	public String toNbt() {
		return getPos().getX() + "|" + getPos().getY() + "|" + getPos().getZ() + "|" + getWorld().toString() + 
				(name.equals("undefined") ? "" : ("|" + name));
	}

	public ResourceLocation getWorld() {
		return world;
	}

	public BlockPos getPos() {
		return pos;
	}
	
	public String getName() {
		return name;
	}
	
	public String asString() {
		return asString(true);
	}
	
	public String asString(boolean w) {
		if(pos == null) return "Undefined";
		return (name.equals("undefined") ? "" : name + ": ") +  pos.getX() + "/" + pos.getY() + "/" + pos.getZ() + (world == null || !w ? "" : " (" + world.getPath() + ")");
	}
}