package com.podloot.eyemod.gui.apps.op;

import com.podloot.eyemod.Eye;
import com.podloot.eyemod.gui.apps.App;
import com.podloot.eyemod.gui.util.Time;
import com.podloot.eyemod.lib.gui.EyeLib;
import com.podloot.eyemod.lib.gui.util.Line;
import com.podloot.eyemod.lib.gui.widgets.EyeButton;
import com.podloot.eyemod.lib.gui.widgets.EyeIcon;
import com.podloot.eyemod.lib.gui.widgets.EyePlane;
import com.podloot.eyemod.lib.gui.widgets.EyeSlider;
import com.podloot.eyemod.network.PacketHandler;
import com.podloot.eyemod.network.ServerWeather;

import net.minecraft.util.ResourceLocation;

public class AppWorld extends App {

	public AppWorld() {
		super(new ResourceLocation(Eye.MODID, "textures/gui/apps/appworld.png"), 0xff2c2775, "EyeOP");
	}

	@Override
	public boolean load() {
		EyeButton sun = new EyeButton(32, 32, EyeLib.SUNNY);
		sun.setOffset(2);
		sun.setColor(appColor);
		boolean snow = device.getUser().clientLevel.getBiome(device.getUser().blockPosition()).getTemperature(device.getUser().blockPosition()) > 0.2;
		EyeButton rain = new EyeButton(32, 32, snow ? EyeLib.SNOW : EyeLib.RAIN);
		rain.setOffset(2);
		rain.setColor(appColor);
		EyeButton thunder = new EyeButton(32, 32, EyeLib.THUNDER);
		thunder.setColor(appColor);
		thunder.setOffset(2);
		
		sun.setAction(() -> {
			PacketHandler.INSTANCE.sendToServer(new ServerWeather(1, 1000000));
		});
		
		rain.setAction(() -> {
			PacketHandler.INSTANCE.sendToServer(new ServerWeather(2, 1000000));
		});
		
		thunder.setAction(() -> {
			PacketHandler.INSTANCE.sendToServer(new ServerWeather(3, 1000000));
		});
		
		add(sun, 8, 8);
		add(rain, getWidth()/2-16, 9);
		add(thunder, getWidth() - 40, 8);
		
		EyeButton morning = new EyeButton(24, 24, EyeLib.MORNING);
		morning.setColor(appColor);
		morning.setOffset(2);
		EyeButton midday = new EyeButton(24, 24, EyeLib.MIDDAY);
		midday.setColor(appColor);
		midday.setOffset(2);
		EyeButton evening = new EyeButton(24, 24, EyeLib.EVENING);
		evening.setColor(appColor);
		evening.setOffset(2);
		EyeButton midnight = new EyeButton(24, 24, EyeLib.MIDNIGHT);
		midnight.setColor(appColor);
		midnight.setOffset(2);
		
		morning.setAction(() -> {
			PacketHandler.INSTANCE.sendToServer(new ServerWeather(0, 0));
		});
		
		midday.setAction(() -> {
			PacketHandler.INSTANCE.sendToServer(new ServerWeather(0, 4000));
		});
		
		evening.setAction(() -> {
			PacketHandler.INSTANCE.sendToServer(new ServerWeather(0, 12000));
		});
		
		midnight.setAction(() -> {
			PacketHandler.INSTANCE.sendToServer(new ServerWeather(0, 16000));
		});
		
		add(morning, 8, 48);
		add(midday, getWidth()/2 - 30, 48);
		add(evening, getWidth()/2 + 6, 48);
		add(midnight, getWidth()-32, 48);
		
		EyePlane back = new EyePlane(getWidth()-4, 32, appColor);
		add(back, 2, getHeight()-34);

		
		EyeIcon cycle = new EyeIcon(144, 32, EyeLib.CYCLE);
		add(cycle, 3, getHeight()-68);
		
		
		EyeSlider time = new EyeSlider(getWidth()-12, 24, 0, 48);
		time.setText(new Line("text.eyemod.world_time"));
		time.setShowValue(() -> Time.time((time.getValue()*500) + 16000));
		time.setAction(() -> {
			PacketHandler.INSTANCE.sendToServer(new ServerWeather(0, (time.getValue()*500) + 16000));
		});
		add(time, 6, getHeight()-30);
		
		return true;
	}

}
