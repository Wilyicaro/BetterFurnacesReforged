package wily.betterfurnaces.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import wily.betterfurnaces.blockentity.AbstractSmeltingBlockEntity;

import java.util.function.Predicate;

public class SlotInput extends HideableSlot {

    public SlotInput(AbstractSmeltingBlockEntity be, int index, int x, int y) {
        super(be, index, x, y);
    }
    public SlotInput(AbstractSmeltingBlockEntity be, int index, int x, int y, Predicate<Slot> isActive) {
        super(be, index, x, y, isActive);
    }


    /**
     * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
     */
    public boolean mayPlace(ItemStack stack) {
            return !(be instanceof AbstractSmeltingBlockEntity sBe) || sBe.hasRecipe(stack);
    }
}
