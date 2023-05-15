package com.podloot.eyemod.gui.apps.server;

import com.podloot.eyemod.Eye;
import com.podloot.eyemod.gui.apps.App;
import com.podloot.eyemod.lib.gui.panels.EyeListPanel;
import com.podloot.eyemod.lib.gui.util.Line;
import com.podloot.eyemod.lib.gui.widgets.EyeButton;
import com.podloot.eyemod.lib.gui.widgets.EyeLabel;
import com.podloot.eyemod.lib.gui.widgets.EyeList;
import com.podloot.eyemod.lib.gui.widgets.EyePlayer;
import com.podloot.eyemod.lib.gui.widgets.EyeTextField;
import com.podloot.eyemod.network.PacketHandler;
import com.podloot.eyemod.network.ServerKick;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class AppPlayers extends App {

	EyeList players;
	
	public AppPlayers() {
		super(new ResourceLocation(Eye.MODID, "textures/gui/apps/appplayers.png"), 0xffe9e025, "EyeServer");
	}

	@Override
	public boolean load() {
		players = new EyeList(getWidth()-4, getHeight()-22, Axis.VERTICAL);
		for(Player p : device.getUser().level.players()) {
			players.add(getPlayer(p));
		}
		
		add(players, 2, 2);
		
		EyeTextField reason = new EyeTextField(getWidth() - 48, 16);
		reason.setText(new Line("text.eyemod.reason"));
		reason.setSuggest(new String[] {"No reason", "AFK", "Hacking"});
		add(reason, 2, getHeight()-18);
		
		EyeButton kick = new EyeButton(42, 16, new Line("text.eyemod.kick"));
		kick.setColor(appColor);
		kick.setAction(() -> {
			String r = (reason.getInput().isEmpty() ? "Kicked" : reason.getInput()) + " (kicked by: " + device.getUser().getScoreboardName() + ")";
			if(players.getSelected() != null) {
				PacketHandler.INSTANCE.sendToServer(new ServerKick((String)players.getSelected().getData(), r));
			}
			this.refresh();
		});
		add(kick, getWidth()-44, getHeight()-18);
		return true;
	}
	
	public EyeListPanel getPlayer(Player player) {
		EyeListPanel lp = new EyeListPanel(players, 16);
		EyeLabel name = new EyeLabel(lp.getWidth()-18, 16, new Line(player.getScoreboardName()));
		name.setBack(appColor);
		lp.add(name, 18, 0);
		
		EyePlayer pl = new EyePlayer(16, 16, player.getScoreboardName());
		lp.add(pl, 0, 0);
		
		lp.setData(player.getScoreboardName());
		
		return lp;
	}

}
