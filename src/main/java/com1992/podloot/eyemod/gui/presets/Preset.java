package com.podloot.eyemod.gui.presets;

import com.podloot.eyemod.gui.apps.App;
import com.podloot.eyemod.lib.gui.panels.EyePanel;

public class Preset extends EyePanel {

	App app;
	
	public Preset(App app, int width, int height) {
		super(width, height);
		this.app = app;
	}


}
