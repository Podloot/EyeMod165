package com.podloot.eyemod.gui.elements.map;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.podloot.eyemod.gui.util.Naming.Dim;
import com.podloot.eyemod.lib.gui.EyeDraw;
import com.podloot.eyemod.lib.gui.EyeLib;
import com.podloot.eyemod.lib.gui.panels.EyeScrollPanel;
import com.podloot.eyemod.lib.gui.util.Color;
import com.podloot.eyemod.lib.gui.util.Line;
import com.podloot.eyemod.lib.gui.util.Pos;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Map extends EyeScrollPanel {
	
	MapContent content;
	boolean dragging;
	double last_x, last_y, drag_x, drag_y;
	boolean showSelectedPos = true;


	public Map(int width, int height, int mapSize, BlockPos mapPos, MapType type) {
		super(width, height);
		type.set(mapPos);
		this.load(mapSize, mapPos, type);
	}
	
	public Map(int width, int height, int mapSize, BlockPos mapPos, World world) {
		super(width, height);
		MapType type = new MapType(world).set(mapPos);
		ResourceLocation wid = world.dimension().location();
		if(wid.equals(Dim.OVERWORLD.id)) type = new MapTypeOverworld(world).set(mapPos);
		if(wid.equals(Dim.NETHER.id)) type.setRange(0, mapPos.getY()+2, 70, 20);
		if(wid.equals(Dim.END.id)) type.setRange(world.getMaxBuildHeight(), mapPos.getY()+40, 40, 40);
		this.load(mapSize, mapPos, type);
	}
	
	public void load(int mapSize, BlockPos mapPos, MapType type) {
		int min = Math.max(getWidth(), getHeight());
		content = new MapContent(this, Math.max(mapSize, min), Math.max(mapSize, min), mapSize, mapPos, type);
		add(content, 0, 0);
		
		this.setScroll(Axis.HORIZONTAL, content.getWidth()/2 - getWidth()/2);
		this.setScroll(Axis.VERTICAL, content.getWidth()/2 - getHeight()/2);
	}
	
	public void addPlayer(ClientPlayerEntity player) {
		MapEntity icon = new MapEntity(EyeLib.MAP_PLAYER, player, 0xffFFFFFF);
		content.addItem(icon);
	}
	
	public void addPlayers() {
		for (AbstractClientPlayerEntity point : Minecraft.getInstance().level.players()) {
			if(!point.getName().equals(Minecraft.getInstance().player.getName())) {
				MapEntity icon = new MapEntity(EyeLib.MAP_PLAYER, Minecraft.getInstance().level.getPlayerByUUID(point.getUUID()), 0xff999999);
				icon.setName(point.getScoreboardName());
				content.addItem(icon);
			}
		}
	}
	
	public void addWaypoints(ListNBT points) {
		for(int i = 0; i < points.size(); i++) {
			Pos pos = new Pos().fromString(points.getString(i));
			if(pos.getWorld().equals(content.type.getWorldID())) {
				if(pos.getPos().distManhattan(content.mapPos) < content.mapSize*2) {
					MapItem waypoint = new MapItem(EyeLib.MAP_LOCATION, pos.getPos(), Color.WHITE);
					waypoint.off = 4;
					waypoint.setName(pos.getName());
					content.addItem(waypoint);
				}	
			}
		}
	}
	
	@Override
	public void draw(MatrixStack matrices, int x, int y) {
		EyeDraw.rect(matrices, x, y, getWidth(), getHeight(), 0xff000000);
		
		super.draw(matrices, x, y);
		
		if(showSelectedPos && content.selected != null) {
			BlockPos selected = content.selected.pos;
			if(selected != null) {
				EyeDraw.text(matrices, new Line(selected.getX() + "/" + selected.getY() + "/" + selected.getZ()).setScale(0.75F), x + getWidth()/2, y + 6);
			}
		}
		EyeDraw.nine(matrices, EyeLib.MAP_BORDER, x, y, getWidth(), getHeight(), 0xffFFFFFF);
		
	}
	
	
	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
		return content.mouseScrolled(mouseX, mouseY, amount);
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		dragging = true;
		last_x = mouseX;
		last_y = mouseY;
		drag_x = mouseX;
		drag_y = mouseY;
		return true;
	}
	
	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		if(!isDragged(4)) {
			content.selected.hide(false);
			content.selected.setBlockPos(content.getBlockPos(mouseX - content.getGlobalX(), mouseY - content.getGlobalY()));
			content.selected.updatePos(getWidth(), getHeight(), content.mapSize, content.mapPos, content.ps);
		}
		dragging = false;
		return true;
	}
	
	@Override
	public void tick(int mx, int my) {
		if(dragging) {
			this.setScroll(Axis.HORIZONTAL, (int) (getScroll_x() + (drag_x - mx)));
			this.setScroll(Axis.VERTICAL, (int) (getScroll_y() + (drag_y - my)));
			drag_x = mx;
			drag_y = my;
		}
		super.tick(mx, my);
	}
	
	public boolean isDragged(int distance) {
		if(Math.abs(drag_x - last_x) < distance && Math.abs(drag_y - last_y) < distance) return false;
		return dragging;
	}
	
	public MapContent getContent() {
		return content;
	}

	public BlockPos getSelected() {
		if(content == null || content.selected == null) return null;
		return content.selected.pos;
	}

	public ResourceLocation getWorld() {
		return content.type.world.dimension().location();
	}

}
