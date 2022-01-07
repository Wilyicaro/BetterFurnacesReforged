package wily.betterfurnaces.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import wily.betterfurnaces.blocks.BlockCobblestoneGenerator;
import wily.betterfurnaces.init.Registration;
import wily.betterfurnaces.items.ItemUpgradeFuelEfficiency;
import wily.betterfurnaces.items.ItemUpgradeOreProcessing;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;

public class BlockCobblestoneGeneratorTile extends TileEntityInventory implements ITickableTileEntity {

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
            public BlockCobblestoneGeneratorContainer(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
            super(Registration.COB_GENERATOR_CONTAINER.get(), windowId, world, pos, playerInventory, player);
        }

    public BlockCobblestoneGeneratorContainer(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player, IIntArray fields) {
            super(Registration.COB_GENERATOR_CONTAINER.get(), windowId, world, pos, playerInventory, player, fields);
        }
    }
    public final IIntArray fields = new IIntArray() {
        public int get(int index) {

            if (index == 0)
            return BlockCobblestoneGeneratorTile.this.cobTime;
            if (index == 1)
                return BlockCobblestoneGeneratorTile.this.resultType;
            if (index == 2)
                return BlockCobblestoneGeneratorTile.this.actualCobTime;
            else return 0;
        }

        public void set(int index, int value) {
            if (index == 0)
            BlockCobblestoneGeneratorTile.this.cobTime = value;
            if (index == 1)
                BlockCobblestoneGeneratorTile.this.resultType = value;
            if (index == 2)
                BlockCobblestoneGeneratorTile.this.actualCobTime = value;
        }

        @Override
        public int getCount() {
            return 3;
        }
    };
    @Override
    public Container IcreateMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
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

    public static class BlockCobblestoneGeneratorTileDefinition extends wily.betterfurnaces.tileentity.BlockCobblestoneGeneratorTile{
        public BlockCobblestoneGeneratorTileDefinition() {
            super(Registration.COB_GENERATOR_TILE.get());
        }

    }
    public BlockCobblestoneGeneratorTile(TileEntityType<?> tileentitytypeIn) {
        super(tileentitytypeIn, 5);

    }

    public void forceUpdateAllStates() {
        BlockState state = level.getBlockState(worldPosition);
        if (state.getValue(BlockCobblestoneGenerator.TYPE) != cobGen()) {
            level.setBlock(worldPosition, state.setValue(BlockCobblestoneGenerator.TYPE, cobGen()), 3);
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
        ItemStack output = this.getItem(2);
        ItemStack upgrade = this.getItem(3);
        ItemStack upgrade1 = this.getItem(4);
        boolean can = (output.getCount() + 1 <= output.getMaxStackSize());
        boolean can1 = (output.isEmpty());
        boolean can3 = (output.getItem() == getResult().getItem());
        forceUpdateAllStates();
        if ((cobGen() == 3) || cobTime > 0 && cobTime < actualCobTime) {
            if ((can && can3 )|| can1)
            ++this.cobTime;
        }
        if (!this.level.isClientSide) {
            if (!output.isEmpty()) AutoIO();
            if ((cobTime >= getCobTime() && ((can  && can3)|| can1))){
                if (can1) {
                    this.inventory.set(OUTPUT, getResult());
                    if (upgrade1.getItem() instanceof ItemUpgradeOreProcessing) {
                        breakDurabilityItem(upgrade1);
                    }
                }else {
                    if (can && can3) {
                        output.grow(getResult().getCount());
                        if (upgrade1.getItem() instanceof ItemUpgradeOreProcessing) {
                            breakDurabilityItem(upgrade1);
                        }
                    }
                }
                this.getLevel().playSound(null, this.getBlockPos(), SoundEvents.LAVA_EXTINGUISH, SoundCategory.BLOCKS, 0.3F, 0.3F);
                cobTime = 0;
                breakDurabilityItem(upgrade);
                AutoIO();
            }
        }

    }
    protected int cobGen(){
        ItemStack input = this.inventory.get(0);
        ItemStack input1 = this.inventory.get(1);
        if (input.getItem() == Items.LAVA_BUCKET.getItem() && input1.isEmpty()){
            return 1;
        }else if (input1.getItem() == Items.WATER_BUCKET.getItem() && input.isEmpty()){
            return 2;
        }else if (input.getItem() == Items.LAVA_BUCKET.getItem() && input1.getItem() == Items.WATER_BUCKET.getItem()){
            return 3;
        }else return 0;
    }
    protected int getCobTime(){
        ItemStack upgrade = this.inventory.get(UPGRADE);
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


    net.minecraftforge.common.util.LazyOptional<? extends net.minecraftforge.items.IItemHandler>[] invHandlers =
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
        if (index == OUTPUT || index == 3) {
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

    protected boolean doesNeedUpdateSend() {
        return !Arrays.equals(this.provides, this.lastProvides);
    }

    public void onUpdateSent() {
        System.arraycopy(this.provides, 0, this.lastProvides, 0, this.provides.length);
        this.level.updateNeighborsAt(this.worldPosition, getBlockState().getBlock());
    }

}
