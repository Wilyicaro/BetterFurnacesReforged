package wily.betterfurnaces.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import wily.betterfurnaces.init.Registration;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;

public class BlockFuelVerifierTile extends TileEntityInventory implements ITickableTileEntity {

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
        return "block.betterfurnacesreforged.fuel_verifier";
    }

    public static class BlockFuelVerifierTileContainer extends wily.betterfurnaces.container.BlockFuelVerifierContainer {
            public BlockFuelVerifierTileContainer(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
            super(Registration.FUEL_VERIFIER_CONTAINER.get(), windowId, world, pos, playerInventory, player);
        }

    public BlockFuelVerifierTileContainer(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player, IIntArray fields) {
            super(Registration.FUEL_VERIFIER_CONTAINER.get(), windowId, world, pos, playerInventory, player, fields);
        }
    }
    public final IIntArray fields = new IIntArray() {
        public int get(int index) {
            if (index == 0)
            return BlockFuelVerifierTile.this.burnTime;
            else return 0;
        }

        public void set(int index, int value) {
            BlockFuelVerifierTile.this.burnTime = value;
        }

        @Override
        public int getCount() {
            return 1;
        }
    };
    @Override
    public Container IcreateMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new BlockFuelVerifierTileContainer(i, level, worldPosition, playerInventory, playerEntity, this.fields);
    }
    public final int[] provides = new int[Direction.values().length];
    private final int[] lastProvides = new int[this.provides.length];

    /**
     * The number of ticks that the furnace will keep burning
     */
    private int burnTime;

    public static class BlockFuelVerifierTileDefinition extends BlockFuelVerifierTile {
        public BlockFuelVerifierTileDefinition() {
            super(Registration.FUEL_VERIFIER_TILE.get());
        }

    }
    public BlockFuelVerifierTile(TileEntityType<?> tileentitytypeIn) {
        super(tileentitytypeIn, 1);

    }

    @Override
    public void tick() {
        ItemStack fuel = this.getItem(0);
        if (!this.level.isClientSide) {
            if (!(fuel.isEmpty()))
            this.burnTime = getBurnTime(fuel);
            else burnTime = 0;
        }

    }
    @Override
    public void load(BlockState state, CompoundNBT tag) {
        ItemStackHelper.loadAllItems(tag, this.inventory);
        this.burnTime = tag.getInt("BurnTime");
        super.load(state, tag);
    }

    @Override
    public CompoundNBT save(CompoundNBT tag) {
        super.save(tag);
        ItemStackHelper.saveAllItems(tag, this.inventory);
        tag.putInt("BurnTime", this.burnTime);
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

    @Nonnull
    @Override
    public <
            T> net.minecraftforge.common.util.LazyOptional<T> getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @Nullable Direction facing) {

        if (!this.isRemoved() && facing != null && capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return LazyOptional.of(() -> inventory).cast();
        }
        return super.getCapability(capability, facing);
    }


    @Override
    public boolean IisItemValidForSlot(int index, ItemStack stack) {
        if (index == 0) {
            return isItemFuel(stack);
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
