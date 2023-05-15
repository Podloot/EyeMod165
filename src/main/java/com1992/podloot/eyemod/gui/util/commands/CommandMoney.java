package com.podloot.eyemod.gui.util.commands;

import com.podloot.eyemod.gui.GuiDevice;
import com.podloot.eyemod.lib.gui.EyeLib;

public class CommandMoney extends Command {

	public CommandMoney() {
		super("money", new String[] {"add", "set"}, false, true);
	}

	@Override
	public String run(String[] args, GuiDevice device, boolean op) {
		if(args.length <= 1) {
			int mon = device.data.getInt("money");
			return "Money: $" + mon;
		}
		switch(args[1]) {
		case "add":
			if(args.length <= 2) return "Use: money add <amount>";
			boolean s = device.addMoney(EyeLib.getInt(args[2]));
			return s ? "Added $" + EyeLib.getInt(args[2]) + " to your acount" : "Not enough items in your inventory";
		case "set":
			if(!op) return "You are not allowed to run this command";
			if(args.length <= 2) return "Use: money set <amount>";
			device.data.setInt("money", EyeLib.getInt(args[2]));
			return "Changed money to $" + EyeLib.getInt(args[2]);
		}
		return null;
	}

}
