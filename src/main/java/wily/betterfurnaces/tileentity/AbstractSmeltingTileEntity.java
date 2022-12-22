package wily.betterfurnaces.tileentity;

import com.google.common.collect.Lists;
import harmonised.pmmo.events.FurnaceHandler;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
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
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import org.apache.commons.lang3.ArrayUtils;
import wily.betterfurnaces.Config;
import wily.betterfurnaces.blocks.AbstractForgeBlock;
import wily.betterfurnaces.blocks.AbstractFurnaceBlock;
import wily.betterfurnaces.blocks.IronFurnaceBlock;
import wily.betterfurnaces.init.Registration;
import wily.betterfurnaces.items.OreProcessingUpgradeItem;
import wily.betterfurnaces.items.UpgradeItem;
import wily.betterfurnaces.items.UpgradeItemXpTank;
import wily.betterfurnaces.util.DirectionUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public abstract class AbstractSmeltingTileEntity extends InventoryTileEntity implements ITickableTileEntity, IRecipeHolder, IRecipeHelperPopulator {
    public final int[] provides = new int[Direction.values().length];
    protected final FluidTank fluidTank = new FluidTank(LiquidCapacity(), fs -> ForgeHooks.getBurnTime(new ItemStack(fs.getFluid().getBucket())) > 0) {
        @Override
        protected void onContentsChanged() {
            super.onContentsChanged();
            setChanged();
            level.sendBlockUpdated(getBlockPos(), level.getBlockState(getBlockPos()), getBlockState(), 2);
        }
    };
    protected final FluidTank xpTank = new FluidTank(2000, xp -> xp.getFluid().equals(Config.getLiquidXP()) && ModList.get().isLoaded(Config.getLiquidXPMod())) {
        @Override
        protected void onContentsChanged() {
            super.onContentsChanged();
            setChanged();
            level.sendBlockUpdated(getBlockPos(), level.getBlockState(getBlockPos()), getBlockState(), 2);
        }
    };
    private final int[] lastProvides = new int[this.provides.length];
    private final Object2IntOpenHashMap<ResourceLocation> recipes = new Object2IntOpenHashMap<>();
    private final EnergyStorage energyStorage = new EnergyStorage(EnergyCapacity(), 3400, 3400, 0) {
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
    public int show_inventory_settings;
    public int cookTime;
    public IRecipeType<? extends AbstractCookingRecipe> recipeType;
    public FactoryUpgradeSettings furnaceSettings;
    protected int timer;
    ITag<Item> ore = ItemTags.getAllTags().getTag(new ResourceLocation("forge", "ores"));

    LazyOptional<? extends IItemHandler>[] invHandlers =
            SidedInvWrapper.create(this, Direction.DOWN, Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST);
    private final Random rand = new Random();
    private int furnaceBurnTime;
    private int recipesUsed;
    private final LRUCache<Item, Optional<AbstractCookingRecipe>> cache = LRUCache.newInstance(Config.cache_capacity.get());
    private final LRUCache<Item, Optional<AbstractCookingRecipe>> blasting_cache = LRUCache.newInstance(Config.cache_capacity.get());
    private final LRUCache<Item, Optional<AbstractCookingRecipe>> smoking_cache = LRUCache.newInstance(Config.cache_capacity.get());
    public AbstractSmeltingTileEntity(TileEntityType<?> tileentitytypeIn, int invsize) {
        super(tileentitytypeIn, invsize);
        this.recipeType = IRecipeType.SMELTING;
        furnaceSettings = new FactoryUpgradeSettings(()->getUpgradeTypeSlotItem(Registration.FACTORY.get())) {
            @Override
            public void onChanged() {
                if (hasUpgradeType(Registration.FACTORY.get())) {
                    setItem(getUpgradeTypeSlot(Registration.FACTORY.get()), factory.get());

                }
                setChanged();
            }
        };

    }

    protected static int getBurnTime(ItemStack stack) {
        if (stack.isEmpty()) {
            return 0;
        } else {
            Item item = stack.getItem();
            int ret = stack.getBurnTime();
            return net.minecraftforge.event.ForgeEventFactory.getItemBurnTime(stack, ret == -1 ? AbstractFurnaceTileEntity.getFuel().getOrDefault(item, 0) : ret);
        }
    }    public int totalCookTime = this.getCookTime();

    public static boolean isItemFuel(ItemStack stack) {
        return getBurnTime(stack) > 0;
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

    public int FUEL() {return 1;}
    public int HEATER() {return FUEL();}
    public int[] UPGRADES(){ return new int[]{3,4,5};}
    public int FINPUT(){ return INPUTS()[0];}
    public int LINPUT(){ return INPUTS()[INPUTS().length - 1];}
    public int FOUTPUT(){ return OUTPUTS()[0];}
    public int LOUTPUT(){ return OUTPUTS()[OUTPUTS().length - 1];}
    public int[] INPUTS(){ return new int[]{0};}
    public int[] OUTPUTS(){ return new int[]{2};}
    public int[] FSLOTS(){ return  ArrayUtils.addAll(ISLOTS(), OUTPUTS());}
    public int[] ISLOTS(){ return  ArrayUtils.addAll(INPUTS(), FUEL());}

    public int EnergyUse() {return 600;}
    public int LiquidCapacity() {return 4000;}
    public int EnergyCapacity() {return 16000;}
    public boolean isForge() {return false;}

    public Direction facing() {
        return this.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
    }

    private int getFromCache(LRUCache<Item, Optional<AbstractCookingRecipe>> c, Item key) {
        if (c.get(key) == null) {
            return 0;
        }
        return c.get(key).orElse(null) == null ? 0 : c.get(key).orElse(null).getCookingTime();
    }

    public boolean hasRecipe(ItemStack stack) {
        return grabRecipe(stack).isPresent();
    }

    protected LRUCache<Item, Optional<AbstractCookingRecipe>> getCache() {

        if (hasUpgrade(Registration.BLAST.get())) {
            if (this.recipeType != IRecipeType.BLASTING) {
                this.recipeType = IRecipeType.BLASTING;
            }
        }
        if (hasUpgrade(Registration.SMOKE.get())) {
            if (this.recipeType != IRecipeType.SMOKING) {
                this.recipeType = IRecipeType.SMOKING;
            }
        }
        if (!((hasUpgrade(Registration.SMOKE.get()))) && !((hasUpgrade(Registration.BLAST.get())))) {
            if (this.recipeType != IRecipeType.SMELTING) {
                this.recipeType = IRecipeType.SMELTING;
            }
        }
        if (this.recipeType == IRecipeType.BLASTING) {
            return blasting_cache;
        }
        if (this.recipeType == IRecipeType.SMOKING) {
            return smoking_cache;
        }
        return cache;
    }

    private Optional<AbstractCookingRecipe> getRecipe(ItemStack stack, IRecipeType recipeType) {
        return this.level.getRecipeManager().getRecipeFor((IRecipeType<AbstractCookingRecipe>) recipeType, new Inventory(stack), this.level);
    }    public final IIntArray fields = new IIntArray() {
        public int get(int index) {
            switch (index) {
                case 0:
                    return AbstractSmeltingTileEntity.this.furnaceBurnTime;
                case 1:
                    return AbstractSmeltingTileEntity.this.recipesUsed;
                case 2:
                    return AbstractSmeltingTileEntity.this.cookTime;
                case 3:
                    return AbstractSmeltingTileEntity.this.totalCookTime;
                case 4:
                    return AbstractSmeltingTileEntity.this.show_inventory_settings;
                default:
                    return 0;
            }
        }

        public void set(int index, int value) {
            switch (index) {
                case 0:
                    AbstractSmeltingTileEntity.this.furnaceBurnTime = value;
                    break;
                case 1:
                    AbstractSmeltingTileEntity.this.recipesUsed = value;
                    break;
                case 2:
                    AbstractSmeltingTileEntity.this.cookTime = value;
                    break;
                case 3:
                    AbstractSmeltingTileEntity.this.totalCookTime = value;
                    break;
                case 4:
                    AbstractSmeltingTileEntity.this.show_inventory_settings = value;
                    break;
            }

        }

        @Override
        public int getCount() {
            return 5;
        }
    };

    private Optional<AbstractCookingRecipe> grabRecipe(ItemStack stack) {
        Item item = stack.getItem();
        if (item instanceof AirItem) {
            return Optional.empty();
        }
        Optional<AbstractCookingRecipe> recipe = getCache().get(item);
        if (recipe == null) {
            recipe = getRecipe(stack, this.recipeType);
            getCache().put(item, recipe);
        }
        return recipe;
    }

    public boolean isLiquid() {
        return (hasUpgrade(Registration.LIQUID.get()));
    }

    public boolean hasXPTank() {
        return hasUpgrade(Registration.XP.get());
    }

    private boolean isEnergy() {
        return ((hasUpgrade(Registration.ENERGY.get())) && energyStorage.getEnergyStored() >= EnergyUse());
    }

    public int getCookTime() {

        if (this.getItem(FINPUT()).getItem() == Items.AIR) {
            return totalCookTime;
        }
        int speed = getSpeed();
        if (speed == -1) {
            return -1;
        }


        return Math.max(1, speed);


    }

    protected int getSpeed() {
        ItemStack stack = getItem(FINPUT());
        int i = getCookTimeConfig().get();
        int j = getFromCache(getCache(), stack.getItem());
        if (j == 0) {
            Optional<AbstractCookingRecipe> recipe = grabRecipe(stack);
            j = !recipe.isPresent() ? -1 : recipe.orElse(null).getCookingTime();
            getCache().put(this.getItem(FINPUT()).getItem(), recipe);

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

    public boolean hasUpgrade(UpgradeItem upg) {
        for (int slot : UPGRADES())
            if (upg.equals(getItem(slot).getItem()) && upg.isEnabled()) return true;
        return false;
    }

    public boolean hasUpgradeType(UpgradeItem upg) {
        return getUpgradeTypeSlot(upg) >= 0;
    }

    public ItemStack getUpgradeTypeSlotItem(UpgradeItem upg) {
        int i = getUpgradeTypeSlot(upg);
        return i < 0 ? ItemStack.EMPTY : getItem(i);
    }
    public int getUpgradeTypeSlot(UpgradeItem upg) {
        for (int slot : UPGRADES()) {
            if (getItem(slot).getItem() instanceof UpgradeItem && ((UpgradeItem) getItem(slot).getItem()).isEnabled() && upg.upgradeType == ((UpgradeItem) getItem(slot).getItem()).upgradeType) return slot;
        }return -1;
    }

    public ItemStack getUpgradeSlotItem(Item upg) {
        for (int slot : UPGRADES())
            if (upg == getItem(slot).getItem()) return getItem(slot);
        return ItemStack.EMPTY;
    }

    public void forceUpdateAllStates() {
        BlockState state = level.getBlockState(worldPosition);
        if (state.getValue(BlockStateProperties.LIT) != this.isBurning()) {
            level.setBlock(worldPosition, state.setValue(BlockStateProperties.LIT, this.isBurning()), 3);
        }
    }

    public int correspondentOutputSlot(int input){return FOUTPUT() - FINPUT() + input;}
    public void trySmelt(){
        for (int i : INPUTS()) {
            if(!this.canSmelt(irecipeSlot(i).orElse(null), i, correspondentOutputSlot(i))) continue;
            this.smeltItem(irecipeSlot(i).orElse(null), i, correspondentOutputSlot(i));
        }
    }

    public Optional<AbstractCookingRecipe> irecipeSlot(int input){
        if (!ArrayUtils.contains(INPUTS(), input)) return Optional.empty();
        if (!getItem(input).isEmpty())
            return grabRecipe(getItem(input));
        else
            return Optional.empty();
    }
    public boolean hasArraySlotSpace(int[] slots){
        for (int i : slots) {
            boolean noFull = this.getItem(i).getCount() < getItem(i).getMaxStackSize() && !getItem(i).isEmpty();
            if(noFull) continue;
            return true;
        }
        return false;
    }
    public boolean arraySlotFilled(int[] slots, boolean isFilled){
        for (int i : slots) {
            boolean filled = this.getItem(i).isEmpty();
            if (!isFilled) filled = !filled;
            if(filled) continue;
            return true;
        }
        return false;
    }
    public boolean smeltValid(){
        for (int i : INPUTS()) {
            if(!this.canSmelt(irecipeSlot(i).orElse(null), i, correspondentOutputSlot(i))) continue;
            return true;
        }
        return false;
    }
    @Override
    public void tick() {

        boolean wasBurning = this.isBurning();
        boolean flag1 = false;

        if (this.isBurning()) {
            --this.furnaceBurnTime;
        }
        if ((hasUpgrade(Registration.COLOR.get()))) {
            if (!(level.getBlockState(getBlockPos()).getValue(IronFurnaceBlock.COLORED)))
                level.setBlock(getBlockPos(), level.getBlockState(getBlockPos()).setValue(IronFurnaceBlock.COLORED, true), 3);
        } else
            level.setBlock(getBlockPos(), level.getBlockState(getBlockPos()).setValue(IronFurnaceBlock.COLORED, false), 3);

        if (hasUpgrade(Registration.BLAST.get())) {
            if (recipeType != IRecipeType.BLASTING) {
                recipeType = IRecipeType.BLASTING;
                if (!isForge())
                    level.setBlock(getBlockPos(), level.getBlockState(getBlockPos()).setValue(AbstractFurnaceBlock.TYPE, 1), 3);
            }
        } else if (hasUpgrade(Registration.SMOKE.get())) {
            if (recipeType != IRecipeType.SMOKING) {
                recipeType = IRecipeType.SMOKING;
                if (!isForge())
                    level.setBlock(getBlockPos(), level.getBlockState(getBlockPos()).setValue(AbstractFurnaceBlock.TYPE, 2), 3);
            }
        } else {
            if (recipeType != IRecipeType.SMELTING) {
                recipeType = IRecipeType.SMELTING;
                if (!isForge())
                    level.setBlock(getBlockPos(), level.getBlockState(getBlockPos()).setValue(AbstractFurnaceBlock.TYPE, 0), 3);
            }
        }

        if (!this.level.isClientSide) {

            int get_cook_time = getCookTime();
            timer++;

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

            if (hasXPTank()) grantStoredRecipeExperience(level, null);
            if (!hasUpgradeType(Registration.FACTORY.get()) && isForge() && (level.getBlockState(getBlockPos()).getValue(AbstractForgeBlock.SHOW_ORIENTATION)))
                level.setBlock(getBlockPos(), level.getBlockState(getBlockPos()).setValue(AbstractForgeBlock.SHOW_ORIENTATION, false), 3);
            getItem(FUEL()).getCapability(CapabilityEnergy.ENERGY).ifPresent(E -> {
                if (energyStorage.getEnergyStored() < energyStorage.getMaxEnergyStored()) {
                    E.extractEnergy(energyStorage.receiveEnergy(E.getEnergyStored(), false), false);
                }
            });

            ItemStack fuel = this.inventory.get(FUEL());
            if (isLiquid() && fuel.hasContainerItem()) {
                FluidActionResult res = FluidUtil.tryEmptyContainer(fuel, fluidTank, 1000, null, true);
                if (res.isSuccess()) {
                    level.playSound(null, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), SoundEvents.BUCKET_FILL_LAVA, SoundCategory.PLAYERS, 0.6F, 0.8F);
                    inventory.set(FUEL(), res.result);
                }
            }
            if ((isBurning() || !fuel.isEmpty() || isLiquid() || isEnergy()) && arraySlotFilled(INPUTS(), true)) {
                boolean valid = smeltValid();
                if (!this.isBurning() && valid) {
                    if (isLiquid() && (fluidTank.getFluidAmount() >= 10)) {
                        int f = getBurnTime(new ItemStack(fluidTank.getFluidInTank(1).getFluid().getBucket()));
                        this.furnaceBurnTime = f * get_cook_time / 20000;
                        if (hasUpgradeType(Registration.FUEL.get()))
                            this.furnaceBurnTime = 2 * f * get_cook_time / 20000;
                        this.recipesUsed = this.furnaceBurnTime;
                        fluidTank.drain(10, IFluidHandler.FluidAction.EXECUTE);
                    } else if (isEnergy() && isForge()) {
                        furnaceBurnTime = 200 * get_cook_time / 200;
                        if (hasUpgradeType(Registration.FUEL.get()))
                            furnaceBurnTime = 2 * 200 * get_cook_time / 200;
                        recipesUsed = furnaceBurnTime;
                        for (int i : INPUTS())
                            energyStorage.extractEnergy(EnergyUse() * OreProcessingMultiplier(getItem(i)), false);
                    } else {
                        if (hasUpgradeType(Registration.FUEL.get())) {
                            this.furnaceBurnTime = 2 * (getBurnTime(fuel)) * get_cook_time / 200;
                        } else {
                            this.furnaceBurnTime = getBurnTime(fuel) * get_cook_time / 200;
                        }
                        this.recipesUsed = this.furnaceBurnTime;
                    }
                    if (this.isBurning()) {
                        flag1 = true;
                        if ((!isLiquid() || fluidTank.getFluidAmount() < 10) && !isEnergy())
                            if ((!isLiquid() || fluidTank.getFluidAmount() < 10) && !isEnergy()) {
                                FluidUtil.getFluidHandler(fuel).ifPresent(
                                        (f) -> {
                                            f.drain(1000, IFluidHandler.FluidAction.EXECUTE);
                                            setItem(FUEL(), f.getContainer());
                                        });

                                if (!fuel.isEmpty() && isItemFuel(fuel)) {
                                    fuel.shrink(1);
                                    if (hasUpgrade(Registration.FUEL.get())) {
                                        breakDurabilityItem(getUpgradeSlotItem(Registration.FUEL.get()));
                                    }
                                }
                            }
                    }
                }
                if (this.isBurning() && valid) {
                    ++this.cookTime;
                    if (this.cookTime >= this.totalCookTime) {
                        this.cookTime = 0;
                        this.totalCookTime = this.getCookTime();
                        trySmelt();
                        if (hasUpgradeType(Registration.FACTORY.get()))
                            this.autoIO();
                        flag1 = true;
                    }
                } else {
                    if (cookTime > 0)
                        --this.cookTime;
                }
            } else if (!this.isBurning() && this.cookTime > 0) {
                this.cookTime = MathHelper.clamp(this.cookTime - 2, 0, this.totalCookTime);
            }
            if (wasBurning != this.isBurning()) {
                flag1 = true;
                this.level.setBlock(this.worldPosition, this.level.getBlockState(this.worldPosition).setValue(BlockStateProperties.LIT, this.isBurning()), 3);
            }
            if ((timer % 24 == 0) && (hasUpgradeType(Registration.FACTORY.get()))){
                if (cookTime <= 0) {
                    if (arraySlotFilled(INPUTS(), false)) {
                        autoIO();
                        flag1 = true;
                    } else if (hasArraySlotSpace(INPUTS())) {
                        autoIO();
                        flag1 = true;
                    }
                    if (arraySlotFilled(OUTPUTS(), true)) {
                        autoIO();
                        flag1 = true;
                    }
                    if (getItem(FUEL()).isEmpty() && !isLiquid() && !isEnergy()) {
                        autoIO();
                        flag1 = true;
                    } else if (getItem(FUEL()).getCount() < getItem(FUEL()).getMaxStackSize() || FluidUtil.getFluidHandler(fuel).isPresent() && FluidUtil.getFluidContained(fuel).isPresent() && (FluidUtil.getFluidContained(fuel).get().getAmount() < fluidTank.getSpace()) ){
                        autoIO();
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
        CompoundNBT nbt = getUpgradeSlotItem(Registration.COLOR.get()).getTag();

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
                                    for (int input : INPUTS()) {
                                        if (this.getItem(input).getCount() >= this.getItem(input).getMaxStackSize()) {
                                            continue;
                                        }
                                        for (int i = 0; i < other.getSlots(); i++) {
                                            if (other.getStackInSlot(i).isEmpty()) {
                                                continue;
                                            }
                                            ItemStack stack = other.extractItem(i, other.getStackInSlot(i).getMaxStackSize(), true);
                                            if (hasRecipe(stack) && getItem(input).isEmpty() || ItemHandlerHelper.canItemStacksStack(getItem(input), stack)) {
                                                insertItemInternal(input, other.extractItem(i, other.getStackInSlot(i).getMaxStackSize() - this.getItem(input).getCount(), false), false);
                                            }
                                        }
                                    }
                                }
                                if (this.furnaceSettings.get(dir.ordinal()) == 4) {
                                    if (this.getItem(FUEL()).getCount() >= this.getItem(FUEL()).getMaxStackSize()) {
                                        continue;
                                    }
                                    for (int i = 0; i < other.getSlots(); i++) {
                                        if (!isItemFuel(other.getStackInSlot(i))) {
                                            continue;
                                        }
                                        if (other.getStackInSlot(i).isEmpty()) {
                                            continue;
                                        }
                                        ItemStack stack = other.extractItem(i, other.getStackInSlot(i).getMaxStackSize(), true);
                                        if (isItemFuel(stack) && getItem(FUEL()).isEmpty() || ItemHandlerHelper.canItemStacksStack(getItem(FUEL()), stack)) {
                                            insertItemInternal(FUEL(), other.extractItem(i, other.getStackInSlot(i).getMaxStackSize() - this.getItem(FUEL()).getCount(), false), false);
                                        }
                                    }
                                }
                            }
                            if (this.getAutoOutput() == 1) {

                                if (this.furnaceSettings.get(dir.ordinal()) == 4) {
                                    if (this.getItem(FUEL()).isEmpty()) {
                                        continue;
                                    }
                                    ItemStack fuel = getItem(FUEL());
                                    if (isItemFuel(fuel)) {
                                        continue;
                                    }
                                    for (int i = 0; i < other.getSlots(); i++) {
                                        ItemStack stack = extractItemInternal(FUEL(), this.getItem(FUEL()).getMaxStackSize() - other.getStackInSlot(i).getCount(), true);
                                        if (other.isItemValid(i, stack) && (other.getStackInSlot(i).isEmpty() || other.isItemValid(i, stack) && (ItemHandlerHelper.canItemStacksStack(other.getStackInSlot(i), stack) && other.getStackInSlot(i).getCount() + stack.getCount() <= other.getSlotLimit(i)))) {
                                            other.insertItem(i, extractItemInternal(FUEL(), stack.getCount(), false), false);
                                        }
                                    }
                                }
                                for (int output : OUTPUTS()) {
                                    if (this.furnaceSettings.get(dir.ordinal()) == 2 || this.furnaceSettings.get(dir.ordinal()) == 3) {
                                        if (this.getItem(output).isEmpty()) {
                                            continue;
                                        }
                                        if (tile.getBlockState().getBlock().getRegistryName().toString().contains("storagedrawers:")) {
                                            continue;
                                        }
                                        for (int i = 0; i < other.getSlots(); i++) {
                                            ItemStack stack = extractItemInternal(output, this.getItem(output).getMaxStackSize() - other.getStackInSlot(i).getCount(), true);
                                            if (other.isItemValid(i, stack) && (other.getStackInSlot(i).isEmpty() || other.isItemValid(i, stack) && (ItemHandlerHelper.canItemStacksStack(other.getStackInSlot(i), stack) && other.getStackInSlot(i).getCount() + stack.getCount() <= other.getSlotLimit(i)))) {
                                                other.insertItem(i, extractItemInternal(output, stack.getCount(), false), false);
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
        return this.furnaceSettings.get(getIndexBottom());
    }

    public int getSettingTop() {
        return this.furnaceSettings.get(getIndexTop());
    }

    public int getSettingFront() {
        return this.furnaceSettings.get(getIndexFront());
    }

    public int getSettingBack() {
        return this.furnaceSettings.get(getIndexBack());
    }

    public int getSettingLeft() {
        return this.furnaceSettings.get(getIndexLeft());
    }

    public int getSettingRight() {
        return this.furnaceSettings.get(getIndexRight());
    }

    public int getIndexFront() {
        int i = facing().ordinal();
        return i;
    }

    public int getIndexBack() {
        int i = facing().getOpposite().ordinal();
        return i;
    }

    public int getIndexLeft() {
        if (facing() == Direction.NORTH) {
            return Direction.EAST.ordinal();
        } else if (facing() == Direction.WEST) {
            return Direction.NORTH.ordinal();
        } else if (facing() == Direction.SOUTH) {
            return Direction.WEST.ordinal();
        } else {
            return Direction.SOUTH.ordinal();
        }
    }

    public int getIndexRight() {
        if (facing() == Direction.NORTH) {
            return Direction.WEST.ordinal();
        } else if (facing() == Direction.WEST) {
            return Direction.SOUTH.ordinal();
        } else if (facing() == Direction.SOUTH) {
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

    protected boolean isOre(ItemStack input) {
        return (input.getItem().is(ore) || input.getItem().getRegistryName().toString().contains("ore"));
    }

    protected int OreProcessingMultiplier(ItemStack input) {
        if (hasUpgradeType(Registration.ORE_PROCESSING.get())) {
            OreProcessingUpgradeItem oreup = (OreProcessingUpgradeItem) getUpgradeTypeSlotItem(Registration.ORE_PROCESSING.get()).getItem();
            if (isOre(input)) return oreup.getMultiplier;

        } else if (input == ItemStack.EMPTY) return 0;
        return 1;
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
                    return output.getCount() + recipeOutput.getCount() * OreProcessingMultiplier(input) <= output.getMaxStackSize();
                }
            }
        }
        return false;
    }

    private ItemStack getResult(@Nullable IRecipe<?> recipe, ItemStack input) {
        ItemStack out = recipe.getResultItem().copy();
        out.setCount(out.getCount() * OreProcessingMultiplier(input));
        return out;
    }

    protected void smeltItem(@Nullable IRecipe<?> recipe, int INPUT, int OUTPUT) {
        timer = 0;
        if (recipe != null && this.canSmelt(recipe, INPUT, OUTPUT)) {
            ItemStack itemstack = this.inventory.get(INPUT);
            ItemStack itemstack2 = this.inventory.get(OUTPUT);
            if (itemstack2.isEmpty()) {
                this.inventory.set(OUTPUT, getResult(recipe, itemstack));
                if (hasUpgrade(Registration.ORE_PROCESSING.get()) && ((isOre(itemstack)))) {
                    breakDurabilityItem(getUpgradeSlotItem(Registration.ORE_PROCESSING.get()));
                }
            } else if (itemstack2.getItem() == getResult(recipe, itemstack).getItem()) {
                itemstack2.grow(getResult(recipe, itemstack).getCount());
                if (hasUpgrade(Registration.ORE_PROCESSING.get()) && (isOre(itemstack))) {
                    breakDurabilityItem(getUpgradeSlotItem(Registration.ORE_PROCESSING.get()));
                }
            }
            this.checkXP(recipe);
            if (!this.level.isClientSide) {
                this.setRecipeUsed(recipe);
            }

            if (itemstack.getItem() == Blocks.WET_SPONGE.asItem() && !this.inventory.get(FUEL()).isEmpty()) {
                FluidUtil.getFluidHandler(this.getItem(FUEL())).ifPresent(e -> {
                    if (e.getFluidInTank(0).isEmpty()) {
                        e.fill(new FluidStack(Fluids.WATER, 1000), IFluidHandler.FluidAction.EXECUTE);
                        this.setItem(FUEL(), e.getContainer());
                    }
                });
            }
            if (ModList.get().isLoaded("pmmo")) {
                if (getRecipe(itemstack, IRecipeType.SMOKING).isPresent()) {
                    FurnaceHandler.handleSmelted(itemstack, itemstack2, level, worldPosition, 1);
                } else FurnaceHandler.handleSmelted(itemstack, itemstack2, level, worldPosition, 0);
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
        this.recipesUsed = getBurnTime(this.inventory.get(1));
        if (tag.get("fluidTank") != null)
            CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.readNBT(fluidTank, null, tag.get("fluidTank"));
        if (tag.get("xpTank") != null)
            CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.readNBT(xpTank, null, tag.get("xpTank"));
        CompoundNBT compoundnbt = tag.getCompound("RecipesUsed");
        CapabilityEnergy.ENERGY.readNBT(energyStorage, null, tag.get("energyStorage"));
        for (String s : compoundnbt.getAllKeys()) {
            this.recipes.put(new ResourceLocation(s), compoundnbt.getInt(s));
        }
        this.show_inventory_settings = tag.getInt("ShowInvSettings");

        super.load(state, tag);
    }

    @Override
    public CompoundNBT save(CompoundNBT tag) {
        super.save(tag);
        ItemStackHelper.saveAllItems(tag, this.inventory);
        tag.putInt("BurnTime", this.furnaceBurnTime);
        tag.putInt("CookTime", this.cookTime);
        tag.putInt("CookTimeTotal", this.totalCookTime);
        tag.put("fluidTank", CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.writeNBT(fluidTank, null));
        tag.put("xpTank", CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.writeNBT(xpTank, null));
        tag.put("energyStorage", CapabilityEnergy.ENERGY.writeNBT(energyStorage, null));
        tag.putInt("ShowInvSettings", this.show_inventory_settings);
        CompoundNBT compoundnbt = new CompoundNBT();
        this.recipes.forEach((recipeId, craftedAmount) -> {
            compoundnbt.putInt(recipeId.toString(), craftedAmount);
        });
        tag.put("RecipesUsed", compoundnbt);

        return tag;
    }

    @Nonnull
    @Override
    public <
            T> LazyOptional<T> getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @Nullable Direction facing) {
        if (!this.isRemoved()) {
            if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
                if (facing != null) {
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
            }
            if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
                if ((facing == null || facing.ordinal() == getIndexTop() || facing.ordinal() == getIndexBottom())) {
                    if (isLiquid())
                        return (LazyOptional.of(() -> fluidTank).cast());
                } else {
                    if (hasXPTank())
                        return (LazyOptional.of(() -> xpTank).cast());
                }
            }
            if ((hasUpgrade(Registration.ENERGY.get())) && capability == CapabilityEnergy.ENERGY)
                return (LazyOptional.of(() -> energyStorage).cast());
        }
        return super.getCapability(capability, facing);
    }

    public int getIndexBottom() {
        return 0;
    }

    public int getIndexTop() {
        return 1;
    }

    @Override
    public int[] IgetSlotsForFace(Direction side) {
        if (hasUpgradeType(Registration.FACTORY.get())) {
            if (this.furnaceSettings.get(DirectionUtil.getId(side)) == 0) {
                return new int[]{};
            } else if (this.furnaceSettings.get(DirectionUtil.getId(side)) == 1) {
                return ISLOTS();
            } else if (this.furnaceSettings.get(DirectionUtil.getId(side)) == 2) {
                return OUTPUTS();
            } else if (this.furnaceSettings.get(DirectionUtil.getId(side)) == 3) {
                return FSLOTS();
            } else if (this.furnaceSettings.get(DirectionUtil.getId(side)) == 4) {
                return new int[]{FUEL()};
            }
        } else {
            if (side == Direction.UP) return INPUTS();
            else if (side == Direction.DOWN) return OUTPUTS();
            else return new int[]{FUEL()};
        }

        return new int[]{};
    }

    @Override
    public boolean IcanExtractItem(int index, ItemStack stack, Direction direction) {
        if (hasUpgradeType(Registration.FACTORY.get())) {
            if (this.furnaceSettings.get(DirectionUtil.getId(direction)) == 0) {
                return false;
            } else if (this.furnaceSettings.get(DirectionUtil.getId(direction)) == 1) {
                return false;
            } else if (this.furnaceSettings.get(DirectionUtil.getId(direction)) == 2) {
                return (index >= FOUTPUT() && index <= LOUTPUT());
            } else if (this.furnaceSettings.get(DirectionUtil.getId(direction)) == 3) {
                return (index >= FOUTPUT() && index <= LOUTPUT());
            } else if (this.furnaceSettings.get(DirectionUtil.getId(direction)) == 4 && !isItemFuel(stack) && !stack.getCapability(CapabilityEnergy.ENERGY).isPresent()) {
                return index == FUEL();
            }
        } else {
            return direction == Direction.DOWN && index >= FOUTPUT() && index <= LOUTPUT();
        }
        return false;
    }

    @Override
    public boolean IisItemValidForSlot(int index, ItemStack stack) {
        if (ArrayUtils.contains(OUTPUTS(), index))
            return false;

        if (ArrayUtils.contains(INPUTS(), index)) {
            if (stack.isEmpty()) {
                return false;
            }

            return hasRecipe(stack);
        }

        if (index == FUEL()) {
            return isItemFuel(stack) || stack.hasContainerItem() || stack.getCapability(CapabilityEnergy.ENERGY).isPresent();
        }
        if (ArrayUtils.contains(UPGRADES(), index)) {
            if (stack.getItem() instanceof UpgradeItem && !hasUpgrade((UpgradeItem) stack.getItem()) && !hasUpgradeType((UpgradeItem) stack.getItem()))
                if (((UpgradeItem) stack.getItem()).upgradeType == 1) return (index == HEATER() || !isForge());
                else return true;
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

    @Nullable
    @Override
    public IRecipe<?> getRecipeUsed() {
        return null;
    }

    @Override
    public void setRecipeUsed(@Nullable IRecipe<?> recipe) {

        if (recipe != null) {
            ResourceLocation resourcelocation = recipe.getId();
            this.recipes.addTo(resourcelocation, 1);
        }

    }

    public void unlockRecipes(PlayerEntity player) {
        List<IRecipe<?>> list = this.grantStoredRecipeExperience(player.level, player.position());
        player.awardRecipes(list);
        this.recipes.clear();
    }

    public List<IRecipe<?>> grantStoredRecipeExperience(World level, Vector3d worldPosition) {
        List<IRecipe<?>> list = Lists.newArrayList();
        if (this.recipes.object2IntEntrySet() != null)
            recipes.object2IntEntrySet().fastForEach(entry-> level.getRecipeManager().byKey(entry.getKey()).ifPresent((h) -> {
                list.add(h);
                int amountLiquidXp = MathHelper.floor((float) entry.getIntValue() * ((AbstractCookingRecipe) h).getExperience()) * 5;
                if (hasXPTank()) {
                    if (amountLiquidXp >= 1) {
                        xpTank.fill(new FluidStack(Config.getLiquidXP(), amountLiquidXp), IFluidHandler.FluidAction.EXECUTE);
                        recipes.clear();
                    }
                } else {
                    if (worldPosition != null)
                        splitAndSpawnExperience(level, worldPosition, entry.getIntValue(), ((AbstractCookingRecipe) h).getExperience());
                }
            }));

        return list;
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






}