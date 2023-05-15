package com.podloot.eyemod.gui.apps.stock;

import java.util.ArrayList;
import java.util.List;

import com.podloot.eyemod.Eye;
import com.podloot.eyemod.gui.apps.App;
import com.podloot.eyemod.gui.panes.AppPane;
import com.podloot.eyemod.gui.panes.ColorPane;
import com.podloot.eyemod.gui.panes.ConfirmPane;
import com.podloot.eyemod.gui.panes.PhotoPane;
import com.podloot.eyemod.gui.panes.RouterPane;
import com.podloot.eyemod.gui.util.Naming.Type;
import com.podloot.eyemod.gui.util.NbtManager;
import com.podloot.eyemod.lib.gui.EyeLib;
import com.podloot.eyemod.lib.gui.panels.EyePanel;
import com.podloot.eyemod.lib.gui.util.Color;
import com.podloot.eyemod.lib.gui.util.Line;
import com.podloot.eyemod.lib.gui.widgets.EyeButton;
import com.podloot.eyemod.lib.gui.widgets.EyeItem;
import com.podloot.eyemod.lib.gui.widgets.EyeLabel;
import com.podloot.eyemod.lib.gui.widgets.EyeList;
import com.podloot.eyemod.lib.gui.widgets.EyeProcess;
import com.podloot.eyemod.lib.gui.widgets.EyeTextField;
import com.podloot.eyemod.lib.gui.widgets.EyeToggle;
import com.podloot.eyemod.lib.gui.widgets.EyeVariable;
import com.podloot.eyemod.lib.gui.widgets.EyeWidget;

import net.minecraft.resources.ResourceLocation;

public class AppSettings extends App {
	
	EyeList settings;
	
	EyeProcess battery;
	
	int lastScroll = 0;
	
	private int[] border = {0xff222222, 0xffFFFFFF, 0xffc09c94, 0xff262b4c, 0xfff92c2c, 0};
	private int[] back = {0xff383838, 0xffCECECE, 0xffE0BFB8, 0xff7a83a9, 0xffdc8585, 0};

	public AppSettings() {
		super(new ResourceLocation(Eye.MODID, "textures/gui/apps/appsettings.png"), 0xffCECECE, "Eye");
		this.setColor(0xff222222, 0xff666666);
	}
	
	@Override
	public void open() {
		device.settings.initSettings(device.data.getList("apps", Type.STRING), this.getSettingsWidth());
		super.open();
	}

	@Override
	public boolean load() {	
		loadSettings();
		return true;
	}
	
	public void loadSettings() {
		settings = new EyeList(getWidth()-4, getHeight()-4, Axis.VERTICAL);
		add(settings, 2, 2);
		
		EyeLabel general = new EyeLabel(getWidth(), 16, new Line("text.eyemod.settings_device").setStyle(true, true));
		
		general.setBack(getPrimary());
		settings.add(general);
		for(EyeWidget w : this.getDeviceSettings(this.getSettingsWidth())) {
			settings.add(w);
		}
		
		EyePanel space = new EyePanel(getWidth(), 16);
		settings.add(space);
		
		EyeLabel apps = new EyeLabel(getWidth(), 16, new Line("text.eyemod.settings_apps").setStyle(true, true));
		apps.setBack(getPrimary());
		settings.add(apps);
		for(List<EyeWidget> w : device.settings.app_settings.values()) {
			for(EyeWidget p : w) {
				settings.add(p);
			}
		}
	}
	
	
	
	public List<EyeWidget> getDeviceSettings(int width) {
		List<EyeWidget> set = new ArrayList<EyeWidget>();
		
		this.general(set, width);
		this.account(set, width);
		this.customization(set, width);
		this.privacy(set, width);
		this.eyenet(set, width);
		
		return set;
	}
	
	
	
