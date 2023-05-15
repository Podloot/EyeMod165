package com.podloot.eyemod.network;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.podloot.eyemod.blocks.entities.RouterEntity;
import com.podloot.eyemod.gui.util.Naming.Action;
import com.podloot.eyemod.lib.gui.util.Pos;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

public class ServerRouterData {
	
	Pos router;
	int action;
	String key;
	CompoundNBT tag;
	
	public ServerRouterData(Pos router, Action action, String key, INBT tag) { 
		this.router = router;
		this.action = action.action;
		this.key = key;
		CompoundNBT nbt = new CompoundNBT();
		nbt.put("value", tag != null ? tag : StringNBT.valueOf(""));
		this.tag = nbt;
	}
	
	public ServerRouterData(PacketBuffer buf) {
		this.router = new Pos().fromString(buf.readUtf());
		this.action = buf.readInt();
		this.key = buf.readUtf();
		this.tag = buf.readNbt();
		
	}
	
	public void encode(PacketBuffer buf) {
		buf.writeUtf(router.toNbt());
		buf.writeInt(action);
		buf.writeUtf(key);
		buf.writeNbt(tag);
	}
	
	public boolean handle(Supplier<NetworkEvent.Context> ctx) {
		final AtomicBoolean succes = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			BlockPos pos = router.getPos();
			ResourceLocation world = router.getWorld();
			RegistryKey<World> wkey = World.OVERWORLD;
			for (RegistryKey<World> w : ctx.get().getSender().server.levelKeys()) {
				if (w.location().equals(world)) {
					wkey = w;
					break;
				}
			}
			ServerWorld lvl = ctx.get().getSender().getServer().getLevel(wkey);
			if(lvl.getBlockEntity(pos) instanceof RouterEntity) {
				((RouterEntity)lvl.getBlockEntity(pos)).setData(action, key, tag.get("value"));
			}
			succes.set(true);
		});
		ctx.get().setPacketHandled(true);
		return succes.get();
	}

}
