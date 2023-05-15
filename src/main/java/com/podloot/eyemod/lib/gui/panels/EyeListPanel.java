package com.podloot.eyemod.lib.gui.panels;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.podloot.eyemod.lib.gui.EyeDraw;
import com.podloot.eyemod.lib.gui.EyeLib;
import com.podloot.eyemod.lib.gui.widgets.EyeList;

public class EyeListPanel extends EyePanel {

	EyeList list;
	boolean selected;
	Object data;
	
	public EyeListPanel(EyeList list, int size) {
		super(list.getAxis() == Axis.HORIZONTAL ? size : list.getWidth()-10, list.getAxis() == Axis.VERTICAL ? size : list.getHeight()-10);
		this.list = list;
		this.setColor(0xffFFFFFF);
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		list.clearSelection();
		list.setSelected(this);
		selected = true;
		return super.mouseClicked(mouseX, mouseY, button);
	}
	
	@Override
	public void draw(MatrixStack matrices, int x, int y) {
		super.draw(matrices, x, y);
		if(selected) EyeDraw.nine(matrices, EyeLib.PLANE_BORDER, x, y, getWidth(), getHeight(), getPrimary());
	}
	
	public Object getData() {
		return data;
	}
	
	public void setData(Object data) {
		this.data = data;
	}

	public boolean isSelected() {
		return selected;
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

}
