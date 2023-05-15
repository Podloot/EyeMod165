package com.podloot.eyemod.gui.apps.op;

import com.podloot.eyemod.Eye;
import com.podloot.eyemod.gui.apps.App;
import com.podloot.eyemod.gui.presets.PlayerPreset;
import com.podloot.eyemod.gui.presets.PotionPreset;
import com.podloot.eyemod.gui.presets.TimerPreset;
import com.podloot.eyemod.lib.gui.util.Color;
import com.podloot.eyemod.lib.gui.util.Line;
import com.podloot.eyemod.lib.gui.util.Space;
import com.podloot.eyemod.lib.gui.widgets.EyeButton;
import com.podloot.eyemod.lib.gui.widgets.EyeLabel;
import com.podloot.eyemod.lib.gui.widgets.EyeSlider;
import com.podloot.eyemod.lib.gui.widgets.EyeToggle;
import com.podloot.eyemod.network.PacketHandler;
import com.podloot.eyemod.network.ServerEffect;

import net.minecraft.potion.Effect;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;

public class AppMagic extends App {

	public AppMagic() {
		super(new ResourceLocation(Eye.MODID, "textures/gui/apps/appmagic.png"), 0xffedaf3e, "EyeOP");
	}

	@Override
	public boolean load() {
		appColor = 0xffedaf3e;
		EyeLabel tospawn = new EyeLabel(getWidth()-4, 14, new Line("text.eyemod.magic_effect"));
		tospawn.setBack(appColor);
		add(tospawn, 2, 2);
		
		PotionPreset pot = new PotionPreset(this, getWidth()-4, effects);
		add(pot, 2, 18);
		
		EyeSlider World = new EyeSlider(getWidth()-4, 24, 1, 100);
		World.setText(new Line("text.eyemod.magic_World").setAllingment(1, 0));
		add(World, 2, 62);
		
		TimerPreset time = new TimerPreset(this, 52, 16);
		time.setTime(1, 0);
		add(time, getWidth() - time.getWidth() - 2, 88);
		
		EyeLabel t = new EyeLabel(getWidth() - time.getWidth() - 4, 16, new Line("text.eyemod.time"));
		t.setAlignment(-1, 0);
		add(t, 2, 88);
		
		EyeToggle inf = new EyeToggle(20, 12);
		inf.setText(new Line("text.eyemod.infinite"));
		add(inf, 2, 90);
		
		
		PlayerPreset player = new PlayerPreset(this, getWidth()-4);
		player.setInput(device.getOwner());
		player.setButton(new Space(0, 34, getWidth()-4, 16), new Line("text.eyemod.apply"), appColor, () -> {
			final int tick = time.getTime() == 0 || inf.getToggle() ? 999999*20 : time.getTime()*20;
			PacketHandler.INSTANCE.sendToServer(new ServerEffect(player.getInput(), pot.getEffect(), World.getValue(), tick));
		});
		add(player, 2, getHeight()-52);
		
		EyeButton clear = new EyeButton(42, 14, new Line("text.eyemod.clear"));
		clear.setColor(Color.RED);
		clear.setAction(() -> {
			PacketHandler.INSTANCE.sendToServer(new ServerEffect(player.getInput(), 0, 0, 0));
		});
		
		add(clear, getWidth()-44, getHeight()-34);
	
		return true;
		
		
	}
	
	
	
	Effect[] effects = {
			Effects.ABSORPTION,
			Effects.BAD_OMEN,
			Effects.BLINDNESS,
			Effects.CONDUIT_POWER,
			//Effects.DARKNESS,
			Effects.DOLPHINS_GRACE,
			Effects.FIRE_RESISTANCE,
			Effects.GLOWING,
			Effects.DIG_SPEED,
			Effects.HEALTH_BOOST,
			Effects.HERO_OF_THE_VILLAGE,
			Effects.HUNGER,
			Effects.HARM,
			Effects.HEAL,
			Effects.INVISIBILITY,
			Effects.JUMP,
			Effects.LEVITATION,
			Effects.LUCK,
			Effects.DIG_SLOWDOWN,
			Effects.CONFUSION,
			Effects.NIGHT_VISION,
			Effects.POISON,
			Effects.REGENERATION,
			Effects.DAMAGE_RESISTANCE,
			Effects.SATURATION,
			Effects.MOVEMENT_SLOWDOWN,
			Effects.SLOW_FALLING,
			Effects.MOVEMENT_SPEED,
			Effects.DAMAGE_BOOST,
			Effects.UNLUCK,
			Effects.WATER_BREATHING,
			Effects.WEAKNESS,
			Effects.WITHER,
	};

}
