package com.podloot.eyemod.items;

import java.util.ArrayList;
import java.util.List;

import com.electronwill.nightconfig.core.UnmodifiableConfig;
import com.podloot.eyemod.Eye;
import com.podloot.eyemod.blocks.Charger;
import com.podloot.eyemod.blocks.Router;
import com.podloot.eyemod.blocks.entities.RouterEntity;
import com.podloot.eyemod.config.EyeConfig;
import com.podloot.eyemod.config.EyeConfigData;
import com.podloot.eyemod.gui.GuiPhone;
import com.podloot.eyemod.gui.GuiRouter;
import com.podloot.eyemod.gui.util.Naming.Type;
import com.podloot.eyemod.gui.util.Timer;
import com.podloot.eyemod.gui.util.messages.EyeMsgHandler;
import com.podloot.eyemod.lib.gui.util.Line;
import com.podloot.eyemod.lib.gui.util.Pos;
import com.podloot.eyemod.network.ClientGuiOpen;
import com.podloot.eyemod.network.PacketHandler;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeConfigSpec.ValueSpec;
import net.minecraftforge.network.PacketDistributor;

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
	public void appendHoverText(ItemStack itemStack, Level world, List<Component> tooltip, TooltipFlag p_41424_) {
		 String name = "No Owner";
		 if(itemStack.getTag() != null && itemStack.getTag().contains("user")) {
			 name = itemStack.getTag().getString("user");
		 }
	    tooltip.add(Component.literal(name).withStyle(ChatFormatting.DARK_GRAY));
		super.appendHoverText(itemStack, world, tooltip, p_41424_);
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
		CompoundTag data = user.getItemInHand(hand).getTag();
		if (!data.contains("user")) {
			user.getItemInHand(hand).setTag(this.getDefaultNbt(data, world, user));
		}
		
		
		if(user.getItemInHand(hand).getDamageValue() < user.getItemInHand(hand).getMaxDamage()) {
			if(!world.isClientSide) {
				EyeConfigData config = new EyeConfigData();
				PacketHandler.INSTANCE.send(PacketDistributor.DIMENSION.with(() -> world.dimension()), new ClientGuiOpen(user.getScoreboardName(), hand, config, EyeConfig.op_apps.get(), this.isOP(world, user) ));
				return InteractionResultHolder.success(user.getItemInHand(hand));
			}
		} else {
			if(world.isClientSide()) {
				sendMsg("text.eyemod.battery_empty");
			}
			return InteractionResultHolder.fail(user.getItemInHand(hand));
		}
		return InteractionResultHolder.fail(user.getItemInHand(hand));
	}
	
	private CompoundTag getDefaultNbt(CompoundTag data, Level world, Player user) {
		data.putString("user", user.getScoreboardName());
		if(!data.contains("apps")) {
			ListTag list = data.getList("apps", Type.STRING.type);
			list.add(StringTag.valueOf("eyemod:settings"));
			list.add(StringTag.valueOf("eyemod:store"));
			list.add(StringTag.valueOf("eyemod:mail"));
			data.put("apps", list);
		}
		data.putString("ID", getUniqueID(world));
		data.putInt("storage", storage);
		
		CompoundTag set = new CompoundTag();
		set.putInt("device", 0xff222222);
		set.putInt("background", 0xff383838);
		set.putBoolean("location", true);
		set.putBoolean("notification", true);
		data.put("settings", set);
		return data;
	}
	
	private boolean isOP(Level world, Player user) {
		boolean opConfig = EyeConfig.eye_ops.get().contains(user.getScoreboardName());
		boolean OP = world.getServer().getPlayerList().isOp(user.getGameProfile());
		return OP;
	}
	
	private String getUniqueID(Level world) {
		int id = (int) (world.dayTime() + world.getRandom().nextInt(99999));
		return Eye.VERSION + "_" + id;
				 
	}
	
	@Override
	public InteractionResult useOn(UseOnContext context) {
		BlockPos bp = context.getClickedPos();
		BlockState bs = context.getLevel().getBlockState(bp);
		if(bs.getBlock() == Eye.ROUTER.get()) {
			return useOnRouter(context.getLevel(), context.getPlayer(), bs, context.getItemInHand(), bp);	
		} else if(bs.getBlock() == Eye.CHARGER.get()) {
			return useOnCharger(context.getLevel(), bs, context.getItemInHand(), bp);
		}
		return super.useOn(context);
	}
	
	public InteractionResult useOnCharger(Level world, BlockState state, ItemStack stack, BlockPos pos) {
		CompoundTag nbt = stack.getTag();
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
						Minecraft.getInstance().player.playSound(SoundEvents.INK_SAC_USE, 1, 1);
					}
				}
			}
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.FAIL;
	}
	
	public InteractionResult useOnRouter(Level world, Player user, BlockState state, ItemStack stack, BlockPos pos) {
		if(state.getValue(Router.ON)) {
			if(world.getBlockEntity(pos) instanceof RouterEntity) {
				RouterEntity re = ((RouterEntity)world.getBlockEntity(pos));
				String routerOwner = re.owner;
				String routerPass = re.password;
				boolean canOpen = routerOwner.equals(stack.getTag().getString("user")) || routerPass.equals(stack.getTag().getString("net")) || routerPass.isEmpty();
				if(!world.isClientSide()) {
					InteractionHand hand = user.getMainHandItem().getItem() instanceof ItemDevice ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
					PacketHandler.INSTANCE.send(PacketDistributor.DIMENSION.with(() -> world.dimension()), new ClientGuiOpen(user.getScoreboardName(), hand, new Pos(pos, world.dimension().location()), canOpen));
				}
				return InteractionResult.SUCCESS;
			} else {
				return InteractionResult.FAIL;
			}
		} else {
			if(world.isClientSide()) {
				sendMsg("text.eyemod.block_router_off");
			}
			return InteractionResult.SUCCESS;
		}
	}
	
	public void sendMsg(String msg) {
		Minecraft.getInstance().player.displayClientMessage(new Line(msg).asText(), true);
	}
	
	@Override
	public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
		if(!world.isClientSide()) {
			if(selected) this.messageHandler(stack);
			this.batteryTick(world.random, stack, selected);
		} else {
			this.timerTick(stack);
		}
		super.inventoryTick(stack, world, entity, slot, selected);
	}
	
	public void messageHandler(ItemStack stack) {
		ListTag msgs = stack.getTag().getList("_msgs", Type.COMPOUND.type);
		if(msgs.size() <= 0) return;
		CompoundTag msg = msgs.getCompound(0);
		msgs.remove(0);
		if(!msg.contains("id")) return;
		EyeMsgHandler.messages.get(msg.getInt("id")).handle(stack, msg);
		stack.getTag().put("_msgs", msgs);
	}
	
	public void batteryTick(RandomSource random, ItemStack stack, boolean selected) {
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
