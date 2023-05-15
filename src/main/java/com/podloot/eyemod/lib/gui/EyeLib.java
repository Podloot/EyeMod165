package com.podloot.eyemod.lib.gui;

import java.util.ArrayList;
import java.util.List;

import com.podloot.eyemod.Eye;
import com.podloot.eyemod.lib.gui.util.Image;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class EyeLib {
	
	//Default
	public static final Image DEFAULT = new Image(new ResourceLocation(Eye.MODID, "textures/gui/default.png"), 148, 192);
	
	//Widgets
	public static final Image PLANE = new Image(new ResourceLocation(Eye.MODID, "textures/gui/plane.png"), 256, 256);
	public static final Image PLANE_BORDER = new Image(new ResourceLocation(Eye.MODID, "textures/gui/plane_border.png"), 256, 256);
	public static final Image SLIDER = new Image(new ResourceLocation(Eye.MODID, "textures/gui/slider.png"), 256, 256);
	public static final Image SLIDER_BORDER = new Image(new ResourceLocation(Eye.MODID, "textures/gui/slider_border.png"), 256, 256);
	public static final Image MAP_BORDER = new Image(new ResourceLocation(Eye.MODID, "textures/gui/map_border.png"), 256, 256);
	
	//Icons
	public static final Image UP = new Image(new ResourceLocation(Eye.MODID, "textures/gui/icons/up.png"), 12, 12);
	public static final Image DOWN = new Image(new ResourceLocation(Eye.MODID, "textures/gui/icons/down.png"), 12, 12);
	public static final Image LEFT = new Image(new ResourceLocation(Eye.MODID, "textures/gui/icons/left.png"), 12, 12);
	public static final Image RIGHT = new Image(new ResourceLocation(Eye.MODID, "textures/gui/icons/right.png"), 12, 12);
	public static final Image ONLINE = new Image(new ResourceLocation(Eye.MODID, "textures/gui/icons/online.png"), 14, 14);
	public static final Image CONTACTS = new Image(new ResourceLocation(Eye.MODID, "textures/gui/icons/contact.png"), 14, 14);
	public static final Image COLOR = new Image(new ResourceLocation(Eye.MODID, "textures/gui/icons/color.png"), 20, 20);
	public static final Image ACCEPT = new Image(new ResourceLocation(Eye.MODID, "textures/gui/icons/ok.png"), 12, 12);
	public static final Image CANCEL = new Image(new ResourceLocation(Eye.MODID, "textures/gui/icons/cancel.png"), 12, 12);
	public static final Image DELETE = new Image(new ResourceLocation(Eye.MODID, "textures/gui/icons/delete.png"), 14, 14);
	public static final Image STEVE = new Image(new ResourceLocation(Eye.MODID, "textures/gui/icons/steve.png"), 16, 16);
	public static final Image FILE = new Image(new ResourceLocation(Eye.MODID, "textures/gui/icons/file.png"), 20, 20);
	public static final Image LOCKED = new Image(new ResourceLocation(Eye.MODID, "textures/gui/icons/locked.png"), 32, 32);
	public static final Image UNLOCKED = new Image(new ResourceLocation(Eye.MODID, "textures/gui/icons/unlocked.png"), 32, 32);
	public static final Image POSITION = new Image(new ResourceLocation(Eye.MODID, "textures/gui/icons/current.png"), 14, 14);
	public static final Image WAYPOINT = new Image(new ResourceLocation(Eye.MODID, "textures/gui/icons/location.png"), 14, 14);
	public static final Image MAP = new Image(new ResourceLocation(Eye.MODID, "textures/gui/icons/mapposition.png"), 14, 14);
	public static final Image LOOKING = new Image(new ResourceLocation(Eye.MODID, "textures/gui/icons/lookingat.png"), 14, 14);
	public static final Image PAUSE = new Image(new ResourceLocation(Eye.MODID, "textures/gui/icons/pause.png"), 12, 12);
	public static final Image PLAY = new Image(new ResourceLocation(Eye.MODID, "textures/gui/icons/play.png"), 12, 12);
	public static final Image NOTIFICATION = new Image(new ResourceLocation(Eye.MODID, "textures/gui/icons/notification.png"), 12, 12);
	
	//WEATHER
	public static final Image SUNNY = new Image(new ResourceLocation(Eye.MODID, "textures/gui/icons/sunny.png"), 16, 16);
	public static final Image RAIN = new Image(new ResourceLocation(Eye.MODID, "textures/gui/icons/raining.png"), 16, 16);
	public static final Image SNOW = new Image(new ResourceLocation(Eye.MODID, "textures/gui/icons/snowing.png"), 16, 16);
	public static final Image THUNDER = new Image(new ResourceLocation(Eye.MODID, "textures/gui/icons/thunder.png"), 16, 16);
	public static final Image MORNING = new Image(new ResourceLocation(Eye.MODID, "textures/gui/icons/morning.png"), 16, 16);
	public static final Image MIDDAY = new Image(new ResourceLocation(Eye.MODID, "textures/gui/icons/midday.png"), 16, 16);
	public static final Image EVENING = new Image(new ResourceLocation(Eye.MODID, "textures/gui/icons/evening.png"), 16, 16);
	public static final Image MIDNIGHT = new Image(new ResourceLocation(Eye.MODID, "textures/gui/icons/midnight.png"), 16, 16);
	public static final Image CYCLE = new Image(new ResourceLocation(Eye.MODID, "textures/gui/icons/timecycle.png"), 144, 32);
	
	//Music
	public static final Image MUSIC_PLAY = new Image(new ResourceLocation(Eye.MODID, "textures/gui/music/play.png"), 64, 32);
	public static final Image MUSIC_PAUSE = new Image(new ResourceLocation(Eye.MODID, "textures/gui/music/pause.png"), 64, 32);
	public static final Image MUSIC_PREVIOUS = new Image(new ResourceLocation(Eye.MODID, "textures/gui/music/previous.png"), 64, 32);
	public static final Image MUSIC_NEXT = new Image(new ResourceLocation(Eye.MODID, "textures/gui/music/next.png"), 64, 32);
	
	//Map
	public static final Image MAP_LOCATION = new Image(new ResourceLocation(Eye.MODID, "textures/gui/icons/waypoint.png"), 12, 12);
	public static final Image MAP_PLAYER = new Image(new ResourceLocation(Eye.MODID, "textures/gui/icons/player.png"), 12, 12);
	public static final Image MAP_ENTITY = new Image(new ResourceLocation(Eye.MODID, "textures/gui/icons/entity.png"), 12, 12);
	public static final Image MAP_SELECTED = new Image(new ResourceLocation(Eye.MODID, "textures/gui/icons/selected.png"), 12, 12);
	
	//Dimensions
	public static final Image OVERWOLD = new Image(new ResourceLocation(Eye.MODID, "textures/gui/icons/overworld.png"), 14, 14);
	public static final Image THE_NETHER = new Image(new ResourceLocation(Eye.MODID, "textures/gui/icons/the_nether.png"), 14, 14);
	public static final Image THE_END = new Image(new ResourceLocation(Eye.MODID, "textures/gui/icons/the_end.png"), 14, 14);
	public static final Image DIMENSION = new Image(new ResourceLocation(Eye.MODID, "textures/gui/icons/dimension.png"), 14, 14);
	public static final Image BIOME = new Image(new ResourceLocation(Eye.MODID, "textures/gui/icons/biome_base.png"), 16, 16);
	public static final Image BIOME_FOG = new Image(new ResourceLocation(Eye.MODID, "textures/gui/icons/biome_fog.png"), 16, 16);
	public static final Image BIOME_WATER = new Image(new ResourceLocation(Eye.MODID, "textures/gui/icons/biome_water.png"), 16, 16);
	public static final Image BIOME_GRASS = new Image(new ResourceLocation(Eye.MODID, "textures/gui/icons/biome_grass.png"), 16, 16);
	
	//Camera
	public static final Image PHOTO_TAKE = new Image(new ResourceLocation(Eye.MODID, "textures/gui/camera/photobutton.png"), 48, 24);
	public static final Image PHOTO_NORMAL = new Image(new ResourceLocation(Eye.MODID, "textures/gui/camera/normalmode.png"), 32, 16);
	public static final Image PHOTO_SELFIE = new Image(new ResourceLocation(Eye.MODID, "textures/gui/camera/selfiemode.png"), 32, 16);
	public static final Image PHOTO_PERSON = new Image(new ResourceLocation(Eye.MODID, "textures/gui/camera/personmode.png"), 32, 16);
	
	//Device
	public static final Image DEVICE = new Image(new ResourceLocation(Eye.MODID, "textures/gui/device/border.png"), 256, 256);
	public static final Image DEVICE_HOME = new Image(new ResourceLocation(Eye.MODID, "textures/gui/device/home.png"), 40, 20);
	public static final Image DEVICE_LEFT = new Image(new ResourceLocation(Eye.MODID, "textures/gui/device/left.png"), 22, 16);
	public static final Image DEVICE_RIGHT = new Image(new ResourceLocation(Eye.MODID, "textures/gui/device/right.png"), 22, 16);
	public static final Image DEVICE_QUIT = new Image(new ResourceLocation(Eye.MODID, "textures/gui/device/quit.png"), 32, 8);
	public static final Image DEVICE_SCREENSHOT = new Image(new ResourceLocation(Eye.MODID, "textures/gui/device/screenshot.png"), 16, 8);
	
	//Statusbar
	public static final Image CONNECTION = new Image(new ResourceLocation(Eye.MODID, "textures/gui/device/connection.png"), 32, 8);
	public static final Image BATTERY = new Image(new ResourceLocation(Eye.MODID, "textures/gui/device/battery.png"), 80, 8);
	public static final Image COVERAGE = new Image(new ResourceLocation(Eye.MODID, "textures/gui/device/coverage.png"), 20, 8);
	public static final Image EYE = new Image(new ResourceLocation(Eye.MODID, "textures/gui/device/eye.png"), 8, 8);
	public static final Image CONSOLE = new Image(new ResourceLocation(Eye.MODID, "textures/gui/device/console.png"), 16, 8);
	
	//Functions	
	public static int getInt(String s) {
		s = s.trim();
		s = s.isEmpty() || s.equals("") || !s.matches("[-0-9]+") ? "0" : s;
		if(s.contains("-")) s = "-" + s.replace("-", "");
		return Integer.valueOf(s);
	}
	
	public static List<String> getLines(String line, int w) {
		int total_width = Minecraft.getInstance().font.width(line);
		int remaining_width = total_width;
		List<String> lines = new ArrayList<String>();
		List<String> words = splitLine(line);
		
		int l = 0;
		for(int i = 0; i < words.size(); i++) {
			if(l >= lines.size()) lines.add("");
			
			//Enter sign (|)
			if(words.get(i).equals("|")) {
				l++;
				continue;
			}
			
			int wordwidth = Minecraft.getInstance().font.width(words.get(i));
			
			//Check and split if word is too long
			if(wordwidth > w) {
				String n1 = Minecraft.getInstance().font.plainSubstrByWidth(words.get(i), w);
				String n2 = words.get(i).substring(n1.length());
				words.set(i, n1);
				words.add(i+1, n2);
			}
			
			//Add to same or next line
			int lw = Minecraft.getInstance().font.width(lines.get(l) + (lines.get(l).isEmpty() ? "" : " ") + words.get(i));
			if(lw <= w) {
				lines.set(l, lines.get(l) + (lines.get(l).isEmpty() ? "" : " ") + words.get(i));
			} else {
				lines.add(words.get(i));
				l++;
			}
		}
		return lines;
	}
	
	private static List<String> splitLine(String line) {
		String[] wrds = line.split(" ");
		List<String> words = new ArrayList<String>();
		for(String s : wrds) {
			if(s.contains("|")) {
				String nw = s + "_";
				String[] e = nw.split("\\|");
				for(int i = 0; i < e.length; i++) {
					if(i != e.length-1) {
						words.add(e[i]);
						words.add("|");
					} else {
						words.add(e[i].substring(0, e[i].length()-1));
					}
				}
			} else {
				words.add(s);
			}
		}
		return words;
	}
}
