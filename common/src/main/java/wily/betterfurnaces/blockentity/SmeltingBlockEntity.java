package wily.betterfurnaces.blockentity;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import me.shedaniel.architectury.fluid.FluidStack;
import me.shedaniel.architectury.platform.Platform;
import me.shedaniel.architectury.registry.fuel.FuelRegistry;
import me.shedaniel.architectury.utils.Fraction;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
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
import net.minecraft.world.level.block.entity.BlockEntityType;
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
import wily.betterfurnaces.init.BlockEntityTypes;
import wily.betterfurnaces.init.ModObjects;
import wily.betterfurnaces.inventory.*;
import wily.betterfurnaces.items.FuelEfficiencyUpgradeItem;
import wily.betterfurnaces.items.GeneratorUpgradeItem;
import wily.betterfurnaces.items.OreProcessingUpgradeItem;
import wily.betterfurnaces.items.UpgradeItem;
import wily.factoryapi.FactoryAPIPlatform;
import wily.factoryapi.ItemContainerUtil;
import wily.factoryapi.base.*;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.function.Supplier;

public class SmeltingBlockEntity extends InventoryBlockEntity implements RecipeHolder, StackedContentsCompatible{
    public final int[] provides = new int[Direction.values().length];
    private final int[] lastProvides = new int[this.provides.length];


    public int[] FUEL() {
        int[] inputs = new int[]{1};
        if (hasUpgradeType(ModObjects.STORAGE.get())) inputs = new  int[]{1,7};
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
        if (hasUpgradeType(ModObjects.STORAGE.get())) inputs = new int[]{0,6};
        return inputs;
    }
    public int[] OUTPUTS(){
        int[] outputs = new int[]{2};
        if (hasUpgradeType(ModObjects.STORAGE.get())) outputs = new int[]{2,8};
        return outputs;
    }
    public int[] FSLOTS(){ return  ArrayUtils.addAll(ISLOTS(), OUTPUTS());}
    public int[] ISLOTS(){ return  ArrayUtils.addAll(INPUTS(), FUEL());}

    private final Random rand = new Random();

    public int showInventorySettings;

    public boolean showOrientation;
    protected int timer;

    public int EnergyUse() {return 500;}
    public long LiquidCapacity() {return 4 * FactoryAPIPlatform.getBucketAmount();}
    public int EnergyCapacity() {return 16000;}
    private int furnaceBurnTime;
    public int cookTime;
    public int totalCookTime = this.getCookTime();
    private int recipesUsed;
    private final Object2IntOpenHashMap<ResourceLocation> recipes = new Object2IntOpenHashMap<>();
    public boolean isForge(){ return false;}
    public RecipeType<? extends AbstractCookingRecipe> recipeType;

    public FactoryUpgradeSettings furnaceSettings;

    private final LRUCache<Item, Optional<AbstractCookingRecipe>> cache = LRUCache.newInstance(Config.cacheCapacity.get());
    protected LRUCache<Item, Optional<AbstractCookingRecipe>> blasting_cache = LRUCache.newInstance(Config.cacheCapacity.get());
    protected LRUCache<Item, Optional<AbstractCookingRecipe>> smoking_cache = LRUCache.newInstance(Config.cacheCapacity.get());


