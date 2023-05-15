package com.podloot.eyemod.blocks;


import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class Charger extends Block {

	public static final BooleanProperty ON = BooleanProperty.create("on");
	public static final IntegerProperty LEVEL = IntegerProperty.create("level", 0, 8);

	public Charger(Properties p) {
		super(p);

		this.registerDefaultState(
				this.stateDefinition.any().setValue(LEVEL, Integer.valueOf(0)).setValue(ON, Boolean.valueOf(false)));
	}
	
	@Override
	public void animateTick(BlockState state, World world, BlockPos pos, Random random) {
		if (state.getValue(ON) && world.random.nextInt(30) <= 1) {
			spawnSmoke(world, pos);
		}
		super.animateTick(state, world, pos, random);
	}

	private static void spawnSmoke(World p_55455_, BlockPos p_55456_) {
		double d0 = 0.5625D;
		Random randomsource = p_55455_.random;

		for (Direction direction : Direction.values()) {
			BlockPos blockpos = p_55456_.relative(direction);
			if (!p_55455_.getBlockState(blockpos).isSolidRender(p_55455_, blockpos)) {
				Direction.Axis direction$axis = direction.getAxis();
				double d1 = direction$axis == Direction.Axis.X ? 0.5D + 0.5625D * (double) direction.getStepX()
						: (double) randomsource.nextFloat();
				double d2 = direction$axis == Direction.Axis.Y ? 0.5D + 0.5625D * (double) direction.getStepY()
						: (double) randomsource.nextFloat();
				double d3 = direction$axis == Direction.Axis.Z ? 0.5D + 0.5625D * (double) direction.getStepZ()
						: (double) randomsource.nextFloat();
				p_55455_.addParticle(RedstoneParticleData.REDSTONE, (double) p_55456_.getX() + d1,
						(double) p_55456_.getY() + d2, (double) p_55456_.getZ() + d3, 0.0D, 0.0D, 0.0D);
			}
		}

	}

	@Override
	public void setPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		if (world.hasNeighborSignal(pos)) {
			world.setBlockAndUpdate(pos, state.setValue(ON, true));
		} else {
			world.setBlockAndUpdate(pos, state.setValue(ON, false));
		}
		super.setPlacedBy(world, pos, state, placer, itemStack);
	}

	@Override
	public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos,
			boolean notify) {
		if (world.hasNeighborSignal(pos)) {
			world.setBlockAndUpdate(pos, state.setValue(ON, true));
		} else {
			world.setBlockAndUpdate(pos, state.setValue(ON, false));
		}
		super.neighborChanged(state, world, pos, block, fromPos, notify);
	}

	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (state.getValue(ON)) {
			int lvl = state.getValue(LEVEL);
			if (lvl < 8) {
				world.setBlockAndUpdate(pos, state.setValue(LEVEL, lvl + 1));
			}
		} else {
			int lvl = state.getValue(LEVEL);
			if (lvl > 0) {
				world.setBlockAndUpdate(pos, state.setValue(LEVEL, lvl - 1));
			}
		}
		super.randomTick(state, world, pos, random);
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState state, World world, BlockPos pos) {
		return state.getValue(LEVEL);
	}

	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> s) {
		s.add(ON);
		s.add(LEVEL);
	}
}
