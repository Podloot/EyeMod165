package com.podloot.eyemod.gui.elements;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.podloot.eyemod.lib.gui.EyeDraw;
import com.podloot.eyemod.lib.gui.EyeLib;
import com.podloot.eyemod.lib.gui.widgets.EyeWidget;

public class Border extends EyeWidget {
		
	public Border(int width, int height, int color) {
		super(false, width, height);
		setColor(color);
	}

	@Override
	public void draw(MatrixStack matrices, int x, int y) {
		EyeDraw.nine(matrices, EyeLib.DEVICE, x, y, getWidth(), getHeight(), getPrimary());
	}

}
