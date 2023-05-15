package com.podloot.eyemod.lib.gui.widgets;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.podloot.eyemod.lib.gui.EyeDraw;
import com.podloot.eyemod.lib.gui.util.Image;

public class EyeState extends EyeIcon {
	
	int state;
	int states;

	public EyeState(int width, int height, Image icon, int states) {
		super(width, height, icon);
		primary = 0xffFFFFFF;
		this.image = icon;
		this.states = states;
	}
	
	public void setIcon(Image icon, int states) {
		this.image = icon;
		this.states = states;
	}

	@Override
	public void draw(MatrixStack matrices, int x, int y) {
		int sw = image.width / states;
		EyeDraw.texture(matrices, image, x, y, width, height, sw*state, 0, sw, height, primary);
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		if(state < 0 || state > states) return;
		this.state = state;
	}
	
	public int getStates() {
		return states;
	}

}
