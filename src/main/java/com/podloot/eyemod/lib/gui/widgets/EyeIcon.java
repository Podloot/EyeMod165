package com.podloot.eyemod.lib.gui.widgets;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.podloot.eyemod.lib.gui.EyeDraw;
import com.podloot.eyemod.lib.gui.util.Image;

public class EyeIcon extends EyeWidget {
	
	Image image;

	public EyeIcon(int width, int height, Image image) {
		super(false, width, height);
		this.image = image;
		this.secondary = 0xffFFFFFF;
	}

	@Override
	public void draw(MatrixStack matrices, int x, int y) {
		super.draw(matrices, x, y);
		EyeDraw.texture(matrices, image, x, y, width, height, secondary);
	}
}
