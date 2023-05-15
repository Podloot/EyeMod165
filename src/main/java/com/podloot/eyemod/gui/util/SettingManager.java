package com.podloot.eyemod.gui.util;

import java.util.HashMap;
import java.util.List;

import com.podloot.eyemod.EyeApps;
import com.podloot.eyemod.gui.apps.App;
import com.podloot.eyemod.lib.gui.widgets.EyeLabel;
import com.podloot.eyemod.lib.gui.widgets.EyeWidget;

import net.minecraft.nbt.ByteNBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

public class SettingManager {
	
	public HashMap<ResourceLocation, List<EyeWidget>> app_settings = new HashMap<ResourceLocation, List<EyeWidget>>();
	
	NbtManager data;
	String skey = "settings";
	
	public SettingManager(NbtManager data) {
		this.data = data;
		if(!data.has(skey)) data.setCompoundNBT(skey, new CompoundNBT());
	}
	
	public boolean has(String key) {
		return data.getCompoundNBT(skey).contains(key);
	}
	
	public void setInt(String key, int v) {
		set(key, IntNBT.valueOf(v));
	}
	
	public int getInt(String key) {
		return data.getCompoundNBT(skey).getInt(key);
	}
	
	public void setString(String key, String v) {
		set(key, StringNBT.valueOf(v));
	}
	
	public String getString(String key) {
		return data.getCompoundNBT(skey).getString(key);
	}
	
	public void setBool(String key, boolean v) {
		set(key, ByteNBT.valueOf(v));
	}
	
	public boolean getBool(String key) {
		return data.getCompoundNBT(skey).getBoolean(key);
	}
	
	public void setList(String key, ListNBT list) {
		set(key, list);
	}
	
	public ListNBT getList(String key, int type) {
		return data.getCompoundNBT(skey).getList(key, type);
	}
	
	public void set(String key, INBT e) {
		CompoundNBT nbt = data.getCompoundNBT(skey);
		nbt.put(key, e);
		data.setCompoundNBT(skey, nbt);
	}
	
	public INBT get(String key) {
		return data.getCompoundNBT(skey).get(key);
	}
	
	public void initSettings(ListNBT apps, int width) {
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
