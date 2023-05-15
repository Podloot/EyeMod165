package com.podloot.eyemod.network;

import com.podloot.eyemod.gui.GuiPhone;
import com.podloot.eyemod.gui.GuiRouter;
import com.podloot.eyemod.lib.gui.util.Pos;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;

@SuppressWarnings("resource")
public class ClientAccess {
	public static boolean openDevice(String id, int hand, int gui, CompoundTag config, boolean op, boolean operator, Pos pos) {
		if(Minecraft.getInstance().player.getScoreboardName().equals(id)) {
			InteractionHand h = hand == 0 ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
			if(gui == -1) {
				Minecraft.getInstance().setScreen(null);
			} else if(gui == 0) {
				try {
					Minecraft.getInstance().setScreen(new GuiPhone(h, config, op, operator));
				} catch(Exception e) {
					e.printStackTrace();
				}
			} else if(gui == 1) {
				Minecraft.getInstance().setScreen(new GuiRouter(h, pos , op));
			}
			return true;
		}
		return false;
	}

}
