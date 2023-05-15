package com.podloot.eyemod.gui.apps.op;

import com.podloot.eyemod.Eye;
import com.podloot.eyemod.config.EyeConfig;
import com.podloot.eyemod.gui.apps.App;
import com.podloot.eyemod.gui.presets.PosPreset;
import com.podloot.eyemod.gui.presets.TimerListPreset;
import com.podloot.eyemod.gui.presets.TimerPreset;
import com.podloot.eyemod.gui.util.Timer;
import com.podloot.eyemod.lib.gui.util.Line;
import com.podloot.eyemod.lib.gui.widgets.EyeLabel;
import com.podloot.eyemod.lib.gui.widgets.EyeSlider;
import com.podloot.eyemod.lib.gui.widgets.EyeToggle;
import com.podloot.eyemod.network.PacketHandler;
import com.podloot.eyemod.network.ServerExplode;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

public class AppTnt extends App {
	
	TimerListPreset timers;
	
	public AppTnt() {
		super(new ResourceLocation(Eye.MODID, "textures/gui/apps/apptnt.png"), 0xffe56975, "EyeOP");
	}

	@Override
	public boolean load() {
		timers = new TimerListPreset(this, getWidth()-4, getHeight()-92, "TNT");
		add(timers, 2, 2);
		
		int max_size = device.config.getInt("max_explode_size");
		EyeSlider size = new EyeSlider(getWidth() - 4, 24, 1, max_size <= 0 ? 24 : max_size);
		size.setText(new Line("text.eyemod.tnt_size"));
		add(size, 2, getHeight() - 90);

		EyeToggle fire = new EyeToggle(40, 12);
		fire.setText(new Line("text.eyemod.tnt_fire"));
		add(fire, 2, getHeight() - 50);

		EyeToggle dmg = new EyeToggle(40, 12);
		dmg.setText(new Line("text.eyemod.tnt_damage"));
		add(dmg, 2, getHeight() - 64);

		
		TimerPreset timer = new TimerPreset(this, 62, 14);
		EyeLabel tlbl = new EyeLabel(32, 14, new Line("text.eyemod.tnt_timer"));
		tlbl.setAlignment(-1, 0);
		add(tlbl, getWidth() - 34 - 64, getHeight() - 50);
		add(timer, getWidth() - 64, getHeight() - 50);

		PosPreset pos = new PosPreset(this, getWidth() - 4);
		pos.setButton(new Line("text.eyemod.tnt_explode"), appColor, () -> {
			final BlockPos fp = pos.getPos();
			final ResourceLocation fi = pos.getWorld();
			final int s = size.getValue();
			final boolean f = fire.getToggle();
			final boolean d = dmg.getToggle();
			Timer tim = new Timer(device.getUniqueID(), "TNT", () -> {
				PacketHandler.INSTANCE.sendToServer(new ServerExplode(pos.getPos(), pos.getWorld(), size.getValue(), fire.getToggle(), dmg.getToggle()));
			}, timer.getTime());
			device.addTimer(tim);
			timers.refresh();
		});
		add(pos, 2, getHeight() - pos.getHeight() - 2);

		return true;
	}

}
