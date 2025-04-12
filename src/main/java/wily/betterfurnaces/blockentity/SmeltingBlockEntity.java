package wily.betterfurnaces.blockentity;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
//? if >=1.21.2 {
import net.minecraft.world.entity.player.StackedItemContents;
//?}
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.AirItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.Nullable;
import wily.betterfurnaces.BFRConfig;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.blocks.SmeltingBlock;
import wily.betterfurnaces.init.BlockEntityTypes;
import wily.betterfurnaces.init.ModObjects;
import wily.betterfurnaces.inventory.*;
import wily.betterfurnaces.items.*;
import wily.factoryapi.FactoryAPI;
import wily.factoryapi.FactoryAPIPlatform;
import wily.factoryapi.ItemContainerPlatform;
import wily.factoryapi.base.*;
import wily.factoryapi.base.network.CommonRecipeManager;
import wily.factoryapi.util.ColorUtil;
import wily.factoryapi.util.CompoundTagUtil;
import wily.factoryapi.util.FactoryItemUtil;
import wily.factoryapi.util.FluidInstance;

import java.util.*;
import java.util.List;
import java.util.function.Consumer;

public class SmeltingBlockEntity extends InventoryBlockEntity implements /*? if <1.20.2 {*//*RecipeHolder*//*?} else {*/RecipeCraftingHolder/*?}*/, StackedContentsCompatible{
    public final int[] provides = new int[Direction.values().length];
    private final int[] lastProvides = new int[this.provides.length];

    public int showInventorySettings;

    public boolean showOrientation;
    protected int timer;
    private int furnaceBurnTime;
    public int cookTime;
    public int totalCookTime = this.getCookTime();
    private int recipesUsed;
    private final Object2IntOpenHashMap<ResourceLocation> recipes = new Object2IntOpenHashMap<>();

    public boolean isForge(){
        return this instanceof ForgeBlockEntity;
    }

    public ResourceLocation getInteractStat(){
        return recipeType == RecipeType.SMOKING ? Stats.INTERACT_WITH_SMOKER : recipeType == RecipeType.BLASTING ? Stats.INTERACT_WITH_BLAST_FURNACE : Stats.INTERACT_WITH_FURNACE;
    }

    public RecipeType<? extends AbstractCookingRecipe> recipeType;

    public FactoryUpgradeSettings furnaceSettings;

    protected final Map<RecipeType<? extends AbstractCookingRecipe>, LRUCache<Item, Optional</*? if <1.20.2 {*//*AbstractCookingRecipe*//*?} else {*/RecipeHolder<AbstractCookingRecipe>/*?}*/>>> caches = new HashMap<>();

