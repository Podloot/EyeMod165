package com.podloot.eyemod.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.podloot.eyemod.EyeApps;
import com.podloot.eyemod.EyeClient;
import com.podloot.eyemod.config.EyeConfig;
import com.podloot.eyemod.gui.apps.App;
import com.podloot.eyemod.gui.apps.AppHome;
import com.podloot.eyemod.gui.apps.AppStart;
import com.podloot.eyemod.gui.elements.Background;
import com.podloot.eyemod.gui.elements.Border;
import com.podloot.eyemod.gui.elements.DeviceButton;
import com.podloot.eyemod.gui.elements.StatusBar;
import com.podloot.eyemod.gui.panes.ConfirmPane;
import com.podloot.eyemod.gui.util.ConnectionManager;
import com.podloot.eyemod.gui.util.Naming.Type;
import com.podloot.eyemod.gui.util.NbtManager;
import com.podloot.eyemod.gui.util.Photos;
import com.podloot.eyemod.gui.util.SettingManager;
import com.podloot.eyemod.gui.util.Time;
import com.podloot.eyemod.gui.util.Timer;
import com.podloot.eyemod.gui.util.commands.CommandManager;
import com.podloot.eyemod.items.ItemDevice;
import com.podloot.eyemod.lib.gui.EyeLib;
import com.podloot.eyemod.lib.gui.EyeScreen;
import com.podloot.eyemod.lib.gui.util.Line;
import com.podloot.eyemod.network.PacketHandler;
import com.podloot.eyemod.network.ServerItemRemove;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;

public abstract class GuiDevice extends EyeScreen {

	//Managers
	public NbtManager data;
	public SettingManager settings;
	public ConnectionManager connect;
	public CommandManager command;
	//public CompoundNBT config;

	//Gui
	int refresh = 0;
	App currentApp;
	int lastPage = 0;
	Border border;
	Background background;
	StatusBar bar;
	
	//Apps
	public Map<ResourceLocation, App> loaded_apps = new HashMap<ResourceLocation, App>();
	private boolean operator = false, opApps = false;
	private Hand hand;
	
	public GuiDevice(String title, int width, int height, Hand hand, CompoundNBT config, boolean operator, boolean opApps) {
		super(title, width, height);
		this.hand = hand;
		this.operator = operator;
		this.opApps = opApps;
		//this.config = config;
		
		data = new NbtManager(hand);
		settings = new SettingManager(data);
		connect = new ConnectionManager(this);
		command = new CommandManager(this, operator);
		

		
		initDevice();
		initApps();
		
		sendSpam(20);
	}
	
	//Setup
	public void initDevice() {		
		base.clear();
		
		//Device
		int deviceColor = settings.getInt("device");
		border = new Border(base.getWidth(), base.getHeight(), deviceColor);
		base.add(border, 0, 0);
		
		//Background
		background = new Background(this.getAppWidth(), this.getAppHeight()+8, settings.get("background"));
		base.add(background, this.getAppX(), this.getAppY()-8);
		
		//Statusbar
		bar = new StatusBar(this, this.getAppWidth(), 8);
		base.add(bar, this.getAppX(), this.getAppY()-8);
		
		//Homebutton
		DeviceButton home = new DeviceButton(20, 20, EyeLib.DEVICE_HOME);
		home.setColor(deviceColor);
		home.setAction(() -> {
			if(currentApp != null) currentApp.onHomeGeneral();
		});
		base.add(home, base.getWidth() / 2 - 10, base.getHeight()-22);
		
		//Left button
		DeviceButton left = new DeviceButton(11, 16, EyeLib.DEVICE_LEFT);
		left.setColor(deviceColor);
		left.setAction(() -> {
			if(currentApp != null) currentApp.onLeft();
		});
		base.add(left, base.getWidth() / 2 - 23, base.getHeight()-20);
		
		//Right button
		DeviceButton right = new DeviceButton(11, 16, EyeLib.DEVICE_RIGHT);
		right.setColor(deviceColor);
		right.setAction(() -> {
			if(currentApp != null) currentApp.onRight();
		});
		base.add(right, base.getWidth() / 2 + 12, base.getHeight()-20);
		
		//Quit button
		DeviceButton quit = new DeviceButton(16, 8, EyeLib.DEVICE_QUIT);
		quit.setColor(deviceColor);
		quit.setAction(() -> {
			this.closeDevice();
		});
		base.add(quit, base.getWidth() - 32, 6);
		
		//Screenshot button
		DeviceButton screenshot = new DeviceButton(8, 8, EyeLib.DEVICE_SCREENSHOT);
		screenshot.setColor(deviceColor);
		screenshot.setAction(() -> {
			String save = "Screenshot_" + Time.getTime().replace(":", "_");
			boolean saved = false;
			if(settings.getBool("camera_res")) {
				saved = Photos.takeShot(save);
			} else {
				saved = Photos.takeShotLow(save);
			}
			if(saved) {
				addNotification(EyeClient.APPPHOTOS.getId(), "New screenshot: " + save);
			} else {
				addNotification(EyeClient.APPPHOTOS.getId(), "Failed to save: " + save);
			}
		});
		if(this.isInstalled(EyeClient.APPCAMERA.getId())) base.add(screenshot, base.getWidth() - 44, 6); //Only works when camera is installed
	}
	
