package com.podloot.eyemod.gui;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;

public class GuiPhone extends GuiDevice {

	public GuiPhone(Hand hand, CompoundNBT config, boolean operator, boolean opApps) {
		super("Phone", 170, 240, hand, config, operator, opApps);
		
	}

}
