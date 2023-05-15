package com.podloot.eyemod.gui.util.commands;

import com.podloot.eyemod.EyeCommands;
import com.podloot.eyemod.gui.GuiDevice;

public class CommandManager {
	
	GuiDevice device;
	boolean operator;
	
	public CommandManager(GuiDevice device, boolean operator) {
		this.device = device;
		this.operator = operator;
	}
	
	public String execute(String msg) {
		String[] cmd = this.getArgs(msg);
		if(cmd.length <= 0) return "Invalid command";
		Command command = this.getCmd(cmd[0]);
		if(command == null) return "Non existing command";
		String out = command.execute(cmd, device, operator);
		return out == null ? "Invalid command" : out;
	}
	
	public String[] getArgs(String msg) {
		msg = msg.toLowerCase();
		if(msg.contains(" ")) {
			return msg.split(" ");
		} else {
			return new String[] {msg};
		}
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
