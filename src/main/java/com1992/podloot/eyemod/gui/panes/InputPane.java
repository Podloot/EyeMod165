package com.podloot.eyemod.gui.panes;

import com.podloot.eyemod.gui.apps.App;
import com.podloot.eyemod.lib.gui.util.Line;
import com.podloot.eyemod.lib.gui.widgets.EyeLabel;
import com.podloot.eyemod.lib.gui.widgets.EyeTextField;

public class InputPane extends Pane {
	
	EyeTextField input;

	public InputPane(App app, int width, int height, Line title) {
		super(app, width, height);
		
		EyeLabel t = new EyeLabel(width-8, 14, title);
		t.setAlignment(1, 0);
		add(t, 4, 4);
		input = new EyeTextField(width-8, 16);
		add(input, 4, 18);
		
		addAccept(0);
		addCancel(1);
	}
	
	public EyeTextField getTextField() {
		return input;
	}
	
	public String getInput() {
		return input.getInput();
	}

}
