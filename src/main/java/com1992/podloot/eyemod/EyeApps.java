package com.podloot.eyemod;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.podloot.eyemod.gui.apps.App;

import net.minecraft.resources.ResourceLocation;

public class EyeApps {
	
	public static Map<ResourceLocation, App> APPS = new HashMap<ResourceLocation, App>();
	
	/**
	 * Registers app (important: only use on client side)
	 * @param id
	 * @param app
	 */
	public static void register(ResourceLocation id, App app) {
		app.setId(id);
		APPS.put(id, app);
	}
	
	public static Collection<App> getApps() {
		return APPS.values();
	}
	
	public static App getApp(String id) {
		return APPS.get(new ResourceLocation(id));
	}
	
	public static App getApp(ResourceLocation id) {
		return APPS.get(id);
	}

}
