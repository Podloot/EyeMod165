package com.podloot.eyemod.gui.apps;

import java.util.List;

import com.podloot.eyemod.Eye;
import com.podloot.eyemod.gui.elements.AppButton;

import net.minecraft.resources.ResourceLocation;

public class AppHome extends App {
	
	int page = 0;

	public AppHome(int page) {
		super(new ResourceLocation(Eye.MODID, "textures/gui/appdefault.png"), 0xffCECECE, "Eye");
		isStock = true;
		this.page = page;
	}

	@Override
	public boolean load() {
		openPage(page);
		return false;
	}
	
	public void openPage(int page) {
		List<App> apps = device.getInstalledApps();
		if(apps.size() < page*12+1) return;
		device.setLastPage(page);
		this.clear();
		int sx = getWidth() / 3;
		int sy = (getHeight()-4) / 4;
		this.page = page;
		for(int x = 0; x < 3; x++) {
			for(int y = 0; y < 4; y++) {
				final int id = x + y*3 + page*12;
				if(id < apps.size()) {
					App app = apps.get(id);
					AppButton app_button = new AppButton(app);
					app_button.setAction(() -> {
						device.openApp(app);
					});
					add(app_button, x*sx + (sx/2 - 16), (y*sy + (sy/2 - 16)));
				}
			}
		}
	}
	
	@Override
	public void onHome() {
		if(page != 0) {
			openPage(0);
		} else {
			if(device != null) device.closeDevice();
		}
	}
	
	@Override
	public void onLeft() {
		if(page > 0) {
			openPage(page-1);
		}
	}
	
	public void onRight() {
		openPage(page+1);
	}
	
	
	

}
