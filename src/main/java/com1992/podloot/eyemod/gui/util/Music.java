package com.podloot.eyemod.gui.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.EntityBoundSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;

public class Music {
	
	public static SoundInstance playing;
	
	public static int song = 0;
	
	public static SoundEvent[] songs = {
			SoundEvents.MUSIC_DISC_5,
			SoundEvents.MUSIC_DISC_11,
			SoundEvents.MUSIC_DISC_13,
			SoundEvents.MUSIC_DISC_BLOCKS,
			SoundEvents.MUSIC_DISC_CAT,
			SoundEvents.MUSIC_DISC_CHIRP,
			SoundEvents.MUSIC_DISC_FAR,
			SoundEvents.MUSIC_DISC_MALL,
			SoundEvents.MUSIC_DISC_MELLOHI,
			SoundEvents.MUSIC_DISC_OTHERSIDE,
			SoundEvents.MUSIC_DISC_PIGSTEP,
			SoundEvents.MUSIC_DISC_STAL,
			SoundEvents.MUSIC_DISC_STRAD,
			SoundEvents.MUSIC_DISC_WAIT,
			SoundEvents.MUSIC_DISC_WARD,
			SoundEvents.MUSIC_GAME,
			SoundEvents.MUSIC_CREATIVE,
			SoundEvents.MUSIC_CREDITS,
			SoundEvents.MUSIC_END,
			SoundEvents.MUSIC_MENU,
			SoundEvents.MUSIC_UNDER_WATER,
	};
	
	public static void play(Entity e, int s) {
		if(playing != null) Minecraft.getInstance().getSoundManager().stop(playing);
		song = s;
		playing = new EntityBoundSoundInstance(songs[s], SoundSource.RECORDS, 1, 1, e, 1);
		Minecraft.getInstance().getSoundManager().play(playing);
	}
	
	public static void stop() {
		Minecraft.getInstance().getSoundManager().stop(playing);
	}
	
	public static boolean playing() {
		return Minecraft.getInstance().getSoundManager().isActive(playing);
	}
	
	public static void sound(SoundEvent e) {
		Minecraft.getInstance().player.playSound(e, 1, 1);
	}
	
	public static void sound(SoundEvent e, float volume, float pitch) {
		Minecraft.getInstance().player.playSound(e, volume, pitch);
	}

}
