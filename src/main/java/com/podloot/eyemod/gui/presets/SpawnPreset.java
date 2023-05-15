package com.podloot.eyemod.gui.presets;

import com.podloot.eyemod.gui.apps.App;
import com.podloot.eyemod.gui.panes.ListPane;
import com.podloot.eyemod.lib.gui.panels.EyeListPanel;
import com.podloot.eyemod.lib.gui.util.Line;
import com.podloot.eyemod.lib.gui.widgets.EyeButton;
import com.podloot.eyemod.lib.gui.widgets.EyeLabel;

public class SpawnPreset extends PresetInput {
	
	
	public SpawnPreset(App app, int width, String[] lib) {
		super(app, width, 32, new Line("text.eyemod.entity"));	
		name.setAllowed("[a-zA-Z_ ]+");
		name.setSuggest(lib);
		EyeButton mobs = new EyeButton(42, 14, new Line("text.eyemod.list"));
		mobs.setColor(app.appColor);
		mobs.setAction(() -> {
			ListPane list = new ListPane(app, app.getWidth()-4, app.getHeight()-4);
			for(int i = 0; i < lib.length; i++) {
				EyeListPanel pan = new EyeListPanel(list.getList(), 14);
				EyeLabel lbl = new EyeLabel(pan.getWidth(), pan.getHeight(), new Line(lib[i]));
				pan.setData(lib[i]);
				lbl.setBack(app.appColor);
				pan.add(lbl, 0, 0);
				list.getList().add(pan);
			}
			list.setAction(() -> {
				if(list.getSelected() != null) name.setInput(list.getSelected().getData() + "");
			});
			app.openPane(list);
		});
		add(mobs, 0, 18);
	}


}
