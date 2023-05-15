package com.podloot.eyemod.gui.util.messages;

import java.util.HashMap;

public class EyeMsgHandler {
	
	public static HashMap<Integer, EyeMsg> messages = new HashMap<Integer, EyeMsg>();
	
	public static void init() {
		messages.put(0, new MessageMsg());
	}

}
