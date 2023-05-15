package com.podloot.eyemod.lib.gui.widgets;

import com.podloot.eyemod.lib.gui.panels.EyeBoxPanel;
import com.podloot.eyemod.lib.gui.panels.EyeListPanel;
import com.podloot.eyemod.lib.gui.panels.EyeScrollPanel;

public class EyeList extends EyeScrollPanel {
	
	Axis axis;
	EyeBoxPanel list;
	EyeScrollBar bar;
	
	EyeListPanel selected;
	Runnable action;

	public EyeList(int width, int height, Axis axis) {
		super(width, height);
		this.axis = axis;
		boolean a = axis == Axis.HORIZONTAL;
		list = new EyeBoxPanel(width - (a ? 0 : 10), height - (a ? 10 : 0), axis);
		list.setSpace(2);
		bar  = new EyeScrollBar(a ? width : 8, a ? 8 : height, axis);
		bar.setColor(getPrimary(), getSecondary());
		add(list, 0, 0);
		add(bar, a ? 0 : (width-8), a ? (height-8) : 0);
		setScrollBar(axis, bar);
	}
	
	public void hideScrollbar(boolean h) {
		bar.hide(h);
	}
	
	public void clearSelection() {
		selected = null;
		for(EyeWidget w : list.getWidgets()) {
			if(w instanceof EyeListPanel) {
				((EyeListPanel)w).setSelected(false);
			}
		}
	}
	
	public void updateItems() {
		list.setItems();
		setScrollBar(getAxis(), bar);
		this.setScroll(Axis.HORIZONTAL, scroll_x);
		this.setScroll(Axis.VERTICAL, scroll_y);
	}
	
	public void add(EyeWidget e) {
		add(-1, e);
	}
	
	public void add(int i, EyeWidget e) {
		if(e == null) return;
		list.add(i, e);
		setScrollBar(getAxis(), bar);
	}
	
	public boolean delete(int i) {
		return list.delete(i);
	}
	
	public boolean delete(EyeWidget e) {
		return list.delete(e);
	}
	
	public void clearList() {
		list.clear();
	}
	
	public EyeWidget get(int i) {
		return list.get(i);
	}
	
	public boolean has(EyeWidget e) {
		return list.has(e);
	}

	public void setSpace(int s) {
		list.setSpace(s);
	}
	
	public int sizeList() {
		return list.size();
	}

	public void setScroll(int value) {
		setScroll(getAxis(), value);
	}

	public int getScroll() {
		return getAxis() == Axis.HORIZONTAL ? getScroll_x() : getScroll_y();
	}

	public Axis getAxis() {
		return axis;
	}
	
	public EyeListPanel getSelected() {
		return selected;
	}
	
	public void onSelect(Runnable action) {
		this.action = action;
	}

	public void setSelected(EyeListPanel sel) {
		selected = sel;
		if(action != null && sel != null) action.run();
	}

}
