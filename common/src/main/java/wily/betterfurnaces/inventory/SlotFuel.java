package wily.betterfurnaces.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import wily.betterfurnaces.blockentity.AbstractSmeltingBlockEntity;
import wily.betterfurnaces.blockentity.InventoryBlockEntity;
import wily.betterfurnaces.init.Registration;
import wily.factoryapi.ItemContainerUtil;

public class SlotFuel extends Slot {

    InventoryBlockEntity be;
    public SlotFuel(InventoryBlockEntity be, int index, int x, int y) {
        super(be.inventory, index, x, y);
        this.be = be;
    }

    /**
     * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
     */
    public boolean mayPlace(ItemStack stack) {
        return AbstractSmeltingBlockEntity.isItemFuel(stack) || ( be instanceof AbstractSmeltingBlockEntity smeltBe && ItemContainerUtil.isEnergyContainer(stack) && smeltBe.hasUpgrade(Registration.ENERGY.get())) ||  ItemContainerUtil.isFluidContainer(stack) ;
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return  ItemContainerUtil.isFluidContainer(stack) ? 1 : super.getMaxStackSize(stack);
    }


}
