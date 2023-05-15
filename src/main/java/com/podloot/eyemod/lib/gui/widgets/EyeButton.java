package com.podloot.eyemod.lib.gui.widgets;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.podloot.eyemod.lib.gui.EyeDraw;
import com.podloot.eyemod.lib.gui.EyeLib;
import com.podloot.eyemod.lib.gui.util.Image;
import com.podloot.eyemod.lib.gui.util.Line;

public class EyeButton extends EyeClickable {

	private Image icon;
	private int offset = 0;
	
	public EyeButton(int width, int height) {
		super(width, height);
	}
	
	public EyeButton(int width, int height, Line text) {
		super(width, height);
		setText(text);
	}
	
	public EyeButton(int width, int height, Image icon) {
		super(width, height);
		secondary = 0xffFFFFFF;
		setIcon(icon);
	}
	
	public void setOffset(int o) {
		offset = o;
	}
	
	public void setIcon(Image icon) {
		this.icon = icon;
	}
	
	@Override
	public void draw(MatrixStack matrices, int x, int y) {
		EyeDraw.nine(matrices, EyeLib.PLANE, x, y, width, height, primary);
		EyeDraw.nine(matrices, EyeLib.PLANE_BORDER, x, y, width, height, this.isHovered() ? 0xffFFFFFF : 0xff000000);
		if(text != null) {
			text.setColor(this.isEnabled() ? textcolor : 0xff444444);
			this.drawText(matrices, x+4, y+4, width-8, height-8);
		}
		if(icon != null) EyeDraw.texture(matrices, icon, x+offset, y+offset, width-(offset*2), height-(offset*2), secondary);
	}
	
	public Image getIcon() {
		return icon;
	}
	

}
