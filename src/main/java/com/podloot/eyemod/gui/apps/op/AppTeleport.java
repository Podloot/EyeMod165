package com.podloot.eyemod.gui.apps.op;

import com.podloot.eyemod.Eye;
import com.podloot.eyemod.gui.apps.App;
import com.podloot.eyemod.gui.presets.PlayerPreset;
import com.podloot.eyemod.gui.presets.PosPreset;
import com.podloot.eyemod.lib.gui.util.Color;
import com.podloot.eyemod.lib.gui.util.Line;
import com.podloot.eyemod.lib.gui.util.Space;
import com.podloot.eyemod.lib.gui.widgets.EyeLabel;
import com.podloot.eyemod.lib.gui.widgets.EyeSwitch;
import com.podloot.eyemod.network.PacketHandler;
import com.podloot.eyemod.network.ServerTeleport;

import net.minecraft.util.ResourceLocation;

public class AppTeleport extends App {
	
	PosPreset pos;
	PlayerPreset player;

	public AppTeleport() {
		super(new ResourceLocation(Eye.MODID, "textures/gui/apps/appteleport.png"), 0xffb054d4, "EyeOP");
	}

	@Override
	public boolean load() {
		
		EyeLabel to = new EyeLabel(getWidth()-4, 16, new Line("text.eyemod.teleport_to"));
		to.setBack(appColor);
		add(to, 2, getHeight()-86);
		
		EyeSwitch kind = new EyeSwitch(getWidth()-4, 14);
		kind.setColor(appColor, Color.DARKGRAY);
		kind.addState(new Line("text.eyemod.position"));
		kind.addState(new Line("text.eyemod.player"));
		
		EyeLabel target = new EyeLabel(getWidth()-4, 16, new Line("text.eyemod.teleport_target"));
		target.setBack(appColor);
		add(target, 2, getHeight()-144);
		
		PlayerPreset who = new PlayerPreset(this, getWidth()-4);
		who.setInput(device.getUser().getScoreboardName());
		add(who, 2, getHeight()-126);
		
		pos = new PosPreset(this, getWidth()-4);
		pos.setButton(new Space(0, 34, getWidth()-4, 16), new Line("text.eyemod.teleport_tp"), appColor, () -> {
			PacketHandler.INSTANCE.sendToServer(new ServerTeleport(who.getInput(), pos.getPos(), pos.getWorld()));
		});
		add(pos, 2, getHeight()-52);
		
		player = new PlayerPreset(this, getWidth()-4);
		player.hide(true);
		player.setButton(new Space(0, 34, getWidth()-4, 16), new Line("text.eyemod.teleport_tp"), appColor, () -> {
			PacketHandler.INSTANCE.sendToServer(new ServerTeleport(who.getInput(), player.getInput()));
		});
		add(player, 2, getHeight()-52);
		
		
		kind.setAction(() -> {
			pos.hide(kind.getState() != 0);
			player.hide(kind.getState() == 0);
		});
		
		add(kind, 2, getHeight()-68);
		
		return true;
	}

}
