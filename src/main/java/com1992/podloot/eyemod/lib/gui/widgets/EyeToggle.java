package com.podloot.eyemod.lib.gui.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import com.podloot.eyemod.gui.util.Music;
import com.podloot.eyemod.lib.gui.EyeDraw;
import com.podloot.eyemod.lib.gui.EyeLib;
import com.podloot.eyemod.lib.gui.util.Line;
import com.podloot.eyemod.lib.gui.util.Space;

import net.minecraft.sounds.SoundEvents;

public class EyeToggle extends EyeInteract {
	
	boolean state;
	int size = 12;
	Space space;
	
	public EyeToggle(int width, int height) {
		super(width, height);
		space = new Space(width/2 - 10, height/2 - 6, 20, 12);
		size = space.height < space.width ? space.height : -1;
		setColor(0xffCECECE, 0xff6cc06b);
	}
	
	public void setState(boolean state) {
		this.state = state;
	}
	
	public void setText(Line text) {
		this.text = text.setAllingment(1, 0);
		space = new Space(0, height/2 - 6, 20, 12);
	}
	
	public void setToggleSpace(Space space) {
		this.space = space;
	}

	@Override
	public void draw(PoseStack matrices, int x, int y) {
		super.draw(matrices, x, y);
		EyeDraw.nine(matrices, EyeLib.SLIDER, x + space.x, y + space.y, space.width, space.height, state ? secondary : primary);
		EyeDraw.nine(matrices, EyeLib.SLIDER_BORDER, x+space.x, y + space.y, space.width, space.height, this.isHovered() ? 0xffFFFFFF : 0xff000000);
		if(size > 0) {
			EyeDraw.nine(matrices, EyeLib.SLIDER, x + space.x + (state ? space.width - size : 0), y + space.y, size, space.height, primary);
		}
		if(text != null) {
			this.drawText(matrices, x + space.width + 2, y, width-space.width-2, height);
		}
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if(isEnabled()) {
			Music.sound(SoundEvents.UI_BUTTON_CLICK);
			state = !state;
			if(action != null) action.run();
		}
		return true;
	}
	
	public boolean getToggle() {
		return state;
	}

}
