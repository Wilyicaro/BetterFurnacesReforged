package wily.betterfurnaces.blockentity;

import com.ibm.icu.impl.Pair;
import me.shedaniel.architectury.registry.fuel.FuelRegistry;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import wily.betterfurnaces.init.Registration;
import wily.betterfurnaces.inventory.FuelVerifierMenu;
import wily.betterfurnaces.inventory.SlotFuel;
import wily.factoryapi.base.IPlatformHandlerApi;
import wily.factoryapi.base.Storages;
import wily.factoryapi.base.TransportState;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Optional;

public class FuelVerifierBlockEntity extends InventoryBlockEntity {

    @Override
    public Map.Entry<int[], TransportState> getSlotsTransport(Direction side) {
        return new AbstractMap.SimpleEntry<>( new int[0],TransportState.EXTRACT);
    }

    @Override
    public boolean IcanExtractItem(int index, ItemStack stack) {
        return false;
    }

    public final ContainerData fields = new ContainerData() {
        public int get(int index) {
            if (index == 0)
            return FuelVerifierBlockEntity.this.burnTime;
            else return 0;
        }

        public void set(int index, int value) {
            FuelVerifierBlockEntity.this.burnTime = value;
        }

        @Override
        public int getCount() {
            return 1;
        }
    };
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
        return new FuelVerifierMenu(i, level, worldPosition, playerInventory, playerEntity, this.fields);
    }

    /**
     * The number of ticks that the furnace will keep burning
     */
    private int burnTime;


    public FuelVerifierBlockEntity() {
        super(Registration.FUEL_VERIFIER_TILE.get());

    }

    public void tick() {
        if (!level.isClientSide) {
            ItemStack fuel = inventory.getItem(0);
            if (!(fuel.isEmpty()))
            burnTime = getBurnTime(fuel);
            else burnTime = 0;
        }

    }
    @Override
    public void load(BlockState blockState,CompoundTag tag) {
        this.burnTime = tag.getInt("BurnTime");
        super.load(blockState ,tag);
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putInt("BurnTime", this.burnTime);
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

    @Override
    public void addSlots(NonNullList<Slot> slots, @Nullable Player player) {
        slots.add(new SlotFuel(this, 0, 80, 48));
    }


}
