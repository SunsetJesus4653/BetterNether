package org.betterx.betternether.blocks;

import org.betterx.bclib.behaviours.interfaces.BehaviourSapling;
import org.betterx.bclib.blocks.FeatureSaplingBlock;
import org.betterx.betternether.BlocksHelper;
import org.betterx.betternether.interfaces.SurvivesOnNetherrack;
import org.betterx.betternether.registry.features.configured.NetherTrees;
import org.betterx.wover.feature.api.configured.ConfiguredFeatureKey;
import org.betterx.wover.state.api.WorldState;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import org.jetbrains.annotations.NotNull;

public class BlockStalagnateSeed extends FeatureSaplingBlock implements BonemealableBlock, SurvivesOnNetherrack, BehaviourSapling {
    public static final int MAX_SEARCH_LENGTH = 25; // 27
    public static final int MIN_LENGTH = 3; // 5

    protected static final VoxelShape SHAPE_TOP = Block.box(4, 6, 4, 12, 16, 12);
    protected static final VoxelShape SHAPE_BOTTOM = Block.box(4, 0, 4, 12, 12, 12);

    public static final BooleanProperty TOP = BooleanProperty.create("top");

    public BlockStalagnateSeed() {
        super((level, pos, state, rnd) -> growsDownward(state)
                ? NetherTrees.STALAGNATE_DOWN.placeInWorld(WorldState.registryAccess(), level, pos, rnd)
                : NetherTrees.STALAGNATE.placeInWorld(WorldState.registryAccess(), level, pos, rnd));
        this.registerDefaultState(getStateDefinition().any().setValue(TOP, true));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
        super.createBlockStateDefinition(stateManager);
        stateManager.add(TOP);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState blockState = this.defaultBlockState();
        if (ctx.getClickedFace() == Direction.DOWN)
            return blockState;
        else if (ctx.getClickedFace() == Direction.UP)
            return blockState.setValue(TOP, false);
        else
            return null;
    }

    public VoxelShape getShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext ePos) {
        return growsDownward(state) ? SHAPE_TOP : SHAPE_BOTTOM;
    }

    private static boolean growsDownward(BlockState state) {
        return state.getValue(TOP);
    }

    @Override
    public boolean isBonemealSuccess(Level world, RandomSource random, BlockPos pos, BlockState state) {
        if (super.isBonemealSuccess(world, random, pos, state)) {
            if (growsDownward(state))
                return BlocksHelper.downRay(world, pos, MIN_LENGTH) > 0;
            else
                return BlocksHelper.upRay(world, pos, MIN_LENGTH) > 0;
        }
        return false;
    }

    @Override
    protected boolean mayPlaceOn(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return isSurvivable(blockState);
    }

    @Override
    public boolean canSurvive(BlockState blockState, LevelReader levelReader, BlockPos blockPos) {
        if (blockState.getValues().get(TOP) == null) {
            return false;
        }
        final BlockPos target;
        if (growsDownward(blockState)) {
            target = blockPos.above();
        } else {
            target = blockPos.below();
        }

        return this.mayPlaceOn(levelReader.getBlockState(target), levelReader, target);
    }

    @Override
    protected boolean growFeature(
            @NotNull ServerLevel world,
            @NotNull BlockPos pos,
            @NotNull BlockState state,
            @NotNull RandomSource random
    ) {
        if (WorldState.registryAccess() == null) return false;
        final ConfiguredFeatureKey<?> featureHolder;
        if (growsDownward(state)) {
            featureHolder = NetherTrees.STALAGNATE_DOWN;
        } else {
            featureHolder = NetherTrees.STALAGNATE;
        }
        return featureHolder.placeInWorld(WorldState.registryAccess(), world, pos, random);
    }
}
