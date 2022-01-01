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
import wily.betterfurnaces.blocks.BlockCobblestoneGenerator;
import wily.betterfurnaces.init.Registration;
import wily.betterfurnaces.items.ItemUpgradeFuelEfficiency;
import wily.betterfurnaces.items.ItemUpgradeOreProcessing;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockEntityCobblestoneGenerator extends BlockEntityInventory {

    @Override
    public int[] IgetSlotsForFace(Direction side) {
        return new int[0];
    }

    @Override
    public boolean IcanExtractItem(int index, ItemStack stack, Direction direction) {
        return false;
    }

    @Override
    public String IgetName() {
        return "block.betterfurnacesreforged.cobblestone_generator";
    }

    public static class BlockCobblestoneGeneratorContainer extends wily.betterfurnaces.container.BlockCobblestoneGeneratorContainer {
            public BlockCobblestoneGeneratorContainer(int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player) {
            super(Registration.COB_GENERATOR_CONTAINER.get(), windowId, world, pos, playerInventory, player);
        }

    public BlockCobblestoneGeneratorContainer(int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player, ContainerData fields) {
            super(Registration.COB_GENERATOR_CONTAINER.get(), windowId, world, pos, playerInventory, player, fields);
        }
    }
    public final ContainerData fields = new ContainerData() {
        public int get(int index) {

            if (index == 0)
            return BlockEntityCobblestoneGenerator.this.cobTime;
            if (index == 1)
                return BlockEntityCobblestoneGenerator.this.resultType;
            if (index == 2)
                return BlockEntityCobblestoneGenerator.this.actualCobTime;
            else return 0;
        }

        public void set(int index, int value) {
            if (index == 0)
            BlockEntityCobblestoneGenerator.this.cobTime = value;
            if (index == 1)
                BlockEntityCobblestoneGenerator.this.resultType = value;
            if (index == 2)
                BlockEntityCobblestoneGenerator.this.actualCobTime = value;
        }

        @Override
        public int getCount() {
            return 3;
        }
    };
    @Override
    public AbstractContainerMenu IcreateMenu(int i, Inventory playerInventory, Player playerEntity) {
        return new BlockCobblestoneGeneratorContainer(i, level, worldPosition, playerInventory, playerEntity, this.fields);
    }
    public final int[] provides = new int[Direction.values().length];
    private final int[] lastProvides = new int[this.provides.length];

    public static final int INPUT = 0;
    public static final int INPUT1 = 1;
    public static final int OUTPUT = 2;
    public static final int UPGRADE = 3;

    /**
     * The number of ticks that the furnace will keep burning
     */
    private int cobTime;
    private int actualCobTime = getCobTime();
    public int resultType = 1;

    public static class BlockEntityCobblestoneGeneratorDefinition extends BlockEntityCobblestoneGenerator {
        public BlockEntityCobblestoneGeneratorDefinition(BlockPos pos, BlockState state) {
            super(Registration.COB_GENERATOR_TILE.get(), pos, state);
        }

    }
    public BlockEntityCobblestoneGenerator(BlockEntityType<?> tileentitytypeIn, BlockPos pos, BlockState state) {
        super(tileentitytypeIn, pos, state, 5);

    }

    public void forceUpdateAllStates() {
        BlockState state = level.getBlockState(worldPosition);
        if (state.getValue(BlockCobblestoneGenerator.TYPE) != cobGen()) {
            level.setBlock(worldPosition, state.setValue(BlockCobblestoneGenerator.TYPE, cobGen()), 3);
        }
    }

    public static void tick(Level level, BlockPos worldPosition, BlockState blockState, BlockEntityCobblestoneGenerator e) {
        if (e.actualCobTime != e.getCobTime()){
            e.actualCobTime = e.getCobTime();
        }
        if (e.cobTime > e.getCobTime()){
            e.cobTime = e.getCobTime();
        }
        ItemStack output = e.getItem(2);
        ItemStack upgrade = e.getItem(3);
        ItemStack upgrade1 = e.getItem(4);
        boolean can = (output.getCount() + 1 <= output.getMaxStackSize());
        boolean can1 = (output.isEmpty());
        boolean can3 = (output.getItem() == e.getResult().getItem());
        if (e.cobGen() > 0){
            e.forceUpdateAllStates();
        }
        if ((e.cobGen() == 3) || e.cobTime > 0 && e.cobTime < e.actualCobTime) {
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
    protected int getCobTime(){
        ItemStack upgrade = this.getInv().getStackInSlot(UPGRADE);
        if (upgrade.isEmpty() && resultType < 3){
            return 80;
        }else if (upgrade.getItem() instanceof ItemUpgradeFuelEfficiency && resultType < 3){
            return 40;
        }else if (upgrade.isEmpty() && resultType == 3){
            return 150;
        }else if (upgrade.getItem() instanceof ItemUpgradeFuelEfficiency && resultType == 3){
            return 75;
        }else if (upgrade.isEmpty() && resultType == 4){
            return 600;
        }else if (upgrade.getItem() instanceof ItemUpgradeFuelEfficiency && resultType == 4){
            return 300;
        }else return 0;
    }
    protected ItemStack getResult(){
        ItemStack result;
        if (resultType == 1) result = new ItemStack(Items.COBBLESTONE);
        else if (resultType == 2) result = new ItemStack(Items.STONE);
        else if (resultType == 3) result = new ItemStack(Items.BLACKSTONE);
        else if (resultType == 4) result = new ItemStack(Items.OBSIDIAN);
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
    public CompoundTag save(CompoundTag tag) {
        tag.put("inventory", getInv().serializeNBT());
        tag.putInt("CobTime", this.cobTime);
        tag.putInt("ResultType", this.resultType);
        tag.putInt("ActualCobTime", this.actualCobTime);

        return super.save(tag);
    }

    LazyOptional<? extends IItemHandler>[] invHandlers =
            net.minecraftforge.items.wrapper.SidedInvWrapper.create(this, Direction.DOWN, Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST);

    @Nonnull
    @Override
    public <
            T> net.minecraftforge.common.util.LazyOptional<T> getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @Nullable Direction facing) {

        if (!this.isRemoved() && facing != null && capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
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
