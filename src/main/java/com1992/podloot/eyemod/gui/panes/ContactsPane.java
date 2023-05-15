package com.podloot.eyemod.gui.panes;

import com.podloot.eyemod.gui.apps.App;

import net.minecraft.nbt.ListTag;

public class ContactsPane extends PlayersPane {

	public ContactsPane(App app, int width, int height) {
		super(app, width, height);
	}
	
	public void addItems(ListTag items) {
		for(int i = 0; i < items.size(); i++) {
			list.add(getPanel(items.getString(i)));
		}
	}

}
