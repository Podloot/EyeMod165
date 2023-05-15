package com.podloot.eyemod.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.podloot.eyemod.blocks.entities.RouterEntity;
import com.podloot.eyemod.gui.util.NbtManager;
import com.podloot.eyemod.lib.gui.EyeScreen;
import com.podloot.eyemod.lib.gui.util.Color;
import com.podloot.eyemod.lib.gui.util.Line;
import com.podloot.eyemod.lib.gui.util.Pos;
import com.podloot.eyemod.lib.gui.widgets.EyeButton;
import com.podloot.eyemod.lib.gui.widgets.EyeLabel;
import com.podloot.eyemod.lib.gui.widgets.EyeTextField;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;

public class GuiRouter extends EyeScreen {

	NbtManager data;
	Pos router;
	boolean canOpen;
	
	public GuiRouter(InteractionHand hand, Pos router, boolean canOpen) {
		super("Router", 160, canOpen ? 38 : 56);
		this.data = new NbtManager(hand);
		this.router = router;
		this.canOpen = canOpen;
		load();
	}
	
	public void load() {
		base.setBack(Color.DARKGRAY);
		
		EyeTextField pass = new EyeTextField(base.getWidth()-8, 16);
		pass.setLimit(16);
		pass.setText(new Line("text.eyemod.settings_password"));
		
		EyeLabel enter = new EyeLabel(base.getWidth(), 16, new Line(canOpen ? "text.eyemod.net_public" : "text.eyemod.net_enter"));
		base.add(enter, 0, 2);
		
		if(!canOpen) {	
			base.add(pass, 4, base.getHeight()-38);
		}
		
		EyeButton cancel = new EyeButton(base.getWidth()/2 - 6, 16, new Line("text.eyemod.cancel"));
		cancel.setAction(() -> {
			Minecraft.getInstance().setScreen(null);
		});
		cancel.setColor(Color.RED);
		base.add(cancel, 4, base.getHeight()-20);
		
		EyeButton connect = new EyeButton(base.getWidth()/2 - 6, 16, new Line("text.eyemod.net_connect"));
		connect.setAction(() -> {
			if((canOpen || pass.getInput().equals(this.getRouterPassword())) && router != null) {
				data.setPos("router", router);
				data.setString("net", this.getRouterPassword());
				Minecraft.getInstance().setScreen(null);
				Minecraft.getInstance().player.displayClientMessage(new Line("text.eyemod.block_router_connect").asText(), true);
				Minecraft.getInstance().player.playSound(SoundEvents.ENDER_EYE_DEATH, 1, 1);
			}
		});
		base.add(connect, 82, base.getHeight()-20);
		
	}
	
	public String getRouterPassword() {
		if(router != null) {
			if(getUser().level.getBlockEntity(router.getPos()) instanceof RouterEntity) {
				return ((RouterEntity)getUser().level.getBlockEntity(router.getPos())).password;
			}
		}
		return "|";
	}
	
	public LocalPlayer getUser() {
		return Minecraft.getInstance().player;
	}
	
	public ResourceLocation getWorldID() {
		return getUser().getLevel().dimension().location();
	}

	@Override
	public void paint(PoseStack matrices) {
		
	}

	@Override
	public void update(int mouseX, int mouseY) {
		data.update();
	}

}
