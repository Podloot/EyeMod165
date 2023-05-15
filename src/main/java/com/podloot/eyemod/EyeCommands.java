package com.podloot.eyemod;

import java.util.Collection;
import java.util.HashMap;

import com.podloot.eyemod.gui.util.commands.Command;

import net.minecraft.util.ResourceLocation;

public class EyeCommands {
	
	public static HashMap<ResourceLocation, Command> COMMANDS = new HashMap<ResourceLocation, Command>();
	
	/**
	 * Registers command (important: only use on client side)
	 * @param id
	 * @param cmd
	 */
	public static void register(ResourceLocation id, Command cmd) {
		cmd.setId(id);
		COMMANDS.put(id, cmd);
	}
	
	public static Command getCommand(ResourceLocation id) {
		if(!COMMANDS.containsKey(id)) return null;
		return COMMANDS.get(id);
	}
	
	public static Collection<Command> getCommands() {
		return COMMANDS.values();
	}
}
