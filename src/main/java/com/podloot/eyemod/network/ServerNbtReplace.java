package com.podloot.eyemod.network;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.podloot.eyemod.Eye;
import com.podloot.eyemod.items.ItemDevice;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraftforge.fml.network.NetworkEvent;

public class ServerNbtReplace {
	
	int hand;
	CompoundNBT nbt;
	
	public ServerNbtReplace(CompoundNBT nbt, Hand hand) {
		this.nbt = nbt;
		this.hand = hand == Hand.MAIN_HAND ? 0 : 1;
	}
	
	public ServerNbtReplace(PacketBuffer buf) {
		this.nbt = buf.readNbt();
		this.hand = buf.readInt();
	}
	
	public void encode(PacketBuffer buf) {
		buf.writeNbt(nbt);
		buf.writeInt(hand);
	}
	
	public boolean handle(Supplier<NetworkEvent.Context> ctx) {
		final AtomicBoolean succes = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			ServerPlayerEntity p = ctx.get().getSender();
			Hand hand = this.hand == 0 ? Hand.MAIN_HAND : Hand.OFF_HAND;
			if(p.getItemInHand(hand) != null && p.getItemInHand(hand).getItem() == Eye.EYEPHONE.get()) {
				p.getItemInHand(hand).setTag(nbt);
				succes.set(true);
			}
		});
		ctx.get().setPacketHandled(true);
		return succes.get();
	}

}
