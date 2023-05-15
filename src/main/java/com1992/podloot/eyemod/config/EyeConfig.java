package com.podloot.eyemod.config;

import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

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
	
	public static List<ConfigValue> keys = new ArrayList<ConfigValue>();
	
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
		
		keys.add(currency);
		keys.add(router_range);
		keys.add(battery_minutes);
		keys.add(max_explode_size);
		keys.add(eye_ops);
		keys.add(spawn_list);
		keys.add(allow_home);
		keys.add(op_apps);
		
		List<String> access = new ArrayList<String>();
		access.add("modid:appname=1");
		
		List<String> price = new ArrayList<String>();
		price.add("modid:appname=10");
		
		app_access = BUILDER.comment("Access level of the apps (0 = everyone, 1 = OP only, 2 = Noone), example: [\"eyemod:tnt=0\",etc]").define("app_access", access);
		app_price = BUILDER.comment("Price of the apps, example: [\"eyemod:store=0\",etc]").define("app_price", price);
		
		BUILDER.pop();
		SPEC = BUILDER.build();
	}
	
}
