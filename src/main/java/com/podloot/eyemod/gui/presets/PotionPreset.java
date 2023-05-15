package com.podloot.eyemod.gui.presets;

import java.util.ArrayList;
import java.util.List;

import com.podloot.eyemod.gui.apps.App;
import com.podloot.eyemod.gui.panes.ListPane;
import com.podloot.eyemod.lib.gui.panels.EyeListPanel;
import com.podloot.eyemod.lib.gui.util.Color;
import com.podloot.eyemod.lib.gui.util.Line;
import com.podloot.eyemod.lib.gui.widgets.EyeButton;
import com.podloot.eyemod.lib.gui.widgets.EyeLabel;

import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

public class PotionPreset extends PresetInput {
	
	Effect[] effects;
	
	public PotionPreset(App app, int width, Effect[] effects) {
		super(app, width, 32, new Line("text.eyemod.effect"));	
		this.effects = effects;
		name.setAllowed("[a-zA-Z_ ]+");
		name.setSuggest(getNames(effects));
		EyeButton p = this.getListButton(new Line("text.eyemod.positive"), Color.GREEN, getEffects(EffectType.BENEFICIAL));
		add(p, 0, 18);
		
		EyeButton e = this.getListButton(new Line("text.eyemod.neutral"), app.appColor, getEffects(EffectType.NEUTRAL));
		add(e, width/2 - 24, 18);
		
		EyeButton n = this.getListButton(new Line("text.eyemod.negative"), Color.RED, getEffects(EffectType.HARMFUL));
		add(n, getWidth() - 48, 18);
	}
	
	public int getEffect() {
		String in = this.getInput();
		for(Effect s : effects) {
			if(s.getDisplayName().getString().equals(in)) {
				return Effect.getId(s);
			}
		}
		return 0;
	}
	
	public Effect[] getEffects(EffectType cat) {
		List<Effect> se = new ArrayList<Effect>();
		for(Effect s : effects) {
			if(s.getCategory() == cat) {
				se.add(s);
			}
		}
		return se.toArray(new Effect[] {});
	}
	
	public String[] getNames(Effect[] lib) {
		String[] names = new String[lib.length];
		for(int i = 0; i < lib.length; i++) {
			names[i] = lib[i].getDisplayName().getString();
		}
		return names;
	}
	
	public EyeButton getListButton(Line name, int color, Effect[] lib) {
		EyeButton button = new EyeButton(48, 14, name);
		button.setColor(color);
		button.setAction(() -> {
			ListPane list = new ListPane(app, app.getWidth()-4, app.getHeight()-4);
			for(int i = 0; i < lib.length; i++) {
				EyeListPanel pan = new EyeListPanel(list.getList(), 14);
				EyeLabel lbl = new EyeLabel(pan.getWidth(), pan.getHeight(), new Line(lib[i].getDisplayName().getString()));
				pan.setData(lib[i].getDisplayName().getString());
				lbl.setBack(color);
				pan.add(lbl, 0, 0);
				list.getList().add(pan);
			}
			list.setAction(() -> {
				if(list.getSelected() != null) this.name.setInput(list.getSelected().getData() + "");
			});
			app.openPane(list);
		});
		return button;
	}
	
	public void combine() {
		
	}


}
