package com.podloot.eyemod.lib.gui.util;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class Line {
	
	String text;
	int color = 0xffFFFFFF;
	boolean bold, italic;
	float scale = 1f;
	int centerX = 0, centerY = 0;

	public Line(String key) {
		if(key == null) text = "";
		else text = translate(key);
	}
	
	public Line(String key, Line copy) {
		if(key == null) text = "";
		else text = translate(key);
		this.bold = copy.bold;
		this.italic = copy.italic;
		this.color = copy.color;
		this.scale = copy.scale;
		this.centerX = copy.centerX;
		this.centerY = copy.centerY;
	}
	
	public Line setColor(int color) {
		this.color = color;
		return this;
	}
	
	public Line setStyle(boolean bold, boolean italic) {
		this.bold = bold;
		this.italic = italic;
		return this;
	}
	
	public Line setScale(float scale) {
		this.scale = scale;
		return this;
	}
	
	public Line setAllingment(int centerX, int centerY) {
		this.centerX = centerX;
		this.centerY = centerY;
		return this;
	}

	public String translate(String key) {
		MutableComponent t = Component.translatable(key);
		return t.getString();
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public String getText() {
		return text;
	}
	
	public MutableComponent asText() {
		return Component.literal(text);
	}
	
	public int getColor() {
		return color;
	}
	
	public boolean isBold() {
		return bold;
	}
	
	public boolean isItalic() {
		return italic;
	}

	public float getScale() {
		return scale;
	}

	public int getCenteredX() {
		return centerX;
	}

	public int getCenteredY() {
		return centerY;
	}
	
}
