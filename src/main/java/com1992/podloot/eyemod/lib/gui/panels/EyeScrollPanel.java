package com.podloot.eyemod.lib.gui.panels;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.vertex.PoseStack;
import com.podloot.eyemod.lib.gui.widgets.EyeScrollBar;
import com.podloot.eyemod.lib.gui.widgets.EyeWidget;

import net.minecraft.client.Minecraft;

public class EyeScrollPanel extends EyePanel {
	
	public Axis axis = Axis.VERTICAL;
	protected int scroll_x = 0;
	protected int scroll_y = 0;
	
	public EyeScrollBar scroll_h = null;
	public EyeScrollBar scroll_v = null;
	
	public EyeScrollPanel(int width, int height) {
		super(width, height);
	}
	
	public void setScrollBar(Axis a, EyeScrollBar bar) {
		bar.setBar(this);
		axis = a;
		if(a == Axis.HORIZONTAL) {
			scroll_h = bar;
		} else {
			scroll_v = bar;
		}
	}
	
	@Override
	public void draw(PoseStack matrices, int x, int y) {
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		int s = (int)Minecraft.getInstance().getWindow().getGuiScale();
		GL11.glScissor((x)*s, (Minecraft.getInstance().getWindow().getHeight()) - ((y+getHeight())*s), getWidth()*s, getHeight()*s);
    	super.draw(matrices, x, y);
    	GL11.glDisable(GL11.GL_SCISSOR_TEST);
	}
	
	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
		this.setScroll(Axis.VERTICAL, getScroll_y() - (int)amount*6);
		return true;
	}
	
	public void setScroll(Axis axis, int a) {		
		if(axis == Axis.HORIZONTAL) {
			int lim = this.getScrollWidth() - this.getWidth();
			if(lim < 0) return;
			int la = a == -999 ? lim : a <= 0 ? 0 : a >= lim ? lim : a;
			scroll_x = la;
			if(scroll_h != null) scroll_h.setValue(la);
		}
		if(axis == Axis.VERTICAL) {
			int lim = this.getScrollHeight() - this.getHeight();
			if(lim < 0) return;
			int la = a == -999 ? lim : a <= 0 ? 0 : a >= lim ? lim : a;
			scroll_y = la;
			if(scroll_v != null) scroll_v.setValue(la);
		}
		this.updateScroll();
	}
	
	public void updateScroll() {
		for(EyeWidget e : widgets) {
			if(!(e instanceof EyeScrollBar)) e.setOff(-getScroll_x(), -getScroll_y());
		}
	}
	
	public int getScrollWidth() {
		int w = 0;
		for(EyeWidget e : widgets) {
			if(!(e instanceof EyeScrollBar)) {
				int nw = e.getX() - e.getOffX() + e.getWidth();
				w = nw > w ? nw : w;
			}
		}
		return w;
	}
	
	public int getScrollHeight() {
		int h = 0;
		for(EyeWidget e : widgets) {
			if(!(e instanceof EyeScrollBar)) {
				int nh = e.getY() - e.getOffY() + e.getHeight();
				h = nh > h ? nh : h;
			}
		}
		return h;
	}

	public int getScroll_x() {
		return scroll_x;
	}

	public int getScroll_y() {
		return scroll_y;
	}


}
