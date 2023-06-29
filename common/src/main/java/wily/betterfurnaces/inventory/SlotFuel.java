package wily.betterfurnaces.inventory;

import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import wily.betterfurnaces.blockentity.InventoryBlockEntity;
import wily.betterfurnaces.blockentity.SmeltingBlockEntity;
import wily.betterfurnaces.init.Registration;
import wily.betterfurnaces.items.UpgradeItem;
import wily.factoryapi.ItemContainerUtil;

import java.util.function.Predicate;

public class SlotFuel extends HideableSlot {

    public SlotFuel(InventoryBlockEntity be, int index, int x, int y) {
        super(be, index, x, y);

    }
    public SlotFuel(InventoryBlockEntity be, int index, int x, int y, Predicate<Slot> isActive) {
        super(be, index, x, y,isActive);
    }

    /**
     * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
     */
    public boolean mayPlace(ItemStack stack) {
        return super.mayPlace(stack) && (!(stack.getItem() instanceof UpgradeItem) && (SmeltingBlockEntity.isItemFuel(stack) || ( be instanceof SmeltingBlockEntity smeltBe && ItemContainerUtil.isEnergyContainer(stack) && smeltBe.hasUpgrade(Registration.ENERGY.get())) ||  ItemContainerUtil.isFluidContainer(stack)));
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return  ItemContainerUtil.isFluidContainer(stack) ? 1 : super.getMaxStackSize(stack);
    }


}
