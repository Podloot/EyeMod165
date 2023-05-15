package com.podloot.eyemod.gui.util;

import java.util.HashMap;
import java.util.List;

import com.podloot.eyemod.EyeApps;
import com.podloot.eyemod.gui.apps.App;
import com.podloot.eyemod.lib.gui.widgets.EyeLabel;
import com.podloot.eyemod.lib.gui.widgets.EyeWidget;

import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;

public class SettingManager {
	
	public HashMap<ResourceLocation, List<EyeWidget>> app_settings = new HashMap<ResourceLocation, List<EyeWidget>>();
	
	NbtManager data;
	String skey = "settings";
	
	public SettingManager(NbtManager data) {
		this.data = data;
		if(!data.has(skey)) data.setCompoundTag(skey, new CompoundTag());
	}
	
	public boolean has(String key) {
		return data.getCompoundTag(skey).contains(key);
	}
	
	public void setInt(String key, int v) {
		set(key, IntTag.valueOf(v));
	}
	
	public int getInt(String key) {
		return data.getCompoundTag(skey).getInt(key);
	}
	
	public void setString(String key, String v) {
		set(key, StringTag.valueOf(v));
	}
	
	public String getString(String key) {
		return data.getCompoundTag(skey).getString(key);
	}
	
	public void setBool(String key, boolean v) {
		set(key, ByteTag.valueOf(v));
	}
	
	public boolean getBool(String key) {
		return data.getCompoundTag(skey).getBoolean(key);
	}
	
	public void setList(String key, ListTag list) {
		set(key, list);
	}
	
	public ListTag getList(String key, int type) {
		return data.getCompoundTag(skey).getList(key, type);
	}
	
	public void set(String key, Tag e) {
		CompoundTag nbt = data.getCompoundTag(skey);
		nbt.put(key, e);
		data.setCompoundTag(skey, nbt);
	}
	
	public Tag get(String key) {
		return data.getCompoundTag(skey).get(key);
	}
	
	public void initSettings(ListTag apps, int width) {
		for(int i = 0; i < apps.size(); i++) {
			App app = EyeApps.getApp(apps.getString(i));
			if(app != null && app.getDevice() != null) {
				List<EyeWidget> stngs = app.getSettings(width);
				if(stngs != null) {
					EyeLabel title = new EyeLabel(width, 16, app.getName());
					title.setBack(0xff555555);
					stngs.add(0, title);
					app_settings.put(app.getId(), stngs);
				}
			}
		}
	}
}