    public SmeltingBlockEntity(BlockEntityType<?> blockEntityType) {
        super(blockEntityType);
        this.recipeType = RecipeType.SMELTING;
        furnaceSettings = new FactoryUpgradeSettings(()->getUpgradeTypeSlotItem(ModObjects.FACTORY.get())) {
            @Override
            public void onChanged() {
                if (hasUpgradeType(ModObjects.FACTORY.get())) {
                    inventory.setItem(getUpgradeTypeSlot(ModObjects.FACTORY.get()), factory.get());

                }
                setChanged();
            }
        };
    }
    public SmeltingBlockEntity(){
        this(BlockEntityTypes.BETTER_FURNACE_TILE.get());
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
        return hasUpgrade(ModObjects.XP.get());
    }
    public boolean hasEnder() {
        return (hasUpgradeType(ModObjects.FUEL.get()));
    }
    public int getEnderMultiplier(){ if (hasEnder()) return ((FuelEfficiencyUpgradeItem)getUpgradeTypeSlotItem(ModObjects.FUEL.get()).getItem()).getMultiplier; else return 1;}
    public boolean isLiquid() {
        return hasUpgrade(ModObjects.LIQUID.get());
    }
    private boolean isEnergy() {
        return (hasUpgrade(ModObjects.ENERGY.get()) && energyStorage.getEnergyStored() >= EnergyUse());
    }
    public int getCookTime() {
        if (hasUpgrade(ModObjects.GENERATOR.get()) || arraySlotAllEmpty(INPUTS())) {
            return Optional.of(getDefaultCookTime()).orElse(totalCookTime);
        }
        int speed = getSpeed();
        if (speed == -1) {
            return -1;
        }

        return Math.max(1, speed);
    }
    public int getDefaultCookTime(){
        return level != null && getBlockState() != null && getBlockState().getBlock() instanceof SmeltingBlock ? ((SmeltingBlock)this.getBlockState().getBlock()).defaultCookTime.get() : 200;
    }

