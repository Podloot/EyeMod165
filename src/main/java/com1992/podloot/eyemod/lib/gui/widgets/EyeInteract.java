package com.podloot.eyemod.lib.gui.widgets;

public abstract class EyeInteract extends EyeWidget {
	
	boolean hovered;
	
	protected Runnable action;
	
	public EyeInteract(int width, int height) {
		super(true, width, height);
	}
	
	public void setAction(Runnable action) {
		this.action = action;
	}
	
	public Runnable getAction() {
		return action;
	}

	@Override
	public void tick(int mx, int my) {
		if(this.isEnabled()) hovered = this.inBounds(mx, my);
		else hovered = false;
		super.tick(mx, my);
	}
	
	public boolean isHovered() {
		return hovered;
	}

}
