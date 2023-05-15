package com.podloot.eyemod.gui.apps.op;

import com.podloot.eyemod.Eye;
import com.podloot.eyemod.gui.apps.AppPoint;
import com.podloot.eyemod.gui.elements.DimensionButton;
import com.podloot.eyemod.gui.util.Naming.Type;
import com.podloot.eyemod.lib.gui.panels.EyePanel;
import com.podloot.eyemod.lib.gui.util.Color;
import com.podloot.eyemod.lib.gui.util.Line;
import com.podloot.eyemod.lib.gui.util.Pos;
import com.podloot.eyemod.lib.gui.util.Space;
import com.podloot.eyemod.lib.gui.widgets.EyeButton;
import com.podloot.eyemod.lib.gui.widgets.EyeLabel;
import com.podloot.eyemod.lib.gui.widgets.EyeWidget;
import com.podloot.eyemod.network.PacketHandler;
import com.podloot.eyemod.network.ServerTeleport;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class AppWarp extends AppPoint {
	

	public AppWarp() {
		super(new ResourceLocation(Eye.MODID, "textures/gui/apps/appwarp.png"), 0xff707dd2, "EyeOP");
	}

	@Override
	public boolean load() {
		this.setList(new Space(2, 2, getWidth() - 4, getHeight() - 4), device.data.getList("waypoints", Type.STRING));
		return super.load();
	}

	@Override
	public EyeWidget getPanel(int index, Pos pos) {
		EyePanel pan = new EyePanel(getWidth()-14, 34);
		pan.setBack(Color.DARKGRAY);
		
		
		EyeLabel name = new EyeLabel(getWidth()-14 - 20, 14, new Line(pos.getName()));
		name.setBack(getAppColor());
		pan.add(name, 2, 2);
		
		BlockPos c = pos.getPos();
		EyeLabel coords = new EyeLabel(getWidth()- 14 - 48, 14, new Line(c.getX() + "/" + c.getY() + "/" + c.getZ()));
		coords.setBack(Color.LIGHTGRAY);
		pan.add(coords, 2, 18);
		
		DimensionButton db = new DimensionButton(14, 14, pos.getWorld());
		db.setEnabled(false);
		pan.add(db, getWidth() - 14 - 16, 2);
		
		EyeButton warp = new EyeButton(42, 14, new Line("text.eyemod.warp"));
		warp.setColor(appColor);
		warp.setAction(() -> {
			PacketHandler.INSTANCE.sendToServer(new ServerTeleport(device.getUser().getScoreboardName(), pos.getPos(), pos.getWorld()));
		});
		pan.add(warp, getWidth() - 16 - 42, 18);
		return pan;
	}

}
