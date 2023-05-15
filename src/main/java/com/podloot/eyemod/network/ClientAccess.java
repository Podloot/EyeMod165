package com.podloot.eyemod.network;

import com.podloot.eyemod.gui.GuiPhone;
import com.podloot.eyemod.gui.GuiRouter;
import com.podloot.eyemod.lib.gui.util.Pos;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;

@SuppressWarnings("resource")
public class ClientAccess {
	public static boolean openDevice(String id, int hand, int gui, boolean op, boolean operator, Pos pos) {
		if(Minecraft.getInstance().player.getScoreboardName().equals(id)) {
			Hand h = hand == 0 ? Hand.MAIN_HAND : Hand.OFF_HAND;
			if(gui == -1) {
				Minecraft.getInstance().setScreen(null);
			} else if(gui == 0) {
				Minecraft.getInstance().setScreen(new GuiPhone(h, new CompoundNBT(), op, operator));
			} else if(gui == 1) {
				Minecraft.getInstance().setScreen(new GuiRouter(h, pos , op));
			}
			return true;
		}
		return false;
	}

}
