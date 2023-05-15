package com.podloot.eyemod.gui.panes;

import com.podloot.eyemod.blocks.entities.RouterEntity;
import com.podloot.eyemod.gui.GuiDevice;
import com.podloot.eyemod.gui.apps.App;
import com.podloot.eyemod.gui.util.Naming.Action;
import com.podloot.eyemod.lib.gui.util.Color;
import com.podloot.eyemod.lib.gui.util.Line;
import com.podloot.eyemod.lib.gui.util.Pos;
import com.podloot.eyemod.lib.gui.widgets.EyeButton;
import com.podloot.eyemod.lib.gui.widgets.EyeLabel;
import com.podloot.eyemod.lib.gui.widgets.EyePlane;
import com.podloot.eyemod.lib.gui.widgets.EyeTextField;
import com.podloot.eyemod.network.PacketHandler;
import com.podloot.eyemod.network.ServerRouter;
import com.podloot.eyemod.network.ServerRouterData;

public class RouterPane extends Pane {

	public RouterPane(App app, int width, int height) {
		super(app, width, height);
		load();
		addAccept(0);
	}
	
	public boolean load() {
		GuiDevice device = app.getDevice();
		boolean connected = device.connect.isConnected();
		Pos router = device.connect.getRouter();
		boolean hasRouter = device.connect.isRouter(router);
		RouterEntity be = device.connect.getRouterData();
		
		if(!connected || be == null || !hasRouter) {
			EyeLabel status = new EyeLabel(getWidth()-4, 14, new Line("text.eyemod.net_noconnection"));
			status.setBack(app.appColor);
			status.setAlignment(1, 0);
			add(status, 2, 2);
			return true;
		}
		
		String owner = be.owner;
		String password = be.password;
		boolean isOwner = device.getUser().getScoreboardName().equals(owner);
		
		EyeLabel status = new EyeLabel(getWidth()-4, 14, new Line("text.eyemod.net_status"));
		status.setBack(app.appColor);
		status.setAlignment(1, 0);
		add(status, 2, 2);
		
		EyePlane background = new EyePlane(getWidth()-4, 66, Color.LIGHTGRAY);
		add(background, 2, 18);
		
		EyeLabel con = new EyeLabel(getWidth()-4, 14, new Line("text.eyemod.net_connection"));
		con.setAlignment(1, 0);
		con.setVariable(connected ? device.connect.getConnection() + "/3" : "NA");
		
		EyeLabel rout = new EyeLabel(getWidth()-4, 14, new Line("text.eyemod.net_router"));
		rout.setAlignment(1, 0);
		rout.setVariable(hasRouter ? router.asString(false) : "NA");
		
		EyeLabel stor = new EyeLabel(getWidth()-4, 14, new Line("text.eyemod.net_storage"));
		stor.setAlignment(1, 0);
		stor.setVariable((be.storage + be.messages.size() + "/" + be.max_storage*2 + "GB"));
		
		EyeLabel own = new EyeLabel(getWidth()-4, 14, new Line("text.eyemod.net_owner"));
		own.setAlignment(1, 0);
		own.setVariable(owner.isEmpty() ? "NA" : owner);
		
		EyeLabel pass = new EyeLabel(getWidth()-4, 14, new Line("text.eyemod.net_password"));
		pass.setAlignment(1, 0);
		if(password.isEmpty()) {
			pass.setVariable("NA");
		} else {
			pass.setVariable(isOwner ? password : "*****");
		}
		
		add(con, 6, 20);
		add(rout, 6, 32);
		add(stor, 6, 44);
		add(own, 6, 56);
		add(pass, 6, 68);
		
		if(owner.equals(device.getOwner())) {
			EyeLabel change = new EyeLabel(getWidth()-4, 16, new Line("text.eyemod.net_change"));
			change.setBack(Color.LIGHTGRAY);
			add(change, 2, getHeight() - 72);
			
			EyeTextField pw = new EyeTextField(getWidth()-48, 16);
			pw.setLimit(16);
			pw.setText(new Line("text.eyemod.settings_password"));
			EyeButton setpw = new EyeButton(42, 16, new Line("text.eyemod.change"));
			setpw.setAction(() -> {
				if(isOwner) {
					PacketHandler.INSTANCE.sendToServer(new ServerRouter(router, Action.SET_PASSWORD, pw.getInput()));
				}
			});		
			add(pw, 2, getHeight()-54);
			add(setpw, getWidth()-44, getHeight()-54);
			
			EyeButton clear = new EyeButton(getWidth()-4, 16, new Line("text.eyemod.net_clear"));
			clear.setColor(Color.RED);
			clear.setAction(() -> {
				if(isOwner) {
					PacketHandler.INSTANCE.sendToServer(new ServerRouterData(router, Action.RESET, "", null));
				}
			});
			add(clear, 2, getHeight()-36);
		}
		
		return true;
	}

}
