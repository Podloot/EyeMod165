package com.podloot.eyemod.lib.gui.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import com.podloot.eyemod.lib.gui.EyeDraw;
import com.podloot.eyemod.lib.gui.EyeLib;
import com.podloot.eyemod.lib.gui.util.Line;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;

public abstract class EyeTextInput extends EyeInteract {
	
	int cursor = 0;
	int ticks = 0;
	
	int max_length = 256;
	String limit = "";
	String disallowed = "";
	
	Font client;
	String input = "";
	
	boolean suggest = false;
	String[] suggestions = {};
	int option = 0;

	public EyeTextInput(int width, int height) {
		super(width, height);
		client = Minecraft.getInstance().font;
		this.setColor(0xff222222, 0xffDDDDDD);
	}

	@Override
	public void draw(PoseStack matrices, int x, int y) {
		int cb = isFocussed() || isHovered() ? secondary : 0xff000000;
		EyeDraw.nine(matrices, EyeLib.PLANE, x, y, width, height, primary);
		EyeDraw.nine(matrices, EyeLib.PLANE_BORDER, x, y, width, height, cb);
		
		if(text != null && input.isEmpty()) {
			this.drawText(matrices, x+4, y+4, getWidth()-8, getHeight()-8);
		}
		if(!input.isEmpty()) {
			this.setCursor(cursor);
			if(suggest) {
				String s = this.getSuggestion(option);
				if(s != null) drawSuggest(matrices, x, y, s);
			}
			drawLines(matrices, x, y);
		}
		if(ticks/30 % 2 == 0 && this.isFocussed()) drawCursor(matrices, x, y);
		ticks++;
		if(ticks > 1001) ticks = 0;
	}
	
	public abstract void drawCursor(PoseStack matrices, int x, int y);
	
	public abstract void drawLines(PoseStack matrices, int x, int y);
	
	public abstract void drawSuggest(PoseStack matrices, int x, int y, String s);
	
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if(this.suggest) {
			if(keyCode == 257) {
				String s = this.getSuggestion(option);
				if(s != null) {
					input = s;
					setCursor(input.length());
				}
			} else if(keyCode == 258) {
				option++;
			}
		}
		
		switch(keyCode) {
		case 259:
			removeText(1);
			break;
		case 263:
			setCursor(cursor-1);
			return true;
		case 262:
			setCursor(cursor+1);
			return true;
		}
		return keyPressed(keyCode);
	}
	
	public abstract boolean keyPressed(int keyCode);
	
	public void setCursor(int p) {
		if(p < 0) cursor = 0;
		else if(p > input.length()) cursor = input.length();
		else cursor = p;
	}
	
	public void setDisallowed(String dis) {
		this.disallowed = dis;
	}
	
	public void setAllowed(String lim) {
		this.limit = lim;
	}
	
	public void setLimit(int lim) {
		this.max_length = lim;
	}
	
	@Override
	public boolean charTyped(char chr, int modifiers) {
		if((chr + "").contains("|")) return false;
		insertText(chr + "");
		return true;
	}
	
	public void insertText(String s) {
		if(!valid(s)) return;
		this.setCursor(cursor);
		String before = input.substring(0, cursor);
		String after = input.substring(cursor, input.length());
		input = before + s + after;
		cursor = before.length() + s.length();
	}
	
	public boolean valid(String s) {
		if(input.length() + s.length() > max_length) return false;
		if(!disallowed.isEmpty() && s.matches(disallowed) && !s.equals("|")) return false;
		if(!limit.isEmpty() && !s.matches(limit) && !s.equals("|")) return false;
		return true;
	}
	
	public void removeText(int a) {
		String before = input.substring(0, cursor);
		String after = input.substring(cursor, input.length());
		if(before.endsWith(" | ")) a = 3;
		if(before.length() > a) {
			input = before.substring(0, before.length() - a) + after;
			cursor = before.length() - a;
		} else {
			input = after;
			cursor = 0;
		}
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		setCursor(input.length());
		return super.mouseClicked(mouseX, mouseY, button);
	}
	
	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}
	
	public void setAction(Runnable action) {
		this.action = action;
	}
	
	public void setSuggest(String[] lib) {
		suggestions = lib;
		suggest = true;
		this.setHover(new Line("text.eyemod.hover_suggestion"));
	}
	
	public String getSuggestion(int i) {
		String in = input.toLowerCase();
		in = in.replace("_", " ");
		int c = 0;
		for(String s : suggestions) {
			if(s == null || s.length() <= in.length()) continue;
			String subs = s.toLowerCase();
			if(subs.startsWith(in) || subs.replace("_", " ").startsWith(in)) {
				if(c == i) return s;
				else c++;
			}

		}
		option = 0;
		return null;
	}

}
