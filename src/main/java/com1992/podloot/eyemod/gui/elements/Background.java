package com.podloot.eyemod.gui.elements;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.vertex.PoseStack;
import com.podloot.eyemod.gui.util.Naming.Type;
import com.podloot.eyemod.gui.util.Photos;
import com.podloot.eyemod.lib.gui.EyeDraw;
import com.podloot.eyemod.lib.gui.EyeLib;
import com.podloot.eyemod.lib.gui.widgets.EyeWidget;

import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;

public class Background extends EyeWidget {
	
	boolean isImage = false;
	int backColor = 0xffCECECE;
	String backImage = "";
	DynamicTexture image;
	

	public Background(int width, int height, Tag background) {
		super(false, width, height);
		if(background != null && background.getId() == Type.INT.type) {
			backColor = ((IntTag)background).getAsInt();
			isImage = false;
		} else if(background != null && background.getId() == Type.STRING.type) {
			backImage = ((StringTag)background).getAsString();
			NativeImage img = Photos.readShot(backImage);
			if(img != null) {
				image = new DynamicTexture(img);
				img.close();
			}
			isImage = true;
		}
	}

	@Override
	public void draw(PoseStack matrices, int x, int y) {
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
