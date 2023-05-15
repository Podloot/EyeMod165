package com.podloot.eyemod.lib.gui.panels;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;
import com.podloot.eyemod.lib.gui.widgets.EyeWidget;

public class EyePanel extends EyeWidget {
	
	protected List<EyeWidget> widgets = new ArrayList<EyeWidget>();
	
	public EyePanel(int width, int height) {
		super(true, width, height);
	}
	
	public void add(EyeWidget e, int x, int y) {
		e.setPos(x, y);
		e.setParent(this);
		widgets.add(e);
	}
	
	public void replace(int i, EyeWidget e) {
		if(widgets.size() > i) {
			e.setParent(this);
			e.setPos(widgets.get(i).getX(), widgets.get(i).getY());
			widgets.set(i, e);
		}
	}
	
	public void remove(EyeWidget e) {
		e.close();
		widgets.remove(e);
	}
	
	public void remove(int i) {
		if(widgets.size() > i && i >= 0) {
			widgets.get(i).close();
			widgets.remove(i);
		}
	}
	
	public boolean has(EyeWidget e) {
		return widgets.contains(e);
	}
	
	public int size() {
		return widgets.size();
	}
	
	public EyeWidget get(int i) {
		if(widgets.size() > i) return widgets.get(i);
		return null;
	}
	
	public void clear() {
		widgets.clear();
	}
	
	@Override
	public void close() {
		for(EyeWidget e : widgets) {
			e.close();
		}
	}
	
	public List<EyeWidget> getWidgets() {
		return widgets;
	}
	
	@Override
	public void draw(PoseStack matrices, int x, int y) {
		if(this.isHidden()) return;
		super.draw(matrices, x, y);
		for(EyeWidget e : widgets) {
			if(!e.isHidden()) e.draw(matrices, x + e.getX(), y + e.getY());
		}		
	}
	
	public void drawHover(PoseStack matrices, int mouseX, int mouseY) {
		EyeWidget w = this.getOn(mouseX, mouseY);
		if(w != null) {
			w.drawHover(matrices, mouseX, mouseY);
		}
	}
	
	@Override
	public void tick(int mx, int my) {
		for(EyeWidget e : widgets) {
			e.tick(mx, my);
		}
	}
	
	public EyeWidget getHit(int x, int y) {
		for(int i = widgets.size()-1; i >= 0; i--) {
			EyeWidget w = widgets.get(i);
			if(w.inBounds(x, y) && !w.isHidden() && w.isClickable()) {
				return w;
			}
		}
		return null;
	}
	
	public EyeWidget getOn(int x, int y) {
		for(int i = widgets.size()-1; i >= 0; i--) {
			EyeWidget w = widgets.get(i);
			if(w.inBounds(x, y) && !w.isHidden()) {
				if(!(w instanceof EyePanel)) {
					if(w.getHover() != null) return w;	
				} else {
					return ((EyePanel)w).getOn(x, y);
				}
				
			}
		}
		return null;
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		this.clearFocus();
		EyeWidget w = this.getHit((int)mouseX, (int)mouseY);
		if(w != null) {
			w.setFocussed(true);
			w.mouseClicked(mouseX, mouseY, button);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
		for(int i = widgets.size()-1; i >= 0; i--) {
			EyeWidget w = widgets.get(i);
			if(w.inBounds((int)mouseX, (int)mouseY)) {
				if(w.mouseScrolled(mouseX, mouseY, amount)) return true;
			}
		}
		return false;
	}
	
	public EyeWidget getFocussed() {
		if(isFocussed()) return this;
		for(int i = widgets.size()-1; i >= 0; i--) {
			EyeWidget foc = widgets.get(i).getFocussed();
			if(foc != null) return foc;
		}
		return null;
	}
	
	@Override
	public void clearFocus() {
		this.setFocussed(false);
		for(EyeWidget w : widgets) {
			w.clearFocus();
		}
	}
}
