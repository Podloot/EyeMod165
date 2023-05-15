package com.podloot.eyemod.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class Charger extends Block {

	public static final BooleanProperty ON = BooleanProperty.create("on");
	public static final IntegerProperty LEVEL = IntegerProperty.create("level", 0, 8);

	public Charger(Properties p) {
		super(p.randomTicks());
		this.registerDefaultState(
				this.stateDefinition.any().setValue(LEVEL, Integer.valueOf(0)).setValue(ON, Boolean.valueOf(false)));
	}

	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
		if (state.getValue(ON) && world.random.nextInt(30) <= 1) {
			spawnSmoke(world, pos);
		}
		super.animateTick(state, world, pos, random);
	}

	private static void spawnSmoke(Level p_55455_, BlockPos p_55456_) {
		double d0 = 0.5625D;
		RandomSource randomsource = p_55455_.random;

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
				p_55455_.addParticle(DustParticleOptions.REDSTONE, (double) p_55456_.getX() + d1,
						(double) p_55456_.getY() + d2, (double) p_55456_.getZ() + d3, 0.0D, 0.0D, 0.0D);
			}
		}

	}

	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		if (world.hasNeighborSignal(pos)) {
			world.setBlockAndUpdate(pos, state.setValue(ON, true));
		} else {
			world.setBlockAndUpdate(pos, state.setValue(ON, false));
		}
		super.setPlacedBy(world, pos, state, placer, itemStack);
	}

	@Override
	public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos fromPos,
			boolean notify) {
		if (world.hasNeighborSignal(pos)) {
			world.setBlockAndUpdate(pos, state.setValue(ON, true));
		} else {
			world.setBlockAndUpdate(pos, state.setValue(ON, false));
		}
		super.neighborChanged(state, world, pos, block, fromPos, notify);
	}

	@Override
	public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
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
	public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
		return state.getValue(LEVEL);
	}

	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> s) {
		s.add(ON);
		s.add(LEVEL);
	}
}
