package com.podloot.eyemod.gui.panes;

import com.podloot.eyemod.gui.apps.App;
import com.podloot.eyemod.gui.presets.PhotosPreset;

public class PhotoPane extends Pane {
	
	PhotosPreset photos;

	public PhotoPane(App app, int width, int height) {
		super(app, width, height);
		photos = new PhotosPreset(app, width-8, height-22);
		add(photos, 4, 4);
		
		addAccept(0);
		addCancel(1);
	}
	
	public String getPhoto() {
		return photos.getPhoto();
	}

}
