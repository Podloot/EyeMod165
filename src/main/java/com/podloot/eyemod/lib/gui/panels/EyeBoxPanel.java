package com.podloot.eyemod.lib.gui.panels;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.podloot.eyemod.lib.gui.widgets.EyeWidget;

public class EyeBoxPanel extends EyePanel {
	
	Axis axis;
	int space = 0;
	
	public EyeBoxPanel(int width, int height, Axis axis) {
		this(width, height, axis, 4);
	}
	
	public EyeBoxPanel(int width, int height, Axis axis, int space) {
		super(width, height);
		this.axis = axis;
		this.space = space;
	}
	
	@Override
	public void draw(MatrixStack matrices, int x, int y) {
		super.draw(matrices, x, y);
	}
	
	public void add(int i, EyeWidget e) {
		boolean a = axis == Axis.HORIZONTAL;
		e.setSize(a ? e.getWidth() : this.getWidth(), a ? this.getHeight() : e.getHeight());
		e.setParent(this);
		if(i < 0 || i >= widgets.size()) {
			widgets.add(e);
		} else {
			widgets.add(i, e);
		}
		setItems();
	}
	
	public boolean delete(int i) {
		if(i >= 0 && i < widgets.size()) {
			widgets.remove(i);
			return true;
		} else {
			widgets.remove(widgets.size()-1);
			return false;
		}
	}
	
	public boolean delete(EyeWidget e) {
		return widgets.remove(e);
	}
	
	public EyeWidget get(int i) {
		if(i >= 0 && i < widgets.size()) {
			return widgets.get(i);
		} else {
			return null;
		}
	}
	
	public boolean has(EyeWidget e) {
		return widgets.contains(e);
	}
	
	public void setItems() {
		boolean a = axis == Axis.HORIZONTAL;
		int range = 0;
		for(int i = 0; i < widgets.size(); i++) {
			EyeWidget w = widgets.get(i);
			w.setPos(a ? range : 0, a ? 0 : range);
			range += (a ? w.getWidth() : w.getHeight()) + space;
		}
		setWidth(a ? range - space : getWidth());
		setHeight(a ? getHeight() : range - space);
	}

	public void setSpace(int s) {
		this.space = s;
		this.setItems();
	}
	
	
}
