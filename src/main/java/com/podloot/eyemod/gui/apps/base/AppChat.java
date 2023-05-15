package com.podloot.eyemod.gui.apps.base;

import java.util.ArrayList;
import java.util.List;

import com.podloot.eyemod.Eye;
import com.podloot.eyemod.gui.apps.App;
import com.podloot.eyemod.gui.util.Naming.Type;
import com.podloot.eyemod.lib.gui.EyeLib;
import com.podloot.eyemod.lib.gui.util.Line;
import com.podloot.eyemod.lib.gui.widgets.EyeButton;
import com.podloot.eyemod.lib.gui.widgets.EyeList;
import com.podloot.eyemod.lib.gui.widgets.EyeSlider;
import com.podloot.eyemod.lib.gui.widgets.EyeText;
import com.podloot.eyemod.lib.gui.widgets.EyeTextField;
import com.podloot.eyemod.lib.gui.widgets.EyeToggle;
import com.podloot.eyemod.lib.gui.widgets.EyeWidget;

import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;

public class AppChat extends App {
	
	EyeList chat;
	String lastChat = "";

	public AppChat() {
		super(new ResourceLocation(Eye.MODID, "textures/gui/apps/appchat.png"), 0xff96c575, "Eye");		
	}

	@Override
	public boolean load() {
		loadChat();
		
		//Sending
		EyeTextField text = new EyeTextField(getWidth()-26, 20);
		text.setText(new Line("text.eyemod.chat_message"));
		
		add(text, 2, getHeight()-22);
		
		EyeButton send = new EyeButton(20, 20, EyeLib.RIGHT);
		send.setColor(getAppColor(), 0xffFFFFFF);
		send.setAction(() -> {
			device.connect.sendChat(text.getInput(), "");
			text.setInput("");
			this.refresh();
		});
		add(send, getWidth()-22, getHeight()-22);
		text.setAction(send.getAction());
		
		return true;
	}
	
	public void loadChat() {
		if(chat != null) {
			chat.clearList();
			remove(chat);
		}
		ListNBT chats = device.data.getList("chat", Type.STRING);
		chat = new EyeList(getWidth()-4, getHeight()-26, Axis.VERTICAL);
		for(int i = 0; i < chats.size(); i++) {
			chat.add(this.getMessage(chats.getString(i)));
		}
		add(chat, 2, 2);
		chat.setScroll(-999);
	}
	
	public EyeWidget getMessage(String msg) {
		EyeText label = new EyeText(getWidth()-20, new Line(msg).setScale(0.8F));
		label.setBack(0xff333333);
		label.setAlignment(1, 0);
		return label;
	}
	
	@Override
	public void onRefresh() {
		this.loadChat();
	}
	
	@Override
	public void onClearData() {
		device.data.remove("chat");
	}
	
	@Override
	public List<EyeWidget> getSettings(int width) {
		List<EyeWidget> settings = new ArrayList<EyeWidget>();
		
		//Settings
		EyeToggle local = new EyeToggle(width, 12);
		local.setText(new Line("text.eyemod.chat_local"));
		local.setState(device.settings.getBool("chat_local"));

		local.setAction(() -> {
			device.settings.setBool("chat_local", local.getToggle());
		});
		
		EyeSlider distance = new EyeSlider(width, 24, 1, 32);
		distance.setText(new Line("text.eyemod.chat_distance"));
		distance.setState(device.settings.getInt("chat_dis"));
		distance.setAction(() -> {
			device.settings.setInt("chat_dis", distance.getValue());
		});
		distance.setShowValue(() -> distance.getValue()*16);
		
		
		settings.add(local);
		settings.add(distance);
		return settings;
	}
	
	int i = 240;
	
	@Override
	public void tick(int mx, int my) {
		if(i < 0) {
			this.refresh();
			i = 60;
		} else {
			i--;
		}
		super.tick(mx, my);
	}

}
