package com.podloot.eyemod.lib.gui.widgets;

import java.util.function.Supplier;

import com.podloot.eyemod.lib.gui.util.Line;

public class EyeVariable extends EyeLabel {
	
	Supplier<String> var;
	String form;

	public EyeVariable(int width, int height, Line text) {
		super(width, height, text);
	}
	
	public void setVariable(Supplier<String> var) {
		this.var = var;
		form = text.getText().replace("{v}", "%s");
	}
	
	@Override
	public void tick(int mx, int my) {
		text.setText(String.format(form, var.get()));
		super.tick(mx, my);
	}
}
