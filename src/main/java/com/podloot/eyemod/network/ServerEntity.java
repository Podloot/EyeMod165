package com.podloot.eyemod.network;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.podloot.eyemod.lib.gui.util.Line;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

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

	public ServerEntity(PacketBuffer buf) {
		this.pos = buf.readBlockPos();
		this.world = buf.readResourceLocation();
		this.entity = buf.readUtf();
		this.amount = buf.readInt();
		this.name = buf.readUtf();
	}

	public void encode(PacketBuffer buf) {
		buf.writeBlockPos(pos);
		buf.writeResourceLocation(world);
		buf.writeUtf(entity);
		buf.writeInt(amount);
		buf.writeUtf(name);
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
			for(int i = 0; i < amount; i++) {
				CompoundNBT nbtCompound = new CompoundNBT();
				nbtCompound.putString("id", entity);
				Entity entity22 = EntityType.loadEntityRecursive(nbtCompound, ctx.get().getSender().getServer().getLevel(key), entity -> {
					entity.absMoveTo(pos.getX(), pos.getY(), pos.getZ(), entity.getYHeadRot(), entity.xRot);
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
