package com.podloot.eyemod.network;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;
import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

public class ServerItemRemove {
	
	ItemStack stack;
	
	public ServerItemRemove(ItemStack stack) {
		this.stack = stack;
	}
	
	public ServerItemRemove(FriendlyByteBuf buf) {
		this.stack = buf.readItem();
	}
	
	public void encode(FriendlyByteBuf buf) {
		buf.writeItem(stack);
	}
	
	public boolean handle(Supplier<NetworkEvent.Context> ctx) {
		final var succes = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			int amount = stack.getCount();
			Player p = ctx.get().getSender();
			 int count = p.getInventory().countItem(stack.getItem());
		       if(count >= amount) {
		    	   Predicate<ItemStack> isStack = (ItemStack t) -> t.getItem() == stack.getItem();
		    	   p.getInventory().clearOrCountMatchingItems(isStack, stack.getCount(), null);
		    	   succes.set(true);
		       }
		});
		ctx.get().setPacketHandled(true);
		return succes.get();
	}

}
