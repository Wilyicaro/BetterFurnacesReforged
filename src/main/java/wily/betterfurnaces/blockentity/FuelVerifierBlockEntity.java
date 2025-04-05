package wily.betterfurnaces.blockentity;

import it.unimi.dsi.fastutil.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import wily.betterfurnaces.init.BlockEntityTypes;
import wily.betterfurnaces.inventory.FuelVerifierMenu;
import wily.betterfurnaces.inventory.SlotFuel;
import wily.factoryapi.base.*;
import wily.factoryapi.util.CompoundTagUtil;

public class FuelVerifierBlockEntity extends InventoryBlockEntity {

    @Override
    public Pair<int[], TransportState> getSlotsTransport(Direction side) {
        return Pair.of(new int[0],TransportState.EXTRACT);
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
            burnTime = value;
        }

        @Override
        public int getCount() {
            return 1;
        }
    };
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory playerInventory) {
        return new FuelVerifierMenu(i, level, worldPosition, playerInventory, this.fields);
    }

    /**
     * The number of ticks that the furnace will keep burning
     */
    private int burnTime;


    public FuelVerifierBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityTypes.FUEL_VERIFIER_TILE.get(), pos, state);

    }

    public void tick(BlockState blockState) {
        if (!level.isClientSide) {
            ItemStack fuel = inventory.getItem(0);
            if (!(fuel.isEmpty()))
            burnTime = FuelManager.getBurnTime(fuel);
            else burnTime = 0;
        }
    }

    public void /*? if <1.20.5 {*//*load(CompoundTag tag)*//*?} else {*/loadAdditional(CompoundTag tag, HolderLookup.Provider provider)/*?}*/ {
        super./*? if <1.20.5 {*//*load(tag)*//*?} else {*/loadAdditional(tag, provider)/*?}*/;
        this.burnTime = CompoundTagUtil.getInt(tag, "BurnTime").orElse(0);
    }

    @Override
    public void saveAdditional(CompoundTag tag/*? if >=1.20.5 {*/, HolderLookup.Provider provider/*?}*/) {
        tag.putInt("BurnTime", this.burnTime);
        super.saveAdditional(tag/*? if >=1.20.5 {*/, provider/*?}*/);
    }


    @Override
    public <T extends IPlatformHandler> ArbitrarySupplier<T> getStorage(FactoryStorage<T> storage, Direction facing) {
        if (!this.isRemoved() && storage == FactoryStorage.ITEM) {
            return ()->(T)inventory;
        }
        return ArbitrarySupplier.empty();
    }


    @Override
    public void addSlots(NonNullList<FactoryItemSlot> slots, @Nullable Player player) {
        slots.add(new SlotFuel(this, 0, 80, 48));
    }


}
