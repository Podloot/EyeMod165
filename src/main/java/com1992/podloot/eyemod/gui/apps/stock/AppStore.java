package com.podloot.eyemod.gui.apps.stock;

import java.util.List;

import com.podloot.eyemod.Eye;
import com.podloot.eyemod.gui.apps.App;
import com.podloot.eyemod.gui.panes.ConfirmPane;
import com.podloot.eyemod.gui.panes.StorePane;
import com.podloot.eyemod.lib.gui.panels.EyePanel;
import com.podloot.eyemod.lib.gui.util.Color;
import com.podloot.eyemod.lib.gui.util.Line;
import com.podloot.eyemod.lib.gui.widgets.EyeButton;
import com.podloot.eyemod.lib.gui.widgets.EyeIcon;
import com.podloot.eyemod.lib.gui.widgets.EyeLabel;
import com.podloot.eyemod.lib.gui.widgets.EyeList;
import com.podloot.eyemod.lib.gui.widgets.EyeText;
import com.podloot.eyemod.lib.gui.widgets.EyeVariable;

import net.minecraft.resources.ResourceLocation;

public class AppStore extends App {
	
	EyeList store;

	public AppStore() {
		super(new ResourceLocation(Eye.MODID, "textures/gui/apps/appstore.png"), 0xff98bdff, "Eye");
	}

	@Override
	public boolean load() {
		store = new EyeList(getWidth()-4, getHeight()-22, Axis.VERTICAL);
		add(store, 2, 2);
		
		List<App> installed = device.getInstalledApps();
		
		for(App a : device.loaded_apps.values()) {
			if(!installed.contains(a)) store.add(getAppPanel(a, getWidth()-14));
		}
		if(store.sizeList() <= 0) {
			EyeText out = new EyeText(getWidth()-14, 8, new Line("text.eyemod.store_out"));
			out.setAlignment(1, 1);
			out.setBack(getAppColor());
			store.add(out);
		}
		
		EyeVariable money = new EyeVariable(getWidth()/2 - 8, 16, new Line("text.eyemod.store_price"));
		money.setBack(Color.DARKGRAY);
		money.setVariable(() -> device.data.getInt("money") + "");
		
		add(money, 2, getHeight() - 18);
		
		EyeButton apps = new EyeButton(getWidth()/2 - 4, 16, new Line("text.eyemod.store_apps"));
		apps.setColor(getAppColor());
		apps.setAction(() -> {
			StorePane pane = new StorePane(this, getWidth()-8, getHeight()-8);
			this.openPane(pane);
			pane.setAction(() -> {
				this.refresh();
			});
		});
		add(apps, getWidth()/2 + 2, getHeight()-18);
		
		return false;
	}
	
	public EyePanel getAppPanel(App app, int w) {
		EyePanel panel = new EyePanel(w, 90);
		panel.setBack(getAppColor());
		
		
		EyeIcon icon = new EyeIcon(32, 32, app.appIcon);
		icon.setBack(app.appColor);
		
		EyeLabel name = new EyeLabel(w-42, 16, app.getName());
		name.setAlignment(1, 0);
		name.setBack(app.getAppColor());
		EyeLabel creator = new EyeLabel(w-42, 14, new Line(app.getCreator()));
		creator.setAlignment(1, 0);
		creator.setBack(app.getAppColor());
		EyeText desc = new EyeText(w-8, 32, app.getDescription());
		desc.setAlignment(0, 0);
		desc.setBack(app.appColor);
		EyeLabel price = new EyeLabel(42, 14, new Line("text.eyemod.store_price"));
		price.setBack(Color.LIGHTGRAY);
		price.setVariable(app.getPrice() + "");
		price.setAlignment(1, 0);
		
		EyeButton buy = new EyeButton(w-52, 14, new Line("text.eyemod.buy"));
		buy.setColor(0xff91d688);
		buy.setAction(() -> {
			if(this.buy(app)) {
				store.delete(panel);
				device.connect.sendMail(getCreator(), "Purchase", "Dear " + device.getOwner() + ", | Thank you for your purchase at the EyeStore! | | "
						+ "App: " + app.getName().getText() + " | "
								+ "Price: $" + app.getPrice());
			}
		});
		panel.add(icon, 4, 4);
		panel.add(name, 38, 4);
		panel.add(creator, 38, 22);
		panel.add(desc, 4, 38);
		panel.add(price, 4, 72);
		panel.add(buy, 48, 72);
		return panel;
	}
	
	public boolean buy(App app) {
		boolean ins = device.installApp(app);
		if(ins) {
			ConfirmPane con = new ConfirmPane(this, getWidth()-8, 52,  new Line("text.eyemod.store_bought"), false);
			openPane(con);
			con.setAction(() -> {
				this.refresh();
			});
			return true;
		} else {
			ConfirmPane mon = new ConfirmPane(this, getWidth()-8, 52,  new Line("text.eyemod.store_money"), false);
			this.openPane(mon);
			return false;
		}
	}
	
	@Override
	public void onRefresh() {
		int scroll = store.getScroll();
		super.onRefresh();
		if(store != null) store.setScroll(scroll);
		
	}
	

}
