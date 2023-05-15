package com.podloot.eyemod.network;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.podloot.eyemod.config.EyeConfigData;
import com.podloot.eyemod.lib.gui.util.Pos;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class ClientGuiOpen {
	
	String id;
	int hand;
	int gui;
	CompoundTag config;
	boolean opApps;
	boolean operator;
	String pos;
	
	public ClientGuiOpen(String id, InteractionHand hand, EyeConfigData config, boolean opApps, boolean operator) {
		this.id = id;
		this.hand = hand == InteractionHand.MAIN_HAND ? 0 : 1;
		this.gui = 0;
		this.config = config.data;
		this.opApps = opApps;
		this.operator = operator;
		this.pos = "";
	}
	
	public ClientGuiOpen(String id, InteractionHand hand, Pos pos, boolean canOpen) {
		this.id = id;
		this.hand = hand == InteractionHand.MAIN_HAND ? 0 : 1;
		this.gui = 1;
		this.config = new CompoundTag();
		this.opApps = canOpen;
		this.operator = false;
		this.pos = pos.toNbt();
	
	}
	
	public ClientGuiOpen(FriendlyByteBuf buf) {
		id = buf.readUtf();
		hand = buf.readInt();
		gui = buf.readInt();
		config = buf.readNbt();
		opApps = buf.readBoolean();
		operator = buf.readBoolean();
		pos = buf.readUtf();
	}
	
	public void encode(FriendlyByteBuf buf) {
		buf.writeUtf(id);
		buf.writeInt(hand);
		buf.writeInt(gui);
		buf.writeNbt(config);
		buf.writeBoolean(opApps);
		buf.writeBoolean(operator);
		buf.writeUtf(pos);
	}
	
	public boolean handle(Supplier<NetworkEvent.Context> ctx) {
		final var succes = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> succes.set(ClientAccess.openDevice(id, hand, gui, config, opApps, operator, new Pos().fromString(pos))));
		});
		ctx.get().setPacketHandled(true);
		return succes.get();
	}

}
