package com.podloot.eyemod.gui.apps.op;

import com.podloot.eyemod.Eye;
import com.podloot.eyemod.gui.apps.App;
import com.podloot.eyemod.gui.presets.PosPreset;
import com.podloot.eyemod.gui.presets.SpawnPreset;
import com.podloot.eyemod.lib.gui.util.Line;
import com.podloot.eyemod.lib.gui.widgets.EyeLabel;
import com.podloot.eyemod.lib.gui.widgets.EyeList;
import com.podloot.eyemod.lib.gui.widgets.EyeSlider;
import com.podloot.eyemod.lib.gui.widgets.EyeTextField;
import com.podloot.eyemod.network.PacketHandler;
import com.podloot.eyemod.network.ServerEntity;

import net.minecraft.util.ResourceLocation;

public class AppSpawner extends App {
	
	EyeList mobs;

	public AppSpawner() {
		super(new ResourceLocation(Eye.MODID, "textures/gui/apps/appspawner.png"), 0xff85bc60, "EyeOP");
	}

	@Override
	public boolean load() {
		EyeLabel tospawn = new EyeLabel(getWidth()-4, 14, new Line("text.eyemod.spawner_tospawn"));
		tospawn.setBack(appColor);
		add(tospawn, 2, 2);
		
		SpawnPreset spawns = new SpawnPreset(this, getWidth()-4, lib);
		add(spawns, 2 , 18);
		
		EyeSlider amount = new EyeSlider(getWidth()-4, 24, 1, 16);
		amount.setText(new Line("text.eyemod.spawner_amount").setAllingment(1, 0));
		add(amount, 2, 52);
		amount.setHover(new Line("amount of entities"));
		
		EyeLabel info = new EyeLabel(getWidth()-4, 14, new Line("text.eyemod.spawner_info"));
		info.setBack(appColor);
		add(info, 2, 82);
		
		EyeTextField name = new EyeTextField(getWidth()-4, 16);
		name.setText(new Line("text.eyemod.name"));
		name.setAllowed("[a-zA-Z0-9_ ]+");
		name.setLimit(128);
		add(name, 2, 98);
		
		PosPreset pos = new PosPreset(this, getWidth()-4);
		pos.setButton(new Line("text.eyemod.spawn"), appColor, () -> {
			String eid = spawns.getInput();
			eid = eid.toLowerCase();
			eid = eid.trim();
			eid = eid.replace(" ", "_");
			PacketHandler.INSTANCE.sendToServer(new ServerEntity(pos.getPos(), pos.getWorld(), eid, amount.getValue(), name.getInput()));
		});
		add(pos, 2, getHeight()-34);
		
		return true;
	}
	
	
	String[] lib = {
			"allay",
			"armor_stand",
			"arrow",
			"axolotl",
			"bat",
			"bee",
			"blaze",
			"boat",
			"cat",
			"cave_spider",
			"chest_minecart",
			"chicken",
			"cod",
			"command_block_minecart",
			"cow",
			"creeper",
			"dolphin",
			"donkey",
			"drowned",
			"egg",
			"end_crystal",
			"ender_pearl",
			"enderman",
			"endermite",
			"evoker",
			"experience_bottle",
			"experience_orb",
			"eye_of_ender",
			"falling_block",
			"fireball",
			"firework_rocket",
			"fox",
			"furnace_minecart",
			"ghast",
			"giant",
			"glow_squid",
			"goat",
			"guardian",
			"hoglin",
			"hopper_minecart",
			"horse",
			"husk",
			"illusioner",
			"iron_golem",
			"lightning_bolt",
			"llama",
			"magma_cube",
			"minecart",
			"mooshroom",
			"mule",
			"ocelot",
			"panda",
			"parrot",
			"phantom",
			"pig",
			"piglin",
			"piglin_brute",
			"pillager",
			"polar_bear",
			"potion",
			"pufferfish",
			"rabbit",
			"ravager",
			"salmon",
			"sheep",
			"shulker",
			"shulker_bullet",
			"silverfish",
			"skeleton",
			"skeleton_horse",
			"slime",
			"small_fireball",
			"snow_golem",
			"snowball",
			"spawner_minecart",
			"spectral_arrow",
			"spider",
			"squid",
			"stray",
			"strider",
			"strider",
			"tnt",
			"tnt_minecart",	
			"trader_llama",
			"trident",
			"tropical_fish",
			"turtle",
			"turtle",
			"villager",
			"vindicator",
			"wandering_trader",
			"witch",
			"witch",
			"wither_skeleton",
			"wither_skull",
			"wolf",
			"zoglin",
			"zombie",
			"zombie_horse",
			"zombie_villager",
			"zombified piglin",
			"zombified piglin",
	};
}
