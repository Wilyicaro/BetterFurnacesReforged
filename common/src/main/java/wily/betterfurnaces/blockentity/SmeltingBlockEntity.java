package wily.betterfurnaces.blockentity;

import java.util.function.Supplier;
import com.google.common.collect.Lists;
import dev.architectury.fluid.FluidStack;
import dev.architectury.platform.Platform;
import dev.architectury.registry.fuel.FuelRegistry;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.AirItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.Nullable;
import wily.betterfurnaces.BetterFurnacesPlatform;
import wily.betterfurnaces.Config;
import wily.betterfurnaces.ProjectMMO;
import wily.betterfurnaces.blocks.SmeltingBlock;
import wily.betterfurnaces.init.Registration;
import wily.betterfurnaces.inventory.*;
import wily.betterfurnaces.items.FuelEfficiencyUpgradeItem;
import wily.betterfurnaces.items.GeneratorUpgradeItem;
import wily.betterfurnaces.items.OreProcessingUpgradeItem;
import wily.betterfurnaces.items.UpgradeItem;
import wily.factoryapi.FactoryAPIPlatform;
import wily.factoryapi.ItemContainerUtil;
import wily.factoryapi.base.*;

import java.awt.*;
import java.util.*;
import java.util.List;

public class SmeltingBlockEntity extends InventoryBlockEntity implements RecipeHolder, StackedContentsCompatible{
    public final int[] provides = new int[Direction.values().length];
    private final int[] lastProvides = new int[this.provides.length];


    public int[] FUEL() {
        int[] inputs = new int[]{1};
        if (hasUpgradeType(Registration.STORAGE.get())) inputs = ArrayUtils.add(inputs,7);
        return inputs;
    }
    public int HEATER() {return FUEL()[0];}
    public int[] UPGRADES(){ return new int[]{3,4,5};}
    public int FINPUT(){ return INPUTS()[0];}
    public int LINPUT(){ return INPUTS()[INPUTS().length - 1];}
    public int FOUTPUT(){ return OUTPUTS()[0];}
    public int LOUTPUT(){ return OUTPUTS()[OUTPUTS().length - 1];}
    public int[] INPUTS(){
        int[] inputs = new int[]{0};
        if (hasUpgradeType(Registration.STORAGE.get())) inputs = ArrayUtils.add(inputs,6);
        return inputs;
    }
    public int[] OUTPUTS(){
        int[] outputs = new int[]{2};
        if (hasUpgradeType(Registration.STORAGE.get())) outputs = ArrayUtils.add(outputs,8);
        return outputs;
    }
    public int[] FSLOTS(){ return  ArrayUtils.addAll(ISLOTS(), OUTPUTS());}
    public int[] ISLOTS(){ return  ArrayUtils.addAll(INPUTS(), FUEL());}

    private Random rand = new Random();

    public int showInventorySettings;

    public boolean showOrientation;
    protected int timer;

    public int EnergyUse() {return 500;}
    public long LiquidCapacity() {return 4 * FluidStack.bucketAmount();}
    public int EnergyCapacity() {return 16000;}
    private int furnaceBurnTime;
    public int cookTime;
    public int totalCookTime = this.getCookTime();
    public final Supplier<Integer> defaultCookTime;
    private int recipesUsed;
    private final Object2IntOpenHashMap<ResourceLocation> recipes = new Object2IntOpenHashMap<>();
    public boolean isForge(){ return false;}
    public RecipeType<? extends AbstractCookingRecipe> recipeType;

    public FactoryUpgradeSettings furnaceSettings;

    private final LRUCache<Item, Optional<AbstractCookingRecipe>> cache = LRUCache.newInstance(Config.cacheCapacity.get());
    protected LRUCache<Item, Optional<AbstractCookingRecipe>> blasting_cache = LRUCache.newInstance(Config.cacheCapacity.get());
    protected LRUCache<Item, Optional<AbstractCookingRecipe>> smoking_cache = LRUCache.newInstance(Config.cacheCapacity.get());

