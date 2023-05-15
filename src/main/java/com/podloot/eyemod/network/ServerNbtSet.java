package com.podloot.eyemod.network;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.podloot.eyemod.Eye;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraftforge.fml.network.NetworkEvent;

public class ServerNbtSet {
	
	int hand;
	String key;
	CompoundNBT tag;
	
	public ServerNbtSet(String key, INBT tag, Hand hand) { 
		this.key = key;
		CompoundNBT nbt = new CompoundNBT();
		nbt.put("v", tag);
		this.tag = nbt;
		this.hand = hand == Hand.MAIN_HAND ? 0 : 1;
	}
	
	public ServerNbtSet(PacketBuffer buf) {
		this.key = buf.readUtf();
		this.tag = buf.readNbt();
		this.hand = buf.readInt();
	}
	
	public void encode(PacketBuffer buf) {
		buf.writeUtf(key);
		buf.writeNbt(tag);
		buf.writeInt(hand);
	}
	
	public boolean handle(Supplier<NetworkEvent.Context> ctx) {
		final AtomicBoolean succes = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			ServerPlayerEntity p = ctx.get().getSender();
			Hand hand = this.hand == 0 ? Hand.MAIN_HAND : Hand.OFF_HAND;
			if(p.getItemInHand(hand) != null && p.getItemInHand(hand).getItem() == Eye.EYEPHONE.get()) {
				p.getItemInHand(hand).getTag().put(key, tag.get("v"));
				succes.set(true);
			}
		});
		ctx.get().setPacketHandled(true);
		return succes.get();
	}

}
