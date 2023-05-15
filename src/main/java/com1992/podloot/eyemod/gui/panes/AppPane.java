package com.podloot.eyemod.gui.panes;

import com.podloot.eyemod.gui.apps.App;
import com.podloot.eyemod.gui.util.Naming.Type;
import com.podloot.eyemod.lib.gui.EyeLib;
import com.podloot.eyemod.lib.gui.panels.EyePanel;
import com.podloot.eyemod.lib.gui.util.Color;
import com.podloot.eyemod.lib.gui.util.Line;
import com.podloot.eyemod.lib.gui.widgets.EyeButton;
import com.podloot.eyemod.lib.gui.widgets.EyeIcon;
import com.podloot.eyemod.lib.gui.widgets.EyeList;

import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;

public class AppPane extends Pane {
	
	EyeList appSettings;
	
	public AppPane(App app, int width, int height) {
		super(app, width, height);
		this.load();
		
	}
	
	public void load() {
		appSettings = new EyeList(getWidth()-4, getHeight()-20, Axis.VERTICAL);
		add(appSettings, 2, 2);
		
		for(App a : app.getDevice().getInstalledApps()) {
			appSettings.add(this.getAppPanel(a));
		}
		addAccept(0);
	}
	
	public EyePanel getAppPanel(App app) {
		EyePanel panel = new EyePanel(getWidth()-14, 40);
		
		EyeIcon icon = new EyeIcon(32, 32, app.appIcon);
		icon.setBack(app.getAppColor());
		
		EyeButton up = new EyeButton(14, 14, EyeLib.UP);
		up.setAction(() -> {
			this.move(app, -1);
		});
		EyeButton down = new EyeButton(14, 14, EyeLib.DOWN);
		down.setAction(() -> {
			this.move(app, 1);
		});
		EyeButton clear = new EyeButton(getWidth()-14 - 50, 14, new Line("text.eyemod.settings_clearapp"));
		clear.setAction(() -> {
			app.onClearData();
		});
		clear.setEnabled(app.hasData());
		panel.add(clear, 50, 0);
		
		EyeButton remove = new EyeButton(getWidth()-14 - 50, 14, new Line("text.eyemod.settings_deinstall"));
		remove.setColor(Color.RED);
		remove.setAction(() -> {
			if(app.hasData()) app.onClearData();
			app.getDevice().deinstallApp(app.getId());
			refresh = 2;
		});
		panel.add(remove, 50, 18);

		panel.add(up, 0, 0);
		panel.add(down, 0, 18);
		
		panel.add(icon, 16, 0);
		
		return panel;
		
	}
	
	public void deleteApp(App app) {
		ListTag apps = this.app.getDevice().data.getList("apps", Type.STRING);
		for(int i = 0; i < apps.size(); i++) {
			if(apps.getString(i).equals(app.getId().toString())) {
				apps.remove(i);
				appSettings.delete(i);
				this.app.getDevice().data.addInt("money", app.getPrice());
				refresh = 2;
				break;
			}
		}
		this.app.getDevice().data.setList("apps", apps);
	}
	
	public ListTag move(App app, int move) {
		ListTag apps = this.app.getDevice().data.getList("apps", Type.STRING);
		StringTag s = StringTag.valueOf(app.getId().toString());
		int i = apps.indexOf(s) + move;
		apps.remove(s);
		int np = i < 0 ? 0 : i > apps.size() ? apps.size() : i;
		apps.add(np, s);
		this.app.getDevice().data.setList("apps", apps);
		appSettings.updateItems();
		refresh = 2;
		return apps;
	}
	
	public void refreshList() {
		int scroll = appSettings.getScroll();
		this.clear();
		this.load();
		appSettings.setScroll(scroll);
	}
	
	int refresh = 0;
	@Override
	public void tick(int mx, int my) {
		if(refresh > 0) {
			if(refresh == 1) this.refreshList();
			refresh--;
		}
		super.tick(mx, my);
	}

}
