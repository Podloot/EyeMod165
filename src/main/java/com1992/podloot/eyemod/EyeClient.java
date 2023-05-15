package com.podloot.eyemod;

import com.podloot.eyemod.gui.apps.App;
import com.podloot.eyemod.gui.apps.base.AppCamera;
import com.podloot.eyemod.gui.apps.base.AppChat;
import com.podloot.eyemod.gui.apps.base.AppClock;
import com.podloot.eyemod.gui.apps.base.AppContacts;
import com.podloot.eyemod.gui.apps.base.AppMaps;
import com.podloot.eyemod.gui.apps.base.AppMessages;
import com.podloot.eyemod.gui.apps.base.AppMusic;
import com.podloot.eyemod.gui.apps.base.AppNetwork;
import com.podloot.eyemod.gui.apps.base.AppNotes;
import com.podloot.eyemod.gui.apps.base.AppPhotos;
import com.podloot.eyemod.gui.apps.base.AppWaypoint;
import com.podloot.eyemod.gui.apps.base.AppWeather;
import com.podloot.eyemod.gui.apps.op.AppMagic;
import com.podloot.eyemod.gui.apps.op.AppSpawner;
import com.podloot.eyemod.gui.apps.op.AppTeleport;
import com.podloot.eyemod.gui.apps.op.AppTnt;
import com.podloot.eyemod.gui.apps.op.AppWarp;
import com.podloot.eyemod.gui.apps.op.AppWorld;
import com.podloot.eyemod.gui.apps.op.AppXRay;
import com.podloot.eyemod.gui.apps.server.AppPlayers;
import com.podloot.eyemod.gui.apps.server.AppSpawn;
import com.podloot.eyemod.gui.apps.stock.AppConsole;
import com.podloot.eyemod.gui.apps.stock.AppMail;
import com.podloot.eyemod.gui.apps.stock.AppSettings;
import com.podloot.eyemod.gui.apps.stock.AppStore;
import com.podloot.eyemod.gui.util.commands.Command;
import com.podloot.eyemod.gui.util.commands.CommandApp;
import com.podloot.eyemod.gui.util.commands.CommandDevice;
import com.podloot.eyemod.gui.util.commands.CommandHelp;
import com.podloot.eyemod.gui.util.commands.CommandMoney;
import com.podloot.eyemod.gui.util.commands.CommandNet;
import com.podloot.eyemod.gui.util.commands.CommandPlayer;
import com.podloot.eyemod.gui.util.commands.CommandWorld;

import net.minecraft.resources.ResourceLocation;

public class EyeClient {
	
	// APPS
	public static final App APPSTORE = new AppStore().setDefault(0, 0).setStock();
	public static final App APPSETTINGS = new AppSettings().setDefault(0, 0).setStock();
	public static final App APPMAIL = new AppMail().setDefault(0, 0).setStock().useData();
	public static final App APPCONSOLE = new AppConsole().setDefault(2, 0).setStock();
	
	public static final App APPCONTACTS = new AppContacts().setDefault(0, 0);
	public static final App APPMESSAGES = new AppMessages().setDefault(6, 0).setNet().useData();
	public static final App APPCHAT = new AppChat().setDefault(8, 0).setLocation().useData();
	public static final App APPMAPS = new AppMaps().setDefault(12, 0).setLocation().useData();
	public static final App APPWAYPOINT = new AppWaypoint().setDefault(6, 0).setLocation().useData();
	public static final App APPCLOCK = new AppClock().setDefault(2, 0);
	public static final App APPNOTES = new AppNotes().setDefault(4, 0).useData();
	public static final App APPCAMERA = new AppCamera().setDefault(10, 0);
	public static final App APPPHOTOS = new AppPhotos().setDefault(2, 0);
	public static final App APPMUSIC = new AppMusic().setDefault(6, 0);
	public static final App APPNET = new AppNetwork().setDefault(4, 0).setNet();
	public static final App APPWEATHER = new AppWeather().setDefault(1, 0).setLocation();
	
