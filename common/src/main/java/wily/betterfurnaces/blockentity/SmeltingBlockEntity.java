package wily.betterfurnaces.blockentity;

import com.google.common.collect.Lists;
import dev.architectury.fluid.FluidStack;
import dev.architectury.platform.Platform;
import dev.architectury.registry.fuel.FuelRegistry;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
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
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.Nullable;
import wily.betterfurnaces.Config;
import wily.betterfurnaces.ProjectMMO;
import wily.betterfurnaces.blocks.SmeltingBlock;
import wily.betterfurnaces.init.BlockEntityTypes;
import wily.betterfurnaces.init.ModObjects;
import wily.betterfurnaces.inventory.*;
import wily.betterfurnaces.items.*;
import wily.factoryapi.FactoryAPIPlatform;
import wily.factoryapi.ItemContainerUtil;
import wily.factoryapi.base.*;

import java.awt.*;
import java.util.*;
import java.util.List;

public class SmeltingBlockEntity extends InventoryBlockEntity implements RecipeCraftingHolder, StackedContentsCompatible{
    public final int[] provides = new int[Direction.values().length];
    private final int[] lastProvides = new int[this.provides.length];


    public int[] FUEL() {
        int[] inputs = new int[]{1};
        if (hasUpgradeType(ModObjects.STORAGE.get())) inputs = new int[]{1,7};
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
    private int recipesUsed;
    private final Object2IntOpenHashMap<ResourceLocation> recipes = new Object2IntOpenHashMap<>();
    public boolean isForge(){ return this instanceof ForgeBlockEntity;}
    public RecipeType<? extends AbstractCookingRecipe> recipeType;

    public FactoryUpgradeSettings furnaceSettings;

    private final LRUCache<Item, Optional<RecipeHolder<AbstractCookingRecipe>>> cache = LRUCache.newInstance(Config.cacheCapacity.get());
    protected LRUCache<Item, Optional<RecipeHolder<AbstractCookingRecipe>>> blasting_cache = LRUCache.newInstance(Config.cacheCapacity.get());
    protected LRUCache<Item, Optional<RecipeHolder<AbstractCookingRecipe>>> smoking_cache = LRUCache.newInstance(Config.cacheCapacity.get());

    public SmeltingBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state) {
        super(blockEntityType, pos, state);
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
    public SmeltingBlockEntity(BlockPos pos, BlockState state){
        this(BlockEntityTypes.BETTER_FURNACE_TILE.get(),pos,state);
    }

    private int getFromCache(LRUCache<Item, Optional<RecipeHolder<AbstractCookingRecipe>>> c, Item key) {
        if (c == null) return 0;
        if (c.get(key) == null)
        {
            return 0;
        }
        return c.get(key).orElse(null) == null ? 0 : c.get(key).orElse(null).value().getCookingTime();
    }

    public boolean hasRecipe(ItemStack stack) {
        return grabRecipe(stack).isPresent();
    }

    protected LRUCache<Item, Optional<RecipeHolder<AbstractCookingRecipe>>> getCache() {
        LRUCache<Item, Optional<RecipeHolder<AbstractCookingRecipe>>>[] caches = new LRUCache[]{cache,blasting_cache,smoking_cache};
        return caches[getUpdatedType() >=3 ? 0 : getUpdatedType()];
    }


    private Optional<RecipeHolder<AbstractCookingRecipe>> getRecipe(ItemStack stack, RecipeType recipeType) {
        return this.level.getRecipeManager().getRecipeFor((RecipeType<AbstractCookingRecipe>) recipeType, new SimpleContainer(stack), this.level);
    }

    private Optional<RecipeHolder<AbstractCookingRecipe>> grabRecipe(ItemStack stack) {
        Item item = stack.getItem();
        if (item instanceof AirItem)
        {
            return Optional.empty();
        }
        Optional<RecipeHolder<AbstractCookingRecipe>> recipe = getCache().get(item);
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
        if (hasUpgrade(ModObjects.GENERATOR.get()) || arraySlotAllEmpty(INPUTS()))
            return getDefaultCookTime();
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
            ItemStack stack = inventory.getItem(i);
            int cache = getFromCache(getCache(), stack.getItem());
            int iC = cache <= 0 ? grabRecipe(stack).map(v-> v.value().getCookingTime()).orElse(-1): cache;
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
            return switch (index) {
                case 0 -> SmeltingBlockEntity.this.furnaceBurnTime;
                case 1 -> SmeltingBlockEntity.this.recipesUsed;
                case 2 -> SmeltingBlockEntity.this.cookTime;
                case 3 -> SmeltingBlockEntity.this.totalCookTime;
                case 4 -> SmeltingBlockEntity.this.showInventorySettings;
                default -> 0;
            };
        }

        public void set(int index, int value) {
            switch (index) {
                case 0 -> SmeltingBlockEntity.this.furnaceBurnTime = value;
                case 1 -> SmeltingBlockEntity.this.recipesUsed = value;
                case 2 -> SmeltingBlockEntity.this.cookTime = value;
                case 3 -> SmeltingBlockEntity.this.totalCookTime = value;
                case 4 -> SmeltingBlockEntity.this.showInventorySettings = value;
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

    public int getDefaultCookTime(){
        return level != null && getBlockState().getBlock() instanceof SmeltingBlock ? ((SmeltingBlock)this.getBlockState().getBlock()).defaultCookTime.get() : 200;
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
    public static final SlotsIdentifier XP = new SlotsIdentifier(ChatFormatting.GREEN,"green");
    public final IPlatformFluidHandler fluidTank = new FactoryFluidHandler(LiquidCapacity(), this,fs -> LiquidFuelUpgradeItem.supportsFluid(fs.getFluid()), SlotsIdentifier.LAVA, TransportState.EXTRACT_INSERT);
    public final IPlatformFluidHandler xpTank = new FactoryFluidHandler(2*FluidStack.bucketAmount(), this, xp -> xp.getFluid().isSame(Config.getLiquidXP()), XP, TransportState.EXTRACT_INSERT);
    public final IPlatformEnergyStorage energyStorage = new FactoryEnergyStorage(EnergyCapacity(),this);
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
        String tier = getBlockState().getBlock().arch$registryName().getPath().split("_")[0];
        return getUpdatedType() != 0 ? Component.translatable("tooltip.betterfurnacesreforged." + tier +"_tier" ,getUpdatedType() == 1 ? Blocks.BLAST_FURNACE.getName().getString() : getUpdatedType() == 2 ? Blocks.SMOKER.getName().getString() : getUpdatedType() == 3 ? Component.translatable("tooltip.betterfurnacesreforged.generator").getString() : "") : super.getDisplayName();
    }


    public Optional<RecipeHolder<AbstractCookingRecipe>> irecipeSlot(int input){
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
            return (ItemContainerUtil.isFluidContainer(stack) && ItemContainerUtil.getFluid(stack).getAmount() > 0) && energyStorage.getEnergySpace() > 0;
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
    public static void tick(Level level, BlockPos worldPosition, BlockState blockState, SmeltingBlockEntity be) {

        boolean wasBurning = be.isBurning();
        boolean flag1 = false;
        boolean flag2 = false;

        if (be.isBurning()) {
            --be.furnaceBurnTime;
        }


        if (be.hasXPTank()) be.grantStoredRecipeExperience(level, null);

        if (!be.hasUpgradeType(ModObjects.FACTORY.get()) && be.showOrientation) be.showOrientation = false;

        ItemStack fuel = be.inventory.getItem(be.FUEL()[0]);
        if ((be.hasUpgrade(ModObjects.COLOR.get()))) {
            if (!(level.getBlockState(be.getBlockPos()).getValue(SmeltingBlock.COLORED)))
                level.setBlock(be.getBlockPos(), level.getBlockState(be.getBlockPos()).setValue(SmeltingBlock.COLORED, true), 3);
        } else if ((level.getBlockState(be.getBlockPos()).getValue(SmeltingBlock.COLORED))) level.setBlock(be.getBlockPos(), level.getBlockState(be.getBlockPos()).setValue(SmeltingBlock.COLORED, false), 3);

        int updatedType = be.getUpdatedType();
        RecipeType<? extends AbstractCookingRecipe>[] recipeTypes = new RecipeType[]{RecipeType.SMELTING,RecipeType.BLASTING, RecipeType.SMOKING};
        if (updatedType == 3) {
            for (int i : new int[]{be.FINPUT(), be.FOUTPUT()}){
                ItemStack stack = be.inventory.getItem(i);
                if (!stack.isEmpty())Containers.dropItemStack(level, be.getBlockPos().getX(), be.getBlockPos().getY() + 1, be.getBlockPos().getZ(), stack);
            }
        }else if (be.recipeType != recipeTypes[updatedType]) be.recipeType = recipeTypes[updatedType];

        if (!be.isForge() &&   level.getBlockState(be.getBlockPos()).getValue(SmeltingBlock.TYPE) != updatedType)
            level.setBlock(be.getBlockPos(), level.getBlockState(be.getBlockPos()).setValue(SmeltingBlock.TYPE, updatedType), 3);

        if (!level.isClientSide) {
            int get_cook_time = be.getCookTime();
            be.timer++;
            if (be.hasUpgrade(ModObjects.GENERATOR.get())) {
                for (Direction d : Direction.values()) {
                    BlockEntity other = be.getLevel().getBlockEntity(be.getBlockPos().relative(d));
                    if (other == null) continue;
                    IPlatformEnergyStorage energyStorage = FactoryAPIPlatform.getPlatformFactoryStorage(be).getStorage(Storages.ENERGY,d.getOpposite()).get();
                    if (energyStorage == null) continue;
                    be.energyStorage.consumeEnergy(energyStorage.receiveEnergy(Math.min(energyStorage.getMaxReceive(), be.energyStorage.getMaxConsume()),false),false);
                }
            }
            if (be.hasUpgradeType(ModObjects.STORAGE.get())) {
                ItemStack storageInput = be.inventory.getItem(6);
                if (!storageInput.isEmpty()) be.inventory.setItem(6, be.inventory.insertItem(be.FINPUT(),storageInput,false));
                ItemStack storageFuel = be.inventory.getItem(7);
                if (!storageFuel.isEmpty()) be.inventory.setItem(7, be.inventory.insertItem(be.FUEL()[0],storageFuel,false));
            }
            else if (!be.isForge()){
                for (int i : new int[]{6,7,8}){
                    ItemStack stack = be.inventory.getItem(i);
                    if (!stack.isEmpty())Containers.dropItemStack(level, be.getBlockPos().getX(), be.getBlockPos().getY() + 1, be.getBlockPos().getZ(), stack);
                }
            }


            if (be.hasUpgrade(ModObjects.ENERGY.get())) {
                if (ItemContainerUtil.isEnergyContainer(fuel) && ItemContainerUtil.getEnergy(fuel) > 0) {
                    if (be.energyStorage.getEnergySpace() > 0) {
                        be.energyStorage.receiveEnergy(ItemContainerUtil.extractEnergy(be.energyStorage.getEnergySpace(),fuel).contextEnergy(), false);
                        be.inventory.setItem(be.FUEL()[0], fuel);
                    }
                }
            }
            if (be.totalCookTime != get_cook_time) {
                be.totalCookTime = get_cook_time;
            }
            int mode = be.getRedstoneSetting();
            if (mode != 0) {
                if (mode == 2) {
                    int i = 0;
                    for (Direction side : Direction.values()) {
                        if (level.getSignal(worldPosition.offset(side.getNormal()), side) > 0) {
                            i++;
                        }
                    }
                    if (i != 0) {
                        be.cookTime = 0;
                        be.furnaceBurnTime = 0;
                        be.forceUpdateAllStates();
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
                        be.cookTime = 0;
                        be.furnaceBurnTime = 0;
                        be.forceUpdateAllStates();
                        return;
                    }
                }
                for (int i = 0; i < Direction.values().length; i++)
                    be.provides[i] = be.getBlockState().getDirectSignal(be.level, worldPosition, Direction.values()[i]);

            } else {
                for (int i = 0; i < Direction.values().length; i++)
                    be.provides[i] = 0;
            }
            if (be.doesNeedUpdateSend()) {
                be.onUpdateSent();
            }

            if (be.isLiquid() && LiquidFuelUpgradeItem.supportsItemFluidHandler(fuel)) {
                ItemContainerUtil.ItemFluidContext context = ItemContainerUtil.drainItem(be.fluidTank.getTotalSpace(),fuel);
                long amount = be.fluidTank.fill(context.fluidStack(), false);
                if (amount > 0) {
                    level.playSound(null, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), SoundEvents.BUCKET_FILL_LAVA, SoundSource.PLAYERS, 0.6F, 0.8F);
                    be.inventory.setItem(be.FUEL()[0], context.container());
                }
            }

            if ((be.isBurning() || !fuel.isEmpty() || be.isLiquid() || (be.isEnergy() && !be.canGeneratorWork())) &&  (be.arraySlotFilled(be.INPUTS(), true)|| be.canGeneratorWork()))  {
                boolean valid = be.smeltValid() || be.canGeneratorWork();
                if (!be.isBurning() && (valid)) {
                    if (be.isLiquid() && !be.fluidTank.getFluidStack().isEmpty() && getFluidBurnTime(be.fluidTank.getFluidStack()) > 0){
                        int fluidAmount= (200 * (int)FluidStack.bucketAmount()) / getFluidBurnTime(be.fluidTank.getFluidStack());
                        if (be.fluidTank.getFluidStack().getAmount() >= fluidAmount) {
                            be.furnaceBurnTime = be.getEnderMultiplier() * get_cook_time;
                            be.recipesUsed = be.furnaceBurnTime;
                            be.fluidTank.drain(fluidAmount, false);
                        }
                    }else if (be.isEnergy()){
                        be.furnaceBurnTime = be.getEnderMultiplier() * get_cook_time;
                        be.recipesUsed = be.furnaceBurnTime;
                        for (int a : be.INPUTS())
                            be.energyStorage.consumeEnergy(be.EnergyUse() * be.OreProcessingMultiplier(be.inventory.getItem(a)), false);
                    }else{
                        be.furnaceBurnTime = (int) Math.ceil(be.getEnderMultiplier() * getBurnTime(fuel) * (get_cook_time / 200D));
                        be.recipesUsed = be.furnaceBurnTime;
                    }
                    if (be.isBurning()) {
                        flag1 = true;
                        if (be.hasEnder()) {
                            if (be.hasUpgrade(ModObjects.FUEL.get())) {
                                be.breakDurabilityItem(be.getUpgradeSlotItem(ModObjects.FUEL.get()));
                            }
                        }
                        if ((!be.isLiquid() || be.fluidTank.getFluidStack().getAmount() < 10) && !be.isEnergy()) {
                            if (ItemContainerUtil.isFluidContainer(fuel)){
                                ItemContainerUtil.ItemFluidContext context = ItemContainerUtil.drainItem(be.fluidTank.getTotalSpace(),fuel);
                                be.inventory.setItem(be.FUEL()[0],context.container());
                            }

                            if (!fuel.isEmpty() && isItemFuel(fuel)) {
                                fuel.shrink(1);
                                if (be.hasUpgrade(ModObjects.FUEL.get())) {
                                    be.breakDurabilityItem(be.getUpgradeSlotItem(ModObjects.FUEL.get()));
                                }
                            }
                        }
                    }
                }
                if (be.isBurning() && valid ) {
                    ++be.cookTime;
                    if (be.hasUpgrade(ModObjects.GENERATOR.get())){
                        ItemContainerUtil.ItemFluidContext context = ItemContainerUtil.drainItem(FluidStack.bucketAmount() / 1000, be.getUpgradeSlotItem(ModObjects.GENERATOR.get()));
                        if (!context.fluidStack().isEmpty()) be.inventory.setItem(be.getUpgradeTypeSlot(ModObjects.GENERATOR.get()), context.container());
                        be.energyStorage.receiveEnergy(Math.round((float)500 / get_cook_time),false);
                    }
                    if (be.cookTime >= be.totalCookTime) {
                        be.cookTime = 0;
                        be.totalCookTime = be.getCookTime();
                        if (!be.hasUpgrade(ModObjects.GENERATOR.get())) {
                            be.trySmelt();
                            if (be.hasUpgradeType(ModObjects.FACTORY.get()))
                                be.handleAutoIO();
                        }
                        flag1 = true;
                    }
                } else {
                    be.cookTime = 0;
                }
            } else if (!be.isBurning() && be.cookTime > 0) {
                be.cookTime = Mth.clamp(be.cookTime - 2, 0, be.totalCookTime);
            }
            if (wasBurning != be.isBurning()) {
                flag1 = true;
                be.level.setBlock(be.worldPosition, be.level.getBlockState(be.worldPosition).setValue(BlockStateProperties.LIT, be.isBurning()), 3);
            }
            if ((be.timer % 24 == 0) && (be.hasUpgradeType(ModObjects.FACTORY.get()))){
                if (be.cookTime <= 0) {
                    if (be.arraySlotFilled(be.INPUTS(), false)) {
                        be.handleAutoIO();
                        flag1 = true;
                    } else if (be.hasArraySlotSpace(be.INPUTS())) {
                        be.handleAutoIO();
                        flag1 = true;
                    }
                    if (be.arraySlotFilled(be.OUTPUTS(), true)) {
                        be.handleAutoIO();
                        flag1 = true;
                    }
                    if (be.inventory.getItem(be.FUEL()[0]).isEmpty() && !be.isLiquid() && !be.isEnergy()) {
                        be.handleAutoIO();
                        flag1 = true;
                    } else if (be.inventory.getItem(be.FUEL()[0]).getCount() < be.inventory.getItem(be.FUEL()[0]).getMaxStackSize() || ItemContainerUtil.isFluidContainer(fuel) && ItemContainerUtil.getFluid(fuel).getAmount() < be.fluidTank.getTotalSpace()){
                        be.handleAutoIO();
                        flag1 = true;
                    }
                }
            }
        }

        if (flag1) {
            be.setChanged();
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

    protected BlockSide[] getSidesOrder(){return BlockSide.values();}
    public int getIndexBottom() {return BlockSide.BOTTOM.blockStateToFacing(getBlockState(),getSidesOrder()).ordinal();}
    public int getIndexTop() {
        return BlockSide.TOP.blockStateToFacing(getBlockState(),getSidesOrder()).ordinal();
    }
    public int getIndexFront() {return BlockSide.FRONT.blockStateToFacing(getBlockState(),getSidesOrder()).ordinal();}
    public int getIndexBack() {return BlockSide.BACK.blockStateToFacing(getBlockState(),getSidesOrder()).ordinal();}
    public int getIndexLeft() {return BlockSide.LEFT.blockStateToFacing(getBlockState(),getSidesOrder()).ordinal();}
    public int getIndexRight() {return BlockSide.RIGHT.blockStateToFacing(getBlockState(),getSidesOrder()).ordinal();}

    public int getAutoInput() {return this.furnaceSettings.get(6);}
    public int getAutoOutput() {return this.furnaceSettings.get(7);}
    public int getRedstoneSetting() {return this.furnaceSettings.get(8);}
    public int getRedstoneComSub() {return this.furnaceSettings.get(9);}



    public boolean isBurning() {
        return this.furnaceBurnTime > 0;
    }

    TagKey<Item> ore = getItemTag(new ResourceLocation(Platform.isForgeLike() ? "forge" : "c", "ores"));



    private TagKey<Item> getItemTag(ResourceLocation resourceLocation) {
        return TagKey.create(Registries.ITEM,resourceLocation);
    }
    private boolean hasRawOreTag(ItemStack stack){
        if (Config.checkRawOresName.get()) return stack.getItem().arch$registryName().getPath().startsWith("raw_");
        if(Platform.isForgeLike()) return stack.is(getItemTag( new ResourceLocation("forge","raw_materials")));
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
        if (hasUpgradeType(ModObjects.ORE_PROCESSING.get())){
            OreProcessingUpgradeItem oreup = (OreProcessingUpgradeItem)getUpgradeTypeSlotItem(ModObjects.ORE_PROCESSING.get()).getItem();
            if  ((isRaw(input) && oreup.acceptRaw) || (isOre( input) && oreup.acceptOre)) return oreup.getMultiplier;

        } else if (input.isEmpty()) return 0;
        return 1;
    }

    @Override
    public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
        return new SmeltingMenu(ModObjects.FURNACE_CONTAINER.get(),i,level,getBlockPos(),playerInventory,playerEntity,fields);
    }

    protected boolean canSmelt(@Nullable RecipeHolder<?> recipe, int INPUT, int OUTPUT) {
        ItemStack input = this.getInv().getItem(INPUT);
        if (OUTPUT >= 0 && !input.isEmpty() && recipe != null) {
            ItemStack recipeOutput = recipe.value().getResultItem(RegistryAccess.EMPTY);
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
    protected void smeltItem(@Nullable RecipeHolder<?> recipe, int INPUT, int OUTPUT) {
        timer = 0;
        if (recipe != null && this.canSmelt(recipe, INPUT, OUTPUT)) {
            ItemStack input = this.getInv().getItem(INPUT);

            if (addOrSetItem(getResult(recipe.value(),input),inventory,OUTPUT) > 0 && hasUpgrade(ModObjects.ORE_PROCESSING.get()) && (isOre(input))) {
                breakDurabilityItem(getUpgradeSlotItem(ModObjects.ORE_PROCESSING.get()));
            }

            this.checkXP(recipe.value());
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



    public <T extends IPlatformHandler> ArbitrarySupplier<T> getStorage(Storages.Storage<T> storage, Direction facing){
        if (storage == Storages.FLUID){
            if ((facing == null || (!hasUpgrade(ModObjects.GENERATOR.get()) && !hasXPTank()) || (facing.ordinal() == getIndexTop() || facing.ordinal() == getIndexBottom()))) {
                if(isLiquid()) return ()-> (T)(facing == null ?  fluidTank : FactoryAPIPlatform.filteredOf(fluidTank,Direction.NORTH,TransportState.INSERT));
            } else {
                if (hasUpgrade(ModObjects.GENERATOR.get())) {
                    ItemStack gen = getUpgradeSlotItem(ModObjects.GENERATOR.get());
                    return ()-> (T)(gen.getItem() instanceof GeneratorUpgradeItem item ? (facing == null ? item.getFluidStorage(gen) : FactoryAPIPlatform.filteredOf(item.getFluidStorage(gen),Direction.NORTH,TransportState.INSERT)) : null);
                } else if (hasXPTank())
                    return ()-> (T)(facing == null ?  xpTank : FactoryAPIPlatform.filteredOf(xpTank,Direction.NORTH,TransportState.EXTRACT));
            }
        }
        if (storage == Storages.ITEM){
            return ()-> (T) (facing == null ? inventory : FactoryAPIPlatform.filteredOf(inventory,facing, getSlotsTransport(facing).key(), getSlotsTransport(facing).value()));
        }
        if (storage == Storages.ENERGY && (hasUpgrade(ModObjects.ENERGY.get()) ||  hasUpgrade(ModObjects.GENERATOR.get()))){
            return ()-> (T)(facing == null ? energyStorage : FactoryAPIPlatform.filteredOf(energyStorage, Direction.NORTH, TransportState.ofBoolean( true, !hasUpgrade(ModObjects.GENERATOR.get()))));
        }
        return ArbitrarySupplier.empty();
    }

    @Override
    public Pair<int[], TransportState> getSlotsTransport(Direction side) {
        if (hasUpgradeType(ModObjects.FACTORY.get())) {
            if (this.furnaceSettings.get(side.ordinal()) == 1) {
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
            return ArrayUtils.contains(OUTPUTS(),index);
        }
    }

    @Override
    public void addSlots(NonNullList<FactoryItemSlot> slots, @Nullable Player player) {
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
    public void setRecipeUsed(RecipeHolder<?> recipeHolder) {
        ResourceLocation resourcelocation = recipeHolder.id();
        this.recipes.addTo(resourcelocation, 1);
    }

    public RecipeHolder<?> getRecipeUsed(){
        return  null;
    }


    public void unlockRecipes(Player player) {
        List<RecipeHolder<?>> list = this.grantStoredRecipeExperience(player.level(), player.position());
        player.awardRecipes(list);
        this.recipes.clear();
    }

    public List<RecipeHolder<?>> grantStoredRecipeExperience(Level level, Vec3 worldPosition) {
        List<RecipeHolder<?>> list = Lists.newArrayList();
            this.recipes.object2IntEntrySet().fastForEach(entry-> {
                level.getRecipeManager().byKey(entry.getKey()).ifPresent((h) -> {
                    list.add(h);
                    if (hasXPTank()) {
                        int amountLiquidXp = Mth.floor((float) entry.getIntValue() * ((AbstractCookingRecipe) h.value()).getExperience()) * 5;
                        if (amountLiquidXp >= 1) {
                          xpTank.fill(FluidStack.create(Config.getLiquidXP(), amountLiquidXp * FluidStack.bucketAmount() /1000), false);
                            recipes.clear();
                        }
                    }else {
                        if (worldPosition != null)
                            splitAndSpawnExperience(level, worldPosition, entry.getIntValue(), ((AbstractCookingRecipe) h.value()).getExperience());
                    }
                });
            });

        return list;
    }
    public void handleAutoIO(){
        for (Direction dir : Direction.values()) {
            BlockEntity tile = getLevel().getBlockEntity(getBlockPos().offset(dir.getNormal()));
            if (tile == null) {
                continue;
            }
            if (furnaceSettings.get(dir.ordinal()) == 1 || furnaceSettings.get(dir.ordinal()) == 2 || furnaceSettings.get(dir.ordinal()) == 3 || furnaceSettings.get(dir.ordinal()) == 4) {
                IPlatformItemHandler other = FactoryAPIPlatform.getPlatformFactoryStorage(tile).getStorage(Storages.ITEM, dir.getOpposite()).get();

                if (other == null)
                    continue;
                if (getAutoInput() != 0 || getAutoOutput() != 0) {
                    if (getAutoInput() == 1) {
                        for (int INPUT : INPUTS())
                            if (furnaceSettings.get(dir.ordinal()) == 1 || furnaceSettings.get(dir.ordinal()) == 3) {
                                if (inventory.getItem(INPUT).getCount() >= inventory.getItem(INPUT).getMaxStackSize()) {
                                    continue;
                                }
                                for (int i = 0; i < other.getContainerSize(); i++) {
                                    if (other.getItem(i).isEmpty()) {
                                        continue;
                                    }
                                    ItemStack stack = other.extractItem(i, other.getItem(i).getMaxStackSize(), true);
                                    if (hasRecipe(stack) && inventory.getItem(INPUT).isEmpty() || ItemStack.isSameItemSameTags(inventory.getItem(INPUT), stack))
                                        inventory.insertItem(INPUT, other.extractItem(i, other.getItem(i).getMaxStackSize() - inventory.getItem(INPUT).getCount(), false), false);
                                }
                            }
                        for (int FUEL : FUEL())
                            if (furnaceSettings.get(dir.ordinal()) == 4) {
                                if (inventory.getItem(FUEL).getCount() >= inventory.getItem(FUEL).getMaxStackSize()) {
                                    continue;
                                }
                                for (int i = 0; i < other.getContainerSize(); i++) {
                                    if (!isItemFuel(other.getItem(i))) {
                                        continue;
                                    }
                                    if (other.getItem(i).isEmpty()) {
                                        continue;
                                    }
                                    ItemStack stack = other.extractItem(i, other.getItem(i).getMaxStackSize(), true);
                                    if (isItemFuel(stack) && inventory.getItem(FUEL).isEmpty() || ItemStack.isSameItemSameTags(inventory.getItem(FUEL), stack)) {
                                        inventory.insertItem(FUEL, other.extractItem(i, other.getItem(i).getMaxStackSize() - inventory.getItem(FUEL).getCount(), false), false);
                                    }
                                }
                            }
                    }

                    if (getAutoOutput() == 1) {
                        for (int FUEL : FUEL())
                            if (furnaceSettings.get(dir.ordinal()) == 4) {
                                if (inventory.getItem(FUEL).isEmpty()) {
                                    continue;
                                }
                                ItemStack fuel = inventory.getItem(FUEL);
                                if (isItemFuel(fuel)) {
                                    continue;
                                }
                                for (int i = 0; i < other.getContainerSize(); i++) {
                                    ItemStack stack = inventory.extractItem(FUEL, inventory.getItem(FUEL).getMaxStackSize() - other.getItem(i).getCount(), true);
                                    if (other.canPlaceItem(i, stack) && (other.getItem(i).isEmpty() || other.canPlaceItem(i, stack) && (ItemStack.isSameItemSameTags(other.getItem(i), stack) && other.getItem(i).getCount() + stack.getCount() <= other.getMaxStackSize()))) {
                                        inventory.setItem(FUEL,other.insertItem(i, stack, false));
                                    }
                                }
                            }
                        for (int output : OUTPUTS()) {
                            if (furnaceSettings.get(dir.ordinal()) == 2 || furnaceSettings.get(dir.ordinal()) == 3) {
                                if (inventory.getItem(output).isEmpty()) {
                                    continue;
                                }
                                if (BuiltInRegistries.BLOCK.getKey(tile.getBlockState().getBlock()).toString().contains("storagedrawers:")) {
                                    continue;
                                }
                                for (int i = 0; i < other.getContainerSize(); i++) {
                                    ItemStack stack = inventory.extractItem(output, inventory.getItem(output).getMaxStackSize() - other.getItem(i).getCount(), true);
                                    if (other.canPlaceItem(i, stack) && (other.getItem(i).isEmpty() || other.canPlaceItem(i, stack) && (ItemStack.isSameItemSameTags(other.getItem(i), stack) && other.getItem(i).getCount() + stack.getCount() <= other.getMaxStackSize()))) {
                                        inventory.setItem(output, other.insertItem(i, stack, false));
                                    }
                                }

                            }
                        }
                    }
                }

            }
        }
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