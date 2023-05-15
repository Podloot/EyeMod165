package com.podloot.eyemod.lib.gui.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import com.podloot.eyemod.lib.gui.util.Line;

public class EyeLabel extends EyeWidget {
	
	
	public EyeLabel(int width, int height, Line text) {
		super(false, width, height);
		setText(text);
	}
	
	public void setAlignment(int x, int y) {
		text = text.setAllingment(x, y);
	}
	
	public void setVariable(String v) {
		text.setText(text.getText().replace("{v}", v));
	}

	@Override
	public void draw(PoseStack matrices, int x, int y) {
		super.draw(matrices, x, y);
		this.drawText(matrices, x+4, y+3, width-8, height-6);
	}

}