	public static final App APPTNT = new AppTnt().setDefault(24, 1).setLocation();
	public static final App APPTELEPORT = new AppTeleport().setDefault(16, 1).setLocation();
	public static final App APPWARP = new AppWarp().setDefault(14, 1).setLocation();
	public static final App APPMAGIC = new AppMagic().setDefault(20, 1);
	public static final App APPSPAWNER = new AppSpawner().setDefault(22, 1).setLocation();
	public static final App APPWORLD = new AppWorld().setDefault(12, 1);
	public static final App APPXRAY = new AppXRay().setDefault(30, 1).setLocation();
	
	public static final App APPSPAWN = new AppSpawn().setDefault(0, 0).setLocation();
	public static final App APPPLAYERS = new AppPlayers().setDefault(0, 1);
	
	// COMMANDS
	public static final Command CMDHELP = new CommandHelp();
	public static final Command CMDDEVICE = new CommandDevice();
	public static final Command CMDAPP = new CommandApp();
	public static final Command CMDNET = new CommandNet();
	public static final Command CMDMONEY = new CommandMoney();
	public static final Command CMDWORLD = new CommandWorld();
	public static final Command CMDPLAYER = new CommandPlayer();

	public static void onInitializeClient() {
		// APPS
		EyeApps.register(new ResourceLocation(Eye.MODID, "settings"), APPSETTINGS);
		EyeApps.register(new ResourceLocation(Eye.MODID, "store"), APPSTORE);
		EyeApps.register(new ResourceLocation(Eye.MODID, "mail"), APPMAIL);
		EyeApps.register(new ResourceLocation(Eye.MODID, "console"), APPCONSOLE);
		
		EyeApps.register(new ResourceLocation(Eye.MODID, "contacts"), APPCONTACTS);
		EyeApps.register(new ResourceLocation(Eye.MODID, "camera"), APPCAMERA);
		EyeApps.register(new ResourceLocation(Eye.MODID, "photos"), APPPHOTOS);
		EyeApps.register(new ResourceLocation(Eye.MODID, "messages"), APPMESSAGES);
		EyeApps.register(new ResourceLocation(Eye.MODID, "chat"), APPCHAT);
		EyeApps.register(new ResourceLocation(Eye.MODID, "maps"), APPMAPS);
		EyeApps.register(new ResourceLocation(Eye.MODID, "waypoint"), APPWAYPOINT);
		EyeApps.register(new ResourceLocation(Eye.MODID, "clock"), APPCLOCK);
		EyeApps.register(new ResourceLocation(Eye.MODID, "notes"), APPNOTES);
		EyeApps.register(new ResourceLocation(Eye.MODID, "music"), APPMUSIC);
		EyeApps.register(new ResourceLocation(Eye.MODID, "network"), APPNET);
		EyeApps.register(new ResourceLocation(Eye.MODID, "weather"), APPWEATHER);
		
		EyeApps.register(new ResourceLocation(Eye.MODID, "tnt"), APPTNT);
		EyeApps.register(new ResourceLocation(Eye.MODID, "teleport"), APPTELEPORT);
		EyeApps.register(new ResourceLocation(Eye.MODID, "warp"), APPWARP);
		EyeApps.register(new ResourceLocation(Eye.MODID, "magic"), APPMAGIC);
		EyeApps.register(new ResourceLocation(Eye.MODID, "spawner"), APPSPAWNER);
		EyeApps.register(new ResourceLocation(Eye.MODID, "world"), APPWORLD);
		EyeApps.register(new ResourceLocation(Eye.MODID, "xray"), APPXRAY);

		EyeApps.register(new ResourceLocation(Eye.MODID, "spawn"), APPSPAWN);
		EyeApps.register(new ResourceLocation(Eye.MODID, "players"), APPPLAYERS);
		
		
		
	}

}
