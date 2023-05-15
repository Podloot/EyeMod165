package com.podloot.eyemod.gui.elements;

import java.util.HashMap;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.podloot.eyemod.gui.util.Naming.Dim;
import com.podloot.eyemod.lib.gui.EyeDraw;
import com.podloot.eyemod.lib.gui.EyeLib;
import com.podloot.eyemod.lib.gui.util.Image;
import com.podloot.eyemod.lib.gui.widgets.EyeClickable;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class DimensionButton extends EyeClickable {
	
	ResourceLocation dimension = Dim.OVERWORLD.id;
	
	HashMap<ResourceLocation, Image> dimensions = new HashMap<ResourceLocation, Image>();
	
	public DimensionButton(int width, int height, ResourceLocation dim) {
		super(width, height);
		setColor(0xffFFFFFF);
		this.dimension = dim;
		dimensions.put(Dim.OVERWORLD.id, EyeLib.OVERWOLD);
		dimensions.put(Dim.NETHER.id, EyeLib.THE_NETHER);
		dimensions.put(Dim.END.id, EyeLib.THE_END);
		
		this.setAction(() -> {
			if(dimension == Dim.OVERWORLD.id) {
				dimension = Dim.NETHER.id;
			} else if(dimension == Dim.NETHER.id) {
				dimension = Dim.END.id;
			} else if(dimension == Dim.END.id) {
				dimension = Dim.OVERWORLD.id;
			} else {
				dimension = Dim.OVERWORLD.id;
			}
		});
		
	}
	
	public DimensionButton(int width, int height, World world) {
		this(width, height, world.dimension().location());
	}
	
	public ResourceLocation getDimension() {
		return dimension;
	}
	
	public void setDimension(ResourceLocation world) {
		dimension = world;
	}
	
	@Override
	public void draw(MatrixStack matrices, int x, int y) {
		EyeDraw.texture(matrices, getIcon(), x, y, getWidth(), getHeight(), getPrimary());
		EyeDraw.nine(matrices, EyeLib.PLANE_BORDER, x, y, getWidth(), getHeight(), this.isHovered() ? 0xffFFFFFF : 0xff000000);
	}
	
	private Image getIcon() {
		if(dimensions.containsKey(dimension)) {
			return dimensions.get(dimension);
		} else {
			return EyeLib.DIMENSION;
		}
	}
}
