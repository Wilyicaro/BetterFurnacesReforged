package wily.betterfurnaces.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.NetworkHooks;
import wily.betterfurnaces.items.FuelEfficiencyUpgradeItem;
import wily.betterfurnaces.items.UpgradeItem;
import wily.betterfurnaces.tileentity.AbstractCobblestoneGeneratorTileEntity;
import wily.betterfurnaces.tileentity.InventoryTileEntity;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class CobblestoneGeneratorBlock extends Block {
    public static final String COBBLESTONE_GENERATOR = "cobblestone_generator";

    public static final DirectionProperty FACING = DirectionalBlock.FACING;
    public static final IntegerProperty TYPE = IntegerProperty.create("state", 0, 3);

    public CobblestoneGeneratorBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH).setValue(TYPE, 0));
    }

    @Nullable
    @Override
    public ToolType getHarvestTool(BlockState state) {
        return ToolType.PICKAXE;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
        int s = state.getValue(TYPE);
        return s == 1 || s == 3  ? 9 : 0;
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
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
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_) {
        ItemStack stack = player.getItemInHand(handIn).copy();
        ItemStack hand = player.getItemInHand(handIn);
        if (world.isClientSide) {
            return ActionResultType.SUCCESS;
        } else {
            if ((hand.getItem() == Items.LAVA_BUCKET) || (hand.getItem() == Items.WATER_BUCKET) || (hand.getItem() instanceof UpgradeItem&& (((UpgradeItem) hand.getItem()).upgradeType == 3 || ((UpgradeItem) hand.getItem()).upgradeType == 2))) {
                interactInsert(world, pos, player, handIn, stack);
            } else this.interactWith(world, pos, player);
        }

        return ActionResultType.SUCCESS;
    }

    private void interactWith(World world, BlockPos pos, PlayerEntity player) {
        TileEntity tileEntity = world.getBlockEntity(pos);
        if (tileEntity instanceof INamedContainerProvider) {
            NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) tileEntity, tileEntity.getBlockPos());
            player.awardStat(Stats.INTERACT_WITH_FURNACE);
        }
    }

    private ActionResultType interactInsert(World world, BlockPos pos, PlayerEntity player, Hand handIn, ItemStack stack) {
        ItemStack hand = player.getItemInHand(handIn);
        if (!((hand.getItem() == Items.LAVA_BUCKET) || (hand.getItem() == Items.WATER_BUCKET) || (hand.getItem() instanceof UpgradeItem&& (((UpgradeItem) hand.getItem()).upgradeType == 3 || ((UpgradeItem) hand.getItem()).upgradeType == 2)))){
            return ActionResultType.SUCCESS;
        }

        if (!(world.getBlockEntity(pos) instanceof AbstractCobblestoneGeneratorTileEntity.CobblestoneGeneratorTileEntity)) {
            return ActionResultType.SUCCESS;
        }
        AbstractCobblestoneGeneratorTileEntity.CobblestoneGeneratorTileEntity be = (AbstractCobblestoneGeneratorTileEntity.CobblestoneGeneratorTileEntity) world.getBlockEntity(pos);

        ItemStack newStack = new ItemStack(stack.getItem(), 1);
        newStack.setTag(stack.getTag());

        for (int i : new int[]{0,1,3,4}) {
            if(!stack.isEmpty() && be.IisItemValidForSlot(i, stack)) {
                if ((!(((IInventory) be).getItem(i).isEmpty())) && (!player.isCreative())) {
                    InventoryHelper.dropItemStack(world, pos.getX(), pos.getY() + 1, pos.getZ(), ((IInventory) be).getItem(i));
                }
                ((IInventory) be).setItem(i, newStack);
            }
        }
        world.playSound(null, be.getBlockPos(), SoundEvents.ARMOR_EQUIP_IRON, SoundCategory.BLOCKS, 1.0F, 1.0F);
        if (!player.isCreative()) {
            player.getItemInHand(handIn).shrink(1);
        }
        be.onUpdateSent();
        return ActionResultType.SUCCESS;
    }

    @Override
    public void onRemove(BlockState state, World world, BlockPos pos, BlockState oldState, boolean p_196243_5_) {
        if (state.getBlock() != oldState.getBlock()) {
            TileEntity te = world.getBlockEntity(pos);
            if (te instanceof AbstractCobblestoneGeneratorTileEntity) {
                InventoryHelper.dropContents(world, pos, (AbstractCobblestoneGeneratorTileEntity) te);
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
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.FACING, TYPE);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new AbstractCobblestoneGeneratorTileEntity.CobblestoneGeneratorTileEntity();
    }

}
