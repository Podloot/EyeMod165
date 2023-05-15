package com.podloot.eyemod.gui.apps.base;

import java.util.HashMap;

import com.podloot.eyemod.Eye;
import com.podloot.eyemod.gui.apps.App;
import com.podloot.eyemod.gui.util.Naming.Dim;
import com.podloot.eyemod.lib.gui.EyeLib;
import com.podloot.eyemod.lib.gui.panels.EyePanel;
import com.podloot.eyemod.lib.gui.util.Color;
import com.podloot.eyemod.lib.gui.util.Image;
import com.podloot.eyemod.lib.gui.util.Line;
import com.podloot.eyemod.lib.gui.widgets.EyeIcon;
import com.podloot.eyemod.lib.gui.widgets.EyeLabel;
import com.podloot.eyemod.lib.gui.widgets.EyeVariable;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;

public class AppWeather extends App {

	HashMap<String, Image> weather = new HashMap<String, Image>();

	public AppWeather() {
		super(new ResourceLocation(Eye.MODID, "textures/gui/apps/appweather.png"), 0xffadc3e9, "Eye");
		weather.put("text.eyemod.weather_sunny", EyeLib.SUNNY);
		weather.put("text.eyemod.weather_raining", EyeLib.RAIN);
		weather.put("text.eyemod.weather_snowing", EyeLib.SNOW);
		weather.put("text.eyemod.weather_thunder", EyeLib.THUNDER);
		
		
	}

	@Override
	public boolean load() {
		add(getWeatherPanel(), 2, 2);
		add(getRainPanel(), 2, 36);
		add(getBiomePanel(), 2, 70);
		add(getLocationPanel(), 2, 104);
		
		return true;
	}

	public EyePanel getWeatherPanel() {
		EyePanel weath = new EyePanel(getWidth()-4, 32);
		weath.setBack(appColor);

		String wn = device.getWeather();
		if (weather.containsKey(wn)) {
			EyeIcon w = new EyeIcon(24, 24, weather.get(wn));
			weath.add(w, 4, 4);
		}
		EyeLabel wl = new EyeLabel(42, 16, new Line(wn).setStyle(true, false));
		wl.setAlignment(1, 1);
		weath.add(wl, 26, 5);

		EyeLabel temp = new EyeLabel(42, 16, new Line("text.eyemod.weather_degrees").setStyle(false, true));
		temp.setAlignment(1, 1);
		temp.setVariable(device.getTemp() + "");
		weath.add(temp, 26, 15);

		return weath;
	}
	
	public EyePanel getRainPanel() {
		EyePanel weath = new EyePanel(getWidth()-4, 32);
		weath.setBack(appColor);
		EyeIcon r = new EyeIcon(24, 24, EyeLib.RAIN);
		weath.add(r, 4, 4);
		
		EyeVariable wl = new EyeVariable(42, 16, new Line("text.eyemod.weather_humidity"));
		wl.setVariable(() -> getHumidity());
		wl.setAlignment(1, 1);
		weath.add(wl, 26, 5);
		
		EyeLabel rc = new EyeLabel(42, 16, new Line("text.eyemod.weather_rain"));
		rc.setVariable(getRainChance());
		rc.setAlignment(1, 1);
		weath.add(rc, 26, 15);
		
		return weath;
	}
	
	public EyePanel getBiomePanel() {
		BlockPos bp = device.getUser().blockPosition();
		String name = device.getUser().clientLevel.getBiome(bp).getRegistryName().getPath();
		Biome b = getBiomeKey();
		
		EyePanel biome = new EyePanel(getWidth()-4, 32);
		biome.setBack(0xff91d264);
		
		EyeIcon base = new EyeIcon(24, 24, EyeLib.BIOME);
		biome.add(base, 4, 4);
		
		EyeIcon fog = new EyeIcon(24, 24, EyeLib.BIOME_FOG);
		Color c = new Color(b.getFogColor());
		fog.setColor(0, new Color(c.getRed(), c.getGreen(), c.getBlue()).getRGB());
		biome.add(fog, 4, 4);
		
		EyeIcon water = new EyeIcon(24, 24, EyeLib.BIOME_WATER);
		water.setColor(0, getWaterColor(b));
		biome.add(water, 4, 4);
		
		EyeIcon grass = new EyeIcon(24, 24, EyeLib.BIOME_GRASS);
		grass.setColor(0, this.getGrassColor(b));
		biome.add(grass, 4, 4);
		
		EyeLabel rc = new EyeLabel(42, 16, new Line(name));
		rc.setAlignment(1, 1);
		biome.add(rc, 26, 15);
		
		EyeLabel title = new EyeLabel(42, 16, new Line("text.eyemod.biome").setStyle(false, true));
		title.setAlignment(1, 1);
		biome.add(title, 26, 5);
		
		return biome;
	}
	
	public EyePanel getLocationPanel() {
		EyePanel loc = new EyePanel(getWidth()-4, 32);
		loc.setBack(Color.LIGHTGRAY);
		
		EyeIcon r = new EyeIcon(24, 24, EyeLib.MAP);
		loc.add(r, 4, 4);
		
		BlockPos bp = device.getUser().blockPosition();
		
		EyeLabel pos = new EyeLabel(42, 16, new Line("X: " + bp.getX() + " Y: " + bp.getY() + " Z: " + bp.getZ()));
		pos.setAlignment(1, 1);
		loc.add(pos, 26, 5);
		
		EyeLabel world = new EyeLabel(42, 16, new Line(device.getWorldID().getPath()).setStyle(false, true));
		world.setAlignment(1, 1);
		loc.add(world, 26, 15);
		
		return loc;
	}
	
	public String getRainChance() {
		float t = getBiomeKey().getDownfall();
		return (int)(t*100) + "%";
	}
	
	public String getHumidity() {
		float t = device.getUser().clientLevel.rainLevel;
		return (int)(t*100) + "%";
	}
	
	private Biome getBiomeKey() {
		BlockPos bp = device.getUser().blockPosition();
		return device.getUser().clientLevel.getBiome(bp);
	}
	
	public int getWaterColor(Biome b) {
		if(device.getWorldID() == Dim.END.id) {
			return 0xff222222;
		} else if(device.getWorldID() == Dim.NETHER.id) {
			return 0xffdf7126;
		} else {
			Color c = new Color(b.getWaterColor());
			return new Color(c.getRed(), c.getGreen(), c.getBlue()).getRGB();
		}
	}
	
	public int getGrassColor(Biome b) {
		if(device.getWorldID().equals(Dim.OVERWORLD.id)) {
			Color c = new Color(b.getFoliageColor());
			return new Color(c.getRed(), c.getGreen(), c.getBlue()).getRGB();
		} else if(device.getWorldID().equals(Dim.END.id)) {
			return 0xffede99a;
		} else {
			return 0xff983d3d;
		}
	}
	
	
	

}
