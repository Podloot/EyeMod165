package com.podloot.eyemod.gui.apps;

import com.podloot.eyemod.gui.util.Naming.Type;
import com.podloot.eyemod.lib.gui.panels.EyeBoxPanel;
import com.podloot.eyemod.lib.gui.util.Line;
import com.podloot.eyemod.lib.gui.util.Space;
import com.podloot.eyemod.lib.gui.widgets.EyeButton;
import com.podloot.eyemod.lib.gui.widgets.EyeList;
import com.podloot.eyemod.lib.gui.widgets.EyeWidget;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;

public abstract class AppTextList extends App {

	EyeList list;
	ListNBT text;
	Space space;

	public EyeButton open;
	public EyeButton close;

	public AppTextList(ResourceLocation icon, int color, String creator) {
		super(icon, color, creator);
	}

	public void setList(Space space, ListNBT text) {
		this.space = space;
		this.text = text;
	}

	public boolean load() {
		loadList();
		return true;
	}

	public void loadList() {
		if (list != null) {
			list.clearList();
			remove(list);
		}
		list = new EyeList(space.width, space.height, Axis.VERTICAL);
		for (int i = text.size()-1; i >= 0; i--) {
			if(text.getElementType() == Type.COMPOUND.type) {
				list.add(getPanel(i, text.getCompound(i)));
			} else {
				list.add(getPanel(i, asNbt(text.getString(i))));
			}
			
		}
		add(list, space.x, space.y);
	}
	
	public CompoundNBT asNbt(String info) {
		CompoundNBT in = new CompoundNBT();
		String[] infos = info.split("~");
		for(int i = 0; i < infos.length; i++) {
			in.putString(i + "", infos[i]);
		}
		return in;
	}

	public EyeWidget getPanel(int index, CompoundNBT info) {
		EyeBoxPanel box = new EyeBoxPanel(list.getWidth(), 16, Axis.VERTICAL, 0);
		box.setBack(appColor);
		box.add(-1, this.getMain(box, index, info));
		return box;
	};
	
	public EyeButton getFold(EyeBoxPanel box, EyeWidget exp, Space space, Line open, int color_open, Line close, int color_close) {
		EyeButton fold = new EyeButton(space.width, space.height, open);
		fold.setColor(color_open);
		
		fold.setAction(() -> {
			if(fold.getText().equals(open)) {
				box.add(-1, exp);
				fold.setText(close);
				fold.setColor(color_close);
			} else {
				box.remove(exp);
				fold.setText(open);
				fold.setColor(color_open);
			}
			box.setItems();
			list.updateItems();
		});
		return fold;
	}

	public abstract EyeWidget getMain(EyeBoxPanel box, int index, CompoundNBT info);

	public abstract EyeWidget getExpendable(EyeBoxPanel box, int index, CompoundNBT info);

}
