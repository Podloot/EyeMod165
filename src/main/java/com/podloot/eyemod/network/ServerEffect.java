package com.podloot.eyemod.network;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraftforge.fml.network.NetworkEvent;

public class ServerEffect {

	String player;
	int effect;
	int lvl;
	int time;

	public ServerEffect(String player, int effect, int lvl, int time) {
		this.player = player;
		this.effect = effect;
		this.lvl = lvl;
		this.time = time;
	}

	public ServerEffect(PacketBuffer buf) {
		this.player = buf.readUtf();
		this.effect = buf.readInt();
		this.lvl = buf.readInt();
		this.time = buf.readInt();
	}

	public void encode(PacketBuffer buf) {
		buf.writeUtf(player);
		buf.writeInt(effect);
		buf.writeInt(lvl);
		buf.writeInt(time);
	}

	public boolean handle(Supplier<NetworkEvent.Context> ctx) {
		final AtomicBoolean succes = new AtomicBoolean(false);
		ctx.get().enqueueWork(() -> {
			PlayerEntity p = ctx.get().getSender().getServer().getPlayerList().getPlayerByName(player);
			if(p == null) return;			
			if(lvl == 0 || time == 0) {
				p.removeAllEffects();
			} else {
				EffectInstance se = new EffectInstance(Effect.byId(effect), time, lvl, false, false);
		        p.addEffect(se);
			}
			succes.set(true);
		});
		ctx.get().setPacketHandled(true);
		return succes.get();
	}

}
