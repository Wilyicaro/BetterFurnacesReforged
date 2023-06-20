package wily.betterfurnaces.blocks;

import me.shedaniel.architectury.registry.menu.ExtendedMenuProvider;
import me.shedaniel.architectury.registry.MenuRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import wily.betterfurnaces.blockentity.CobblestoneGeneratorBlockEntity;
import wily.betterfurnaces.items.UpgradeItem;

import java.util.Collections;
import java.util.List;

public class CobblestoneGeneratorBlock extends Block implements EntityBlock {
    public static final String COBBLESTONE_GENERATOR = "cobblestone_generator";


    public static final DirectionProperty FACING = DirectionalBlock.FACING;
    public static final IntegerProperty TYPE = IntegerProperty.create("state", 0, 3);

    public CobblestoneGeneratorBlock(Properties properties) {
        super(properties.lightLevel(b->{ int s = b.getValue(TYPE);  return s == 1 || s == 3 ? 9 : 0;}));
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
        CobblestoneGeneratorBlockEntity be = (CobblestoneGeneratorBlockEntity) world.getBlockEntity(pos);

        if (world.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            if ((hand.getItem() == Items.LAVA_BUCKET) || (hand.getItem() == Items.WATER_BUCKET) || (hand.getItem() instanceof UpgradeItem && (((UpgradeItem) hand.getItem()).upgradeType == 3 || ((UpgradeItem) hand.getItem()).upgradeType == 2))) {
                interactInsert(world, pos, player, handIn, stack);
            } else this.interactWith(world, pos, player);
        }

        return InteractionResult.SUCCESS;
    }

    private void interactWith(Level world, BlockPos pos, Player player) {
        BlockEntity tileEntity = world.getBlockEntity(pos);
        if (tileEntity instanceof ExtendedMenuProvider) {
            MenuRegistry.openExtendedMenu((ServerPlayer) player, (ExtendedMenuProvider) tileEntity);
        }
    }
    private InteractionResult interactInsert(Level world, BlockPos pos, Player player, InteractionHand handIn, ItemStack stack) {
        ItemStack hand = player.getItemInHand(handIn);
        if (!((hand.getItem() == Items.LAVA_BUCKET) || (hand.getItem() == Items.WATER_BUCKET) || (hand.getItem() instanceof UpgradeItem  && (((UpgradeItem) hand.getItem()).upgradeType == 3 || ((UpgradeItem) hand.getItem()).upgradeType == 2)))){
            return InteractionResult.SUCCESS;
        }

        if (!(world.getBlockEntity(pos) instanceof CobblestoneGeneratorBlockEntity)) {
            return InteractionResult.SUCCESS;
        }
        CobblestoneGeneratorBlockEntity be = (CobblestoneGeneratorBlockEntity) world.getBlockEntity(pos);
        ItemStack newStack = new ItemStack(stack.getItem(), 1);
        newStack.setTag(stack.getTag());

        for (int i : new int[]{0,1,3,4}) {
            if(!stack.isEmpty() && be.IisItemValidForSlot(i, stack)) {
                if ((!(be.inventory.getItem(i).isEmpty())) && (!player.isCreative())) {
                    Containers.dropItemStack(world, pos.getX(), pos.getY() + 1, pos.getZ(), be.inventory.getItem(i));
                }
                be.inventory.setItem(i, newStack);
            }
        }
        world.playSound(null, be.getBlockPos(), SoundEvents.ARMOR_EQUIP_IRON, SoundSource.BLOCKS, 1.0F, 1.0F);
        if (!player.isCreative()) {
            player.getItemInHand(handIn).shrink(1);
        }
        be.onUpdateSent();
        return InteractionResult.SUCCESS;
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean p_196243_5_) {
        if (state.getBlock() != oldState.getBlock()) {
            BlockEntity be = world.getBlockEntity(pos);
            if (be instanceof CobblestoneGeneratorBlockEntity) {
                Containers.dropContents(world, pos, ((CobblestoneGeneratorBlockEntity) be).inventory);
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
        builder.add(BlockStateProperties.FACING, TYPE);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockGetter blockGetter) {
        return new CobblestoneGeneratorBlockEntity();
    }


}
