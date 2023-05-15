package com.podloot.eyemod.gui.presets;

import java.util.List;

import com.podloot.eyemod.gui.apps.App;
import com.podloot.eyemod.gui.panes.ContactsPane;
import com.podloot.eyemod.gui.panes.PlayersPane;
import com.podloot.eyemod.gui.util.Naming.Type;
import com.podloot.eyemod.lib.gui.EyeLib;
import com.podloot.eyemod.lib.gui.util.Color;
import com.podloot.eyemod.lib.gui.util.Line;
import com.podloot.eyemod.lib.gui.widgets.EyeButton;

import net.minecraft.world.entity.player.Player;

public class PlayerPreset extends PresetInput {
	

	public PlayerPreset(App app, int width) {
		super(app, width, 32, new Line("text.eyemod.player").setColor(Color.LIGHTGRAY).setStyle(false, true));	
		name.setAllowed("[a-zA-Z0-9_]+");
		name.setSuggest(getPlayerNames());
		
		EyeButton contacts = new EyeButton(14, 14, EyeLib.CONTACTS);
		contacts.setHover(new Line("text.eyemod.hover_contacts"));
		EyeButton online = new EyeButton(14, 14, EyeLib.ONLINE);
		online.setColor(Color.LIGHTGRAY, Color.GREEN);
		online.setHover(new Line("text.eyemod.hover_online"));
		
		contacts.setAction(() -> {
			ContactsPane con = new ContactsPane(app, app.getWidth() - 4, app.getHeight() - 22);
			con.addItems(app.getDevice().data.getList("contacts", Type.STRING));
			con.setAction(() -> {
				String data = (String)con.getData();
				if(data != null) name.setInput(data);
			});
			app.openPane(con);
		});
		
		online.setAction(() -> {
			PlayersPane onl = new PlayersPane(app, app.getWidth() - 4, app.getHeight() - 22);
			onl.addItems(app.getDevice().getUser().level);
			onl.setAction(() -> {
				String data = (String)onl.getData();
				if(data != null) name.setInput(data);
			});
			app.openPane(onl);
		});
		
		add(contacts, 0, 18);
		add(online, 16, 18);
	}
	
	public String[] getPlayerNames() {
		List<? extends Player> pl = app.getDevice().getUser().level.players();
		String[] names = new String[pl.size()];
		for(int i = 0; i < pl.size(); i++) {
			names[i] = pl.get(i).getScoreboardName();
		}
		return names;
	}

}
