package com.podloot.eyemod.lib.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.podloot.eyemod.lib.gui.panels.EyePanel;
import com.podloot.eyemod.lib.gui.util.Line;
import com.podloot.eyemod.lib.gui.widgets.EyeWidget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

public abstract class EyeScreen extends Screen {
	
	public EyePanel base;
	int hover_wait = 40;
	int hover_time = hover_wait;
	int last_x, last_y;
	
	public EyeScreen(String title, int width, int height) {
		super(new Line(title).asText());
		base = new EyePanel(width, height);
		setBase();
	}
	
	public abstract void paint(PoseStack matrices);
	public abstract void update(int mouseX, int mouseY);
	
	public void setBase() {
		if(base == null) return;
		int w = Minecraft.getInstance().getWindow().getGuiScaledWidth();
		int h = Minecraft.getInstance().getWindow().getGuiScaledHeight();
		base.setPos((w - base.getWidth())/2, (h - base.getHeight())/2);
	}
	
	@Override
	public void resize(Minecraft client, int width, int height) {
		setBase();
		super.resize(client, width, height);
	}
	
	@Override
	public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
		super.render(matrices, mouseX, mouseY, delta);
		this.paint(matrices);
		if(base != null) {
			base.tick(mouseX, mouseY);
			base.draw(matrices, base.getX(), base.getY());
			if(hover_time < 0) base.drawHover(matrices, mouseX, mouseY);
		}
		this.update(mouseX, mouseY);
		if(hover_time >= -1) hover_time--;
	}
	
	public EyeWidget getFocussed() {
		return base.getFocussed();
	}
	
	@Override
	public boolean isPauseScreen() {
		return false;
	}
	
	@Override
	public void onClose() {
		base.close();
		super.onClose();
	}
	
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if(getFocussed() != null) getFocussed().keyPressed(keyCode, scanCode, modifiers);
		return super.keyPressed(keyCode, scanCode, modifiers);
	}
	
	@Override
	public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
		if(getFocussed() != null) getFocussed().keyReleased(keyCode, scanCode, modifiers);
		return super.keyReleased(keyCode, scanCode, modifiers);
	}
	
	@Override
	public boolean charTyped(char chr, int modifiers) {
		if(getFocussed() != null) getFocussed().charTyped(chr, modifiers);
		return super.charTyped(chr, modifiers);
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		return base.mouseClicked(mouseX, mouseY, button);
	}
	
	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		if(getFocussed() != null) return getFocussed().mouseReleased(mouseX, mouseY, button);
		return super.mouseReleased(mouseX, mouseY, button);
	}
	
	@Override
	public void mouseMoved(double mouseX, double mouseY) {
		hover_time = hover_wait;
		base.mouseMoved(mouseX, mouseY);
		super.mouseMoved(mouseX, mouseY);
	}
	
	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
		if(getFocussed() != null) return getFocussed().mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
		return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
	}
	
	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
		return base.mouseScrolled(mouseX, mouseY, amount);
	}
	
}
