package com.podloot.eyemod.gui.elements.map;

import java.util.HashMap;

import com.podloot.eyemod.lib.gui.util.Color;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PaneBlock;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MapType {
	
	World world;
	BlockPos mapPos;
	
	int bottom = 0;
	int height = 0;
	int light_min = 50;
	int light_max = 20;
	
	HashMap<Block, MaterialColor> recolor = new HashMap<Block, MaterialColor>();
	
	//Adding values for solid, air, water, lava, glass
	int[] tr = {0, 0, 0, 110, 20, 0};
	int[] tg = {0, 0, 30, 20, 20, 0};
	int[] tb = {0, 0, 100, -10, 30, 0};
	
	public MapType(World world) {
		this.world = world;
		bottom = 0;
		height = world.getMaxBuildHeight();
		recolor.put(Blocks.TALL_GRASS, Blocks.GRASS_BLOCK.defaultMaterialColor());
		recolor.put(Blocks.GRASS, Blocks.GRASS_BLOCK.defaultMaterialColor());
	}
	
	public MapType set(BlockPos mapPos) {
		this.mapPos = mapPos;
		return this;
	}
	
	public void setRange(int bottom, int height, int lmin, int lmax) {
		this.bottom = bottom;
		this.height = height;
		this.light_min = lmin;
		this.light_max = lmax;
		System.out.println(Blocks.ICE.defaultMaterialColor().col);
	}
	
	public DynamicTexture getMap(int width, int height) {
		NativeImage map = new NativeImage(width, height, false);
		if(mapPos == null) return new DynamicTexture(map);
		for(int i = 0; i < width; i++) {
			for(int j = 0; j < height; j++) {
				int x = mapPos.getX() - width/2;
				int z = mapPos.getZ() - height/2;
				map.setPixelRGBA(i, j, this.getMapColor(x + i, z + j));
			}
		}
		return new DynamicTexture(map);
	}
	
	public int getMapColor(int x, int z) {
		int trans = 0;
		for(int i = height; i >= bottom; i--) {
			BlockState bs = world.getBlockState(new BlockPos(x, i, z));
			int t = this.isTransperant(bs);
			if(t == 0) {
				float grey = this.getDarkness(mapPos.getY() - light_min, mapPos.getY() + light_max, i);
				Color co = new Color(getColor(bs));
				int e = (i % 2 == 0 ? 8 : 0);
				int b = (int) (co.getBlue() * grey) + e + tb[trans];
				int g = (int) (co.getGreen() * grey) + e + tg[trans];
				int r = (int) (co.getRed() * grey) + e + tr[trans];
				return new Color(r < 0 ? 0 : r > 255 ? 255 : r, g < 0 ? 0 : g > 255 ? 255 : g, b < 0 ? 0 : b > 255 ? 255 : b).getBGR();
			} else if(t > 1) {
				trans = t;
			}
		}
		return 0xff000000;
	}
	
	public int isTransperant(BlockState bs) {
		if(bs.isAir()) return 1;
		if(!bs.getFluidState().isEmpty()) {
			if(bs.getBlock() == Blocks.LAVA) return 3;
			return 2;
		}
		if(bs.getBlock() == Blocks.GLASS || bs.getBlock() instanceof PaneBlock) return 4;
		return 0;
	}
	
	public int getColor(BlockState bs) {
		if(recolor.containsKey(bs.getBlock())) {
			return recolor.get(bs.getBlock()).col;
		} else {
			MaterialColor mc = bs.getBlock().defaultMaterialColor();
			return mc == MaterialColor.NONE ? Blocks.OAK_WOOD.defaultMaterialColor().col : mc.col;
		}
	}
	
	public int getHeight(int x, int z) {
		for(int i = height; i >= bottom; i--) {
			BlockState bs = world.getBlockState(new BlockPos(x, i, z));
			if(!bs.isAir()) {
				return i;
			}
		}
		return 0;
	}
	
	public float getDarkness(int min, int max, int value) {
		value = value < min ? min : value > max ? max : value;
		return (((float)(value - min) / (float)(max - min)) * 0.9F) + 0.1F;
	}
	
	public ResourceLocation getWorldID() {
		return world.dimension().location();
	}
	
	public World getWorld() {
		return world;
	}
	
	

	
	

}
