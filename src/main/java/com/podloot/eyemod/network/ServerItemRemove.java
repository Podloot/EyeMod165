package com.podloot.eyemod.network;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;
import java.util.function.Supplier;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class ServerItemRemove {
	
	ItemStack stack;
	
	public ServerItemRemove(ItemStack stack) {
		this.stack = stack;
	}
	
	public ServerItemRemove(PacketBuffer buf) {
		this.stack = buf.readItem();
	}
	
	public void encode(PacketBuffer buf) {
		buf.writeItem(stack);
	}
	
	public boolean handle(Supplier<NetworkEvent.Context> ctx) {
		final AtomicBoolean succes = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			int amount = stack.getCount();
			ServerPlayerEntity p = ctx.get().getSender();
			 int count = p.inventory.countItem(stack.getItem());
		       if(count >= amount) {
		    	   Predicate<ItemStack> isStack = (ItemStack t) -> t.getItem() == stack.getItem();
		    	   p.inventory.clearOrCountMatchingItems(isStack, amount, null);
		    	   succes.set(true);
		       }
		});
		ctx.get().setPacketHandled(true);
		return succes.get();
	}

}
