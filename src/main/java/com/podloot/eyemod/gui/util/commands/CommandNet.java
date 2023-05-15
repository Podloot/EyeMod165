package com.podloot.eyemod.gui.util.commands;

import com.podloot.eyemod.blocks.entities.RouterEntity;
import com.podloot.eyemod.gui.GuiDevice;
import com.podloot.eyemod.lib.gui.util.Pos;

public class CommandNet extends Command {

	public CommandNet() {
		super("net", new String[] {"info", "disconnect", "data"}, false, true);
	}

	@Override
	public String run(String[] args, GuiDevice device, boolean op) {
		if(args.length <= 1) return help;
		switch(args[1]) {
		case "info":
			Pos router = device.data.getPos("router");
			return router == null ? "Not connected to a router" : "Connected to: " + router.asString();
		case "disconnect":
			if(device.data.has("router")) {
				device.data.remove("net");
				device.data.remove("router");
				return "Disconnected from the EyeNet";
			}
			return "No connection was found";
		case "data":
			if(device.connect.isRouter(device.connect.getRouter())) {
				RouterEntity be = device.connect.getRouterData();
				return "Router storage:|- data: " + be.storage + "/" + be.max_storage + "GB|- unsend messages: " + be.messages.size() + "/" + be.max_storage + "GB";
			}
			return "No router could be found";
		}
		return null;
	}
	
	public String getConnectionDistance(GuiDevice device) {
		int d = device.connect.getDistance();
		int md = device.connect.max_distance;
		if(d == -1 || d > md) return "None";
		else return d + "/" + md;
	}

}