	private void general(List<EyeWidget> set, int width) {
		EyeLabel gen = new EyeLabel(width, 16, new Line("text.eyemod.settings_general"));
		gen.setBack(getSecondary());
		set.add(gen);
		
		battery = new EyeProcess(width, 16, device.getItem().getMaxDamage());
		battery.setText(new Line("text.eyemod.settings_battery"), true);
		battery.setColor(Color.DARKGRAY, Color.GREEN);
		set.add(battery);

		
		EyeProcess storage = new EyeProcess(width, 16, device.data.getInt("storage"));
		storage.setColor(0xff444444, 0xff333333);
		storage.setText(new Line("text.eyemod.settings_storage"), true);
		storage.setState(NbtManager.getStorage(device.data.getNbt()));
		set.add(storage);
		
		EyeButton appMan = new EyeButton(width, 16);
		appMan.setText(new Line("text.eyemod.settings_appman"));
		appMan.setAction(() -> {
			AppPane ap = new AppPane(this, width, getHeight() - 8);
			this.openPane(ap);
		});
		set.add(appMan);
		
		EyeButton reset = new EyeButton(width, 16);
		reset.setText(new Line("text.eyemod.settings_cleardevice"));
		reset.setColor(Color.RED);
		reset.setAction(() -> {
			ConfirmPane con = new ConfirmPane(this, width, 52, new Line("text.eyemod.settings_clearconfirm"), true);
			con.setAction(() -> {
				device.clearAll();
				device.closeDevice();
			});
			this.openPane(con);
		});
		set.add(reset);
	}
	
	private void account(List<EyeWidget> set, int width) {
		EyeLabel gen = new EyeLabel(width, 16, new Line("text.eyemod.settings_account"));
		gen.setBack(getSecondary());
		set.add(gen);
		
		EyePanel panel = new EyePanel(width, 28);
		EyeVariable var = new EyeVariable(width, 10, new Line("text.eyemod.settings_money"));
		var.setVariable(() -> device.data.getInt("money") + "");
		var.setAlignment(1, 0);
		panel.add(var, 0, 0);
		
		EyeItem item = new EyeItem(16, 16, device.getCurrency());
		panel.add(item, 0, 12);
		
		EyeTextField number = new EyeTextField(width-64, 16);
		int curAmount = device.getUser().getInventory().countItem(device.getCurrency().getItem());
		number.setAllowed("[0-9]+");
		number.setLimit(3);
		number.setInput((curAmount > 64 ? 64 : curAmount) + "");
		panel.add(number, 20, 12);
		
		EyeButton add = new EyeButton(42, 16, new Line("text.eyemod.add"));
		add.setColor(getAppColor());
		add.setAction(() -> {
			String input = number.getInput();
			if(input.matches("[0-9]+")) {
				int amount = Integer.parseInt(input);
				boolean s = device.addMoney(amount > 64 ? 64 : amount);
				this.refresh();
			}
		});
		panel.add(add, width-42, 12);
		set.add(panel);
	}
	
	private void customization(List<EyeWidget> set, int width) {
		EyeLabel gen = new EyeLabel(width, 16, new Line("text.eyemod.settings_customization"));
		gen.setBack(getSecondary());
		set.add(gen);
		set.add(this.getColorSet(width, "device", "text.eyemod.settings_border", border));
		set.add(this.getColorSet(width, "background", "text.eyemod.settings_background", back));
		
		EyeButton custom = new EyeButton(width, 16, new Line("text.eyemod.settings_custom"));
		custom.setColor(getAppColor());
		custom.setAction(() -> {
			PhotoPane pho = new PhotoPane(this, 96, getHeight() - 32);
			pho.setAction(() -> {
				String photo = pho.getPhoto();
				if(photo != null && !photo.isEmpty()) {
					device.settings.setString("background", photo);
				}
				device.onRefresh(this);
			});
			openPane(pho);
		});
		set.add(custom);
	}
	
