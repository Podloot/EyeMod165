package com.podloot.eyemod.lib.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.podloot.eyemod.lib.gui.util.Image;
import com.podloot.eyemod.lib.gui.util.Line;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class EyeDraw {
		
	public static void texture(PoseStack matrices, Image texture, int x, int y, int w, int h) {
		texture(matrices, texture, x, y, w, h, 0, 0, texture.width, texture.height, 0xffFFFFFF);
	}

	public static void texture(PoseStack matrices, Image texture, int x, int y, int w, int h, int color) {
		texture(matrices, texture, x, y, w, h, 0, 0, texture.width, texture.height, color);
	}
	
	public static void texture(PoseStack matrices, DynamicTexture texture, int x, int y, int w, int h) {
		RenderSystem.enableBlend();
		RenderSystem.setShaderTexture(0, texture.getId());
		GuiComponent.blit(matrices, x, y, 0, 0, w, h, w, h);
		RenderSystem.disableBlend();
	}
	
	
	public static void texture(PoseStack matrices, Image texture, int x, int y, int w, int h, int tx, int ty, int rw, int rh, int color) {
		RenderSystem.enableBlend();
		RenderSystem.setShaderTexture(0, texture.id);
		float a = (float) (color >> 24 & 0xFF) / 255.0f;
		float r = (float) (color >> 16 & 0xFF) / 255.0f;
		float g = (float) (color >> 8 & 0xFF) / 255.0f;
		float b = (float) (color & 0xFF) / 255.0f;
		RenderSystem.setShaderColor(r, g, b, a);
		GuiComponent.blit(matrices, x, y, w, h, tx, ty, rw, rh, texture.width, texture.height);
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.disableBlend();
	}
	
	/**
	 * Draws a nine-patched texture based on a 256x256 template
	 * Max size = (2*side) - (2*border)
	 **/
	public static void nine(PoseStack matrices, Image texture, int x, int y, int w, int h, int color) {
		int hw = w/2;
		int hh = h/2;
		texture(matrices, texture, x, y, hw, hh, 0, 0, hw, hh, color); //topleft
		texture(matrices, texture, x+hw, y, hw, hh, 256-hw, 0, hw, hh, color); //topright
		texture(matrices, texture, x, y+hh, hw, hh, 0, 256-hh, hw, hh, color); //bottemleft
		texture(matrices, texture, x+hw, y+hh, hw, hh, 256-hw, 256-hh, hw, hh, color); //bottemright
	}
	
	public static void rect(PoseStack matrices, int x, int y, int w, int h, int color) {
		GuiComponent.fill(matrices, x, y, x+w, y+h, color);
	}

	public static void text(PoseStack matrices, Line text, int x, int y) {
		MutableComponent t = text.asText();
		int tw = (Minecraft.getInstance().font.width(t)/2)*(text.getCenteredX()-1);
		int th = (int)((4.5F)*(text.getCenteredY()-1));
	
		if(text.isBold()) t.getStyle().applyFormat(ChatFormatting.BOLD);
		if(text.isItalic()) t.getStyle().applyFormat(ChatFormatting.ITALIC);
		matrices.pushPose();
		matrices.translate(x + (tw*text.getScale()), y + th*text.getScale(), 0);
		matrices.scale(text.getScale(), text.getScale(), 1);
		Minecraft.getInstance().font.draw(matrices, t, 0 , 0, text.getColor());
		matrices.popPose();
	}
	
	

	public static void text(PoseStack matrices, String text, int x, int y, int color) {
		Minecraft.getInstance().font.draw(matrices, Component.literal(text), x, y, color);
	}

	
}
