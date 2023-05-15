package com.podloot.eyemod.gui.util.commands;

import com.podloot.eyemod.EyeClient;
import com.podloot.eyemod.EyeCommands;
import com.podloot.eyemod.gui.GuiDevice;

public class CommandHelp extends Command {

	public CommandHelp() {
		super("help", new String[] {}, false, true);
		sub = new String[EyeCommands.getCommands().size()];
		int i = 0;
		for(Command c : EyeCommands.getCommands()) {
			if(sub.length < i) {
				sub[i] = c.cmd;
			}
			i++;
		}
		help = "Use: help <command>";
	}

	@Override
	public String run(String[] args, GuiDevice device, boolean op) {
		if(args.length == 1) {
			String commands = "Use: help <command> | Commands:";
			for(Command c : EyeCommands.getCommands()) {
				if(!(!c.alwaysUsable && !device.isInstalled(EyeClient.APPCONSOLE.getId()))) {
					commands += " | - " + c.getCommand() + (c.isOP ? "*" : "");
				}
			}
			commands += " | Note: * = operators only";
			return commands;
		} else if(args.length >= 2) {
			Command toHelp = this.getCmd(args[1]);
			if(toHelp != null) {
				return toHelp.getHelp();
			} else {
				return "Non existing command";
			}
		}
		return null;
	}
	
	public Command getCmd(String cmd) {
		for(Command c : EyeCommands.getCommands()) {
			if(c.getCommand().equals(cmd)) {
				return c;
			}
		}
		return null;
	}

}
