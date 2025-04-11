package wily.betterfurnaces.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
//? if >=1.20.5 {
//? if <=1.21.1 {
/*import net.minecraft.world.ItemInteractionResult;
*///?}
//?}
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
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import wily.betterfurnaces.blockentity.CobblestoneGeneratorBlockEntity;
import wily.betterfurnaces.blockentity.SmeltingBlockEntity;
import wily.betterfurnaces.items.UpgradeItem;

public class CobblestoneGeneratorBlock extends BFRBlock implements EntityBlock {
    public static final String COBBLESTONE_GENERATOR = "cobblestone_generator";


    public static final EnumProperty<Direction> FACING = DirectionalBlock.FACING;
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
    public InteractionResult /*? if >=1.20.5 {*/useWithoutItem/*?} else {*//*use*//*?}*/(BlockState state, Level level, BlockPos pos, Player player/*? if <1.20.5 {*//*, InteractionHand hand*//*?}*/, BlockHitResult blockHitResult) {

        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            //? if <1.20.5 {
            /*if (interactInsert(level, pos, player, hand)) return InteractionResult.CONSUME;
            *///?}
            this.openBlockEntityMenu(level, pos, player);
        }

        return InteractionResult.CONSUME;
    }

    //? if >=1.20.5 {
    @Override
    protected /*? if >=1.21.2 {*/InteractionResult/*?} else {*//*ItemInteractionResult*//*?}*/ useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (interactInsert(level, blockPos, player, interactionHand)) return /*? if >=1.21.2 {*/InteractionResult.SUCCESS/*?} else {*//*ItemInteractionResult.sidedSuccess(level.isClientSide())*//*?}*/;
        return super.useItemOn(itemStack, blockState, level, blockPos, player, interactionHand, blockHitResult);
    }
    //?}

    protected boolean interactInsert(Level level, BlockPos pos, Player player, InteractionHand hand) {
        ItemStack handItem = player.getItemInHand(hand);
        if (!((handItem.getItem() == Items.LAVA_BUCKET) || (handItem.getItem() == Items.WATER_BUCKET) || (handItem.getItem() instanceof UpgradeItem upg && (upg.upgradeType == UpgradeItem.Type.ORE || upg.upgradeType == UpgradeItem.Type.FUEL)))){
            return false;
        }

        if (!(level.getBlockEntity(pos) instanceof CobblestoneGeneratorBlockEntity be)) {
            return false;
        }
        ItemStack newStack = handItem.copyWithCount(1);

        for (int i : new int[]{0,1,3,4}) {
            if(!handItem.isEmpty() && be.getSlots().get(i).mayPlace(handItem)) {
                if ((!(be.inventory.getItem(i).isEmpty())) && (!player.isCreative())) {
                    Containers.dropItemStack(level, pos.getX(), pos.getY() + 1, pos.getZ(), be.inventory.getItem(i));
                }
                be.inventory.setItem(i, newStack);
            }
        }
        level.playSound(null, be.getBlockPos(), SoundEvents.ARMOR_EQUIP_IRON/*? if >=1.20.5 {*/.value()/*?}*/, SoundSource.BLOCKS, 1.0F, 1.0F);
        if (!player.isCreative()) {
            player.getItemInHand(hand).shrink(1);
        }
        be.onUpdateSent();
        return true;
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
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return new CobblestoneGeneratorBlockEntity(p_153215_, p_153216_);
    }
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return (level1, pos, state1, tile) -> {
            if (tile instanceof CobblestoneGeneratorBlockEntity be) {
                be.tick(state1);
            }
        };
    }

}
