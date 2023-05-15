package com.podloot.eyemod.gui.apps.base;

import com.podloot.eyemod.Eye;
import com.podloot.eyemod.gui.apps.App;
import com.podloot.eyemod.gui.panes.ConfirmPane;
import com.podloot.eyemod.gui.panes.InputPane;
import com.podloot.eyemod.gui.presets.PhotosPreset;
import com.podloot.eyemod.gui.util.Photos;
import com.podloot.eyemod.lib.gui.EyeLib;
import com.podloot.eyemod.lib.gui.util.Color;
import com.podloot.eyemod.lib.gui.util.Line;
import com.podloot.eyemod.lib.gui.widgets.EyeButton;
import com.podloot.eyemod.lib.gui.widgets.EyeWidget;

import net.minecraft.util.ResourceLocation;

public class AppPhotos extends App {

	public AppPhotos() {
		super(new ResourceLocation(Eye.MODID, "textures/gui/apps/appphotos.png"), 0xff9bb4c5, "Eye");
		
	}

	@Override
	public boolean load() {
		PhotosPreset photos = new PhotosPreset(this, getWidth()-14, getHeight()-20);
		add(photos, 7, 2);
		
		EyeButton delete = new EyeButton(14, 14, EyeLib.DELETE);
		delete.setColor(Color.DARKGRAY, Color.RED);
		delete.setAction(() -> {
			ConfirmPane del = new ConfirmPane(this, getWidth()-4, 38, new Line("text.eyemod.photos_delete"), true);
			del.setAction(() -> {
				String photo = photos.getPhoto();
				if(photo != null && !photo.isEmpty()) {
					Photos.deleteShot(photo);
					this.refresh();
				}
			});
			openPane(del);
		});
		add(delete, getWidth()-21, getHeight() - 16);
		
		EyeButton name = new EyeButton(getWidth() - 30, 14, new Line(""));
		name.setColor(getAppColor());
		name.setAction(() -> {
			InputPane in = new InputPane(this, getWidth()-4, 52, new Line("text.eyemod.photos_rename"));
			in.getTextField().setAllowed("[0-9a-zA-Z_]+");
			in.getTextField().setLimit(24);
			in.setAction(() -> {
				String i = in.getInput();
				String photo = photos.getPhoto();
				if(i != null && !i.isEmpty() && photo != null && !photo.isEmpty()) {
					Photos.renameShot(photo, i);
					this.refresh();
				}
			});
			openPane(in);
		});
		
		add(name, 7, getHeight() - 16);
		
		photos.getList().onSelect(() -> {
			String photo = photos.getPhoto();
			name.setText(new Line(photo != null ? photo : ""));
		});
		
		return true;
	}
	
	@Override
	public EyeWidget getNotification(String message, int width) {
		EyeButton msg = new EyeButton(width, 16, new Line(message).setScale(0.75F));
		msg.setColor(getAppColor());
		msg.setAction(() -> {
			if(device != null) {
				if(!device.isLocked()) {
					device.openApp(this);
				}
			}
		});
		return msg;
	}

}
