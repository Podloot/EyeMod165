package com.podloot.eyemod.lib.gui.widgets;

import java.util.function.Supplier;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.podloot.eyemod.lib.gui.EyeDraw;
import com.podloot.eyemod.lib.gui.EyeLib;
import com.podloot.eyemod.lib.gui.util.Line;
import com.podloot.eyemod.lib.gui.util.Space;

public class EyeSlider extends EyeInteract {
	
	int min, max;
	int slide = 0;
	int size = 12;
	int old_value = 0;
	boolean dragging = false;
	Axis axis = Axis.HORIZONTAL;
	Supplier showValue;
	String form;
	
	Space space;
		
	public EyeSlider(int width, int height, int min, int max) {
		super(width, height);
		this.min = min;
		this.max = max < min ? min : max;
		size = 12;
		space = new Space(0, height/2 - 6, width, 12);
		showValue = () -> {
			return getValue();
		};
	}
	
	public void setAxis(Axis axis) {
		this.axis = axis;
		if(axis == Axis.VERTICAL) {
			space = new Space(width/2 - 6, 0, 12, height);
		} else {
			space = new Space(0, height/2 - 6, width, 12);
		}
	}
	
	public void setSliderSpace(Space space) {
		this.space = space;
	}
	
	public void setSize(int dotsize) {
		this.size = dotsize;
	}
	
	@Override
	public void setText(Line text) {
		this.text = text;
		form = text.getText().replace("{v}", "%s");
		if(axis == Axis.HORIZONTAL) {
			space = new Space(0, height-12, width, 12);
		}
		
	}
	
	public void setState(int state) {
		this.setValue(state);
	}
	
	public void setShowValue(Supplier show) {
		this.showValue = show;
	}
	
	@Override
	public void draw(MatrixStack matrices, int x, int y) {
		super.draw(matrices, x, y);
		
		//Bar
		boolean a = axis == Axis.HORIZONTAL;
		EyeDraw.nine(matrices, EyeLib.PLANE, x + space.x+2, y + space.y +2, space.width-4, space.height-4, secondary);
		
		//Title
		if(text != null) {
			text.setText(String.format(form, showValue.get()));
			this.drawText(matrices, x+4, y+4, width-8, 8);
		}
		
		EyeDraw.nine(matrices, EyeLib.SLIDER, x + space.x + (a ? getLoc(a) : 0), y + space.y + (a ? 0 : getLoc(a)), (a ? size : space.width), (a ? space.height : size), primary);
		EyeDraw.nine(matrices, EyeLib.SLIDER_BORDER, x + space.x + (a ? getLoc(a) : 0), y + space.y + (a ? 0 : getLoc(a)), (a ? size : space.width), (a ? space.height : size), isHovered() ? 0xffFFFFFF : 0xff000000);
		
	}
	
	public int getLoc(boolean a) {
		slide = slide < 0 ? 0 : slide > (a ? space.width : space.height) - size ? (a ? space.width : space.height) - size : slide;
		return slide;
	}
	
	public int getValue() {
		boolean a = axis == Axis.HORIZONTAL;
		float l = getLoc(a);
		float total = a ? space.width - size : space.height - size;
		float range = (max-min) + 1;
		float steps = total / range;
		int f = (int)(l / steps) + min;
		return f < min ? min : f > max ? max : f;
	}
	
	public void setValue(int amount) {
		amount = amount-1;
		boolean a = axis == Axis.HORIZONTAL;
		float total = a ? space.width - size : space.height - size;
		if(amount >= max) {
			slide = (int) total;
			return;
		} else if(amount <= min) {
			slide = 0;
			return;
		}
		float range = (max-min) + 1;
		float steps = total / range;
		slide = (int) (steps * amount + (steps/2F) + min);
	}
	
	@Override
	public void tick(int mx, int my) {
		if(dragging) {
			if(axis == Axis.HORIZONTAL) {
				slide = mx-6 - this.getGlobalX();
			} else {
				slide = my-6 - this.getGlobalY();
			}
		}
		
		super.tick(mx, my);
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		dragging = true;
		old_value = this.getValue();
		return true;
	}
	
	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		dragging = false;
		if(this.getValue() != old_value) {
			old_value = this.getValue();
			if(action != null) action.run();
		}
		return false;
	}
	
	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
		slide += amount;
		return true;
	}
	
	@Override
	public boolean isHovered() {
		return super.isHovered() || dragging;
	}

}