    protected int getSpeed() {
        int j = 0;
        int length = INPUTS().length;
        for (int i : INPUTS()) {
            ItemStack stack = inventory.getItem(i);
            int cache = getFromCache(getCache(), stack.getItem());
            int iC = cache <= 0 ? grabRecipe(stack).map(AbstractCookingRecipe::getCookingTime).orElse(-1): cache;
            if (iC <= 0){
                length -= 1;
                continue;
            }
            j += iC;
        }
        j = length <= 0 ? 0 : j / length;

        if (j < getDefaultCookTime()) {
            return (int) (j * (getDefaultCookTime() / 200F));
        } else {
            return getDefaultCookTime();
        }
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
            if (inventory.getItem(slot).getItem() instanceof UpgradeItem ){
                list.add((UpgradeItem)inventory.getItem(slot).getItem());
            }
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
            if (inventory.getItem(slot).getItem() instanceof UpgradeItem &&  ((UpgradeItem)inventory.getItem(slot).getItem()).isEnabled() && upg.isSameType(((UpgradeItem)inventory.getItem(slot).getItem()))) {
                return slot;
            }
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
    public final IPlatformFluidHandler<?> fluidTank = FactoryAPIPlatform.getFluidHandlerApi(LiquidCapacity(), this,fs -> (getBurnTime(new ItemStack(fs.getFluid().getBucket())) > 0), SlotsIdentifier.LAVA, TransportState.EXTRACT_INSERT);
    public final IPlatformFluidHandler<?> xpTank = FactoryAPIPlatform.getFluidHandlerApi(2*FactoryAPIPlatform.getBucketAmount(), this, xp -> xp.getFluid().isSame(Config.getLiquidXP()), SlotsIdentifier.GENERIC, TransportState.EXTRACT_INSERT);
    public final IPlatformEnergyStorage<?> energyStorage = FactoryAPIPlatform.getEnergyStorageApi(EnergyCapacity(),this);
    public void forceUpdateAllStates() {
        BlockState state = level.getBlockState(worldPosition);
        if (state.getValue(BlockStateProperties.LIT) != this.isBurning()) {
            level.setBlock(worldPosition, state.setValue(BlockStateProperties.LIT, this.isBurning()), 3);
        }
    }
    public int correspondentOutputSlot(int input){
        return hasUpgradeType(ModObjects.STORAGE.get())?  Arrays.stream(OUTPUTS()).filter(i-> canSmelt(irecipeSlot(input).orElse(null), input, i)).min().orElse(-1) : FOUTPUT() - FINPUT() + input;
    }
    public void trySmelt(){
        if (hasUpgradeType(ModObjects.STORAGE.get())) {
            int i = correspondentOutputSlot(FINPUT());
            if (i>=0) this.smeltItem(irecipeSlot(FINPUT()).orElse(null), FINPUT(), i);
        } else
                for (int i : INPUTS()) {
                    if(!this.canSmelt(irecipeSlot(i).orElse(null), i, correspondentOutputSlot(i))) continue;
                    this.smeltItem(irecipeSlot(i).orElse(null), i, correspondentOutputSlot(i));
                }
    }
    public int getUpdatedType(){
        return hasUpgrade(ModObjects.BLAST.get()) ? 1 : hasUpgrade(ModObjects.SMOKE.get()) ? 2 : hasUpgrade(ModObjects.GENERATOR.get()) ? 3 : 0;
    }

    @Override
    public Component getDisplayName() {
        String tier = Registry.ITEM.getKey(getBlockState().getBlock().asItem()).getPath().split("_")[0];
        return getUpdatedType() != 0 ? new TranslatableComponent("tooltip.betterfurnacesreforged." + tier +"_tier" ,getUpdatedType() == 1 ? Blocks.BLAST_FURNACE.getName().getString() : getUpdatedType() == 2 ? Blocks.SMOKER.getName().getString() : getUpdatedType() == 3 ? new TranslatableComponent("tooltip.betterfurnacesreforged.generator").getString() : "") : super.getDisplayName();
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
        if (hasUpgrade(ModObjects.GENERATOR.get())){
            ItemStack stack = getUpgradeSlotItem(ModObjects.GENERATOR.get());
            return (ItemContainerUtil.isFluidContainer(stack) && ItemContainerUtil.getFluid(stack).getAmount().longValue() > 0) && energyStorage.getSpace() > 0;
        }
        return false;
    }
    public boolean smeltValid(){
        if (!hasUpgrade(ModObjects.GENERATOR.get()))
            for (int i : INPUTS()) {
            if(!this.canSmelt(irecipeSlot(i).orElse(null), i, correspondentOutputSlot(i))) continue;
            return true;
        }
        return false;
    }

    public void tick() {

        boolean wasBurning = this.isBurning();
        boolean flag1 = false;
        boolean flag2 = false;

        if (this.isBurning()) {
            --this.furnaceBurnTime;

        }


        if (this.hasXPTank()) this.grantStoredRecipeExperience(level, null);

        if (!this.hasUpgradeType(ModObjects.FACTORY.get()) && this.showOrientation) this.showOrientation = false;

        ItemStack fuel = this.inventory.getItem(this.FUEL()[0]);
        if (this.hasUpgrade(ModObjects.COLOR.get()) != level.getBlockState(this.getBlockPos()).getValue(SmeltingBlock.COLORED)) {
            level.setBlock(this.getBlockPos(), level.getBlockState(this.getBlockPos()).setValue(SmeltingBlock.COLORED, this.hasUpgrade(ModObjects.COLOR.get())), 3);
            setChanged();
        }

        int updatedType = this.getUpdatedType();
        RecipeType<? extends AbstractCookingRecipe>[] recipeTypes = new RecipeType[]{RecipeType.SMELTING,RecipeType.BLASTING, RecipeType.SMOKING};
        if (updatedType == 3) {
            for (int i : new int[]{this.FINPUT(), this.FOUTPUT()}){
                ItemStack stack = this.inventory.getItem(i);
                if (!stack.isEmpty())Containers.dropItemStack(level, this.getBlockPos().getX(), this.getBlockPos().getY() + 1, this.getBlockPos().getZ(), stack);
            }
        }else if (this.recipeType != recipeTypes[updatedType]) this.recipeType = recipeTypes[updatedType];

        if (!this.isForge() &&   level.getBlockState(this.getBlockPos()).getValue(SmeltingBlock.TYPE) != updatedType)
            level.setBlock(this.getBlockPos(), level.getBlockState(this.getBlockPos()).setValue(SmeltingBlock.TYPE, updatedType), 3);

        if (!level.isClientSide) {
            int get_cook_time = this.getCookTime();
            this.timer++;
            if (this.hasUpgrade(ModObjects.GENERATOR.get()))
                BetterFurnacesPlatform.transferEnergySides(this);

            if (this.hasUpgradeType(ModObjects.STORAGE.get())) {
                ItemStack storageInput = this.inventory.getItem(6);
                if (!storageInput.isEmpty()){
                    int added = this.addOrSetItem(storageInput,this.inventory,this.FINPUT());
                    if (added > 0) this.inventory.extractItem(6, added,false);
                }
                ItemStack storageFuel = this.inventory.getItem(7);
                if (!storageFuel.isEmpty()){
                    int added = this.addOrSetItem(storageFuel,this.inventory,this.FUEL()[0]);
                    if (added > 0) this.inventory.extractItem(7, added,false);
                }
            }
            else if (!this.isForge()){
                for (int i : new int[]{6,7,8}){
                    ItemStack stack = this.inventory.getItem(i);
                    if (!stack.isEmpty())Containers.dropItemStack(level, this.getBlockPos().getX(), this.getBlockPos().getY() + 1, this.getBlockPos().getZ(), stack);
                }
            }


            if (this.hasUpgrade(ModObjects.ENERGY.get())) {
                if (ItemContainerUtil.isEnergyContainer(fuel) && ItemContainerUtil.getEnergy(fuel) > 0) {
                    if (this.energyStorage.getSpace() > 0) {
                        energyStorage.receiveEnergy(ItemContainerUtil.extractEnergy(energyStorage.getSpace(),fuel).contextEnergy, false);
                        this.inventory.setItem(this.FUEL()[0], fuel);

                    }
                }
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
                        this.forceUpdateAllStates();
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
                        this.forceUpdateAllStates();
                        return;
                    }
                }
                for (int i = 0; i < Direction.values().length; i++)
                    this.provides[i] = this.getBlockState().getDirectSignal(this.level, worldPosition, Direction.values()[i]);

            } else {
                for (int i = 0; i < Direction.values().length; i++)
                    this.provides[i] = 0;
            }
            if (this.doesNeedUpdateSend()) {
                this.onUpdateSent();
            }

            if (this.isLiquid() && ItemContainerUtil.isFluidContainer(fuel) && isItemFuel(fuel)) {
                ItemContainerUtil.ItemFluidContext context = ItemContainerUtil.drainItem(this.fluidTank.getTotalSpace(),fuel);
                    long amount = this.fluidTank.fill(context.fluidStack, false);
                    if (amount > 0) {
                        level.playSound(null, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), SoundEvents.BUCKET_FILL_LAVA, SoundSource.PLAYERS, 0.6F, 0.8F);
                        this.inventory.setItem(this.FUEL()[0], context.container);
                    }


            }
            if ((this.isBurning() || !fuel.isEmpty() || this.isLiquid() || (this.isEnergy() && !this.canGeneratorWork())) &&  (this.arraySlotFilled(this.INPUTS(), true)|| this.canGeneratorWork()))  {
                boolean valid = this.smeltValid() || this.canGeneratorWork();
                if (!this.isBurning() && (valid)) {
                    if (this.isLiquid() && !this.fluidTank.getFluidStack().isEmpty() && getFluidBurnTime(this.fluidTank.getFluidStack()) > 0){
                        int fluidAmount = (int) ((200 * FactoryAPIPlatform.getBucketAmount()) / getFluidBurnTime(this.fluidTank.getFluidStack()));
                        if (this.fluidTank.getFluidStack().getAmount().longValue() >= fluidAmount) {
                            this.furnaceBurnTime = this.getEnderMultiplier() * get_cook_time;
                            this.recipesUsed = this.furnaceBurnTime;
                            this.fluidTank.drain(fluidAmount, false);
                        }
                    }else if (this.isEnergy()){
                        this.furnaceBurnTime = this.getEnderMultiplier() * get_cook_time;
                        this.recipesUsed = this.furnaceBurnTime;
                        for (int a : this.INPUTS())
                            this.energyStorage.consumeEnergy(this.EnergyUse() * this.OreProcessingMultiplier(this.inventory.getItem(a)), false);
                    }else{
                        this.furnaceBurnTime = (int) Math.ceil(this.getEnderMultiplier() * getBurnTime(fuel) * (get_cook_time / 200D));
                        this.recipesUsed = this.furnaceBurnTime;
                    }
                    if (this.isBurning()) {
                        flag1 = true;
                        if (this.hasEnder()) {
                            if (this.hasUpgrade(ModObjects.FUEL.get())) {
                                this.breakDurabilityItem(this.getUpgradeSlotItem(ModObjects.FUEL.get()));
                            }
                        }
                        if ((!this.isLiquid() || this.fluidTank.getFluidStack().getAmount().longValue() < 10) && !this.isEnergy()) {
                            if (ItemContainerUtil.isFluidContainer(fuel)){
                                ItemContainerUtil.ItemFluidContext context = ItemContainerUtil.drainItem(this.fluidTank.getTotalSpace(),fuel);
                                this.inventory.setItem(this.FUEL()[0],context.container);
                            }

                            if (!fuel.isEmpty() && isItemFuel(fuel)) {
                                fuel.shrink(1);
                                if (this.hasUpgrade(ModObjects.FUEL.get())) {
                                    this.breakDurabilityItem(this.getUpgradeSlotItem(ModObjects.FUEL.get()));
                                }
                            }
                        }
                    }
                }
                if (this.isBurning() && valid ) {
                    ++this.cookTime;
                    if (this.hasUpgrade(ModObjects.GENERATOR.get())){
                        ItemContainerUtil.ItemFluidContext context = ItemContainerUtil.drainItem(FactoryAPIPlatform.getBucketAmount() / 1000, this.getUpgradeSlotItem(ModObjects.GENERATOR.get()));
                        if (!context.fluidStack.isEmpty()) this.inventory.setItem(this.getUpgradeTypeSlot(ModObjects.GENERATOR.get()), context.container);
                        this.energyStorage.receiveEnergy(Math.round((float)500 / get_cook_time),false);
                    }
                    if (this.cookTime >= this.totalCookTime) {
                        this.cookTime = 0;
                        this.totalCookTime = this.getCookTime();
                        if (!this.hasUpgrade(ModObjects.GENERATOR.get())) {
                            this.trySmelt();
                            if (this.hasUpgradeType(ModObjects.FACTORY.get()))
                                BetterFurnacesPlatform.smeltingAutoIO(this);
                        }
                        flag1 = true;
                    }
                } else {
                    this.cookTime = 0;
                }
            } else if (!this.isBurning() && this.cookTime > 0) {
                this.cookTime = Mth.clamp(this.cookTime - 2, 0, this.totalCookTime);
            }
            if (wasBurning != this.isBurning()) {
                flag1 = true;
                this.level.setBlock(this.worldPosition, this.level.getBlockState(this.worldPosition).setValue(BlockStateProperties.LIT, this.isBurning()), 3);
            }
            if ((this.timer % 24 == 0) && (this.hasUpgradeType(ModObjects.FACTORY.get()))){
                if (this.cookTime <= 0) {
                    if (this.arraySlotFilled(this.INPUTS(), false)) {
                        BetterFurnacesPlatform.smeltingAutoIO(this);
                        flag1 = true;
                    } else if (this.hasArraySlotSpace(this.INPUTS())) {
                        BetterFurnacesPlatform.smeltingAutoIO(this);
                        flag1 = true;
                    }
                    if (this.arraySlotFilled(this.OUTPUTS(), true)) {
                        BetterFurnacesPlatform.smeltingAutoIO(this);
                        flag1 = true;
                    }
                    if (this.inventory.getItem(this.FUEL()[0]).isEmpty() && !this.isLiquid() && !this.isEnergy()) {
                        BetterFurnacesPlatform.smeltingAutoIO(this);
                        flag1 = true;
                    } else if (this.inventory.getItem(this.FUEL()[0]).getCount() < this.inventory.getItem(this.FUEL()[0]).getMaxStackSize() || ItemContainerUtil.isFluidContainer(fuel) && ItemContainerUtil.getFluid(fuel).getAmount().longValue() < this.fluidTank.getTotalSpace()){
                        BetterFurnacesPlatform.smeltingAutoIO(this);
                        flag1 = true;
                    }
                }
            }
        }

        if (flag1) {
            this.setChanged();
        }

    }
    public Color getColor() {
        if (getUpgradeSlotItem(ModObjects.COLOR.get()).isEmpty()) return Color.WHITE;
        CompoundTag nbt = getUpgradeSlotItem(ModObjects.COLOR.get()).getTag();
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
    public BlockSide[] getSidesOrder(){
        return BlockSide.values();
    }

    public int getIndexBottom() {return BlockSide.BOTTOM.blockStateToFacing(getBlockState(),getSidesOrder()).ordinal();}
    public int getIndexTop() {return BlockSide.TOP.blockStateToFacing(getBlockState(),getSidesOrder()).ordinal();}

    public int getIndexFront() {
        return BlockSide.FRONT.blockStateToFacing(getBlockState(),getSidesOrder()).ordinal();
    }

    public int getIndexBack() {
        return BlockSide.BACK.blockStateToFacing(getBlockState(),getSidesOrder()).ordinal();
    }

    public int getIndexLeft() {
        return BlockSide.LEFT.blockStateToFacing(getBlockState(),getSidesOrder()).ordinal();
    }

    public int getIndexRight() {
        return BlockSide.RIGHT.blockStateToFacing(getBlockState(),getSidesOrder()).ordinal();
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

    Tag<Item> ore = getItemTag(new ResourceLocation(Platform.isForge() ? "forge" : "c", "ores"));


    private Tag<Item> getItemTag(ResourceLocation resourceLocation) {
        return ItemTags.getAllTags().getTag(resourceLocation);
    }

    protected boolean isOre(ItemStack input){
        if (Config.checkOresName.get()) return Registry.ITEM.getKey(input.getItem()).getPath().endsWith("_ore");
        return (ore != null && input.getItem().is(ore));
    }
    protected int OreProcessingMultiplier(ItemStack input){
        if (hasUpgradeType(ModObjects.ORE_PROCESSING.get())){
            OreProcessingUpgradeItem oreup = (OreProcessingUpgradeItem)getUpgradeTypeSlotItem(ModObjects.ORE_PROCESSING.get()).getItem();
            if  (isOre( input)) return oreup.getMultiplier;

        } else if (input == ItemStack.EMPTY) return 0;
        return 1;
    }

    @Override
    public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
        return new SmeltingMenu(ModObjects.FURNACE_CONTAINER.get(),i,level,getBlockPos(),playerInventory,playerEntity,fields);
    }

    protected boolean canSmelt(@Nullable Recipe<?> recipe, int INPUT, int OUTPUT) {
        ItemStack input = this.getInv().getItem(INPUT);
        if (OUTPUT >= 0 && !input.isEmpty() && recipe != null) {
            ItemStack recipeOutput = recipe.getResultItem();
            if (!recipeOutput.isEmpty()) {
                ItemStack output = this.getInv().getItem(OUTPUT);
                if (output.isEmpty()) return true;
                else if (!output.sameItem(recipeOutput)) return false;
                else {
                    return output.getCount() + recipeOutput.getCount() * OreProcessingMultiplier(input) <= output.getMaxStackSize();
                }
            }
        }
        return false;
    }
    private ItemStack getResult(Recipe<?> recipe, ItemStack input) {
        ItemStack out = recipe.getResultItem().copy();
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
            if (slotStack.sameItem(stack) && slotStack.getCount() < maxStack) {
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

            if (addOrSetItem(getResult(recipe,input),inventory,OUTPUT) > 0 && hasUpgrade(ModObjects.ORE_PROCESSING.get()) && (isOre(input))) {
                breakDurabilityItem(getUpgradeSlotItem(ModObjects.ORE_PROCESSING.get()));
            }

            this.checkXP(recipe);
            if (!this.level.isClientSide) {
                this.setRecipeUsed(recipe);
            }
            ItemStack fuel = inventory.getItem(FUEL()[0]);
            if (input.getItem() == Blocks.WET_SPONGE.asItem() && !fuel.isEmpty()) {
                if (ItemContainerUtil.isFluidContainer(fuel)){
                    ItemContainerUtil.fillItem(fuel,FluidStack.create(Fluids.WATER, Fraction.ofWhole(FactoryAPIPlatform.getBucketAmount())));
                    inventory.setItem(FUEL()[0],fuel);
                }
            }

            if (Platform.isModLoaded("pmmo"))
                ProjectMMO.burnEvent(input,  level, worldPosition, getRecipe(input, RecipeType.SMOKING).isPresent() ? 1:0);

            input.shrink(1);
        }
    }
    @Override
    public void load(BlockState blockState, CompoundTag tag) {
        super.load(blockState, tag);
        this.furnaceBurnTime = tag.getInt("BurnTime");
        this.cookTime = tag.getInt("CookTime");
        this.totalCookTime = tag.getInt("CookTimeTotal");
        this.timer = 0;
        this.recipesUsed = getBurnTime(this.getInv().getItem(1));
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
    public CompoundTag save(CompoundTag tag) {
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
        return super.save(tag);
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



    public <T extends IPlatformHandlerApi<?>> ArbitrarySupplier<T> getStorage(Storages.Storage<T> storage, Direction facing){
        if (storage == Storages.FLUID){
            if (facing == null || (!hasUpgrade(ModObjects.GENERATOR.get()) && !hasXPTank()) || facing.ordinal() == getIndexTop() || facing.ordinal() == getIndexBottom()) {
                if(isLiquid())
                    return ()-> (T) fluidTank;
            } else {
                if (hasUpgrade(ModObjects.GENERATOR.get())) {
                    ItemStack gen = getUpgradeSlotItem(ModObjects.GENERATOR.get());
                    return ()-> (T)(gen.getItem() instanceof GeneratorUpgradeItem ? ((GeneratorUpgradeItem) gen.getItem()).getFluidStorage(gen) : null);
                } else if (hasXPTank())
                    return ()-> (T) xpTank;
            }
        }
        if (storage == Storages.ITEM){
            if (facing != null)
                return ()-> (T)FactoryAPIPlatform.filteredOf(inventory,facing, getSlotsTransport(facing).getFirst(), getSlotsTransport(facing).getSecond());
            else return ()-> (T)inventory;
        }
        if (storage == Storages.ENERGY && (hasUpgrade(ModObjects.ENERGY.get()) ||  hasUpgrade(ModObjects.GENERATOR.get()))){
            return ()-> (T)FactoryAPIPlatform.filteredOf(energyStorage, TransportState.ofBoolean( true, !hasUpgrade(ModObjects.GENERATOR.get())));
        }
        return ArbitrarySupplier.empty();
    }

    @Override
    public Pair<int[], TransportState> getSlotsTransport(Direction side) {
        if (hasUpgradeType(ModObjects.FACTORY.get())) {
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
        if (hasUpgradeType(ModObjects.FACTORY.get())) {
            return !ArrayUtils.contains(INPUTS(), index) && !ArrayUtils.contains(UPGRADES(), index) && (!ArrayUtils.contains(FUEL(), index) || (!isItemFuel(stack) && (!ItemContainerUtil.isEnergyContainer(stack) || ItemContainerUtil.getEnergy(stack) <= 0)));
        }else{
            return index >= FOUTPUT() && index <= LOUTPUT();
        }
    }

    @Override
    public boolean IisItemValidForSlot(int index, ItemStack stack) {
        return getSlots(null).get(index).mayPlace(stack);
    }

    @Override
    public void addSlots(NonNullList<Slot> slots, @Nullable Player player) {
        slots.add(new SlotInput(this, 0, 54, 18, (s) -> !this.hasUpgrade(ModObjects.GENERATOR.get())));
        slots.add(new SlotFuel(this, 1, 54, 54));
        slots.add(new SlotOutput(player, this, 2, 116, 35, (s) -> !this.hasUpgrade(ModObjects.GENERATOR.get())));

        slots.add(new SlotUpgrade(this, 3, 8, 18));
        slots.add(new SlotUpgrade(this, 4, 8, 36));
        slots.add(new SlotUpgrade(this, 5, 8, 54));

        slots.add(new SlotInput(this, 6, 36, 18, s -> hasUpgradeType(ModObjects.STORAGE.get())));
        slots.add(new SlotFuel(this, 7, 36, 54, s-> hasUpgradeType(ModObjects.STORAGE.get())));
        slots.add(new SlotOutput(player, this, 8, 138, 35, s -> hasUpgradeType(ModObjects.STORAGE.get())));

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

        List<Recipe<?>> list = this.grantStoredRecipeExperience(player.level, player.position());
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
                          xpTank.fill(FluidStack.create(Config.getLiquidXP(), Fraction.ofWhole(amountLiquidXp * FactoryAPIPlatform.getBucketAmount() /1000)), false);
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