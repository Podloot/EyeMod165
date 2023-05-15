package com.podloot.eyemod.gui.util.messages;

import java.util.ArrayList;
import java.util.List;

import com.podloot.eyemod.gui.util.Naming.Type;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class MessageMsg extends EyeMsg {

	@Override
	public void handle(ItemStack stack, CompoundTag msg) {
		CompoundTag messages = stack.getTag().getCompound("messages");
		ListTag conv = messages.getList(msg.getString("sen"), Type.STRING.type);
		conv.add(StringTag.valueOf(msg.getString("msg")));
		
		if(conv.size() > 16) {
			List<Tag> remove = new ArrayList<Tag>();
			for(int i = 0; i < conv.size()-16; i++) {
				remove.add(conv.get(i));
			}
			conv.removeAll(remove);
		}
		
		messages.put(msg.getString("sen"), conv);
		stack.getTag().put("messages", messages);
		addNotification(new ResourceLocation("eyemod:messages"), "From: " + msg.getString("sen"), stack);
	}

}
