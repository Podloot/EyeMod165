package com.podloot.eyemod.network;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

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

	public ServerExplode(PacketBuffer buf) {
		this.pos = buf.readBlockPos();
		this.world = buf.readResourceLocation();
		this.size = buf.readInt();
		this.fire = buf.readBoolean();
		this.dmg = buf.readBoolean();
	}

	public void encode(PacketBuffer buf) {
		buf.writeBlockPos(pos);
		buf.writeResourceLocation(world);
		buf.writeInt(size);
		buf.writeBoolean(fire);
		buf.writeBoolean(dmg);
	}

	public boolean handle(Supplier<NetworkEvent.Context> ctx) {
		final AtomicBoolean succes = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			RegistryKey<World> key = World.OVERWORLD;
			for (RegistryKey<World> w : ctx.get().getSender().server.levelKeys()) {
				if (w.location().equals(world)) {
					key = w;
					break;
				}
			}
			ctx.get().getSender().getServer().getLevel(key).explode(ctx.get().getSender(), DamageSource.explosion(ctx.get().getSender()), null, pos.getX(), pos.getY(),
					pos.getZ(), size, fire, dmg ? Explosion.Mode.BREAK : Explosion.Mode.NONE);
			succes.set(true);
		});
		ctx.get().setPacketHandled(true);
		return succes.get();
	}

}
