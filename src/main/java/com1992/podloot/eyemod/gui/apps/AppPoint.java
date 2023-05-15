package com.podloot.eyemod.gui.apps;

import com.podloot.eyemod.lib.gui.util.Pos;
import com.podloot.eyemod.lib.gui.util.Space;
import com.podloot.eyemod.lib.gui.widgets.EyeList;
import com.podloot.eyemod.lib.gui.widgets.EyeWidget;

import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;

public abstract class AppPoint extends App {
	
	public EyeList list = null;
	int listheight;
	ListTag points;
	Space space;
	
	public AppPoint(ResourceLocation icon, int color, String creator) {
		super(icon, color, creator);
		
	}
	
	public void setList(Space space, ListTag points) {
		this.space = space;
		this.points = points;
	}
	
	public boolean load() {
		loadList();
		return true;
	}
	
	public void loadList() {
		if(list != null) {
			list.clearList();
			remove(list);
		}
		list = new EyeList(space.width, space.height, Axis.VERTICAL);
		for (int i = 0; i < points.size(); i++) {
			String pos = points.getString(i);
			list.add(getPanel(i, new Pos().fromString(pos)));

		}
		add(list, space.x, space.y);
	}
	
	public abstract EyeWidget getPanel(int index, Pos pos);
	
	

}
