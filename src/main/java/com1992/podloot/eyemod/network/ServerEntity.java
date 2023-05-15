package com.podloot.eyemod.network;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.podloot.eyemod.lib.gui.util.Line;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

public class ServerEntity {

	BlockPos pos;
	ResourceLocation world;
	String entity;
	int amount;
	String name;

	public ServerEntity(BlockPos pos, ResourceLocation world, String entity, int amount, String name) {
		this.pos = pos;
		this.world = world;
		this.entity = entity;
		this.amount = amount;
		this.name = name;
	}

	public ServerEntity(FriendlyByteBuf buf) {
		this.pos = buf.readBlockPos();
		this.world = buf.readResourceLocation();
		this.entity = buf.readUtf();
		this.amount = buf.readInt();
		this.name = buf.readUtf();
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeBlockPos(pos);
		buf.writeResourceLocation(world);
		buf.writeUtf(entity);
		buf.writeInt(amount);
		buf.writeUtf(name);
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
			for(int i = 0; i < amount; i++) {
				CompoundTag nbtCompound = new CompoundTag();
				nbtCompound.putString("id", entity);
				Entity entity22 = EntityType.loadEntityRecursive(nbtCompound, ctx.get().getSender().getServer().getLevel(key), entity -> {
					entity.absMoveTo(pos.getX(), pos.getY(), pos.getZ(), entity.getYRot(), entity.getXRot());
					return entity;
				});
				if(entity22 != null) {
					if(!name.isEmpty()) entity22.setCustomName(new Line(name).asText());
					ctx.get().getSender().getServer().getLevel(key).addFreshEntity(entity22);
				}
			}
			succes.set(true);
		});
		ctx.get().setPacketHandled(true);
		return succes.get();
	}

}
