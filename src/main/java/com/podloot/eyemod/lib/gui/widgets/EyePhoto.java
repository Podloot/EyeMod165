package com.podloot.eyemod.lib.gui.widgets;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.podloot.eyemod.gui.util.Photos;
import com.podloot.eyemod.lib.gui.EyeDraw;
import com.podloot.eyemod.lib.gui.EyeLib;

import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.NativeImage;

public class EyePhoto extends EyeWidget {
	
	DynamicTexture photo;

	public EyePhoto(int width, int height, String photo) {
		super(false, width, height);
		NativeImage img = Photos.readShot(photo);
		if(img != null) {
			this.photo = new DynamicTexture(img);
		}
		img.close();
	}

	@Override
	public void draw(MatrixStack matrices, int x, int y) {
		if(photo != null) EyeDraw.texture(matrices, photo, x, y, width, height);
		EyeDraw.nine(matrices, EyeLib.MAP_BORDER, x, y, width, height, 0xffFFFFFF);
	}
	
	@Override
	public void close() {
		if(photo != null) photo.close();
		super.close();
	}

}
