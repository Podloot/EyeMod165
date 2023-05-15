package com.podloot.eyemod.lib.gui.widgets;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;
import com.podloot.eyemod.lib.gui.EyeDraw;
import com.podloot.eyemod.lib.gui.EyeLib;
import com.podloot.eyemod.lib.gui.util.Line;

public class EyeTextArea extends EyeTextInput {
	
	List<String> lines = new ArrayList<String>();
	List<String> subLines = new ArrayList<String>();
	int cursorLine = 0;
	int startLine = 0;
	int totalLines = 1;
	int maxLines = 24;

	public EyeTextArea(int width, int height) {
		super(width, height);
		totalLines = (int)((float)(height-8) / 9F);
	}
	
	@Override
	public void setText(Line text) {
		this.text = text.setAllingment(1, 1).setColor(0xff777777);
	}

	@Override
	public void drawCursor(PoseStack matrices, int x, int y) {
		int cl = cursorLine - startLine;
		if(cl >= 0 && cl < lines.size()) {
			String line = lines.get(cursorLine);
			int i = (cursor - this.getSubLength());
			i = i < 0 ? 0 : i > line.length() ? line.length() : i;
			int w = client.width(line.substring(0, i));
			EyeDraw.text(matrices, "|", x+4+w, y+4 + (cl*9), 0xffFFFFFF);
		} else {
			EyeDraw.text(matrices, "|", x+4, y+4, 0xffFFFFFF);
		}
	}

	@Override
	public void drawLines(PoseStack matrices, int x, int y) {
		lines = EyeLib.getLines(input, getWidth()-8);
		cursorLine = this.getCursorLine();
		for(int i = 0; i < totalLines; i++) {
			int si = i;
			if(cursorLine >= totalLines) si = i + (cursorLine - totalLines)+1;
			if(i == 0) startLine = si;
			if(si < lines.size() && si >= 0) {
				EyeDraw.text(matrices, lines.get(si), x+4, y+4 + (i*9), 0xffFFFFFF);
			}
		}
	}
	
	public void drawSuggest(PoseStack matrices, int x, int y, String s) {
		
	}
	
	public int getCursorLine() {
		if(cursor > input.length()) return lines.size()-1;
		if(cursor <= 0) return 0;
		String subInput = input.substring(0, cursor);
		subLines = EyeLib.getLines(subInput, getWidth()-8);
		return subLines.size()-1;
	}
	
	public int getSubLength() {
		if(cursor <= 0) return 0;
		if(cursor > input.length()) return input.length();
		String subInput = input.substring(0, cursor);
		int t = 0;
		for(int i = 0; i < subInput.length(); i++) {
			if(subInput.charAt(i) == '|') t++;
		}

		for(int i = 0; i < subLines.size()-1; i++) {
			t += subLines.get(i).length();
		}
		
		return t;
	}

	@Override
	public boolean keyPressed(int keyCode) {
		switch(keyCode) {
		case 257:
			this.insertText("|");
			break;
		case 264:
			moveCursor(1);
			break;
		case 265:
			moveCursor(-1);
			break;
		}
		return false;
	}
	
	@Override
	public boolean valid(String s) {
		if(EyeLib.getLines(input + s, getWidth()-8).size() > maxLines) return false;
		return super.valid(s);
	}
	
	public void moveCursor(int move) {
		int subLength = this.getSubLength();
		int curLength = cursor - subLength;
		int currentLine = (cursorLine >= 0 && cursorLine < lines.size()) ? lines.get(cursorLine).length() : 0;
		if(move < 0) {
			setCursor(cursor - curLength-1);
		} else if(move > 0) {
			int nextLine = (cursorLine+1 >= 0 && cursorLine+1 < lines.size()) ? lines.get(cursorLine+1).length() : 0;
			setCursor(cursor + nextLine + (currentLine - curLength)+1);
		}
		
	}
	
	public void setLineLimit(int limit) {
		maxLines = limit >= 24 ? 24 : limit < 0 ? 0 : limit;
	}
	
	

}
