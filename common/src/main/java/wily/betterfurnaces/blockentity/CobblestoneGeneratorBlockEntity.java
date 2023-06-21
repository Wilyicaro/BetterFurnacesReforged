package wily.betterfurnaces.blockentity;

import com.ibm.icu.impl.Pair;
import me.shedaniel.architectury.fluid.FluidStack;
import me.shedaniel.architectury.utils.Fraction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;
import wily.betterfurnaces.BetterFurnacesPlatform;
import wily.betterfurnaces.blocks.CobblestoneGeneratorBlock;
import wily.betterfurnaces.init.Registration;
import wily.betterfurnaces.inventory.CobblestoneGeneratorMenu;
import wily.betterfurnaces.inventory.SlotOutput;
import wily.betterfurnaces.inventory.SlotUpgrade;
import wily.betterfurnaces.items.FuelEfficiencyUpgradeItem;
import wily.betterfurnaces.items.OreProcessingUpgradeItem;
import wily.betterfurnaces.recipes.CobblestoneGeneratorRecipes;
import wily.factoryapi.FactoryAPIPlatform;
import wily.factoryapi.ItemContainerUtil;
import wily.factoryapi.base.IPlatformHandlerApi;
import wily.factoryapi.base.Storages;
import wily.factoryapi.base.TransportState;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CobblestoneGeneratorBlockEntity extends InventoryBlockEntity {

    public static List<CobblestoneGeneratorRecipes> recipes;
    protected CobblestoneGeneratorRecipes recipe;

    @Override
    public Pair<int[], TransportState> getSlotsTransport(Direction side) {
        return Pair.of( new int[0],TransportState.EXTRACT);
    }
    public static Predicate<ItemStack> HAS_LAVA = s-> hasFluidAsBucket(s,Fluids.LAVA);
    public static Predicate<ItemStack> HAS_WATER = s-> hasFluidAsBucket(s,Fluids.WATER);

    public static boolean hasFluidAsBucket(ItemStack stack, Fluid fluid){
        return  (ItemContainerUtil.isFluidContainer(stack) && ItemContainerUtil.getFluid(stack).isFluidStackEqual(FluidStack.create(fluid, Fraction.ofWhole(FactoryAPIPlatform.getBucketAmount()))));
    }

    @Override
    public boolean IcanExtractItem(int index, ItemStack stack) {
        return false;
    }

    public final ContainerData fields = new ContainerData() {
        public int get(int index) {

            if (index == 0)
                return CobblestoneGeneratorBlockEntity.this.cobTime;
            if (index == 1)
                return CobblestoneGeneratorBlockEntity.this.resultType;
            if (index == 2)
                return CobblestoneGeneratorBlockEntity.this.actualCobTime;
            else return 0;
        }

        public void set(int index, int value) {
            if (index == 0)
                CobblestoneGeneratorBlockEntity.this.cobTime = value;
            if (index == 1)
                CobblestoneGeneratorBlockEntity.this.resultType = value;
            if (index == 2)
                CobblestoneGeneratorBlockEntity.this.actualCobTime = value;
        }

        @Override
        public int getCount() {
            return 3;
        }
    };
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
        return new CobblestoneGeneratorMenu(i, level, worldPosition, playerInventory, playerEntity, this.fields);
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


    public CobblestoneGeneratorBlockEntity() {
        super(Registration.COB_GENERATOR_TILE.get());

    }
    public void forceUpdateAllStates() {
        BlockState state = level.getBlockState(worldPosition);
        if (state.getValue(CobblestoneGeneratorBlock.TYPE) != cobGen()) {
            level.setBlock(worldPosition, state.setValue(CobblestoneGeneratorBlock.TYPE, cobGen()), 3);
        }
    }
    protected List<CobblestoneGeneratorRecipes> getSortedCobRecipes(){
        return Objects.requireNonNull(getLevel()).getRecipeManager().getAllRecipesFor(Registration.ROCK_GENERATING_RECIPE.get()).stream().sorted(Comparator.comparing(o -> o.recipeId.getPath())).collect(Collectors.toList());
    }
    public void initRecipes() {
        recipes = getSortedCobRecipes();
    }
    public void setRecipe(int index) {
        if (level != null) {
            this.recipe = (recipes == null ? getSortedCobRecipes() : recipes).get(index);
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
    public void tick() {

        if (actualCobTime != getCobTime()){
            actualCobTime = getCobTime();
        }
        if (cobTime > getCobTime()){
            cobTime = getCobTime();
        }
        if (recipes == null) {
            initRecipes();
        }
        if (recipe == null && recipes != null || level.isClientSide && recipes.indexOf(recipe) != resultType) {
            setRecipe(resultType);
            updateBlockState();
        }

        if (!getLevel().isClientSide)forceUpdateAllStates();
        ItemStack output = inventory.getItem(OUTPUT);
        ItemStack upgrade = inventory.getItem(UPGRADE);
        ItemStack upgrade1 = inventory.getItem(UPGRADE1);
        boolean active = true;
        for (Direction side : Direction.values()) {
            if (level.getSignal(worldPosition.offset(side.getNormal()), side) > 0) {
                active = false;
            }
        }
        boolean can = (output.getCount() + 1 <= output.getMaxStackSize());
        boolean can1 = (output.isEmpty());
        boolean can3 = (output.getItem() == getResult().getItem());
        if (((cobGen() == 3) || cobTime > 0 && cobTime < actualCobTime) && active) {
            if ((can && can3 )|| can1)
                ++cobTime;
        }

        if ((cobTime >= getCobTime() && ((can  && can3)|| can1))){
            if (!level.isClientSide) {
                if (can1) {
                    getInv().setItem(OUTPUT, getResult());
                    if (upgrade1.getItem() instanceof OreProcessingUpgradeItem) {
                        breakDurabilityItem(upgrade1);
                    }
                }else {
                    if (can && can3) {
                        output.grow(getResult().getCount());
                        if (upgrade1.getItem() instanceof OreProcessingUpgradeItem) {
                            breakDurabilityItem(upgrade1);
                        }
                    }
                }
                level.playSound(null, getBlockPos(), SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS, 0.3F, 0.3F);
                breakDurabilityItem(upgrade);
                BetterFurnacesPlatform.outputAutoIO(this);
            }
            cobTime = 0;

            Random rand = level.random;
            double d0 = (double) worldPosition.getX() + 0.5D;
            double d1 = (double) worldPosition.getY() + 0.5D;
            double d2 = (double) worldPosition.getZ() + 0.5D;

            Direction direction = getBlockState().getValue(BlockStateProperties.FACING);
            Direction.Axis direction$axis = direction.getAxis();
            double d4 = rand.nextDouble() * 0.6D - 0.3D;
            double d5 = direction$axis == Direction.Axis.X ? (double) direction.getStepX() * 0.52D : d4;
            double d6 = rand.nextDouble() * 6.0D / 16.0D;
            double d7 = direction$axis == Direction.Axis.Z ? (double) direction.getStepZ() * 0.52D : d4;
            for (int i = 0; i < 6; i++) {
                level.addParticle(ParticleTypes.LARGE_SMOKE, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
            }
        }
        if (!output.isEmpty() && !level.isClientSide) BetterFurnacesPlatform.outputAutoIO(this);
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
    protected int FuelEfficiencyMultiplier(){
        ItemStack upgrade = this.inventory.getItem(UPGRADE);
        if (!upgrade.isEmpty() && upgrade.getItem() instanceof FuelEfficiencyUpgradeItem) return 2;
        return 1;

    }
    protected int getCobTime(){
        if (recipe != null) return recipe.duration / FuelEfficiencyMultiplier();
        return -1;
    }
    public ItemStack getResult(){
        ItemStack result;
        if (recipe != null) result = new ItemStack(recipe.getResultItem().getItem());
        else result = new ItemStack(Items.COBBLESTONE);
        result.setCount(getResultCount());
        return result;
    }
    protected int getResultCount(){
        ItemStack upgrade1 = inventory.getItem(4);
        if (upgrade1.getItem() instanceof OreProcessingUpgradeItem)
            return 2;
        else return 1;
    }
    @Override
    public void load(BlockState blockState,CompoundTag tag) {
        super.load(blockState,tag);
        this.cobTime = tag.getInt("CobTime");
        this.resultType = tag.getInt("ResultType");
        this.actualCobTime = tag.getInt("ActualCobTime");

    }



    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putInt("CobTime", this.cobTime);
        tag.putInt("ResultType", this.resultType);
        tag.putInt("ActualCobTime", this.actualCobTime);

        return super.save(tag);
    }

    @Override
    public <T extends IPlatformHandlerApi> Optional<T> getStorage(Storages.Storage<T> storage, Direction facing) {
        if (!this.isRemoved() && storage == Storages.ITEM) {
            return (Optional<T>) Optional.of(FactoryAPIPlatform.filteredOf(inventory,facing, new int[]{0,1,2,3,4},TransportState.EXTRACT_INSERT));
        }
        return super.getStorage(storage, facing);
    }


    @Override
    public boolean IisItemValidForSlot(int index, ItemStack stack) {
        if (index == OUTPUT) {
            return false;
        }
        if (index == INPUT) {
            if (stack.isEmpty()) {
                return false;
            }

            return stack.getItem() == Items.LAVA_BUCKET;

        }
        if (index == INPUT1) {
            if (stack.isEmpty()) {
                return false;
            }

            return stack.getItem() == Items.WATER_BUCKET;

        }
        if (index == UPGRADE) {
            if (stack.isEmpty()) {
                return false;
            }

            return stack.getItem() instanceof FuelEfficiencyUpgradeItem;

        }
        if (index == UPGRADE1) {
            if (stack.isEmpty()) {
                return false;
            }

            return stack.getItem() instanceof OreProcessingUpgradeItem;

        }
        return false;
    }

    @Override
    public void addSlots(NonNullList<Slot> slots, @Nullable Player player) {
        slots.add(new Slot(inventory, 0, 53, 27){
            @Override
            public boolean mayPlace(ItemStack stack) {
                return CobblestoneGeneratorBlockEntity.HAS_LAVA.test(stack);
            }
        });
        slots.add(new Slot(inventory, 1, 108, 27){
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

    public void onUpdateSent() {
        System.arraycopy(this.provides, 0, this.lastProvides, 0, this.provides.length);
        this.level.updateNeighborsAt(this.worldPosition, getBlockState().getBlock());
    }

}
