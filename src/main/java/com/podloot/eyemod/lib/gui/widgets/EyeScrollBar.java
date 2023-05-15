package com.podloot.eyemod.lib.gui.widgets;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.podloot.eyemod.lib.gui.EyeDraw;
import com.podloot.eyemod.lib.gui.EyeLib;
import com.podloot.eyemod.lib.gui.panels.EyeScrollPanel;

public class EyeScrollBar extends EyeInteract {
	
	public Axis axis;
	EyeScrollPanel scroll;
	int min, max, xoff, yoff;
	int slide = 0;
	int size = 12;
	boolean dragging = false;
	
	public EyeScrollBar(int width, int height, Axis axis) {
		super(width, height);
		this.axis = axis;
		min = 0;
		max = 100;
	}
	
	public void setState(int state) {
		this.setValue(state);
	}
	
	public void setBar(EyeScrollPanel scroll) {
		max = scroll.axis == Axis.HORIZONTAL ? scroll.getScrollWidth() - scroll.getWidth() : scroll.getScrollHeight() - scroll.getHeight();
		max = max < 0 ? 0 : max;
		if(axis == Axis.HORIZONTAL) {
			int s = this.getHeight() - max;
			size = max < this.getWidth() ? this.getWidth() : (s > 10 ? s : 10);
		} else {
			int s = max <= 0 ? this.getHeight() : max <= this.getHeight()*4 ? (this.getHeight()- (max/4)) : 10;
			size = s < 10 ? 10 : s > this.getHeight() ? this.getHeight() : s;
		}
		this.scroll = scroll;
	}
	
	@Override
	public void draw(MatrixStack matrices, int x, int y) {
		//Bar
		boolean a = axis == Axis.HORIZONTAL;
		EyeDraw.nine(matrices, EyeLib.PLANE, x, y, width, height, secondary);
		EyeDraw.nine(matrices, EyeLib.PLANE, x + (a ? getLoc(a) : 0), y + (a ? 0 : getLoc(a)), (a ? size : width), (a ? height : size), primary);
		EyeDraw.nine(matrices, EyeLib.PLANE_BORDER, x + (a ? getLoc(a) : 0), y + (a ? 0 : getLoc(a)), (a ? size : width), (a ? height : size), this.isHovered() ? 0xffFFFFFF : 0xff000000);
		
	}
	
	public int getLoc(boolean a) {
		slide = slide < 0 ? 0 : slide > (a ? width : height) - size ? (a ? width : height) - size : slide;
		return slide;
	}
	
	public int getValue() {
		boolean a = axis == Axis.HORIZONTAL;
		float l = getLoc(a);
		float total = a ? width - size : height - size;
		float range = (max-min) + 1;
		float steps = total / range;
		int f = (int)(l / steps) + min;
		return f < min ? min : f > max ? max : f;
	}
	
	public void setValue(int amount) {
		boolean a = axis == Axis.HORIZONTAL;
		float total = a ? width - size : height - size;
		if(amount >= max || amount == -1) {
			slide = (int) total;
			return;
		} else if(amount <= min) {
			slide = 0;
			return;
		}
		float range = (max-min) + 1;
		float steps = total / range;
		slide = (int) (steps * amount + (steps/2F) + min);
	}
	
	@Override
	public void tick(int mx, int my) {
		if(dragging) {
			if(axis == Axis.HORIZONTAL) {
				slide = (mx+xoff) - (size/2) - this.getGlobalX();
				this.scroll.setScroll(axis, this.getValue());
			} else {
				slide = (my+yoff) - (size/2) - this.getGlobalY();
				this.scroll.setScroll(axis, this.getValue());
			}
		}
		super.tick(mx, my);
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		dragging = true;
		if(axis == Axis.HORIZONTAL) {
			int mx =  ((int)mouseX) - (size/2) - this.getGlobalX();
			if(mx > slide - (size/2) && mx < slide + (size/2)) {
				xoff = (int) (mx-slide)*-1;
			} else {
				xoff = 0;
			}
		} else {
			int my =  ((int)mouseY) - (size/2) - this.getGlobalY();
			if(my > slide - (size/2) && my < slide + (size/2)) {
				yoff = (int) (my-slide)*-1;
			} else {
				yoff = 0;
			}
		}
		return true;
	}
	
	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		dragging = false;
		return false;
	}
	
	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
		scroll((int)amount);
		return true;
	}
	
	public void scroll(int a) {
		slide += a;
		this.scroll.setScroll(axis, this.getValue());
	}
	
	@Override
	public boolean isHovered() {
		return super.isHovered() || dragging;
	}

}
