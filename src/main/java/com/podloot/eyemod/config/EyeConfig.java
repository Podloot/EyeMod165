package com.podloot.eyemod.config;

import java.util.ArrayList;
import java.util.List;

import com.podloot.eyemod.lib.gui.EyeLib;
import com.podloot.eyemod.lib.gui.util.Pos;

import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;

public final class EyeConfig {
	public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec SPEC;
	
	public static final ForgeConfigSpec.ConfigValue<String> currency;
	public static final ForgeConfigSpec.ConfigValue<Integer> router_range;
	public static final ForgeConfigSpec.ConfigValue<Integer> battery_minutes;
	public static final ForgeConfigSpec.ConfigValue<Integer> max_explode_size;
	public static final ForgeConfigSpec.ConfigValue<List<String>> eye_ops;
	public static final ForgeConfigSpec.ConfigValue<List<String>> spawn_list;
	public static final ForgeConfigSpec.ConfigValue<Boolean> allow_home;
	public static final ForgeConfigSpec.ConfigValue<Boolean> op_apps;

	public static final ForgeConfigSpec.ConfigValue<List<String>> app_access;
	public static final ForgeConfigSpec.ConfigValue<List<String>> app_price;
	
	static {
		BUILDER.push("Config for the EyeMod");
		
		currency = BUILDER.comment("Currency item used to transfer to money").define("currency", "minecraft:emerald");
		router_range = BUILDER.comment("The range of the router, use 0 for unlimited range").define("router_range", 512);
		battery_minutes = BUILDER.comment("Battery drain of the EyePhone").define("battery_minutes", 120);
		max_explode_size = BUILDER.comment("Max explosion size").define("max_explode_size", 16);
		eye_ops = BUILDER.comment("Should be considered OP, but aren't OP ingame, use: [\"NAME\",\"NAME2\",etc]").define("eye_ops", new ArrayList<String>());
		spawn_list = BUILDER.comment("A list of location in the server, used for AppSpawn, use: [\"X|Y|Z|dimension|name\",etc], for example: [\"0|62|0|minecraft:overworld|Spawn\",\"0|50|0|minecraft:nether|Nether spawn\"]").define("spawn_list", new ArrayList<String>());
		allow_home = BUILDER.comment("If a player is allowed to set a home location, used for AppSpawn").define("allow_home", false);
		op_apps = BUILDER.comment("Allow the use of OP apps").define("allow_op_apps", true);
		
		List<String> access = new ArrayList<String>();
		access.add("modid:appname=1");
		
		List<String> price = new ArrayList<String>();
		price.add("modid:appname=10");
		
		app_access = BUILDER.comment("Access World of the apps (0 = everyone, 1 = OP only, 2 = Noone), example: [\"eyemod:tnt=0\",etc]").define("app_access", access);
		app_price = BUILDER.comment("Price of the apps, example: [\"eyemod:store=0\",etc]").define("app_price", price);
		
		BUILDER.pop();
		SPEC = BUILDER.build();
	}
	
	public static int getAccess(ResourceLocation app) {
		List<String> access = app_access.get();
		for(String s : access) {
			if(s.contains("=")) {
				String[] i = s.split("=");
				if(app.toString().equals(i[0].toLowerCase())) {
					return EyeLib.getInt(i[1]);
				}
			}
		}
		return -1;
	}
	
	public static int getPrice(ResourceLocation app) {
		List<String> price = app_price.get();
		for(String s : price) {
			if(s.contains("=")) {
				String[] i = s.split("=");
				if(app.toString().equals(i[0].toLowerCase())) {
					return EyeLib.getInt(i[1]);
				}
			}
		}
		return -1;
	}
	
	public static ListNBT getSpawns() {
		List<String> ls = spawn_list.get();
		ListNBT ns = new ListNBT();
		for(String s : ls) {
			Pos p = new Pos().fromString(s);
			if(p.getPos() != null && p.getWorld() != null) {
				ns.add(StringNBT.valueOf(s));
			}
		}
		return ns;
	}
	
}
