package com.podloot.eyemod.gui.util.commands;

import com.podloot.eyemod.EyeApps;
import com.podloot.eyemod.EyeClient;
import com.podloot.eyemod.gui.GuiDevice;
import com.podloot.eyemod.gui.apps.App;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

public abstract class Command {
	
	ResourceLocation id;
	String cmd;
	String help;
	boolean isOP = false;
	boolean alwaysUsable = false;
	
	String[] sub;
	
	public Command(String cmd, String[] sub, boolean OP) {
		this(cmd, sub, OP, false);
	}
	
	public Command(String cmd, String[] sub, boolean OP, boolean alwaysUsable) {
		this.cmd = cmd;
		this.sub = sub;
		this.help = getHelpLine();
		this.isOP = OP;
		this.alwaysUsable = alwaysUsable;
	}
	
	public String getHelpLine() {
		String h = "Use: " + cmd + " <";
		for(String s : sub) {
			h += s + "/";
		}
		h = h.substring(0, h.length()-1);
		return h + ">";
	}
	
	public String execute(String[] args, GuiDevice device, boolean operator) {
		if(this.isOP && !operator) return "You are not allowed to run this command";
		if(!this.alwaysUsable && !device.isInstalled(EyeClient.APPCONSOLE.getId())) return "You need to install the app to use this command";
		return this.run(args, device, operator);
	}
	
	public abstract String run(String[] args, GuiDevice device, boolean op);
	
	public BlockPos getPos(String x, String y, String z) {
		BlockPos player = Minecraft.getInstance().player.blockPosition();
		return new BlockPos(getPosInt(x, player.getX()), getPosInt(y, player.getY()), getPosInt(z, player.getZ()));
	}
	
	private int getPosInt(String s, int tilt) {
		s = s.isEmpty() || s.equals("") || !s.matches("[0-9~-]+") ? "~" : s;
		if(s.contains("-")) s = "-" + s.replace("-", "");
		if(s.contains("~")) {
			String v = s.replace("~", "");
			return Integer.valueOf(v.equals("") ? "0" : v) + tilt;
		} else {
			return Integer.valueOf(s);
		}
	}
	
	
	
	
	public ResourceLocation getWorld(String world) {
		if(!world.contains(":")) {
			return new ResourceLocation("minecraft:" + world);
		} else {
			return new ResourceLocation(world);
		}
	}
	
	public ResourceLocation getApp(String id) {
		if(!id.contains(":")) {
			for(App a : EyeApps.getApps()) {
				if(a.getId().getPath().equals(id)) {
					return a.getId();
				}
			}
		}
		return new ResourceLocation(id);
	}
	
	
	public void setId(ResourceLocation id) {
		this.id = id;
	}
	
	public ResourceLocation getId() {
		return id;
	}
	
	public String getCommand() {
		return cmd;
	}

	public String getHelp() {
		return help;
	}

	public String[] getSub() {
		return sub;
	}

}
