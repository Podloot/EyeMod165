package com.podloot.eyemod.gui.panes;

import com.podloot.eyemod.gui.apps.App;
import com.podloot.eyemod.lib.gui.panels.EyePanel;
import com.podloot.eyemod.lib.gui.util.Color;
import com.podloot.eyemod.lib.gui.util.Line;
import com.podloot.eyemod.lib.gui.widgets.EyeButton;
import com.podloot.eyemod.lib.gui.widgets.EyeIcon;
import com.podloot.eyemod.lib.gui.widgets.EyeLabel;

public class StorePane extends AppPane {

	public StorePane(App app, int width, int height) {
		super(app, width, height);
	}
	
	public EyePanel getAppPanel(App app) {
		EyePanel panel = new EyePanel(getWidth()-14, 40);
		
		EyeIcon icon = new EyeIcon(32, 32, app.appIcon);
		icon.setBack(app.appColor);
		EyeLabel name = new EyeLabel(getWidth()-14 - 34, 16, app.getName());
		name.setBack(app.getAppColor());
		
		EyeButton remove = new EyeButton(getWidth()-14 - 34, 14, new Line("text.eyemod.settings_deinstall"));
		remove.setColor(Color.RED);
		remove.setAction(() -> {
			if(app.hasData()) app.onClearData();
			deleteApp(app);
		});
		panel.add(remove, 34, 18);
		panel.add(name, 34, 0);
		panel.add(icon, 0, 0);
		
		return panel;
		
	}

}
