package com.podloot.eyemod.gui.elements;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.podloot.eyemod.gui.util.Naming.Type;
import com.podloot.eyemod.gui.util.Photos;
import com.podloot.eyemod.lib.gui.EyeDraw;
import com.podloot.eyemod.lib.gui.EyeLib;
import com.podloot.eyemod.lib.gui.widgets.EyeWidget;

import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.nbt.StringNBT;

public class Background extends EyeWidget {
	
	boolean isImage = false;
	int backColor = 0xffCECECE;
	String backImage = "";
	DynamicTexture image;
	

	public Background(int width, int height, INBT background) {
		super(false, width, height);
		if(background != null && background.getId() == Type.INT.type) {
			backColor = ((IntNBT)background).getAsInt();
			isImage = false;
		} else if(background != null && background.getId() == Type.STRING.type) {
			backImage = ((StringNBT)background).getAsString();
			NativeImage img = Photos.readShot(backImage);
			if(img != null) {
				image = new DynamicTexture(img);
				img.close();
			}
			isImage = true;
		}
	}

	@Override
	public void draw(MatrixStack matrices, int x, int y) {
		if(!isImage) {
			EyeDraw.rect(matrices, x, y, getWidth(), getHeight(), backColor);
		} else {
			if(image != null) {
				EyeDraw.texture(matrices, image, x, y, getWidth(), getHeight());
			} else {
				EyeDraw.texture(matrices, EyeLib.DEFAULT, x, y, getWidth(), getHeight(), 0xffCECECE);
			}
		}
	}
	
	@Override
	public void close() {
		if(image != null) image.close();
		super.close();
	}

}
