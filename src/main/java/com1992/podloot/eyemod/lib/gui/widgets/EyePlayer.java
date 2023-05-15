package com.podloot.eyemod.lib.gui.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import com.podloot.eyemod.lib.gui.EyeDraw;
import com.podloot.eyemod.lib.gui.EyeLib;
import com.podloot.eyemod.lib.gui.util.Color;
import com.podloot.eyemod.lib.gui.util.Image;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;

public class EyePlayer extends EyeWidget {
	
	PlayerInfo entry;
	Image skin;
	
	public EyePlayer(int width, int height, String name) {
		super(false, width, height);
		entry = this.getPlayer(name);
		if(entry != null) skin = new Image(entry.getSkinLocation(), 64, 64);
	}
	
	public PlayerInfo getPlayer() {
		return entry;
	}
	
	@Override
	public void draw(PoseStack matrices, int x, int y) {
		if(entry != null) {
			EyeDraw.texture(matrices, skin, x+1, y+1, width-2, height-2, 8, 8, 8, 8, 0xffFFFFFF);
		} else {
			EyeDraw.texture(matrices, EyeLib.STEVE, x+1, y+1, width-2, height-2, 0xffFFFFFF);
		}
		EyeDraw.nine(matrices, EyeLib.PLANE_BORDER, x, y, width, height, entry != null ? Color.GREEN : Color.RED);
	}
	
	public PlayerInfo getPlayer(String name) {
		return Minecraft.getInstance().getConnection().getPlayerInfo(name);
	}

}
