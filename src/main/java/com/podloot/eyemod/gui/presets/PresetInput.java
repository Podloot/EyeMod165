package com.podloot.eyemod.gui.presets;

import com.podloot.eyemod.gui.apps.App;
import com.podloot.eyemod.lib.gui.util.Line;
import com.podloot.eyemod.lib.gui.util.Space;
import com.podloot.eyemod.lib.gui.widgets.EyeButton;
import com.podloot.eyemod.lib.gui.widgets.EyeTextField;

public abstract class PresetInput extends Preset {
	
	EyeButton select;
	EyeTextField name;
	
	public PresetInput(App app, int width, int height, Line text) {
		super(app, width, height);
		name = new EyeTextField(width, 16);
		name.setText(text);
		add(name, 0, 0);
	}

	public void setButton(Line text, int color, Runnable action) {
		setButton(new Space(40, 18, getWidth()-40, 14), text, color, action);
	}
	
	public void setButton(Space space, Line text, int color, Runnable action) {
		select = new EyeButton(space.width, space.height, text);
		select.setText(text);
		select.setColor(color);
		select.setAction(action);
		add(select, space.x, space.y);
		if(space.y + space.height > getHeight()) {
			setHeight(space.y + space.height);
		}
	}
	
	public EyeTextField getField() {
		return name;
	}
	
	public String getInput() {
		return name.getInput();
	}
	
	public void setInput(String input) {
		name.setInput(input);
	}
	

}
