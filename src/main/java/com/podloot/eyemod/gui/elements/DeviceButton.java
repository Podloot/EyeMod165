package com.podloot.eyemod.gui.elements;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.podloot.eyemod.lib.gui.EyeDraw;
import com.podloot.eyemod.lib.gui.util.Image;
import com.podloot.eyemod.lib.gui.widgets.EyeClickable;

public class DeviceButton extends EyeClickable {
	
	Image icon;

	public DeviceButton(int width, int height, Image icon) {
		super(width, height);
		this.icon = icon;
	}
	
	@Override
	public void draw(MatrixStack matrices, int x, int y) {
		EyeDraw.texture(matrices, icon, x, y, getWidth(), getHeight(), 0, 0, icon.width/2, icon.height, getPrimary());
		if(isHovered()) EyeDraw.texture(matrices, icon, x, y, getWidth(), getHeight(), icon.width/2, 0, icon.width/2, icon.height, 0xffFFFFFF);
	}

}