	public void hidePanel(boolean background, boolean bar) {
		this.background.hide(background);
		this.bar.hide(bar);
	}
	
	
	public void initApps() {
		loaded_apps.clear();
		for(App a : EyeApps.getApps()) {
			addApp(a);
		}
		
		App h = new AppStart();
		h.set(this, 0, 0);
		openApp(h);
	}
	
	//Apps
	private void addApp(App app) {
		int access = EyeConfig.getAccess(app.getId());
		int price = EyeConfig.getPrice(app.getId());
		if(access == -1) access = app.getAccess();
		if(price == -1) price = app.getPrice();
		
		if(access == 2) return;
		else if(access == 1 && !operator) return;
		else if(!opApps && app.isOP()) return;
		else {
			app.set(this, price, access);
			loaded_apps.put(app.getId(), app);
		}
	}
	
	
	/**
	 * Method to open an app
	 * @param app
	 * @return if the app could be opened (warning panes are shown anyway)
	 */
	public boolean openApp(App app) {
		if(app.getDevice() == null) return false;
		boolean connected = this.connect.isConnected();
		boolean loc = this.settings.getBool("location");
		if(app.useNet() && !connected) {
			if(currentApp != null) {
				ConfirmPane con = new ConfirmPane(currentApp, this.getAppWidth()-8, 48, new Line("text.eyemod.notify_connection"), false);
				currentApp.openPane(con);
			}
			return false;
		}
		
		if(app.useLocation() && !loc) {
			if(currentApp != null) {
				ConfirmPane con = new ConfirmPane(currentApp, this.getAppWidth()-8, 48, new Line("text.eyemod.notify_location"), false);
				currentApp.openPane(con);
			}
			return false;
		}
		
		if(app.isStock() || isInstalled(app.getId())) {
			if(currentApp != null) {
				currentApp.close();
				base.remove(currentApp);
			}
			
			currentApp = app;
			app.setSize(getAppWidth(), getAppHeight());
			base.add(app, getAppX(), getAppY());
			app.open();
			clearNotifications(app.getId());
			return true;
		}
		return false;		
	}
	
	/**
	 * Open app with ID
	 * @param app id
	 * @return if it could be opened or not
	 */
	public boolean openApp(ResourceLocation app) {
		return openApp(getApp(app));
	}
	
	public List<App> getInstalledApps() {
		ListNBT apps = data.getList("apps", Type.STRING);
		List<App> allowed = new ArrayList<App>();
		for(int i = 0; i < apps.size(); i++) {
			ResourceLocation id = new ResourceLocation(apps.getString(i));
			if(loaded_apps.containsKey(id)) allowed.add(loaded_apps.get(id));
		}
		return allowed;
	}
	
	public App getApp(ResourceLocation id) {
		if(loaded_apps.containsKey(id)) return loaded_apps.get(id);
		else return null;
	}
	
	public void openHome() {
		App h = new AppHome(lastPage);
		h.set(this, 0, 0);
		openApp(h);
	}
	
	public void openConsole() {
		openApp(EyeClient.APPCONSOLE);
	}
	
	public void closeDevice() {
		Minecraft.getInstance().setScreen(null);
    	Minecraft.getInstance().mouseHandler.cursorEntered();
    	this.onClose();
	}
	
	@Override
	public void paint(MatrixStack matrices) {
		if(refresh > 0) {
			if(refresh == 1) this.onRefresh();
			refresh--;
		}
	}

	@Override
	public void update(int mouseX, int mouseY) {
		data.update();
	}
	
	public ClientPlayerEntity getUser() {
		return Minecraft.getInstance().player;
	}
	
	public String getOwner() {
		return data.getString("user");
	}
	