	private void privacy(List<EyeWidget> set, int width) {
		EyeLabel gen = new EyeLabel(width, 16, new Line("text.eyemod.settings_privacy"));
		gen.setBack(getSecondary());
		set.add(gen);
		EyeToggle face = getToggle(width, new Line("text.eyemod.settings_face"), "faceid");
		set.add(face);
		
		EyeToggle locpanel = getToggle(width, new Line("text.eyemod.settings_location"), "location");
		set.add(locpanel);
		
		EyeToggle not = getToggle(width, new Line("text.eyemod.settings_notifications"), "notification");
		set.add(not);
	}
	
	private void eyenet(List<EyeWidget> set, int width) {
		
		EyeLabel gen = new EyeLabel(width, 16, new Line("text.eyemod.settings_eyenet"));
		gen.setBack(getSecondary());
		set.add(gen);
		
		EyePanel password = new EyePanel(width, 32);
		EyeLabel enter = new EyeLabel(width, 14, new Line("text.eyemod.settings_enterpassword"));
		enter.setBack(Color.LIGHTGRAY);
		EyeTextField pw = new EyeTextField(width-34, 16);
		pw.setInput(device.data.getString("net"));
		pw.setLimit(16);
		pw.setText(new Line("text.eyemod.settings_password"));
		
		EyeButton setpw = new EyeButton(32, 16, new Line("text.eyemod.set"));
		setpw.setColor(appColor);
		setpw.setAction(() -> {
			device.data.setString("net", pw.getInput());
		});
		password.add(enter, 0, 0);
		password.add(pw, 0, 16);
		password.add(setpw, width-32, 16);
		set.add(password);
		
		EyeButton disc = new EyeButton(width, 16, new Line("text.eyemod.settings_disconnect"));
		disc.setColor(Color.RED);
		disc.setAction(() -> {
			device.data.remove("net");
			device.data.remove("router");
		});
		set.add(disc);
		
		EyeButton routerman = new EyeButton(width, 16, new Line("text.eyemod.settings_managenet"));
		routerman.setAction(() -> {
			RouterPane r = new RouterPane(this, getWidth()-4, getHeight()-4);
			this.openPane(r);
		});
		set.add(routerman);
	}
	
	private EyeToggle getToggle(int width, Line line, String key) {
		EyeToggle tog = new EyeToggle(20, 12);
		tog.setText(line);
		tog.setState(device.settings.getBool(key));
		tog.setAction(() -> {
			device.settings.setBool(key, tog.getToggle());
		});
		return tog;
	}

	private EyePanel getColorSet(int width, String key, String title, int[] colors) {
		EyePanel panel = new EyePanel(width, 30);
		int sx = width / (colors.length);
		
		EyeLabel lab = new EyeLabel(width, 10, new Line(title));
		lab.setAlignment(1, 0);
		panel.add(lab, 0, 0);
		
		for(int i = 0; i < colors.length; i++) {
			final int id = i;
			EyeButton clr = new EyeButton(20, 20);
			if(colors[i] != 0) {
				clr.setColor(colors[i]);
				clr.setAction(() -> {
					device.settings.setInt(key, colors[id]);
					this.refresh();
				});
			} else {
				clr.setColor(Color.DARKGRAY, 0xffFFFFFF);
				clr.setIcon(EyeLib.COLOR);
				clr.setAction(() -> {
					ColorPane c = new ColorPane(this, width, 70);
					if(device.settings.get(key).getId() == Type.INT.type) {
						c.setValue(device.settings.getInt(key));
					}
					c.setAction(() -> {
						device.settings.setInt(key, c.getColor());
						this.refresh();
					});
					this.openPane(c);
				});
			}
			panel.add(clr, i*(sx+1), 10);
		}
		
		return panel;
	}
	
	@Override
	public void tick(int mx, int my) {
		battery.setValue(device.getItem().getMaxDamage() - device.getItem().getDamageValue());
		super.tick(mx, my);
	}
	
	@Override
	public void onRefresh() {
		this.lastScroll = settings.getScroll();
		device.onRefresh(this);
		settings.setScroll(lastScroll);
	}
	
	
	@Override
	public void close() {
		super.close();
	}

	public int getSettingsWidth() {
		return getWidth() - 14;
	}

}
