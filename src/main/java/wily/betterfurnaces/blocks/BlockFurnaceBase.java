package wily.betterfurnaces.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.network.NetworkHooks;
import wily.betterfurnaces.init.Registration;
import wily.betterfurnaces.items.ItemFuelEfficiency;
import wily.betterfurnaces.items.ItemLiquidFuel;
import wily.betterfurnaces.items.ItemOreProcessing;
import wily.betterfurnaces.items.ItemUpgradeMisc;
import wily.betterfurnaces.tileentity.BlockSmeltingTileBase;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public abstract class BlockFurnaceBase extends Block {

    public static final BooleanProperty COLORED = BooleanProperty.create("colored");

    public BlockFurnaceBase(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(BlockStateProperties.LIT, false).setValue(COLORED,false));
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
        return state.getValue(BlockStateProperties.LIT) ? 14 : 0;
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext ctx) {
        return (BlockState) this.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, ctx.getHorizontalDirection().getOpposite());
    }
    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        List<ItemStack> dropsOriginal = super.getDrops(state, builder);
        if (!dropsOriginal.isEmpty())
            return dropsOriginal;
        return Collections.singletonList(new ItemStack(this, 1));
    }

    @Override
    public void setPlacedBy(World world, BlockPos pos, BlockState p_180633_3_, @Nullable LivingEntity entity, ItemStack stack) {
        if (entity != null) {
            BlockSmeltingTileBase te = (BlockSmeltingTileBase) world.getBlockEntity(pos);
            if (stack.hasCustomHoverName()) {
                te.setCustomName(stack.getDisplayName());
            }
            te.totalCookTime = te.getCookTimeConfig().get();
            te.placeConfig();
        }
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_) {
        ItemStack stack = player.getItemInHand(handIn).copy();
        ItemStack hand = player.getItemInHand(handIn);
        BlockSmeltingTileBase te = (BlockSmeltingTileBase) world.getBlockEntity(pos);

        if (world.isClientSide) {
            return ActionResultType.SUCCESS;
        } else {
            if ((hand.getItem() instanceof ItemUpgradeMisc || hand.getItem() instanceof ItemOreProcessing || hand.getItem() instanceof ItemFuelEfficiency || hand.getItem() instanceof ItemLiquidFuel) && !(player.isCrouching())) {
                return this.interactUpgrade(world, pos, player, handIn, stack);
            }else if ((te.inventory.get(5).getItem() == new ItemStack(Registration.LIQUID.get()).getItem())  && hand.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).isPresent() &&  !(player.isCrouching())){
                FluidStack fluid = hand.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).resolve().get().getFluidInTank(1);
                FluidActionResult res = FluidUtil.tryEmptyContainer(hand, te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY).resolve().get(), 1000, player, true);
                if (fluid != null && ForgeHooks.getBurnTime(hand) > 0)
                    if (res.isSuccess()) {
                        world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BUCKET_FILL_LAVA, SoundCategory.PLAYERS, 0.6F, 0.8F);
                        if (!player.isCreative()) player.setItemInHand(handIn, res.result);
                    }
            }else {
                this.interactWith(world, pos, player);
            }
            return ActionResultType.SUCCESS;
        }
    }

    private ActionResultType interactUpgrade(World world, BlockPos pos, PlayerEntity player, Hand handIn, ItemStack stack) {
        ItemStack hand = player.getItemInHand(handIn);
        if (!((hand.getItem() instanceof ItemOreProcessing) || (hand.getItem() instanceof ItemFuelEfficiency) || (hand.getItem() instanceof ItemUpgradeMisc) || (hand.getItem() instanceof ItemLiquidFuel))){
            return ActionResultType.SUCCESS;
        }
        TileEntity te = world.getBlockEntity(pos);
        if (!(te instanceof BlockSmeltingTileBase)) {
            return ActionResultType.SUCCESS;
        }
        ItemStack newStack = new ItemStack(stack.getItem(), 1);
        newStack.setTag(stack.getTag());

        if (player.getItemInHand(handIn).getItem() instanceof ItemOreProcessing) {
            if ((!(((IInventory) te).getItem(3).isEmpty())) && (!player.isCreative())){
                InventoryHelper.dropItemStack(world, pos.getX(), pos.getY() + 1, pos.getZ(), ((IInventory) te).getItem(3));
            }
            ((IInventory) te).setItem(3, newStack);
        }
        if (player.getItemInHand(handIn).getItem() instanceof ItemFuelEfficiency) {
            if ((!(((IInventory) te).getItem(4).isEmpty())) && (!player.isCreative())) {
                InventoryHelper.dropItemStack(world, pos.getX(), pos.getY() + 1, pos.getZ(), ((IInventory) te).getItem(4));
            }
            ((IInventory) te).setItem(4, newStack);
        }
        if (player.getItemInHand(handIn).getItem() instanceof ItemUpgradeMisc || player.getItemInHand(handIn).getItem() instanceof ItemLiquidFuel) {
            if ((!(((IInventory) te).getItem(5).isEmpty())) && (!player.isCreative())) {
                    InventoryHelper.dropItemStack(world, pos.getX(), pos.getY() + 1, pos.getZ(), ((IInventory) te).getItem(5));
            }
            ((IInventory) te).setItem(5, stack.copy());
        }
        world.playSound(null, te.getBlockPos(), SoundEvents.ARMOR_EQUIP_IRON, SoundCategory.BLOCKS, 1.0F, 1.0F);
        if (!player.isCreative()) {
            player.getItemInHand(handIn).shrink(1);
        }
        ((BlockSmeltingTileBase)te).onUpdateSent();
        return ActionResultType.SUCCESS;
    }

    private void interactWith(World world, BlockPos pos, PlayerEntity player) {
        TileEntity tileEntity = world.getBlockEntity(pos);
        if (tileEntity instanceof INamedContainerProvider) {
            NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) tileEntity, tileEntity.getBlockPos());
            player.awardStat(Stats.INTERACT_WITH_FURNACE);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState state, World world, BlockPos pos, Random rand) {
        if (state.getValue(BlockStateProperties.LIT)) {
            if (world.getBlockEntity(pos) == null)
            {
                return;
            }
            if (!(world.getBlockEntity(pos) instanceof BlockSmeltingTileBase))
            {
                return;
            }
            {
                double d0 = (double) pos.getX() + 0.5D;
                double d1 = (double) pos.getY();
                double d2 = (double) pos.getZ() + 0.5D;
                if (rand.nextDouble() < 0.1D) {
                    world.playLocalSound(d0, d1, d2, SoundEvents.FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);

                }

                Direction direction = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
                Direction.Axis direction$axis = direction.getAxis();
                double d3 = 0.52D;
                double d4 = rand.nextDouble() * 0.6D - 0.3D;
                double d5 = direction$axis == Direction.Axis.X ? (double) direction.getStepX() * 0.52D : d4;
                double d6 = rand.nextDouble() * 6.0D / 16.0D;
                double d7 = direction$axis == Direction.Axis.Z ? (double) direction.getStepZ() * 0.52D : d4;
                world.addParticle(ParticleTypes.SMOKE, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
                world.addParticle(ParticleTypes.FLAME, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);

            }
        }
    }

    public void onReplaced(BlockState state, World world, BlockPos pos, BlockState oldState, boolean p_196243_5_) {

    }

    @Override
    public void onRemove(BlockState state, World world, BlockPos pos, BlockState oldState, boolean p_196243_5_) {
        if (state.getBlock() != oldState.getBlock()) {
            TileEntity te = world.getBlockEntity(pos);
            if (te instanceof BlockSmeltingTileBase) {
                InventoryHelper.dropContents(world, pos, (BlockSmeltingTileBase) te);
                ((BlockSmeltingTileBase)te).grantStoredRecipeExperience(world, Vector3d.atCenterOf(pos));
                world.updateNeighbourForOutputSignal(pos, this);
            }

            super.onRemove(state, world, pos, oldState, p_196243_5_);
        }
    }

    public boolean hasComparatorInputOverride(BlockState p_149740_1_) {
        return true;
    }

    public int getComparatorInputOverride(BlockState state, World world, BlockPos pos) {
        return Container.getRedstoneSignalFromContainer((IInventory) world.getBlockEntity(pos));

    }

    public BlockRenderType getRenderType(BlockState p_149645_1_) {
        return BlockRenderType.MODEL;
    }

    public BlockState rotate(BlockState p_185499_1_, Rotation p_185499_2_) {
        return (BlockState)p_185499_1_.setValue(BlockStateProperties.HORIZONTAL_FACING, p_185499_2_.rotate((Direction)p_185499_1_.getValue(BlockStateProperties.HORIZONTAL_FACING)));
    }

    public BlockState mirror(BlockState p_185471_1_, Mirror p_185471_2_) {
        return p_185471_1_.rotate(p_185471_2_.getRotation((Direction)p_185471_1_.getValue(BlockStateProperties.HORIZONTAL_FACING)));
    }

    private int calculateOutput(World worldIn, BlockPos pos, BlockState state) {
        BlockSmeltingTileBase tile = ((BlockSmeltingTileBase)worldIn.getBlockEntity(pos));
        int i = this.getComparatorInputOverride(state, worldIn, pos);
        if (tile != null)
        {
            int j = tile.furnaceSettings.get(9);
            return tile.furnaceSettings.get(8) == 4 ? Math.max(i - j, 0) : i;
        }
        return 0;
    }

    @Override
    public boolean isSignalSource(BlockState p_149744_1_) {
        return true;
    }

    @Override
    public int getSignal(BlockState p_180656_1_, IBlockReader p_180656_2_, BlockPos p_180656_3_, Direction p_180656_4_) {
        return super.getDirectSignal(p_180656_1_, p_180656_2_, p_180656_3_, p_180656_4_);
    }

    @Override
    public int getDirectSignal(BlockState blockState, IBlockReader world, BlockPos pos, Direction direction) {
        BlockSmeltingTileBase furnace = ((BlockSmeltingTileBase) world.getBlockEntity(pos));
        if (furnace != null)
        {
            int mode = furnace.furnaceSettings.get(8);
            if (mode == 0)
            {
                return 0;
            }
            else if (mode == 1)
            {
                return 0;
            }
            else if (mode == 2)
            {
                return 0;
            }
            else
            {
                return calculateOutput(furnace.getLevel(), pos, blockState);
            }
        }
        return 0;
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.HORIZONTAL_FACING, BlockStateProperties.LIT, COLORED);
    }

}
