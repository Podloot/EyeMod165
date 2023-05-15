package com.podloot.eyemod.gui.panes;

import com.podloot.eyemod.gui.apps.App;
import com.podloot.eyemod.lib.gui.EyeLib;
import com.podloot.eyemod.lib.gui.panels.EyePanel;
import com.podloot.eyemod.lib.gui.util.Color;
import com.podloot.eyemod.lib.gui.widgets.EyeButton;
import com.podloot.eyemod.lib.gui.widgets.EyePlane;

public abstract class Pane extends EyePanel {

	App app;
	Runnable accept;
	
	public Pane(App app, int width, int height) {
		super(width, height);
		this.app = app;
	}
	
	public void addAccept(int xoff) {
		EyeButton ok = new EyeButton(12, 12, EyeLib.ACCEPT);
		ok.setColor(Color.DARKGRAY, 0xffFFFFFF);
		ok.setAction(() -> {
			if(accept != null) accept.run();
			app.closePane(this);
		});
		add(ok, getWidth() - (16 + 16*xoff), getHeight() - 16);
	}
	
	public void addCancel(int xoff) {
		EyeButton c = new EyeButton(12, 12, EyeLib.CANCEL);
		c.setColor(Color.DARKGRAY, 0xffFFFFFF);
		c.setAction(() -> {
			app.closePane(this);
		});
		add(c, getWidth() - (16 + 16*xoff), getHeight() - 16);
	}
	
	public void setAction(Runnable accept) {
		this.accept = accept;
	}

}
