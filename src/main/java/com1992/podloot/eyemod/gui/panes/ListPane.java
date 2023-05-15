package com.podloot.eyemod.gui.panes;

import com.podloot.eyemod.gui.apps.App;
import com.podloot.eyemod.lib.gui.panels.EyeListPanel;
import com.podloot.eyemod.lib.gui.widgets.EyeList;

public class ListPane extends Pane {
	
	EyeList list;

	public ListPane(App app, int width, int height) {
		super(app, width, height);
		
		list = new EyeList(width - 8, height - 22, Axis.VERTICAL);
		add(list, 4, 4);
		
		addAccept(0);
		addCancel(1);
	}
	
	public EyeList getList() {
		return list;
	}
	
	public EyeListPanel getSelected() {
		return list.getSelected();
	}
	
	public Object getData() {
		EyeListPanel lp = getSelected();
		return lp == null ? null : lp.getData();
	}

}
