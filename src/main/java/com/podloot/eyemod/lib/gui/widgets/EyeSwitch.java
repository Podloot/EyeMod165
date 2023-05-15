package com.podloot.eyemod.lib.gui.widgets;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.podloot.eyemod.lib.gui.EyeDraw;
import com.podloot.eyemod.lib.gui.EyeLib;
import com.podloot.eyemod.lib.gui.util.Line;

public class EyeSwitch extends EyeClickable {
	
	List<Line> options = new ArrayList<Line>();
	int state = 0;
	
	public EyeSwitch(int width, int height) {
		super(width, height);
	}
	
	public void addState(Line line) {
		options.add(line);
	}
	
	@Override
	public void draw(MatrixStack matrices, int x, int y) {
		EyeDraw.nine(matrices, EyeLib.PLANE, x, y, width, height, secondary);
		
		int w = (width / options.size());

		EyeDraw.nine(matrices, EyeLib.PLANE, x + w*state, y, w, height, primary);
		EyeDraw.nine(matrices, EyeLib.PLANE_BORDER, x + w*state, y, w, height, this.isHovered() ? 0xffFFFFFF : 0xff000000);
		
		
		if(!options.isEmpty()) {
			for(int i = 0; i < options.size(); i++) {
				options.get(i).setColor(this.isEnabled() ? textcolor : 0xff444444);
				EyeDraw.text(matrices, options.get(i), x + (w*i) + w/2, y + (height/2));
			}
		}
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if(state < options.size()-1) {
			state++;
		} else {
			state = 0;
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}

	public int getState() {
		return state;
	}

}
