package com.podloot.eyemod.blocks;

import com.podloot.eyemod.Eye;
import com.podloot.eyemod.blocks.entities.RouterEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class Router extends Block implements EntityBlock {

	public static final BooleanProperty ON = BooleanProperty.create("on");
	public static final BooleanProperty SMOKE = BooleanProperty.create("smoke");
	public static final BooleanProperty FIRE = BooleanProperty.create("fire");

	public Router(Properties p) {
		super(p);
		this.registerDefaultState(this.stateDefinition.any().setValue(ON, Boolean.valueOf(false))
				.setValue(SMOKE, Boolean.valueOf(false)).setValue(FIRE, Boolean.valueOf(false)));
	}

	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		if (world.hasNeighborSignal(pos)) {
			world.setBlockAndUpdate(pos, state.setValue(ON, true));
		} else {
			world.setBlockAndUpdate(pos, state.setValue(ON, false));
		}
		if (world.getBlockEntity(pos) instanceof RouterEntity && placer != null && placer instanceof Player) {
			((RouterEntity) world.getBlockEntity(pos)).owner = placer.getScoreboardName();
			((RouterEntity) world.getBlockEntity(pos)).update();
		}
		super.setPlacedBy(world, pos, state, placer, itemStack);
	}

	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
		if (world.getBlockState(pos).getValue(SMOKE)) {
			spawnSmoke(world, pos, ParticleTypes.SMOKE);
		}
		if (world.getBlockState(pos).getValue(FIRE)) {
			spawnSmoke(world, pos, ParticleTypes.LARGE_SMOKE);
		}
		super.animateTick(state, world, pos, random);
	}

	private static void spawnSmoke(Level world, BlockPos pos, SimpleParticleType smoke2) {
		double d0 = 0.5625D;
		RandomSource randomsource = world.random;

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
	public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos fromPos,
			boolean notify) {
		if (world.hasNeighborSignal(pos)) {
			world.setBlockAndUpdate(pos, state.setValue(ON, true));
		} else {
			world.setBlockAndUpdate(pos, state.setValue(Router.SMOKE, false).setValue(Router.FIRE, false).setValue(Router.ON, false));
		}
		super.neighborChanged(state, world, pos, block, fromPos, notify);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		// TODO Auto-generated method stub
		return Eye.ROUTER_ENTITY.get().create(pos, state);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state,
			BlockEntityType<T> type) {
		return world.isClientSide() ? null : (world1, pos1, sate1, blockEntity) -> ((RouterEntity) blockEntity).tick();
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
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

	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> s) {
		s.add(ON);
		s.add(SMOKE);
		s.add(FIRE);
	}

}
