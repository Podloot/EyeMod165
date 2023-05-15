package com.podloot.eyemod.gui.presets;

import com.podloot.eyemod.gui.apps.App;
import com.podloot.eyemod.gui.elements.DimensionButton;
import com.podloot.eyemod.gui.panes.MapPane;
import com.podloot.eyemod.gui.panes.WaypointPane;
import com.podloot.eyemod.gui.util.Naming.Type;
import com.podloot.eyemod.lib.gui.EyeLib;
import com.podloot.eyemod.lib.gui.util.Color;
import com.podloot.eyemod.lib.gui.util.Line;
import com.podloot.eyemod.lib.gui.util.Pos;
import com.podloot.eyemod.lib.gui.util.Space;
import com.podloot.eyemod.lib.gui.widgets.EyeButton;
import com.podloot.eyemod.lib.gui.widgets.EyeTextField;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

public class PosPreset extends Preset {
	
	DimensionButton dim;
	EyeButton select;
	
	EyeTextField x;
	EyeTextField y;
	EyeTextField z;
	
	RayTraceResult blockHit;

	public PosPreset(App app, int width) {
		super(app, width, 32);
		
		dim = new DimensionButton(14, 14, Minecraft.getInstance().level);
		dim.setHover(new Line("text.eyemod.hover_dimension"));
		
		int w = width - 14;
		int s = (w - 4) / 3;
		
		BlockPos bp = app.getDevice().getUser().blockPosition();
		x = new EyeTextField(s, 16);
		x.setText(new Line("X"));
		x.setAllowed("[0-9~-]+");
		x.setSuggest(new String[] {bp.getX() + ""});
		y = new EyeTextField(s, 16);
		y.setText(new Line("Y"));
		y.setAllowed("[0-9~-]+");
		y.setSuggest(new String[] {bp.getY() + ""});
		z = new EyeTextField(s, 16);
		z.setText(new Line("Z"));
		z.setAllowed("[0-9~-]+");
		z.setSuggest(new String[] {bp.getZ() + ""});
		
		add(x, 0, 0);
		add(y, (w/2) - s/2 - 1, 0);
		add(z, (w/2) + s/2 + 1, 0);
		add(dim, width - 14, 1);
		
		EyeButton current = new EyeButton(14, 14, EyeLib.POSITION);
		current.setColor(Color.LIGHTGRAY, 0xffFFFFFF);
		current.setHover(new Line("text.eyemod.hover_currentpos"));
		EyeButton way = new EyeButton(14, 14, EyeLib.WAYPOINT);
		way.setColor(Color.LIGHTGRAY, 0xffFFFFFF);
		way.setHover(new Line("text.eyemod.hover_waypoints"));
		EyeButton map = new EyeButton(14, 14, EyeLib.MAP);
		map.setColor(Color.LIGHTGRAY, 0xffFFFFFF);
		map.setHover(new Line("text.eyemod.hover_mappos"));
		EyeButton look = new EyeButton(14, 14, EyeLib.LOOKING);
		look.setColor(Color.LIGHTGRAY, 0xffFFFFFF);
		look.setHover(new Line("text.eyemod.hover_lookingat"));
		
		current.setAction(() -> {
			x.setInput("~");
			y.setInput("~");
			z.setInput("~");
		});
		
		way.setAction(() -> {
			WaypointPane l = new WaypointPane(app, app.getWidth() - 4, app.getHeight() - 22);
			l.addItems(app.getDevice().data.getList("waypoints", Type.STRING));
			l.setAction(() -> {
				String sp = (String)l.getData();
				if(sp != null) this.setInput(new Pos().fromString(sp));
			});
			app.openPane(l);
		});
		
		map.setAction(() -> {
			MapPane m = new MapPane(app, app.getWidth() - 4, app.getHeight() - 22);
			m.setAction(() -> {
				setInput(m.getPos().offset(0, 1, 0), m.getWorld());
			});
			app.openPane(m);
		});
		
		look.setAction(() -> {
			blockHit = app.getDevice().getUser().pick(100.0, 0.0f, false);
			if (this.blockHit.getType() == RayTraceResult.Type.BLOCK) {
	            BlockPos data = new BlockPos(((RayTraceResult)this.blockHit).getLocation());
	            if(data != null) this.setInput(data.offset(0, 1, 0), app.getDevice().getWorldID());
			}
		});
		
		add(current, 0, 18);
		add(way, 16, 18);
		add(map, 32, 18);
		add(look, 48, 18);
		
	}
	
	public void setInput(BlockPos pos, ResourceLocation world) {
		if(pos != null) {
			x.setInput(pos.getX() + "");
			y.setInput(pos.getY() + "");
			z.setInput(pos.getZ() + "");
			if(world != null) dim.setDimension(world);
		}
	}
	
	public void setInput(Pos m) {
		BlockPos pos = m.getPos();
		if(pos != null) {
			x.setInput(pos.getX() + "");
			y.setInput(pos.getY() + "");
			z.setInput(pos.getZ() + "");
			dim.setDimension(m.getWorld());
		}
	}
	
	public void setButton(Line text, int color, Runnable action) {
		setButton(new Space(64, 18, getWidth()-64, 14), text, color, action);
	}
	
	public void setButton(Space space, Line text, int color, Runnable action) {
		select = new EyeButton(space.width, space.height, text);
		select.setText(text);
		select.setColor(color);
		select.setAction(action);
		add(select, space.x, space.y);
		if(space.y + space.height > getHeight()) {
			setHeight(space.y + space.height);
		}
	}
	
	public BlockPos getPos() {
		BlockPos u = Minecraft.getInstance().player.blockPosition();
		String sx = x.getInput();
		String sy = y.getInput();
		String sz = z.getInput();
		return new BlockPos(getInt(sx, u.getX()), getInt(sy, u.getY()), getInt(sz, u.getZ()));
	}
	
	private int getInt(String s, int tilt) {
		s = s.isEmpty() || s.equals("") || !s.matches("[0-9~-]+") ? "~" : s;
		if(s.contains("-")) s = "-" + s.replace("-", "");
		if(s.contains("~")) {
			String v = s.replace("~", "");
			return Integer.valueOf(v.equals("") ? "0" : v) + tilt;
		} else {
			return Integer.valueOf(s);
		}
	}

	public ResourceLocation getWorld() {
		return dim.getDimension();
	}

}
