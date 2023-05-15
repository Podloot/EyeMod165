package com.podloot.eyemod.gui.apps.stock;

import java.util.ArrayList;
import java.util.List;

import com.podloot.eyemod.Eye;
import com.podloot.eyemod.EyeCommands;
import com.podloot.eyemod.gui.apps.App;
import com.podloot.eyemod.gui.util.Time;
import com.podloot.eyemod.gui.util.commands.Command;
import com.podloot.eyemod.lib.gui.EyeLib;
import com.podloot.eyemod.lib.gui.util.Line;
import com.podloot.eyemod.lib.gui.widgets.EyeButton;
import com.podloot.eyemod.lib.gui.widgets.EyeList;
import com.podloot.eyemod.lib.gui.widgets.EyeText;
import com.podloot.eyemod.lib.gui.widgets.EyeTextField;
import com.podloot.eyemod.lib.gui.widgets.EyeWidget;

import net.minecraft.util.ResourceLocation;

public class AppConsole extends App {
	
	String lastCmd = "";
	EyeList console;

	public AppConsole() {
		super(new ResourceLocation(Eye.MODID, "textures/gui/apps/appconsole.png"), 0xff333333, "Eye");
	}

	@Override
	public boolean load() {
		console = new EyeList(getWidth()-4, getHeight()-26, Axis.VERTICAL);
		console.add(this.getMessage(Time.getTime() + " - Console"));
		console.add(this.getMessage("Use: help for more info"));
		console.setScroll(-999);
		add(console, 2, 2);
		
		//Sending
		EyeTextField text = new EyeTextField(getWidth()-26, 20);
		text.setSuggest(getCommands());
		text.setText(new Line("text.eyemod.console_command"));
		add(text, 2, getHeight()-22);
		
		EyeButton send = new EyeButton(20, 20, EyeLib.RIGHT);
		send.setColor(getAppColor(), 0xffFFFFFF);
		send.setAction(() -> {
			console.add(this.getMessage("> " + text.getInput()));
			String output = device.command.execute(text.getInput());
			console.add(this.getMessage(output));
			lastCmd = text.getInput();
			text.setInput("");
			console.updateItems();
			console.setScroll(-999);
		});
		add(send, getWidth()-22, getHeight()-22);
		text.setAction(send.getAction());
		text.setOnUp(() -> {
			text.setInput(lastCmd);
		});
		
		return true;
	}
	
	public String[] getCommands() {
		List<String> sug = new ArrayList<String>();
		for(Command c : EyeCommands.getCommands()) {
			sug.add(c.getCommand());
			for(String s : c.getSub()) {
				sug.add(c.getCommand() + " " + s);
			}
		}
		return sug.toArray(new String[] {});
	}
	
	public EyeWidget getMessage(String msg) {
		Line l = new Line(msg).setScale(0.8F);
		if(!msg.startsWith("> ")) {
			l.setStyle(false, true);
			l.setColor(0xff888888);
		}
		EyeText label = new EyeText(getWidth()-20, l);
		label.setBack(0xff333333);
		label.setAlignment(1, 0);
		return label;
	}
	
	@Override
	public void close() {
		super.close();
	}

}
