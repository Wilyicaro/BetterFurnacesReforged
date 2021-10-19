package wily.betterfurnaces.tileentity;

import com.google.common.collect.Lists;
import harmonised.pmmo.events.FurnaceHandler;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IRecipeHelperPopulator;
import net.minecraft.inventory.IRecipeHolder;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.AirItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.registries.ForgeRegistries;
import wily.betterfurnaces.Config;
import wily.betterfurnaces.blocks.BlockForgeBase;
import wily.betterfurnaces.init.Registration;
import wily.betterfurnaces.items.*;
import wily.betterfurnaces.util.DirectionUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public abstract class BlockForgeTileBase extends TileEntityInventory implements ITickableTileEntity, IRecipeHolder, IRecipeHelperPopulator {
    public final int[] provides = new int[Direction.values().length];
    private final int[] lastProvides = new int[this.provides.length];

    public static final int INPUT = 0;
    public static final int INPUT1 = 1;
    public static final int INPUT2 = 2;
    public static final int FUEL = 3;
    public static final int OUTPUT = 4;
    public static final int OUTPUT1 = 5;
    public static final int OUTPUT2 = 6;
    public static final int[] UPMIS = { 11, 12, 13};

    protected AbstractCookingRecipe curRecipe;

    private Random rand = new Random();

    public int show_inventory_settings;
    protected int timer;
    private int currentUpgrade;
    /**
     * The number of ticks that the furnace will keep burning
     */
    private int furnaceBurnTime;
    public int cookTime;
    public int totalCookTime = this.getCookTime();
    private int recipesUsed;
    private final Object2IntOpenHashMap<ResourceLocation> recipes = new Object2IntOpenHashMap<>();

    public IRecipeType<? extends AbstractCookingRecipe> recipeType;

    public FurnaceSettings furnaceSettings;

    private LRUCache<Item, Optional<AbstractCookingRecipe>> cache = LRUCache.newInstance(Config.cache_capacity.get());


    public BlockForgeTileBase(TileEntityType<?> tileentitytypeIn) {
        super(tileentitytypeIn, 14);
        this.recipeType = IRecipeType.SMELTING;
        furnaceSettings = new FurnaceSettings() {
            @Override
            public void onChanged() {
                setChanged();
            }
        };

    }

    private int getFromCache(LRUCache<Item, Optional<AbstractCookingRecipe>> c, Item key) {
        if (c.get(key) == null)
        {
            return 0;
        }
        return c.get(key).orElse(null) == null ? 0 : c.get(key).orElse(null).getCookingTime();
    }

    public boolean hasRecipe(ItemStack stack) {
        return grabRecipe(stack).isPresent();
    }

    private LRUCache<Item, Optional<AbstractCookingRecipe>> getCache() {
        return cache;
    }

    private Optional<AbstractCookingRecipe> grabRecipe(ItemStack stack) {
        Item item = stack.getItem();
        if (item instanceof AirItem)
        {
            return Optional.empty();
        }
        Optional<AbstractCookingRecipe> recipe = getCache().get(item);
        if (recipe == null) {
            recipe = this.level.getRecipeManager().getRecipeFor((IRecipeType<AbstractCookingRecipe>) this.recipeType, new Inventory(stack), this.level);
            getCache().put(item, recipe);
        }
        return recipe;
    }
    public boolean hasXPTank() {
        return ((getUpgrade(getItem(9)) == 7));
    }
    public boolean isLiquid() {
       return (getUpgrade(getItem(10)) == 4);
    }
    private boolean isEnergy() {
        return ((getUpgrade(getItem(10)) == 5) && energyStorage.getEnergyStored() >= 80);
    }
    protected int getCookTime() {

        if (this.getItem(INPUT).getItem() == Items.AIR ) {
            return totalCookTime;
        }
        int speed = getSpeed();
        if (speed == -1) {
            return -1;
        }


        return Math.max(1, speed);


    }

    protected int getSpeed() {
        int i = getCookTimeConfig().get();
        int j = getFromCache(getCache(), getItem(INPUT).getItem());
        if (j == 0) {
            Optional<AbstractCookingRecipe> recipe = grabRecipe(getItem(INPUT));
            j = !recipe.isPresent() ? -1 : recipe.orElse(null).getCookingTime();

            if (j == -1) {
                return -1;
            }
        }
        if (j < i) {
            int k = j - (200 - i);
            return k;
        } else {
            return i;
        }


    }

    public ForgeConfigSpec.IntValue getCookTimeConfig() {
        return null;
    }

    public final IIntArray fields = new IIntArray() {
        public int get(int index) {
            switch (index) {
                case 0:
                    return BlockForgeTileBase.this.furnaceBurnTime;
                case 1:
                    return BlockForgeTileBase.this.recipesUsed;
                case 2:
                    return BlockForgeTileBase.this.cookTime;
                case 3:
                    return BlockForgeTileBase.this.totalCookTime;
                case 4:
                    return BlockForgeTileBase.this.show_inventory_settings;
                default:
                    return 0;
            }
        }

        public void set(int index, int value) {
            switch (index) {
                case 0:
                    BlockForgeTileBase.this.furnaceBurnTime = value;
                    break;
                case 1:
                    BlockForgeTileBase.this.recipesUsed = value;
                    break;
                case 2:
                    BlockForgeTileBase.this.cookTime = value;
                    break;
                case 3:
                    BlockForgeTileBase.this.totalCookTime = value;
                    break;
                case 4:
                    BlockForgeTileBase.this.show_inventory_settings = value;
                    break;
            }

        }

        @Override
        public int getCount() {
            return 5;
        }
    };

    private int getUpgrade(ItemStack stack) {
        if (stack.getItem() instanceof ItemOreProcessing) {
            return 1;
        } else if (stack.getItem() instanceof ItemFuelEfficiency) {
            return 2;
        } else if (stack.getItem() instanceof ItemColorUpgrade) {
            return 3;
        }else if (stack.getItem() instanceof ItemLiquidFuel) {
            return 4;
        }else if (stack.getItem() instanceof ItemEnergyFuel) {
            return 5;
        }else if (stack.getItem() == Registration.FACTORY.get()) {
            return 6;
        }else if (stack.getItem() == Registration.XP.get()) {
            return 7;
        }else if (stack.getItem().getRegistryName().equals("ultimatefurnaces_bfr:ultimate_ore_processing_upgrade")){
            return 8;
        }
        return 0;
    }
    protected final FluidTank fluidTank = new FluidTank(8000, fs -> {
        if (ForgeHooks.getBurnTime(new ItemStack(fs.getFluid().getBucket())) > 0)
            return true;
        return false;
    }){
        @Override
        protected void onContentsChanged() {
            super.onContentsChanged();
            setChanged();
            level.sendBlockUpdated(getBlockPos(), level.getBlockState(getBlockPos()), getBlockState(), 2);
        }
    };
    protected final FluidTank xpTank = new FluidTank(2000, xp -> {
        if (xp.getFluid().getRegistryName().toString().equals(Config.getLiquidXPType()) && ModList.get().isLoaded(Config.getLiquidXPMod()))
            return true;
        return false;
    }){
        @Override
        protected void onContentsChanged() {
            super.onContentsChanged();
            setChanged();
            level.sendBlockUpdated(getBlockPos(), level.getBlockState(getBlockPos()), getBlockState(), 2);
        }
    };
    private final EnergyStorage energyStorage = new EnergyStorage(32000,1000,1000, 0) {
        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            int retval = super.receiveEnergy(maxReceive, simulate);
            if (!simulate) {
                setChanged();
                level.sendBlockUpdated(getBlockPos(), level.getBlockState(getBlockPos()), getBlockState(), 2);
            }
            return retval;
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            int retval = super.extractEnergy(maxExtract, simulate);
            if (!simulate) {
                setChanged();
                level.sendBlockUpdated(getBlockPos(), level.getBlockState(getBlockPos()), getBlockState(), 2);
            }
            return retval;
        }
    };

    public void forceUpdateAllStates() {
        BlockState state = level.getBlockState(worldPosition);
        if (state.getValue(BlockStateProperties.LIT) != this.isBurning()) {
            level.setBlock(worldPosition, state.setValue(BlockStateProperties.LIT, this.isBurning()), 3);
        }
    }

    @Override
    public void tick() {
        if (furnaceSettings.size() <= 0) {
            furnaceSettings = new FurnaceSettings() {
                @Override
                public void onChanged() {
                    setChanged();
                }
            };
        }
        boolean wasBurning = this.isBurning();
        boolean flag1 = false;
        boolean flag2 = false;

        if (this.isBurning()) {
            --this.furnaceBurnTime;
        }

            if ((getUpgrade(getItem(12)) == 3)) {
                if (!(level.getBlockState(getBlockPos()).getValue(BlockForgeBase.COLORED)))
                    level.setBlock(getBlockPos(), level.getBlockState(getBlockPos()).setValue(BlockForgeBase.COLORED, true), 3);
            } else if ((level.getBlockState(getBlockPos()).getValue(BlockForgeBase.COLORED))) level.setBlock(getBlockPos(), level.getBlockState(getBlockPos()).setValue(BlockForgeBase.COLORED, false), 3);
        if (this.recipeType != IRecipeType.SMELTING) {
                this.recipeType = IRecipeType.SMELTING;
        }

        if (!this.level.isClientSide) {
            int get_cook_time = getCookTime();
            timer++;

            if (currentUpgrade != getUpgrade(this.getItem(7))) {
                this.currentUpgrade = getUpgrade(this.getItem(7));
            }


            if (this.totalCookTime != get_cook_time) {
                this.totalCookTime = get_cook_time;
            }
            int mode = this.getRedstoneSetting();
            if (mode != 0) {
                if (mode == 2) {
                    int i = 0;
                    for (Direction side : Direction.values()) {
                        if (level.getSignal(worldPosition.offset(side.getNormal()), side) > 0) {
                            i++;
                        }
                    }
                    if (i != 0) {
                        this.cookTime = 0;
                        this.furnaceBurnTime = 0;
                        forceUpdateAllStates();
                        return;
                    }
                }
                if (mode == 1) {
                    boolean flag = false;
                    for (Direction side : Direction.values()) {

                        if (level.getSignal(worldPosition.offset(side.getNormal()), side) > 0) {
                            flag = true;
                        }
                    }
                    if (!flag) {
                        this.cookTime = 0;
                        this.furnaceBurnTime = 0;
                        forceUpdateAllStates();
                        return;
                    }
                }
                for (int i = 0; i < Direction.values().length; i++)
                    this.provides[i] = getBlockState().getDirectSignal(this.level, worldPosition, DirectionUtil.fromId(i));

            } else {
                for (int i = 0; i < Direction.values().length; i++)
                    this.provides[i] = 0;
            }
            if (this.doesNeedUpdateSend()) {
                this.onUpdateSent();
            }

            ItemStack itemstack = this.inventory.get(FUEL);
            if ((this.isBurning() || !itemstack.isEmpty() || isLiquid() || isEnergy() || isBurning()) && (!this.inventory.get(INPUT).isEmpty() || !this.inventory.get(INPUT1).isEmpty() || !this.inventory.get(INPUT2).isEmpty())) {
                Optional<AbstractCookingRecipe> irecipe = Optional.empty();
                Optional<AbstractCookingRecipe> irecipe1 = Optional.empty();
                Optional<AbstractCookingRecipe> irecipe2 = Optional.empty();
                if (!this.inventory.get(INPUT).isEmpty()) {
                    irecipe = grabRecipe(getItem(INPUT));
                }
                if (!this.inventory.get(INPUT1).isEmpty()) {
                    irecipe1 = grabRecipe(getItem(INPUT1));
                }
                if (!this.inventory.get(INPUT2).isEmpty()) {
                    irecipe2 = grabRecipe(getItem(INPUT2));
                }

                boolean valid = this.canSmelt(irecipe.orElse(null), INPUT, OUTPUT);
                boolean valid1 = this.canSmelt(irecipe1.orElse(null), INPUT1, OUTPUT1);
                boolean valid2 = this.canSmelt(irecipe2.orElse(null), INPUT2, OUTPUT2);
                if (!this.isBurning() && (valid || valid1 || valid2)) {
                    if (isLiquid() && fluidTank.getFluidAmount() >= 10 ){
                        int f = getBurnTime(new ItemStack(fluidTank.getFluidInTank(1).getFluid().getBucket()));
                        this.furnaceBurnTime = f * get_cook_time / 20000;
                        if (!this.getItem(8).isEmpty() && getUpgrade(getItem(8)) == 2)
                            this.furnaceBurnTime = 2 * f * get_cook_time / 20000;
                        this.recipesUsed = this.furnaceBurnTime;
                            fluidTank.drain(10, IFluidHandler.FluidAction.EXECUTE);
                    }else if (isEnergy()){
                        this.furnaceBurnTime = 200 * get_cook_time / 200;
                        if (!this.getItem(8).isEmpty() && getUpgrade(getItem(8)) == 2)
                            this.furnaceBurnTime = 2 * 200 * get_cook_time / 200;
                        this.recipesUsed = this.furnaceBurnTime;
                        energyStorage.extractEnergy(200, false);
                    }else{
                        if (!this.getItem(8).isEmpty() && getUpgrade(getItem(8)) == 2){
                            this.furnaceBurnTime = 2 * (getBurnTime(itemstack)) * get_cook_time / 200;
                        }else{
                            this.furnaceBurnTime = getBurnTime(itemstack) * get_cook_time / 200;
                        }
                        this.recipesUsed = this.furnaceBurnTime;
                    }
                    if (this.isBurning()) {
                        flag1 = true;
                        if (!isLiquid() && !isEnergy())
                            if (itemstack.hasContainerItem()) this.inventory.set(FUEL, itemstack.getContainerItem());
                            else if (!itemstack.isEmpty()) {
                                itemstack.shrink(1);
                                if (itemstack.isEmpty()) {
                                    this.inventory.set(FUEL, itemstack.getContainerItem());
                                }
                            }
                    }
                    if (!this.getItem(8).isEmpty() && getUpgrade(getItem(8)) == 2) {
                        breakDurabilityItem(getItem(8));
                    }
                }
                if (this.isBurning() && (valid || valid1 || valid2)) {
                    ++this.cookTime;
                    if (this.cookTime >= this.totalCookTime) {
                        this.cookTime = 0;
                        this.totalCookTime = this.getCookTime();
                        this.smeltItem(irecipe.orElse(null), INPUT, OUTPUT);
                        this.smeltItem(irecipe1.orElse(null), INPUT1, OUTPUT1);
                        this.smeltItem(irecipe2.orElse(null), INPUT2, OUTPUT2);
                        for (int slot : UPMIS)
                        if (getUpgrade(getItem(slot)) == 6) this.autoIO();
                        flag1 = true;
                    }
                } else {
                    this.cookTime = 0;
                }
            } else if (!this.isBurning() && this.cookTime > 0) {
                this.cookTime = MathHelper.clamp(this.cookTime - 2, 0, this.totalCookTime);
            }
            if (wasBurning != this.isBurning()) {
                flag1 = true;
                this.level.setBlock(this.worldPosition, this.level.getBlockState(this.worldPosition).setValue(BlockStateProperties.LIT, this.isBurning()), 3);
            }
            for (int slot : UPMIS)
            if ((timer % 24 == 0) && (getUpgrade(getItem(slot)) == 6)){
                if (this.cookTime <= 0) {

                    if (this.getItem(INPUT).isEmpty() || this.getItem(INPUT1).isEmpty() || this.getItem(INPUT2).isEmpty()) {
                        this.autoIO();
                        flag1 = true;
                    } else if (this.getItem(INPUT).getCount() < this.getItem(INPUT).getMaxStackSize() || this.getItem(INPUT1).getCount() < this.getItem(INPUT1).getMaxStackSize() || this.getItem(INPUT2).getCount() < this.getItem(INPUT2).getMaxStackSize()) {
                        this.autoIO();
                        flag1 = true;
                    }
                    if (this.getItem(FUEL).isEmpty()) {
                        this.autoIO();
                        flag1 = true;
                    } else if (this.getItem(FUEL).getCount() < this.getItem(FUEL).getMaxStackSize()) {
                        this.autoIO();
                        flag1 = true;
                    }
                }
            }
        }

        if (flag1) {
            this.setChanged();
        }

    }
    public int hex() {
            CompoundNBT nbt = inventory.get(12).getTag();

            return ((nbt.getInt("red") & 0x0ff) << 16) | ((nbt.getInt("green") & 0x0ff) << 8) | (nbt.getInt("blue") & 0x0ff);
    }


    private void autoIO() {
        for (Direction dir : Direction.values()) {
            TileEntity tile = level.getBlockEntity(worldPosition.offset(dir.getNormal()));
            if (tile == null) {
                continue;
            }
            if (this.furnaceSettings.get(dir.ordinal()) == 1 || this.furnaceSettings.get(dir.ordinal()) == 2 || this.furnaceSettings.get(dir.ordinal()) == 3 || this.furnaceSettings.get(dir.ordinal()) == 4) {
                if (tile != null) {
                    IItemHandler other = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, dir.getOpposite()).map(other1 -> other1).orElse(null);

                    if (other == null) {
                        continue;
                    }
                    if (other != null) {
                        if (this.getAutoInput() != 0 || this.getAutoOutput() != 0) {
                            if (this.getAutoInput() == 1) {
                                if (this.furnaceSettings.get(dir.ordinal()) == 1 || this.furnaceSettings.get(dir.ordinal()) == 3) {
                                    if (this.getItem(INPUT).getCount() >= this.getItem(INPUT).getMaxStackSize() && (this.getItem(INPUT1).getCount() >= this.getItem(INPUT1).getMaxStackSize()) && (this.getItem(INPUT2).getCount() >= this.getItem(INPUT2).getMaxStackSize())) {
                                        continue;
                                    }
                                    for (int i = 0; i < other.getSlots(); i++) {
                                        if (other.getStackInSlot(i).isEmpty()) {
                                            continue;
                                        }
                                        ItemStack stack = other.extractItem(i, other.getStackInSlot(i).getMaxStackSize(), true);
                                        if (hasRecipe(stack) && getItem(INPUT).isEmpty() || ItemHandlerHelper.canItemStacksStack(getItem(INPUT), stack))
                                            insertItemInternal(INPUT, other.extractItem(i, other.getStackInSlot(i).getMaxStackSize() - this.getItem(INPUT).getCount(), false), false);
                                        if (hasRecipe(stack) && getItem(INPUT1).isEmpty() || ItemHandlerHelper.canItemStacksStack(getItem(INPUT1), stack))
                                            insertItemInternal(INPUT1, other.extractItem(i, other.getStackInSlot(i).getMaxStackSize() - this.getItem(INPUT1).getCount(), false), false);
                                        if (hasRecipe(stack) && getItem(INPUT2).isEmpty() || ItemHandlerHelper.canItemStacksStack(getItem(INPUT2), stack))
                                            insertItemInternal(INPUT2, other.extractItem(i, other.getStackInSlot(i).getMaxStackSize() - this.getItem(INPUT2).getCount(), false), false);
                                    }
                                }
                                if (this.furnaceSettings.get(dir.ordinal()) == 4) {
                                    if (this.getItem(FUEL).getCount() >= this.getItem(FUEL).getMaxStackSize()) {
                                        continue;
                                    }
                                    for (int i = 0; i < other.getSlots(); i++) {
                                        if (other.getStackInSlot(i).isEmpty()) {
                                            continue;
                                        }
                                        ItemStack stack = other.extractItem(i, other.getStackInSlot(i).getMaxStackSize(), true);
                                        if (isItemFuel(stack) && getItem(FUEL).isEmpty() || ItemHandlerHelper.canItemStacksStack(getItem(FUEL), stack)) {
                                            insertItemInternal(FUEL, other.extractItem(i, other.getStackInSlot(i).getMaxStackSize() - this.getItem(FUEL).getCount(), false), false);
                                        }
                                    }
                                }
                            }

                            if (this.getAutoOutput() == 1) {

                                if (this.furnaceSettings.get(dir.ordinal()) == 4) {
                                    if (this.getItem(FUEL).isEmpty()) {
                                        continue;
                                    }
                                    ItemStack stack = extractItemInternal(FUEL, 1, true);
                                    if (stack.getItem() != Items.BUCKET) {
                                        continue;
                                    }
                                    for (int i = 0; i < other.getSlots(); i++) {
                                        if (other.isItemValid(i, stack) && (other.getStackInSlot(i).isEmpty() || (ItemHandlerHelper.canItemStacksStack(other.getStackInSlot(i), stack) && other.getStackInSlot(i).getCount() + stack.getCount() <= other.getSlotLimit(i)))) {
                                            other.insertItem(i, extractItemInternal(FUEL, stack.getCount(), false), false);
                                        }
                                    }
                                }

                                if (this.furnaceSettings.get(dir.ordinal()) == 2 || this.furnaceSettings.get(dir.ordinal()) == 3) {
                                    if (this.getItem(OUTPUT).isEmpty() && this.getItem(OUTPUT1).isEmpty() && this.getItem(OUTPUT2).isEmpty()) {
                                        continue;
                                    }
                                    if (tile.getBlockState().getBlock().getRegistryName().toString().contains("storagedrawers:")) {
                                        continue;
                                    }
                                    for (int i = 0; i < other.getSlots(); i++) {
                                        ItemStack stack = extractItemInternal(OUTPUT, this.getItem(OUTPUT).getMaxStackSize() - other.getStackInSlot(i).getCount(), true);
                                        ItemStack stack1 = extractItemInternal(OUTPUT1, this.getItem(OUTPUT1).getMaxStackSize() - other.getStackInSlot(i).getCount(), true);
                                        ItemStack stack2 = extractItemInternal(OUTPUT2, this.getItem(OUTPUT2).getMaxStackSize() - other.getStackInSlot(i).getCount(), true);
                                        if (other.isItemValid(i, stack) && (other.getStackInSlot(i).isEmpty() || (ItemHandlerHelper.canItemStacksStack(other.getStackInSlot(i), stack) && other.getStackInSlot(i).getCount() + stack.getCount() <= other.getSlotLimit(i)))) {
                                            other.insertItem(i, extractItemInternal(OUTPUT, stack.getCount(), false), false);
                                        }
                                        if (other.isItemValid(i, stack1) && (other.getStackInSlot(i).isEmpty() || (ItemHandlerHelper.canItemStacksStack(other.getStackInSlot(i), stack1) && other.getStackInSlot(i).getCount() + stack1.getCount() <= other.getSlotLimit(i)))) {
                                            other.insertItem(i, extractItemInternal(OUTPUT1, stack1.getCount(), false), false);
                                        }
                                        if (other.isItemValid(i, stack2) && (other.getStackInSlot(i).isEmpty() || (ItemHandlerHelper.canItemStacksStack(other.getStackInSlot(i), stack2) && other.getStackInSlot(i).getCount() + stack2.getCount() <= other.getSlotLimit(i)))) {
                                            other.insertItem(i, extractItemInternal(OUTPUT2, stack2.getCount(), false), false);
                                        }
                                    }
                                }
                            }
                        }
                        }
                    }

                }
            }
        }

    @Nonnull
    public ItemStack insertItemInternal(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (stack.isEmpty())
            return ItemStack.EMPTY;

        if (!canPlaceItemThroughFace(slot, stack, null))
            return stack;

        ItemStack existing = this.inventory.get(slot);

        int limit = stack.getMaxStackSize();

        if (!existing.isEmpty()) {
            if (!ItemHandlerHelper.canItemStacksStack(stack, existing))
                return stack;

            limit -= existing.getCount();
        }

        if (limit <= 0)
            return stack;

        boolean reachedLimit = stack.getCount() > limit;

        if (!simulate) {
            if (existing.isEmpty()) {
                this.inventory.set(slot, reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, limit) : stack);
            } else {
                existing.grow(reachedLimit ? limit : stack.getCount());
            }
            this.setChanged();
        }

        return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - limit) : ItemStack.EMPTY;
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

    //CLIENT SYNC
    public int getSettingBottom() {
        return this.furnaceSettings.get(0);
    }

    public int getSettingTop() {
        return this.furnaceSettings.get(1);
    }

    public int getSettingFront() {
        int i = DirectionUtil.getId(this.getBlockState().getValue(BlockStateProperties.FACING));
        return this.furnaceSettings.get(i);
    }

    public int getSettingBack() {
        int i = DirectionUtil.getId(this.getBlockState().getValue(BlockStateProperties.FACING).getOpposite());
        return this.furnaceSettings.get(i);
    }

    public int getSettingLeft() {
        Direction facing = this.getBlockState().getValue(BlockStateProperties.FACING);
        if (facing == Direction.NORTH) {
            return this.furnaceSettings.get(DirectionUtil.getId(Direction.EAST));
        } else if (facing == Direction.WEST) {
            return this.furnaceSettings.get(DirectionUtil.getId(Direction.NORTH));
        } else if (facing == Direction.SOUTH) {
            return this.furnaceSettings.get(DirectionUtil.getId(Direction.WEST));
        } else {
            return this.furnaceSettings.get(DirectionUtil.getId(Direction.SOUTH));
        }
    }

    public int getSettingRight() {
        Direction facing = this.getBlockState().getValue(BlockStateProperties.FACING);
        if (facing == Direction.NORTH) {
            return this.furnaceSettings.get(DirectionUtil.getId(Direction.WEST));
        } else if (facing == Direction.WEST) {
            return this.furnaceSettings.get(DirectionUtil.getId(Direction.SOUTH));
        } else if (facing == Direction.SOUTH) {
            return this.furnaceSettings.get(DirectionUtil.getId(Direction.EAST));
        } else {
            return this.furnaceSettings.get(DirectionUtil.getId(Direction.NORTH));
        }
    }

    public int getIndexFront() {
        int i = this.getBlockState().getValue(BlockStateProperties.FACING).ordinal();
        return i;
    }

    public int getIndexBack() {
        int i = this.getBlockState().getValue(BlockStateProperties.FACING).getOpposite().ordinal();
        return i;
    }

    public int getIndexLeft() {
        Direction facing = this.getBlockState().getValue(BlockStateProperties.FACING);
        if (facing == Direction.NORTH) {
            return Direction.EAST.ordinal();
        } else if (facing == Direction.WEST) {
            return Direction.NORTH.ordinal();
        } else if (facing == Direction.SOUTH) {
            return Direction.WEST.ordinal();
        } else {
            return Direction.SOUTH.ordinal();
        }
    }

    public int getIndexRight() {
        Direction facing = this.getBlockState().getValue(BlockStateProperties.FACING);
        if (facing == Direction.NORTH) {
            return Direction.WEST.ordinal();
        } else if (facing == Direction.WEST) {
            return Direction.SOUTH.ordinal();
        } else if (facing == Direction.SOUTH) {
            return Direction.EAST.ordinal();
        } else {
            return Direction.NORTH.ordinal();
        }
    }

    public int getAutoInput() {
        return this.furnaceSettings.get(6);
    }

    public int getAutoOutput() {
        return this.furnaceSettings.get(7);
    }

    public int getRedstoneSetting() {
        return this.furnaceSettings.get(8);
    }

    public int getRedstoneComSub() {
        return this.furnaceSettings.get(9);
    }



    public boolean isBurning() {
        return this.furnaceBurnTime > 0;
    }

    protected boolean canSmelt(@Nullable IRecipe<?> recipe, int INPUT, int OUTPUT) {
        ItemStack input = this.inventory.get(INPUT);
        if (!input.isEmpty() && recipe != null) {
            ItemStack recipeOutput = recipe.getResultItem();
            if (!recipeOutput.isEmpty()) {
                ItemStack output = this.inventory.get(OUTPUT);
                if (output.isEmpty()) return true;
                else if (!output.sameItem(recipeOutput)) return false;
                else {
                    if ((getUpgrade(this.getItem(7)) == 1) && (input.getItem().is(ore))) {
                        return output.getCount() + recipeOutput.getCount() * 2 <= output.getMaxStackSize();
                    }else if ((getUpgrade(this.getItem(7)) == 8) && (input.getItem().is(ore))) {
                        return output.getCount() + recipeOutput.getCount() * 4 <= output.getMaxStackSize();
                    }else{
                        return output.getCount() + recipeOutput.getCount() <= output.getMaxStackSize();
                    }
                }
            }
        }
        return false;
    }
    private ItemStack getResult(@Nullable IRecipe<?> recipe, ItemStack input) {
        ItemStack itemstack = input;
        ItemStack out = recipe.getResultItem().copy();
        if (((itemstack.getItem().is(ore)))) {
            if ((getUpgrade(this.getItem(7)) == 1)) {
                out.setCount(out.getCount() * 2);
                return out;
            }
            if ((getUpgrade(this.getItem(7)) == 8)) {
                out.setCount(out.getCount() * 4);
                return out;
            }
        }
            return recipe.getResultItem().copy();
    }

    ITag<Item> ore = ItemTags.getAllTags().getTag(new ResourceLocation("forge", "ores"));
    protected void smeltItem(@Nullable IRecipe<?> recipe, int INPUT, int OUTPUT) {
        timer = 0;
        if (recipe != null && this.canSmelt(recipe, INPUT, OUTPUT)) {
            ItemStack itemstack = this.inventory.get(INPUT);
            ItemStack itemstack2 = this.inventory.get(OUTPUT);
            if (itemstack2.isEmpty()) {
                this.inventory.set(OUTPUT, getResult(recipe, itemstack));
                if ((getUpgrade(this.getItem(7)) == 1) && ((itemstack.getItem().is(ore)))) {
                    breakDurabilityItem(getItem(7));
                    if ((this.getItem(9).getItem() == Registration.ORE_PROCESSING.get())) {
                        breakDurabilityItem(getItem(9));
                    }
                }
            } else if (itemstack2.getItem() == getResult(recipe, itemstack).getItem()) {
                itemstack2.grow(getResult(recipe, itemstack).getCount());
                if ((getUpgrade(this.getItem(7)) == 1) && (itemstack.getItem().is(ore))) {
                    breakDurabilityItem(getItem(7));
                    if ((this.getItem(9).getItem() == Registration.ORE_PROCESSING.get())) {
                        breakDurabilityItem(getItem(9));
                    }
                }
            }
            this.checkXP(recipe);
            if (!this.level.isClientSide) {
                this.setRecipeUsed(recipe);
            }

            if (itemstack.getItem() == Blocks.WET_SPONGE.asItem() && !this.inventory.get(FUEL).isEmpty() && this.inventory.get(FUEL).getItem() == Items.BUCKET) {
                this.inventory.set(FUEL, new ItemStack(Items.WATER_BUCKET));
            }
            if (ModList.get().isLoaded("pmmo")) {
                FurnaceHandler.handleSmelted(itemstack, itemstack2, level, worldPosition, 0);
                if (this.recipeType == IRecipeType.SMOKING) {
                    FurnaceHandler.handleSmelted(itemstack, itemstack2, level, worldPosition, 1);
                }
            }
            itemstack.shrink(1);
        }
    }
    @Override
    public void load(BlockState state, CompoundNBT tag) {
        ItemStackHelper.loadAllItems(tag, this.inventory);
        this.furnaceBurnTime = tag.getInt("BurnTime");
        this.cookTime = tag.getInt("CookTime");
        this.totalCookTime = tag.getInt("CookTimeTotal");
        this.timer = 0;
        this.currentUpgrade = tag.getInt("Upgrade");
        this.recipesUsed = this.getBurnTime(this.inventory.get(1));
        if (tag.get("fluidTank") != null)
            CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.readNBT(fluidTank, null, tag.get("fluidTank"));
        if (tag.get("xpTank") != null)
            CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.readNBT(xpTank, null, tag.get("xpTank"));
        CompoundNBT compoundnbt = tag.getCompound("RecipesUsed");
        if (tag.get("energyStorage") != null)
            CapabilityEnergy.ENERGY.readNBT(energyStorage, null, tag.get("energyStorage"));

        for (String s : compoundnbt.getAllKeys()) {
            this.recipes.put(new ResourceLocation(s), compoundnbt.getInt(s));
        }
        this.show_inventory_settings = tag.getInt("ShowInvSettings");
        this.furnaceSettings.read(tag);
        /**
         CompoundNBT energyTag = tag.getCompound("energy");
         energy.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(energyTag));
         **/

        super.load(state, tag);
    }

    @Override
    public CompoundNBT save(CompoundNBT tag) {
        super.save(tag);
        ItemStackHelper.saveAllItems(tag, this.inventory);
        tag.putInt("BurnTime", this.furnaceBurnTime);
        tag.putInt("CookTime", this.cookTime);
        tag.putInt("CookTimeTotal", this.totalCookTime);
        tag.putInt("Upgrade", this.currentUpgrade);
        tag.put("fluidTank", CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.writeNBT(fluidTank, null));
        tag.put("xpTank", CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.writeNBT(xpTank, null));
        tag.put("energyStorage", CapabilityEnergy.ENERGY.writeNBT(energyStorage, null));

        tag.putInt("ShowInvSettings", this.show_inventory_settings);
        this.furnaceSettings.write(tag);
        CompoundNBT compoundnbt = new CompoundNBT();
        this.recipes.forEach((recipeId, craftedAmount) -> {
            compoundnbt.putInt(recipeId.toString(), craftedAmount);
        });
        tag.put("RecipesUsed", compoundnbt);

        return tag;
    }

    protected static int getBurnTime(ItemStack stack) {
        if (stack.isEmpty()) {
            return 0;
        } else {
            Item item = stack.getItem();
            int ret = stack.getBurnTime();
            return net.minecraftforge.event.ForgeEventFactory.getItemBurnTime(stack, ret == -1 ? AbstractFurnaceTileEntity.getFuel().getOrDefault(item, 0) : ret);
        }
    }


    public static boolean isItemFuel(ItemStack stack) {
        return getBurnTime(stack) > 0;
    }

    public static boolean isItemUpgrade(ItemStack stack) {
        return stack.getItem() instanceof ItemUpgradeMisc || stack.getItem() instanceof ItemOreProcessing || stack.getItem() instanceof ItemFuelEfficiency;
    }

    LazyOptional<? extends IItemHandler>[] invHandlers =
            net.minecraftforge.items.wrapper.SidedInvWrapper.create(this, Direction.DOWN, Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST);

    @Nonnull
    @Override
    public <
            T> LazyOptional<T> getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @Nullable Direction facing) {

        if (!this.isRemoved() && facing != null && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (facing == Direction.DOWN)
                return invHandlers[0].cast();
            else if (facing == Direction.UP)
                return invHandlers[1].cast();
            else if (facing == Direction.NORTH)
                return invHandlers[2].cast();
            else if (facing == Direction.SOUTH)
                return invHandlers[3].cast();
            else if (facing == Direction.WEST)
                return invHandlers[4].cast();
            else
                return invHandlers[5].cast();
        }
        if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            if (!this.isRemoved() && (getUpgrade(getItem(10)) == 4)) {
                return (LazyOptional.of(() -> fluidTank).cast());
            }
            if (!this.isRemoved() && hasXPTank()) {
                return (LazyOptional.of(() -> xpTank).cast());
            }
        }
        if (!this.isRemoved() && (getUpgrade(getItem(10)) == 5) && capability == CapabilityEnergy.ENERGY)
            return LazyOptional.of(() -> energyStorage).cast();

        return super.getCapability(capability, facing);
    }


    @Override
    public int[] IgetSlotsForFace(Direction side) {
        if (this.furnaceSettings.get(DirectionUtil.getId(side)) == 0) {
            return new int[]{};
        } else if (this.furnaceSettings.get(DirectionUtil.getId(side)) == 1) {
            return new int[]{0, 1};
        } else if (this.furnaceSettings.get(DirectionUtil.getId(side)) == 2) {
            return new int[]{2};
        } else if (this.furnaceSettings.get(DirectionUtil.getId(side)) == 3) {
            return new int[]{0, 1, 2};
        } else if (this.furnaceSettings.get(DirectionUtil.getId(side)) == 4) {
            return new int[]{1};
        }
        return new int[]{};
    }

    @Override
    public boolean IcanExtractItem(int index, ItemStack stack, Direction direction) {
        if (this.furnaceSettings.get(DirectionUtil.getId(direction)) == 0) {
            return false;
        } else if (this.furnaceSettings.get(DirectionUtil.getId(direction)) == 1) {
            return false;
        } else if (this.furnaceSettings.get(DirectionUtil.getId(direction)) == 2) {
            return (index == 4 || index == 5 || index == 6);
        } else if (this.furnaceSettings.get(DirectionUtil.getId(direction)) == 3) {
            return (index == 4 || index == 5 || index == 6);
        } else if (this.furnaceSettings.get(DirectionUtil.getId(direction)) == 4 && stack.getItem() != Items.BUCKET) {
            return false;
        } else if (this.furnaceSettings.get(DirectionUtil.getId(direction)) == 4 && stack.getItem() == Items.BUCKET) {
            return true;
        }
        return false;
    }

    @Override
    public boolean IisItemValidForSlot(int index, ItemStack stack) {
        if (index == OUTPUT || index == OUTPUT1 || index == OUTPUT2 || index > 6) {
            return false;
        }
        if (index == INPUT || index == INPUT1 || index == INPUT2) {
            if (stack.isEmpty()) {
                return false;
            }

            return hasRecipe(stack);

        }
        if (index == FUEL) {
            ItemStack itemstack = this.inventory.get(FUEL);
            return getBurnTime(stack) > 0 || (stack.getItem() == Items.BUCKET && itemstack.getItem() != Items.BUCKET);
        }
        return false;
    }

    public void checkXP(@Nullable IRecipe<?> recipe) {
        if (!level.isClientSide) {
            boolean flag2 = false;
            if (this.recipes.size() > Config.furnaceXPDropValue.get()) {
                this.grantStoredRecipeExperience(level, new Vector3d(worldPosition.getX() + rand.nextInt(2) - 1, worldPosition.getY(), worldPosition.getZ() + rand.nextInt(2) - 1));
                this.recipes.clear();
            } else {
                for (Object2IntMap.Entry<ResourceLocation> entry : this.recipes.object2IntEntrySet()) {
                    if (level.getRecipeManager().byKey(entry.getKey()).isPresent()) {
                        if (entry.getIntValue() > Config.furnaceXPDropValue2.get()) {
                            if (!flag2) {
                                this.grantStoredRecipeExperience(level, new Vector3d(worldPosition.getX() + rand.nextInt(2) - 1, worldPosition.getY(), worldPosition.getZ() + rand.nextInt(2) - 1));
                            }
                            flag2 = true;
                        }
                    }

                }
                if (flag2) {
                    this.recipes.clear();
                }
            }
        }
    }

    @Override
    public void setRecipeUsed(@Nullable IRecipe<?> recipe) {

        if (recipe != null) {
            ResourceLocation resourcelocation = recipe.getId();
            this.recipes.addTo(resourcelocation, 1);
        }

    }

    @Nullable
    @Override
    public IRecipe<?> getRecipeUsed() {
        return null;
    }

    public void unlockRecipes(PlayerEntity player) {
        List<IRecipe<?>> list = this.grantStoredRecipeExperience(player.level, player.position());
        player.awardRecipes(list);
        this.recipes.clear();
    }

    public List<IRecipe<?>> grantStoredRecipeExperience(World level, Vector3d worldPosition) {
        List<IRecipe<?>> list = Lists.newArrayList();

        for (Object2IntMap.Entry<ResourceLocation> entry : this.recipes.object2IntEntrySet()) {
            level.getRecipeManager().byKey(entry.getKey()).ifPresent((h) -> {
                list.add(h);
                if (hasXPTank()) xpTank.fill(new FluidStack(ForgeRegistries.FLUIDS.getValue(new ResourceLocation(Config.getLiquidXPType())), MathHelper.floor((float) entry.getIntValue() * ((AbstractCookingRecipe) h).getExperience()) * 5), IFluidHandler.FluidAction.EXECUTE);
                else splitAndSpawnExperience(level, worldPosition, entry.getIntValue(), ((AbstractCookingRecipe) h).getExperience());

            });
        }

        return list;
    }

    private static void splitAndSpawnExperience(World level, Vector3d worldPosition, int craftedAmount, float experience) {
        int i = MathHelper.floor((float) craftedAmount * experience);
        float f = MathHelper.frac((float) craftedAmount * experience);
        if (f != 0.0F && Math.random() < (double) f) {
            ++i;
        }

        while (i > 0) {
            int j = ExperienceOrbEntity.getExperienceValue(i);
            i -= j;
            level.addFreshEntity(new ExperienceOrbEntity(level, worldPosition.x, worldPosition.y, worldPosition.z, j));
        }

    }

    @Override
    public void fillStackedContents(RecipeItemHelper helper) {
        for (ItemStack itemstack : this.inventory) {
            helper.accountStack(itemstack);
        }

    }

    protected boolean doesNeedUpdateSend() {
        return !Arrays.equals(this.provides, this.lastProvides);
    }

    public void onUpdateSent() {
        System.arraycopy(this.provides, 0, this.lastProvides, 0, this.provides.length);
        this.level.updateNeighborsAt(this.worldPosition, getBlockState().getBlock());
    }


    public void placeConfig() {

        if (this.furnaceSettings != null) {
            this.furnaceSettings.set(0, 2);
            this.furnaceSettings.set(1, 1);
            for (Direction dir : Direction.values()) {
                if (dir != Direction.DOWN && dir != Direction.UP) {
                    this.furnaceSettings.set(dir.ordinal(), 4);
                }
            }
            level.markAndNotifyBlock(worldPosition, level.getChunkAt(worldPosition), level.getBlockState(worldPosition).getBlock().defaultBlockState(), level.getBlockState(worldPosition), 3, 3);
        }

    }
}