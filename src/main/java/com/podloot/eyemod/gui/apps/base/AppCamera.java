package com.podloot.eyemod.gui.apps.base;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.podloot.eyemod.Eye;
import com.podloot.eyemod.EyeClient;
import com.podloot.eyemod.gui.apps.App;
import com.podloot.eyemod.gui.elements.DeviceButton;
import com.podloot.eyemod.gui.util.Photos;
import com.podloot.eyemod.gui.util.Time;
import com.podloot.eyemod.lib.gui.EyeDraw;
import com.podloot.eyemod.lib.gui.EyeLib;
import com.podloot.eyemod.lib.gui.util.Line;
import com.podloot.eyemod.lib.gui.widgets.EyeSlider;
import com.podloot.eyemod.lib.gui.widgets.EyeToggle;
import com.podloot.eyemod.lib.gui.widgets.EyeWidget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.util.ResourceLocation;

public class AppCamera extends App {

	
	PointOfView per = PointOfView.FIRST_PERSON;
	double fov = 70;
	int panelColor = 0xCECECECE;
	int takePhoto = 0;
	
	EyeSlider zoom;
	
	public AppCamera() {
		super(new ResourceLocation(Eye.MODID, "textures/gui/apps/appcamera.png"), 0xff999999, "Eye");	
	
	}

	@Override
	public boolean load() {
		device.hidePanel(true, false);
		per = Minecraft.getInstance().options.getCameraType();
		fov = Minecraft.getInstance().options.fov;
		
		DeviceButton take = new DeviceButton(24, 24, EyeLib.PHOTO_TAKE);
		take.setAction(() -> {
			takePhoto = 5;
		});
		add(take, getWidth()/2 - 12, getHeight() - 28);
		
		DeviceButton normal = new DeviceButton(16, 16, EyeLib.PHOTO_NORMAL);
		normal.setAction(() -> {
			Minecraft.getInstance().options.setCameraType(PointOfView.FIRST_PERSON);
		});
		add(normal, 2 + 2, getHeight() - 24);
		
		DeviceButton selfie = new DeviceButton(16, 16, EyeLib.PHOTO_SELFIE);
		selfie.setAction(() -> {
			Minecraft.getInstance().options.setCameraType(PointOfView.THIRD_PERSON_FRONT);
		});
		add(selfie, 20 + 4, getHeight() - 24);
		
		DeviceButton person = new DeviceButton(16, 16, EyeLib.PHOTO_PERSON);
		person.setAction(() -> {
			Minecraft.getInstance().options.setCameraType(PointOfView.THIRD_PERSON_BACK);
		});
		add(person, 38 + 6, getHeight() - 24);
		
		zoom = new EyeSlider(getWidth()/2 - 18, 12, 0, 40);
		zoom.setState(21);
		add(zoom, getWidth()/2 + 16, getHeight()-22);
		
		return true;
	}
	
	@Override
	public void tick(int mx, int my) {
		if(zoom != null) Minecraft.getInstance().options.fov = (zoom.getValue()*2) + 30;
		
		if(takePhoto > 0) {
			switch(takePhoto) {
			case 3:
				Minecraft.getInstance().options.hideGui = true;
				device.hidePanel(true, true);
				this.hide(true);
				break;
			case 2:
				String save = "Photo_" + Time.getTime();
				save = save.replace(":", "_");
				boolean saved = false;
				if(device.settings.getBool("camera_res")) {
					saved = Photos.takeShot(save);
				} else {
					saved =Photos.takeShotLow(save);
				}
				if(saved) {
					device.addNotification(EyeClient.APPPHOTOS.getId(), "New photo: " + save);
				} else {
					device.addNotification(EyeClient.APPPHOTOS.getId(), "Failed to save: " + save);
				}
				break;
			case 1:
				Minecraft.getInstance().options.hideGui = false;
				device.hidePanel(true, false);
				this.hide(false);
				break;
			}
			takePhoto--;
		}
		super.tick(mx, my);
	}
	
	@Override
	public List<EyeWidget> getSettings(int width) {
		List<EyeWidget> settings = new ArrayList<EyeWidget>();
		EyeToggle res = new EyeToggle(20, 12);
		res.setText(new Line("text.eyemod.camera_resolution"));
		res.setState(device.settings.getBool("camera_res"));
		res.setAction(() -> {
			device.settings.setBool("camera_res", res.getToggle());
		});
		settings.add(res);
		return settings;
	}
	
	@Override
	public void draw(MatrixStack matrices, int x, int y) {
		EyeDraw.rect(matrices, x, y + getHeight() - 32, getWidth(), 32, 0x44444444);
		
		super.draw(matrices, x, y);
	}
	
	@Override
	public void close() {
		Minecraft.getInstance().options.setCameraType(per);
		Minecraft.getInstance().options.fov = fov;
		device.hidePanel(false, false);
		super.close();
	}
}
