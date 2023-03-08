package wily.betterfurnaces.inventory;

import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import wily.betterfurnaces.blockentity.SmeltingBlockEntity;

import java.util.function.Predicate;

public class SlotInput extends HideableSlot {

    public SlotInput(SmeltingBlockEntity be, int index, int x, int y) {
        super(be, index, x, y);
    }
    public SlotInput(SmeltingBlockEntity be, int index, int x, int y, Predicate<Slot> isActive) {
        super(be, index, x, y, isActive);
    }


    /**
     * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
     */
    public boolean mayPlace(ItemStack stack) {
            return !(be instanceof SmeltingBlockEntity sBe) || sBe.hasRecipe(stack);
    }
}
