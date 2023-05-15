package com.podloot.eyemod.network;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

public class ServerTeleport {

	String player;
	BlockPos pos;
	ResourceLocation world;
	String target;

	public ServerTeleport(String player, BlockPos pos, ResourceLocation world) {
		this.player = player;
		this.pos = pos;
		this.world = world;
		this.target = "";
	}
	
	public ServerTeleport(String player, String target) {
		this.player = player;
		this.pos = new BlockPos(0, 0, 0);
		this.world = new ResourceLocation("minecraft:overworld");
		this.target = target;
	}

	public ServerTeleport(FriendlyByteBuf buf) {
		this.player = buf.readUtf();
		this.pos = buf.readBlockPos();
		this.world = buf.readResourceLocation();
		this.target = buf.readUtf();
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeUtf(player);
		buf.writeBlockPos(pos);
		buf.writeResourceLocation(world);
		buf.writeUtf(target);
	}

	public boolean handle(Supplier<NetworkEvent.Context> ctx) {
		final var succes = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			ServerPlayer p = ctx.get().getSender().getServer().getPlayerList().getPlayerByName(player);
			if(p == null) return;			
			if(target.isEmpty() && pos != null && world != null) {
				ResourceKey<Level> key = Level.OVERWORLD;
				for (ResourceKey<Level> w : ctx.get().getSender().server.levelKeys()) {
					if (w.location().equals(world)) {
						key = w;
						break;
					}
				}
				p.teleportTo(ctx.get().getSender().getServer().getLevel(key), pos.getX(), pos.getY(), pos.getZ(), p.yRotO, p.xRotO);
				succes.set(true);
			} else if(target != null && !target.isEmpty()) {
				ServerPlayer t = ctx.get().getSender().getServer().getPlayerList().getPlayerByName(target);
				BlockPos bp = t.blockPosition();
				p.teleportTo(t.getLevel(), bp.getX(), bp.getY(), bp.getZ(), t.yRotO, t.xRotO);
				succes.set(true);
			}
		});
		ctx.get().setPacketHandled(true);
		return succes.get();
	}

}
