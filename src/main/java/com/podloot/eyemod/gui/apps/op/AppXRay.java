package com.podloot.eyemod.gui.apps.op;

import com.podloot.eyemod.Eye;
import com.podloot.eyemod.gui.apps.App;
import com.podloot.eyemod.gui.elements.map.Map;
import com.podloot.eyemod.gui.elements.map.MapTypeXRay;
import com.podloot.eyemod.gui.panes.ListPane;
import com.podloot.eyemod.lib.gui.util.Line;
import com.podloot.eyemod.lib.gui.widgets.EyeButton;
import com.podloot.eyemod.lib.gui.widgets.EyeLabel;
import com.podloot.eyemod.lib.gui.widgets.EyeSlider;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class AppXRay extends App {

	Map map;
	MapTypeXRay type;
	EyeLabel ylvl;
	int previous_alpha = 0;

	public AppXRay() {
		super(new ResourceLocation(Eye.MODID, "textures/gui/apps/appxray.png"), 0xff494949, "EyeOP");
	}

	@Override
	public boolean load() {
		int ms = device.settings.getInt("map_size") * 64 + 64;
		type = new MapTypeXRay(device.getUser().clientLevel, 255);
		map = new Map(getWidth() - 4, getHeight() - 20, ms < 16 ? 16 : ms > 1048 ? 1048 : ms,
				device.getUser().blockPosition(), type);
		map.addPlayer(device.getUser());
		add(map, 2, 2);
		
		EyeButton legend = new EyeButton(42, 14, new Line("text.eyemod.xray_legend"));
		legend.setColor(appColor);
		legend.setAction(() -> {
			ListPane pane = new ListPane(this, getWidth()-4, getHeight()-4);
			for(Block b : type.ores.keySet()) {
				EyeLabel lbl = new EyeLabel(pane.getWidth()-10, 14, new Line(b.getName().getString()));
				lbl.setBack(type.ores.get(b));
				pane.getList().add(lbl);
			}
			this.openPane(pane);
		});
		add(legend, 2, getHeight()-16);

		EyeSlider alpha = new EyeSlider(getWidth()-48, 12, 0, 60);
		alpha.setAction(() -> {
			type.setAlpha((60-alpha.getValue())*4 + 15);
			map.getContent().refresh(map.getContent().getMapPos(), type);
		});
		alpha.setHover(new Line("text.eyemod.hover_trans"));
		add(alpha, 46, getHeight()-15);
		
		EyeButton plus = new EyeButton(12, 12, new Line("+"));
		plus.setAction(() -> {
			BlockPos pos = map.getContent().getMapPos();
			if(pos.getY() < type.getWorld().getMaxBuildHeight()) {
				map.getContent().refresh(pos.offset(0, 1, 0));
			}
		});
		add(plus, getWidth()-16, getHeight()-32);
		
		EyeButton min = new EyeButton(12, 12, new Line("-"));
		min.setAction(() -> {
			BlockPos pos = map.getContent().getMapPos();
			if(pos.getY() > 0) {
				map.getContent().refresh(pos.offset(0, -1, 0));
			}
		});
		add(min, getWidth()-48, getHeight()-32);
		
		ylvl = new EyeLabel(18, 12, new Line("256"));
		add(ylvl, getWidth()-34, getHeight()-32);
		
		EyeButton reset = new EyeButton(12, 12, new Line("r"));
		reset.setAction(() -> {
			map.getContent().refresh(device.getUser().blockPosition());
		});
		add(reset, 4, getHeight()-32);
		
		return true;
	}
	
	@Override
	public void tick(int mx, int my) {
		ylvl.setText(new Line(map.getContent().getMapPos().getY() + ""));
		super.tick(mx, my);
	}

}
