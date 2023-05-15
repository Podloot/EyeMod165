package com.podloot.eyemod.blocks.entities;

import java.util.ArrayList;
import java.util.List;

import com.podloot.eyemod.Eye;
import com.podloot.eyemod.blocks.Router;
import com.podloot.eyemod.gui.util.Naming.Type;
import com.podloot.eyemod.items.ItemDevice;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.World;

public class RouterEntity extends TileEntity implements ITickableTileEntity{

	

	public int max_storage = 128;
	int steps = 43;
	int timer = 5;
	int max_time = 10;

	public CompoundNBT data = new CompoundNBT();
	public ListNBT messages = new ListNBT();
	public String owner = "";
	public String password = "";
	public int storage = 0;

	public RouterEntity(TileEntityType<?> te) {
		super(te);
		steps = max_storage / 4; 
	}
	
	public RouterEntity() {
		this(Eye.ROUTER_ENTITY.get());
	}

	public void setData(int action, String key, INBT tag) {
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
				ListNBT list = data.getList(key, tag.getId());
				list.add(tag);
				data.put(key, list);
			}
			break;
		case 2:
			data.remove(key);
			break;
		case 3:
			ListNBT rlist = (ListNBT) data.get(key);
			if (tag.getId() == Type.INT.type) {
				rlist.remove(((IntNBT) tag).getAsInt());
			} else {
				rlist.remove(tag);
			}
			data.put(key, rlist);
			break;
		case 4:
			data = new CompoundNBT();
			messages.clear();
			break;
		case 5:
			if (tag.getId() != Type.COMPOUND.type)
				break;
			ListNBT posts = data.getList("posts", Type.COMPOUND.type);
			CompoundNBT reaction = ((CompoundNBT) tag);
			int index = reaction.getInt("i");
			String re = reaction.getString("re");
			if (index >= posts.size() || index < 0)
				break;
			CompoundNBT post = posts.getCompound(index);
			ListNBT res = post.getList("re", Type.STRING.type);
			res.add(StringNBT.valueOf(re));
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
		level.sendBlockUpdated(worldPosition, level.getBlockState(worldPosition), level.getBlockState(worldPosition), 2);
	}

	public void recieveMessage(CompoundNBT msg) {
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

	@Override
	public void tick() {
		System.out.println(timer);
		if (timer <= 0) {
			BlockState bs = level.getBlockState(this.getBlockPos());
			if (bs.getValue(Router.ON)) {
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
		CompoundNBT msg = messages.getCompound(0);
		msg.putInt("-t", msg.getInt("-t") + 1);
		messages.remove(0);
		ItemStack device = getPhone(msg.getString("rec"));
		if (device != null) {
			ListNBT msgs = device.getTag().getList("_msgs", Type.COMPOUND.type);
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
					storage += getPostSize((ListNBT) data.get(k));
				else
					storage += ((ListNBT) data.get(k)).size();
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

	public int getPostSize(ListNBT post) {
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
		for (World w : level.getServer().getAllLevels()) {
			for (PlayerEntity player : w.players()) {
				List<ItemStack> stacks = new ArrayList<ItemStack>();
				stacks.addAll(player.inventory.items);
				stacks.addAll(player.inventory.offhand);
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
	public CompoundNBT save(CompoundNBT nbt) {
		nbt.put("rdata", data);
		nbt.put("rmsg", messages);
		nbt.putString("owner", owner);
		nbt.putString("password", password);
		nbt.putInt("storage", storage);
		return super.save(nbt);
	}
	
	@Override
	public void load(BlockState bs, CompoundNBT nbt) {
		data = nbt.getCompound("rdata");
		messages = nbt.getList("rmsg", Type.COMPOUND.type);
		owner = nbt.getString("owner");
		password = nbt.getString("password");
		storage = nbt.getInt("storage");
		super.load(bs, nbt);
	}

	public CompoundNBT getUpdateTag() {
		return this.getTileData();
	}

}
