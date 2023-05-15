package com.podloot.eyemod.network;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.podloot.eyemod.blocks.entities.RouterEntity;
import com.podloot.eyemod.gui.util.Naming.Action;
import com.podloot.eyemod.lib.gui.util.Pos;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

public class ServerRouter {
	
	Pos router;
	int action;
	String value;
	
	public ServerRouter(Pos router, Action action, String value) { 
		this.router = router;
		this.action = action.action;
		this.value = value;
	}
	
	public ServerRouter(FriendlyByteBuf buf) {
		this.router = new Pos().fromString(buf.readUtf());
		this.action = buf.readInt();
		this.value = buf.readUtf();
		
	}
	
	public void encode(FriendlyByteBuf buf) {
		buf.writeUtf(router.toNbt());
		buf.writeInt(action);
		buf.writeUtf(value);
	}
	
	public boolean handle(Supplier<NetworkEvent.Context> ctx) {
		final var succes = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			BlockPos pos = router.getPos();
			ResourceLocation world = router.getWorld();
			ResourceKey<Level> wkey = Level.OVERWORLD;
			for (ResourceKey<Level> w : ctx.get().getSender().server.levelKeys()) {
				if (w.location().equals(world)) {
					wkey = w;
					break;
				}
			}
			ServerLevel lvl = ctx.get().getSender().getServer().getLevel(wkey);
			if(lvl.getBlockEntity(pos) instanceof RouterEntity) {
				if(action == 10) {
					((RouterEntity)lvl.getBlockEntity(pos)).owner = value;
					((RouterEntity)lvl.getBlockEntity(pos)).update();
				} else if(action == 11) {
					((RouterEntity)lvl.getBlockEntity(pos)).password = value;
					((RouterEntity)lvl.getBlockEntity(pos)).update();
				}
			}
			succes.set(true);
		});
		ctx.get().setPacketHandled(true);
		return succes.get();
	}

}
