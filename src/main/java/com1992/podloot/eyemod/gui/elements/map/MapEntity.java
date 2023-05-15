package com.podloot.eyemod.gui.elements.map;

import com.podloot.eyemod.lib.gui.util.Image;

import net.minecraft.world.entity.Entity;

public class MapEntity extends MapItem {
	
	Entity entity;
	boolean frozen = false;

	public MapEntity(Image icon, Entity entity, int color) {
		super(icon, entity.blockPosition(), color);
		this.entity = entity;
	}
	
	public MapEntity setStatic() {
		this.frozen = true;
		return this;
	}
	
	@Override
	public void tick(int mx, int my) {
		if(!frozen) {
			this.pos = entity.blockPosition();
			this.rotation = (int) entity.yRotO;
		}
		super.tick(mx, my);
	}

}
