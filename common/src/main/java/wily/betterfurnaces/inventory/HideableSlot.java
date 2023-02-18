package wily.betterfurnaces.inventory;

import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import wily.betterfurnaces.blockentity.AbstractSmeltingBlockEntity;
import wily.betterfurnaces.blockentity.InventoryBlockEntity;

import java.util.function.Predicate;

public class HideableSlot extends Slot {
    protected InventoryBlockEntity be;
    protected Predicate<Slot> isActive = s->true;
    public HideableSlot(InventoryBlockEntity be, int index, int x, int y, Predicate<Slot> isActive) {
        this(be, index, x, y);
        this.isActive = isActive;
    }
    public HideableSlot(InventoryBlockEntity be, int index, int x, int y) {
        super(be.inventory, index, x, y);
        this.be = be;
    }

    @Override
    public boolean isActive() {
        return isActive.test(this);
    }

}
