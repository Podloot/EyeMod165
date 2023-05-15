package com.podloot.eyemod.gui.elements.map;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.podloot.eyemod.lib.gui.EyeLib;
import com.podloot.eyemod.lib.gui.panels.EyePanel;
import com.podloot.eyemod.lib.gui.util.Color;
import com.podloot.eyemod.lib.gui.widgets.EyeWidget;

import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.core.BlockPos;

public class MapContent extends EyePanel {
	
	int zoom = 0;
	int w, h;
	Map map;
	BlockPos mapPos;
	DynamicTexture content;
	MapType type;
	MapItem selected;
	
	float ps = 0.5f;
	int mapSize = 64;
	int max = 2560;

	public MapContent(Map map, int width, int height, int mapSize, BlockPos mapPos, MapType type) {
		super(width, height);
		this.w = width;
		this.h = height;
		this.map = map;
		this.mapSize = mapSize;
		this.mapPos = mapPos;
		this.type = type;
		this.setMap();
		
		selected = new MapItem(EyeLib.MAP_SELECTED, mapPos, Color.RED);
		selected.setRotation(45);
		selected.hide(true);
		addItem(selected);
	}
	
	public void refresh(BlockPos mapPos) {
		type.set(mapPos);
		this.mapPos = mapPos;
		this.setMap();
	}
	
	public void refresh(BlockPos mapPos, MapType type) {
		this.type = type.set(mapPos);
		this.mapPos = mapPos;
		this.setMap();
	}
	
	@Override
	public void draw(PoseStack matrices, int x, int y) {
		//Map
		if(content != null) {
			RenderSystem.setShaderTexture(0, content.getId());
			RenderSystem.setShaderColor(1, 1, 1, 1);
			RenderSystem.enableBlend();
			GuiComponent.blit(matrices, x, y, getWidth(), getHeight(), 0, 0, mapSize, mapSize, mapSize, mapSize);
			RenderSystem.disableBlend();
		}
		super.draw(matrices, x, y);
	}
	
	@Override
	public void tick(int mx, int my) {
		this.updateItems();
		super.tick(mx, my);
	}
	
	public void setMap() {
		content = type.getMap(mapSize, mapSize);
	}
	
	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
		int pw = getWidth();
		int ph = getHeight();
		double d = (amount*32)*(Math.max(ps, 0.5F));
		int nw = (int)(getWidth() + d);
		int nh = (int)(getHeight() + d);
		int min = Math.max(map.getWidth(), map.getHeight());
		nw = nw < min ? min : nw > max ? max : nw;
		nh = nh < min ? min : nh > max ? max : nh;
		this.setSize(nw, nh);
		ps = (getWidth() / mapSize) / 2F;
		map.setScroll(Axis.HORIZONTAL, (int)(map.getScroll_x() + (nw - pw)/2));
		map.setScroll(Axis.VERTICAL, (int)(map.getScroll_y() + (nh - ph)/2));
		return true;
	}
	
	
	public void updateItems() {
		for(EyeWidget w : widgets) {
			if(w instanceof MapItem) {
				((MapItem) w).updatePos(getWidth(), getHeight(), mapSize, mapPos, ps);
			}
		}
	}
	
	@Override
	public void close() {
		if(content != null) content.close();
		super.close();
	}

	public void addItem(MapItem icon) {
		add(icon, 0, 0);
		BlockPos pos = icon.getPos();
		float nx = ((float)getWidth() / (float)mapSize) * (float)(pos.getX() - mapPos.getX() + mapSize/2F);
		float nz = ((float)getHeight() / (float)mapSize) * (float)(pos.getZ() - mapPos.getZ() + mapSize/2F);
		icon.setPos((int)nx, (int)nz);
	}
	
	public BlockPos getBlockPos(double mouseX, double mouseY) {
		double px = ((float)mapSize / (float)getWidth()) * mouseX;
		double pz = ((float)mapSize / (float)getHeight()) * mouseY;
		int x = (int) (px + mapPos.getX() - mapSize/2);
		int z = (int) (pz + mapPos.getZ() - mapSize/2);
		x -= (x < 0 ? 1 : 0);
		z -= (z < 0 ? 1 : 0);
		return new BlockPos(x, type.getHeight(x, z), z);
	}
	
	public BlockPos getMapPos() {
		return mapPos;
	}

}
