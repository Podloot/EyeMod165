package com.podloot.eyemod.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.electronwill.nightconfig.core.UnmodifiableConfig;
import com.podloot.eyemod.gui.util.Naming.Type;
import com.podloot.eyemod.lib.gui.EyeLib;
import com.podloot.eyemod.lib.gui.util.Pos;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.ValueSpec;

public class EyeConfigData {
	
	public CompoundTag data;
	
	public EyeConfigData() {
		data = toNbt();
	}
	
	public EyeConfigData(CompoundTag nbt) {
		data = nbt;
	}
	
	public CompoundTag toNbt() {
		CompoundTag nbt = new CompoundTag();
		for(int i = 0; i < EyeConfig.keys.size(); i++) {
			Object value = EyeConfig.keys.get(0).get();
			String key = EyeConfig.keys.get(i).getPath().get(EyeConfig.keys.get(i).getPath().size()-1).toString();
			if(value instanceof Integer) {
				nbt.putInt(key, (int)value);
			} else if(value instanceof String) {
				nbt.putString(key, (String)value);
			} else if(value instanceof Boolean) {
				nbt.putBoolean(key, (boolean)value);
			} else if(value instanceof ArrayList) {
				List list = (List)value;
				if(list.size() > 0 && list.get(0) instanceof String) {
					ListTag listTag = new ListTag();
					for(String s : (List<String>)list) {
						listTag.add(StringTag.valueOf(s));
					}
					nbt.put(key, listTag);
				}				
			}
		}
		return nbt;
	}
	
	public int getInt(String key) {
		return data.getInt(key);
	}
	
	public String getString(String key) {
		return data.getString(key);
	}
	
	public boolean getBoolean(String key) {
		return data.getBoolean(key);
	}
	
	public String[] getList(String key) {
		ListTag tag = data.getList(key, Type.STRING.type);
		String[] list = new String[tag.size()];
		for(int i = 0; i < list.length; i++) {
			list[i] = tag.getString(i);
		}
		return list;
	}
	
	public int getAccess(String appid) {
		String[] access = getList("app_access");
		for(String s : access) {
			if(s.contains("=")) {
				String[] info = s.split("=");
				if(info.length >= 2 && info[0].equals(appid)) return EyeLib.getInt(info[1]);
			}
		}
		return -1;
	}
	
	public int getPrice(String appid) {
		String[] access = getList("app_price");
		for(String s : access) {
			if(s.contains("=")) {
				String[] info = s.split("=");
				if(info.length >= 2 && info[0].equals(appid)) return EyeLib.getInt(info[1]);
			}
		}
		return -1;
	}
	
	public ListTag getSpawns() {
		String[] spawns = getList("spawn_list");
		ListTag list = new ListTag();
		for(String s : spawns) {
			Pos p = new Pos().fromString(s);
			if(p.getPos() != null && p.getWorld() != null) {
				list.add(StringTag.valueOf(s));
			}
		}
		return list;
	}
	

}
