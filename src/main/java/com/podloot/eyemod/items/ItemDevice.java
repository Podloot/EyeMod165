package com.podloot.eyemod.items;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.podloot.eyemod.Eye;
import com.podloot.eyemod.blocks.Charger;
import com.podloot.eyemod.blocks.Router;
import com.podloot.eyemod.blocks.entities.RouterEntity;
import com.podloot.eyemod.config.EyeConfig;
import com.podloot.eyemod.gui.util.Naming.Type;
import com.podloot.eyemod.gui.util.Timer;
import com.podloot.eyemod.gui.util.messages.EyeMsgHandler;
import com.podloot.eyemod.lib.gui.util.Line;
import com.podloot.eyemod.lib.gui.util.Pos;
import com.podloot.eyemod.network.ClientGuiOpen;
import com.podloot.eyemod.network.PacketHandler;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.PacketDistributor;

public class ItemDevice extends Item {

	public List<Timer> timers = new ArrayList<Timer>();
	int storage = 256;
	int battery = 256;
	
	public ItemDevice(Properties p, int storage, int battery) {
		super(p);
		this.storage = storage;
		this.battery = battery;
	}
	
	@Override
	public void appendHoverText(ItemStack itemStack, World world, List<ITextComponent> tooltip, ITooltipFlag p_41424_) {
		 String name = "No Owner";
		 if(itemStack.getTag() != null && itemStack.getTag().contains("user")) {
			 name = itemStack.getTag().getString("user");
		 }
	    tooltip.add(new TranslationTextComponent(name).withStyle(TextFormatting.DARK_GRAY));
		super.appendHoverText(itemStack, world, tooltip, p_41424_);
	}
	
	@Override
	public ActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		// TODO Auto-generated method stub
		
		CompoundNBT data = user.getItemInHand(hand).getTag();
		if (!data.contains("user")) {
			user.getItemInHand(hand).setTag(this.getDefaultNbt(data, world, user));
		}
		
		if(user.getItemInHand(hand).getDamageValue() < user.getItemInHand(hand).getMaxDamage()) {
			if (!world.isClientSide()) {
				PacketHandler.INSTANCE.send(PacketDistributor.DIMENSION.with(() -> world.dimension()), new ClientGuiOpen(user.getScoreboardName(), hand, EyeConfig.op_apps.get(), this.isOP(world, user)));
			}
		} else {
			if(world.isClientSide()) {
				sendMsg("text.eyemod.battery_empty");
			}
		}
		
