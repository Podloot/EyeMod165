package com.podloot.eyemod.network;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.podloot.eyemod.blocks.entities.RouterEntity;
import com.podloot.eyemod.gui.util.Naming.Action;
import com.podloot.eyemod.lib.gui.util.Pos;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

public class ServerRouter {
	
	Pos router;
	int action;
	String value;
	
	public ServerRouter(Pos router, Action action, String value) { 
		this.router = router;
		this.action = action.action;
		this.value = value;
	}
	
	public ServerRouter(PacketBuffer buf) {
		this.router = new Pos().fromString(buf.readUtf());
		this.action = buf.readInt();
		this.value = buf.readUtf();
		
	}
	
	public void encode(PacketBuffer buf) {
		buf.writeUtf(router.toNbt());
		buf.writeInt(action);
		buf.writeUtf(value);
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
