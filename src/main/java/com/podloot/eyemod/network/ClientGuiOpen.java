package com.podloot.eyemod.network;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.podloot.eyemod.lib.gui.util.Pos;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

public class ClientGuiOpen {
	
	String id;
	int hand;
	int gui;
	boolean opApps;
	boolean operator;
	String pos;
	
	public ClientGuiOpen(String id, Hand hand, boolean opApps, boolean operator) {
		this.id = id;
		this.hand = hand == Hand.MAIN_HAND ? 0 : 1;
		this.gui = 0;
		this.opApps = opApps;
		this.operator = operator;
		this.pos = "";
	}
	
	public ClientGuiOpen(String id, Hand hand, Pos pos, boolean canOpen) {
		this.id = id;
		this.hand = hand == Hand.MAIN_HAND ? 0 : 1;
		this.gui = 1;
		this.opApps = canOpen;
		this.operator = false;
		this.pos = pos.toNbt();
	
	}
	
	public ClientGuiOpen(PacketBuffer buf) {
		id = buf.readUtf();
		hand = buf.readInt();
		gui = buf.readInt();
		opApps = buf.readBoolean();
		operator = buf.readBoolean();
		pos = buf.readUtf();
	}
	
	public void encode(PacketBuffer buf) {
		buf.writeUtf(id);
		buf.writeInt(hand);
		buf.writeInt(gui);
		buf.writeBoolean(opApps);
		buf.writeBoolean(operator);
		buf.writeUtf(pos);
	}
	
	public boolean handle(Supplier<NetworkEvent.Context> ctx) {
		final AtomicBoolean succes = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> succes.set(ClientAccess.openDevice(id, hand, gui, opApps, operator, new Pos().fromString(pos))));
		});
		ctx.get().setPacketHandled(true);
		return succes.get();
	}

}
