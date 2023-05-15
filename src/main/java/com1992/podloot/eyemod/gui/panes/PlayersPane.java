package com.podloot.eyemod.gui.panes;

import com.podloot.eyemod.gui.apps.App;
import com.podloot.eyemod.lib.gui.panels.EyeListPanel;
import com.podloot.eyemod.lib.gui.util.Line;
import com.podloot.eyemod.lib.gui.widgets.EyeLabel;
import com.podloot.eyemod.lib.gui.widgets.EyePlayer;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class PlayersPane extends ListPane {

	public PlayersPane(App app, int width, int height) {
		super(app, width, height);
	}
	
	public void addItems(Level world) {
		for (Player point : world.players()) {
			list.add(getPanel(point.getScoreboardName()));
		}
	}
	
	public EyeListPanel getPanel(String name) {
		EyeListPanel lp = new EyeListPanel(list, 16);
		EyePlayer p = new EyePlayer(16, 16, name);
		lp.add(p, 0, 0);
		EyeLabel plane = new EyeLabel(list.getWidth()-10 - 18, 16, new Line(name));
		plane.setBack(app.getAppColor());
		lp.add(plane, 18, 0);
		lp.setData(name);
		return lp;
	}

}
