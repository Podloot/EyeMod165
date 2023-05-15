package com.podloot.eyemod.gui.panes;

import com.podloot.eyemod.gui.apps.App;
import com.podloot.eyemod.lib.gui.util.Color;
import com.podloot.eyemod.lib.gui.util.Line;
import com.podloot.eyemod.lib.gui.widgets.EyeTextArea;
import com.podloot.eyemod.lib.gui.widgets.EyeTextField;

public class PostPane extends Pane {
	
	EyeTextField title;
	EyeTextArea message;

	public PostPane(App app, int width, int height, Line msg, String name, String text) {
		super(app, width, height);
		title = new EyeTextField(width-8, 16);
		title.setInput(name);
		title.setLimit(24);
		title.setText(new Line("text.eyemod.title"));
		add(title, 4, 4);
		
		message = new EyeTextArea(width-8, height - 42);
		message.setInput(text);
		message.setLimit(512);
		message.setText(msg);
		message.setLineLimit(22);
		add(message, 4, 23);
		
		addAccept(0);
		addCancel(1);
	}
	
	public PostPane(App app, int width, int height, Line msg) {
		this(app, width, height, msg, "", "");
	}
	
	public EyeTextField getTitleField() {
		return title;
	}
	
	public EyeTextField getMessageField() {
		return title;
	}
	
	public String getTitle() {
		return title.getInput();
	}
	
	public String getMessage() {
		return message.getInput();
	}

}
