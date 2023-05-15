package com.podloot.eyemod.lib.gui.widgets;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemStack;

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
	public void draw(PoseStack matrices, int x, int y) {
		renderer.renderGuiItem(item, x, y);
	}

}
