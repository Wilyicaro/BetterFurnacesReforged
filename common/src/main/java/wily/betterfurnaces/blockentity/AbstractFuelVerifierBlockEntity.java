package wily.betterfurnaces.blockentity;

import dev.architectury.registry.fuel.FuelRegistry;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import wily.betterfurnaces.init.Registration;
import wily.betterfurnaces.inventory.AbstractFuelVerifierMenu;
import wily.factoryapi.base.IPlatformHandlerApi;
import wily.factoryapi.base.Storages;
import wily.factoryapi.base.TransportState;

import java.util.Optional;

public abstract class AbstractFuelVerifierBlockEntity extends InventoryBlockEntity {

    @Override
    public Pair<int[], TransportState> getSlotsTransport(Direction side) {
        return Pair.of( new int[0],TransportState.EXTRACT);
    }

    @Override
    public boolean IcanExtractItem(int index, ItemStack stack) {
        return false;
    }

    public final ContainerData fields = new ContainerData() {
        public int get(int index) {
            if (index == 0)
            return AbstractFuelVerifierBlockEntity.this.burnTime;
            else return 0;
        }

        public void set(int index, int value) {
            AbstractFuelVerifierBlockEntity.this.burnTime = value;
        }

        @Override
        public int getCount() {
            return 1;
        }
    };
    @Override
    public AbstractContainerMenu IcreateMenu(int i, Inventory playerInventory, Player playerEntity) {
        return new AbstractFuelVerifierMenu.FuelVerifierMenu(i, level, worldPosition, playerInventory, playerEntity, this.fields);
    }

    /**
     * The number of ticks that the furnace will keep burning
     */
    private int burnTime;

    public static class FuelVerifierBlockEntity extends AbstractFuelVerifierBlockEntity {
        public FuelVerifierBlockEntity(BlockPos pos, BlockState state) {
            super(Registration.FUEL_VERIFIER_TILE.get(), pos, state);
        }

    }
    public AbstractFuelVerifierBlockEntity(BlockEntityType<?> tileentitytypeIn, BlockPos pos, BlockState state) {
        super(tileentitytypeIn, pos, state, 1);

    }

    public void tick(BlockState blockState) {
        if (!level.isClientSide) {
            ItemStack fuel = inventory.getItem(0);
            if (!(fuel.isEmpty()))
            burnTime = getBurnTime(fuel);
            else burnTime = 0;
        }

    }
    @Override
    public void load(CompoundTag tag) {
        this.burnTime = tag.getInt("BurnTime");
        super.load(tag);
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        tag.putInt("BurnTime", this.burnTime);
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

    @Override
    public <T extends IPlatformHandlerApi> Optional<T> getStorage(Storages.Storage<T> storage, Direction facing) {
        if (!this.isRemoved() && facing != null && storage == Storages.ITEM) {
            return (Optional<T>) Optional.of(inventory);
        }
        return super.getStorage(storage, facing);
    }


    @Override
    public boolean IisItemValidForSlot(int index, ItemStack stack) {
        if (index == 0) {
            return isItemFuel(stack);
        }
        return false;
    }


}
