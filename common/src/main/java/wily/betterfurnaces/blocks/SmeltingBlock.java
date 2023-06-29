package wily.betterfurnaces.blocks;

import me.shedaniel.architectury.fluid.FluidStack;
import me.shedaniel.architectury.hooks.FluidStackHooks;
import me.shedaniel.architectury.registry.MenuRegistry;
import me.shedaniel.architectury.registry.menu.ExtendedMenuProvider;
import me.shedaniel.architectury.utils.Fraction;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.blockentity.SmeltingBlockEntity;
import wily.betterfurnaces.init.Registration;
import wily.betterfurnaces.items.UpgradeItem;
import wily.factoryapi.FactoryAPIPlatform;
import wily.factoryapi.ItemContainerUtil;
import wily.factoryapi.base.Bearer;
import wily.factoryapi.base.Storages;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class SmeltingBlock extends Block implements EntityBlock {

    public static final BooleanProperty COLORED = BooleanProperty.create("colored");
    // 0= Furnace, 1= Blast Furnace, 2= Smoker
    public static final IntegerProperty TYPE = IntegerProperty.create("type",0,3);

    public boolean shouldDropContent = true;

    public SmeltingBlock(Properties properties) {
        super(properties.strength(3f).lightLevel((b) -> b.getValue(BlockStateProperties.LIT) ? 14 : 0).noOcclusion().emissiveRendering(SmeltingBlock::getOrientation));
        this.registerDefaultState(this.defaultBlockState().setValue(BlockStateProperties.LIT, false).setValue(COLORED,false));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return (BlockState) this.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, ctx.getHorizontalDirection().getOpposite());
    }
    public static boolean getOrientation(BlockState state, BlockGetter world, BlockPos pos) {
        return world.getBlockEntity(pos) != null && ((SmeltingBlockEntity) (world.getBlockEntity(pos))).showOrientation;
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        ItemStack drop = new ItemStack(this.asItem());
        ItemStack stack = builder.getOptionalParameter(LootContextParams.TOOL);
        BlockEntity be = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (EnchantmentHelper.getEnchantments(stack).containsKey(Enchantments.SILK_TOUCH) && stack.isCorrectToolForDrops(state)) {
            CompoundTag tag = new CompoundTag();
            tag.put("BlockEntityTag", be.getUpdateTag());
            drop.setTag(tag);
        }
        return Collections.singletonList(drop);
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState p_180633_3_, @Nullable LivingEntity entity, ItemStack stack) {
        if (entity != null) {
            SmeltingBlockEntity be = (SmeltingBlockEntity) world.getBlockEntity(pos);
            if (stack.hasCustomHoverName()) {
                be.setCustomName(stack.getDisplayName());
            }
            be.totalCookTime = be.getCookTimeConfig();
            be.forceUpdateAllStates();
        }
    }


    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult p_225533_6_) {
        ItemStack stack = player.getItemInHand(handIn).copy();
        ItemStack hand = player.getItemInHand(handIn);
        SmeltingBlockEntity be = (SmeltingBlockEntity) world.getBlockEntity(pos);

        if (world.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            if (hand.getItem() instanceof UpgradeItem  && ((UpgradeItem)hand.getItem()).isEnabled() && !(player.isCrouching())) {
                return this.interactUpgrade(world, pos, player, handIn, stack);
            }if (ItemContainerUtil.isFluidContainer(hand) &&  !(player.isCrouching())) {
                Bearer<FluidStack> fluid = Bearer.of(FluidStack.empty());
                if ((be.hasUpgrade(Registration.GENERATOR.get()) && ItemContainerUtil.getFluid(stack).getFluid().isSame(Fluids.WATER) && ItemContainerUtil.getFluid(be.getUpgradeSlotItem(Registration.GENERATOR.get())).getAmount().longValue() <= 3 * FactoryAPIPlatform.getBucketAmount())){
                    ItemContainerUtil.ItemFluidContext context = ItemContainerUtil.fillItem(be.getUpgradeSlotItem(Registration.GENERATOR.get()), ItemContainerUtil.drainItem(FactoryAPIPlatform.getBucketAmount(), player, handIn));
                    be.inventory.setItem(be.getUpgradeTypeSlot(Registration.GENERATOR.get()), context.container);
                    fluid.set(context.fluidStack);
                }else if (be.hasUpgrade(Registration.LIQUID.get()) && SmeltingBlockEntity.isItemFuel(ItemContainerUtil.getFluid(hand).getFluid().getBucket().getDefaultInstance()))
                    be.getStorage(Storages.FLUID, null).ifPresent(e -> {if (e.getTotalSpace() > 0 && e.getFluidStack().getFluid() == ItemContainerUtil.getFluid(hand).getFluid() || e.getFluidStack().isEmpty()) fluid.set(FluidStack.create(ItemContainerUtil.getFluid(hand), Fraction.ofWhole(e.fill((ItemContainerUtil.drainItem(e.getTotalSpace(), player, handIn)), false))));});
                if (fluid.get().getAmount().longValue() > 0) return InteractionResult.SUCCESS;
            }
            this.interactWith(world, pos, player);
        }

        return InteractionResult.FAIL;
    }

    protected InteractionResult interactUpgrade(Level world, BlockPos pos, Player player, InteractionHand handIn, ItemStack stack) {
        ItemStack hand = player.getItemInHand(handIn);
        if (!(hand.getItem() instanceof UpgradeItem)){
            return InteractionResult.SUCCESS;
        }
        if (!(world.getBlockEntity(pos) instanceof SmeltingBlockEntity)) {
            return InteractionResult.SUCCESS;
        }
        SmeltingBlockEntity be = (SmeltingBlockEntity) world.getBlockEntity(pos);
        ItemStack newStack = new ItemStack(stack.getItem(), 1);
        newStack.setTag(stack.getTag());

        if (be.hasUpgradeType((UpgradeItem) stack.getItem())) {
            if (!player.isCreative())
            Containers.dropItemStack(world, pos.getX(), pos.getY() + 1, pos.getZ(), be.getUpgradeTypeSlotItem((UpgradeItem) stack.getItem()));
            else  be.getUpgradeTypeSlotItem((UpgradeItem) stack.getItem()).shrink(1);
        }
        for (int upg : be.UPGRADES()) {
            if (be.IisItemValidForSlot(upg, stack) && !stack.isEmpty()) {
                if (!(be.inventory.getItem(upg).isEmpty()) && upg == be.UPGRADES()[be.UPGRADES().length - 1]) {
                    if (!player.isCreative())
                    Containers.dropItemStack(world, pos.getX(), pos.getY() + 1, pos.getZ(), be.inventory.getItem(upg));
                    else be.inventory.getItem(upg).shrink(1);
                }
                if (be.inventory.getItem(upg).isEmpty()) {
                    be.inventory.setItem(upg, newStack);
                    if (!player.isCreative()) {
                        player.getItemInHand(handIn).shrink(1);
                    }
                    world.playSound(null, be.getBlockPos(), SoundEvents.ARMOR_EQUIP_IRON, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
            }
        }
        be.onUpdateSent();
        return InteractionResult.SUCCESS;
    }

    private void interactWith(Level world, BlockPos pos, Player player) {
        BlockEntity tileEntity = world.getBlockEntity(pos);
        if (tileEntity instanceof ExtendedMenuProvider) {
            MenuRegistry.openExtendedMenu((ServerPlayer) player, (ExtendedMenuProvider) tileEntity);
            player.awardStat(Stats.INTERACT_WITH_FURNACE);
        }
    }


    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter worldIn, List<Component> tooltip, TooltipFlag flagIn) {

        if (stack.getOrCreateTag().getInt("type") == 1)
            tooltip.add(new TranslatableComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".furnace.only", new ItemStack(Registration.BLAST.get()).getHoverName().getString()).setStyle(Style.EMPTY.applyFormat((ChatFormatting.DARK_RED))));
        else if (stack.getOrCreateTag().getInt("type") == 2)
            tooltip.add(new TranslatableComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".furnace.only", new ItemStack(Registration.SMOKE.get()).getHoverName().getString()).setStyle(Style.EMPTY.applyFormat((ChatFormatting.DARK_RED))));
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }


    public void animateTick(BlockState state, Level world, BlockPos pos, Random rand) {
        if (state.getValue(BlockStateProperties.LIT)) {
            int type = state.getValue(TYPE);
            if (type == 0  || type == 3) {
                if (world.getBlockEntity(pos) == null) {
                    return;
                }
                if (!(world.getBlockEntity(pos) instanceof SmeltingBlockEntity)) {
                    return;
                }
                {
                    double d0 = (double) pos.getX() + 0.5D;
                    double d1 = (double) pos.getY();
                    double d2 = (double) pos.getZ() + 0.5D;
                    if (rand.nextDouble() < 0.1D) {
                        world.playLocalSound(d0, d1, d2, SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F, 1.0F, false);

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
            if (type == 1) {
                double d0 = (double) pos.getX() + 0.5D;
                double d1 = (double) pos.getY();
                double d2 = (double) pos.getZ() + 0.5D;
                if (rand.nextDouble() < 0.1D) {
                    world.playLocalSound(d0, d1, d2, SoundEvents.BLASTFURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
                }

                Direction direction = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
                Direction.Axis direction$axis = direction.getAxis();
                double d3 = 0.52D;
                double d4 = rand.nextDouble() * 0.6D - 0.3D;
                double d5 = direction$axis == Direction.Axis.X ? (double) direction.getStepX() * 0.52D : d4;
                double d6 = rand.nextDouble() * 9.0D / 16.0D;
                double d7 = direction$axis == Direction.Axis.Z ? (double) direction.getStepZ() * 0.52D : d4;
                world.addParticle(ParticleTypes.SMOKE, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
            }
            if (type == 2) {
                double d0 = (double) pos.getX() + 0.5D;
                double d1 = pos.getY();
                double d2 = (double) pos.getZ() + 0.5D;
                if (rand.nextDouble() < 0.1D) {
                    world.playLocalSound(d0, d1, d2, SoundEvents.SMOKER_SMOKE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
                }

                world.addParticle(ParticleTypes.SMOKE, d0, d1 + 1.1D, d2, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos blockPos, BlockState blockState, Player player) {
        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (EnchantmentHelper.getEnchantments(stack).containsKey(Enchantments.SILK_TOUCH) && stack.isCorrectToolForDrops(blockState))
            shouldDropContent = false;
        else shouldDropContent = true;
        super.playerWillDestroy(level, blockPos, blockState, player);
    }
    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean p_196243_5_) {
        if (state.getBlock() != oldState.getBlock()) {
            if (world.getBlockEntity(pos) instanceof SmeltingBlockEntity) {
                SmeltingBlockEntity be = (SmeltingBlockEntity) world.getBlockEntity(pos);
                if (shouldDropContent) {
                    Containers.dropContents(world, pos, be.inventory);
                    be.grantStoredRecipeExperience(world, Vec3.atCenterOf(pos));
                }
                world.updateNeighbourForOutputSignal(pos, this);
            }

            super.onRemove(state, world, pos, oldState, p_196243_5_);
        }
    }
    
    public int getComparatorInputOverride(BlockState state, Level world, BlockPos pos) {
        return AbstractContainerMenu.getRedstoneSignalFromContainer(((SmeltingBlockEntity)world.getBlockEntity(pos)).inventory);

    }

    public BlockState rotate(BlockState p_185499_1_, Rotation p_185499_2_) {
        return (BlockState)p_185499_1_.setValue(BlockStateProperties.HORIZONTAL_FACING, p_185499_2_.rotate((Direction)p_185499_1_.getValue(BlockStateProperties.HORIZONTAL_FACING)));
    }

    public BlockState mirror(BlockState p_185471_1_, Mirror p_185471_2_) {
        return p_185471_1_.rotate(p_185471_2_.getRotation((Direction)p_185471_1_.getValue(BlockStateProperties.HORIZONTAL_FACING)));
    }

    private int calculateOutput(Level worldIn, BlockPos pos, BlockState state) {
        SmeltingBlockEntity tile = ((SmeltingBlockEntity)worldIn.getBlockEntity(pos));
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
        SmeltingBlockEntity furnace = ((SmeltingBlockEntity) world.getBlockEntity(pos));
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
        builder.add(BlockStateProperties.HORIZONTAL_FACING, BlockStateProperties.LIT, COLORED, TYPE);
    }


    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockGetter getter) {
        return Registry.BLOCK_ENTITY_TYPE.get(Registry.BLOCK.getKey(this)).create();
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.INVISIBLE;
    }

}
