package com.podloot.eyemod.gui.apps.base;

import com.podloot.eyemod.Eye;
import com.podloot.eyemod.gui.apps.App;
import com.podloot.eyemod.gui.presets.TimerListPreset;
import com.podloot.eyemod.gui.presets.TimerPreset;
import com.podloot.eyemod.gui.util.Time;
import com.podloot.eyemod.gui.util.Timer;
import com.podloot.eyemod.lib.gui.util.Line;
import com.podloot.eyemod.lib.gui.widgets.EyeButton;
import com.podloot.eyemod.lib.gui.widgets.EyeSwitch;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;

public class AppClock extends App {

	TimerListPreset timers;
	
	public AppClock() {
		super(new ResourceLocation(Eye.MODID, "textures/gui/apps/appclock.png"), 0xff999999, "Eye");
	}

	@Override
	public boolean load() {
		timers = new TimerListPreset(this, getWidth()-4, getHeight()-38, null);
		add(timers, 2, 2);
		
		EyeSwitch type = new EyeSwitch(getWidth()-4, 14);
		type.addState(new Line("text.eyemod.clock_timer"));
		type.addState(new Line("text.eyemod.clock_stopwatch"));
		
		add(type, 2, getHeight() - 34);
		
		TimerPreset time = new TimerPreset(this, 68, 16);
		add(time, 2, getHeight() - 18);
		
		EyeButton start = new EyeButton(74, 16, new Line("text.eyemod.start"));
		start.setAction(() -> {
			Timer t = new Timer(device.getUniqueID(), Time.getTime(), () -> {
				Minecraft.getInstance().player.playSound(SoundEvents.NOTE_BLOCK_COW_BELL, 1, 1);
			}, time.getTime());
			if(type.getState() == 1) {
				t.setStopwatch();
			}
			device.addTimer(t);
			timers.refresh();
		});
		add(start, getWidth() - 76, getHeight() - 18);
		
		return false;
	}
}
