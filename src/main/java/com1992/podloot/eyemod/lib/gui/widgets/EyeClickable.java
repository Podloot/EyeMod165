package com.podloot.eyemod.lib.gui.widgets;

import com.podloot.eyemod.gui.util.Music;

import net.minecraft.sounds.SoundEvents;

public abstract class EyeClickable extends EyeInteract {

	public EyeClickable(int width, int height) {
		super(width, height);
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if(isEnabled()) {
			Music.sound(SoundEvents.UI_BUTTON_CLICK);
			if(action != null) action.run();
		}
		return true;
	}

}
