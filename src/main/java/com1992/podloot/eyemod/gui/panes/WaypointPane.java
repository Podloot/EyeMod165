package com.podloot.eyemod.gui.panes;

import com.podloot.eyemod.gui.apps.App;
import com.podloot.eyemod.gui.elements.DimensionButton;
import com.podloot.eyemod.lib.gui.panels.EyeListPanel;
import com.podloot.eyemod.lib.gui.util.Line;
import com.podloot.eyemod.lib.gui.util.Pos;
import com.podloot.eyemod.lib.gui.widgets.EyeLabel;

import net.minecraft.nbt.ListTag;

public class WaypointPane extends ListPane {

	public WaypointPane(App app, int width, int height) {
		super(app, width, height);
	}
	
	public void addItems(ListTag items) {
		for(int i = 0; i < items.size(); i++) {
			list.add(getPanel(items.getString(i)));
		}
	}
	
	public EyeListPanel getPanel(String pos) {
		EyeListPanel lp = null;
		Pos p = new Pos().fromString(pos);
		if(p != null) {
			lp = new EyeListPanel(list, 16);
			DimensionButton dm = new DimensionButton(14, 14, p.getWorld());
			lp.add(dm, 1, 1);
			EyeLabel plane = new EyeLabel(list.getWidth()-10 - 18, 16, new Line(p.asString(false)));
			plane.setBack(app.getAppColor());
			lp.add(plane, 18, 0);
			lp.setData(pos);
		}
		return lp;
	}

}
