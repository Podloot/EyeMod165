package com.podloot.eyemod.gui.util.messages;

import com.podloot.eyemod.gui.util.Naming.Type;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public abstract class EyeMsg {
	
	public EyeMsg() {
		
	}
	
	public abstract void handle(ItemStack stack, CompoundTag msg);
	
	public void addNotification(ResourceLocation app, String msg, ItemStack stack) {
		if (stack.getTag().getCompound("settings").getBoolean("notification")) {
			ListTag apps = stack.getTag().getList("apps", Type.STRING.type);
			if(apps.contains(StringTag.valueOf("eyemod:messages"))) {
				ListTag no = stack.getTag().getList("notifications", Type.STRING.type);
				no.add(StringTag.valueOf(app.toString() + "~" + msg));
				stack.getTag().put("notifications", no);
			}
		}
	}

}
