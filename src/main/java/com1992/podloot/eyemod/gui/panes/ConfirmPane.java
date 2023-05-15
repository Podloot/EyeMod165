package com.podloot.eyemod.gui.panes;

import com.podloot.eyemod.gui.apps.App;
import com.podloot.eyemod.lib.gui.util.Line;
import com.podloot.eyemod.lib.gui.widgets.EyeText;

public class ConfirmPane extends Pane {

	Line text;
	
	public ConfirmPane(App app, int width, int height, Line text, boolean cancel) {
		super(app, width, height);
		this.text = text;
		if(text != null) {
			EyeText t = new EyeText(width-8, height-8, text);
			t.setAlignment(1, 1);
			add(t, 4, 4);
		}
		addAccept(0);
		if(cancel) addCancel(1);
		
	}

}
