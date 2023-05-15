package com.podloot.eyemod.gui.elements.map;

import java.util.HashMap;

import com.podloot.eyemod.lib.gui.util.Color;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MapTypeXRay extends MapType {
	
	int alpha = 255;
	
	public MapTypeXRay(World world, int alpha) {
		super(world);
		this.setOres();
		this.alpha = alpha;
	}
	
	public MapType set(BlockPos mapPos) {
		this.setRange(mapPos.getY()-12, mapPos.getY()+2, 12, 4);
		return super.set(mapPos);
	}
	
	public void setAlpha(int a) {
		this.alpha = a;
	}
	
	public int getMapColor(int x, int z) {
		int trans = 0;
		int first = 0xff000000;
		for(int i = height; i >= bottom; i--) {
			BlockState bs = world.getBlockState(new BlockPos(x, i, z));
			int t = this.isTransperant(bs);
			if(i > mapPos.getY()-2) {
				int v = this.getValuable(bs.getBlock(), 0);
				if(v != 0) return new Color(v).getBGR();
			}
			if(t == 0) {
				if(first == 0xff000000) {
					float grey = this.getDarkness(mapPos.getY() - light_min, mapPos.getY() + light_max, i);
					Color co = new Color(getColor(bs));
					int e = (i % 2 == 0 ? 8 : 0);
					int b = (int) (co.getBlue() * grey) + e + tb[trans];
					int g = (int) (co.getGreen() * grey) + e + tg[trans];
					int r = (int) (co.getRed() * grey) + e + tr[trans];
					first = new Color(r < 0 ? 0 : r > 255 ? 255 : r, g < 0 ? 0 : g > 255 ? 255 : g, b < 0 ? 0 : b > 255 ? 255 : b, alpha).getBGR();
				}
			} else if(t > 1) {
				trans = t;
			}
		}
		return first;
	}
	
	public int getValuable(Block b, int input) {
		if(ores.containsKey(b)) return ores.get(b);
		return input;
	}
	
	public HashMap<Block, Integer> ores = new HashMap<Block, Integer>();
	
	final int DIAMOND = 0xff1ed0d6;
	final int GOLD = 0xfffcee4b;
	final int LAPIS = 0xff446fdc;
	final int REDSTONE = 0xffca0707;
	final int IRON = 0xffd8af93;
	final int COPPER = 0xffe0734d;
	final int COAL = 0xff363636;
	final int QUARTZ = 0xffeae5de;
	final int EMERALD = 0xff17dd62;
	final int RITE = 0xff804656;
	final int CHEST = 0xffa76e1f;
	final int INV = 0xff444444;
	final int AME = 0xff976997;
	final int OBSIDIAN = 0xff3b2754;

	public void setOres() {
		//ores.put(Blocks.AMETHYST_BLOCK, AME);
		//ores.put(Blocks.AMETHYST_CLUSTER, AME);
		ores.put(Blocks.ANCIENT_DEBRIS, RITE);
		ores.put(Blocks.BARREL, CHEST);
		ores.put(Blocks.BEACON, 0xffFFFFFF);
		ores.put(Blocks.CHEST, CHEST);
		ores.put(Blocks.COAL_BLOCK, COAL);
        ores.put(Blocks.COAL_ORE, COAL);
		//ores.put(Blocks.COPPER_BLOCK, COPPER);
		//ores.put(Blocks.COPPER_ORE, COPPER);
		ores.put(Blocks.CRYING_OBSIDIAN, OBSIDIAN);
		//ores.put(Blocks.DEEPSLATE_COAL_ORE, COAL);
		//ores.put(Blocks.DEEPSLATE_COPPER_ORE, COPPER);
		//ores.put(Blocks.DEEPSLATE_DIAMOND_ORE, DIAMOND);
		//ores.put(Blocks.DEEPSLATE_EMERALD_ORE, EMERALD);
		//ores.put(Blocks.DEEPSLATE_GOLD_ORE, GOLD);
		//ores.put(Blocks.DEEPSLATE_IRON_ORE, IRON);
		//ores.put(Blocks.DEEPSLATE_LAPIS_ORE, LAPIS);
		//ores.put(Blocks.DEEPSLATE_REDSTONE_ORE, REDSTONE);
		ores.put(Blocks.DIAMOND_BLOCK, DIAMOND);
		ores.put(Blocks.DIAMOND_ORE, DIAMOND);
		ores.put(Blocks.DISPENSER, INV);
		ores.put(Blocks.DROPPER, INV);
		ores.put(Blocks.EMERALD_BLOCK, EMERALD);
		ores.put(Blocks.EMERALD_ORE, EMERALD);
		ores.put(Blocks.ENDER_CHEST, 0xff976997);
		ores.put(Blocks.FURNACE, INV);
		ores.put(Blocks.GOLD_BLOCK, GOLD);
		ores.put(Blocks.GOLD_ORE, GOLD);
		ores.put(Blocks.HOPPER, INV);
		ores.put(Blocks.IRON_BLOCK, IRON);
		ores.put(Blocks.IRON_ORE, IRON);
		ores.put(Blocks.LAPIS_BLOCK, LAPIS);
		ores.put(Blocks.LAPIS_ORE, LAPIS);
		//ores.put(Blocks.LARGE_AMETHYST_BUD, AME);
		ores.put(Blocks.NETHERITE_BLOCK, RITE);
		ores.put(Blocks.NETHER_GOLD_ORE, GOLD);
		ores.put(Blocks.NETHER_QUARTZ_ORE, QUARTZ);
		ores.put(Blocks.OBSIDIAN, OBSIDIAN);
		ores.put(Blocks.QUARTZ_BLOCK, QUARTZ);
		//ores.put(Blocks.RAW_COPPER_BLOCK, COPPER);
		//ores.put(Blocks.RAW_GOLD_BLOCK, GOLD);
		//ores.put(Blocks.RAW_IRON_BLOCK, IRON);
		ores.put(Blocks.REDSTONE_BLOCK, REDSTONE);
		ores.put(Blocks.REDSTONE_ORE, REDSTONE);
		ores.put(Blocks.SHULKER_BOX, 0xff976997);
		ores.put(Blocks.SPAWNER, 0xff487573);
		ores.put(Blocks.TRAPPED_CHEST, CHEST);
	}

	
}
