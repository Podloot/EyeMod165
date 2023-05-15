package com.podloot.eyemod.gui.panes;

import com.podloot.eyemod.gui.apps.App;
import com.podloot.eyemod.gui.elements.map.Map;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

public class MapPane extends Pane {
	
	Map map;

	public MapPane(App app, int width, int height) {
		super(app, width, height);
		map = new Map(width-4, height-20, 128, app.getDevice().getUser().blockPosition(), app.getDevice().getUser().level);
		map.addPlayer(app.getDevice().getUser());
		add(map, 2, 2);
		
		this.addAccept(0);
		this.addCancel(1);
	}
	
	public BlockPos getPos() {
		return map.getSelected();
	}
	
	public ResourceLocation getWorld() {
		return map.getWorld();
	}

}
