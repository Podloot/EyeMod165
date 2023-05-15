package com.podloot.eyemod.gui.apps.base;

import com.podloot.eyemod.Eye;
import com.podloot.eyemod.gui.apps.AppTextList;
import com.podloot.eyemod.gui.panes.PostPane;
import com.podloot.eyemod.gui.util.Naming.Type;
import com.podloot.eyemod.gui.util.Time;
import com.podloot.eyemod.lib.gui.EyeLib;
import com.podloot.eyemod.lib.gui.panels.EyeBoxPanel;
import com.podloot.eyemod.lib.gui.panels.EyePanel;
import com.podloot.eyemod.lib.gui.util.Color;
import com.podloot.eyemod.lib.gui.util.Line;
import com.podloot.eyemod.lib.gui.util.Space;
import com.podloot.eyemod.lib.gui.widgets.EyeButton;
import com.podloot.eyemod.lib.gui.widgets.EyeIcon;
import com.podloot.eyemod.lib.gui.widgets.EyeLabel;
import com.podloot.eyemod.lib.gui.widgets.EyeText;
import com.podloot.eyemod.lib.gui.widgets.EyeWidget;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.ResourceLocation;

public class AppNotes extends AppTextList {

	
	public AppNotes() {
		super(new ResourceLocation(Eye.MODID, "textures/gui/apps/appnotes.png"), 0xffe3e571, "Eye");
	}
	
	@Override
	public boolean load() {
		setList(new Space(2, 2, getWidth()-4, getHeight()-20), device.data.getList("notes", Type.STRING));
		
		open = new EyeButton(42, 14, new Line("text.eyemod.open"));
		open.setColor(appColor);
		close = new EyeButton(42, 14, new Line("text.eyemod.close"));
		close.setColor(Color.RED);
		
		EyeButton add = new EyeButton(getWidth()-4, 14, new Line("text.eyemod.notes_new"));
		add.setColor(getAppColor());
		add.setAction(() -> {
			PostPane pane = new PostPane(this, getWidth()-4, getHeight()-4, new Line("text.eyemod.note"));
			pane.setAction(() -> {
				String n = pane.getTitle() + "~" + Time.getTime() + "~" + pane.getMessage();
				device.data.addToList("notes", StringNBT.valueOf(n));
				this.refresh();
			});
			this.openPane(pane);
		});
		add(add, 2, getHeight() - 16);
		return super.load();
	}
	
	@Override
	public EyeWidget getMain(EyeBoxPanel box, int index, CompoundNBT info) {
		EyePanel pan = new EyePanel(getWidth()-14, 36);

		
		EyeIcon icon = new EyeIcon(32, 32, this.appIcon);
		pan.add(icon, 2, 2);
		
		EyeLabel n = new EyeLabel(getWidth()-14 - 38 - 16, 14, new Line(info.getString("0")));
		EyeLabel t = new EyeLabel(getWidth()-14 - 82, 14, new Line(info.getString("1")));
		n.setBack(Color.LIGHTGRAY);
		t.setBack(Color.LIGHTGRAY);
		pan.add(n, 34, 3);
		pan.add(t, 34, 19);
		
		EyeButton delete = new EyeButton(14, 14, EyeLib.DELETE);
		delete.setColor(Color.DARKGRAY, Color.RED);
		delete.setAction(() -> {
			device.data.removeFromList("notes", index);
			this.refresh();
		});
		pan.add(delete, getWidth()-14 - 18, 3);
		
		EyeWidget exp = getExpendable(box, index, info);
		pan.add(getFold(box, exp, new Space(42, 14), new Line("text.eyemod.read"), appColor, new Line("text.eyemod.close"), Color.RED), pan.getWidth() - 46, pan.getHeight()-17);
		return pan;
	}


	@Override
	public EyeWidget getExpendable(EyeBoxPanel box, int index, CompoundNBT info) {
		EyePanel back = new EyePanel(box.getWidth()-10, 16);
		EyeText msg = new EyeText(back.getWidth()-8, new Line(info.getString("2")));
		back.setHeight(msg.getHeight()+4 + 18);
		msg.setBack(Color.LIGHTGRAY);
		msg.setAlignment(1, 1);
		back.add(msg, 4, 2);
		
		EyeButton edit = new EyeButton(42, 14, new Line("text.eyemod.edit"));
		edit.setColor(appColor);
		edit.setAction(() -> {
			PostPane pane = new PostPane(this, getWidth()-4, getHeight()-4, new Line("text.eyemod.note"), info.getString("0"), info.getString("2"));
			pane.setAction(() -> {
				String n = pane.getTitle() + "~" + Time.getTime() + "~" + pane.getMessage();
				device.data.addToList("notes", index, StringNBT.valueOf(n));
				this.refresh();
			});
			this.openPane(pane);
		});
		back.add(edit, back.getWidth()-46, back.getHeight()-18);
		
		return back;
	}
	
	@Override
	public void onClearData() {
		device.data.remove("notes");
	}


	

}
