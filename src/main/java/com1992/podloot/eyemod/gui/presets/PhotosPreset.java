package com.podloot.eyemod.gui.presets;

import com.podloot.eyemod.gui.apps.App;
import com.podloot.eyemod.gui.util.Photos;
import com.podloot.eyemod.lib.gui.panels.EyeListPanel;
import com.podloot.eyemod.lib.gui.widgets.EyeList;
import com.podloot.eyemod.lib.gui.widgets.EyePhoto;
import com.podloot.eyemod.lib.gui.widgets.EyePlane;

public class PhotosPreset extends Preset {
	
	EyeList photos;
	float res = 192F / 148F;

	public PhotosPreset(App app, int width, int height) {
		super(app, width, height);
		photos = new EyeList(width, height, Axis.VERTICAL);
		for(String s : Photos.getPhotos()) {
			photos.add(getPanel(s));
		}
		add(photos, 0, 0);
	}
	
	public EyeListPanel getPanel(String photoname) {
		int pw = photos.getWidth()-12;
		int ph = (int)(res*pw);
		ph = ph % 2 == 1 ? ph+1 : ph;
		EyeListPanel lp = new EyeListPanel(photos, ph+2);
		EyePhoto photo = new EyePhoto(pw, ph, photoname);
		lp.add(photo, 1, 1);
		lp.setData(photoname);
		return lp;
		
	}
	
	public EyeList getList() {
		return photos;
	}

	public String getPhoto() {
		if(photos.getSelected() == null) return null;
		return (String)photos.getSelected().getData();
	}

}
