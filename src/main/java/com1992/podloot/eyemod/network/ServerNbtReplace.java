package com.podloot.eyemod.network;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.podloot.eyemod.items.ItemDevice;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

public class ServerNbtReplace {
	
	int hand;
	CompoundTag nbt;
	
	public ServerNbtReplace(CompoundTag nbt, InteractionHand hand) {
		this.nbt = nbt;
		this.hand = hand == InteractionHand.MAIN_HAND ? 0 : 1;
	}
	
	public ServerNbtReplace(FriendlyByteBuf buf) {
		this.nbt = buf.readNbt();
		this.hand = buf.readInt();
	}
	
	public void encode(FriendlyByteBuf buf) {
		buf.writeNbt(nbt);
		buf.writeInt(hand);
	}
	
	public boolean handle(Supplier<NetworkEvent.Context> ctx) {
		final var succes = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			Player p = ctx.get().getSender();
			InteractionHand hand = this.hand == 0 ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
			if(p.getItemInHand(hand) != null && p.getItemInHand(hand).getItem() instanceof ItemDevice) {
				p.getItemInHand(hand).setTag(nbt);
				succes.set(true);
			}
		});
		ctx.get().setPacketHandled(true);
		return succes.get();
	}

}
