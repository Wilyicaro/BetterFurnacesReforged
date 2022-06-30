package wily.betterfurnaces.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.registries.ForgeRegistries;
import wily.betterfurnaces.blocks.CobblestoneGeneratorBlock;
import wily.betterfurnaces.container.AbstractCobblestoneGeneratorMenu;
import wily.betterfurnaces.init.Registration;
import wily.betterfurnaces.items.FuelEfficiencyUpgradeItem;
import wily.betterfurnaces.items.ItemUpgradeOreProcessing;
import wily.betterfurnaces.recipes.CobblestoneGeneratorRecipes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public abstract class AbstractCobblestoneGeneratorBlockEntity extends InventoryBlockEntity {

    public static List<CobblestoneGeneratorRecipes> recipes;
    protected CobblestoneGeneratorRecipes recipe;

    @Override
    public int[] IgetSlotsForFace(Direction side) {
        return new int[0];
    }

    @Override
    public boolean IcanExtractItem(int index, ItemStack stack, Direction direction) {
        return false;
    }

    public final ContainerData fields = new ContainerData() {
        public int get(int index) {

            if (index == 0)
                return AbstractCobblestoneGeneratorBlockEntity.this.cobTime;
            if (index == 1)
                return AbstractCobblestoneGeneratorBlockEntity.this.resultType;
            if (index == 2)
                return AbstractCobblestoneGeneratorBlockEntity.this.actualCobTime;
            else return 0;
        }

        public void set(int index, int value) {
            if (index == 0)
                AbstractCobblestoneGeneratorBlockEntity.this.cobTime = value;
            if (index == 1)
                AbstractCobblestoneGeneratorBlockEntity.this.resultType = value;
            if (index == 2)
                AbstractCobblestoneGeneratorBlockEntity.this.actualCobTime = value;
        }

        @Override
        public int getCount() {
            return 3;
        }
    };
    @Override
    public AbstractContainerMenu IcreateMenu(int i, Inventory playerInventory, Player playerEntity) {
        return new AbstractCobblestoneGeneratorMenu.CobblestoneGeneratorMenu(i, level, worldPosition, playerInventory, playerEntity, this.fields);
    }
    public final int[] provides = new int[Direction.values().length];
    private final int[] lastProvides = new int[this.provides.length];

    public static final int INPUT = 0;
    public static final int INPUT1 = 1;
    public static final int OUTPUT = 2;
    public static final int UPGRADE = 3;

    private int cobTime;
    private int actualCobTime = getCobTime();
    public int resultType = 0;

    public static class CobblestoneGeneratorBlockEntity extends AbstractCobblestoneGeneratorBlockEntity {
        public CobblestoneGeneratorBlockEntity(BlockPos pos, BlockState state) {
            super(Registration.COB_GENERATOR_TILE.get(), pos, state);
        }
    }
    public AbstractCobblestoneGeneratorBlockEntity(BlockEntityType<?> tileentitytypeIn, BlockPos pos, BlockState state) {
        super(tileentitytypeIn, pos, state, 5);

    }

    public void forceUpdateAllStates() {
        BlockState state = level.getBlockState(worldPosition);
        if (state.getValue(CobblestoneGeneratorBlock.TYPE) != cobGen()) {
            level.setBlock(worldPosition, state.setValue(CobblestoneGeneratorBlock.TYPE, cobGen()), 3);
        }
    }
    public void initRecipes() {
        recipes = Objects.requireNonNull(getLevel()).getRecipeManager().getAllRecipesFor(CobblestoneGeneratorRecipes.TYPE);
    }
    public void setRecipe(int index) {
        if (level != null) {
            this.recipe = Objects.requireNonNullElseGet(recipes, () -> level.getRecipeManager().getAllRecipesFor(CobblestoneGeneratorRecipes.TYPE)).get(index);
        }
    }
    public void changeRecipe(boolean next, boolean onlyUpdate) {
        if (recipes != null && !onlyUpdate) {
            int newIndex = resultType + (next ? 1 : -1);
            if (newIndex > recipes.size() - 1) newIndex = 0;
            if (newIndex < 0) newIndex = recipes.size() - 1;

            setRecipe(newIndex);
            this.resultType = newIndex;

            this.updateBlockState();
        }
        initRecipes();
    }
    public static void tick(Level level, BlockPos worldPosition, BlockState blockState, AbstractCobblestoneGeneratorBlockEntity e) {
        if (e.actualCobTime != e.getCobTime()){
            e.actualCobTime = e.getCobTime();
        }
        if (e.cobTime > e.getCobTime()){
            e.cobTime = e.getCobTime();
        }
        if (recipes == null) {
            e.initRecipes();
        }
        if (e.recipe == null && recipes != null) {
            e.setRecipe(0);
            e.updateBlockState();
        }
        ItemStack output = e.getItem(2);
        ItemStack upgrade = e.getItem(3);
        ItemStack upgrade1 = e.getItem(4);
        boolean active = true;
        for (Direction side : Direction.values()) {
            if (level.getSignal(worldPosition.offset(side.getNormal()), side) > 0) {
                active = false;
            }
        }
        boolean can = (output.getCount() + 1 <= output.getMaxStackSize());
        boolean can1 = (output.isEmpty());
        boolean can3 = (output.getItem() == e.getResult().getItem());
        e.forceUpdateAllStates();
        if (((e.cobGen() == 3) || e.cobTime > 0 && e.cobTime < e.actualCobTime) && active) {
            if ((can && can3 )|| can1)
                ++e.cobTime;
        }
        if (!e.level.isClientSide) {
            if (!output.isEmpty()) e.AutoIO();
            if ((e.cobTime >= e.getCobTime() && ((can  && can3)|| can1))){
                if (can1) {
                    e.getInv().setStackInSlot(OUTPUT, e.getResult());
                    if (upgrade1.getItem() instanceof ItemUpgradeOreProcessing) {
                        e.breakDurabilityItem(upgrade1);
                    }
                }else {
                    if (can && can3) {
                        output.grow(e.getResult().getCount());
                        if (upgrade1.getItem() instanceof ItemUpgradeOreProcessing) {
                            e.breakDurabilityItem(upgrade1);
                        }
                    }
                }
                e.getLevel().playSound(null, e.getBlockPos(), SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS, 0.3F, 0.3F);
                e.cobTime = 0;
                e.breakDurabilityItem(upgrade);
                e.AutoIO();
            }
        }

    }
    protected int cobGen(){
        ItemStack input = this.getInv().getStackInSlot(0);
        ItemStack input1 = this.getInv().getStackInSlot(1);
        if (input.getItem() == Items.LAVA_BUCKET && input1.isEmpty()){
            return 1;
        }else if (input1.getItem() == Items.WATER_BUCKET && input.isEmpty()){
            return 2;
        }else if (input.getItem() == Items.LAVA_BUCKET && input1.getItem() == Items.WATER_BUCKET){
            return 3;
        }else return 0;
    }
    protected int FuelEfficiencyMultiplier(){
        ItemStack upgrade = this.inventory.getStackInSlot(UPGRADE);
        if (!upgrade.isEmpty() && upgrade.getItem() instanceof FuelEfficiencyUpgradeItem) return 2;
        return 1;

    }
    protected int getCobTime(){
        if (recipe != null) return recipe.duration / FuelEfficiencyMultiplier();
        return -1;
    }
    protected ItemStack getResult(){
        ItemStack result;
        if (recipe != null) result = new ItemStack(recipes.get(resultType).getResultItem().getItem());
        else result = new ItemStack(Items.COBBLESTONE);
        result.setCount(getResultCount());
        return result;
    }
    protected int getResultCount(){
        ItemStack upgrade1 = this.getItem(4);
        if (upgrade1.getItem() instanceof ItemUpgradeOreProcessing)
            return 2;
        else return 1;
    }
    protected boolean hasLava() {
        ItemStack input = this.getInv().getStackInSlot(0);
        return (input.getItem() == Items.WATER_BUCKET);
    }
    protected boolean hasWater() {
        ItemStack input = this.getInv().getStackInSlot(1);
        return (input.getItem() == Items.WATER_BUCKET);
    }
    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.getCompound("inventory").isEmpty() && !tag.getList("Items",10).isEmpty()) {
            if (isEmpty())
                getInv().deserializeNBT(tag);
        }else
            getInv().deserializeNBT(tag.getCompound("inventory"));
        this.cobTime = tag.getInt("CobTime");
        this.resultType = tag.getInt("ResultType");
        this.actualCobTime = tag.getInt("ActualCobTime");

    }



    @Override
    public void saveAdditional(CompoundTag tag) {
        tag.putInt("CobTime", this.cobTime);
        tag.putInt("ResultType", this.resultType);
        tag.putInt("ActualCobTime", this.actualCobTime);

        super.saveAdditional(tag);
    }

    @Nonnull
    @Override
    public <
            T> LazyOptional<T> getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @Nullable Direction facing) {

        if (!this.isRemoved() && facing != null && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return LazyOptional.of(this::getInv).cast();
        }
        return super.getCapability(capability, facing);
    }
    private void AutoIO(){
        for (Direction dir : Direction.values()) {
            BlockEntity tile = level.getBlockEntity(worldPosition.offset(dir.getNormal()));
            if (tile == null) {
                continue;
            }
            if (tile != null) {
                IItemHandler other = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, dir.getOpposite()).map(other1 -> other1).orElse(null);

                if (other == null) {
                    continue;
                }
                if (other != null) {
                    if (this.getItem(OUTPUT).isEmpty()) {
                        continue;
                    }
                    if (ForgeRegistries.BLOCKS.getKey(tile.getBlockState().getBlock()).toString().contains("storagedrawers:")) {
                        continue;
                    }
                    for (int i = 0; i < other.getSlots(); i++) {
                        ItemStack stack = inventory.extractItem(OUTPUT, this.getItem(OUTPUT).getMaxStackSize() - other.getStackInSlot(i).getCount(), true);
                        if (other.isItemValid(i, stack) && (other.getStackInSlot(i).isEmpty() || other.isItemValid(i, stack) && ItemHandlerHelper.canItemStacksStack(other.getStackInSlot(i), stack) && other.getStackInSlot(i).getCount() + stack.getCount() <= other.getSlotLimit(i))) {
                            other.insertItem(i, inventory.extractItem(OUTPUT, stack.getCount(), false), false);
                        }
                    }
                }
            }
        }
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

            return hasLava();

        }
        if (index == INPUT1) {
            if (stack.isEmpty()) {
                return false;
            }

            return hasWater();

        }
        return false;
    }

    public void onUpdateSent() {
        System.arraycopy(this.provides, 0, this.lastProvides, 0, this.provides.length);
        this.level.updateNeighborsAt(this.worldPosition, getBlockState().getBlock());
    }

}