	public boolean isLocked() {
		boolean locked = !getUser().getScoreboardName().equals(getOwner());
		return data.getBool("faceid") ? locked : false;
	}
	
	public ItemStack getItem() {
		return getUser().getItemInHand(hand);
	}
	
	public ResourceLocation getWorldID() {
		return getUser().clientLevel.dimension().location();
	}
	
	public int getAppWidth() {
		return base.getWidth() - 20;
	}
	
	public int getAppHeight() {
		return base.getHeight() - 48 - 8;
	}
	
	public int getAppX() {
		return 10;
	}
	
	public int getAppY() {
		return 24 + 8;
	}
	
	//UTIL
	public ItemStack getCurrency() {
		String id = EyeConfig.currency.get();
		if(id.contains(":")) {
			Item i = Registry.ITEM.get(new ResourceLocation(id.split(":")[0], id.split(":")[1]));
			if(i != null) return i.getDefaultInstance();
			else return Items.EMERALD.getDefaultInstance();
		}
		return Items.EMERALD.getDefaultInstance();
	}
	
	/**
	 * Resets device to clear all data
	 */
	public void clearAll() {
		CompoundNBT empty = new CompoundNBT();
		empty.put("apps", data.getList("apps", Type.STRING));
		empty.putInt("Damage", data.getInt("Damage"));
		empty.putInt("money", data.getInt("money"));
		data.updateNbt(empty);
	}
	
	public boolean hasData() {
		boolean b = data.hasStorage();
		if(b) {
			return true;
		} else {
			if(currentApp != null) {
				ConfirmPane out = new ConfirmPane(currentApp, this.getAppWidth()-4, 36, new Line("text.eyemod.notify_storage"), false);
				currentApp.openPane(out);
			}
			return false;
		}
	}
	
	public boolean hasRouterData() {
		boolean b = connect.getRouterData().storage < connect.getRouterData().max_storage;
		if(b) {
			return true;
		} else {
			if(currentApp != null) {
				ConfirmPane out = new ConfirmPane(currentApp, this.getAppWidth()-4, 36, new Line("text.eyemod.notify_routerstorage"), false);
				currentApp.openPane(out);
			}
			return false;
		}
	}
	
	public void onRefresh() {
		base.clear();
		initDevice();
		initApps();
	}
	
	public void onRefresh(App toOpen) {
		base.clear();
		initDevice();
		this.openApp(toOpen);
	}
	
	public void addNotification(ResourceLocation app, String notification) {
		if(!settings.getBool("notification")) return;
		if(!loaded_apps.containsKey(app)) return;
		notification = notification.replace("~", "-");
		String n = app + "~" + notification;
		data.addToList("notifications", StringNBT.valueOf(n));
	}
	
	public ListNBT getNotifications(ResourceLocation app) {
		ListNBT not = new ListNBT();
		ListNBT no = data.getList("notifications", Type.STRING);
		for(int i = 0; i < no.size(); i++) {
			String s = no.getString(i);
			if(s.contains("~")) {
				String[] in = s.split("~");
				if(in[0].equals(app.toString())) {
					not.add(no.get(i));
				}
			}
		}
		return not;
	}
	
	public void clearNotifications(ResourceLocation app) {
		if(app == null) return;
		ListNBT not = this.getNotifications(app);
		ListNBT no = data.getList("notifications", Type.STRING);
		if(!not.isEmpty()) {
			no.removeAll(not);
		}
		data.setList("notifications", no);
	}

	//Store
	public boolean isInstalled(ResourceLocation app) {
		ListNBT apps = data.getList("apps", Type.STRING);
		if(apps.contains(StringNBT.valueOf(app.getNamespace() + ":" + app.getPath()))) {
			return true;
		}
		return false;
	}
	
	public void deinstallApp(App app) {
		this.deinstallApp(app.getId());
	}

	public void deinstallApp(ResourceLocation app) {
		ListNBT apps = data.getList("apps", Type.STRING);
		for(int i = 0; i < apps.size(); i++) {
			if(apps.getString(i).equals(app.toString())) {
				apps.remove(i);
				if(this.loaded_apps.containsKey(app)) {
					data.addInt("money", this.loaded_apps.get(app).getPrice());
				}
				break;
			}
		}
		data.setList("apps", apps);
	}
	
	public boolean installApp(ResourceLocation app) {
		return installApp(EyeApps.getApp(app));
	}
	
