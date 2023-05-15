package com.podloot.eyemod.gui.apps.base;

import java.util.HashMap;
import java.util.Map;

import com.podloot.eyemod.Eye;
import com.podloot.eyemod.gui.apps.App;
import com.podloot.eyemod.gui.elements.DeviceButton;
import com.podloot.eyemod.gui.util.Music;
import com.podloot.eyemod.lib.gui.EyeLib;
import com.podloot.eyemod.lib.gui.panels.EyeListPanel;
import com.podloot.eyemod.lib.gui.util.Color;
import com.podloot.eyemod.lib.gui.util.Line;
import com.podloot.eyemod.lib.gui.widgets.EyeItem;
import com.podloot.eyemod.lib.gui.widgets.EyeLabel;
import com.podloot.eyemod.lib.gui.widgets.EyeList;
import com.podloot.eyemod.lib.gui.widgets.EyePlane;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.RecordItem;

public class AppMusic extends App {

	EyeList songs;
	Map<SoundEvent, RecordItem> discs = new HashMap<SoundEvent, RecordItem>();
	
	public AppMusic() {
		super(new ResourceLocation(Eye.MODID, "textures/gui/apps/appmusic.png"), 0xffDDDDDD, "Eye");
		discs.put(SoundEvents.MUSIC_DISC_5, (RecordItem) Items.MUSIC_DISC_5);
		discs.put(SoundEvents.MUSIC_DISC_11, (RecordItem) Items.MUSIC_DISC_11);
		discs.put(SoundEvents.MUSIC_DISC_13, (RecordItem) Items.MUSIC_DISC_13);
		discs.put(SoundEvents.MUSIC_DISC_BLOCKS, (RecordItem) Items.MUSIC_DISC_BLOCKS);
		discs.put(SoundEvents.MUSIC_DISC_CAT, (RecordItem) Items.MUSIC_DISC_CAT);
		discs.put(SoundEvents.MUSIC_DISC_CHIRP, (RecordItem) Items.MUSIC_DISC_CHIRP);
		discs.put(SoundEvents.MUSIC_DISC_FAR, (RecordItem) Items.MUSIC_DISC_FAR);
		discs.put(SoundEvents.MUSIC_DISC_MALL, (RecordItem) Items.MUSIC_DISC_MALL);
		discs.put(SoundEvents.MUSIC_DISC_MELLOHI, (RecordItem) Items.MUSIC_DISC_MELLOHI);
		discs.put(SoundEvents.MUSIC_DISC_OTHERSIDE, (RecordItem) Items.MUSIC_DISC_OTHERSIDE);
		discs.put(SoundEvents.MUSIC_DISC_PIGSTEP, (RecordItem) Items.MUSIC_DISC_PIGSTEP);
		discs.put(SoundEvents.MUSIC_DISC_STAL, (RecordItem) Items.MUSIC_DISC_STAL);
		discs.put(SoundEvents.MUSIC_DISC_STRAD, (RecordItem) Items.MUSIC_DISC_STRAD);
		discs.put(SoundEvents.MUSIC_DISC_WAIT, (RecordItem) Items.MUSIC_DISC_WAIT);
		discs.put(SoundEvents.MUSIC_DISC_WARD, (RecordItem) Items.MUSIC_DISC_WARD);
	}
	
	@Override
	public boolean load() {
		songs = new EyeList(getWidth()-4, getHeight()-42, Axis.VERTICAL);
		for(int i = 0; i < Music.songs.length; i++) {
			songs.add(getSongPanel(i, Music.songs[i]));
		}
		add(songs, 2, 2);
		
		DeviceButton previous = new DeviceButton(32, 32, EyeLib.MUSIC_PREVIOUS);
		previous.setColor(Color.DARKGRAY);
		DeviceButton pause = new DeviceButton(32, 32, EyeLib.MUSIC_PAUSE);
		pause.setColor(Color.DARKGRAY);
		DeviceButton play = new DeviceButton(32, 32, EyeLib.MUSIC_PLAY);
		play.setColor(Color.DARKGRAY);
		DeviceButton next = new DeviceButton(32, 32, EyeLib.MUSIC_NEXT);
		next.setColor(Color.DARKGRAY);
		
		play.setAction(() -> {
			EyeListPanel sel = songs.getSelected();
			if(sel != null) {
				Object index = sel.getData();
				if(index != null && index instanceof Integer) {
					Music.play(device.getUser(), (int)index);
				} else {
					Music.play(device.getUser(), Music.song);
				}
			} else {
				Music.play(device.getUser(), Music.song);
			}
		});
		
		pause.setAction(() -> {
			Music.stop();
		});
		
		previous.setAction(() -> {
			if(Music.song > 0) {
				Music.play(device.getUser(), Music.song-1);
			} else {
				Music.play(device.getUser(), Music.songs.length-1);
			}
		});
		
		next.setAction(() -> {
			if(Music.song < Music.songs.length-1) {
				Music.play(device.getUser(), Music.song+1);
			} else {
				Music.play(device.getUser(), 0);
			}
		});
		
		EyePlane back = new EyePlane(getWidth()-4, 36, getAppColor());
		add(back, 2, getHeight()-38);
		
		add(previous, 4, getHeight() - 36);
		add(pause, getWidth()/2 - 34, getHeight()-36);
		add(play, getWidth()/2 + 2, getHeight()-36);
		add(next, getWidth() - 36, getHeight() - 36);
		
		
		return true;
	}
	
	public EyeListPanel getSongPanel(int index, SoundEvent s) {
		EyeListPanel song = new EyeListPanel(songs, 16);
		ItemStack disc = Items.MUSIC_DISC_CAT.getDefaultInstance();
		if(discs.containsKey(s)) disc = discs.get(s).getDefaultInstance();
		EyeItem item = new EyeItem(16, 16, disc);
		song.add(item, 1, 0);
		String n = s.getLocation().getPath();
		if(n.contains(".")) {
			String[] ns = n.split("\\.");
			if(ns.length >= 1) n = ns[ns.length-1];
		}
		EyeLabel name = new EyeLabel(song.getWidth()-18, 16, new Line(n).setStyle(true, true));
		name.setBack(getAppColor());
		song.add(name, 18, 0);
		song.setData(index);
		return song;
	}

}
