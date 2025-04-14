package wily.betterfurnaces.blocks;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
//? if >=1.20.5 {
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemContainerContents;
//? if <=1.21.1 {
/*import net.minecraft.world.ItemInteractionResult;
*///?}
//?}
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.Enchantments;
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
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.blockentity.SmeltingBlockEntity;
import wily.betterfurnaces.init.BlockEntityTypes;
import wily.betterfurnaces.init.ModObjects;
import wily.betterfurnaces.items.ColorUpgradeItem;
import wily.betterfurnaces.items.LiquidFuelUpgradeItem;
import wily.betterfurnaces.items.UpgradeItem;
import wily.factoryapi.FactoryAPIPlatform;
import wily.factoryapi.ItemContainerPlatform;
import wily.factoryapi.base.Bearer;
import wily.factoryapi.base.FactoryStorage;
import wily.factoryapi.util.CompoundTagUtil;
import wily.factoryapi.util.FactoryItemUtil;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SmeltingBlock extends BFRBlock implements EntityBlock {

    public static final BooleanProperty COLORED = BooleanProperty.create("colored");
    // 0 = Furnace, 1 = Blast Furnace, 2 = Smoker
    public static final IntegerProperty TYPE = IntegerProperty.create("type",0,3);

    public final Tier tier;

    public SmeltingBlock(Properties properties, Tier tier) {
        super(properties.destroyTime(3f).lightLevel((b) -> b.getValue(BlockStateProperties.LIT) ? 14 : 0).noOcclusion().emissiveRendering(SmeltingBlock::getOrientation));
        this.registerDefaultState(this.defaultBlockState().setValue(BlockStateProperties.LIT, false).setValue(COLORED,false));
        this.tier = tier;
    }

    public record Tier(String name, Supplier<Integer> defaultCookTime){
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, ctx.getHorizontalDirection().getOpposite());
    }
    public static boolean getOrientation(BlockState state, BlockGetter world, BlockPos pos) {
        return world.getBlockEntity(pos) != null && ((SmeltingBlockEntity) (world.getBlockEntity(pos))).showOrientation;
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        ItemStack drop = new ItemStack(this.asItem());
        ItemStack stack = builder.getOptionalParameter(LootContextParams.TOOL);
        SmeltingBlockEntity be = (SmeltingBlockEntity) builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (FactoryItemUtil.getEnchantmentLevel(stack, Enchantments.SILK_TOUCH, builder.getLevel().registryAccess()) > 0 && stack.isCorrectToolForDrops(state)) {
            ItemStack colorUpgrade = be.getUpgradeStack(ModObjects.COLOR.get());
            //? if <1.20.5 {
            /*CompoundTag tag = new CompoundTag();
            if (!colorUpgrade.isEmpty()) ColorUpgradeItem.putColor(tag, colorUpgrade.getOrCreateTag().getInt("red"), colorUpgrade.getOrCreateTag().getInt("green"), colorUpgrade.getOrCreateTag().getInt("blue"));
            tag.put("BlockEntityTag", be.getUpdateTag());
            drop.setTag(tag);
            *///?} else {
            if (!colorUpgrade.isEmpty()) drop.set(ModObjects.BLOCK_TINT.get(), colorUpgrade.getOrDefault(ModObjects.BLOCK_TINT.get(), ModObjects.BlockTint.WHITE));
            BlockItem.setBlockEntityData(drop, be.getType(), be.getUpdateTag(be.getLevel().registryAccess()));
            drop.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(be.inventory.getItems()));
            //?}
        }
        return Collections.singletonList(drop);
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState p_180633_3_, @Nullable LivingEntity entity, ItemStack stack) {
        if (entity != null) {
            SmeltingBlockEntity be = (SmeltingBlockEntity) world.getBlockEntity(pos);
            //? if <1.20.5 {
            /*if (FactoryItemUtil.hasCustomName(stack)) {
                be.setCustomName(stack.getHoverName());
            }
            *///?}
            be.forceUpdateAllStates();
        }
    }

    @Override
    public InteractionResult /*? if >=1.20.5 {*/useWithoutItem/*?} else {*//*use*//*?}*/(BlockState state, Level level, BlockPos pos, Player player/*? if <1.20.5 {*//*, InteractionHand hand*//*?}*/, BlockHitResult blockHitResult) {
        SmeltingBlockEntity be = (SmeltingBlockEntity) level.getBlockEntity(pos);

        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            //? if <1.20.5 {
            /*if (interactItem(be, level, pos, player, hand, player.getItemInHand(hand))) return InteractionResult.CONSUME;
            *///?}
            if (openBlockEntityMenu(level, pos, player)) {
                player.awardStat(be.getInteractStat());
            }
        }

        return InteractionResult.CONSUME;
    }

    //? if >=1.20.5 {
    @Override
    protected /*? if >=1.21.2 {*/InteractionResult/*?} else {*//*ItemInteractionResult*//*?}*/ useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        SmeltingBlockEntity be = (SmeltingBlockEntity) level.getBlockEntity(blockPos);
        if (interactItem(be, level, blockPos, player, interactionHand, player.getItemInHand(interactionHand))) return /*? if >=1.21.2 {*/InteractionResult.SUCCESS/*?} else {*//*ItemInteractionResult.sidedSuccess(level.isClientSide())*//*?}*/;
        return super.useItemOn(itemStack, blockState, level, blockPos, player, interactionHand, blockHitResult);
    }
    //?}

    protected boolean interactItem(SmeltingBlockEntity be, Level level, BlockPos pos, Player player, InteractionHand handIn, ItemStack handItem){
        if (handItem.getItem() instanceof UpgradeItem upg && upg.isValid(be) && !(player.isCrouching())) {
            this.interactUpgrade(be, level, pos, player, handIn, handItem);
            return true;
        }if (ItemContainerPlatform.isFluidContainer(handItem) && !(player.isCrouching())) {
            Bearer<Integer> fluidAmount = Bearer.of(0);
            if ((be.hasUpgrade(ModObjects.GENERATOR.get()) && ItemContainerPlatform.getFluid(handItem).getFluid().isSame(Fluids.WATER) && ItemContainerPlatform.getFluid(be.getUpgradeSlotItem(ModObjects.GENERATOR.get())).getAmount() <= 3000)){
                fluidAmount.set(FactoryAPIPlatform.getItemFluidHandler(be.getUpgradeSlotItem(ModObjects.GENERATOR.get())).fill(ItemContainerPlatform.drainItem(1000, player, handIn),false));
            } else if (be.hasUpgrade(ModObjects.LIQUID.get()) && LiquidFuelUpgradeItem.supportsFluid(ItemContainerPlatform.getFluid(handItem).getFluid()))
                be.getStorage(FactoryStorage.FLUID, null).ifPresent(e -> {if (e.getTotalSpace() > 0 && e.getFluidInstance().isFluidEqual(ItemContainerPlatform.getFluid(handItem)) || e.getFluidInstance().isEmpty()) fluidAmount.set(e.fill((ItemContainerPlatform.drainItem(e.getTotalSpace(), player, handIn)), false));});
            return fluidAmount.get() > 0;
        }
        return false;
    }


    protected void interactUpgrade(SmeltingBlockEntity be, Level level, BlockPos pos, Player player, InteractionHand handIn, ItemStack handItem) {
        if (be.hasUpgradeFromSameType((UpgradeItem) handItem.getItem())) {
            if (!player.isCreative()) Containers.dropItemStack(level, pos.getX(), pos.getY() + 1, pos.getZ(), be.getUpgradeStack((UpgradeItem) handItem.getItem()));
            else be.getUpgradeStack((UpgradeItem) handItem.getItem()).shrink(1);
        }
        for (int upg : be.getUpgradeIndexes()) {
            if (be.getSlots().get(upg).mayPlace(handItem) && !handItem.isEmpty()) {
                if (!(be.inventory.getItem(upg).isEmpty()) && upg == be.getUpgradeIndexes()[be.getUpgradeIndexes().length - 1]) {
                    if (!player.isCreative()) Containers.dropItemStack(level, pos.getX(), pos.getY() + 1, pos.getZ(), be.inventory.getItem(upg));
                    else be.inventory.getItem(upg).shrink(1);
                }
                if (be.inventory.getItem(upg).isEmpty()) {
                    be.inventory.setItem(upg, handItem.copyWithCount(1));
                    if (!player.isCreative()) {
                        player.getItemInHand(handIn).shrink(1);
                    }
                    level.playSound(null, be.getBlockPos(), SoundEvents.ARMOR_EQUIP_IRON/*? if >=1.20.5 {*/.value()/*?}*/, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
            }
        }
        be.onUpdateSent();
    }


    public void appendHoverText(ItemStack stack, Consumer<Component> tooltip, TooltipFlag flagIn) {
        CompoundTag tag = /*? if <1.20.5 {*//*stack.getTag()*//*?} else {*/ stack.has(DataComponents.CUSTOM_DATA) ? stack.get(DataComponents.CUSTOM_DATA).copyTag() : null/*?}*/;
        if (tag != null) {
            if (CompoundTagUtil.getInt(tag,"type").orElse(0) == 1)
                tooltip.accept(Component.translatable("tooltip." + BetterFurnacesReforged.MOD_ID + ".furnace.only", ModObjects.BLAST.get().getName(ItemStack.EMPTY)).setStyle(Style.EMPTY.applyFormat((ChatFormatting.DARK_RED))));
            else if (CompoundTagUtil.getInt(tag,"type").orElse(0) == 2)
                tooltip.accept(Component.translatable("tooltip." + BetterFurnacesReforged.MOD_ID + ".furnace.only", ModObjects.SMOKE.get().getName(ItemStack.EMPTY)).setStyle(Style.EMPTY.applyFormat((ChatFormatting.DARK_RED))));
        }
    }

    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource rand) {
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
                    double d1 = pos.getY();
                    double d2 = (double) pos.getZ() + 0.5D;
                    if (rand.nextDouble() < 0.1D) {
                        world.playLocalSound(d0, d1, d2, SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F, 1.0F, false);

                    }

                    Direction direction = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
                    Direction.Axis direction$axis = direction.getAxis();
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
                double d1 = pos.getY();
                double d2 = (double) pos.getZ() + 0.5D;
                if (rand.nextDouble() < 0.1D) {
                    world.playLocalSound(d0, d1, d2, SoundEvents.BLASTFURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
                }

                Direction direction = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
                Direction.Axis direction$axis = direction.getAxis();
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
    public /*? if >=1.20.3 {*/BlockState/*?} else {*//*void*//*?}*/ playerWillDestroy(Level level, BlockPos blockPos, BlockState blockState, Player player) {
        if (level.getBlockEntity(blockPos) instanceof SmeltingBlockEntity smeltBe) {
            ItemStack itemStack = player.getMainHandItem();
            onRemoveInventoryBlockEntity(level, blockPos, smeltBe, FactoryItemUtil.getEnchantmentLevel(itemStack, Enchantments.SILK_TOUCH, level.registryAccess()) == 0 || !itemStack.isCorrectToolForDrops(blockState));
            level.removeBlockEntity(blockPos);
        }
        /*? if >=1.20.3 {*/return /*?}*/super.playerWillDestroy(level, blockPos, blockState, player);
    }

    public BlockState rotate(BlockState p_185499_1_, Rotation p_185499_2_) {
        return p_185499_1_.setValue(BlockStateProperties.HORIZONTAL_FACING, p_185499_2_.rotate(p_185499_1_.getValue(BlockStateProperties.HORIZONTAL_FACING)));
    }

    public BlockState mirror(BlockState p_185471_1_, Mirror p_185471_2_) {
        return p_185471_1_.rotate(p_185471_2_.getRotation(p_185471_1_.getValue(BlockStateProperties.HORIZONTAL_FACING)));
    }

    @Override
    public boolean isSignalSource(BlockState p_149744_1_) {
        return true;
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState blockState) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos blockPos) {
        SmeltingBlockEntity be = ((SmeltingBlockEntity) level.getBlockEntity(blockPos));
        if (be != null) {
            int mode = be.furnaceSettings.getRedstone(0);
            int i = !be.hasUpgradeType(UpgradeItem.Type.FACTORY) || mode == 3 || mode == 4 ? AbstractContainerMenu.getRedstoneSignalFromContainer((be).inventory) : 0;
            if (mode != 4) return i;
            else return Math.max(i - be.furnaceSettings.getRedstone(1), 0);
        }
        return 0;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.HORIZONTAL_FACING, BlockStateProperties.LIT, COLORED, TYPE);
    }

    @Nullable
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> p_152133_, BlockEntityType<E> p_152134_, BlockEntityTicker<? super E> p_152135_) {
        return p_152134_ == p_152133_ ? (BlockEntityTicker<A>)p_152135_ : null;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return BlockEntityTypes.BETTER_FURNACE_TILE.get().create(blockPos,blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createFurnaceTicker(level, type, BlockEntityTypes.BETTER_FURNACE_TILE.get());
    }
    @Nullable
    protected static <T extends BlockEntity> BlockEntityTicker<T> createFurnaceTicker(Level level, BlockEntityType<T> blockEntityType, BlockEntityType<? extends SmeltingBlockEntity> blockEntityType1) {
        return level.isClientSide ? null : createTickerHelper(blockEntityType, blockEntityType1, SmeltingBlockEntity::tick);
    }
}
