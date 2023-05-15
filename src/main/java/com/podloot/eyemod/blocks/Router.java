package com.podloot.eyemod.blocks;

import java.util.Random;

import javax.annotation.Nullable;

import com.podloot.eyemod.Eye;
import com.podloot.eyemod.blocks.entities.RouterEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class Router extends Block implements ITileEntityProvider {

	public static final BooleanProperty ON = BooleanProperty.create("on");
	public static final BooleanProperty SMOKE = BooleanProperty.create("smoke");
	public static final BooleanProperty FIRE = BooleanProperty.create("fire");

	public Router(Properties p) {
		super(p);
		this.registerDefaultState(this.stateDefinition.any().setValue(ON, Boolean.valueOf(false))
				.setValue(SMOKE, Boolean.valueOf(false)).setValue(FIRE, Boolean.valueOf(false)));
	}

	@Override
	public void setPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		if (world.hasNeighborSignal(pos)) {
			world.setBlockAndUpdate(pos, state.setValue(ON, true));
		} else {
			world.setBlockAndUpdate(pos, state.setValue(ON, false));
		}
		if (world.getBlockEntity(pos) instanceof RouterEntity && placer != null && placer instanceof Entity) {
			((RouterEntity) world.getBlockEntity(pos)).owner = placer.getScoreboardName();
			((RouterEntity) world.getBlockEntity(pos)).update();
		}
		super.setPlacedBy(world, pos, state, placer, itemStack);
	}

	@Override
	public void animateTick(BlockState state, World world, BlockPos pos, Random random) {
		if (world.getBlockState(pos).getValue(SMOKE)) {
			spawnSmoke(world, pos, ParticleTypes.SMOKE);
		}
		if (world.getBlockState(pos).getValue(FIRE)) {
			spawnSmoke(world, pos, ParticleTypes.LARGE_SMOKE);
		}
		super.animateTick(state, world, pos, random);
	}

	private static void spawnSmoke(World world, BlockPos pos, BasicParticleType smoke2) {
		double d0 = 0.5625D;
		Random randomsource = world.random;

		for (Direction direction : Direction.values()) {
			BlockPos blockpos = pos.relative(direction);
			if (!world.getBlockState(blockpos).isSolidRender(world, blockpos)) {
				Direction.Axis direction$axis = direction.getAxis();
				double d1 = direction$axis == Direction.Axis.X ? 0.5D + 0.5625D * (double) direction.getStepX()
						: (double) randomsource.nextFloat();
				double d2 = direction$axis == Direction.Axis.Y ? 0.5D + 0.5625D * (double) direction.getStepY()
						: (double) randomsource.nextFloat();
				double d3 = direction$axis == Direction.Axis.Z ? 0.5D + 0.5625D * (double) direction.getStepZ()
						: (double) randomsource.nextFloat();
				world.addParticle(smoke2, (double) pos.getX() + d1,
						(double) pos.getY() + d2, (double) pos.getZ() + d3, 0.0D, 0.0D, 0.0D);
			}
		}
	}

	@Override
	public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos,
			boolean notify) {
		if (world.hasNeighborSignal(pos)) {
			world.setBlockAndUpdate(pos, state.setValue(ON, true));
		} else {
			world.setBlockAndUpdate(pos, state.setValue(Router.SMOKE, false).setValue(Router.FIRE, false).setValue(Router.ON, false));
		}
		super.neighborChanged(state, world, pos, block, fromPos, notify);
	}

	//@Override
	//public <T extends TileEntity> TileEntityTicker<T> getTicker(World world, BlockState state,
	//		TileEntityType<T> type) {
	//	return world.isClientSide() ? null : (world1, pos1, sate1, blockEntity) -> ((RouterEntity) tileEntity).tick();
	//}
	

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState state, World world, BlockPos pos) {
		if (world.getBlockEntity(pos) instanceof RouterEntity) {
			RouterEntity re = (RouterEntity) world.getBlockEntity(pos);
			int max = re.max_storage;
			int size = re.storage;
			if (size == 0)
				return 0;
			float per = (float) size / (float) max;
			int out = (int) (14 * per) + 1;
			return out < 0 ? 0 : out > 15 ? 15 : out;
		}
		return 0;

	}

	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> s) {
		s.add(ON);
		s.add(SMOKE);
		s.add(FIRE);
	}

	@Override
	public TileEntity newBlockEntity(IBlockReader p) {
		return Eye.ROUTER_ENTITY.get().create();
	}

}