    public Direction facing(){
        return this.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING);
    }


    public SmeltingBlockEntity(BlockPos pos, BlockState state, final Supplier<Integer> cookTime) {
        super(Registration.BLOCK_ENTITIES.getRegistrar().get(state.getBlock().arch$registryName()), pos, state);
        this.defaultCookTime = cookTime;
        this.recipeType = RecipeType.SMELTING;
        furnaceSettings = new FactoryUpgradeSettings(()->getUpgradeTypeSlotItem(Registration.FACTORY.get())) {
            @Override
            public void onChanged() {
                if (hasUpgradeType(Registration.FACTORY.get())) {
                    inventory.setItem(getUpgradeTypeSlot(Registration.FACTORY.get()), factory.get());

                }
                setChanged();
            }
        };
    }


    private int getFromCache(LRUCache<Item, Optional<AbstractCookingRecipe>> c, Item key) {
        if (c == null) return 0;
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
        LRUCache<Item, Optional<AbstractCookingRecipe>>[] caches = new LRUCache[]{cache,blasting_cache,smoking_cache};
        return caches[getUpdatedType() >=3 ? 0 : getUpdatedType()];
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
        return (hasUpgrade(Registration.ENERGY.get()) && energyStorage.getEnergyStored() >= EnergyUse());
    }
    public int getCookTime() {
        if (hasUpgrade(Registration.GENERATOR.get()) || arraySlotAllEmpty(INPUTS())) {
            return Optional.ofNullable(defaultCookTime).orElse(()->totalCookTime).get();
        }
        int speed = getSpeed();
        if (speed == -1) {
            return -1;
        }

        return Math.max(1, speed);
    }

    protected int getSpeed() {
        int j = 0;
            int length = INPUTS().length;
            for (int i : INPUTS()) {
                int iC = getFromCache(getCache(), inventory.getItem(i).getItem());
                j += Math.max(iC, 0);
                if (j <= 0) length -= 1;
            }
            j = length <= 0 ? 0 : j / length;
            if (j == 0) {
                for (int i : INPUTS()) {
                    Optional<AbstractCookingRecipe> recipe = grabRecipe(inventory.getItem(i));
                    j = !recipe.isPresent() ? -1 : recipe.orElse(null).getCookingTime();
                    getCache().put(inventory.getItem(i).getItem(), recipe);
                }
                if (j == -1) {
                    return -1;
                }
            }

        if (j < defaultCookTime.get()) {
            return j - (200 - defaultCookTime.get());
        } else {
            return defaultCookTime.get();
        }
    }

    public int getCookTimeConfig() {
        return 0;
    }

    public final ContainerData fields = new ContainerData() {
        public int get(int index) {
            switch (index) {
                case 0:
                    return SmeltingBlockEntity.this.furnaceBurnTime;
                case 1:
                    return SmeltingBlockEntity.this.recipesUsed;
                case 2:
                    return SmeltingBlockEntity.this.cookTime;
                case 3:
                    return SmeltingBlockEntity.this.totalCookTime;
                case 4:
                    return SmeltingBlockEntity.this.showInventorySettings;
                default:
                    return 0;
            }
        }

        public void set(int index, int value) {
            switch (index) {
                case 0:
                    SmeltingBlockEntity.this.furnaceBurnTime = value;
                    break;
                case 1:
                    SmeltingBlockEntity.this.recipesUsed = value;
                    break;
                case 2:
                    SmeltingBlockEntity.this.cookTime = value;
                    break;
                case 3:
                    SmeltingBlockEntity.this.totalCookTime = value;
                    break;
                case 4:
                    SmeltingBlockEntity.this.showInventorySettings = value;
                    break;
            }

        }

        @Override
        public int getCount() {
            return 5;
        }
    };

    public List<UpgradeItem> getUpgrades() {
        List<UpgradeItem> list = new ArrayList<>();
        for (int slot : UPGRADES())
            if (inventory.getItem(slot).getItem() instanceof UpgradeItem upg )list.add(upg);
        return list;
    }
    public boolean hasUpgrade(UpgradeItem upg) {
        for (int slot : UPGRADES())
            if (upg.equals(inventory.getItem(slot).getItem()) && upg.isEnabled()) return true;
        return false;
    }

    public boolean hasUpgradeType(UpgradeItem upg) {
        return getUpgradeTypeSlot(upg)>=0;
    }

    public ItemStack getUpgradeTypeSlotItem(UpgradeItem upg) {
        int i = getUpgradeTypeSlot(upg);
        return i < 0 ? ItemStack.EMPTY : inventory.getItem(i);
    }
    public int getUpgradeTypeSlot(UpgradeItem upg) {
        for (int slot : UPGRADES())
            if (inventory.getItem(slot).getItem() instanceof UpgradeItem upgradeItem &&  upgradeItem.isEnabled() && upg.isSameType(upgradeItem)) return slot;
        return -1;
    }

    public ItemStack getUpgradeSlotItem(Item upg) {
        for (int slot : UPGRADES())
            if (upg == inventory.getItem(slot).getItem()) return inventory.getItem(slot);
        return ItemStack.EMPTY;
    }
    public static int getFluidBurnTime(FluidStack stack) {
        return stack == null ? 0 : FuelRegistry.get(stack.getFluid().getBucket().getDefaultInstance());
    }
    public final IPlatformFluidHandler<?> fluidTank = FactoryAPIPlatform.getFluidHandlerApi(LiquidCapacity(), this,fs -> (getBurnTime(new ItemStack(fs.getFluid().getBucket())) > 0), SlotsIdentifier.LAVA, TransportState.INSERT);
    public final IPlatformFluidHandler<?> xpTank = FactoryAPIPlatform.getFluidHandlerApi(2*FluidStack.bucketAmount(), this, xp -> xp.getFluid().isSame(Config.getLiquidXP()), SlotsIdentifier.GENERIC, TransportState.EXTRACT);
    public final IPlatformEnergyStorage<?> energyStorage = FactoryAPIPlatform.getEnergyStorageApi(EnergyCapacity(),this);
    public void forceUpdateAllStates() {
        BlockState state = level.getBlockState(worldPosition);
        if (state.getValue(BlockStateProperties.LIT) != this.isBurning()) {
            level.setBlock(worldPosition, state.setValue(BlockStateProperties.LIT, this.isBurning()), 3);
        }
    }
    public int correspondentOutputSlot(int input){
        return hasUpgradeType(Registration.STORAGE.get())?  Arrays.stream(OUTPUTS()).filter(i-> canSmelt(irecipeSlot(input).orElse(null), input, i)).min().orElse(-1) : FOUTPUT() - FINPUT() + input;
    }
    public void trySmelt(){
        if (hasUpgradeType(Registration.STORAGE.get())) {
            int i = correspondentOutputSlot(FINPUT());
            if (i>=0) this.smeltItem(irecipeSlot(FINPUT()).orElse(null), FINPUT(), i);
        } else
                for (int i : INPUTS()) {
                    if(!this.canSmelt(irecipeSlot(i).orElse(null), i, correspondentOutputSlot(i))) continue;
                    this.smeltItem(irecipeSlot(i).orElse(null), i, correspondentOutputSlot(i));
                }
    }
    public int getUpdatedType(){
        return hasUpgrade(Registration.BLAST.get()) ? 1 : hasUpgrade(Registration.SMOKE.get()) ? 2 : hasUpgrade(Registration.GENERATOR.get()) ? 3 : 0;
    }

    @Override
    public Component getDisplayName() {
        String tier = getBlockState().getBlock().arch$registryName().getPath().split("_")[0];
        return getUpdatedType() != 0 ? Component.translatable("tooltip.betterfurnacesreforged." + tier +"_tier" ,getUpdatedType() == 1 ? Blocks.BLAST_FURNACE.getName().getString() : getUpdatedType() == 2 ? Blocks.SMOKER.getName().getString() : getUpdatedType() == 3 ? Component.translatable("tooltip.betterfurnacesreforged.generator").getString() : "") : super.getDisplayName();
    }


    public Optional<AbstractCookingRecipe> irecipeSlot(int input){
        if (!ArrayUtils.contains(INPUTS(), input)) return Optional.empty();
        if (!inventory.getItem(input).isEmpty())
            return grabRecipe(inventory.getItem(input));
        else
            return Optional.empty();
    }
    public boolean hasArraySlotSpace(int[] slots){
        for (int i : slots) {
            boolean noFull = inventory.getItem(i).getCount() < inventory.getItem(i).getMaxStackSize() && !inventory.getItem(i).isEmpty();
            if(noFull) continue;
            return true;
        }
        return false;
    }
    public boolean arraySlotFilled(int[] slots, boolean isFilled){
        for (int i : slots) {
            boolean filled = inventory.getItem(i).isEmpty();
            if (!isFilled) filled = !filled;
            if(filled) continue;
            return true;
        }
        return false;
    }
    public boolean arraySlotAllEmpty(int[] slots){
        for (int i : slots) {
            boolean empty = inventory.getItem(i).isEmpty();
            if (!empty) return false;
        }
        return true;
    }
    public boolean canGeneratorWork(){
        if (hasUpgrade(Registration.GENERATOR.get())){
            ItemStack stack = getUpgradeSlotItem(Registration.GENERATOR.get());
            return (ItemContainerUtil.isFluidContainer(stack) && ItemContainerUtil.getFluid(stack).getAmount() > 0) && energyStorage.getSpace() > 0;
        }
        return false;
    }
    public boolean smeltValid(){
        if (!hasUpgrade(Registration.GENERATOR.get()))
            for (int i : INPUTS()) {
            if(!this.canSmelt(irecipeSlot(i).orElse(null), i, correspondentOutputSlot(i))) continue;
            return true;
        }
        return false;
    }
    public static void tick(Level level, BlockPos worldPosition, BlockState blockState, SmeltingBlockEntity e) {

        boolean wasBurning = e.isBurning();
        boolean flag1 = false;
        boolean flag2 = false;

        if (e.isBurning()) {
            --e.furnaceBurnTime;
        }


        if (e.hasXPTank()) e.grantStoredRecipeExperience(level, null);

        if (!e.hasUpgradeType(Registration.FACTORY.get()) && e.showOrientation) e.showOrientation = false;

        ItemStack fuel = e.inventory.getItem(e.FUEL()[0]);
        if ((e.hasUpgrade(Registration.COLOR.get()))) {
            if (!(level.getBlockState(e.getBlockPos()).getValue(SmeltingBlock.COLORED)))
                level.setBlock(e.getBlockPos(), level.getBlockState(e.getBlockPos()).setValue(SmeltingBlock.COLORED, true), 3);
        } else if ((level.getBlockState(e.getBlockPos()).getValue(SmeltingBlock.COLORED))) level.setBlock(e.getBlockPos(), level.getBlockState(e.getBlockPos()).setValue(SmeltingBlock.COLORED, false), 3);

        int updatedType = e.getUpdatedType();
        RecipeType<? extends AbstractCookingRecipe>[] recipeTypes = new RecipeType[]{RecipeType.SMELTING,RecipeType.BLASTING, RecipeType.SMOKING};
        if (updatedType == 3) {
            for (int i : new int[]{e.FINPUT(), e.FOUTPUT()}){
                ItemStack stack = e.inventory.getItem(i);
                if (!stack.isEmpty())Containers.dropItemStack(level, e.getBlockPos().getX(), e.getBlockPos().getY() + 1, e.getBlockPos().getZ(), stack);
            }
        }else if (e.recipeType != recipeTypes[updatedType]) e.recipeType = recipeTypes[updatedType];

        if (!e.isForge() &&   level.getBlockState(e.getBlockPos()).getValue(SmeltingBlock.TYPE) != updatedType)
            level.setBlock(e.getBlockPos(), level.getBlockState(e.getBlockPos()).setValue(SmeltingBlock.TYPE, updatedType), 3);

        if (!level.isClientSide) {
            int get_cook_time = e.getCookTime();
            e.timer++;
            if (e.hasUpgrade(Registration.GENERATOR.get()))
                BetterFurnacesPlatform.transferEnergySides(e);

            if (e.hasUpgradeType(Registration.STORAGE.get())) {
                ItemStack storageInput = e.inventory.getItem(6);
                if (!storageInput.isEmpty()){
                    int added = e.addOrSetItem(storageInput,e.inventory,e.FINPUT());
                    if (added > 0) e.inventory.extractItem(6, added,false);
                }
                ItemStack storageFuel = e.inventory.getItem(7);
                if (!storageFuel.isEmpty()){
                    int added = e.addOrSetItem(storageFuel,e.inventory,e.FUEL()[0]);
                    if (added > 0) e.inventory.extractItem(7, added,false);
                }
            }
            else if (!e.isForge()){
                for (int i : new int[]{6,7,8}){
                    ItemStack stack = e.inventory.getItem(i);
                    if (!stack.isEmpty())Containers.dropItemStack(level, e.getBlockPos().getX(), e.getBlockPos().getY() + 1, e.getBlockPos().getZ(), stack);
                }
            }


            if (e.hasUpgrade(Registration.ENERGY.get())) {
                if (ItemContainerUtil.isEnergyContainer(fuel) && ItemContainerUtil.getEnergy(fuel) > 0) {
                    if (e.energyStorage.getSpace() > 0) {
                        e.energyStorage.receiveEnergy(ItemContainerUtil.extractEnergy(e.energyStorage.getSpace(),fuel).contextEnergy(), false);
                        e.inventory.setItem(e.FUEL()[0], fuel);
                    }
                }
            }
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
                    e.provides[i] = e.getBlockState().getDirectSignal(e.level, worldPosition, Direction.values()[i]);

            } else {
                for (int i = 0; i < Direction.values().length; i++)
                    e.provides[i] = 0;
            }
            if (e.doesNeedUpdateSend()) {
                e.onUpdateSent();
            }

            if (e.isLiquid() && ItemContainerUtil.isFluidContainer(fuel) && SmeltingBlockEntity.isItemFuel(ItemContainerUtil.getFluid(fuel).getFluid().getBucket().getDefaultInstance())) {
                ItemContainerUtil.ItemFluidContext context = ItemContainerUtil.drainItem(e.fluidTank.getTotalSpace(),fuel);
                    long amount = e.fluidTank.fill(context.fluidStack(), false);
                    if (amount > 0) {
                        level.playSound(null, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), SoundEvents.BUCKET_FILL_LAVA, SoundSource.PLAYERS, 0.6F, 0.8F);
                        e.inventory.setItem(e.FUEL()[0], context.container());
                    }


            }
            if ((e.isBurning() || !fuel.isEmpty() || e.isLiquid() || (e.isEnergy() && !e.canGeneratorWork())) &&  (e.arraySlotFilled(e.INPUTS(), true)|| e.canGeneratorWork()))  {
                boolean valid = e.smeltValid() || e.canGeneratorWork();
                if (!e.isBurning() && (valid)) {
                    if (e.isLiquid() && !e.fluidTank.getFluidStack().isEmpty() && getFluidBurnTime(e.fluidTank.getFluidStack()) > 0){
                        int fluidAmount= (200 * (int)FluidStack.bucketAmount()) / getFluidBurnTime(e.fluidTank.getFluidStack());
                        if (e.fluidTank.getFluidStack().getAmount() >= fluidAmount) {
                            e.furnaceBurnTime = e.getEnderMultiplier() * get_cook_time;
                            e.recipesUsed = e.furnaceBurnTime;
                            e.fluidTank.drain(fluidAmount, false);
                        }
                    }else if (e.isEnergy()){
                        e.furnaceBurnTime = e.getEnderMultiplier() * get_cook_time;
                        e.recipesUsed = e.furnaceBurnTime;
                        for (int a : e.INPUTS())
                            e.energyStorage.consumeEnergy(e.EnergyUse() * e.OreProcessingMultiplier(e.inventory.getItem(a)), false);
                    }else{
                        e.furnaceBurnTime =  e.getEnderMultiplier() * getBurnTime(fuel) * get_cook_time / 200;
                        e.recipesUsed = e.furnaceBurnTime;
                    }
                    if (e.isBurning()) {
                        flag1 = true;
                        if (e.hasEnder()) {
                            if (e.hasUpgrade(Registration.FUEL.get())) {
                                e.breakDurabilityItem(e.getUpgradeSlotItem(Registration.FUEL.get()));
                            }
                        }
                        if ((!e.isLiquid() || e.fluidTank.getFluidStack().getAmount() < 10) && !e.isEnergy()) {
                            if (ItemContainerUtil.isFluidContainer(fuel)){
                                ItemContainerUtil.ItemFluidContext context = ItemContainerUtil.drainItem(e.fluidTank.getTotalSpace(),fuel);
                                e.inventory.setItem(e.FUEL()[0],context.container());
                            }

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
                    if (e.hasUpgrade(Registration.GENERATOR.get())){
                        ItemContainerUtil.ItemFluidContext context = ItemContainerUtil.drainItem(FluidStack.bucketAmount() / 1000, e.getUpgradeSlotItem(Registration.GENERATOR.get()));
                        if (!context.fluidStack().isEmpty()) e.inventory.setItem(e.getUpgradeTypeSlot(Registration.GENERATOR.get()), context.container());
                        e.energyStorage.receiveEnergy(Math.round((float)500 / get_cook_time),false);
                    }
                    if (e.cookTime >= e.totalCookTime) {
                        e.cookTime = 0;
                        e.totalCookTime = e.getCookTime();
                        if (!e.hasUpgrade(Registration.GENERATOR.get())) {
                            e.trySmelt();
                            if (e.hasUpgradeType(Registration.FACTORY.get()))
                                BetterFurnacesPlatform.smeltingAutoIO(e);
                        }
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
                        BetterFurnacesPlatform.smeltingAutoIO(e);
                        flag1 = true;
                    } else if (e.hasArraySlotSpace(e.INPUTS())) {
                        BetterFurnacesPlatform.smeltingAutoIO(e);
                        flag1 = true;
                    }
                    if (e.arraySlotFilled(e.OUTPUTS(), true)) {
                        BetterFurnacesPlatform.smeltingAutoIO(e);
                        flag1 = true;
                    }
                    if (e.inventory.getItem(e.FUEL()[0]).isEmpty() && !e.isLiquid() && !e.isEnergy()) {
                        BetterFurnacesPlatform.smeltingAutoIO(e);
                        flag1 = true;
                    } else if (e.inventory.getItem(e.FUEL()[0]).getCount() < e.inventory.getItem(e.FUEL()[0]).getMaxStackSize() || ItemContainerUtil.isFluidContainer(fuel) && ItemContainerUtil.getFluid(fuel).getAmount() < e.fluidTank.getTotalSpace()){
                        BetterFurnacesPlatform.smeltingAutoIO(e);
                        flag1 = true;
                    }
                }
            }
        }

        if (flag1) {
            e.setChanged();
        }

    }
    public Color getColor() {
        if (getUpgradeSlotItem(Registration.COLOR.get()).isEmpty()) return Color.WHITE;
        CompoundTag nbt = getUpgradeSlotItem(Registration.COLOR.get()).getTag();
            return new Color(nbt.getInt("red") ,nbt.getInt("green") ,nbt.getInt("blue"));
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

    TagKey<Item> ore = getItemTag(new ResourceLocation(Platform.isForge() ? "forge" : "c", "ores"));



    private TagKey<Item> getItemTag(ResourceLocation resourceLocation) {
        return TagKey.create(Registries.ITEM,resourceLocation);
    }
    private boolean hasRawOreTag(ItemStack stack){
        if (Config.checkRawOresName.get()) return stack.getItem().arch$registryName().getPath().startsWith("raw_");
        if(Platform.isForge()) return stack.is(getItemTag( new ResourceLocation("forge","raw_materials")));
        else {
            if (stack.is(getItemTag(new ResourceLocation("c", "raw_materials")))) return true;
            for (TagKey<Item> tag: stack.getTags().toList()){
                if (!tag.location().toString().contains("raw_") ||!tag.location().toString().contains("_ores")) continue;
                return true;
            }
            return false;
        }
    }
    protected boolean isOre(ItemStack input){
        if (Config.checkCommonOresName.get()) return input.getItem().arch$registryName().getPath().endsWith("_ore");
        return (input.is(ore));
    }
    protected boolean isRaw(ItemStack input){
        return (hasRawOreTag(input));
    }
    protected int OreProcessingMultiplier(ItemStack input){
        if (hasUpgradeType(Registration.ORE_PROCESSING.get())){
            OreProcessingUpgradeItem oreup = (OreProcessingUpgradeItem)getUpgradeTypeSlotItem(Registration.ORE_PROCESSING.get()).getItem();
            if  ((isRaw(input) && oreup.acceptRaw) || (isOre( input) && oreup.acceptOre)) return oreup.getMultiplier;

        } else if (input.isEmpty()) return 0;
        return 1;
    }

    @Override
    public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
        return new SmeltingMenu(Registration.FURNACE_CONTAINER.get(),i,level,getBlockPos(),playerInventory,playerEntity,fields);
    }

    protected boolean canSmelt(@Nullable Recipe<?> recipe, int INPUT, int OUTPUT) {
        ItemStack input = this.getInv().getItem(INPUT);
        if (OUTPUT >= 0 && !input.isEmpty() && recipe != null) {
            ItemStack recipeOutput = recipe.getResultItem(RegistryAccess.EMPTY);
            if (!recipeOutput.isEmpty()) {
                ItemStack output = this.getInv().getItem(OUTPUT);
                if (output.isEmpty()) return true;
                else if (!ItemStack.isSameItemSameTags(output,recipeOutput)) return false;
                else {
                    return output.getCount() + recipeOutput.getCount() * OreProcessingMultiplier(input) <= output.getMaxStackSize();
                }
            }
        }
        return false;
    }
    private ItemStack getResult(@Nullable Recipe<?> recipe, ItemStack input) {
        ItemStack out = recipe.getResultItem(RegistryAccess.EMPTY).copy();
        out.setCount(out.getCount() * OreProcessingMultiplier(input));
        return out;
    }
    protected int addOrSetItem(ItemStack stack, Container inv, int index){
        ItemStack slotStack = inv.getItem(index);

        if (slotStack.isEmpty()) {
            inv.setItem(index, stack.copy());
            return Math.min(inv.getMaxStackSize(),stack.getCount());
        } else {
            int resultCount =  slotStack.getCount() + stack.getCount();
            int maxStack = Math.min(inv.getMaxStackSize(),slotStack.getMaxStackSize());
            if (slotStack.is(stack.getItem()) && slotStack.getCount() < maxStack) {
                if (resultCount <= maxStack) {
                    slotStack.grow(Math.max(stack.getCount(), 1));
                    return Math.max(stack.getCount(), 1);
                }
                int count = maxStack - slotStack.getCount();
                slotStack.setCount(maxStack);
                return count;
            }
        }
        return 0;
    }
    protected void smeltItem(@Nullable Recipe<?> recipe, int INPUT, int OUTPUT) {
        timer = 0;
        if (recipe != null && this.canSmelt(recipe, INPUT, OUTPUT)) {
            ItemStack input = this.getInv().getItem(INPUT);

            if (addOrSetItem(getResult(recipe,input),inventory,OUTPUT) > 0 && hasUpgrade(Registration.ORE_PROCESSING.get()) && (isOre(input))) {
                breakDurabilityItem(getUpgradeSlotItem(Registration.ORE_PROCESSING.get()));
            }

            this.checkXP(recipe);
            if (!this.level.isClientSide) {
                this.setRecipeUsed(recipe);
            }
            ItemStack fuel = inventory.getItem(FUEL()[0]);
            if (input.getItem() == Blocks.WET_SPONGE.asItem() && !fuel.isEmpty()) {
                if (ItemContainerUtil.isFluidContainer(fuel)){
                    ItemContainerUtil.fillItem(fuel,FluidStack.create(Fluids.WATER, 1000));
                    inventory.setItem(FUEL()[0],fuel);
                }
            }

            if (Platform.isModLoaded("pmmo")) {
                ProjectMMO.burnEvent(input,level,worldPosition);
            }
            input.shrink(1);
        }
    }
    @Override
    public void load( CompoundTag tag) {
        super.load(tag);
        this.furnaceBurnTime = tag.getInt("BurnTime");
        this.cookTime = tag.getInt("CookTime");
        this.totalCookTime = tag.getInt("CookTimeTotal");
        this.timer = 0;
        this.recipesUsed = this.getBurnTime(this.getInv().getItem(1));
        fluidTank.deserializeTag(tag.getCompound("fluidTank"));
        xpTank.deserializeTag(tag.getCompound("xpTank"));
        CompoundTag compoundnbt = tag.getCompound("RecipesUsed");
        energyStorage.deserializeTag(tag.getCompound("energy"));
        for (String s : compoundnbt.getAllKeys()) {
            this.recipes.put(new ResourceLocation(s), compoundnbt.getInt(s));
        }
        this.showInventorySettings = tag.getInt("ShowInvSettings");
        this.showOrientation = tag.getBoolean("ShowOrientation");
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        tag.putInt("BurnTime", this.furnaceBurnTime);
        tag.putInt("CookTime", this.cookTime);
        tag.putInt("CookTimeTotal", this.totalCookTime);
        tag.put("fluidTank", fluidTank.serializeTag());
        tag.put("xpTank", xpTank.serializeTag());
         tag.put("energy",energyStorage.serializeTag());
        tag.putInt("ShowInvSettings", this.showInventorySettings);
        tag.putBoolean("ShowOrientation", this.showOrientation);
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
            return FuelRegistry.get(stack);
        }
    }


    public static boolean isItemFuel(ItemStack stack) {
        return getBurnTime(stack) > 0;
    }



    public <T extends IPlatformHandlerApi<?>> Optional<T> getStorage(Storages.Storage<T> storage, Direction facing){
        if (storage == Storages.FLUID){
            if ((facing == null || facing.ordinal() == getIndexTop() || facing.ordinal() == getIndexBottom())) {
                if(isLiquid())
                    return ((Optional<T>) Optional.of(fluidTank));
            }
            else {
                if (hasUpgrade(Registration.GENERATOR.get())) {
                    ItemStack gen = getUpgradeSlotItem(Registration.GENERATOR.get());
                    return ((Optional<T>) Optional.ofNullable(gen.getItem() instanceof GeneratorUpgradeItem item ? item.getFluidStorage(gen) : null));
                } else if (hasXPTank())
                    return ((Optional<T>) Optional.of( xpTank));
            }
        }
        if (storage == Storages.ITEM){
            if (facing != null)
                return (Optional<T>) Optional.of(FactoryAPIPlatform.filteredOf(inventory,facing, getSlotsTransport(facing).key(), getSlotsTransport(facing).value()));
            else return (Optional<T>) Optional.of(inventory);
        }
        if (storage == Storages.ENERGY && (hasUpgrade(Registration.ENERGY.get()) ||  hasUpgrade(Registration.GENERATOR.get()))){
            return (Optional<T>) Optional.ofNullable(FactoryAPIPlatform.filteredOf(energyStorage, TransportState.ofBoolean( true, !hasUpgrade(Registration.GENERATOR.get()))));
        }
        return Optional.empty();
    }
    public int getIndexBottom() {
        return 0;
    }
    public int getIndexTop() {
        return 1;
    }

    @Override
    public Pair<int[], TransportState> getSlotsTransport(Direction side) {
        if (hasUpgradeType(Registration.FACTORY.get())) {
            if (this.furnaceSettings.get(side.ordinal()) == 0) {
                return Pair.of(new int[]{},TransportState.NONE);
            } else if (this.furnaceSettings.get(side.ordinal()) == 1) {
                return Pair.of(ISLOTS(),TransportState.INSERT);
            } else if (this.furnaceSettings.get(side.ordinal()) == 2) {
                return Pair.of(OUTPUTS(),TransportState.EXTRACT_INSERT);
            } else if (this.furnaceSettings.get(side.ordinal()) == 3) {
                return Pair.of(FSLOTS(), TransportState.EXTRACT_INSERT);
            } else if (this.furnaceSettings.get(side.ordinal()) == 4) {
                return Pair.of(new int[]{FUEL()[0]}, TransportState.EXTRACT_INSERT);
            }
        }else {
            if (side == Direction.UP) return Pair.of(INPUTS(),TransportState.INSERT);
            else if (side == Direction.DOWN) return Pair.of(OUTPUTS(),TransportState.EXTRACT);
            else return Pair.of( new int[]{FUEL()[0]},TransportState.EXTRACT_INSERT);
        }

        return Pair.of(new int[]{},TransportState.NONE);
    }

    @Override
    public boolean IcanExtractItem(int index, ItemStack stack) {
        if (hasUpgradeType(Registration.FACTORY.get())) {
            if (ArrayUtils.contains(INPUTS(),index) || ArrayUtils.contains(UPGRADES(),index) || (ArrayUtils.contains(FUEL(),index) && (isItemFuel(stack) ||( ItemContainerUtil.isEnergyContainer(stack) && ItemContainerUtil.getEnergy(stack) > 0)))) return false;
        }else{
            if (index >= FOUTPUT() && index <= LOUTPUT()) return true;
            return false;
        }
        return true;
    }

    @Override
    public boolean IisItemValidForSlot(int index, ItemStack stack) {
        return getSlots(null).get(index).mayPlace(stack);
    }

    @Override
    public void addSlots(NonNullList<Slot> slots, @Nullable Player player) {
        slots.add(new SlotInput(this, 0, 54, 18, (s) -> !this.hasUpgrade(Registration.GENERATOR.get())));
        slots.add(new SlotFuel(this, 1, 54, 54));
        slots.add(new SlotOutput(player, this, 2, 116, 35, (s) -> !this.hasUpgrade(Registration.GENERATOR.get())));

        slots.add(new SlotUpgrade(this, 3, 8, 18));
        slots.add(new SlotUpgrade(this, 4, 8, 36));
        slots.add(new SlotUpgrade(this, 5, 8, 54));

        slots.add(new SlotInput(this, 6, 36, 18, s -> hasUpgradeType(Registration.STORAGE.get())));
        slots.add(new SlotFuel(this, 7, 36, 54, s-> hasUpgradeType(Registration.STORAGE.get())));
        slots.add(new SlotOutput(player, this, 8, 138, 35, s -> hasUpgradeType(Registration.STORAGE.get())));

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

    public Recipe<?> getRecipeUsed(){
        return  null;
    }


    public void unlockRecipes(Player player) {

        List<Recipe<?>> list = this.grantStoredRecipeExperience(player.level(), player.position());
        player.awardRecipes(list);
        this.recipes.clear();
    }

    public List<Recipe<?>> grantStoredRecipeExperience(Level level, Vec3 worldPosition) {
        List<Recipe<?>> list = Lists.newArrayList();
            this.recipes.object2IntEntrySet().fastForEach(entry-> {
                level.getRecipeManager().byKey(entry.getKey()).ifPresent((h) -> {
                    list.add(h);
                    if (hasXPTank()) {
                        int amountLiquidXp = Mth.floor((float) entry.getIntValue() * ((AbstractCookingRecipe) h).getExperience()) * 5;
                        if (amountLiquidXp >= 1) {
                          xpTank.fill(FluidStack.create(Config.getLiquidXP(), amountLiquidXp * FluidStack.bucketAmount() /1000), false);
                            recipes.clear();
                        }
                    }else {
                        if (worldPosition != null)
                            splitAndSpawnExperience(level, worldPosition, entry.getIntValue(), ((AbstractCookingRecipe) h).getExperience());
                    }
                });
            });

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
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            helper.accountStack(inventory.getItem(i));
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