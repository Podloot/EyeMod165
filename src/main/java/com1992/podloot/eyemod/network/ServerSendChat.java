package com.podloot.eyemod.network;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import com.podloot.eyemod.items.ItemDevice;

import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

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
	
	public ServerSendChat(FriendlyByteBuf buf) {
		this.sender = buf.readUtf();
		this.msg = buf.readUtf();
		this.local = buf.readBoolean();
		this.dis = buf.readInt();
	}
	
	public void encode(FriendlyByteBuf buf) {
		buf.writeUtf(sender);
		buf.writeUtf(msg);
		buf.writeBoolean(local);
		buf.writeInt(dis);
	}
	
	public boolean handle(Supplier<NetworkEvent.Context> ctx) {
		final var succes = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			Player p = ctx.get().getSender();
			for (Level w : ctx.get().getSender().server.getAllLevels()) {
				for (Player player : w.players()) {
					List<ItemStack> stacks = new ArrayList<ItemStack>();
					stacks.addAll(player.getInventory().items);
					stacks.addAll(player.getInventory().offhand);
					for (ItemStack s : stacks) {
						if (s.getItem() instanceof ItemDevice) {
							if(s.getTag().contains("apps") && s.getTag().getList("apps", ListTag.TAG_STRING).contains(StringTag.valueOf("eyemod:chat"))) {
								int phone_distance = s.getTag().getCompound("settings").getInt("chat_dis");
								boolean phone_local = s.getTag().getCompound("settings").getBoolean("chat_local");
								int player_distance = player.blockPosition().distManhattan(p.blockPosition());
								
								if(dis == 0) dis = 128;
								if(phone_distance == 0) phone_distance = 128;
								
								if(phone_local && player_distance > phone_distance) continue;
								if(local && player_distance > dis) continue;
								
								ListTag chat = s.getTag().getList("chat", ListTag.TAG_STRING);
								if(chat.size() > 50) {
									chat.remove(0);
								}
								chat.add(StringTag.valueOf("<" + sender + "> " + msg));
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
