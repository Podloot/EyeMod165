package com.podloot.eyemod.gui.util.commands;

import com.podloot.eyemod.gui.GuiDevice;
import com.podloot.eyemod.gui.util.Naming.Type;

import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;

public class CommandApp extends Command {

	public CommandApp() {
		super("app", new String[] {"close", "open", "clear", "install", "deinstall", "list"} , false, true);
	}

	@Override
	public String run(String[] args, GuiDevice device, boolean op) {
		if(args.length <= 1) return help;
		switch(args[1]) {
		case "clear":
			if(args.length <= 2) return "Use: device clear <app>";
			ResourceLocation toClear = getApp(args[2]);
			if(device.loaded_apps.containsKey(toClear)) {
				device.loaded_apps.get(toClear).onClearData();
				return "App cleared";
			} else {
				return "Non existing app";
			}
		case "open":
			if(args.length <= 2) return "Use: device open <app>";
			ResourceLocation toOpen = getApp(args[2]);
			if(device.loaded_apps.containsKey(toOpen)) {
				boolean opened = device.openApp(device.loaded_apps.get(toOpen));
				if(opened) {
					return "App opened";
				} else {
					return "Failed to open app";
				}
			} else {
				return "Non existing app";
			}
		case "deinstall":
			if(args.length <= 2) return "Use: device deinstall <app>";
			ResourceLocation toRemove = getApp(args[2]);
			if(device.loaded_apps.containsKey(toRemove)) {
				if(!device.isInstalled(toRemove)) {
					return "App is already deinstalled";
				} else {
					device.deinstallApp(toRemove);
					return "App deinstalled";
				}
				
			} else {
				return "Non existing app";
			}
		case "install":
			if(args.length <= 2) return "Use: device install <app>";
			ResourceLocation toInstall = getApp(args[2]);
			if(device.loaded_apps.containsKey(toInstall)) {
				if(device.isInstalled(toInstall)) {
					return "App is already installed";
				} else {
					boolean ins = device.installApp(toInstall);
					return ins ? "App installed" : "You do not have enough money";
				}
			} else {
				return "Non existing app";
			}
		case "close":
			device.openHome();
			return "Console closed";
		case "list":
			ListTag l = device.data.getList("apps", Type.STRING);
			String list = "Installed apps: \n";
			for(int i = 0; i < l.size(); i++) {
				list += "- " + l.getString(i) + "\n";
			}
			return list;
		}
		return null;
	}

}
