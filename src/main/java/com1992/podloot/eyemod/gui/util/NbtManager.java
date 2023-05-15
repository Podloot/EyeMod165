package com.podloot.eyemod.gui.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.podloot.eyemod.Eye;
import com.podloot.eyemod.gui.util.Naming.Type;
import com.podloot.eyemod.items.ItemDevice;
import com.podloot.eyemod.lib.gui.util.Pos;
import com.podloot.eyemod.network.PacketHandler;
import com.podloot.eyemod.network.ServerNbtReplace;
import com.podloot.eyemod.network.ServerNbtSet;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public class NbtManager {
	
	private InteractionHand hand = InteractionHand.MAIN_HAND;
	
	private int saving = -1;
	
	/** the max storage, can depend on device itself */
	public int max_storage = 256;
	
	/**
	 * Manages the nbt of the item the player used to open the GUI.
	 * Try to call updateNbt only once per cycle. When setting multiple tags, manually edit the nbt by calling getTag() and updateNbt() at the end
	 * @param hand the hand in with GuiDevice is opened
	 */
	public NbtManager(InteractionHand hand) {
		this.hand = hand;
		this.max_storage = this.getInt("storage");
	}
	
	public boolean has(String key) {
		return getNbt().contains(key);
	}
	
	public void setCompoundTag(String key, CompoundTag value) {
		sendNbt(key, value);
	}
	
	public CompoundTag getCompoundTag(String key) {
		return getNbt().getCompound(key);
	}
	
	public void setString(String key, String value) {
		sendNbt(key, StringTag.valueOf(value));
	}
	
	public String getString(String key) {
		return getNbt().getString(key);
	}
	
	public void setInt(String key, int value) {
		sendNbt(key, IntTag.valueOf(value));
	}
	
	public void addInt(String key, int add) {
		sendNbt(key, IntTag.valueOf(getInt(key)+add));
	}
	
	public int getInt(String key) {
		return getNbt().getInt(key);
	}
	
	public void setBool(String key, boolean value) {
		sendNbt(key, ByteTag.valueOf(value));
	}
	
	public boolean getBool(String key) {
		return getNbt().getBoolean(key);
	}
	
	public boolean toggleBool(String key) {
		boolean v = getBool(key);
		setBool(key, !v);
		return !v;
	}
	
	public void setPos(String key, Pos pos) {
		setString(key, pos.toNbt());
	}
	
	public Pos getPos(String key) {
		String bp = getString(key);
		return new Pos().fromString(bp);
	}
	
	public void setPosList(String key, List<Pos> pos) {
		ListTag poss = new ListTag();
		for(Pos p : pos) {
			poss.add(StringTag.valueOf(p.toNbt()));
		}
		this.setList(key, poss);
	}
	
	public List<Pos> getPosList(String key) {
		ListTag poss = this.getList(key, ListTag.TAG_STRING);
		List<Pos> pos = new ArrayList<Pos>();
		for(int i = 0; i < poss.size(); i++) {
			String p = poss.getString(i);
			pos.add(new Pos().fromString(p));
		}
		return pos;
	}
	
	public void setList(String key, ListTag list) {
		sendNbt(key, list);
	}
	
	public void addToList(String key, Tag item) {
		addToList(key, -1, item);
	}
	
	public void addToList(String key, int index, Tag item) {
		ListTag list = getList(key, item.getId());
		if(index < 0 || index >= list.size()) list.add(item);
		else list.set(index, item);
		setList(key, list);
	}
	
	public void removeFromList(String key, Tag item) {
		ListTag list = getList(key, item.getId());
		list.remove(item);
		setList(key, list);
	}
	
	public void removeFromList(String key, int index) {
		if(getNbt().get(key).getId() != Type.LIST.type) return;
		ListTag list = (ListTag) getNbt().get(key);
		index = index < 0 ? list.size()-1 : index >= list.size()-1 ? list.size()-1 : index;
		list.remove(index);
		setList(key, list);
	}
	
	public ListTag getList(String key, Type type) {
		CompoundTag nbt = getNbt();
		ListTag list = nbt.getList(key, type.type);
		return list;
	}
	
	public ListTag getList(String key, byte type) {
		CompoundTag nbt = getNbt();
		ListTag list = nbt.getList(key, type);
		return list;
	}
	
	public void remove(String key) {
		CompoundTag nbt = this.getNbt();
		nbt.remove(key);
		PacketHandler.INSTANCE.sendToServer(new ServerNbtReplace(nbt, hand));
	}
		
	/**
	 * Gets the nbt of the item the player is holding
	 * @return CompoundTag of item; without item, a empty compound
	 */
	public CompoundTag getNbt() {
		ItemStack stack = Minecraft.getInstance().player.getItemInHand(hand);
		if(stack != null && stack.getItem() instanceof ItemDevice) return stack.getTag();
		else {
			Eye.LOGGER.warn("Failed to retrieve nbt data, returned empty one.");
			return new CompoundTag();
		}
	}
	
	/**
	 * Overrides nbt of the item; for smaller changes, use sendNbt()
	 * @param nbt
	 */
	public void updateNbt(CompoundTag nbt) {
		PacketHandler.INSTANCE.sendToServer(new ServerNbtReplace(nbt, hand));
    }
	
	/**
	 * Used to update a specific key, used to keep to package size small
	 * @param key The key of the nbt element you want to change
	 * @param element The item you want to change in Element form 
	 */
	public void sendNbt(String key, Tag element) {
		PacketHandler.INSTANCE.sendToServer(new ServerNbtSet(key, element, hand));
	}
	
	
	/**
	 * Gets the total amount of keys in the dataset. Used for the fictional storage of the device.
	 * @return the amount of items (up to 2 levels of CompoundTags.
	 */
	public static int getStorage(CompoundTag nbt) {
		Set<String> keys = nbt.getAllKeys();
		int storage = 0;
		for(String k : keys) {
			if(nbt.get(k).getType() == ListTag.TYPE) {
				storage += ((ListTag)nbt.get(k)).size();
			} else if(nbt.get(k).getType() == CompoundTag.TYPE) {
				storage += ((CompoundTag)nbt.get(k)).size();
			} else if(nbt.get(k).getType() == IntArrayTag.TYPE) {
				storage += nbt.getIntArray(k).length;
			} else {
				storage += 1;
			}
		}
		return storage;
	}
	
	public boolean hasStorage() {
		return getStorage(getNbt()) < max_storage;
	}

	
	public boolean saving() {
		return saving >= 0;
	}

	public void update() {
		if(saving >= 0) saving--;
	}
}
