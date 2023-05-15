package com.podloot.eyemod.blocks.entities;

import java.util.ArrayList;
import java.util.List;

import com.podloot.eyemod.Eye;
import com.podloot.eyemod.blocks.Router;
import com.podloot.eyemod.gui.util.Naming.Type;
import com.podloot.eyemod.items.ItemDevice;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class RouterEntity extends BlockEntity {

	public int max_storage = 128;
	int steps = 43;
	int timer = 5;
	int max_time = 10;

	public CompoundTag data = new CompoundTag();
	public ListTag messages = new ListTag();
	public String owner = "";
	public String password = "";
	public int storage = 0;

	public RouterEntity(BlockPos pos, BlockState state) {
		super(Eye.ROUTER_ENTITY.get(), pos, state);
		steps = max_storage / 4;
	}

	public void setData(int action, String key, Tag tag) {
		switch (action) {
		case 0:
			if (storage >= max_storage)
				break;
			if (tag != null)
				data.put(key, tag);
			break;
		case 1:
			if (storage >= max_storage)
				break;
			if (tag != null) {
				ListTag list = data.getList(key, tag.getId());
				list.add(tag);
				data.put(key, list);
			}
			break;
		case 2:
			data.remove(key);
			break;
		case 3:
			ListTag rlist = (ListTag) data.get(key);
			if (tag.getId() == Type.INT.type) {
				rlist.remove(((IntTag) tag).getAsInt());
			} else {
				rlist.remove(tag);
			}
			data.put(key, rlist);
			break;
		case 4:
			data = new CompoundTag();
			messages.clear();
			break;
		case 5:
			if (tag.getId() != Type.COMPOUND.type)
				break;
			ListTag posts = data.getList("posts", Type.COMPOUND.type);
			CompoundTag reaction = ((CompoundTag) tag);
			int index = reaction.getInt("i");
			String re = reaction.getString("re");
			if (index >= posts.size() || index < 0)
				break;
			CompoundTag post = posts.getCompound(index);
			ListTag res = post.getList("re", Type.STRING.type);
			res.add(StringTag.valueOf(re));
			post.put("re", res);
			posts.remove(index);
			posts.add(post);
			data.put("posts", posts);
			break;
		}
		update();

	}

	public void update() {
		storage = this.getDataStorage();
		this.setChanged();
		level.sendBlockUpdated(worldPosition, level.getBlockState(worldPosition), level.getBlockState(worldPosition), Block.UPDATE_CLIENTS);
	}

	public void recieveMessage(CompoundTag msg) {
		if (!msg.contains("rec") || !msg.contains("id"))
			return;
		msg.putInt("-t", 0);
		messages.add(msg);
		if (messages.size() > max_storage) {
			removeMessage();
		}
		this.setChanged();
	}

	public void removeMessage() {
		int oldest = 0;
		int index = -1;
		for (int i = 0; i < messages.size(); i++) {
			if (messages.getCompound(i).getInt("-t") > oldest) {
				oldest = messages.getCompound(i).getInt("-t");
				index = i;
			}
		}
		if (index != -1)
			messages.remove(index);
	}

	public void tick() {
		if (timer <= 0) {
			BlockState state = level.getBlockState(worldPosition);
			if (state.getValue(Router.ON)) {
				sendMessage();
				updateRouter();
			}
			timer = max_time;
		} else {
			timer--;
		}
	}

	public void sendMessage() {
		if (messages == null || messages.size() <= 0)
			return;
		CompoundTag msg = messages.getCompound(0);
		msg.putInt("-t", msg.getInt("-t") + 1);
		messages.remove(0);
		ItemStack device = getPhone(msg.getString("rec"));
		if (device != null) {
			ListTag msgs = device.getTag().getList("_msgs", Type.COMPOUND.type);
			msg.remove("-t");
			msg.remove("rec");
			msgs.add(msg);
			device.getTag().put("_msgs", msgs);
		} else {
			messages.add(msg);
		}
		this.setChanged();
	}

	public void updateRouter() {
		BlockState state = level.getBlockState(worldPosition);
		int tot = messages.size();
		if (tot >= steps && tot <= steps * 3) {
			level.setBlockAndUpdate(worldPosition, state.setValue(Router.SMOKE, true).setValue(Router.FIRE, false));
			max_time = 10;
		} else if (tot > steps * 3) {
			level.setBlockAndUpdate(worldPosition, state.setValue(Router.SMOKE, true).setValue(Router.FIRE, true));
			max_time = 20;
		} else {
			level.setBlockAndUpdate(worldPosition, state.setValue(Router.SMOKE, false).setValue(Router.FIRE, false));
			max_time = 5;
		}
	}

	public int getDataStorage() {
		int storage = 0;
		for (String k : data.getAllKeys()) {
			if (data.get(k).getId() == Type.LIST.type) {
				if (k.equals("posts"))
					storage += getPostSize((ListTag) data.get(k));
				else
					storage += ((ListTag) data.get(k)).size();
			} else if (data.get(k).getId() == Type.COMPOUND.type) {
				storage += data.getCompound(k).size();
			} else if (data.get(k).getId() == Type.INTARRAY.type) {
				storage += data.getIntArray(k).length;
			} else {
				storage += 1;
			}
		}
		return storage;
	}

	public int getPostSize(ListTag post) {
		int r = 0;
		for (int i = 0; i < post.size(); i++) {
			r += 1;
			if (post.getCompound(i).contains("re")) {
				r += post.getCompound(i).getList("re", Type.STRING.type).size();
			}
		}
		return r;
	}

	private ItemStack getPhone(String name) {
		if (level.isClientSide())
			return null;
		if (level.getServer() == null)
			return null;
		for (Level w : level.getServer().getAllLevels()) {
			for (Player player : w.players()) {
				List<ItemStack> stacks = new ArrayList<ItemStack>();
				stacks.addAll(player.getInventory().items);
				stacks.addAll(player.getInventory().offhand);
				for (ItemStack s : stacks) {
					if (s.getItem() instanceof ItemDevice) {
						if (s.getTag().getString("user").equals(name)) {
							return s;
						}
					}
				}
			}

		}
		return null;
	}
	
	

	@Override
	protected void saveAdditional(CompoundTag nbt) {
		super.saveAdditional(nbt);
		nbt.put("rdata", data);
		nbt.put("rmsg", messages);
		nbt.putString("owner", owner);
		nbt.putString("password", password);
		nbt.putInt("storage", storage);
	}

	@Override
	public void load(CompoundTag nbt) {
		super.load(nbt);
		data = nbt.getCompound("rdata");
		messages = nbt.getList("rmsg", Type.COMPOUND.type);
		owner = nbt.getString("owner");
		password = nbt.getString("password");
		storage = nbt.getInt("storage");
	}

	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	public CompoundTag getUpdateTag() {
		return this.saveWithoutMetadata();
	}

}
