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

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;

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
		
		EyeSlider level = new EyeSlider(getWidth()-4, 24, 1, 100);
		level.setText(new Line("text.eyemod.magic_level").setAllingment(1, 0));
		add(level, 2, 62);
		
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
			PacketHandler.INSTANCE.sendToServer(new ServerEffect(player.getInput(), pot.getEffect(), level.getValue(), tick));
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
	
	
	
	MobEffect[] effects = {
			MobEffects.ABSORPTION,
			MobEffects.BAD_OMEN,
			MobEffects.BLINDNESS,
			MobEffects.CONDUIT_POWER,
			MobEffects.DARKNESS,
			MobEffects.DOLPHINS_GRACE,
			MobEffects.FIRE_RESISTANCE,
			MobEffects.GLOWING,
			MobEffects.DIG_SPEED,
			MobEffects.HEALTH_BOOST,
			MobEffects.HERO_OF_THE_VILLAGE,
			MobEffects.HUNGER,
			MobEffects.HARM,
			MobEffects.HEAL,
			MobEffects.INVISIBILITY,
			MobEffects.JUMP,
			MobEffects.LEVITATION,
			MobEffects.LUCK,
			MobEffects.DIG_SLOWDOWN,
			MobEffects.CONFUSION,
			MobEffects.NIGHT_VISION,
			MobEffects.POISON,
			MobEffects.REGENERATION,
			MobEffects.DAMAGE_RESISTANCE,
			MobEffects.SATURATION,
			MobEffects.MOVEMENT_SLOWDOWN,
			MobEffects.SLOW_FALLING,
			MobEffects.MOVEMENT_SPEED,
			MobEffects.DAMAGE_BOOST,
			MobEffects.UNLUCK,
			MobEffects.WATER_BREATHING,
			MobEffects.WEAKNESS,
			MobEffects.WITHER,
	};

}
