package com.podloot.eyemod.gui.util.messages;

import com.podloot.eyemod.gui.util.Naming.Type;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.ResourceLocation;

public abstract class EyeMsg {
	
	public EyeMsg() {
		
	}
	
	public abstract void handle(ItemStack stack, CompoundNBT msg);
	
	public void addNotification(ResourceLocation app, String msg, ItemStack stack) {
		if (stack.getTag().getCompound("settings").getBoolean("notification")) {
			ListNBT apps = stack.getTag().getList("apps", Type.STRING.type);
			if(apps.contains(StringNBT.valueOf("eyemod:messages"))) {
				ListNBT no = stack.getTag().getList("notifications", Type.STRING.type);
				no.add(StringNBT.valueOf(app.toString() + "~" + msg));
				stack.getTag().put("notifications", no);
			}
		}
	}

}
