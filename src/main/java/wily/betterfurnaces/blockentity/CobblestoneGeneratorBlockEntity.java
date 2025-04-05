package wily.betterfurnaces.blockentity;

import it.unimi.dsi.fastutil.Pair;
import net.minecraft.core.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
//? if >=1.20.2 {
import net.minecraft.world.item.crafting.RecipeHolder;
//?}
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;
import wily.betterfurnaces.blocks.CobblestoneGeneratorBlock;
import wily.betterfurnaces.init.BlockEntityTypes;
import wily.betterfurnaces.init.ModObjects;
import wily.betterfurnaces.inventory.CobblestoneGeneratorMenu;
import wily.betterfurnaces.inventory.SlotOutput;
import wily.betterfurnaces.inventory.SlotUpgrade;
import wily.betterfurnaces.items.FuelEfficiencyUpgradeItem;
import wily.betterfurnaces.items.OreProcessingUpgradeItem;
import wily.betterfurnaces.recipes.CobblestoneGeneratorRecipe;
import wily.factoryapi.FactoryAPIPlatform;
import wily.factoryapi.ItemContainerPlatform;
import wily.factoryapi.base.*;
import wily.factoryapi.base.network.CommonRecipeManager;
import wily.factoryapi.util.CompoundTagUtil;
import wily.factoryapi.util.FactoryItemUtil;
import wily.factoryapi.util.FluidInstance;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class CobblestoneGeneratorBlockEntity extends InventoryBlockEntity {

    public static List</*? if >1.20.1 {*/RecipeHolder<CobblestoneGeneratorRecipe>/*?} else {*//*CobblestoneGeneratorRecipe*//*?}*/> recipes;
    protected CobblestoneGeneratorRecipe recipe;

    @Override
    public Pair<int[], TransportState> getSlotsTransport(Direction side) {
        return Pair.of( new int[0],TransportState.EXTRACT);
    }
    public static Predicate<ItemStack> HAS_LAVA = s-> hasFluidAsBucket(s,Fluids.LAVA);
    public static Predicate<ItemStack> HAS_WATER = s-> hasFluidAsBucket(s,Fluids.WATER);

    public static boolean hasFluidAsBucket(ItemStack stack, Fluid fluid){
        return (ItemContainerPlatform.isFluidContainer(stack) && ItemContainerPlatform.getFluid(stack).isFluidEqual(FluidInstance.create(fluid,1000)));
    }

    @Override
    public boolean IcanExtractItem(int index, ItemStack stack) {
        return index == 2;
    }

    public final ContainerData fields = new ContainerData() {
        public int get(int index) {
            return switch (index){
                case 0 -> cobTime;
                case 1 -> resultType;
                case 2 -> actualCobTime;
                case 3 -> autoOutput ? 1 : 0;
                default -> 0;
            };
        }

        public void set(int index, int value) {
            switch (index){
                case 0 -> cobTime = value;
                case 1 -> resultType = value;
                case 2 -> actualCobTime = value;
                case 3 -> autoOutput = value == 1;
            }
        }
        @Override
        public int getCount() {
            return 4;
        }
    };

    @Override
    public AbstractContainerMenu createMenu(int i, Inventory playerInventory) {
        return new CobblestoneGeneratorMenu(i, level, worldPosition, playerInventory, this.fields);
    }

    public final int[] provides = new int[Direction.values().length];
    private final int[] lastProvides = new int[this.provides.length];

    public static final int INPUT = 0;
    public static final int INPUT1 = 1;
    public static final int OUTPUT = 2;
    public static final int UPGRADE = 3;
    public static final int UPGRADE1 = 4;

    private int cobTime;
    private int actualCobTime = getCobTime();
    public int resultType = 0;

    public boolean autoOutput = false;


    public CobblestoneGeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityTypes.COB_GENERATOR_TILE.get(), pos, state);
    }

    public boolean hasAutoOutput(){
        return autoOutput;
    }

    public void forceUpdateAllStates() {
        BlockState state = level.getBlockState(worldPosition);
        if (state.getValue(CobblestoneGeneratorBlock.TYPE) != cobGen()) {
            level.setBlock(worldPosition, state.setValue(CobblestoneGeneratorBlock.TYPE, cobGen()), 3);
        }
    }

    protected List</*? if >1.20.1 {*/RecipeHolder<CobblestoneGeneratorRecipe>/*?} else {*//*CobblestoneGeneratorRecipe*//*?}*/> getSortedCobRecipes(){
        return CommonRecipeManager.byType(ModObjects.ROCK_GENERATING_RECIPE.get()).stream().sorted(Comparator.comparing(o -> o.id()/*? if >=1.21.2 {*/.location()/*?}*/.getPath())).toList();
    }

    public void initRecipes() {
        recipes = getSortedCobRecipes();
    }

    public void setRecipe(int index) {
        if (level != null) {
            this.recipe = Objects.requireNonNullElseGet(recipes, this::getSortedCobRecipes).get(index)/*? if >1.20.2 {*/.value()/*?}*/;
        }
    }

    public void changeRecipe(boolean next) {
        if (recipes != null) {
            int newIndex = resultType + (next ? 1 : -1);
            if (newIndex > recipes.size() - 1) newIndex = 0;
            if (newIndex < 0) newIndex = recipes.size() - 1;

            setRecipe(newIndex);
            this.resultType = newIndex;

            this.updateBlockState();
        }
    }

    public void tick(BlockState state) {

        if (recipes == null) {
            initRecipes();
        }
        if (recipes.isEmpty()) return;


        if (recipe == null && recipes != null || level.isClientSide && recipes.indexOf(recipe) != resultType) {
            setRecipe(resultType);
            updateBlockState();
        }

        if (!level.isClientSide) {
            if (actualCobTime != getCobTime()) {
                actualCobTime = getCobTime();
            }
            if (cobTime > getCobTime()) {
                cobTime = getCobTime();
            }

            if (!getLevel().isClientSide) forceUpdateAllStates();
            ItemStack output = inventory.getItem(OUTPUT);
            ItemStack upgrade = inventory.getItem(UPGRADE);
            ItemStack upgrade1 = inventory.getItem(UPGRADE1);
            boolean active = true;
            for (Direction side : Direction.values()) {
                if (level.getSignal(worldPosition.relative(side), side) > 0) {
                    active = false;
                }
            }
            boolean can = (output.getCount() + 1 <= output.getMaxStackSize());
            boolean can1 = (output.isEmpty());
            boolean can3 = (output.getItem() == getResult().getItem());
            if (((cobGen() == 3) || cobTime > 0 && cobTime < actualCobTime) && active) {
                if ((can && can3) || can1)
                    ++cobTime;
            }

            if ((cobTime >= getCobTime() && ((can && can3) || can1))) {

                if (can1) getInv().setItem(OUTPUT, getResult());
                else output.grow(getResult().getCount());
                if (upgrade1.getItem() == ModObjects.ORE_PROCESSING.get())
                    breakDurabilityItem(upgrade1);
                level.playSound(null, getBlockPos(), SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS, 0.3F, 0.3F);
                if (upgrade.getItem() == ModObjects.FUEL.get())
                    breakDurabilityItem(upgrade);

                cobTime = 0;

                RandomSource rand = level.random;
                double d0 = (double) worldPosition.getX() + 0.5D;
                double d1 = (double) worldPosition.getY() + 0.5D;
                double d2 = (double) worldPosition.getZ() + 0.5D;

                Direction direction = state.getValue(BlockStateProperties.FACING);
                Direction.Axis direction$axis = direction.getAxis();
                double d4 = rand.nextDouble() * 0.6D - 0.3D;
                double d5 = direction$axis == Direction.Axis.X ? (double) direction.getStepX() * 0.52D : d4;
                double d6 = rand.nextDouble() * 6.0D / 16.0D;
                double d7 = direction$axis == Direction.Axis.Z ? (double) direction.getStepZ() * 0.52D : d4;
                for (int i = 0; i < 6; i++) {
                    ((ServerLevel)level).sendParticles(ParticleTypes.LARGE_SMOKE, d0 + d5, d1 + d6, d2 + d7, 0, 0.0D, 0.0D, 0.0D, 1);
                }
            }
            if (!output.isEmpty() && hasAutoOutput()) handleAutoOutput(this, OUTPUT);
        }
    }

    protected int cobGen(){
        ItemStack input = this.getInv().getItem(0);
        ItemStack input1 = this.getInv().getItem(1);
        if (HAS_LAVA.test(input) && input1.isEmpty()){
            return 1;
        }else if (HAS_WATER.test(input1) && input.isEmpty()){
            return 2;
        }else if (HAS_LAVA.test(input)  && HAS_WATER.test(input1) ){
            return 3;
        }else return 0;
    }

    protected int getFuelEfficiencyMultiplier(){
        ItemStack upgrade = this.inventory.getItem(UPGRADE);
        if (!upgrade.isEmpty() && upgrade.getItem() instanceof FuelEfficiencyUpgradeItem) return 2;
        return 1;
    }

    protected int getCobTime(){
        if (recipe != null) return recipe.duration() / getFuelEfficiencyMultiplier();
        return -1;
    }

    public ItemStack getResult(){
        return recipe == null ? new ItemStack(Items.COBBLESTONE, getResultCount()) : recipe.result().copyWithCount(getResultCount());
    }

    protected int getResultCount(){
        ItemStack upgrade1 = inventory.getItem(4);
        if (upgrade1.getItem() instanceof OreProcessingUpgradeItem)
            return 2;
        else return 1;
    }
    @Override
    public void /*? if <1.20.5 {*//*load(CompoundTag tag)*//*?} else {*/loadAdditional(CompoundTag tag, HolderLookup.Provider provider)/*?}*/ {
        super./*? if <1.20.5 {*//*load(tag)*//*?} else {*/loadAdditional(tag, provider)/*?}*/;
        this.cobTime = CompoundTagUtil.getInt(tag, "CobTime").orElse(0);
        this.resultType = CompoundTagUtil.getInt(tag, "ResultType").orElse(0);
        this.actualCobTime = CompoundTagUtil.getInt(tag, "ActualCobTime").orElse(0);
        this.autoOutput = CompoundTagUtil.getBoolean(tag, "autoOutput").orElse(false);
    }

    @Override
    public void saveAdditional(CompoundTag tag/*? if >=1.20.5 {*/, HolderLookup.Provider provider/*?}*/) {
        tag.putInt("CobTime", this.cobTime);
        tag.putInt("ResultType", this.resultType);
        tag.putInt("ActualCobTime", this.actualCobTime);
        tag.putBoolean("autoOutput", this.autoOutput);

        super.saveAdditional(tag/*? if >=1.20.5 {*/, provider/*?}*/);
    }

    @Override
    public <T extends IPlatformHandler> ArbitrarySupplier<T> getStorage(FactoryStorage<T> storage, Direction facing) {
        if (!this.isRemoved() && storage == FactoryStorage.ITEM) {
            return ()->(T) inventory;
        }
        return ArbitrarySupplier.empty();
    }


    @Override
    public void addSlots(NonNullList<FactoryItemSlot> slots, @Nullable Player player) {
        slots.add(new FactoryItemSlot(getInv(), SlotsIdentifier.LAVA,TransportState.INSERT, 0, 53, 27){
            @Override
            public boolean mayPlace(ItemStack stack) {
                return CobblestoneGeneratorBlockEntity.HAS_LAVA.test(stack);
            }
        });
        slots.add(new FactoryItemSlot(getInv(), SlotsIdentifier.WATER,TransportState.INSERT, 1, 108, 27){
            @Override
            public boolean mayPlace(ItemStack stack) {
                return CobblestoneGeneratorBlockEntity.HAS_WATER.test(stack);
            }
        });
        slots.add(new SlotOutput(player, this, 2, 80, 45));
        slots.add(new SlotUpgrade(this, 3, 8, 18){
            @Override
            public boolean mayPlace(ItemStack stack) {
                return ( stack.getItem() instanceof FuelEfficiencyUpgradeItem);
            }
        });
        slots.add(new SlotUpgrade(this, 4, 8, 36){
            @Override
            public boolean mayPlace(ItemStack stack) {
                return ( stack.getItem() instanceof OreProcessingUpgradeItem);
            }
        });
    }

    public static void handleAutoOutput(InventoryBlockEntity be, int output) {
        for (Direction dir : Direction.values()) {
            BlockEntity tile = be.getLevel().getBlockEntity(be.getBlockPos().relative(dir));
            if (tile == null)
                continue;
            IPlatformItemHandler other = FactoryAPIPlatform.getPlatformFactoryStorage(tile).getStorage(FactoryStorage.ITEM, dir.getOpposite()).get();

            if (other == null || be.inventory.getItem(output).isEmpty() || BuiltInRegistries.BLOCK.getKey(tile.getBlockState().getBlock()).getNamespace().equals("storagedrawers"))
                continue;

            for (int i = 0; i < other.getContainerSize(); i++) {
                ItemStack stack = be.inventory.extractItem(output, be.inventory.getItem(output).getMaxStackSize() - other.getItem(i).getCount(), true);
                if (other.canPlaceItem(i, stack) && (other.getItem(i).isEmpty() || other.canPlaceItem(i, stack) && (FactoryItemUtil.equalItems(other.getItem(i), stack) && other.getItem(i).getCount() + stack.getCount() <= other.getMaxStackSize()))) {
                    be.inventory.setItem(output, other.insertItem(i, stack, false));
                }
            }
        }
    }

    public void onUpdateSent() {
        System.arraycopy(this.provides, 0, this.lastProvides, 0, this.provides.length);
        this.level.updateNeighborsAt(this.worldPosition, getBlockState().getBlock());
    }
}
