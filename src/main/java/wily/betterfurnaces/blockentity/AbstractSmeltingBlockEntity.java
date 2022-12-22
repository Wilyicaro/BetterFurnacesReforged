package wily.betterfurnaces.blockentity;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeHolder;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.AirItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
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
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.ArrayUtils;
import wily.betterfurnaces.Config;
import wily.betterfurnaces.blocks.AbstractForgeBlock;
import wily.betterfurnaces.blocks.AbstractFurnaceBlock;
import wily.betterfurnaces.compat.ProjectMMO;
import wily.betterfurnaces.init.Registration;
import wily.betterfurnaces.items.FuelEfficiencyUpgradeItem;
import wily.betterfurnaces.items.OreProcessingUpgradeItem;
import wily.betterfurnaces.items.UpgradeItem;
import wily.betterfurnaces.items.XpTankUpgradeItem;
import wily.betterfurnaces.util.DirectionUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public abstract class AbstractSmeltingBlockEntity extends InventoryBlockEntity implements RecipeHolder, StackedContentsCompatible {
    public final int[] provides = new int[Direction.values().length];
    private final int[] lastProvides = new int[this.provides.length];


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

    private Random rand = new Random();

    public int show_inventory_settings;
    protected int timer;

    public int EnergyUse() {return 600;}
    public int LiquidCapacity() {return 4000;}
    public int EnergyCapacity() {return 16000;}
    private int furnaceBurnTime;
    public int cookTime;
    public int totalCookTime = this.getCookTime();
    private int recipesUsed;
    private final Object2IntOpenHashMap<ResourceLocation> recipes = new Object2IntOpenHashMap<>();
    public boolean isForge(){ return false;}
    public RecipeType<? extends AbstractCookingRecipe> recipeType;

    public FactoryUpgradeSettings furnaceSettings;

    private LRUCache<Item, Optional<AbstractCookingRecipe>> cache = LRUCache.newInstance(Config.cache_capacity.get());
    protected LRUCache<Item, Optional<AbstractCookingRecipe>> blasting_cache = LRUCache.newInstance(Config.cache_capacity.get());
    protected LRUCache<Item, Optional<AbstractCookingRecipe>> smoking_cache = LRUCache.newInstance(Config.cache_capacity.get());

    public Direction facing(){
        return this.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
    }


    public AbstractSmeltingBlockEntity(BlockEntityType<?> tileentitytypeIn, BlockPos pos, BlockState state, int invsize) {
        super(tileentitytypeIn, pos, state, invsize);
        this.recipeType = RecipeType.SMELTING;
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

    protected LRUCache<Item, Optional<AbstractCookingRecipe>> getCache() {

        if (hasUpgrade(Registration.BLAST.get())) {
            if (this.recipeType != RecipeType.BLASTING) {
                this.recipeType = RecipeType.BLASTING;
            }
        }
        if (hasUpgrade(Registration.SMOKE.get())){
            if (this.recipeType != RecipeType.SMOKING) {
                this.recipeType = RecipeType.SMOKING;
            }
        }
        if (!((hasUpgrade(Registration.SMOKE.get()))) && !((hasUpgrade(Registration.BLAST.get()))))
        {
            if (this.recipeType != RecipeType.SMELTING) {
                this.recipeType = RecipeType.SMELTING;
            }
        }
        if (this.recipeType == RecipeType.BLASTING) {
            return blasting_cache;
        }
        if (this.recipeType == RecipeType.SMOKING) {
            return smoking_cache;
        }
        return cache;
    }


    private Optional<AbstractCookingRecipe> getRecipe(ItemStack stack, RecipeType recipeType) {
        return this.level.getRecipeManager().getRecipeFor((RecipeType<AbstractCookingRecipe>) recipeType, new SimpleContainer(stack), this.level);
    }

    private Optional<AbstractCookingRecipe> grabRecipe(ItemStack stack) {
        Item item = stack.getItem();
        if (item instanceof AirItem)
        {
            return Optional.empty();
        }
        Optional<AbstractCookingRecipe> recipe = getCache().get(item);
        if (recipe == null) {
            recipe = getRecipe(stack, recipeType);
            getCache().put(item, recipe);
        }
        return recipe;
    }
    public boolean hasXPTank() {
        return hasUpgrade(Registration.XP.get());
    }
    public boolean hasEnder() {
        return (hasUpgradeType(Registration.FUEL.get()));
    }
    public int getEnderMultiplier(){ if (hasEnder()) return ((FuelEfficiencyUpgradeItem)getUpgradeTypeSlotItem(Registration.FUEL.get()).getItem()).getMultiplier; else return 1;}
    public boolean isLiquid() {
        return hasUpgrade(Registration.LIQUID.get());
    }
    private boolean isEnergy() {
        return (hasUpgrade(Registration.ENERGY.get()) && energyStorage.getEnergyStored() > EnergyUse());
    }
    public int getCookTime() {

        if (this.getItem(FINPUT()).getItem() == Items.AIR ) {
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

    public final ContainerData fields = new ContainerData() {
        public int get(int index) {
            switch (index) {
                case 0:
                    return AbstractSmeltingBlockEntity.this.furnaceBurnTime;
                case 1:
                    return AbstractSmeltingBlockEntity.this.recipesUsed;
                case 2:
                    return AbstractSmeltingBlockEntity.this.cookTime;
                case 3:
                    return AbstractSmeltingBlockEntity.this.totalCookTime;
                case 4:
                    return AbstractSmeltingBlockEntity.this.show_inventory_settings;
                default:
                    return 0;
            }
        }

        public void set(int index, int value) {
            switch (index) {
                case 0:
                    AbstractSmeltingBlockEntity.this.furnaceBurnTime = value;
                    break;
                case 1:
                    AbstractSmeltingBlockEntity.this.recipesUsed = value;
                    break;
                case 2:
                    AbstractSmeltingBlockEntity.this.cookTime = value;
                    break;
                case 3:
                    AbstractSmeltingBlockEntity.this.totalCookTime = value;
                    break;
                case 4:
                    AbstractSmeltingBlockEntity.this.show_inventory_settings = value;
                    break;
            }

        }

        @Override
        public int getCount() {
            return 5;
        }
    };

    public boolean hasUpgrade(UpgradeItem upg) {
        for (int slot : UPGRADES())
            if (upg.equals(getItem(slot).getItem()) && upg.isEnabled()) return true;
        return false;
    }

    public boolean hasUpgradeType(UpgradeItem upg) {
        for (int slot : UPGRADES()) {
            if (getItem(slot).getItem() instanceof UpgradeItem upgradeItem &&  upgradeItem.isEnabled() && upg.upgradeType == upgradeItem.upgradeType) return true;
        }
        return hasUpgrade(upg);
    }

    public ItemStack getUpgradeTypeSlotItem(UpgradeItem upg) {
        int i = getUpgradeTypeSlot(upg);
        return i < 0 ? ItemStack.EMPTY : getItem(i);
    }
    public int getUpgradeTypeSlot(UpgradeItem upg) {
        for (int slot : UPGRADES())
            if (getItem(slot).getItem() instanceof UpgradeItem upgradeItem &&  upgradeItem.isEnabled() && upg.upgradeType == upgradeItem.upgradeType) return slot;
        return -1;
    }

    public ItemStack getUpgradeSlotItem(Item upg) {
        for (int slot : UPGRADES())
            if (upg == getItem(slot).getItem()) return getItem(slot);
        return ItemStack.EMPTY;
    }

    protected final FluidTank fluidTank = new FluidTank(LiquidCapacity(), fs -> (getBurnTime(new ItemStack(fs.getFluid().getBucket())) > 0)){
        @Override
        protected void onContentsChanged() {
            super.onContentsChanged();
            setChanged();
            updateBlockState();
        }
    };
    protected final FluidTank xpTank = new FluidTank(2000, xp -> (ModList.get().isLoaded(Config.getLiquidXPMod()) && xp.getFluid().equals(Config.getLiquidXP()))){
        @Override
        protected void onContentsChanged() {
            super.onContentsChanged();
            setChanged();
            updateBlockState();
        }
    };
    private final ForgeEnergyStorage energyStorage = new ForgeEnergyStorage(this,0,EnergyCapacity()) {
        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            int retval = super.receiveEnergy(maxReceive, simulate);
            if (!simulate) {
                setChanged();
                updateBlockState();
            }
            return retval;
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            int retval = super.extractEnergy(maxExtract, simulate);
            if (!simulate) {
                setChanged();
                updateBlockState();
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

    public static void tick(Level level, BlockPos worldPosition, BlockState blockState, AbstractSmeltingBlockEntity e) {

        boolean wasBurning = e.isBurning();
        boolean flag1 = false;
        boolean flag2 = false;

        if (e.isBurning()) {
            --e.furnaceBurnTime;
        }

        if (e.hasXPTank()) e.grantStoredRecipeExperience(level, null);

        if (!e.hasUpgradeType(Registration.FACTORY.get()) && e instanceof AbstractForgeBlockEntity && (level.getBlockState(e.getBlockPos()).getValue(AbstractForgeBlock.SHOW_ORIENTATION))) level.setBlock(e.getBlockPos(), level.getBlockState(e.getBlockPos()).setValue(AbstractForgeBlock.SHOW_ORIENTATION, false), 3);
        e.getItem(e.FUEL()).getCapability(CapabilityEnergy.ENERGY).ifPresent(E -> {
            if (e.energyStorage.getEnergyStored() < e.energyStorage.getMaxEnergyStored()) {
                E.extractEnergy(e.energyStorage.receiveEnergy(E.getEnergyStored(), false), false);
            }
        });
        if ((e.hasUpgrade(Registration.COLOR.get()))) {
            if (!(level.getBlockState(e.getBlockPos()).getValue(AbstractFurnaceBlock.COLORED)))
                level.setBlock(e.getBlockPos(), level.getBlockState(e.getBlockPos()).setValue(AbstractFurnaceBlock.COLORED, true), 3);
        } else if ((level.getBlockState(e.getBlockPos()).getValue(AbstractFurnaceBlock.COLORED))) level.setBlock(e.getBlockPos(), level.getBlockState(e.getBlockPos()).setValue(AbstractFurnaceBlock.COLORED, false), 3);

        if (e.hasUpgrade(Registration.BLAST.get())) {
            if (e.recipeType != RecipeType.BLASTING) {
                e.recipeType = RecipeType.BLASTING;
                if (!e.isForge())
                    level.setBlock(e.getBlockPos(), level.getBlockState(e.getBlockPos()).setValue(AbstractFurnaceBlock.TYPE, 1), 3);
            }
        } else if (e.hasUpgrade(Registration.SMOKE.get())) {
            if (e.recipeType != RecipeType.SMOKING) {
                e.recipeType = RecipeType.SMOKING;
                if (!e.isForge())
                    level.setBlock(e.getBlockPos(), level.getBlockState(e.getBlockPos()).setValue(AbstractFurnaceBlock.TYPE, 2), 3);
            }
        } else {
            if (e.recipeType != RecipeType.SMELTING) {
                e.recipeType = RecipeType.SMELTING;
                if (!e.isForge())
                    level.setBlock(e.getBlockPos(), level.getBlockState(e.getBlockPos()).setValue(AbstractFurnaceBlock.TYPE, 0), 3);
            }
        }

        if (!e.level.isClientSide) {
            int get_cook_time = e.getCookTime();
            e.timer++;


            if (e.totalCookTime != get_cook_time) {
                e.totalCookTime = get_cook_time;
            }
            int mode = e.getRedstoneSetting();
            if (mode != 0) {
                if (mode == 2) {
                    int i = 0;
                    for (Direction side : Direction.values()) {
                        if (level.getSignal(worldPosition.offset(side.getNormal()), side) > 0) {
                            i++;
                        }
                    }
                    if (i != 0) {
                        e.cookTime = 0;
                        e.furnaceBurnTime = 0;
                        e.forceUpdateAllStates();
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
                        e.cookTime = 0;
                        e.furnaceBurnTime = 0;
                        e.forceUpdateAllStates();
                        return;
                    }
                }
                for (int i = 0; i < Direction.values().length; i++)
                    e.provides[i] = e.getBlockState().getDirectSignal(e.level, worldPosition, DirectionUtil.fromId(i));

            } else {
                for (int i = 0; i < Direction.values().length; i++)
                    e.provides[i] = 0;
            }
            if (e.doesNeedUpdateSend()) {
                e.onUpdateSent();
            }
            ItemStack fuel = e.getItem(e.FUEL());
            if (e.isLiquid() && fuel.hasContainerItem()) {
                FluidActionResult res = FluidUtil.tryEmptyContainer(fuel, e.fluidTank, 1000, null, true);
                if ( res.isSuccess()) {
                    e.level.playSound(null, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), SoundEvents.BUCKET_FILL_LAVA, SoundSource.PLAYERS, 0.6F, 0.8F);
                    e.setItem(e.FUEL(), res.result);
                }
            }
            if ((e.isBurning() || !fuel.isEmpty() || e.isLiquid() || e.isEnergy()) &&  e.arraySlotFilled(e.INPUTS(), true)) {
                boolean valid = e.smeltValid();
                if (!e.isBurning() && (valid)) {
                    if (e.isLiquid() && (e.fluidTank.getFluidAmount() >= 10) ){
                        int f = getBurnTime(new ItemStack(e.fluidTank.getFluidInTank(1).getFluid().getBucket()));
                        e.furnaceBurnTime = f * get_cook_time / 20000;
                        if (e.hasEnder()) {
                            e.furnaceBurnTime = e.getEnderMultiplier() * f * get_cook_time / 20000;
                            if (e.hasUpgrade(Registration.FUEL.get())) {
                                e.breakDurabilityItem(e.getUpgradeSlotItem(Registration.FUEL.get()));
                            }
                        }
                        e.recipesUsed = e.furnaceBurnTime;
                        e.fluidTank.drain(10, IFluidHandler.FluidAction.EXECUTE);
                    }else if (e.isEnergy()){
                        e.furnaceBurnTime = 200 * get_cook_time / 200;
                        if (e.hasEnder()) {
                            e.furnaceBurnTime = e.getEnderMultiplier() * 200 * get_cook_time / 200;
                            if (e.hasUpgrade(Registration.FUEL.get())) {
                                e.breakDurabilityItem(e.getUpgradeSlotItem(Registration.FUEL.get()));
                            }
                        }
                        e.recipesUsed = e.furnaceBurnTime;
                        for (int a : e.INPUTS())
                            e.energyStorage.consumeEnergy(e.EnergyUse() * e.OreProcessingMultiplier(e.getItem(a)), false);
                    }else{
                        if (e.hasEnder()){
                            e.furnaceBurnTime = e.getEnderMultiplier() * (getBurnTime(fuel)) * get_cook_time / 200;
                        }else{
                            e.furnaceBurnTime = getBurnTime(fuel) * get_cook_time / 200;
                        }
                        e.recipesUsed = e.furnaceBurnTime;
                    }
                    if (e.isBurning()) {
                        flag1 = true;
                        if ((!e.isLiquid() || e.fluidTank.getFluidAmount() < 10) && !e.isEnergy()) {
                            FluidUtil.getFluidHandler(fuel).ifPresent(
                                    (f) -> {
                                        f.drain(1000, IFluidHandler.FluidAction.EXECUTE);
                                        e.setItem(e.FUEL(), f.getContainer());
                                    });

                            if (!fuel.isEmpty() && isItemFuel(fuel)) {
                                fuel.shrink(1);
                                if (e.hasUpgrade(Registration.FUEL.get())) {
                                    e.breakDurabilityItem(e.getUpgradeSlotItem(Registration.FUEL.get()));
                                }
                            }
                        }
                    }
                }
                if (e.isBurning() && valid ) {
                    ++e.cookTime;
                    if (e.cookTime >= e.totalCookTime) {
                        e.cookTime = 0;
                        e.totalCookTime = e.getCookTime();
                        e.trySmelt();
                        if (e.hasUpgradeType(Registration.FACTORY.get()))
                            e.autoIO();
                        flag1 = true;
                    }
                } else {
                    e.cookTime = 0;
                }
            } else if (!e.isBurning() && e.cookTime > 0) {
                e.cookTime = Mth.clamp(e.cookTime - 2, 0, e.totalCookTime);
            }
            if (wasBurning != e.isBurning()) {
                flag1 = true;
                e.level.setBlock(e.worldPosition, e.level.getBlockState(e.worldPosition).setValue(BlockStateProperties.LIT, e.isBurning()), 3);
            }
            if ((e.timer % 24 == 0) && (e.hasUpgradeType(Registration.FACTORY.get()))){
                if (e.cookTime <= 0) {
                    if (e.arraySlotFilled(e.INPUTS(), false)) {
                        e.autoIO();
                        flag1 = true;
                    } else if (e.hasArraySlotSpace(e.INPUTS())) {
                        e.autoIO();
                        flag1 = true;
                    }
                    if (e.arraySlotFilled(e.OUTPUTS(), true)) {
                        e.autoIO();
                        flag1 = true;
                    }
                    if (e.getItem(e.FUEL()).isEmpty() && !e.isLiquid() && !e.isEnergy()) {
                        e.autoIO();
                        flag1 = true;
                    } else if (e.getItem(e.FUEL()).getCount() < e.getItem(e.FUEL()).getMaxStackSize() || FluidUtil.getFluidHandler(fuel).isPresent() && FluidUtil.getFluidContained(fuel).isPresent() && (FluidUtil.getFluidContained(fuel).get().getAmount() < e.fluidTank.getSpace()) ){
                        e.autoIO();
                        flag1 = true;
                    }
                }
            }
        }

        if (flag1) {
            e.setChanged();
        }

    }
    public int hex() {
        CompoundTag nbt = getUpgradeSlotItem(Registration.COLOR.get()).getTag();

        return ((nbt.getInt("red") & 0x0ff) << 16) | ((nbt.getInt("green") & 0x0ff) << 8) | (nbt.getInt("blue") & 0x0ff);
    }


    private void autoIO() {
        for (Direction dir : Direction.values()) {
            BlockEntity tile = level.getBlockEntity(worldPosition.offset(dir.getNormal()));
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
                                for (int INPUT : INPUTS())
                                    if (this.furnaceSettings.get(dir.ordinal()) == 1 || this.furnaceSettings.get(dir.ordinal()) == 3) {
                                        if (this.getItem(INPUT).getCount() >= this.getItem(INPUT).getMaxStackSize()) {
                                            continue;
                                        }
                                        for (int i = 0; i < other.getSlots(); i++) {
                                            if (other.getStackInSlot(i).isEmpty()) {
                                                continue;
                                            }
                                            ItemStack stack = other.extractItem(i, other.getStackInSlot(i).getMaxStackSize(), true);
                                            if (hasRecipe(stack) && getItem(INPUT).isEmpty() || ItemHandlerHelper.canItemStacksStack(getItem(INPUT), stack))
                                                inventory.insertItem(INPUT, other.extractItem(i, other.getStackInSlot(i).getMaxStackSize() - this.getItem(INPUT).getCount(), false), false);
                                        }
                                    }
                                if (this.furnaceSettings.get(dir.ordinal()) == 4) {
                                    if (this.getItem(FUEL()).getCount() >= this.getItem(FUEL()).getMaxStackSize()) {
                                        continue;
                                    }
                                    for (int i = 0; i < other.getSlots(); i++) {
                                        if (!isItemFuel(other.getStackInSlot(i))){
                                            continue;
                                        }
                                        if (other.getStackInSlot(i).isEmpty()) {
                                            continue;
                                        }
                                        ItemStack stack = other.extractItem(i, other.getStackInSlot(i).getMaxStackSize(), true);
                                        if (isItemFuel(stack) && getItem(FUEL()).isEmpty() || ItemHandlerHelper.canItemStacksStack(getItem(FUEL()), stack)) {
                                            inventory.insertItem(FUEL(), other.extractItem(i, other.getStackInSlot(i).getMaxStackSize() - this.getItem(FUEL()).getCount(), false), false);
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
                                        ItemStack stack = inventory.extractItem(FUEL(), this.getItem(FUEL()).getMaxStackSize() - other.getStackInSlot(i).getCount(), true);
                                        if (other.isItemValid(i, stack) && (other.getStackInSlot(i).isEmpty() || other.isItemValid(i, stack) && (ItemHandlerHelper.canItemStacksStack(other.getStackInSlot(i), stack) && other.getStackInSlot(i).getCount() + stack.getCount() <= other.getSlotLimit(i)))) {
                                            other.insertItem(i, inventory.extractItem(FUEL(), stack.getCount(), false), false);
                                        }
                                    }
                                }

                                for (int output : OUTPUTS()) {
                                    if (this.furnaceSettings.get(dir.ordinal()) == 2 || this.furnaceSettings.get(dir.ordinal()) == 3) {
                                        if (this.getItem(output).isEmpty()) {
                                            continue;
                                        }
                                        if (ForgeRegistries.BLOCKS.getKey(tile.getBlockState().getBlock()).toString().contains("storagedrawers:")) {
                                            continue;
                                        }
                                        for (int i = 0; i < other.getSlots(); i++) {
                                            ItemStack stack = inventory.extractItem(output, this.getItem(output).getMaxStackSize() - other.getStackInSlot(i).getCount(), true);
                                            if (other.isItemValid(i, stack) && (other.getStackInSlot(i).isEmpty() || other.isItemValid(i, stack) && (ItemHandlerHelper.canItemStacksStack(other.getStackInSlot(i), stack) && other.getStackInSlot(i).getCount() + stack.getCount() <= other.getSlotLimit(i)))) {
                                                other.insertItem(i, inventory.extractItem(output, stack.getCount(), false), false);
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

    TagKey<Item> ore = ItemTags.create(new ResourceLocation("forge", "ores"));

    TagKey<Item> raw = ItemTags.create(new ResourceLocation("forge", "raw_materials"));

    protected boolean isOre(ItemStack input){
        return (input.is(ore));
    }
    protected boolean isRaw(ItemStack input){
        return (input.is(raw));
    }
    protected int OreProcessingMultiplier(ItemStack input){
        if (hasUpgradeType(Registration.ORE_PROCESSING.get())){
            OreProcessingUpgradeItem oreup = (OreProcessingUpgradeItem)getUpgradeTypeSlotItem(Registration.ORE_PROCESSING.get()).getItem();
            if  ((isRaw(input) && oreup.acceptRaw) || (isOre( input) && oreup.acceptOre)) return oreup.getMultiplier;

        } else if (input == ItemStack.EMPTY) return 0;
        return 1;
    }
    protected boolean canSmelt(@Nullable Recipe<?> recipe, int INPUT, int OUTPUT) {
        ItemStack input = this.getInv().getStackInSlot(INPUT);
        if (!input.isEmpty() && recipe != null) {
            ItemStack recipeOutput = recipe.getResultItem();
            if (!recipeOutput.isEmpty()) {
                ItemStack output = this.getInv().getStackInSlot(OUTPUT);
                if (output.isEmpty()) return true;
                else if (!output.sameItem(recipeOutput)) return false;
                else {
                    return output.getCount() + recipeOutput.getCount() * OreProcessingMultiplier(input) <= output.getMaxStackSize();
                }
            }
        }
        return false;
    }
    private ItemStack getResult(@Nullable Recipe<?> recipe, ItemStack input) {
        ItemStack out = recipe.getResultItem().copy();
        out.setCount(out.getCount() * OreProcessingMultiplier(input));
        return out;
    }

    protected void smeltItem(@Nullable Recipe<?> recipe, int INPUT, int OUTPUT) {
        timer = 0;
        if (recipe != null && this.canSmelt(recipe, INPUT, OUTPUT)) {
            ItemStack itemstack = this.getInv().getStackInSlot(INPUT);
            ItemStack itemstack2 = this.getInv().getStackInSlot(OUTPUT);
            if (itemstack2.isEmpty()) {
                this.getInv().setStackInSlot(OUTPUT, getResult(recipe, itemstack));
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

            if (itemstack.getItem() == Blocks.WET_SPONGE.asItem() && !this.getInv().getStackInSlot(FUEL()).isEmpty()) {
                FluidUtil.getFluidHandler(this.getItem(FUEL())).ifPresent(e -> {
                    if (e.getFluidInTank(0).isEmpty()) {
                        e.fill(new FluidStack(Fluids.WATER, 1000), IFluidHandler.FluidAction.EXECUTE);
                        this.setItem(FUEL(), e.getContainer());
                    }
                });
            }
            if (ModList.get().isLoaded("pmmo")) {
                    ProjectMMO.burnEvent(itemstack, level, worldPosition);

            }
            itemstack.shrink(1);
        }
    }
    @Override
    public void load( CompoundTag tag) {
        super.load(tag);
        if (tag.getCompound("inventory").isEmpty() && !tag.getList("Items",10).isEmpty()) {
            if (isEmpty())
                getInv().deserializeNBT(tag);
        }else
            getInv().deserializeNBT(tag.getCompound("inventory"));
        this.furnaceBurnTime = tag.getInt("BurnTime");
        this.cookTime = tag.getInt("CookTime");
        this.totalCookTime = tag.getInt("CookTimeTotal");
        this.timer = 0;
        this.recipesUsed = this.getBurnTime(this.getInv().getStackInSlot(1));
        fluidTank.readFromNBT(tag.getCompound("fluidTank"));
        xpTank.readFromNBT(tag.getCompound("xpTank"));
        CompoundTag compoundnbt = tag.getCompound("RecipesUsed");
        energyStorage.deserializeNBT(tag.getCompound("energy"));
        for (String s : compoundnbt.getAllKeys()) {
            this.recipes.put(new ResourceLocation(s), compoundnbt.getInt(s));
        }
        this.show_inventory_settings = tag.getInt("ShowInvSettings");
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        tag.putInt("BurnTime", this.furnaceBurnTime);
        tag.putInt("CookTime", this.cookTime);
        tag.putInt("CookTimeTotal", this.totalCookTime);
        tag.put("fluidTank", fluidTank.writeToNBT(tag.getCompound("fluidTank")));
        tag.put("xpTank", xpTank.writeToNBT(tag.getCompound("xpTank")));
        tag.put("energy", energyStorage.serializeNBT());
        tag.putInt("ShowInvSettings", this.show_inventory_settings);
        CompoundTag compoundnbt = new CompoundTag();
        this.recipes.forEach((recipeId, craftedAmount) -> {
            compoundnbt.putInt(recipeId.toString(), craftedAmount);
        });
        tag.put("RecipesUsed", compoundnbt);
        super.saveAdditional(tag);
    }

    protected static int getBurnTime(ItemStack stack) {
        if (stack.isEmpty()) {
            return 0;
        } else {
            Item item = stack.getItem();
            int ret = stack.getBurnTime(RecipeType.SMELTING);
            return net.minecraftforge.event.ForgeEventFactory.getItemBurnTime(stack, ret == -1 ? AbstractFurnaceBlockEntity.getFuel().getOrDefault(item, 0) : ret, RecipeType.SMELTING);
        }
    }


    public static boolean isItemFuel(ItemStack stack) {
        return getBurnTime(stack) > 0;
    }
    SidedInvWrapper invHandler = new
            SidedInvWrapper (this, null){
                @Override
                public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                    return IisItemValidForSlot(slot, stack);
                }
            };
    LazyOptional<? extends IItemHandler>[] invHandlers =
            invHandler.create(this, Direction.DOWN, Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST);



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
                    if(isLiquid())
                        return (LazyOptional.of(() -> fluidTank).cast());
                }
                else {
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
        }else {
            if (side == side.UP) return INPUTS();
            else if (side == side.DOWN) return OUTPUTS();
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
        }else{
            if (direction == Direction.DOWN && index >= FOUTPUT() && index <= LOUTPUT()) return true;
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

    public void checkXP(@Nullable Recipe<?> recipe) {
        if (!level.isClientSide) {
            boolean flag2 = false;
            if (this.recipes.size() > Config.furnaceXPDropValue.get()) {
                this.grantStoredRecipeExperience(level, new Vec3(worldPosition.getX() + rand.nextInt(2) - 1, worldPosition.getY(), worldPosition.getZ() + rand.nextInt(2) - 1));
                this.recipes.clear();
            } else {
                for (Object2IntMap.Entry<ResourceLocation> entry : this.recipes.object2IntEntrySet()) {
                    if (level.getRecipeManager().byKey(entry.getKey()).isPresent()) {
                        if (entry.getIntValue() > Config.furnaceXPDropValue2.get()) {
                            if (!flag2) {
                                this.grantStoredRecipeExperience(level, new Vec3(worldPosition.getX() + rand.nextInt(2) - 1, worldPosition.getY(), worldPosition.getZ() + rand.nextInt(2) - 1));
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
    public void setRecipeUsed(@Nullable Recipe<?> recipe) {

        if (recipe != null) {
            ResourceLocation resourcelocation = recipe.getId();
            this.recipes.addTo(resourcelocation, 1);
        }

    }

    @Nullable
    @Override
    public Recipe<?> getRecipeUsed() {
        return null;
    }

    public void unlockRecipes(Player player) {
        List<Recipe<?>> list = this.grantStoredRecipeExperience(player.level, player.position());
        player.awardRecipes(list);
        this.recipes.clear();
    }

    public List<Recipe<?>> grantStoredRecipeExperience(Level level, Vec3 worldPosition) {
        List<Recipe<?>> list = Lists.newArrayList();
        if (this.recipes.object2IntEntrySet() != null)
            recipes.object2IntEntrySet().fastForEach(entry-> level.getRecipeManager().byKey(entry.getKey()).ifPresent((h) -> {
                list.add(h);
                if (hasXPTank()) {
                    int amountLiquidXp = Mth.floor((float) entry.getIntValue() * ((AbstractCookingRecipe) h).getExperience()) * 5;
                    if (amountLiquidXp >= 1) {
                        xpTank.fill(new FluidStack(Config.getLiquidXP(), amountLiquidXp), IFluidHandler.FluidAction.EXECUTE);
                        recipes.clear();
                    }
                }else {
                    if (worldPosition != null)
                        splitAndSpawnExperience(level, worldPosition, entry.getIntValue(), ((AbstractCookingRecipe) h).getExperience());
                }
            }));

        return list;
    }

    private static void splitAndSpawnExperience(Level level, Vec3 worldPosition, int craftedAmount, float experience) {
        int i = Mth.floor((float) craftedAmount * experience);
        float f = Mth.frac((float) craftedAmount * experience);
        if (f != 0.0F && Math.random() < (double) f) {
            ++i;
        }

        while (i > 0) {
            int j = ExperienceOrb.getExperienceValue(i);
            i -= j;
            level.addFreshEntity(new ExperienceOrb(level, worldPosition.x, worldPosition.y, worldPosition.z, j));
        }

    }

    @Override
    public void fillStackedContents(StackedContents helper) {
        for (ItemStack itemstack : this.inventoryList) {
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