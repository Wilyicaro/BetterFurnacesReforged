package wily.betterfurnaces.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import wily.betterfurnaces.blockentity.FuelVerifierBlockEntity;

import org.jetbrains.annotations.Nullable;

public class FuelVerifierBlock extends BFRBlock implements EntityBlock {
    public static final String FUEL_VERIFIER = "fuel_verifier";

    public static final EnumProperty<Direction> FACING = DirectionalBlock.FACING;

    public FuelVerifierBlock(Properties properties) {
        super(properties.destroyTime(3f));
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite());
    }

    @Override
    public InteractionResult /*? if >=1.20.5 {*/useWithoutItem/*?} else {*//*use*//*?}*/(BlockState state, Level level, BlockPos pos, Player player/*? if <1.20.5 {*//*, InteractionHand hand*//*?}*/, BlockHitResult blockHitResult) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            this.openBlockEntityMenu(level, pos, player);
        }

        return InteractionResult.CONSUME;
    }

    public RenderShape getRenderType(BlockState p_149645_1_) {
        return RenderShape.MODEL;
    }

    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.FACING);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return (level1, pos, state1, tile) -> {
            if (tile instanceof FuelVerifierBlockEntity be)
                be.tick(state1);
        };
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return new FuelVerifierBlockEntity(p_153215_, p_153216_);
    }

}
