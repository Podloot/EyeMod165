package com.podloot.eyemod.network;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.podloot.eyemod.blocks.entities.RouterEntity;
import com.podloot.eyemod.gui.util.Naming.Msg;
import com.podloot.eyemod.lib.gui.util.Pos;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

public class ServerSendMessage {
	
	String router;
	int type;
	String reciever;
	CompoundTag msg;

	
	public ServerSendMessage(Pos router, Msg type, String reciever, CompoundTag message) { 
		this.router = router.toNbt();
		this.type = type.type;
		this.reciever = reciever;
		this.msg = message;
	}
	
	public ServerSendMessage(FriendlyByteBuf buf) {
		this.router = buf.readUtf();
		this.type = buf.readInt();
		this.reciever = buf.readUtf();
		this.msg = buf.readNbt();
	}
	
	public void encode(FriendlyByteBuf buf) {
		buf.writeUtf(router);
		buf.writeInt(type);
		buf.writeUtf(reciever);
		buf.writeNbt(msg);
	}
	
	public boolean handle(Supplier<NetworkEvent.Context> ctx) {
		final var succes = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			Pos r = new Pos().fromString(router);
			ResourceLocation i = r.getWorld();
			ResourceKey<Level> key = Level.OVERWORLD;
			for(ResourceKey<Level> w : ctx.get().getSender().server.levelKeys()) {
				if(w.location().equals(i)) {
					key = w;
					break;
				}
			}
			
			msg.putString("rec", reciever);
			msg.putInt("id", type);
			
			if(key != null && ctx.get().getSender().server.getLevel(key) != null) {
	    		if(ctx.get().getSender().server.getLevel(key).getBlockEntity(r.getPos()) instanceof RouterEntity) {
	    			((RouterEntity)ctx.get().getSender().server.getLevel(key).getBlockEntity(r.getPos())).recieveMessage(msg);		    			
	    			succes.set(true);
	    		}
	    	}
		});
		ctx.get().setPacketHandled(true);
		return succes.get();
	}

}
