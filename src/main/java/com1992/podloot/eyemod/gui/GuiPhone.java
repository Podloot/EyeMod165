package com.podloot.eyemod.gui;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;

public class GuiPhone extends GuiDevice {

	public GuiPhone(InteractionHand hand, CompoundTag config, boolean operator, boolean opApps) {
		super("Phone", 170, 240, hand, config, operator, opApps);
		
	}

}
