package com.podloot.eyemod.gui.apps.base;

import java.util.Set;

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
import com.podloot.eyemod.lib.gui.widgets.EyeText;
import com.podloot.eyemod.lib.gui.widgets.EyeTextField;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;

public class AppMessages extends App {
	
	int w;
	
	EyePanel chat;
	int chatSize = 0;
	String currentUser;
	
	String lastMessage;

	public AppMessages() {
		super(new ResourceLocation(Eye.MODID, "textures/gui/apps/appmessages.png"), 0xff7dba52, "Eye");
		w = getWidth()-14;
	}

	@Override
	public boolean load() {
		this.openUsers(this.getUsers());
		return true;
	}
	
	public void openUsers(Set<String> users) {
		chat = null;
		currentUser = null;
		chatSize = 0;
		
		EyeList chats = new EyeList(getWidth()-4, getHeight()-40, Axis.VERTICAL);
		add(chats, 2, 4);
		
		for(String u : users) {
			chats.add(this.getUserPanel(chats, u));
		}
		
		
		PlayerPreset user = new PlayerPreset(this, getWidth()-4);
		user.setButton(new Line("text.eyemod.open"), getAppColor(), () -> {
			if(!user.getInput().isEmpty() && user.getInput() != "") {
				if(device.hasData()) {
					this.clear();
					this.openChat(user.getInput());
				}
			}
		});
		add(user, 2, getHeight()-34);
	}
	
	public EyePanel getUserPanel(EyeList chats, String user) {
		EyePanel panel = new EyePanel(getWidth()-14, 34);
		panel.setBack(Color.DARKGRAY);
		
		EyePlayer player = new EyePlayer(14, 14, user);
		EyeLabel name = new EyeLabel(getWidth()-14-40, 14, new Line(user));
		name.setBack(getAppColor());
		panel.add(player, 4, 2);
		panel.add(name, 20, 2);
		
		EyeButton delete = new EyeButton(14, 14, EyeLib.DELETE);
		delete.setColor(Color.DARKGRAY, Color.RED);
		delete.setAction(() -> {
			this.deleteChat(user);
			chats.remove(panel);
			this.refresh();
		});
		panel.add(delete, getWidth()-14-18, 2);
		
		EyeButton open = new EyeButton(62, 14);
		open.setText(new Line("text.eyemod.open"));
		open.setColor(getAppColor());
		open.setAction(() -> {
			if(!user.isEmpty() && user != "") {
				this.clear();
				this.openChat(user);
			}
		});
		panel.add(open, getWidth()-14 - 66, 18);
		
		return panel;
	}
	
	public void openChat(String user) {
		currentUser = user;
		
		ListTag messages = this.getMessages().getList(user, Type.STRING.type);
		chatSize = messages.size();
		
		//Top
		EyeButton back = new EyeButton(14, 14, EyeLib.LEFT);
		back.setColor(0xff444444);
		back.setAction(() -> {
			clear();
			openUsers(this.getUsers());
		});
		
		EyePlayer player = new EyePlayer(14, 14, user);
		EyeLabel name = new EyeLabel(getWidth()-36, 14, new Line(user));
		name.setBack(getAppColor());
		add(player, 18, 2);
		add(name, 34, 2);
		add(back, 2, 2);
		
		chat = this.getChat(messages, getWidth()-4, getHeight()-42);
		add(chat, 2, 18);
		
		EyeTextField text = new EyeTextField(getWidth()-26, 20);
		text.setText(new Line("text.eyemod.chat_message"));
		add(text, 2, getHeight()-22);
		
		EyeButton send = new EyeButton(20, 20, EyeLib.RIGHT);
		send.setColor(appColor, 0xffFFFFFF);
		send.setAction(() -> {
			if(!text.getInput().isEmpty()) {
				device.connect.sendMessage(user, text.getInput());
				text.setInput("");
			}
		});
		add(send, getWidth()-22, getHeight()-22);
		text.setAction(send.getAction());
	}
	
	public EyePanel getChat(ListTag messages, int w, int h) {
		EyePanel chat = new EyePanel(w, h);
		EyeList chats = new EyeList(chat.getWidth(), chat.getHeight(), Axis.VERTICAL);
		chatSize = messages.size();
		
		for(Tag n : messages) {
			String msg = ((StringTag)n).getAsString();
			chats.add(getMessage(msg));
			this.lastMessage = msg;
		}
		
		chat.add(chats, 0, 0);
		chats.setScroll(-999);
		
		return chat;
	}
	
	public void refreshChat(ListTag messages) {
		if(chat != null) remove(chat);
		chat = this.getChat(messages, getWidth()-4, getHeight()-42);
		add(chat, 2, 18);
	}
	
	public void deleteChat(String user) {
		CompoundTag msgs = this.getMessages();
		msgs.remove(user);
		device.data.setCompoundTag("messages", msgs);
	}
	
	public EyePanel getMessage(String msg) {
		EyePanel space = new EyePanel(getWidth()-14, 16);
		boolean toself = msg.startsWith("|");
		if(toself) msg = msg.substring(1);
		
		EyeText plane = new EyeText(getWidth()-14 - 30, new Line(msg));
		plane.setAlignment(1, 0);
		plane.setBack(toself ? Color.DARKGRAY : appColor);
		
		space.setHeight(plane.getHeight());
		space.add(plane, toself ? 30 : 0, 0);
		
		return space;
	}
	
	public Set<String> getUsers() {
		Set<String> users = getMessages().getAllKeys();
		return users;
	}
	
	public CompoundTag getMessages() {
		return device.data.getNbt().getCompound("messages");
	}
	
	@Override
	public void tick(int mx, int my) {
		if(chat != null && currentUser != null) {
			ListTag m = this.getMessages().getList(currentUser, Type.STRING.type);
			if(m.size() > 0 && !m.getString(m.size()-1).equals(lastMessage)) {
				this.refreshChat(m);
			}
		}
		super.tick(mx, my);
	}
	
	@Override
	public void onClearData() {
		device.data.remove("messages");
	}

}
