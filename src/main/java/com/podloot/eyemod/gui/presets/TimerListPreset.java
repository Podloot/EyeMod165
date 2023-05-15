package com.podloot.eyemod.gui.presets;

import com.podloot.eyemod.gui.apps.App;
import com.podloot.eyemod.gui.util.Timer;
import com.podloot.eyemod.lib.gui.EyeLib;
import com.podloot.eyemod.lib.gui.panels.EyePanel;
import com.podloot.eyemod.lib.gui.util.Color;
import com.podloot.eyemod.lib.gui.util.Line;
import com.podloot.eyemod.lib.gui.widgets.EyeButton;
import com.podloot.eyemod.lib.gui.widgets.EyeLabel;
import com.podloot.eyemod.lib.gui.widgets.EyeList;
import com.podloot.eyemod.lib.gui.widgets.EyePlane;
import com.podloot.eyemod.lib.gui.widgets.EyeVariable;

public class TimerListPreset extends Preset {

	EyeList timers = null;
	String filter = null;
	
	public TimerListPreset(App app, int width, int height, String filter) {
		super(app, width, height);
		this.setFilter(filter);
	}
	
	public void setFilter(String name) {
		filter = name;
		refresh();
	}
	
	public void loadList() {
		timers = new EyeList(getWidth(), getHeight(), Axis.VERTICAL);
		for(Timer t : app.getDevice().getTimers()) {
			if(t.getDeviceID() == app.getDevice().getUniqueID()) {
				if(filter == null || t.getName().equals(filter)) timers.add(this.getTimerPanel(t));
			}
		}
		add(timers, 0, 0);
	}
	
	public EyePanel getTimerPanel(Timer timer) {
		EyePanel pan = new EyePanel(getWidth()-10, 34);
		pan.setBack(Color.DARKGRAY);
		EyeLabel name = new EyeLabel(getWidth()-10 - 42 - 40, 14, new Line(timer.getName()));
		name.setBack(app.getAppColor());
		
		EyeLabel type = new EyeLabel(60, 14, timer.isStopwatch() ? new Line("text.eyemod.clock_stopwatch") : new Line("text.eyemod.clock_timer"));
		type.setBack(app.getAppColor());
		pan.add(name, 2, 2);
		pan.add(type, pan.getWidth() - 42 - 36, 2);
		
		EyeButton del = new EyeButton(14, 14, EyeLib.DELETE);
		del.setColor(Color.DARKGRAY, Color.RED);
		del.setAction(() -> {
			app.getDevice().stopTimer(timer);
			this.refresh();
		});
		pan.add(del, getWidth()-10-16, 2);
		
		
		
		EyeVariable time = new EyeVariable(getWidth()-10 - 20, 14, new Line("{v}"));
		time.setBack(app.getAppColor());
		time.setVariable(() -> {
			return timer.getTime();
		});
		pan.add(time, 18, 18);
		
		EyeButton pause = new EyeButton(14, 14, EyeLib.PLAY);
		pause.setAction(() -> {
			if(timer.pause) {
				timer.pause = false;
				time.setText(time.getText().setStyle(false, false));
				pause.setIcon(EyeLib.PLAY);
			} else {
				timer.pause = true;
				time.setText(time.getText().setStyle(false, true));
				pause.setIcon(EyeLib.PAUSE);
			}
		});
		pan.add(pause, 2, 18);
		return pan;
	}
	
	public void refresh() {
		if(timers != null) {
			timers.clearList();
			remove(timers);
		}
		this.loadList();
	}
	
	

}
