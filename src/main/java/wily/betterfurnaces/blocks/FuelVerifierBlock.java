package wily.betterfurnaces.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import wily.betterfurnaces.blockentity.AbstractCobblestoneGeneratorBlockEntity;
import wily.betterfurnaces.blockentity.AbstractFuelVerifierBlockEntity;
import wily.betterfurnaces.init.Registration;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class FuelVerifierBlock extends Block implements EntityBlock {
    public static final String FUEL_VERIFIER = "fuel_verifier";

    public static final DirectionProperty FACING = DirectionalBlock.FACING;

    public FuelVerifierBlock(Properties properties) {
        super(properties.destroyTime(3f));
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite());
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        List<ItemStack> dropsOriginal = super.getDrops(state, builder);
        if (!dropsOriginal.isEmpty())
            return dropsOriginal;
        return Collections.singletonList(new ItemStack(this, 1));
    }


    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult p_225533_6_) {
        if (world.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            this.interactWith(world, pos, player);
        }

        return InteractionResult.SUCCESS;
    }

    private void interactWith(Level world, BlockPos pos, Player player) {
        BlockEntity tileEntity = world.getBlockEntity(pos);
        if (tileEntity instanceof MenuProvider) {
            NetworkHooks.openGui((ServerPlayer) player, (MenuProvider) tileEntity, tileEntity.getBlockPos());
        }
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean p_196243_5_) {
        if (state.getBlock() != oldState.getBlock()) {
            BlockEntity be = world.getBlockEntity(pos);
            if (be instanceof AbstractFuelVerifierBlockEntity) {
                Containers.dropContents(world, pos, (AbstractFuelVerifierBlockEntity) be);
                world.updateNeighbourForOutputSignal(pos, this);
            }

            super.onRemove(state, world, pos, oldState, p_196243_5_);
        }
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
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return new AbstractFuelVerifierBlockEntity.FuelVerifierBlockEntity(p_153215_, p_153216_);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return (level1, pos, state1, tile) -> {
            if (tile instanceof AbstractFuelVerifierBlockEntity.FuelVerifierBlockEntity be) {
                be.tick(state1);
            }
        };
    }
}
