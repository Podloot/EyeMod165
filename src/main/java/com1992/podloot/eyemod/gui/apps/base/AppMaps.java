package com.podloot.eyemod.gui.apps.base;

import java.util.ArrayList;
import java.util.List;

import com.podloot.eyemod.Eye;
import com.podloot.eyemod.config.EyeConfig;
import com.podloot.eyemod.gui.apps.App;
import com.podloot.eyemod.gui.elements.map.Map;
import com.podloot.eyemod.gui.util.Naming.Type;
import com.podloot.eyemod.lib.gui.util.Line;
import com.podloot.eyemod.lib.gui.util.Pos;
import com.podloot.eyemod.lib.gui.widgets.EyeSlider;
import com.podloot.eyemod.lib.gui.widgets.EyeWidget;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.ResourceLocation;

public class AppMaps extends App {
	
	Map map;

	public AppMaps() {
		super(new ResourceLocation(Eye.MODID, "textures/gui/apps/appmaps.png"), 0xffd4d1ab, "Eye");
	}

	@Override
	public boolean load() {
		int ms = device.settings.getInt("map_size")*64 + 64;
	
		map = new Map(getWidth()-4, getHeight()-4, ms < 16 ? 16 : ms > 1048 ? 1048 : ms, device.getUser().blockPosition(), device.getUser().level);
		add(map, 2, 2);
		
		map.addPlayer(device.getUser());
		map.addPlayers();
		
		ListTag spawns = device.config.getSpawns();
		if(device.data.has("home") && device.config.getBoolean("allow_home")) spawns.add(StringTag.valueOf(device.data.getString("home")));
		spawns.addAll(device.data.getList("waypoints", Type.STRING));
		map.addWaypoints(spawns);
		
		return true;
	}
	
	public String getPos() {
		BlockPos p = map.getSelected();
		if(p == null) return "";
		return p.getX() + "/" + p.getY() + "/" + p.getZ();
	}
	
	@Override
	public List<EyeWidget> getSettings(int width) {
		List<EyeWidget> settings = new ArrayList<EyeWidget>();
		
		EyeSlider size = new EyeSlider(width, 24, 1, 7);
		size.setText(new Line("text.eyemod.map_size"));
		size.setState(device.settings.getInt("map_size"));
		size.setAction(() -> {
			device.settings.setInt("map_size", size.getValue());
		});
		size.setShowValue(() -> size.getValue()*64 + 64);
		settings.add(size);
		return settings;
	}

}
