package com.podloot.eyemod.lib.gui.widgets;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.podloot.eyemod.lib.gui.EyeDraw;
import com.podloot.eyemod.lib.gui.util.Line;

public class EyeTextField extends EyeTextInput {
	
	
	String show = "";
	Runnable onUp;
	

	public EyeTextField(int width, int height) {
		super(width, height);
		
	}
	
	@Override
	public void setText(Line text) {
		this.text = text.setAllingment(1, 0).setColor(0xff777777);
	}
	
	public void drawCursor(MatrixStack matrices, int x, int y) {
		if(cursor < 0 || cursor > input.length()) return;
		int i = show.length();
		if(cursor <= show.length()) i = cursor;
		int w = client.width(show.substring(0, i));
		EyeDraw.text(matrices, "|", x+4+w, y+(height/2) - 4, 0xffFFFFFF);
	}
	
	public void drawLines(MatrixStack matrices, int x, int y) {
		String sub = input.substring(0, cursor);
		show = client.plainSubstrByWidth(sub, getWidth()-8, true);
		show = client.width(sub) < width-8 ? client.plainSubstrByWidth(input, width-8) : show;
		EyeDraw.text(matrices, show, x+4, y+(height/2) - 4, 0xffFFFFFF);
	}
	
	public void drawSuggest(MatrixStack matrices, int x, int y, String s) {
		if(cursor < 0 || cursor > input.length()) return;
		int i = show.length();
		if(cursor <= show.length()) i = cursor;
		int w = client.width(show.substring(0, i));
		s = s.substring(input.length());
		EyeDraw.text(matrices, s, x+4+w, y+(height/2) - 4, 0xff999999);
	}
	
	public void setOnUp(Runnable onUp) {
		this.onUp = onUp;
	}

	@Override
	public boolean keyPressed(int keyCode) {
		switch(keyCode) {
		case 257:
			if(action != null) action.run();
			return true;
		case 265:
			if(onUp != null) onUp.run();
			return true;
		}
		return false;
	}

}
