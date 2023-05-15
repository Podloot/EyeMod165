package com.podloot.eyemod.gui.util.commands;

import com.podloot.eyemod.gui.GuiDevice;
import com.podloot.eyemod.lib.gui.util.Pos;

import net.minecraft.core.BlockPos;

public class CommandPlayer extends Command {

	public CommandPlayer() {
		super("player", new String[] {"xp", "coords"}, false);
	}

	@Override
	public String run(String[] args, GuiDevice device, boolean op) {
		if(args.length <= 1) return help;		
		BlockPos bp = device.getUser().blockPosition();
		
		switch(args[1]) {
		case "coords":
			Pos p = new Pos(bp, device.getWorldID());
			return "Coords: " + p.asString();
		case "xp":
			return "XP: " + device.getUser().totalExperience + " (" + device.getUser().experienceLevel + " levels)";
		}
		return null;
	}

}
