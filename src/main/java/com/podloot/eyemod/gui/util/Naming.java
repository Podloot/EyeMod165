package com.podloot.eyemod.gui.util;

import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.util.Constants.NBT;

public class Naming {
	
	public enum Type {
		LIST(NBT.TAG_LIST),
		INT(NBT.TAG_INT),
		STRING(NBT.TAG_STRING),
		BOOLEAN(NBT.TAG_BYTE),
		COMPOUND(NBT.TAG_COMPOUND),
		INTARRAY(NBT.TAG_INT_ARRAY);
		
		public final int type;
		private Type(int tagByte) {
			this.type = tagByte;
		}
	}
	
	public enum Dim {
		OVERWORLD(DimensionType.OVERWORLD_LOCATION),
		NETHER(DimensionType.NETHER_LOCATION),
		END(DimensionType.END_LOCATION);
		
		public final RegistryKey<DimensionType> key;
		public final ResourceLocation id;
		private Dim(RegistryKey<DimensionType> key) {
			this.id = key.location();
			this.key = key;
		}
	}
	
	public enum Msg {
		MESSAGE(0);
		
		public final int type;
		private Msg(int type) {
			this.type = type;
		}
	}
	
	public enum Action {
		SET(0),
		ADD_TO_LIST(1),
		REMOVE(2),
		REMOVE_FROM_LIST(3),
		RESET(4),
		REACT_TO_POST(5),
		
		SET_OWNER(10),
		SET_PASSWORD(11);
		
		public final int action;
		private Action(int action) {
			this.action = action;
		}
	}

}
