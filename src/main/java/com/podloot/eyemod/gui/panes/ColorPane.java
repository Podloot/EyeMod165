package com.podloot.eyemod.gui.panes;

import java.awt.Color;

import com.podloot.eyemod.gui.apps.App;
import com.podloot.eyemod.lib.gui.widgets.EyePlane;
import com.podloot.eyemod.lib.gui.widgets.EyeSlider;

public class ColorPane extends Pane {
	
	EyeSlider red;
	EyeSlider green;
	EyeSlider blue;
	
	EyePlane color;

	public ColorPane(App app, int width, int height) {
		super(app, width, height);	
		
		red = new EyeSlider(width-8, 12, 0, 255);
		red.setColor(0xffFF0000);
		green = new EyeSlider(width-8, 12, 0, 255);
		green.setColor(0xff00FF00);
		blue = new EyeSlider(width-8, 12, 0, 255);
		blue.setColor(0xff0000FF);
		
		add(red, 4, 4);
		add(green, 4, 20);
		add(blue, 4, 36);
		
		color = new EyePlane(width-42, 12, 0xffFFFFFF);
		add(color, 4, height-16);
		
		addAccept(0);
		addCancel(1);
	}
	
	public void setValue(int color) {
		Color c = new Color(color);
		red.setState(c.getRed());
		green.setState(c.getGreen());
		blue.setState(c.getBlue());
	}
	
	@Override
	public void tick(int mx, int my) {
		Color c = new Color(red.getValue(), green.getValue(), blue.getValue());
		color.setColor(c.getRGB());
		super.tick(mx, my);
	}
	
	
	public int getColor() {
		return new Color(red.getValue(), green.getValue(), blue.getValue()).getRGB();
	}

}
