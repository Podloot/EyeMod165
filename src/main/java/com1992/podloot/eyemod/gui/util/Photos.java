package com.podloot.eyemod.gui.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.platform.NativeImage;
import com.podloot.eyemod.Eye;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;

public class Photos {
	
	public static int[] toArray(NativeImage image, int comp) {
		int nw = image.getWidth() / comp;
		int nh = image.getHeight() / comp;
				
		int[] array = new int[nw*nh + 1];
		array[0] = nw;
		
		for(int i = 0; i < nw; i++) {
			for(int j = 0; j < nh; j++) {
				int id = (i + j*nw) + 1;
				array[id] = image.getPixelRGBA(i*comp, j*comp);
			}
		}
		return array;
	}
	
	public static NativeImage toImage(int[] array) {
		NativeImage img = new NativeImage(array[0], (array.length-1) / array[0], false);
		try {
			for(int i = 0; i < img.getWidth(); i++) {
				for(int j = 0; j < img.getHeight(); j++) {
					img.setPixelRGBA(i, j, array[(i + (j*array[0])) + 1]);
				}
			}
		} catch(Exception e) {
			System.out.println(e);
		}
		return img;
	}
	
	public static List<String> getPhotos() {
		File file = new File(Minecraft.getInstance().gameDirectory, "screenshots/eyemod");
		File[] files = file.listFiles();
		List<String> names = new ArrayList<String>();
		if(!file.exists()) return names;
		for(File f : files) {
			if(f.getName().endsWith(".png")) {
				NativeImage i = readShot(f.getName());
				if(i.getWidth() <= 150*4 && i.getHeight() <= 200*4) {
					names.add(f.getName());
				}
			}
		}
		
		return names;
	}
	
	public static NativeImage readShot(String name) {
		if(!name.contains(".png")) name = name + ".png";
		File file = new File(Minecraft.getInstance().gameDirectory, "screenshots/eyemod/" + name);
		if(!file.exists()) return null;
		 try {
			FileInputStream inputStream = new FileInputStream(file);
			NativeImage img = NativeImage.read(inputStream);
			return img;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static boolean takeShot(String name) {
		NativeImage img = Screenshot.takeScreenshot(Minecraft.getInstance().getMainRenderTarget());
		
		double sf = Minecraft.getInstance().getWindow().getGuiScale();		
		double s = sf;
		
		NativeImage im = new NativeImage((int)(148*s), (int)((184 + 8)*s), false);
		int mw = Minecraft.getInstance().getWindow().getWidth();
		int mh = Minecraft.getInstance().getWindow().getHeight();
		
		double ow = (mw/2) - (im.getWidth()/2);
		double oh = (mh/2) - (im.getHeight()/2);
		
		try {
			for(int i = 0; i < im.getWidth(); i++) {
				for(int j = 0; j < im.getHeight(); j++) {
					int ix = (int)(i + ow);
					int iy = (int)(j + oh);
					if(ix < img.getWidth() && iy < img.getHeight() && ix >= 0 && iy >= 0) {
						im.setPixelRGBA(i, j, img.getPixelRGBA(ix, iy+(int)s));
					}
				}
			}
			img.close();
		} catch(Exception e) {
			System.out.println(e);
			return false;
		}
		return saveShot(name + ".png", im);
	}
	
	public static boolean takeShotLow(String name) {
		NativeImage img = Screenshot.takeScreenshot(Minecraft.getInstance().getMainRenderTarget());
		NativeImage im = new NativeImage(148, 184 + 8, false);
		int mw = Minecraft.getInstance().getWindow().getWidth();
		int mh = Minecraft.getInstance().getWindow().getHeight();
		double sf = Minecraft.getInstance().getWindow().getGuiScale();		
		double s = sf;
		double ow = (mw/2) - ((148/2) * s);
		double oh = (mh/2) - ((192/2) * s);
		
		try {
			for(int i = 0; i < im.getWidth(); i++) {
				for(int j = 0; j < im.getHeight(); j++) {
					int ix = (int)(i*s + ow);
					int iy = (int)(j*s + oh);
					if(ix < img.getWidth() && iy < img.getHeight() && ix >= 0 && iy >= 0) {
						im.setPixelRGBA(i, j, img.getPixelRGBA(ix, iy+(int)s));
					}
				}
			}
			img.close();
		} catch(Exception e) {
			System.out.println(e);
			return false;
		}
		return saveShot(name + ".png", im);
	}
	
	public static boolean saveShot(String fileName, NativeImage nativeImage) {
		File screenshot = new File(Minecraft.getInstance().gameDirectory, "screenshots/");
		screenshot.mkdir();
		
		File file = new File(screenshot, "eyemod/");
		file.mkdir();
		
		File file2 = new File(file, fileName);
		if(file2.exists()) return false;
		Util.backgroundExecutor().execute(() -> {
			try {
				nativeImage.writeToFile(file2);
			} catch (Exception text) {
				Eye.LOGGER.warn("Couldn't save screenshot", (Throwable) text);
			} finally {
				nativeImage.close();
			}
		});
		return true;
	}
	
	public static boolean deleteShot(String filename) {
		if(!filename.contains(".png")) filename = filename + ".png";
		File file = new File(Minecraft.getInstance().gameDirectory, "screenshots/eyemod/" + filename);
		if(file.exists()) {
			return file.delete();
		}
		return false;
	}
	
	public static boolean renameShot(String filename, String newname) {
		if(!filename.contains(".png")) filename = filename + ".png";
		if(!newname.contains(".png")) newname = newname + ".png";
		try {
			File file = new File(Minecraft.getInstance().gameDirectory, "screenshots/eyemod/" + filename);
			File destfile = new File(Minecraft.getInstance().gameDirectory, "screenshots/eyemod/" + newname);
			if(!file.exists()) return false;
			if(destfile.exists()) return false;
			Path source = Paths.get(file.getPath());
			Files.move(source, source.resolveSibling(newname));
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	

}
