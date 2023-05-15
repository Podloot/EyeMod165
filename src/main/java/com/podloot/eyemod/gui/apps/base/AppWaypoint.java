package com.podloot.eyemod.gui.apps.base;

import java.util.List;

import com.podloot.eyemod.Eye;
import com.podloot.eyemod.gui.apps.AppPoint;
import com.podloot.eyemod.gui.elements.DimensionButton;
import com.podloot.eyemod.gui.presets.PosPreset;
import com.podloot.eyemod.gui.util.Naming.Type;
import com.podloot.eyemod.lib.gui.EyeLib;
import com.podloot.eyemod.lib.gui.panels.EyePanel;
import com.podloot.eyemod.lib.gui.util.Color;
import com.podloot.eyemod.lib.gui.util.Line;
import com.podloot.eyemod.lib.gui.util.Pos;
import com.podloot.eyemod.lib.gui.util.Space;
import com.podloot.eyemod.lib.gui.widgets.EyeButton;
import com.podloot.eyemod.lib.gui.widgets.EyeLabel;
import com.podloot.eyemod.lib.gui.widgets.EyeTextField;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class AppWaypoint extends AppPoint {

	public AppWaypoint() {
		super(new ResourceLocation(Eye.MODID, "textures/gui/apps/appwaypoint.png"), 0xff70bfd2, "Eye");
	}

	@Override
	public boolean load() {
		setList(new Space(2, 2, getWidth()-4, getHeight()-56), device.data.getList("waypoints", Type.STRING));
		
		EyeTextField name = new EyeTextField(getWidth()-4, 16);
		name.setText(new Line("text.eyemod.name"));
		name.setDisallowed("[|]+");
		add(name, 2, getHeight() - 52);
		
		PosPreset pos = new PosPreset(this, getWidth()-4);
		pos.setButton(new Line("text.eyemod.add"), getAppColor(), () -> {
			if(device.hasData()) {
				ResourceLocation w = pos.getWorld();
				BlockPos p = pos.getPos();
				String n = name.getInput();
				n = n.isEmpty() || n == "" ? "Waypoint" : n;
				Pos way = new Pos(p, w, n);
				List<Pos> listpos = device.data.getPosList("waypoints");
				listpos.add(way);
				device.data.setPosList("waypoints", listpos);
				this.refresh();
			}
		});
		add(pos, 2, getHeight() - 34);
		return super.load();
	}
	
	public EyePanel getPanel(int index, Pos waypoint) {
		EyePanel pan = new EyePanel(getWidth()-14, 34);
		pan.setBack(Color.DARKGRAY);
		
		EyeLabel name = new EyeLabel(getWidth()-14 - 20, 14, new Line(waypoint.getName()));
		name.setBack(getAppColor());
		pan.add(name, 2, 2);
		
		BlockPos c = waypoint.getPos();
		EyeLabel coords = new EyeLabel(getWidth()-14 - 20, 14, new Line(c.getX() + "/" + c.getY() + "/" + c.getZ()));
		coords.setBack(getAppColor());
		pan.add(coords, 18, 18);
		
		DimensionButton db = new DimensionButton(14, 14, waypoint.getWorld());
		db.setEnabled(false);
		pan.add(db, 2, 18);
		
		EyeButton delete = new EyeButton(14, 14, EyeLib.DELETE);
		delete.setColor(Color.DARKGRAY, Color.RED);
		delete.setAction(() -> {
			device.data.removeFromList("waypoints", index);
			//List<Pos> p = device.data.getPosList("waypoints");
			//if(index < p.size()) p.remove(index);
			//device.data.setPosList("waypoints", p);
			this.refresh();
		});
		pan.add(delete, getWidth() - 14 - 16, 2);
		return pan;
	}

}
