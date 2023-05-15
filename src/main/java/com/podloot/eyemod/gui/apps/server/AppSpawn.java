package com.podloot.eyemod.gui.apps.server;

import com.podloot.eyemod.Eye;
import com.podloot.eyemod.config.EyeConfig;
import com.podloot.eyemod.gui.apps.AppPoint;
import com.podloot.eyemod.gui.elements.DimensionButton;
import com.podloot.eyemod.lib.gui.EyeLib;
import com.podloot.eyemod.lib.gui.panels.EyePanel;
import com.podloot.eyemod.lib.gui.util.Color;
import com.podloot.eyemod.lib.gui.util.Line;
import com.podloot.eyemod.lib.gui.util.Pos;
import com.podloot.eyemod.lib.gui.util.Space;
import com.podloot.eyemod.lib.gui.widgets.EyeButton;
import com.podloot.eyemod.lib.gui.widgets.EyeLabel;
import com.podloot.eyemod.lib.gui.widgets.EyeWidget;
import com.podloot.eyemod.network.PacketHandler;
import com.podloot.eyemod.network.ServerTeleport;

import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class AppSpawn extends AppPoint {

	public AppSpawn() {
		super(new ResourceLocation(Eye.MODID, "textures/gui/apps/appspawn.png"), 0xffe9a7d1, "EyeServer");
	}
	
	@Override
	public boolean load() {
		ListNBT spawns = EyeConfig.getSpawns();
		if(device.data.has("home") && EyeConfig.allow_home.get()) spawns.add(StringNBT.valueOf(device.data.getString("home")));
		this.setList(new Space(2, 2, getWidth() - 4, getHeight() - 22), spawns);
		
		EyeButton home = new EyeButton(62, 16, new Line("text.eyemod.spawn_home"));
		home.setColor(appColor);
		home.setEnabled(EyeConfig.allow_home.get());
		home.setAction(() -> {
			Pos pos = new Pos(device.getUser().blockPosition(), device.getWorldID(), "Home");
			device.data.setPos("home", pos);
			this.refresh();
		});
		add(home, getWidth() - 64, getHeight()-18);
		
		EyeButton delete = new EyeButton(16, 16, EyeLib.DELETE);
		delete.setColor(Color.DARKGRAY, Color.RED);
		delete.setEnabled(EyeConfig.allow_home.get());
		delete.setHover(new Line("text.eyemod.hover_deletehome"));
		delete.setAction(() -> {
			device.data.remove("home");
			this.refresh();
		});
		add(delete, getWidth() - 84, getHeight() - 18);
		
		return super.load();
	}

	@Override
	public EyeWidget getPanel(int index, Pos pos) {
		EyePanel pan = new EyePanel(getWidth()-14, 34);
		pan.setBack(Color.DARKGRAY);
		if(pos == null) return pan;
		 
		EyeLabel name = new EyeLabel(getWidth()-14 - 20, 14, new Line(pos.getName()));
		name.setBack(getAppColor());
		pan.add(name, 2, 2);
		
		BlockPos c = pos.getPos();
		EyeLabel coords = new EyeLabel(getWidth()- 14 - 48, 14, new Line(c.getX() + "/" + c.getY() + "/" + c.getZ()));
		coords.setBack(Color.LIGHTGRAY);
		pan.add(coords, 2, 18);
		
		DimensionButton db = new DimensionButton(14, 14, pos.getWorld());
		db.setEnabled(false);
		pan.add(db, getWidth() - 14 - 16, 2);
		
		EyeButton warp = new EyeButton(42, 14, new Line("text.eyemod.spawn"));
		warp.setColor(appColor);
		warp.setAction(() -> {
			PacketHandler.INSTANCE.sendToServer(new ServerTeleport(device.getUser().getScoreboardName(), pos.getPos(), pos.getWorld()));
		});
		pan.add(warp, getWidth() - 16 - 42, 18);
		return pan;
	}

}
