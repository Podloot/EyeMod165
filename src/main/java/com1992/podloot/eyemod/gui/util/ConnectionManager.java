package com.podloot.eyemod.gui.util;

import com.podloot.eyemod.EyeClient;
import com.podloot.eyemod.blocks.Router;
import com.podloot.eyemod.blocks.entities.RouterEntity;
import com.podloot.eyemod.config.EyeConfig;
import com.podloot.eyemod.gui.GuiDevice;
import com.podloot.eyemod.gui.util.Naming.Dim;
import com.podloot.eyemod.gui.util.Naming.Msg;
import com.podloot.eyemod.gui.util.Naming.Type;
import com.podloot.eyemod.lib.gui.util.Pos;
import com.podloot.eyemod.network.PacketHandler;
import com.podloot.eyemod.network.ServerSendChat;
import com.podloot.eyemod.network.ServerSendMessage;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ConnectionManager {
	
	GuiDevice device;
	
	public int max_distance = 512;
	
	public ConnectionManager(GuiDevice device) {
		this.device = device;
		max_distance = device.config.getInt("router_range");
	}
	
	public void sendMessage(String rec, String msg) {
		if(this.isConnected()) {
			if(msg.startsWith("\\|")) msg = msg.substring(1);
			if(msg.isEmpty()) return;
			
			CompoundTag messages = device.data.getCompoundTag("messages");
			ListTag conv = messages.getList(rec, Type.STRING.type);
			conv.add(StringTag.valueOf("|" + msg));
			messages.put(rec, conv);
			device.data.setCompoundTag("messages", messages);
			
			Pos router = getRouter();
			CompoundTag msg_data = new CompoundTag();
			msg_data.putString("sen", device.getOwner());
			msg_data.putString("msg", msg);
			PacketHandler.INSTANCE.sendToServer(new ServerSendMessage(router, Msg.MESSAGE, rec, msg_data));
		}
	}
	
	public void sendChat(String msg, String channel) {		
        int d = device.settings.getInt("chat_dis");
        d = d < 0 ? -1 : d == 0 ? 16 : 16*d;
        PacketHandler.INSTANCE.sendToServer(new ServerSendChat(device.getOwner(), msg, device.settings.getBool("chat_local"), d));
	}
	
	public void sendMail(String sender, String subject, String message) {
		String mail = sender + "~" + subject + "~" + message;
		ListTag mails = device.data.getList("mail", Type.STRING);
		if(mails.size() > 8) {
			for(int i = 0; i < mails.size()-8; i++) {
				mails.remove(i);
			}
		}
		mails.add(StringTag.valueOf(mail));
		device.addNotification(EyeClient.APPMAIL.getId(), "From: " + sender);
		device.data.setList("mail", mails);
	}
	
	public boolean isConnected() {
		return getConnection() > 0;
	}
	
	public int getConnection() {
		Pos router = device.data.getPos("router");
		return getConnection(router);
	}
	
	public int getConnection(Pos router) {
		int distance = this.getDistance();
		if(distance < 0) return 0;
		if(!device.getUser().getLevel().getBlockState(router.getPos()).getValue(Router.ON)) return 0;
		String pass = "";//device.getUser().world.getBlockEntity(getRouter().getPos()).createNbt().getCompound("data").getString("password");
		String owner = "";//device.getUser().world.getBlockEntity(getRouter().getPos()).createNbt().getCompound("data").getString("owner");
		if(pass.isEmpty() || pass.equals(device.data.getString("net_pw")) || owner == device.getUser().getScoreboardName()) {
			if(max_distance == 0) return 3;
			float steps = (float)max_distance/3F;
			if(distance < steps) return 3;
			else if(distance < steps*2) return 2;
			else if(distance < max_distance) return 3;
		}
		return 0;
	}
	
	public boolean isRouter(Pos router) {
		if(router == null) return false;
		if(device.getUser().getLevel().getBlockEntity(router.getPos()) instanceof RouterEntity) return true;
		return false;
	}
	
	public int getDistance() {
		Pos router = getRouter();
		if(router == null) return -1;
		if(!isRouter(router)) return -1;
		if(!router.getWorld().equals(device.getWorldID())) return -1;
		BlockPos player = device.getUser().blockPosition();
		int distance = player.distManhattan(router.getPos());
		return distance;
	}
	
	public int getReach() {
		ResourceLocation w = device.getWorldID();
		if(w.equals(Dim.OVERWORLD.id)) return 3;
		if(w.equals(Dim.NETHER.id)) return 2;
		if(w.equals(Dim.END.id)) return 1;
		return device.getUser().getLevel().random.nextInt(4);
	}
	
	public Pos getRouter() {
		return device.data.getPos("router");
	}
	
	public RouterEntity getRouterData() {
		Pos pos = getRouter();
		if(pos == null) return null;
		BlockEntity be =  device.getUser().level.getBlockEntity(pos.getPos());
		if(be instanceof RouterEntity) return (RouterEntity) be;
		return null;
	}

}
