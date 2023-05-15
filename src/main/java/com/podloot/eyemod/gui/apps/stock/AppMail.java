package com.podloot.eyemod.gui.apps.stock;

import java.util.ArrayList;
import java.util.List;

import com.podloot.eyemod.Eye;
import com.podloot.eyemod.gui.apps.AppTextList;
import com.podloot.eyemod.gui.util.Naming.Type;
import com.podloot.eyemod.lib.gui.EyeLib;
import com.podloot.eyemod.lib.gui.panels.EyeBoxPanel;
import com.podloot.eyemod.lib.gui.panels.EyePanel;
import com.podloot.eyemod.lib.gui.util.Color;
import com.podloot.eyemod.lib.gui.util.Line;
import com.podloot.eyemod.lib.gui.util.Space;
import com.podloot.eyemod.lib.gui.widgets.EyeButton;
import com.podloot.eyemod.lib.gui.widgets.EyeIcon;
import com.podloot.eyemod.lib.gui.widgets.EyeLabel;
import com.podloot.eyemod.lib.gui.widgets.EyeList;
import com.podloot.eyemod.lib.gui.widgets.EyeText;
import com.podloot.eyemod.lib.gui.widgets.EyeToggle;
import com.podloot.eyemod.lib.gui.widgets.EyeWidget;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

public class AppMail extends AppTextList {
	
	EyeList mail;

	public AppMail() {
		super(new ResourceLocation(Eye.MODID, "textures/gui/apps/appmail.png"), 0xff91d4ca, "Eye");
		
	}

	@Override
	public boolean load() {
		setList(new Space(2, 2, getWidth()-4, getHeight()-4), device.data.getList("mail", Type.STRING));
		return super.load();
	}
	
	@Override
	public EyeWidget getMain(EyeBoxPanel box, int index, CompoundNBT info) {
		EyePanel pan = new EyePanel(getWidth()-14, 36);
		if(info.size() < 3) return pan;
		
		EyeIcon icon = new EyeIcon(32, 32, this.appIcon);
		pan.add(icon, 2, 2);
		
		EyeLabel sub = new EyeLabel(getWidth()-14 - 38 - 16, 14, new Line(info.getString("1")));
		EyeLabel send = new EyeLabel(getWidth()-14 - 80, 14, new Line(info.getString("0")));
		sub.setBack(Color.LIGHTGRAY);
		send.setBack(Color.LIGHTGRAY);
		pan.add(sub, 34, 3);
		pan.add(send, 34, 19);
		
		EyeButton delete = new EyeButton(14, 14, EyeLib.DELETE);
		delete.setColor(Color.DARKGRAY, Color.RED);
		delete.setAction(() -> {
			device.data.removeFromList("mail", index);
			this.refresh();
		});
		pan.add(delete, getWidth()-14 - 18, 3);
		
		EyeWidget exp = getExpendable(box, index, info);
		pan.add(this.getFold(box, exp, new Space(42, 14), new Line("text.eyemod.read"), appColor, new Line("text.eyemod.close"), Color.RED), getWidth()-14 - 44, 19);
		return pan;
	}

	@Override
	public EyeWidget getExpendable(EyeBoxPanel box, int index, CompoundNBT info) {
		EyePanel back = new EyePanel(box.getWidth(), 16);
		if(info.size() < 3) return back;
		EyeText msg = new EyeText(back.getWidth()-14, new Line(info.getString("2")));
		back.setHeight(msg.getHeight()+4);
		msg.setBack(Color.LIGHTGRAY);
		msg.setAlignment(1, 1);
		back.add(msg, 2, 2);
		return back;
	}
	
	@Override
	public List<EyeWidget> getSettings(int width) {
		List<EyeWidget> settings = new ArrayList<EyeWidget>();
		settings.add(getToggle(width, new Line("text.eyemod.mail_filter"), "nospam"));
		return settings;
	}
	
	public EyeToggle getToggle(int width, Line line, String key) {
		EyeToggle tog = new EyeToggle(width, 12);
		tog.setText(line);
		tog.setState(device.settings.getBool(key));
		tog.setAction(() -> {
			device.settings.setBool(key, tog.getToggle());
		});
		return tog;
	}
	
	@Override
	public void onClearData() {
		device.data.remove("mail");
	}

}
