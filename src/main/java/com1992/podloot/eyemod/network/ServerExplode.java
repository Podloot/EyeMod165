package com.podloot.eyemod.network;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

public class ServerExplode {

	BlockPos pos;
	ResourceLocation world;
	int size;
	boolean fire;
	boolean dmg;

	public ServerExplode(BlockPos pos, ResourceLocation world, int size, boolean fire, boolean dmg) {
		this.pos = pos;
		this.world = world;
		this.size = size;
		this.fire = fire;
		this.dmg = dmg;
	}

	public ServerExplode(FriendlyByteBuf buf) {
		this.pos = buf.readBlockPos();
		this.world = buf.readResourceLocation();
		this.size = buf.readInt();
		this.fire = buf.readBoolean();
		this.dmg = buf.readBoolean();
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeBlockPos(pos);
		buf.writeResourceLocation(world);
		buf.writeInt(size);
		buf.writeBoolean(fire);
		buf.writeBoolean(dmg);
	}

	public boolean handle(Supplier<NetworkEvent.Context> ctx) {
		final var succes = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			ResourceKey<Level> key = Level.OVERWORLD;
			for (ResourceKey<Level> w : ctx.get().getSender().server.levelKeys()) {
				if (w.location().equals(world)) {
					key = w;
					break;
				}
			}
			ctx.get().getSender().getServer().getLevel(key).explode(ctx.get().getSender(), DamageSource.explosion(ctx.get().getSender()), null, pos.getX(), pos.getY(),
					pos.getZ(), size, fire, dmg ? Explosion.BlockInteraction.BREAK : Explosion.BlockInteraction.NONE);
			succes.set(true);
		});
		ctx.get().setPacketHandled(true);
		return succes.get();
	}

}
