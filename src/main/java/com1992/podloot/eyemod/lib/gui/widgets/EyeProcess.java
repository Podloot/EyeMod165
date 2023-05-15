package com.podloot.eyemod.lib.gui.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import com.podloot.eyemod.lib.gui.EyeDraw;
import com.podloot.eyemod.lib.gui.EyeLib;
import com.podloot.eyemod.lib.gui.util.Line;

public class EyeProcess extends EyeWidget {
	
	int range = 100;
	int state = range;
	String form;
	boolean isProcent = false;

	public EyeProcess(int width, int height, int range) {
		super(false, width, height);
		this.range = range;
		this.setState(range);
	}

	public void setText(Line text, boolean procent) {
		this.text = text;
		this.isProcent = procent;
		form = text.getText().replace("{v}", "%s");
	}
	
	public void setText(Line text) {
		setText(text, false);
	}
	
	public void setState(int state) {
		this.setValue(state);
	}

	@Override
	public void draw(PoseStack matrices, int x, int y) {
		EyeDraw.nine(matrices, EyeLib.PLANE, x, y, width, height, primary);
		int p = (int)(width * (float)state/(float)range);
		if(p >= 0) EyeDraw.nine(matrices, EyeLib.PLANE, x, y, p < 4 ? 4 : p, height, secondary);
		if(text != null) EyeDraw.text(matrices, text, x + width/2, y + height/2);
	}
	
	@Override
	public void tick(int mx, int my) {
		if(text != null && form.contains("%s")) {
			if(isProcent) {
				int pr =  Math.round(((float)state / (float)range)*100);
				text.setText(String.format(form, pr + "%"));
			} else {
				text.setText(String.format(form, state, range));
			}
			
		}
		super.tick(mx, my);
	}

	public int getValue() {
		return state;
	}

	public void setValue(int value) {
		this.state = value < 0 ? 0 : state > range ? range : value;
	}

}
