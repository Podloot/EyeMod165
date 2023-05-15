package com.podloot.eyemod.gui.util.messages;

import java.util.ArrayList;
import java.util.List;

import com.podloot.eyemod.gui.util.Naming.Type;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.ResourceLocation;

public class MessageMsg extends EyeMsg {

	@Override
	public void handle(ItemStack stack, CompoundNBT msg) {
		CompoundNBT messages = stack.getTag().getCompound("messages");
		ListNBT conv = messages.getList(msg.getString("sen"), Type.STRING.type);
		conv.add(StringNBT.valueOf(msg.getString("msg")));
		
		if(conv.size() > 16) {
			List<INBT> remove = new ArrayList<INBT>();
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
