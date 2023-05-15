package com.podloot.eyemod.network;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

public class ServerKick {

	String player;
	String reason;

	public ServerKick(String player, String reason) {
		this.player = player;
		this.reason = reason;
	}

	public ServerKick(PacketBuffer buf) {
		this.player = buf.readUtf();
		this.reason = buf.readUtf();
	}

	public void encode(PacketBuffer buf) {
		buf.writeUtf(player);
		buf.writeUtf(reason);
	}

	public boolean handle(Supplier<NetworkEvent.Context> ctx) {
		final AtomicBoolean succes = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			ServerPlayerEntity p = ctx.get().getSender().getServer().getPlayerList().getPlayerByName(player);
			if(p == null) return;			
			p.connection.disconnect(new StringTextComponent(reason));
			succes.set(true);
		});
		ctx.get().setPacketHandled(true);
		return succes.get();
	}

}