    public SmeltingBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state) {
        super(blockEntityType, pos, state);
        this.recipeType = RecipeType.SMELTING;
        furnaceSettings = new FactoryUpgradeSettings() {
            @Override
            public ItemStack getContainer() {
                return getUpgradeTypeSlotItem(ModObjects.FACTORY.get());
            }

            @Override
            public void onChanged() {
                setChanged();
            }
        };
    }
    public SmeltingBlockEntity(BlockPos pos, BlockState state){
        this(BlockEntityTypes.BETTER_FURNACE_TILE.get(),pos,state);
    }

    public int[] getFuelIndexes() {
        return hasUpgradeType(ModObjects.STORAGE.get()) ? new int[]{1,7} : new int[]{1};
    }

    public int getHeaterIndex() {
        return getFuelIndexes()[0];
    }

    public int[] getUpgradeIndexes(){
        return new int[]{3,4,5};
    }

    public int getFirstInputIndex(){
        return getInputs()[0];
    }

    public int getLastInput(){
        return getInputs()[getInputs().length - 1];
    }

    public int getFirstOutput(){
        return getOutputs()[0];
    }

    public int getLastOutput(){
        return getOutputs()[getOutputs().length - 1];
    }

    public int[] getInputs(){
        int[] inputs = new int[]{0};
        if (hasUpgradeType(ModObjects.STORAGE.get())) inputs = new int[]{0,6};
        return inputs;
    }

    public int[] getOutputs(){
        int[] outputs = new int[]{2};
        if (hasUpgradeType(ModObjects.STORAGE.get())) outputs = new int[]{2,8};
        return outputs;
    }

    public int[] getAllInputs(){
        return ArrayUtils.addAll(getInputs(), getFuelIndexes());
    }

    public int[] getAllSlots(){
        return ArrayUtils.addAll(getAllInputs(), getOutputs());
    }

    public int getEnergyConsume() {
        return 500;
    }

    public int getLiquidCapacity() {
        return 4000;
    }

    public int getEnergyCapacity() {
        return 16000;
    }

    public int getRecipeCookingTime(AbstractCookingRecipe recipe){
        return recipe./*? if >=1.21.2 {*/cookingTime/*?} else {*//*getCookingTime*//*?}*/();
    }

    private int getFromCache(LRUCache<Item, Optional</*? if <1.20.2 {*//*AbstractCookingRecipe*//*?} else {*/RecipeHolder<AbstractCookingRecipe>/*?}*/>> cache, Item key) {
        var recipe = cache.get(key);
        if (recipe == null || recipe.isEmpty()) return 0;
        return getRecipeCookingTime(recipe.get()/*? if >1.20.1 {*/.value()/*?}*/);
    }

    public boolean hasRecipe(ItemStack stack) {
        return grabRecipe(stack).isPresent();
    }

    protected LRUCache<Item, Optional</*? if <1.20.2 {*//*AbstractCookingRecipe*//*?} else {*/RecipeHolder<AbstractCookingRecipe>/*?}*/>> getCache() {
        return caches.computeIfAbsent(recipeType, key-> LRUCache.newInstance(BFRConfig.cacheCapacity.get()));
    }

    private Optional</*? if <1.20.2 {*//*AbstractCookingRecipe*//*?} else {*/RecipeHolder<AbstractCookingRecipe>/*?}*/> getRecipe(ItemStack stack, RecipeType<AbstractCookingRecipe> recipeType) {
        return CommonRecipeManager.getRecipeFor(recipeType, /*? if <1.20.5 {*//*new SimpleContainer(stack)*//*?} else {*/new SingleRecipeInput(stack)/*?}*/, this.level);
    }

    private Optional</*? if <1.20.2 {*//*AbstractCookingRecipe*//*?} else {*/RecipeHolder<AbstractCookingRecipe>/*?}*/> grabRecipe(ItemStack stack) {
        Item item = stack.getItem();
        if (item instanceof AirItem) {
            return Optional.empty();
        }
        return getCache().computeIfAbsent(item, i-> getRecipe(stack, (RecipeType<AbstractCookingRecipe>) recipeType));
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
        return (hasUpgrade(ModObjects.ENERGY.get()) && energyStorage.getEnergyStored() >= getEnergyConsume());
    }

    public int getCookTime() {
        if (hasUpgrade(ModObjects.GENERATOR.get()) || arraySlotAllEmpty(getInputs()))
            return getDefaultCookTime();
        int speed = getSpeed();
        if (speed == -1) {
            return -1;
        }

        return Math.max(1, speed);
    }

    protected int getSpeed() {
        int j = 0;
        int length = getInputs().length;
        for (int i : getInputs()) {
            ItemStack stack = inventory.getItem(i);
            int cache = getFromCache(getCache(), stack.getItem());
            int iC = cache <= 0 ? grabRecipe(stack).map(v-> getRecipeCookingTime(v/*? if >1.20.1 {*/.value()/*?}*/)).orElse(-1): cache;
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
                case 0 -> furnaceBurnTime;
                case 1 -> recipesUsed;
                case 2 -> cookTime;
                case 3 -> totalCookTime;
                case 4 -> showInventorySettings;
                case 5 -> energyStorage.getEnergyStored();
                case 6 -> energyStorage.getMaxEnergyStored();
                default -> 0;
            };
        }

        public void set(int index, int value) {
            switch (index) {
                case 0 -> furnaceBurnTime = value;
                case 1 -> recipesUsed = value;
                case 2 -> cookTime = value;
                case 3 -> totalCookTime = value;
                case 4 -> showInventorySettings = value;
                case 5 -> energyStorage.setEnergyStored(value);
                case 6 -> {}
            }

        }

        @Override
        public int getCount() {
            return 7;
        }
    };

    public List<UpgradeItem> getUpgrades() {
        List<UpgradeItem> list = new ArrayList<>();
        for (int slot : getUpgradeIndexes())
            if (inventory.getItem(slot).getItem() instanceof UpgradeItem upg) list.add(upg);
        return list;
    }

    public boolean hasUpgrade(UpgradeItem upg) {
        for (int slot : getUpgradeIndexes())
            if (upg.equals(inventory.getItem(slot).getItem()) && upg.isEnabled()) return true;
        return false;
    }

    public boolean hasUpgradeType(UpgradeItem upg) {
        return getUpgradeTypeSlot(upg) >= 0;
    }

    public int getDefaultCookTime(){
        return level != null && getBlockState().getBlock() instanceof SmeltingBlock ? ((SmeltingBlock)this.getBlockState().getBlock()).tier.defaultCookTime().get() : 200;
    }

    public ItemStack getUpgradeTypeSlotItem(UpgradeItem upg) {
        int i = getUpgradeTypeSlot(upg);
        return i < 0 ? ItemStack.EMPTY : inventory.getItem(i);
    }

    public int getUpgradeTypeSlot(UpgradeItem upg) {
        for (int slot : getUpgradeIndexes())
            if (inventory.getItem(slot).getItem() instanceof UpgradeItem upgradeItem &&  upgradeItem.isEnabled() && upg.isSameType(upgradeItem)) return slot;
        return -1;
    }

    public ItemStack getUpgradeSlotItem(Item upg) {
        for (int slot : getUpgradeIndexes())
            if (upg == inventory.getItem(slot).getItem()) return inventory.getItem(slot);
        return ItemStack.EMPTY;
    }

    public static int getFluidBurnTime(FluidInstance stack) {
        return stack == null ? 0 : FuelManager.getBurnTime(stack.getFluid().getBucket());
    }

    public static final SlotsIdentifier XP = new SlotsIdentifier(ChatFormatting.GREEN,"green");
    public final IPlatformFluidHandler fluidTank = new FactoryFluidHandler(getLiquidCapacity(), this, fs -> LiquidFuelUpgradeItem.supportsFluid(fs.getFluid()), SlotsIdentifier.LAVA, TransportState.EXTRACT_INSERT);
    public final IPlatformFluidHandler xpTank = new FactoryFluidHandler(2000, this, xp -> xp.getFluid().isSame(BFRConfig.getLiquidXP()), XP, TransportState.EXTRACT_INSERT);
    public final IPlatformEnergyStorage energyStorage = new FactoryEnergyStorage(getEnergyCapacity(),this);

    public void forceUpdateAllStates() {
        BlockState state = level.getBlockState(worldPosition);
        if (state.getValue(BlockStateProperties.LIT) != this.isBurning()) {
            level.setBlock(worldPosition, state.setValue(BlockStateProperties.LIT, this.isBurning()), 3);
        }
    }

    public int correspondentOutputSlot(int input){
        return hasUpgradeType(ModObjects.STORAGE.get())?  Arrays.stream(getOutputs()).filter(i-> canSmelt(irecipeSlot(input), input, i)).min().orElse(-1) : getFirstOutput() - getFirstInputIndex() + input;
    }

    public void trySmelt(){
        if (hasUpgradeType(ModObjects.STORAGE.get())) {
            int i = correspondentOutputSlot(getFirstInputIndex());
            if (i>=0) this.smeltItem(irecipeSlot(getFirstInputIndex()), getFirstInputIndex(), i);
        } else
            for (int i : getInputs()) {
                var recipe = irecipeSlot(i);
                if(!this.canSmelt(recipe, i, correspondentOutputSlot(i))) continue;
                this.smeltItem(recipe, i, correspondentOutputSlot(i));
            }
    }

    public int getUpdatedType(){
        return hasUpgrade(ModObjects.BLAST.get()) ? 1 : hasUpgrade(ModObjects.SMOKE.get()) ? 2 : hasUpgrade(ModObjects.GENERATOR.get()) ? 3 : 0;
    }

    @Override
    public Component getDisplayName() {
        return getUpdatedType() != 0 ? Component.translatable("tooltip.betterfurnacesreforged." + ((SmeltingBlock)this.getBlockState().getBlock()).tier.name() + "_tier", getUpdatedType() == 1 ? Blocks.BLAST_FURNACE.getName().getString() : getUpdatedType() == 2 ? Blocks.SMOKER.getName().getString() : getUpdatedType() == 3 ? Component.translatable("tooltip.betterfurnacesreforged.generator").getString() : "") : super.getDisplayName();
    }

    public /*? if <1.20.2 {*//*AbstractCookingRecipe*//*?} else {*/RecipeHolder<AbstractCookingRecipe>/*?}*/ irecipeSlot(int input){
        if (!ArrayUtils.contains(getInputs(), input)) return null;
        if (!inventory.getItem(input).isEmpty())
            return grabRecipe(inventory.getItem(input)).orElse(null);
        else return null;
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
            return (ItemContainerPlatform.isFluidContainer(stack) && ItemContainerPlatform.getFluid(stack).getAmount() > 0) && energyStorage.getEnergySpace() > 0;
        }
        return false;
    }

    public boolean smeltValid(){
        if (!hasUpgrade(ModObjects.GENERATOR.get()))
            for (int i : getInputs()) {
                if(!this.canSmelt(irecipeSlot(i), i, correspondentOutputSlot(i))) continue;
                return true;
            }
        return false;
    }

    public static void tick(Level level, BlockPos worldPosition, BlockState blockState, SmeltingBlockEntity be) {

        boolean wasBurning = be.isBurning();
        boolean setChanged = false;

        if (be.isBurning()) {
            --be.furnaceBurnTime;
        }


        if (be.hasXPTank()) be.grantStoredRecipeExperience(level, null);

        if (!be.hasUpgradeType(ModObjects.FACTORY.get()) && be.showOrientation) be.showOrientation = false;

        ItemStack fuel = be.inventory.getItem(be.getFuelIndexes()[0]);
        if ((be.hasUpgrade(ModObjects.COLOR.get()))) {
            if (!(level.getBlockState(be.getBlockPos()).getValue(SmeltingBlock.COLORED)))
                level.setBlock(be.getBlockPos(), level.getBlockState(be.getBlockPos()).setValue(SmeltingBlock.COLORED, true), 3);
        } else if ((level.getBlockState(be.getBlockPos()).getValue(SmeltingBlock.COLORED))) level.setBlock(be.getBlockPos(), level.getBlockState(be.getBlockPos()).setValue(SmeltingBlock.COLORED, false), 3);

        int updatedType = be.getUpdatedType();
        RecipeType<? extends AbstractCookingRecipe>[] recipeTypes = new RecipeType[]{RecipeType.SMELTING,RecipeType.BLASTING, RecipeType.SMOKING};
        if (updatedType == 3) {
            for (int i : new int[]{be.getFirstInputIndex(), be.getFirstOutput()}){
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
                    IPlatformEnergyStorage energyStorage = FactoryAPIPlatform.getPlatformFactoryStorage(other).getStorage(FactoryStorage.ENERGY,d.getOpposite()).get();
                    if (energyStorage == null) continue;
                    be.energyStorage.consumeEnergy(energyStorage.receiveEnergy(Math.min(energyStorage.getMaxReceive(), be.energyStorage.getMaxConsume()),false),false);
                }
            }
            if (be.hasUpgradeType(ModObjects.STORAGE.get())) {
                ItemStack storageInput = be.inventory.getItem(6);
                if (!storageInput.isEmpty()) be.inventory.setItem(6, be.inventory.insertItem(be.getFirstInputIndex(),storageInput,false));
                ItemStack storageFuel = be.inventory.getItem(7);
                if (!storageFuel.isEmpty()) be.inventory.setItem(7, be.inventory.insertItem(be.getFuelIndexes()[0],storageFuel,false));
            }
            else if (!be.isForge()){
                for (int i : new int[]{6,7,8}){
                    ItemStack stack = be.inventory.getItem(i);
                    if (!stack.isEmpty())Containers.dropItemStack(level, be.getBlockPos().getX(), be.getBlockPos().getY() + 1, be.getBlockPos().getZ(), stack);
                }
            }


            if (be.hasUpgrade(ModObjects.ENERGY.get())) {
                if (ItemContainerPlatform.isEnergyContainer(fuel) && ItemContainerPlatform.getEnergy(fuel) > 0) {
                    if (be.energyStorage.getEnergySpace() > 0) {
                        be.energyStorage.receiveEnergy(ItemContainerPlatform.extractEnergy(be.energyStorage.getEnergySpace(),fuel).contextEnergy(), false);
                        be.inventory.setItem(be.getFuelIndexes()[0], fuel);
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
                        if (level.getSignal(worldPosition.relative(side), side) > 0) {
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

                        if (level.getSignal(worldPosition.relative(side), side) > 0) {
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
                ItemContainerPlatform.ItemFluidContext context = ItemContainerPlatform.drainItem(be.fluidTank.getTotalSpace(),fuel);
                long amount = be.fluidTank.fill(context.fluidInstance(), false);
                if (amount > 0) {
                    level.playSound(null, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), SoundEvents.BUCKET_FILL_LAVA, SoundSource.PLAYERS, 0.6F, 0.8F);
                    be.inventory.setItem(be.getFuelIndexes()[0], context.container());
                }
            }

            if ((be.isBurning() || !fuel.isEmpty() || be.isLiquid() || (be.isEnergy() && !be.canGeneratorWork())) &&  (be.arraySlotFilled(be.getInputs(), true)|| be.canGeneratorWork()))  {
                boolean valid = be.smeltValid() || be.canGeneratorWork();
                if (!be.isBurning() && (valid)) {
                    if (be.isLiquid() && !be.fluidTank.getFluidInstance().isEmpty() && getFluidBurnTime(be.fluidTank.getFluidInstance()) > 0){
                        int fluidAmount = 200000 / getFluidBurnTime(be.fluidTank.getFluidInstance());
                        if (be.fluidTank.getFluidInstance().getAmount() >= fluidAmount) {
                            be.furnaceBurnTime = be.getEnderMultiplier() * get_cook_time;
                            be.recipesUsed = be.furnaceBurnTime;
                            be.fluidTank.drain(fluidAmount, false);
                        }
                    }else if (be.isEnergy()){
                        be.furnaceBurnTime = be.getEnderMultiplier() * get_cook_time;
                        be.recipesUsed = be.furnaceBurnTime;
                        for (int a : be.getInputs())
                            be.energyStorage.consumeEnergy(be.getEnergyConsume() * be.getOreProcessingMultiplier(be.inventory.getItem(a)), false);
                    }else{
                        be.furnaceBurnTime = (int) Math.ceil(be.getEnderMultiplier() * getBurnTime(fuel) * (get_cook_time / 200D));
                        be.recipesUsed = be.furnaceBurnTime;
                    }
                    if (be.isBurning()) {
                        setChanged = true;
                        if (be.hasEnder()) {
                            if (be.hasUpgrade(ModObjects.FUEL.get())) {
                                be.breakDurabilityItem(be.getUpgradeSlotItem(ModObjects.FUEL.get()));
                            }
                        }
                        if ((!be.isLiquid() || be.fluidTank.getFluidInstance().getAmount() < 10) && !be.isEnergy()) {
                            if (ItemContainerPlatform.isFluidContainer(fuel)){
                                ItemContainerPlatform.ItemFluidContext context = ItemContainerPlatform.drainItem(be.fluidTank.getTotalSpace(),fuel);
                                be.inventory.setItem(be.getFuelIndexes()[0],context.container());
                            }

                            if (!fuel.isEmpty() && FuelManager.isFuel(fuel)) {
                                fuel.shrink(1);
                                if (be.hasUpgrade(ModObjects.FUEL.get())) {
                                    be.breakDurabilityItem(be.getUpgradeSlotItem(ModObjects.FUEL.get()));
                                }
                            }
                        }
                    }
                }
                if (be.isBurning() && valid) {
                    ++be.cookTime;
                    if (be.hasUpgrade(ModObjects.GENERATOR.get())){
                        ItemContainerPlatform.ItemFluidContext context = ItemContainerPlatform.drainItem(1, be.getUpgradeSlotItem(ModObjects.GENERATOR.get()));
                        if (!context.fluidInstance().isEmpty()) be.inventory.setItem(be.getUpgradeTypeSlot(ModObjects.GENERATOR.get()), context.container());
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
                        setChanged = true;
                    }
                } else {
                    be.cookTime = 0;
                }
            } else if (!be.isBurning() && be.cookTime > 0) {
                be.cookTime = Mth.clamp(be.cookTime - 2, 0, be.totalCookTime);
            }
            if (wasBurning != be.isBurning()) {
                setChanged = true;
                be.level.setBlock(be.worldPosition, be.level.getBlockState(be.worldPosition).setValue(BlockStateProperties.LIT, be.isBurning()), 3);
            }
            if ((be.timer % 24 == 0) && (be.hasUpgradeType(ModObjects.FACTORY.get()))){
                if (be.cookTime <= 0) {
                    if (be.arraySlotFilled(be.getInputs(), false)) {
                        be.handleAutoIO();
                        setChanged = true;
                    } else if (be.hasArraySlotSpace(be.getInputs())) {
                        be.handleAutoIO();
                        setChanged = true;
                    }
                    if (be.arraySlotFilled(be.getOutputs(), true)) {
                        be.handleAutoIO();
                        setChanged = true;
                    }
                    if (be.inventory.getItem(be.getFuelIndexes()[0]).isEmpty() && !be.isLiquid() && !be.isEnergy()) {
                        be.handleAutoIO();
                        setChanged = true;
                    } else if (be.inventory.getItem(be.getFuelIndexes()[0]).getCount() < be.inventory.getItem(be.getFuelIndexes()[0]).getMaxStackSize() || ItemContainerPlatform.isFluidContainer(fuel) && ItemContainerPlatform.getFluid(fuel).getAmount() < be.fluidTank.getTotalSpace()){
                        be.handleAutoIO();
                        setChanged = true;
                    }
                }
            }
        }

        if (setChanged) {
            be.setChanged();
        }

    }

    public float[] getColor() {
        ItemStack colorUpgrade = getUpgradeSlotItem(ModObjects.COLOR.get());
        if (colorUpgrade.isEmpty()) return new float[]{1,1,1};
        //? if <1.20.5 {
        /*CompoundTag nbt = colorUpgrade.getOrCreateTag();
        return new float[]{nbt.getInt("red") / 255f, nbt.getInt("green") / 255f, nbt.getInt("blue") / 255f};
        *///?} else {
        return colorUpgrade.getOrDefault(ModObjects.BLOCK_TINT.get(), ModObjects.BlockTint.WHITE).toFloatArray();
        //?}
    }

    public int getSettingBottom() {
        return this.furnaceSettings.getSides(getIndexBottom());
    }

    public int getSettingTop() {
        return this.furnaceSettings.getSides(getIndexTop());
    }

    public int getSettingFront() {
        return this.furnaceSettings.getSides(getIndexFront());
    }

    public int getSettingBack() {
        return this.furnaceSettings.getSides(getIndexBack());
    }

    public int getSettingLeft() {
        return this.furnaceSettings.getSides(getIndexLeft());
    }

    public int getSettingRight() {
        return this.furnaceSettings.getSides(getIndexRight());
    }

    protected BlockSide[] getSidesOrder(){return BlockSide.values();}

    public int getIndexBottom() {
        return BlockSide.BOTTOM.blockStateToFacing(getBlockState(),getSidesOrder()).ordinal();
    }

    public int getIndexTop() {
        return BlockSide.TOP.blockStateToFacing(getBlockState(),getSidesOrder()).ordinal();
    }

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
        return this.furnaceSettings.getAutoIO(0);
    }

    public int getAutoOutput() {
        return this.furnaceSettings.getAutoIO(1);
    }

    public int getRedstoneSetting() {
        return this.furnaceSettings.getRedstone(0);
    }

    public int getRedstoneComSub() {
        return this.furnaceSettings.getRedstone(1);
    }

    public boolean isBurning() {
        return this.furnaceBurnTime > 0;
    }

    TagKey<Item> ore = getItemTag(FactoryAPI.createLocation(FactoryAPI.getLoader().isForgeLike() ? "forge" : "c", "ores"));

    private TagKey<Item> getItemTag(ResourceLocation resourceLocation) {
        return TagKey.create(Registries.ITEM,resourceLocation);
    }

    private boolean hasRawOreTag(ItemStack stack){
        if (BFRConfig.checkRawOresName.get()) return BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath().startsWith("raw_");
        if (FactoryAPI.getLoader().isForgeLike()) return stack.is(getItemTag( FactoryAPI.createLocation("forge","raw_materials")));
        else {
            if (stack.is(getItemTag(FactoryAPI.createLocation("c", "raw_materials")))) return true;
            for (TagKey<Item> tag: stack.getTags().toList()){
                if (!tag.location().toString().contains("raw_") ||!tag.location().toString().contains("_ores")) continue;
                return true;
            }
            return false;
        }
    }

    protected boolean isOre(ItemStack input){
        if (BFRConfig.checkCommonOresName.get()) return BuiltInRegistries.ITEM.getKey(input.getItem()).getPath().endsWith("_ore");
        return (input.is(ore));
    }

    protected boolean isRaw(ItemStack input){
        return (hasRawOreTag(input));
    }

    protected int getOreProcessingMultiplier(ItemStack input){
        if (hasUpgradeType(ModObjects.ORE_PROCESSING.get())){
            OreProcessingUpgradeItem oreup = (OreProcessingUpgradeItem) getUpgradeTypeSlotItem(ModObjects.ORE_PROCESSING.get()).getItem();
            if ((isRaw(input) && oreup.acceptRaw) || (isOre(input) && oreup.acceptOre)) return oreup.multiplier;

        }
        return input.isEmpty() ? 0 : 1;
    }

    @Override
    public void onRemoved(boolean withContents) {
        super.onRemoved(withContents);
        if (withContents) grantStoredRecipeExperience(getLevel(), getBlockPos().getCenter());
    }

    @Override
    public AbstractContainerMenu createMenu(int i, Inventory playerInventory) {
        return new SmeltingMenu(i, level, getBlockPos(), playerInventory, fields);
    }

    protected boolean canSmelt(/*? if <1.20.2 {*//*AbstractCookingRecipe*//*?} else {*/RecipeHolder<AbstractCookingRecipe>/*?}*/ recipe, int inputIndex, int outputIndex) {
        ItemStack input = this.getInv().getItem(inputIndex);
        if (outputIndex >= 0 && !input.isEmpty() && recipe != null) {
            ItemStack recipeOutput = getResult(recipe/*? if >1.20.1 {*/.value()/*?}*/, input);
            if (!recipeOutput.isEmpty()) {
                ItemStack output = this.getInv().getItem(outputIndex);
                if (output.isEmpty()) return true;
                else if (!FactoryItemUtil.equalItems(output,recipeOutput)) return false;
                else {
                    return output.getCount() + recipeOutput.getCount() <= output.getMaxStackSize();
                }
            }
        }
        return false;
    }

    private ItemStack getResult(@Nullable AbstractCookingRecipe recipe, ItemStack input) {
        ItemStack out = recipe.assemble(/*? if >=1.20.5 {*/new SingleRecipeInput(input)/*?} else {*//*new SimpleContainer(input)*//*?}*/, level.registryAccess());
        out.setCount(out.getCount() * getOreProcessingMultiplier(input));
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

    protected void smeltItem(@Nullable /*? if <1.20.2 {*//*AbstractCookingRecipe*//*?} else {*/RecipeHolder<AbstractCookingRecipe>/*?}*/ recipe, int inputIndex, int outputIndex) {
        timer = 0;
        if (recipe != null && this.canSmelt(recipe, inputIndex, outputIndex)) {
            ItemStack input = this.getInv().getItem(inputIndex);

            if (addOrSetItem(getResult(recipe/*? if >1.20.1 {*/.value()/*?}*/, input),inventory,outputIndex) > 0 && hasUpgrade(ModObjects.ORE_PROCESSING.get()) && (isOre(input))) {
                breakDurabilityItem(getUpgradeSlotItem(ModObjects.ORE_PROCESSING.get()));
            }

            this.checkXP();
            this.setRecipeUsed(recipe);

            ItemStack fuel = inventory.getItem(getFuelIndexes()[0]);
            if (input.getItem() == Blocks.WET_SPONGE.asItem() && !fuel.isEmpty()) {
                if (ItemContainerPlatform.isFluidContainer(fuel)){
                    ItemContainerPlatform.fillItem(fuel, FluidInstance.create(Fluids.WATER, 1000));
                    inventory.setItem(getFuelIndexes()[0],fuel);
                }
            }
            //? if (<=1.20.1 && forge) || (1.21.1 && neoforge) {
            /*if (FactoryAPI.isModLoaded("pmmo")) {
                wily.betterfurnaces.compat.ProjectMMOCompat.burnEvent(input,level,worldPosition);
            }
            *///?}
            input.shrink(1);
        }
    }

    @Override
    public void /*? if <1.20.5 {*//*load(CompoundTag tag)*//*?} else {*/loadAdditional(CompoundTag tag, HolderLookup.Provider provider)/*?}*/ {
        super./*? if <1.20.5 {*//*load(tag)*//*?} else {*/loadAdditional(tag, provider)/*?}*/;
        this.furnaceBurnTime = CompoundTagUtil.getInt(tag, "BurnTime").orElse(0);
        this.cookTime = CompoundTagUtil.getInt(tag, "CookTime").orElse(0);
        this.totalCookTime = CompoundTagUtil.getInt(tag, "CookTimeTotal").orElse(0);
        this.timer = 0;
        this.recipesUsed = getBurnTime(this.getInv().getItem(1));
        fluidTank.deserializeTag(CompoundTagUtil.getCompoundTagOrEmpty(tag, "fluidTank"));
        xpTank.deserializeTag(CompoundTagUtil.getCompoundTagOrEmpty(tag, "xpTank"));
        energyStorage.deserializeTag(CompoundTagUtil.getCompoundTagOrEmpty(tag, "energy"));
        CompoundTag compoundnbt = CompoundTagUtil.getCompoundTagOrEmpty(tag, "RecipesUsed");
        for (String s : compoundnbt./*? if <1.21.5 {*//*getAllKeys*//*?} else {*/keySet/*?}*/()) {
            this.recipes.put(FactoryAPI.createLocation(s), CompoundTagUtil.getInt(compoundnbt, s).orElse(0));
        }
        this.showInventorySettings = CompoundTagUtil.getInt(tag, "ShowInvSettings").orElse(0);
        this.showOrientation = CompoundTagUtil.getBoolean(tag, "ShowOrientation").orElse(false);
    }

    @Override
    public void saveAdditional(CompoundTag tag/*? if >=1.20.5 {*/, HolderLookup.Provider provider/*?}*/) {
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
        super.saveAdditional(tag/*? if >=1.20.5 {*/, provider/*?}*/);
    }

    protected static int getBurnTime(ItemStack stack) {
        if (stack.isEmpty()) {
            return 0;
        } else {
            return FuelManager.getBurnTime(stack);
        }
    }

    public <T extends IPlatformHandler> ArbitrarySupplier<T> getStorage(FactoryStorage<T> storage, Direction facing){
        if (storage == FactoryStorage.FLUID){
            if ((facing == null || (!hasUpgrade(ModObjects.GENERATOR.get()) && !hasXPTank()) || (facing.ordinal() == getIndexTop() || facing.ordinal() == getIndexBottom()))) {
                if(isLiquid()) return ()-> (T)(facing == null ?  fluidTank : FactoryAPIPlatform.filteredOf(fluidTank,Direction.NORTH,TransportState.INSERT));
            } else {
                if (hasUpgrade(ModObjects.GENERATOR.get())) {
                    ItemStack gen = getUpgradeSlotItem(ModObjects.GENERATOR.get());
                    return ()-> (T)(gen.getItem() instanceof GeneratorUpgradeItem item ? (facing == null ? item.getFluidStorage(gen) : FactoryAPIPlatform.filteredOf(item.getFluidStorage(gen),Direction.NORTH,TransportState.INSERT)) : null);
                } else if (hasXPTank())
                    return ()-> (T)(facing == null ?  xpTank : FactoryAPIPlatform.filteredOf(xpTank,Direction.NORTH, TransportState.EXTRACT));
            }
        }
        if (storage == FactoryStorage.ITEM){
            return ()-> (T) (facing == null ? inventory : FactoryAPIPlatform.filteredOf(inventory,facing, getSlotsTransport(facing).key(), getSlotsTransport(facing).value()));
        }
        if (storage == FactoryStorage.ENERGY && (hasUpgrade(ModObjects.ENERGY.get()) ||  hasUpgrade(ModObjects.GENERATOR.get()))){
            return ()-> (T)(facing == null || !hasUpgrade(ModObjects.GENERATOR.get()) ? energyStorage : FactoryAPIPlatform.filteredOf(energyStorage, Direction.NORTH, TransportState.ofBoolean( true, false)));
        }
        return ArbitrarySupplier.empty();
    }

    @Override
    public Pair<int[], TransportState> getSlotsTransport(Direction side) {
        if (hasUpgradeType(ModObjects.FACTORY.get())) {
            if (this.furnaceSettings.getSides(side.ordinal()) == 1) {
                return Pair.of(getAllInputs(),TransportState.INSERT);
            } else if (this.furnaceSettings.getSides(side.ordinal()) == 2) {
                return Pair.of(getOutputs(),TransportState.EXTRACT_INSERT);
            } else if (this.furnaceSettings.getSides(side.ordinal()) == 3) {
                return Pair.of(getAllSlots(), TransportState.EXTRACT_INSERT);
            } else if (this.furnaceSettings.getSides(side.ordinal()) == 4) {
                return Pair.of(getFuelIndexes(), TransportState.EXTRACT_INSERT);
            }
        } else {
            if (side == Direction.UP) return Pair.of(getInputs(),TransportState.INSERT);
            else if (side == Direction.DOWN) return Pair.of(getOutputs(),TransportState.EXTRACT);
            else return Pair.of(getFuelIndexes(),TransportState.EXTRACT_INSERT);
        }
        return Pair.of(new int[0],TransportState.NONE);
    }

    @Override
    public boolean IcanExtractItem(int index, ItemStack stack) {
        if (hasUpgradeType(ModObjects.FACTORY.get())) {
            return !ArrayUtils.contains(getInputs(), index) && !ArrayUtils.contains(getUpgradeIndexes(), index) && (!ArrayUtils.contains(getFuelIndexes(), index) || (!FuelManager.isFuel(stack) && (!ItemContainerPlatform.isEnergyContainer(stack) || ItemContainerPlatform.getEnergy(stack) <= 0)));
        }else{
            return ArrayUtils.contains(getOutputs(),index);
        }
    }

    @Override
    public void addSlots(Consumer<FactoryItemSlot> slots, @Nullable Player player) {
        slots.accept(new SlotInput(this, 0, 54, 18, (s) -> !this.hasUpgrade(ModObjects.GENERATOR.get())));
        slots.accept(new SlotFuel(this, 1, 54, 54));
        slots.accept(new SlotOutput(player, this, 2, 116, 35, (s) -> !this.hasUpgrade(ModObjects.GENERATOR.get())));

        slots.accept(new SlotUpgrade(this, 3, 8, 18));
        slots.accept(new SlotUpgrade(this, 4, 8, 36));
        slots.accept(new SlotUpgrade(this, 5, 8, 54));

        slots.accept(new SlotInput(this, 6, 36, 18, s -> hasUpgradeType(ModObjects.STORAGE.get())));
        slots.accept(new SlotFuel(this, 7, 36, 54, s-> hasUpgradeType(ModObjects.STORAGE.get())));
        slots.accept(new SlotOutput(player, this, 8, 138, 35, s -> hasUpgradeType(ModObjects.STORAGE.get())));

    }

    public void clearRecipes(){
        recipes.clear();
    }

    public void checkXP() {
        boolean flag2 = false;
        if (this.recipes.size() > BFRConfig.furnaceXPDropValue.get()) {
            this.grantStoredRecipeExperience(level, new Vec3(worldPosition.getX() + level.random.nextInt(2) - 1, worldPosition.getY(), worldPosition.getZ() + level.random.nextInt(2) - 1));
            this.recipes.clear();
        } else {
            for (Object2IntMap.Entry<ResourceLocation> entry : this.recipes.object2IntEntrySet()) {
                var recipe = CommonRecipeManager.byId(entry.getKey(), recipeType);
                if (recipe != null) {
                    if (entry.getIntValue() > BFRConfig.furnaceAccumulatedXPDropValue.get()) {
                        if (!flag2) {
                            this.grantStoredRecipeExperience(level, new Vec3(worldPosition.getX() + level.random.nextInt(2) - 1, worldPosition.getY(), worldPosition.getZ() + level.random.nextInt(2) - 1));
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

    @Override
    public void setRecipeUsed(/*? if <1.20.2 {*//*Recipe<?>*//*?} else {*/RecipeHolder<?>/*?}*/ recipeHolder) {
        this.recipes.addTo(recipeHolder./*? if >=1.21.2 {*/id().location()/*?} else if >1.20.1 {*//*id()*//*?} else {*//*getId()*//*?}*/, 1);
    }

    public /*? if <1.20.2 {*//*Recipe<?>*//*?} else {*/RecipeHolder<?>/*?}*/ getRecipeUsed(){
        return null;
    }

    public void unlockRecipes(Player player) {
        player.awardRecipes(grantStoredRecipeExperience(player.level(), player.position()));
        clearRecipes();
    }

    public List</*? if <1.20.2 {*//*Recipe<?>*//*?} else {*/RecipeHolder<?>/*?}*/> grantStoredRecipeExperience(Level level, Vec3 worldPosition) {
        List</*? if <1.20.2 {*//*Recipe<?>*//*?} else {*/RecipeHolder<?>/*?}*/> list = Lists.newArrayList();
        this.recipes.object2IntEntrySet().fastForEach(entry-> {
            var recipe = CommonRecipeManager.byId(entry.getKey(), recipeType);
            if (recipe == null) return;
            list.add(recipe);
            float experience = recipe/*? if >1.20.1 {*/.value()/*?}*/./*? if <1.21.2 {*//*getExperience*//*?} else {*/experience/*?}*/();
            if (hasXPTank()) {
                int amountLiquidXp = Mth.floor((float) entry.getIntValue() * experience) * 5;
                if (amountLiquidXp >= 1) {
                    xpTank.fill(FluidInstance.create(BFRConfig.getLiquidXP(), amountLiquidXp), false);
                    recipes.clear();
                }
            }else {
                if (worldPosition != null)
                    splitAndSpawnExperience(level, worldPosition, entry.getIntValue(), experience);
            }
        });

        return list;
    }

    public void handleAutoIO(){
        for (Direction dir : Direction.values()) {
            BlockEntity tile = getLevel().getBlockEntity(getBlockPos().relative(dir));
            if (tile == null) {
                continue;
            }
            if (furnaceSettings.getSides(dir.ordinal()) == 1 || furnaceSettings.getSides(dir.ordinal()) == 2 || furnaceSettings.getSides(dir.ordinal()) == 3 || furnaceSettings.getSides(dir.ordinal()) == 4) {
                IPlatformItemHandler other = FactoryAPIPlatform.getPlatformFactoryStorage(tile).getStorage(FactoryStorage.ITEM, dir.getOpposite()).get();

                if (other == null)
                    continue;
                if (getAutoInput() != 0 || getAutoOutput() != 0) {
                    if (getAutoInput() == 1) {
                        for (int INPUT : getInputs())
                            if (furnaceSettings.getSides(dir.ordinal()) == 1 || furnaceSettings.getSides(dir.ordinal()) == 3) {
                                if (inventory.getItem(INPUT).getCount() >= inventory.getItem(INPUT).getMaxStackSize()) {
                                    continue;
                                }
                                for (int i = 0; i < other.getContainerSize(); i++) {
                                    if (other.getItem(i).isEmpty()) {
                                        continue;
                                    }
                                    ItemStack stack = other.extractItem(i, other.getItem(i).getMaxStackSize(), true);
                                    if (hasRecipe(stack) && inventory.getItem(INPUT).isEmpty() || FactoryItemUtil.equalItems(inventory.getItem(INPUT), stack))
                                        inventory.insertItem(INPUT, other.extractItem(i, other.getItem(i).getMaxStackSize() - inventory.getItem(INPUT).getCount(), false), false);
                                }
                            }
                        for (int FUEL : getFuelIndexes())
                            if (furnaceSettings.getSides(dir.ordinal()) == 4) {
                                if (inventory.getItem(FUEL).getCount() >= inventory.getItem(FUEL).getMaxStackSize()) {
                                    continue;
                                }
                                for (int i = 0; i < other.getContainerSize(); i++) {
                                    if (!FuelManager.isFuel(other.getItem(i))) {
                                        continue;
                                    }
                                    if (other.getItem(i).isEmpty()) {
                                        continue;
                                    }
                                    ItemStack stack = other.extractItem(i, other.getItem(i).getMaxStackSize(), true);
                                    if (FuelManager.isFuel(stack) && inventory.getItem(FUEL).isEmpty() || FactoryItemUtil.equalItems(inventory.getItem(FUEL), stack)) {
                                        inventory.insertItem(FUEL, other.extractItem(i, other.getItem(i).getMaxStackSize() - inventory.getItem(FUEL).getCount(), false), false);
                                    }
                                }
                            }
                    }

                    if (getAutoOutput() == 1) {
                        for (int FUEL : getFuelIndexes())
                            if (furnaceSettings.getSides(dir.ordinal()) == 4) {
                                if (inventory.getItem(FUEL).isEmpty()) {
                                    continue;
                                }
                                ItemStack fuel = inventory.getItem(FUEL);
                                if (FuelManager.isFuel(fuel)) {
                                    continue;
                                }
                                for (int i = 0; i < other.getContainerSize(); i++) {
                                    ItemStack stack = inventory.extractItem(FUEL, inventory.getItem(FUEL).getMaxStackSize() - other.getItem(i).getCount(), true);
                                    if (other.canPlaceItem(i, stack) && (other.getItem(i).isEmpty() || other.canPlaceItem(i, stack) && (FactoryItemUtil.equalItems(other.getItem(i), stack) && other.getItem(i).getCount() + stack.getCount() <= other.getMaxStackSize()))) {
                                        inventory.setItem(FUEL,other.insertItem(i, stack, false));
                                    }
                                }
                            }
                        for (int output : getOutputs()) {
                            if (furnaceSettings.getSides(dir.ordinal()) == 2 || furnaceSettings.getSides(dir.ordinal()) == 3) {
                                if (inventory.getItem(output).isEmpty()) {
                                    continue;
                                }
                                if (BuiltInRegistries.BLOCK.getKey(tile.getBlockState().getBlock()).toString().contains("storagedrawers:")) {
                                    continue;
                                }
                                for (int i = 0; i < other.getContainerSize(); i++) {
                                    ItemStack stack = inventory.extractItem(output, inventory.getItem(output).getMaxStackSize() - other.getItem(i).getCount(), true);
                                    if (other.canPlaceItem(i, stack) && (other.getItem(i).isEmpty() || other.canPlaceItem(i, stack) && (FactoryItemUtil.equalItems(other.getItem(i), stack) && other.getItem(i).getCount() + stack.getCount() <= other.getMaxStackSize()))) {
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
    public void fillStackedContents(/*? if >=1.21.2 {*/StackedItemContents/*?} else {*//*StackedContents*//*?}*/ stackedContents) {
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            stackedContents.accountStack(inventory.getItem(i));
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