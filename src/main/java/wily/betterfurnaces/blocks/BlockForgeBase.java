package wily.betterfurnaces.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fmllegacy.network.NetworkHooks;
import wily.betterfurnaces.init.Registration;
import wily.betterfurnaces.items.*;
import wily.betterfurnaces.tileentity.BlockForgeTileBase;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public abstract class BlockForgeBase extends Block implements SimpleWaterloggedBlock, EntityBlock {

    public static final DirectionProperty FACING = DirectionalBlock.FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty COLORED = BooleanProperty.create("colored");

    public BlockForgeBase(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.SOUTH).setValue(BlockStateProperties.LIT, false).setValue(WATERLOGGED, false).setValue(COLORED,false));
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter world, BlockPos pos) {
        return state.getValue(BlockStateProperties.LIT) ? 14 : 0;
    }
    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        Direction facing = ctx.getClickedFace();
        if (facing == Direction.WEST || facing == Direction.EAST)
            facing = Direction.UP;
        else if (facing == Direction.NORTH || facing == Direction.SOUTH)
            facing = Direction.EAST;
        else
            facing = Direction.SOUTH;
        boolean flag = ctx.getLevel().getFluidState(ctx.getClickedPos()).getType() == Fluids.WATER;;
        return this.defaultBlockState().setValue(FACING, facing).setValue(WATERLOGGED, flag);
    }
    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        List<ItemStack> dropsOriginal = super.getDrops(state, builder);
        if (!dropsOriginal.isEmpty())
            return dropsOriginal;
        return Collections.singletonList(new ItemStack(this, 1));
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState p_180633_3_, @Nullable LivingEntity entity, ItemStack stack) {
        if (entity != null) {
            BlockForgeTileBase te = (BlockForgeTileBase) world.getBlockEntity(pos);
            if (stack.hasCustomHoverName()) {
                te.setCustomName(stack.getDisplayName());
            }
            te.totalCookTime = te.getCookTimeConfig().get();
            te.placeConfig();
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult p_225533_6_) {
        ItemStack stack = player.getItemInHand(handIn).copy();
        ItemStack hand = player.getItemInHand(handIn);
        BlockForgeTileBase te = (BlockForgeTileBase) world.getBlockEntity(pos);

        if (world.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            if ((((hand.getItem() instanceof ItemUpgradeMisc && !(hand.getItem() instanceof ItemXpTank)) || hand.getItem() instanceof ItemOreProcessing || hand.getItem() instanceof ItemFuelEfficiency || (hand.getItem() instanceof ItemLiquidFuel && !te.hasXPTank()) || hand.getItem() instanceof ItemEnergyFuel || (hand.getItem() == Registration.XP.get() && !te.isLiquid())) && !(player.isCrouching()))) {
                return this.interactUpgrade(world, pos, player, handIn, stack);
            }else if ((te.inventory.get(10).getItem() == new ItemStack(Registration.LIQUID.get()).getItem())  && hand.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).isPresent() &&  !(player.isCrouching())){
                FluidStack fluid = hand.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).resolve().get().getFluidInTank(1);
                FluidActionResult res = FluidUtil.tryEmptyContainer(hand, te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY).resolve().get(), 1000, player, true);
                if (fluid != null && ForgeHooks.getBurnTime(hand, RecipeType.SMELTING) > 0)
                    if (res.isSuccess()) {
                        world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BUCKET_FILL_LAVA, SoundSource.PLAYERS, 0.6F, 0.8F);
                        if (!player.isCreative()) player.setItemInHand(handIn, res.result);
                    }
            }else {
                this.interactWith(world, pos, player);
            }
            return InteractionResult.SUCCESS;
        }
    }

    private InteractionResult interactUpgrade(Level world, BlockPos pos, Player player, InteractionHand handIn, ItemStack stack) {
        Item hand = player.getItemInHand(handIn).getItem();
        if (!((hand instanceof ItemOreProcessing) || (hand instanceof ItemFuelEfficiency) || (hand instanceof ItemUpgradeMisc) || (hand instanceof ItemLiquidFuel) || (hand instanceof ItemEnergyFuel))){
            return InteractionResult.SUCCESS;
        }
        BlockEntity te = world.getBlockEntity(pos);
        if (!(te instanceof BlockForgeTileBase)) {
            return InteractionResult.SUCCESS;
        }
        ItemStack newStack = new ItemStack(stack.getItem(), 1);
        newStack.setTag(stack.getTag());

        if (hand instanceof ItemOreProcessing && hand != ((Container) te).getItem(7).getItem()) {
                if ((!(((Container) te).getItem(7).isEmpty())) && (!player.isCreative())) {
                    Containers.dropItemStack(world, pos.getX(), pos.getY() + 1, pos.getZ(), ((Container) te).getItem(7));
                }
                    ((Container) te).setItem(7, newStack);

        }
        if (hand instanceof ItemFuelEfficiency) {
            if ((!(((Container) te).getItem(8).isEmpty())) && (!player.isCreative())) {
                Containers.dropItemStack(world, pos.getX(), pos.getY() + 1, pos.getZ(), ((Container) te).getItem(8));
            }
            ((Container) te).setItem(8, newStack);
        }
        if (hand == Registration.XP.get()) {
            if ((!(((Container) te).getItem(9).isEmpty())) && (!player.isCreative())) {
                Containers.dropItemStack(world, pos.getX(), pos.getY() + 1, pos.getZ(), ((Container) te).getItem(9));
            }
            ((Container) te).setItem(9, newStack);
        }
        if (hand == Registration.FACTORY.get()) {
            if ((!(((Container) te).getItem(11).isEmpty())) && (!player.isCreative())) {
                Containers.dropItemStack(world, pos.getX(), pos.getY() + 1, pos.getZ(), ((Container) te).getItem(11));
            }
            ((Container) te).setItem(11, newStack);

        }
        if (hand instanceof ItemColorUpgrade) {
            if ((!(((Container) te).getItem(12).isEmpty())) && (!player.isCreative())) {
                Containers.dropItemStack(world, pos.getX(), pos.getY() + 1, pos.getZ(), ((Container) te).getItem(12));
            }
            ((Container) te).setItem(12, newStack);

        }
        if (hand instanceof ItemLiquidFuel || hand instanceof ItemEnergyFuel) {
            if ((!(((Container) te).getItem(10).isEmpty())) && (!player.isCreative())) {
                Containers.dropItemStack(world, pos.getX(), pos.getY() + 1, pos.getZ(), ((Container) te).getItem(10));
            }
            ((Container) te).setItem(10, newStack);
        }
        world.playSound(null, te.getBlockPos(), SoundEvents.ARMOR_EQUIP_IRON, SoundSource.BLOCKS, 1.0F, 1.0F);
        if (!player.isCreative()) {
            player.getItemInHand(handIn).shrink(1);
        }
        ((BlockForgeTileBase)te).onUpdateSent();
        return InteractionResult.SUCCESS;
    }
    private void interactWith(Level world, BlockPos pos, Player player) {
        BlockEntity tileEntity = world.getBlockEntity(pos);
        if (tileEntity instanceof MenuProvider) {
            NetworkHooks.openGui((ServerPlayer) player, (MenuProvider) tileEntity, tileEntity.getBlockPos());
            player.awardStat(Stats.INTERACT_WITH_FURNACE);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, Random rand) {
        if (state.getValue(BlockStateProperties.LIT)) {
            if (world.getBlockEntity(pos) == null)
            {
                return;
            }
            if (!(world.getBlockEntity(pos) instanceof BlockForgeTileBase))
            {
                return;
            }
            BlockForgeTileBase tile = ((BlockForgeTileBase) world.getBlockEntity(pos));

            if (state.getValue(BlockStateProperties.FACING) == Direction.SOUTH){
                double d0 = (double) pos.getX() + 0.5D;
                double d1 = (double) pos.getY() + rand.nextDouble() * 6.0D / 16.0D;
                double d2 = (double) pos.getZ() + 0.5D;
                double d4 = rand.nextDouble() * 0.6D - 0.3D;
                if (rand.nextDouble() < 0.1D) {
                    world.playLocalSound(d0, pos.getY(), d2, SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
                }
                world.addParticle(ParticleTypes.SMOKE, d0 - 0.52D, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
                world.addParticle(ParticleTypes.SOUL_FIRE_FLAME, d0 - 0.52D, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
                world.addParticle(ParticleTypes.SMOKE, d0 + 0.52D, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
                world.addParticle(ParticleTypes.SOUL_FIRE_FLAME, d0 + 0.52D, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
                world.addParticle(ParticleTypes.SMOKE, d0 + d4, d1, d2 - 0.52D, 0.0D, 0.0D, 0.0D);
                world.addParticle(ParticleTypes.SOUL_FIRE_FLAME, d0 + d4, d1, d2 - 0.52D, 0.0D, 0.0D, 0.0D);
                world.addParticle(ParticleTypes.SMOKE, d0 + d4, d1, d2 + 0.52D, 0.0D, 0.0D, 0.0D);
                world.addParticle(ParticleTypes.SOUL_FIRE_FLAME, d0 + d4, d1, d2 + 0.52D, 0.0D, 0.0D, 0.0D);
            }else{
                for (int l = 0; l < 3; ++l) {
                    double d0 = (pos.getX() + rand.nextFloat());
                    double d1 = (pos.getY() + rand.nextFloat());
                    double d2 = (pos.getZ() + rand.nextFloat());
                    int i1 = rand.nextInt(2) * 2 - 1;
                    double d3 = (rand.nextFloat() - 0.5D) * 0.2D;
                    double d4 = (rand.nextFloat() - 0.5D) * 0.2D;
                    double d5 = (rand.nextFloat() - 0.5D) * 0.2D;
                    world.addParticle(ParticleTypes.DRIPPING_LAVA, d0, d1, d2, d3, d4, d5);

                }
            }
        }
    }

    public void onReplaced(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean p_196243_5_) {

    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean p_196243_5_) {
        if (state.getBlock() != oldState.getBlock()) {
            BlockEntity te = world.getBlockEntity(pos);
            if (te instanceof BlockForgeTileBase) {
                Containers.dropContents(world, pos, (BlockForgeTileBase) te);
                ((BlockForgeTileBase)te).grantStoredRecipeExperience(world, Vec3.atCenterOf(pos));
                world.updateNeighbourForOutputSignal(pos, this);
            }

            super.onRemove(state, world, pos, oldState, p_196243_5_);
        }
    }

    public boolean hasComparatorInputOverride(BlockState p_149740_1_) {
        return true;
    }

    public int getComparatorInputOverride(BlockState state, Level world, BlockPos pos) {
        return AbstractContainerMenu.getRedstoneSignalFromContainer((Container) world.getBlockEntity(pos));

    }

    public RenderShape getRenderType(BlockState p_149645_1_) {
        return RenderShape.MODEL;
    }

    public BlockState rotate(BlockState p_185499_1_, Rotation p_185499_2_) {
        if (p_185499_2_ == Rotation.CLOCKWISE_90 || p_185499_2_ == Rotation.COUNTERCLOCKWISE_90) {
            if ((Direction) p_185499_1_.getValue(FACING) == Direction.WEST || (Direction) p_185499_1_.getValue(FACING) == Direction.EAST) {
                return p_185499_1_.setValue(FACING, Direction.UP);
            } else if ((Direction) p_185499_1_.getValue(FACING) == Direction.UP || (Direction)p_185499_1_.getValue(FACING) == Direction.DOWN) {
                return p_185499_1_.setValue(FACING, Direction.WEST);
            }
        }
        return p_185499_1_;
    }

    private int calculateOutput(Level worldIn, BlockPos pos, BlockState state) {
        BlockForgeTileBase tile = ((BlockForgeTileBase)worldIn.getBlockEntity(pos));
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
    public int getSignal(BlockState p_180656_1_, BlockGetter p_180656_2_, BlockPos p_180656_3_, Direction p_180656_4_) {
        return super.getDirectSignal(p_180656_1_, p_180656_2_, p_180656_3_, p_180656_4_);
    }

    @Override
    public int getDirectSignal(BlockState blockState, BlockGetter world, BlockPos pos, Direction direction) {
        BlockForgeTileBase furnace = ((BlockForgeTileBase) world.getBlockEntity(pos));
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
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.FACING, WATERLOGGED, BlockStateProperties.LIT, COLORED);
    }
    @Nullable
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> p_152133_, BlockEntityType<E> p_152134_, BlockEntityTicker<? super E> p_152135_) {
        return p_152134_ == p_152133_ ? (BlockEntityTicker<A>)p_152135_ : null;
    }

    @Nullable
    protected static <T extends BlockEntity> BlockEntityTicker<T> createFurnaceTicker(Level p_151988_, BlockEntityType<T> p_151989_, BlockEntityType<? extends BlockForgeTileBase> p_151990_) {
        return p_151988_.isClientSide ? null : createTickerHelper(p_151989_, p_151990_, BlockForgeTileBase::tick);
    }

}
