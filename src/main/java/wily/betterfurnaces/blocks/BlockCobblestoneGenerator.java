package wily.betterfurnaces.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import wily.betterfurnaces.blockentity.BlockEntityCobblestoneGenerator;
import wily.betterfurnaces.init.Registration;
import wily.betterfurnaces.items.ItemUpgradeFuelEfficiency;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public abstract class BlockCobblestoneGenerator extends Block implements EntityBlock {
    public static final String COBBLESTONE_GENERATOR = "cobblestone_generator";

    public static final DirectionProperty FACING = DirectionalBlock.FACING;
    public static final IntegerProperty TYPE = IntegerProperty.create("state", 0, 3);

    public BlockCobblestoneGenerator(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH).setValue(TYPE, 0));
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
        ItemStack stack = player.getItemInHand(handIn).copy();
        ItemStack hand = player.getItemInHand(handIn);
        BlockEntityCobblestoneGenerator te = (BlockEntityCobblestoneGenerator) world.getBlockEntity(pos);

        if (world.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            if ((hand.getItem() == Items.LAVA_BUCKET) || (hand.getItem() == Items.WATER_BUCKET) || (hand.getItem() instanceof ItemUpgradeFuelEfficiency)) {
                interactInsert(world, pos, player, handIn, stack);
            } else this.interactWith(world, pos, player);
        }

        return InteractionResult.SUCCESS;
    }

    private void interactWith(Level world, BlockPos pos, Player player) {
        BlockEntity tileEntity = world.getBlockEntity(pos);
        if (tileEntity instanceof MenuProvider) {
            NetworkHooks.openGui((ServerPlayer) player, (MenuProvider) tileEntity, tileEntity.getBlockPos());
            player.awardStat(Stats.INTERACT_WITH_FURNACE);
        }
    }
    private InteractionResult interactInsert(Level world, BlockPos pos, Player player, InteractionHand handIn, ItemStack stack) {
        ItemStack hand = player.getItemInHand(handIn);
        if (!((hand.getItem() == Items.LAVA_BUCKET) || (hand.getItem() == Items.WATER_BUCKET) || (hand.getItem() instanceof ItemUpgradeFuelEfficiency))){
            return InteractionResult.SUCCESS;
        }
        BlockEntity te = world.getBlockEntity(pos);
        if (!(te instanceof BlockEntityCobblestoneGenerator)) {
            return InteractionResult.SUCCESS;
        }
        ItemStack newStack = new ItemStack(stack.getItem(), 1);
        newStack.setTag(stack.getTag());
        if (player.getItemInHand(handIn).getItem() instanceof ItemUpgradeFuelEfficiency) {
            if ((!(((Container) te).getItem(3).isEmpty())) && (!player.isCreative())){
                Containers.dropItemStack(world, pos.getX(), pos.getY() + 1, pos.getZ(), ((Container) te).getItem(3));
            }
            ((Container) te).setItem(3, newStack);
        }
        if (hand.getItem() == Items.LAVA_BUCKET) {
            if ((!(((Container) te).getItem(0).isEmpty())) && (!player.isCreative())){
                Containers.dropItemStack(world, pos.getX(), pos.getY() + 1, pos.getZ(), ((Container) te).getItem(0));
            }
            ((Container) te).setItem(0, newStack);
        }
        if (hand.getItem() == Items.WATER_BUCKET) {
            if ((!(((Container) te).getItem(1).isEmpty())) && (!player.isCreative())){
                Containers.dropItemStack(world, pos.getX(), pos.getY() + 1, pos.getZ(), ((Container) te).getItem(1));
            }
            ((Container) te).setItem(1, newStack);
        }
        world.playSound(null, te.getBlockPos(), SoundEvents.ARMOR_EQUIP_IRON, SoundSource.BLOCKS, 1.0F, 1.0F);
        if (!player.isCreative()) {
            player.getItemInHand(handIn).shrink(1);
        }
        ((BlockEntityCobblestoneGenerator)te).onUpdateSent();
        return InteractionResult.SUCCESS;
    }
    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean p_196243_5_) {
        if (state.getBlock() != oldState.getBlock()) {
            BlockEntity te = world.getBlockEntity(pos);
            if (te instanceof BlockEntityCobblestoneGenerator) {
                Containers.dropContents(world, pos, (BlockEntityCobblestoneGenerator) te);
                world.updateNeighbourForOutputSignal(pos, this);
            }

            super.onRemove(state, world, pos, oldState, p_196243_5_);
        }
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
        builder.add(BlockStateProperties.FACING, TYPE);
    }
    @Nullable
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> p_152133_, BlockEntityType<E> p_152134_, BlockEntityTicker<? super E> p_152135_) {
        return p_152134_ == p_152133_ ? (BlockEntityTicker<A>)p_152135_ : null;
    }

    @Nullable
    protected static <T extends BlockEntity> BlockEntityTicker<T> createFurnaceTicker(Level p_151988_, BlockEntityType<T> p_151989_, BlockEntityType<? extends BlockEntityCobblestoneGenerator> p_151990_) {
        return p_151988_.isClientSide ? null : createTickerHelper(p_151989_, p_151990_, BlockEntityCobblestoneGenerator::tick);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return new BlockEntityCobblestoneGenerator.BlockEntityCobblestoneGeneratorDefinition(p_153215_, p_153216_);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createFurnaceTicker(level, type, Registration.COB_GENERATOR_TILE.get());
    }

}
