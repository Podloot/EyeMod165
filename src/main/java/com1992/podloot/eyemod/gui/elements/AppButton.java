package com.podloot.eyemod.gui.elements;

import com.mojang.blaze3d.vertex.PoseStack;
import com.podloot.eyemod.gui.apps.App;
import com.podloot.eyemod.lib.gui.EyeDraw;
import com.podloot.eyemod.lib.gui.EyeLib;
import com.podloot.eyemod.lib.gui.util.Image;
import com.podloot.eyemod.lib.gui.widgets.EyeClickable;

public class AppButton extends EyeClickable {
	
	Image icon;
	boolean notification;

	public AppButton(App app) {
		super(32, 32);
		this.icon = app.appIcon;
		this.setColor(app.getAppColor());
		setText(app.getName().setAllingment(0, 1).setScale(0.8F));
		
		if(app.getDevice() != null) {
			notification = !app.getDevice().getNotifications(app.getId()).isEmpty();
		}
		
	}
	
	@Override
	public void draw(PoseStack matrices, int x, int y) {
		EyeDraw.nine(matrices, EyeLib.PLANE, x, y, getWidth(), getHeight(), getPrimary());
		EyeDraw.nine(matrices, EyeLib.PLANE_BORDER, x, y, getWidth(), getHeight(), this.isHovered() ? 0xffFFFFFF : 0xff000000);
		if(icon != null) EyeDraw.texture(matrices, icon, x, y, getWidth(), getHeight(), 0xffFFFFFF);
		if(getText() != null) EyeDraw.text(matrices, getText(), x + (getWidth()/2), y + (getHeight()) + 1);
		if(notification) EyeDraw.texture(matrices, EyeLib.NOTIFICATION, x + getWidth() - 9, y - 3, 12, 12);
		
	}

}
