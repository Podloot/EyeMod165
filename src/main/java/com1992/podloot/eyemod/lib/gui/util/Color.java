package com.podloot.eyemod.lib.gui.util;

public class Color {
	
	public static final int WHITE = 0xffFFFFFF;
	public static final int LIGHTGRAY = 0xffCECECE;
	public static final int DARKGRAY = 0xff444444;
	public static final int RED = 0xfffb8787;
	public static final int GREEN = 0xff59bc5c;
	
	int rgb;
	int r, g, b, a = 255;
	
	public Color(int color) {
		this.rgb = color;
		this.setRed((color >> 16) & 0xFF);
		this.setGreen((color >> 8) & 0xFF);
		this.setBlue((color >> 0) & 0xFF);
		
	}
	
	public Color(int r, int g, int b) {
		this.setRed(r);
		this.setGreen(g);
		this.setBlue(b);
		rgb = (255 << 24 | this.r << 16 | this.g << 8 | this.b);
	}
	
	public Color(int r, int g, int b, int a) {
		this.setRed(r);
		this.setGreen(g);
		this.setBlue(b);
		this.setAlpha(a);
		rgb = (a << 24 | this.r << 16 | this.g << 8 | this.b);
	}

	public void setRed(int r) {
		this.r = r < 0 ? 0 : r > 255 ? 255 : r;
	}
	
	public void setGreen(int g) {
		this.g = g < 0 ? 0 : g > 255 ? 255 : g;
	}
	
	public void setBlue(int b) {
		this.b = b < 0 ? 0 : b > 255 ? 255 : b;
	}
	
	public void setAlpha(int a) {
		this.a = a < 0 ? 0 : a > 255 ? 255 : a;
	}
	
	public int getAlpha() {
		return a;
	}

	public int getBlue() {
		return b;
	}

	public int getGreen() {
		return g;
	}

	public int getRed() {
		return r;
	}

	public int getRGB() {
		return rgb;
	}
	
	public int getBGR() {
		return (a << 24 | this.b << 16 | this.g << 8 | this.r);
	}

}
