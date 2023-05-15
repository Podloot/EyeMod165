package com.podloot.eyemod.gui.presets;

import com.podloot.eyemod.gui.apps.App;
import com.podloot.eyemod.gui.presets.Preset;
import com.podloot.eyemod.lib.gui.util.Line;
import com.podloot.eyemod.lib.gui.widgets.EyeLabel;
import com.podloot.eyemod.lib.gui.widgets.EyeTextField;

public class TimerPreset extends Preset {
	
	EyeTextField min;
	EyeTextField sec;

	public TimerPreset(App app, int width, int height) {
		super(app, width, height);
		
		min = new EyeTextField(width/2 - 2, height);
		sec = new EyeTextField(width/2 - 2, height);
		min.setHover(new Line("text.eyemod.minutes"));
		sec.setHover(new Line("text.eyemod.seconds"));
		min.setText(new Line("00"));
		sec.setText(new Line("00"));
		min.setAllowed("[0-9]+");
		sec.setAllowed("[0-9]+");
		min.setLimit(2);
		sec.setLimit(2);
		
		add(min, 0, 0);
		add(sec, width/2 + 2, 0);
		
		EyeLabel lbl = new EyeLabel(width, height, new Line(":"));
		add(lbl, 0, 0);
	}
	
	public int getTime() {
		return getTime(min.getInput(), sec.getInput());
	}
	
	private int getTime(String input, String input2) {
		int m = 0;
		int s = 0;
		if(input.matches("[0-9]+")) m = Integer.valueOf(input);
		if(input2.matches("[0-9]+")) s = Integer.valueOf(input2);
		
		return m*60 + s;
	}
	
	public void setTime(int min, int sec) {
		this.min.setInput(min + "");
		this.sec.setInput(sec + "");
	}


}
