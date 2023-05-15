package com.podloot.eyemod.network;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.podloot.eyemod.gui.util.Naming.Type;
import com.podloot.eyemod.items.ItemDevice;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

public class ServerSendChat {
	
	String sender;
	String msg;
	boolean local;
	int dis;
	
	public ServerSendChat(String sender, String msg, boolean local, int dis) { 
		this.sender = sender;
		this.msg = msg;
		this.local = local;
		this.dis = dis;
	}
	
	public ServerSendChat(PacketBuffer buf) {
		this.sender = buf.readUtf();
		this.msg = buf.readUtf();
		this.local = buf.readBoolean();
		this.dis = buf.readInt();
	}
	
	public void encode(PacketBuffer buf) {
		buf.writeUtf(sender);
		buf.writeUtf(msg);
		buf.writeBoolean(local);
		buf.writeInt(dis);
	}
	
	public boolean handle(Supplier<NetworkEvent.Context> ctx) {
		final AtomicBoolean succes = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			ServerPlayerEntity p = ctx.get().getSender();
			for (World w : ctx.get().getSender().server.getAllLevels()) {
				for (PlayerEntity player : w.players()) {
					List<ItemStack> stacks = new ArrayList<ItemStack>();
					stacks.addAll(player.inventory.items);
					stacks.addAll(player.inventory.offhand);
					for (ItemStack s : stacks) {
						if (s.getItem() instanceof ItemDevice) {
							if(s.getTag().contains("apps") && s.getTag().getList("apps", Type.STRING.type).contains(StringNBT.valueOf("eyemod:chat"))) {
								int phone_distance = s.getTag().getCompound("settings").getInt("chat_dis");
								boolean phone_local = s.getTag().getCompound("settings").getBoolean("chat_local");
								int player_distance = player.blockPosition().distManhattan(p.blockPosition());
								
								if(dis == 0) dis = 128;
								if(phone_distance == 0) phone_distance = 128;
								
								if(phone_local && player_distance > phone_distance) continue;
								if(local && player_distance > dis) continue;
								
								ListNBT chat = s.getTag().getList("chat", Type.STRING.type);
								if(chat.size() > 50) {
									chat.remove(0);
								}
								chat.add(StringNBT.valueOf("<" + sender + "> " + msg));
								s.getTag().put("chat", chat);
								succes.set(true);
							}
						}
						
					}
				}
			}
		});
		ctx.get().setPacketHandled(true);
		return succes.get();
	}

}
