package com.podloot.eyemod.lib.gui.widgets;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;

public class EyeItem extends EyeWidget {
	
	ItemStack item;
	ItemRenderer renderer;

	public EyeItem(int width, int height, ItemStack item) {
		super(false, width, height);
		renderer = Minecraft.getInstance().getItemRenderer();
		setItem(item);
	}
	
	public void setItem(ItemStack item) {
		this.item = item;
	}
	
	public ItemStack getItem() {
		return item;
	}

	@Override
	public void draw(MatrixStack matrices, int x, int y) {
		renderer.renderGuiItem(item, x, y);
	}

}
