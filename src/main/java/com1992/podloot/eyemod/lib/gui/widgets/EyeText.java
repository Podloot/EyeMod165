package com.podloot.eyemod.lib.gui.widgets;

import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;
import com.podloot.eyemod.lib.gui.EyeDraw;
import com.podloot.eyemod.lib.gui.EyeLib;
import com.podloot.eyemod.lib.gui.util.Line;

public class EyeText extends EyeWidget {

	List<String> lines;
	int textHeight = 11;

	public EyeText(int width, int height, Line text) {
		super(false, width, height);
		this.setText(text);
	}

	public EyeText(int width, Line text) {
		this(width, 0, text);
	}

	@Override
	public void setText(Line text) {
		this.text = text;
		lines = EyeLib.getLines(text.getText(), (int) ((width-8) / text.getScale()));
		textHeight = (int) (textHeight * text.getScale());
		int h = (int) ((lines.size() * textHeight));
		setHeight(height < h + 6 ? h + 6 : height);
	}

	public void setAlignment(int x, int y) {
		text = text.setAllingment(x, y);
	}

	@Override
	public void draw(PoseStack matrices, int x, int y) {
		super.draw(matrices, x, y);

		int w = (width - 8) / 2;
		int h = (height - 6) / 2;
		int cx = text.getCenteredX();
		int cy = text.getCenteredY();

		int yoff = (int) ((lines.size() * textHeight) / 2 - (textHeight / 2));
		for (int i = 0; i < lines.size(); i++) {
			Line line = new Line(lines.get(i), text);
			EyeDraw.text(matrices, line, (x + 4) - (w * (cx - 1)),
					(int) ((y + 3) - ((h - yoff) * (cy - 1)) + (i * textHeight)));
		}

	}

	public float getTextHeight() {
		return textHeight;
	}

}
