package com.podloot.eyemod.gui.util.commands;

import java.util.List;

import com.podloot.eyemod.gui.GuiDevice;
import com.podloot.eyemod.lib.gui.EyeLib;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class CommandWorld extends Command {

	public CommandWorld() {
		super("world", new String[] {"entities", "players"}, false);
		help += " [range]";
	}

	@Override
	public String run(String[] args, GuiDevice device, boolean op) {
		if(args.length <= 1) return help;		
		BlockPos bp = device.getUser().blockPosition();
		switch(args[1]) {
		case "entities":
			int range = 32;
			if(args.length > 2) range = EyeLib.getInt(args[2]);
			List<Entity> list = device.getUser().clientLevel.getEntities(Minecraft.getInstance().player, 
					new AxisAlignedBB(bp.offset(-range, -range, -range), bp.offset(range, range, range)));
			return "Entities within " + range + " blocks: " + list.size();
		case "players":
			List<? extends AbstractClientPlayerEntity> players = device.getUser().clientLevel.players();
			if(args.length > 2) {
				int prange = EyeLib.getInt(args[2]);
				return "Players within " + prange + " blocks: " + this.getPlayers(players, bp, prange);
			}
			return "Amount of players in world: " + players.size();
		}
		return null;
	}

	private int getPlayers(List<? extends AbstractClientPlayerEntity> players, BlockPos bp, int range) {
		int a = 0;
		for (AbstractClientPlayerEntity pe : players) {
			BlockPos pp = pe.blockPosition();
			int dx = Math.abs(pp.getX() - bp.getX());
			int dy = Math.abs(pp.getY() - bp.getY());
			int dz = Math.abs(pp.getZ() - bp.getZ());
			if(dx <= range && dy <= range && dz <= range) a++;
		}
		return a - 1;
	}

}
