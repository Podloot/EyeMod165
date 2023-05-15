package com.podloot.eyemod.lib.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.podloot.eyemod.lib.gui.util.Image;
import com.podloot.eyemod.lib.gui.util.Line;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

public class EyeDraw {
		
	public static void texture(MatrixStack matrices, Image texture, int x, int y, int w, int h) {
		texture(matrices, texture, x, y, w, h, 0, 0, texture.width, texture.height, 0xffFFFFFF);
	}

	public static void texture(MatrixStack matrices, Image texture, int x, int y, int w, int h, int color) {
		texture(matrices, texture, x, y, w, h, 0, 0, texture.width, texture.height, color);
	}
	
	public static void texture(MatrixStack matrices, DynamicTexture texture, int x, int y, int w, int h) {
		RenderSystem.enableBlend();
		RenderSystem.bindTexture(texture.getId());
		Screen.blit(matrices, x, y, 0, 0, w, h, w, h);
		RenderSystem.disableBlend();
	}
	
	
	public static void texture(MatrixStack matrices, Image texture, int x, int y, int w, int h, int tx, int ty, int rw, int rh, int color) {
		RenderSystem.enableBlend();
		Minecraft.getInstance().getTextureManager().bind(texture.id);
		float a = (float) (color >> 24 & 0xFF) / 255.0f;
		float r = (float) (color >> 16 & 0xFF) / 255.0f;
		float g = (float) (color >> 8 & 0xFF) / 255.0f;
		float b = (float) (color & 0xFF) / 255.0f;
		RenderSystem.color4f(r, g, b, a);
		Screen.blit(matrices, x, y, w, h, tx, ty, rw, rh, texture.width, texture.height);
		RenderSystem.color4f(1, 1, 1, 1);
		RenderSystem.disableBlend();
	}
	
	/**
	 * Draws a nine-patched texture based on a 256x256 template
	 * Max size = (2*side) - (2*border)
	 **/
	public static void nine(MatrixStack matrices, Image texture, int x, int y, int w, int h, int color) {
		int hw = w/2;
		int hh = h/2;
		texture(matrices, texture, x, y, hw, hh, 0, 0, hw, hh, color); //topleft
		texture(matrices, texture, x+hw, y, hw, hh, 256-hw, 0, hw, hh, color); //topright
		texture(matrices, texture, x, y+hh, hw, hh, 0, 256-hh, hw, hh, color); //bottemleft
		texture(matrices, texture, x+hw, y+hh, hw, hh, 256-hw, 256-hh, hw, hh, color); //bottemright
	}
	
	public static void rect(MatrixStack matrices, int x, int y, int w, int h, int color) {
		Screen.fill(matrices, x, y, x+w, y+h, color);
	}

	public static void text(MatrixStack matrices, Line text, int x, int y) {
		ITextComponent t = text.asText();
		int tw = (Minecraft.getInstance().font.width(t)/2)*(text.getCenteredX()-1);
		int th = (int)((4.5F)*(text.getCenteredY()-1));
	
		if(text.isBold()) t.getStyle().applyFormat(TextFormatting.BOLD);
		if(text.isItalic()) t.getStyle().applyFormat(TextFormatting.ITALIC);
		matrices.pushPose();
		matrices.translate(x + (tw*text.getScale()), y + th*text.getScale(), 0);
		matrices.scale(text.getScale(), text.getScale(), 1);
		Minecraft.getInstance().font.draw(matrices, t, 0 , 0, text.getColor());
		matrices.popPose();
	}
	
	

	public static void text(MatrixStack matrices, String text, int x, int y, int color) {
		Minecraft.getInstance().font.draw(matrices, ITextComponent.nullToEmpty(text), x, y, color);
	}

	
}