		return super.use(world, user, hand);
	}
	
	private CompoundNBT getDefaultNbt(CompoundNBT data, World world, PlayerEntity user) {
		data.putString("user", user.getScoreboardName());
		if(!data.contains("apps")) {
			ListNBT list = data.getList("apps", Type.STRING.type);
			list.add(StringNBT.valueOf("eyemod:settings"));
			list.add(StringNBT.valueOf("eyemod:store"));
			list.add(StringNBT.valueOf("eyemod:mail"));
			data.put("apps", list);
		}
		data.putString("ID", getUniqueID(world));
		data.putInt("storage", storage);
		
		CompoundNBT set = new CompoundNBT();
		set.putInt("device", 0xff222222);
		set.putInt("background", 0xff383838);
		set.putBoolean("location", true);
		set.putBoolean("notification", true);
		data.put("settings", set);
		return data;
	}
	
	private boolean isOP(World world, PlayerEntity user) {
		boolean OP = world.getServer().getPlayerList().isOp(user.getGameProfile());
		boolean opConfig = EyeConfig.eye_ops.get().contains(user.getScoreboardName());
		return OP || opConfig;
	}
	
	private String getUniqueID(World world) {
		int id = (int) (world.dayTime() + world.getRandom().nextInt(99999));
		return Eye.VERSION + "_" + id;
				 
	}
	
	@Override
	public ActionResultType useOn(ItemUseContext context) {
		BlockPos bp = context.getClickedPos();
		BlockState bs = context.getLevel().getBlockState(bp);
		if(bs.getBlock() == Eye.ROUTER.get()) {
			return useOnRouter(context.getLevel(), context.getPlayer(), bs, context.getItemInHand(), bp);	
		} else if(bs.getBlock() == Eye.CHARGER.get()) {
			return useOnCharger(context.getLevel(), bs, context.getItemInHand(), bp);
		}
		return super.useOn(context);
	}
	
	public ActionResultType useOnCharger(World world, BlockState state, ItemStack stack, BlockPos pos) {
		CompoundNBT nbt = stack.getTag();
		int lvl = state.getValue(Charger.LEVEL);
		if(lvl >= 1) {
			if(stack.getDamageValue() > 0) {
				int nb = stack.getDamageValue() - 10;
				stack.setDamageValue(nb < 0 ? 0 : nb);
				world.setBlockAndUpdate(pos, state.setValue(Charger.LEVEL, lvl-1));
				if(world.isClientSide()) {
					if(lvl-1 <= 0) {
						Minecraft.getInstance().player.playSound(SoundEvents.BUCKET_EMPTY, 1, 1);
					} else {
						Minecraft.getInstance().player.playSound(SoundEvents.GRINDSTONE_USE, 1, 1);
					}
				}
			}
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.CONSUME;
	}
	
	public ActionResultType useOnRouter(World world, PlayerEntity user, BlockState state, ItemStack stack, BlockPos pos) {
		if(state.getValue(Router.ON)) {
			if(world.getBlockEntity(pos) instanceof RouterEntity) {
				RouterEntity re = ((RouterEntity)world.getBlockEntity(pos));
				String routerOwner = re.owner;
				String routerPass = re.password;
				boolean canOpen = routerOwner.equals(stack.getTag().getString("user")) || routerPass.equals(stack.getTag().getString("net")) || routerPass.isEmpty();
				if(!world.isClientSide()) {
					PacketHandler.INSTANCE.send(PacketDistributor.DIMENSION.with(() -> world.dimension()), new ClientGuiOpen(user.getScoreboardName(), Hand.MAIN_HAND, new Pos(pos, world.dimension().location()), canOpen));
				}
				return ActionResultType.SUCCESS;
			} else {
				return ActionResultType.CONSUME;
			}
		} else {
			if(world.isClientSide()) {
				sendMsg("text.eyemod.block_router_off");
			}
			return ActionResultType.SUCCESS;
		}
	}
	
	public void sendMsg(String msg) {
		Minecraft.getInstance().player.displayClientMessage(new Line(msg).asText(), true);
	}
	
	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		if(!world.isClientSide()) {
			if(selected) this.messageHandler(stack);
			this.batteryTick(world.random, stack, selected);
		} else {
			this.timerTick(stack);
		}
		super.inventoryTick(stack, world, entity, slot, selected);
	}
	
	public void messageHandler(ItemStack stack) {
		ListNBT msgs = stack.getTag().getList("_msgs", Type.COMPOUND.type);
		if(msgs.size() <= 0) return;
		CompoundNBT msg = msgs.getCompound(0);
		msgs.remove(0);
		if(!msg.contains("id")) return;
		EyeMsgHandler.messages.get(msg.getInt("id")).handle(stack, msg);
		stack.getTag().put("_msgs", msgs);
	}
	
	public void batteryTick(Random random, ItemStack stack, boolean selected) {
		if(stack.getDamageValue() < stack.getMaxDamage()) {
			int min =  120; //Eye.CONFIG.getConfig().getInt("battery_minutes");
			int range = 20*(min <= 0 ? 1 : min);
			range = selected ? range : range*60;
			if(random.nextInt(range) < 1)	stack.setDamageValue(stack.getDamageValue()+1);
		}
	}
	
	public void timerTick(ItemStack stack) {
		List<Timer> remove = new ArrayList<Timer>();
		for(Timer t : timers) {
			if(stack.getTag().getString("ID").equals(t.getDeviceID())) {
				t.tick();
				if(t.done()) {
					remove.add(t);
				}
			}
		}
		timers.removeAll(remove);
	}
	
	
}
