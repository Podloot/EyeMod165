package com.podloot.eyemod.gui.apps.base;

import com.podloot.eyemod.Eye;
import com.podloot.eyemod.gui.apps.App;
import com.podloot.eyemod.gui.presets.PlayerPreset;
import com.podloot.eyemod.gui.util.Naming.Type;
import com.podloot.eyemod.lib.gui.EyeLib;
import com.podloot.eyemod.lib.gui.panels.EyePanel;
import com.podloot.eyemod.lib.gui.util.Color;
import com.podloot.eyemod.lib.gui.util.Line;
import com.podloot.eyemod.lib.gui.widgets.EyeButton;
import com.podloot.eyemod.lib.gui.widgets.EyeLabel;
import com.podloot.eyemod.lib.gui.widgets.EyeList;
import com.podloot.eyemod.lib.gui.widgets.EyePlayer;

import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.ResourceLocation;

public class AppContacts extends App {

	EyeList contacts;

	public AppContacts() {
		super(new ResourceLocation(Eye.MODID, "textures/gui/apps/appcontacts.png"), 0xffc7a47d, "Eye");
	}

	@Override
	public boolean load() {
		contacts = new EyeList(getWidth() - 4, getHeight() - 4 - 34, Axis.VERTICAL);
		ListTag cons = device.data.getList("contacts", Type.STRING);
		for (int i = 0; i < cons.size(); i++) {
			contacts.add(getContactPane(i, cons.getString(i)));
		}
		add(contacts, 2, 2);

		PlayerPreset add = new PlayerPreset(this, getWidth() - 4);
		add.setButton(new Line("text.eyemod.add"), getAppColor(), () -> {
			if (!add.getInput().isEmpty()) {
				if (device.hasData()) {
					device.data.addToList("contacts", StringTag.valueOf(add.getInput()));
					this.refresh();
				}
			}
		});
		add(add, 2, getHeight() - 34);

		return true;
	}

	public EyePanel getContactPane(int index, String name) {
		EyePanel pan = new EyePanel(getWidth() - 14, 18);
		pan.setBack(Color.DARKGRAY);

		EyePlayer player = new EyePlayer(14, 14, name);
		pan.add(player, 2, 2);

		EyeLabel plate = new EyeLabel(getWidth() - 14 - 36, 14, new Line(name));
		plate.setBack(getAppColor());
		pan.add(plate, 18, 2);

		EyeButton rem = new EyeButton(14, 14, EyeLib.DELETE);
		rem.setColor(Color.DARKGRAY, Color.RED);
		rem.setAction(() -> {
			ListTag cont = device.data.getList("contacts", Type.STRING);
			cont.remove(index);
			device.data.setList("contacts", cont);
			this.refresh();
		});
		pan.add(rem, getWidth() - 14 - 16, 2);

		return pan;
	}

}
