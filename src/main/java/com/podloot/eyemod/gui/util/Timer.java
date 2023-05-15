package com.podloot.eyemod.gui.util;

public class Timer {
	
	String deviceID;
	
	String name = "Timer";
	boolean done = false;
	boolean stopwatch = false;
	int ticks = 0;
	int s = 0;
	Runnable run;
	Runnable tick;
	public boolean pause;
	
	public Timer(String deviceid, String name, Runnable run, int seconds) {
		this.deviceID = deviceid;
		this.name = name;
		this.run = run;
		this.ticks = seconds*20;
	}
	
	public void setTick(Runnable tick, int s) {
		this.tick = tick;
		this.s = s <= 0 ? 1 : s;
	}
	
	public void setStopwatch() {
		this.stopwatch = true;
	}
	
	public void tick() {
		if(pause || done) return;
		if(ticks >= 20 || stopwatch) {
			if(tick != null) {
				if(ticks % s == 0) {
					tick.run();
				}
			}
			ticks += stopwatch ? 1 : -1;
		} else {
			run.run();
			done = true;
		}
	}
	
	public boolean done() {
		return done;
	}
	
	public String getTime() {
		int total = (ticks/20);
		int minutes = total/60;
		int seconds = (total) % 60;
		return String.format("%02d:%02d", minutes, seconds);
	}

	public String getName() {
		return name;
	}

	public boolean isStopwatch() {
		return stopwatch;
	}

	public String getDeviceID() {
		return deviceID;
	}

}
