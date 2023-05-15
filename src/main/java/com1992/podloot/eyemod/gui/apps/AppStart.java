package com.podloot.eyemod.gui.apps;

import java.util.HashMap;

import com.mojang.blaze3d.vertex.PoseStack;
import com.podloot.eyemod.Eye;
import com.podloot.eyemod.gui.util.Naming.Type;
import com.podloot.eyemod.gui.util.Time;
import com.podloot.eyemod.lib.gui.EyeDraw;
import com.podloot.eyemod.lib.gui.EyeLib;
import com.podloot.eyemod.lib.gui.panels.EyePanel;
import com.podloot.eyemod.lib.gui.util.Image;
import com.podloot.eyemod.lib.gui.util.Line;
import com.podloot.eyemod.lib.gui.widgets.EyeIcon;
import com.podloot.eyemod.lib.gui.widgets.EyeLabel;
import com.podloot.eyemod.lib.gui.widgets.EyeList;
import com.podloot.eyemod.lib.gui.widgets.EyePlane;
import com.podloot.eyemod.lib.gui.widgets.EyeWidget;

import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;

public class AppStart extends App {
	
	public boolean locked = true;
	
	HashMap<String, Image> weather = new HashMap<String, Image>();
	
	EyeList notifications;

	public AppStart() {
		super(new ResourceLocation(Eye.MODID, "textures/gui/appdefault.png"), 0xffCECECE, "Eye");
		isStock = true;
		weather.put("text.eyemod.weather_sunny", EyeLib.SUNNY);
		weather.put("text.eyemod.weather_raining", EyeLib.RAIN);
		weather.put("text.eyemod.weather_snowing", EyeLib.SNOW);
		weather.put("text.eyemod.weather_thunder", EyeLib.THUNDER);
	}
	
	@Override
	public boolean load() {
		notifications = new EyeList(getWidth()-10, 70, Axis.VERTICAL);
		notifications.hideScrollbar(true);
		ListTag no = device.data.getList("notifications", Type.STRING);
		for(int i = no.size()-1; i >= 0; i--) {
			String s = no.getString(i);
			if(s.contains("~")) {
				String[] in = s.split("~");
				if(in.length > 1) {
					App a = device.getApp(new ResourceLocation(in[0]));
					EyePanel np = getNote(a, in[1]);
					if(a != null && np != null) notifications.add(np);
				}
			}
		}
		add(notifications, 10, 84);
		
		locked = device.isLocked();
		EyeIcon lock = new EyeIcon(24, 24, locked ? EyeLib.LOCKED : EyeLib.UNLOCKED);
		add(lock, getWidth()/2 - 12, getHeight() - 28);
		
		EyePanel weath = new EyePanel(64, 24);
		
		String wn = device.getWeather();
		if(weather.containsKey(wn)) {
			EyeIcon w = new EyeIcon(24, 24, weather.get(wn));
			weath.add(w, 0, 0);
		}
		EyeLabel wl = new EyeLabel(42, 16, new Line(wn).setStyle(true, false));
		wl.setAlignment(1, 1);
		weath.add(wl, 26, 1);
		
		EyeLabel temp = new EyeLabel(42, 16, new Line("text.eyemod.weather_degrees").setStyle(false, true));
		temp.setAlignment(1, 1);
		temp.setVariable(device.getTemp() + "");
		weath.add(temp, 26, 11);
		
		add(weath, getWidth()/2 - 44, 54);
		
		return true;
	}
	
	@Override
	public void draw(PoseStack matrices, int x, int y) {
		EyeDraw.text(matrices, new Line(Time.getTime()).setScale(4F), x + (getWidth()/2), y + 36);
		super.draw(matrices, x, y);
	}
	
	public EyePanel getNote(App app, String msg) {
		EyeWidget a = app.getNotification(msg, notifications.getWidth()-28);
		EyePanel n = new EyePanel(notifications.getWidth()-10, a.getHeight());
		EyePlane back = new EyePlane(16, 16, app.getAppColor());
		EyeIcon icon = new EyeIcon(16, 16, app.appIcon);
		n.add(back, 0, a.getHeight()/2 - 8);
		n.add(icon, 0, a.getHeight()/2 - 8);
		n.add(a, 18, 0);
		return n;
	}
	
	@Override
	public void onHome() {
		if(!locked) {
			super.onHome();
		}
	}

}
