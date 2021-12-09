package wily.betterfurnaces.tileentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import wily.betterfurnaces.init.Registration;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;

public class BlockFuelVerifierTile extends TileEntityInventory {

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
            public BlockFuelVerifierTileContainer(int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player) {
            super(Registration.FUEL_VERIFIER_CONTAINER.get(), windowId, world, pos, playerInventory, player);
        }

    public BlockFuelVerifierTileContainer(int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player, ContainerData fields) {
            super(Registration.FUEL_VERIFIER_CONTAINER.get(), windowId, world, pos, playerInventory, player, fields);
        }
    }
    public final ContainerData fields = new ContainerData() {
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
    public AbstractContainerMenu IcreateMenu(int i, Inventory playerInventory, Player playerEntity) {
        return new BlockFuelVerifierTileContainer(i, level, worldPosition, playerInventory, playerEntity, this.fields);
    }
    public final int[] provides = new int[Direction.values().length];
    private final int[] lastProvides = new int[this.provides.length];

    /**
     * The number of ticks that the furnace will keep burning
     */
    private int burnTime;

    public static class BlockFuelVerifierTileDefinition extends BlockFuelVerifierTile {
        public BlockFuelVerifierTileDefinition(BlockPos pos, BlockState state) {
            super(Registration.FUEL_VERIFIER_TILE.get(), pos, state);
        }

    }
    public BlockFuelVerifierTile(BlockEntityType<?> tileentitytypeIn, BlockPos pos, BlockState state) {
        super(tileentitytypeIn, pos, state, 1);

    }

    public static void tick(Level level, BlockPos worldPosition, BlockState blockState, BlockFuelVerifierTile e) {
        ItemStack fuel = e.getItem(0);
        if (!e.level.isClientSide) {
            if (!(fuel.isEmpty()))
            e.burnTime = getBurnTime(fuel);
            else e.burnTime = 0;
        }

    }
    @Override
    public void load(CompoundTag tag) {
        ContainerHelper.loadAllItems(tag, this.inventory);
        this.burnTime = tag.getInt("BurnTime");
        super.load(tag);
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        ContainerHelper.saveAllItems(tag, this.inventory);
        tag.putInt("BurnTime", this.burnTime);
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
