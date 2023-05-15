package com.podloot.eyemod.gui.util.commands;

import com.podloot.eyemod.gui.GuiDevice;

public class CommandDevice extends Command {

	public CommandDevice() {
		super("device", new String[] {"reset", "storage", "battery", "close"}, false, true);
	}

	@Override
	public String run(String[] args, GuiDevice device, boolean op) {
		if(args.length <= 1) return help;
		switch(args[1]) {
		case "storage":
			int gb = device.data.getStorage(device.data.getNbt());
			int mgb = device.data.getInt("storage");
			return "Storage: " + gb + "GB / " + mgb + "GB";
		case "reset":
			device.clearAll();
			return "Device cleared";
		case "battery":
			int m = device.getItem().getMaxDamage();
			int b = m - device.getItem().getDamageValue();
			return "Battery: " + b + "/" + m;
		case "close":
			device.closeDevice();
			return "Closed device";
		}
		return null;
	}

}
