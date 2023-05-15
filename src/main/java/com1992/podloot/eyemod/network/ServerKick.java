package com.podloot.eyemod.network;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

public class ServerKick {

	String player;
	String reason;

	public ServerKick(String player, String reason) {
		this.player = player;
		this.reason = reason;
	}

	public ServerKick(FriendlyByteBuf buf) {
		this.player = buf.readUtf();
		this.reason = buf.readUtf();
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeUtf(player);
		buf.writeUtf(reason);
	}

	public boolean handle(Supplier<NetworkEvent.Context> ctx) {
		final var succes = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			ServerPlayer p = ctx.get().getSender().getServer().getPlayerList().getPlayerByName(player);
			if(p == null) return;			
			p.connection.disconnect(Component.literal(reason));
			succes.set(true);
		});
		ctx.get().setPacketHandled(true);
		return succes.get();
	}

}