	public boolean installApp(App app) {
		int money = data.getInt("money");
		if(money >= app.getPrice()) {
			data.addInt("money", app.getPrice()*-1);
			data.addToList("apps", StringNBT.valueOf(app.getId().toString()));
			return true;
		} else {
			return false;
		}
	}
	
	public boolean addMoney(int amount) {
		amount = amount >= 128 ? 127 : amount;
		ItemStack currency = getCurrency();
		int inv = getUser().inventory.countItem(currency.getItem());
		if(inv >= amount) {
			currency.setCount(amount);
			PacketHandler.INSTANCE.sendToServer(new ServerItemRemove(currency));
			data.setInt("money", data.getInt("money") + amount);
			return true;
		}
		return false;
	}
	
	public void addTimer(Timer t) {
		if(this.getUser().getItemInHand(hand).getItem() instanceof ItemDevice) {
			((ItemDevice)getUser().getItemInHand(hand).getItem()).timers.add(t);
		}
	}
	
	public void stopTimer(Timer t) {
		if(this.getUser().getItemInHand(hand).getItem() instanceof ItemDevice) {
			((ItemDevice)getUser().getItemInHand(hand).getItem()).timers.remove(t);
		}
	}
	
	public List<Timer> getTimers() {
		if(this.getUser().getItemInHand(hand).getItem() instanceof ItemDevice) {
			return ((ItemDevice)getUser().getItemInHand(hand).getItem()).timers;
		}
		return null;
	}
	
	/***
	 * Get the unique ID of the device
	 * @return device id
	 */
	public String getUniqueID() {
		return data.getString("ID");
	}

	public App getOpenedApp() {
		return currentApp;
	}
	
	private String[][] mails = {
			{"Store", "Update", "Hello {u},|The {ia} app just had an update, go check it out!|Kind regards,|EyeStore"},
			{"Store", "New", "Hello {u},|Did you already check out the {ua} app in the store?|Kind regards,|EyeStore"},
			{"News", "Look out", "There seem to be some lose withers flying around, please close your windows to prevent further damage."},
			{"News", "Outbreak", "Some villagers escaped their trading facility, if you find some suspicious villagers, please notify the authorities!"},
			{"News", "Missing", "A wandering trader hasn't returned home yet... Do you know more about this?"},
			{"News", "Nuisance", "There have been a lot of complaints about some flying creatures, however a solution is yet to be found..."}
	};
	
	private void sendSpam(int range) {
		if(settings.getBool("spam")) return;
		int r = getUser().clientLevel.random.nextInt(range*mails.length + range);
		if(r < mails.length && r >= 0) {
			String sen = mails[r][0];
			String sub = mails[r][1];
			String msg = applyMessage(mails[r][2]);
			connect.sendMail(sen, sub, msg);
		}
	}
	
	public String applyMessage(String msg) {
		msg = msg.replace("{u}", getOwner());
		if(msg.contains("{ua}")) {
			List<String> un = new ArrayList<String>();
			for(ResourceLocation i : loaded_apps.keySet()) {
				if(!getInstalledApps().contains(i)) {
					un.add(loaded_apps.get(i).getName().getText());
				}
			}
			String name = "???";
			if(un.size() >= 1) name = un.get(getUser().clientLevel.random.nextInt(un.size()));
			msg = msg.replace("{ua}", name);
		}
		
		if(msg.contains("{ia}")) {
			String name = "???";
			int i = 0;
			int ra = getUser().clientLevel.random.nextInt(loaded_apps.keySet().size());
			for(ResourceLocation id : loaded_apps.keySet()) {
				if(i == ra) name = loaded_apps.get(id).getName().getText();
				i++;
			}
			msg = msg.replace("{ia}", name);
		}
		
		return msg;
	}
	
	public int getTemp() {
		BlockPos bp = getUser().blockPosition();
		float t = getUser().clientLevel.getBiome(bp).getBaseTemperature();
		return (int)(30F * t)+2;
	}

	public String getWeather() {
		String weather = "text.eyemod.weather_sunny";
		BlockPos bp = getUser().blockPosition();
		boolean snow = getUser().clientLevel.getBiome(bp).getBaseTemperature() < 0.2;
		boolean rain = getUser().clientLevel.isRaining();
		boolean thunder = getUser().clientLevel.isThundering();
		if (rain)
			weather = snow ? "text.eyemod.weather_snowing" : "text.eyemod.weather_raining";
		if (thunder)
			weather = "text.eyemod.weather_thunder";
		return weather;
	}
	
	public int getLastPage() {
		return lastPage;
	}

	public void setLastPage(int page) {
		this.lastPage = page;
	}

}
