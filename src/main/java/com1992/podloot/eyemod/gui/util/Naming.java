package com.podloot.eyemod.gui.util;

import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;

public class Naming {
	
	public enum Type {
		LIST(ListTag.TAG_LIST),
		INT(ListTag.TAG_INT),
		STRING(ListTag.TAG_STRING),
		BOOLEAN(ListTag.TAG_BYTE),
		COMPOUND(ListTag.TAG_COMPOUND),
		INTARRAY(ListTag.TAG_INT_ARRAY);
		
		public final byte type;
		private Type(byte type) {
			this.type = type;
		}
	}
	
	public enum Dim {
		OVERWORLD(BuiltinDimensionTypes.OVERWORLD),
		NETHER(BuiltinDimensionTypes.NETHER),
		END(BuiltinDimensionTypes.END);
		
		public final ResourceKey<DimensionType> key;
		public final ResourceLocation id;
		private Dim(ResourceKey<DimensionType> key) {
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
