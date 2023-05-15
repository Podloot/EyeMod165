package com.podloot.eyemod.gui.apps.base;

import com.podloot.eyemod.Eye;
import com.podloot.eyemod.blocks.entities.RouterEntity;
import com.podloot.eyemod.gui.apps.AppTextList;
import com.podloot.eyemod.gui.panes.ConfirmPane;
import com.podloot.eyemod.gui.panes.InputPane;
import com.podloot.eyemod.gui.panes.PostPane;
import com.podloot.eyemod.gui.util.Naming.Action;
import com.podloot.eyemod.gui.util.Naming.Type;
import com.podloot.eyemod.lib.gui.EyeLib;
import com.podloot.eyemod.lib.gui.panels.EyeBoxPanel;
import com.podloot.eyemod.lib.gui.panels.EyePanel;
import com.podloot.eyemod.lib.gui.util.Color;
import com.podloot.eyemod.lib.gui.util.Line;
import com.podloot.eyemod.lib.gui.util.Space;
import com.podloot.eyemod.lib.gui.widgets.EyeButton;
import com.podloot.eyemod.lib.gui.widgets.EyeLabel;
import com.podloot.eyemod.lib.gui.widgets.EyeText;
import com.podloot.eyemod.lib.gui.widgets.EyeWidget;
import com.podloot.eyemod.network.PacketHandler;
import com.podloot.eyemod.network.ServerRouterData;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;

public class AppNetwork extends AppTextList {

	public AppNetwork() {
		super(new ResourceLocation(Eye.MODID, "textures/gui/apps/appnet.png"), 0xff5867c0, "Eye");
		setColor(Color.LIGHTGRAY, Color.DARKGRAY);
	}

	@Override
	public boolean load() {
		setColor(0xffAAAAAA, 0xffCCCCCC);
		RouterEntity re = device.connect.getRouterData();
		if (re != null)
			this.setList(new Space(2, 2, getWidth() - 4, getHeight() - 22),
					re.data.getList("posts", Type.COMPOUND.type));

		EyeButton newpost = new EyeButton(getWidth() - 4, 16, new Line("text.eyemod.network_create"));
		newpost.setColor(appColor);
		newpost.setAction(() -> {
			if (device.hasRouterData()) {
				PostPane create = new PostPane(this, getWidth() - 4, getHeight() - 4, new Line("text.eyemod.message"));
				create.setAction(() -> {
					CompoundNBT msg = new CompoundNBT();
					msg.putString("title", create.getTitle());
					msg.putString("sen", device.getOwner());
					msg.putString("msg", create.getMessage());
					PacketHandler.INSTANCE.sendToServer(new ServerRouterData(device.connect.getRouter(), Action.ADD_TO_LIST, "posts", msg));
					this.refresh();

				});
				this.openPane(create);
			}
		});
		add(newpost, 2, getHeight() - 18);

		return super.load();
	}

	@Override
	public EyeWidget getMain(EyeBoxPanel box, int index, CompoundNBT info) {
		EyePanel post = new EyePanel(box.getWidth() - 10, 0);
		EyeText title = new EyeText(post.getWidth() - 4, new Line(info.getString("title")).setStyle(true, false));
		title.setAlignment(1, 0);
		title.setBack(getPrimary());
		EyeText msg = new EyeText(post.getWidth() - 4, new Line(info.getString("msg")));
		msg.setAlignment(1, 0);
		msg.setBack(getPrimary());
		post.setSize(post.getWidth(), 8 + 16 + title.getHeight() + msg.getHeight());

		EyeLabel writer = new EyeLabel(post.getWidth() - 18, 14,
				new Line("By: " + info.getString("sen")).setScale(0.8F).setStyle(false, true));
		writer.setAlignment(1, -1);
		post.add(title, 2, 2);
		post.add(msg, 2, 4 + title.getHeight());
		post.add(writer, 2, post.getHeight() - 22);
		EyeWidget reactions = this.getExpendable(box, index, info);

		if(info.getString("sen").equals(device.getOwner()) || device.connect.getRouterData().owner.equals(device.getOwner())) {
			EyeButton delete = new EyeButton(14, 14, EyeLib.DELETE);
			delete.setColor(Color.DARKGRAY, Color.RED);
			delete.setAction(() -> {
				ConfirmPane pane = new ConfirmPane(this, getWidth() - 4, 42, new Line("text.eyemod.network_delete"), true);
				pane.setAction(() -> {
					PacketHandler.INSTANCE.sendToServer(new ServerRouterData(device.connect.getRouter(), Action.REMOVE, "posts", IntNBT.valueOf(getIndex(info))));
					this.refresh();
				});
				openPane(pane);
			});
			post.add(delete, post.getWidth() - 64, post.getHeight() - 18);
		}

		post.add(this.getFold(box, reactions, new Space(42, 14), new Line("text.eyemod.open"), appColor,
				new Line("text.eyemod.close"), Color.RED), post.getWidth() - 44, post.getHeight() - 18);
		return post;
	}

	@Override
	public EyeWidget getExpendable(EyeBoxPanel box, int index, CompoundNBT info) {
		EyePanel rec = new EyePanel(box.getWidth() - 10, 0);
		ListNBT re = info.getList("re", Type.STRING.type);
		int h = 0;
		for (int i = 0; i < re.size(); i++) {
			EyeText r = new EyeText(rec.getWidth() - 4, new Line(re.getString(i)));
			r.setAlignment(1, 0);
			r.setBack(getSecondary());
			rec.add(r, 2, h);
			h += r.getHeight() + 2;
		}
		rec.setHeight(h + 18);

		EyeButton react = new EyeButton(rec.getWidth() - 4, 14, new Line("text.eyemod.react"));
		react.setColor(Color.DARKGRAY);
		react.setAction(() -> {
			if (device.hasRouterData()) {
				InputPane input = new InputPane(this, getWidth() - 4, 54, new Line("text.eyemod.network_reaction"));
				input.setAction(() -> {
					CompoundNBT reaction = new CompoundNBT();
					reaction.putInt("i", getIndex(info));
					reaction.putString("re", device.getOwner() + ": " + input.getInput());
					PacketHandler.INSTANCE.sendToServer(new ServerRouterData(device.connect.getRouter(), Action.REACT_TO_POST, "posts", reaction));
					this.refresh();
				});
				this.openPane(input);
			}
		});
		rec.add(react, 2, rec.getHeight() - 18);

		return rec;
	}

	public int getIndex(CompoundNBT info) {
		ListNBT psts = device.connect.getRouterData().data.getList("posts", Type.COMPOUND.type);
		for (int i = 0; i < psts.size(); i++) {
			CompoundNBT p = psts.getCompound(i);
			if (p.getString("title").equals(info.getString("title"))
					&& p.getString("msg").equals(info.getString("msg"))) {
				return i;
			}
		}
		return -1;
	}

}
