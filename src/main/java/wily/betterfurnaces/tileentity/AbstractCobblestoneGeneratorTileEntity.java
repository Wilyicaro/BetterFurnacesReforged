package wily.betterfurnaces.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.client.audio.SoundSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.InvWrapper;
import wily.betterfurnaces.blocks.CobblestoneGeneratorBlock;
import wily.betterfurnaces.init.Registration;
import wily.betterfurnaces.inventory.AbstractCobblestoneGeneratorContainer;
import wily.betterfurnaces.items.FuelEfficiencyUpgradeItem;
import wily.betterfurnaces.items.OreProcessingUpgradeItem;
import wily.betterfurnaces.recipes.CobblestoneGeneratorRecipes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractCobblestoneGeneratorTileEntity extends InventoryTileEntity implements ITickableTileEntity {

    public static final int INPUT = 0;
    public static final int INPUT1 = 1;
    public static final int OUTPUT = 2;
    public static final int UPGRADE = 3;
    public static final int UPGRADE1 = 4;
    public static List<CobblestoneGeneratorRecipes> recipes;
    public final int[] provides = new int[Direction.values().length];
    private final int[] lastProvides = new int[this.provides.length];
    public int resultType = 1;
    protected CobblestoneGeneratorRecipes recipe;
    /**
     * The number of ticks that the furnace will keep burning
     */
    private int cobTime;
    private int actualCobTime = getCobTime();
    public final IIntArray fields = new IIntArray() {
        public int get(int index) {

            if (index == 0)
                return AbstractCobblestoneGeneratorTileEntity.this.cobTime;
            if (index == 1)
                return AbstractCobblestoneGeneratorTileEntity.this.resultType;
            if (index == 2)
                return AbstractCobblestoneGeneratorTileEntity.this.actualCobTime;
            else return 0;
        }

        public void set(int index, int value) {
            if (index == 0)
                AbstractCobblestoneGeneratorTileEntity.this.cobTime = value;
            if (index == 1)
                AbstractCobblestoneGeneratorTileEntity.this.resultType = value;
            if (index == 2)
                AbstractCobblestoneGeneratorTileEntity.this.actualCobTime = value;
        }

        @Override
        public int getCount() {
            return 3;
        }
    };

    public AbstractCobblestoneGeneratorTileEntity(TileEntityType<?> tileentitytypeIn) {
        super(tileentitytypeIn, 5);

    }

    @Override
    public int[] IgetSlotsForFace(Direction side) {
        return new int[0];
    }

    @Override
    public boolean IcanExtractItem(int index, ItemStack stack, Direction direction) {
        return false;
    }

    @Override
    public Container IcreateMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new AbstractCobblestoneGeneratorContainer.CobblestoneGeneratorContainer(i, level, worldPosition, playerInventory, playerEntity, this.fields);
    }

    public void forceUpdateAllStates() {
        BlockState state = level.getBlockState(worldPosition);
        if (state.getValue(CobblestoneGeneratorBlock.TYPE) != cobGen()) {
            level.setBlock(worldPosition, state.setValue(CobblestoneGeneratorBlock.TYPE, cobGen()), 3);
        }
    }

    protected List<CobblestoneGeneratorRecipes> getSortedCobRecipes(){
        return Objects.requireNonNull(getLevel()).getRecipeManager().getAllRecipesFor(Registration.COB_GENERATION_RECIPE).stream().sorted(Comparator.comparing(o -> o.recipeId.getPath())).collect(Collectors.toList());
    }
    public void initRecipes() {
        recipes = getSortedCobRecipes();
    }
    public void setRecipe(int index) {
        if (level != null) {
            this.recipe = Optional.ofNullable(recipes).orElse(getSortedCobRecipes()).get(index);
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

    @Override
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
        ItemStack output = getItem(OUTPUT);
        ItemStack upgrade = getItem(UPGRADE);
        ItemStack upgrade1 = getItem(UPGRADE1);
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
                    setItem(OUTPUT, getResult());
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
                level.playSound(null, getBlockPos(), SoundEvents.LAVA_EXTINGUISH, SoundCategory.BLOCKS, 0.3F, 0.3F);
                breakDurabilityItem(upgrade);
                AutoIO();
            }
            cobTime = 0;

            if (level.isClientSide) {
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
        }
        if (!output.isEmpty() && !level.isClientSide) AutoIO();

    }

    protected int cobGen() {
        ItemStack input = this.inventory.get(0);
        ItemStack input1 = this.inventory.get(1);
        if (input.getItem() == Items.LAVA_BUCKET.getItem() && input1.isEmpty()) {
            return 1;
        } else if (input1.getItem() == Items.WATER_BUCKET.getItem() && input.isEmpty()) {
            return 2;
        } else if (input.getItem() == Items.LAVA_BUCKET.getItem() && input1.getItem() == Items.WATER_BUCKET.getItem()) {
            return 3;
        } else return 0;
    }

    protected int FuelEfficiencyMultiplier() {
        ItemStack upgrade = this.inventory.get(UPGRADE);
        if (!upgrade.isEmpty() && upgrade.getItem() instanceof FuelEfficiencyUpgradeItem) return 2;
        return 1;

    }

    protected int getCobTime() {
        if (recipe != null) return recipe.duration / FuelEfficiencyMultiplier();
        return -1;
    }

    public ItemStack getResult() {
        ItemStack result;
        if (recipe != null) result = new ItemStack(recipe.getResultItem().getItem());
        else result = new ItemStack(Items.COBBLESTONE);
        result.setCount(getResultCount());
        return result;
    }

    protected int getResultCount() {
        ItemStack upgrade1 = this.getItem(4);
        if (upgrade1.getItem() instanceof OreProcessingUpgradeItem)
            return 2;
        else return 1;
    }

    protected boolean hasLava() {
        ItemStack input = this.inventory.get(0);
        return (input.getItem() == Items.WATER_BUCKET.getItem());
    }

    protected boolean hasWater() {
        ItemStack input = this.inventory.get(1);
        return (input.getItem() == Items.WATER_BUCKET.getItem());
    }

    @Override
    public void load(BlockState state, CompoundNBT tag) {
        ItemStackHelper.loadAllItems(tag, this.inventory);
        this.cobTime = tag.getInt("CobTime");
        this.resultType = tag.getInt("ResultType");
        this.actualCobTime = tag.getInt("ActualCobTime");

        super.load(state, tag);
    }

    @Override
    public CompoundNBT save(CompoundNBT tag) {
        super.save(tag);
        ItemStackHelper.saveAllItems(tag, this.inventory);
        tag.putInt("CobTime", this.cobTime);
        tag.putInt("ResultType", this.resultType);
        tag.putInt("ActualCobTime", this.actualCobTime);
        return tag;
    }

    @Nonnull
    @Override
    public <
            T> net.minecraftforge.common.util.LazyOptional<T> getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @Nullable Direction facing) {

        if (!this.isRemoved() && facing != null && capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            InvWrapper inv = new InvWrapper(this);
            return LazyOptional.of(() -> inv).cast();
        }
        return super.getCapability(capability, facing);
    }

    @Nonnull
    private ItemStack extractItemInternal(int slot, int amount, boolean simulate) {
        if (amount == 0)
            return ItemStack.EMPTY;

        ItemStack existing = this.getItem(slot);

        if (existing.isEmpty())
            return ItemStack.EMPTY;

        int toExtract = Math.min(amount, existing.getMaxStackSize());

        if (existing.getCount() <= toExtract) {
            if (!simulate) {
                this.setItem(slot, ItemStack.EMPTY);
                this.setChanged();
                return existing;
            } else {
                return existing.copy();
            }
        } else {
            if (!simulate) {
                this.setItem(slot, ItemHandlerHelper.copyStackWithSize(existing, existing.getCount() - toExtract));
                this.setChanged();
            }

            return ItemHandlerHelper.copyStackWithSize(existing, toExtract);
        }
    }

    private void AutoIO() {
        for (Direction dir : Direction.values()) {
            TileEntity tile = level.getBlockEntity(worldPosition.offset(dir.getNormal()));
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
                    if (tile.getBlockState().getBlock().getRegistryName().toString().contains("storagedrawers:")) {
                        continue;
                    }
                    for (int i = 0; i < other.getSlots(); i++) {
                        ItemStack stack = extractItemInternal(OUTPUT, this.getItem(OUTPUT).getMaxStackSize() - other.getStackInSlot(i).getCount(), true);
                        if (other.isItemValid(i, stack) && (other.getStackInSlot(i).isEmpty() || other.isItemValid(i, stack) && ItemHandlerHelper.canItemStacksStack(other.getStackInSlot(i), stack) && other.getStackInSlot(i).getCount() + stack.getCount() <= other.getSlotLimit(i))) {
                            other.insertItem(i, extractItemInternal(OUTPUT, stack.getCount(), false), false);
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

            return stack.getItem() == Items.LAVA_BUCKET;

        }
        if (index == INPUT1) {
            if (stack.isEmpty()) {
                return false;
            }

            return stack.getItem() == Items.WATER_BUCKET;

        }
        if (index == UPGRADE) {
            return stack.getItem() instanceof FuelEfficiencyUpgradeItem;

        }
        if (index == UPGRADE1) {
            return stack.getItem() instanceof OreProcessingUpgradeItem;

        }
        return false;
    }

    public void onUpdateSent() {
        System.arraycopy(this.provides, 0, this.lastProvides, 0, this.provides.length);
        this.level.updateNeighborsAt(this.worldPosition, getBlockState().getBlock());
    }

    public static class CobblestoneGeneratorTileEntity extends AbstractCobblestoneGeneratorTileEntity {
        public CobblestoneGeneratorTileEntity() {
            super(Registration.COB_GENERATOR_TILE.get());
        }

    }

}
