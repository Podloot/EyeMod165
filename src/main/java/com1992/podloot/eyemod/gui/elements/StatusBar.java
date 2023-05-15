package com.podloot.eyemod.gui.elements;

import com.mojang.blaze3d.vertex.PoseStack;
import com.podloot.eyemod.gui.GuiDevice;
import com.podloot.eyemod.gui.util.Time;
import com.podloot.eyemod.lib.gui.EyeDraw;
import com.podloot.eyemod.lib.gui.EyeLib;
import com.podloot.eyemod.lib.gui.panels.EyePanel;
import com.podloot.eyemod.lib.gui.util.Line;
import com.podloot.eyemod.lib.gui.widgets.EyeIcon;
import com.podloot.eyemod.lib.gui.widgets.EyeState;

public class StatusBar extends EyePanel {
	
	GuiDevice device;
	EyeState wifi;
	EyeState reach;
	EyeState battery;

	public StatusBar(GuiDevice device, int width, int height) {
		super(width, height);
		this.device = device;
		
		EyeIcon logo = new EyeIcon(8, 8, EyeLib.EYE);
		add(logo, 1, 0);
		
		wifi = new EyeState(8, 8, EyeLib.CONNECTION, 4);
		int connect = device.connect.getConnection();
		wifi.setState(connect);
		add(wifi, 16, 0);
		
		reach = new EyeState(5, 8, EyeLib.COVERAGE, 4);
		reach.setState(device.connect.getReach());
		add(reach, 10, 0);
		
		battery = new EyeState(10, 8, EyeLib.BATTERY, 8);
		add(battery, width-11, 0);
		
		DeviceButton console = new DeviceButton(8, 8, EyeLib.CONSOLE);
		console.setAction(() -> {
			if(!device.isLocked()) {
				device.openConsole();
			}
			
		});
		add(console, width - 21, 0);
	}
	
	@Override
	public void draw(PoseStack matrices, int x, int y) {
		EyeDraw.rect(matrices, x, y, getWidth(), getHeight(), 0x55000000);
		EyeDraw.text(matrices, new Line(Time.getTime()).setScale(0.8F), x + (getWidth()/2), y + (getHeight()/2));
		super.draw(matrices, x, y);
	}
	
	@Override
	public void tick(int mx, int my) {
		int dmg = device.getItem().getDamageValue();
		int max = device.getItem().getMaxDamage();
		int state = (int)(dmg / (max/8));
		battery.setState(7-state);
		
		int connect = device.connect.getConnection();
		wifi.setState(connect);
		
		super.tick(mx, my);
	}

}
