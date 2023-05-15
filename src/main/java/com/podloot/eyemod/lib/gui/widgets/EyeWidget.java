package com.podloot.eyemod.lib.gui.widgets;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.podloot.eyemod.lib.gui.EyeDraw;
import com.podloot.eyemod.lib.gui.EyeLib;
import com.podloot.eyemod.lib.gui.util.Color;
import com.podloot.eyemod.lib.gui.util.Line;
import com.podloot.eyemod.lib.gui.util.Space;

import net.minecraft.client.Minecraft;

public abstract class EyeWidget {
	
	boolean clickable = false;
	boolean hide = false;
	
	private EyeText hover = null;
	
	public enum Axis {
			HORIZONTAL,
			VERTICAL
	}

	EyeWidget parent;

	int primary = Color.LIGHTGRAY;
	int secondary = Color.DARKGRAY;
	
	Line text;
	int textcolor = 0xffFFFFFF;
	
	boolean enabled = true;
	boolean focussed = false;
	boolean background = false;
	int width = 12, height = 12;
	int x, y, offx, offy;
	
	public EyeWidget(boolean clickable, int width, int height) {
		this.setSize(width, height);
		this.clickable = clickable;
	}
	
	public void copy(EyeWidget e) {
		hide = e.hide;
		parent = e.parent;
		primary = e.primary;
		secondary = e.secondary;
		text = e.text;
		textcolor = e.textcolor;
		enabled = e.enabled;
		focussed = e.focussed;
		background = e.background;
		width = e.width;
		height = e.height;
		x = e.x;
		y = e.y;
		offx = e.offx;
		offy = e.offx;
	}
	
	public void setColor(int color) {
		this.primary = color;
	}
	
	public void setBack(int color) {
		this.background = true;
		if(background) setColor(color);
	}
	
	public void setColor(int primary, int secondary) {
		this.primary = primary;
		this.secondary = secondary;
	}
	
	public void setSize(int w, int h) {
		this.setWidth(w);
		this.setHeight(h);
	}

	public void setPos(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void setOff(int x, int y) {
		this.offx = x;
		this.offy = y;
	}

	
	public void draw(MatrixStack matrices, int x, int y) {
		if(background) EyeDraw.nine(matrices, EyeLib.PLANE, x, y, width, height, primary);
	}
	
	public void drawHover(MatrixStack matrices, int mouseX, int mouseY) {
		if(hover != null) {
			hover.draw(matrices, mouseX-24, mouseY+4);
		}
	}
	
	public void drawText(MatrixStack matrices, int x, int y, int w, int h) {
		int cx = text.getCenteredX();
		int cy = text.getCenteredY();
		EyeDraw.text(matrices, text, x - (w/2 * (cx-1)), y - (h/2 * (cy-1)));
	}

	public void tick(int mx, int my) {}

	public void close() {}

	public EyeWidget getHit(int x, int y) {
		if (inBounds(x, y)) return this;
		else return null;
	}

	public boolean inBounds(int x, int y) {
		return (x >= getGlobalX() && y >= getGlobalY() && x <= getGlobalX() + getWidth()
				&& y < getGlobalY() + getHeight());
	}

	public int getGlobalX() {
		if (getParent() == null)
			return getX();
		else
			return getX() + getParent().getGlobalX();
	}

	public int getGlobalY() {
		if (getParent() == null)
			return getY();
		else
			return getY() + getParent().getGlobalY();
	}

	public int getX() {
		return x + offx;
	}

	public int getY() {
		return y + offy;
	}
	
	public int getOffX() {
		return offx;
	}

	public int getOffY() {
		return offy;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	// Mouse and size
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		return false;
	}

	public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
		return false;
	}
	
	public boolean charTyped(char chr, int modifiers) {
		return false;
	}

	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		return false;
	}

	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		return false;
	}

	public void mouseMoved(double mouseX, double mouseY) {

	}

	public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
		return false;
	}
	
	public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
		return false;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isFocussed() {
		return focussed;
	}

	public void setFocussed(boolean focussed) {
		this.focussed = focussed;
	}
	
	public EyeWidget getFocussed() {
		return focussed ? this : null;
	}
	
	public boolean isClickable() {
		return clickable;
	}
	
	public void clearFocus() {
		focussed = false;
	}
	
	public void hide(boolean hide) {
		this.hide = hide;
	}
	
	public boolean isHidden() {
		return hide;
	}
	
	public int getPrimary() {
		return primary;
	}
	
	public int getSecondary() {
		return secondary;
	}

	public EyeWidget getParent() {
		return parent;
	}

	public void setParent(EyeWidget parent) {
		this.parent = parent;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	public void setAsSpace(Space space) {
		this.height = space.height;
		this.width = space.width;
		this.x = space.x;
		this.y = space.y;
	}
	
	public void setText(Line text) {
		this.text = text;
		if(text != null) textcolor = text.getColor();
	}
	
	public Line getText() {
		return text;
	}

	public EyeText getHover() {
		return hover;
	}

	public void setHover(Line hover) {
		hover.setAllingment(0, 0);
		int ts = Minecraft.getInstance().font.width(hover.getText()) + 8;
		this.hover = new EyeText(ts >= 140 ? 140 : ts, hover);	
		this.hover.setBack(0xff222222);
	}

}
