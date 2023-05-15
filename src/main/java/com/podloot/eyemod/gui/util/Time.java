package com.podloot.eyemod.gui.util;

import net.minecraft.client.Minecraft;

public class Time {
	
	public static String getTime() {
		return time(Minecraft.getInstance().level.dayTime());
	}
	
	public static String time(long time) {
		int hours = (int) ((time / 1000 + 8) % 24);
		int minutes = (int) (60 * (time % 1000) / 1000);
		return String.format("%02d:%02d", hours, minutes);
	}


}
