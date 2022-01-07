package wily.betterfurnaces.container;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import wily.betterfurnaces.items.ItemUpgrade;
import wily.betterfurnaces.items.ItemUpgradeEnergyFuel;
import wily.betterfurnaces.items.ItemUpgradeLiquidFuel;
import wily.betterfurnaces.tileentity.BlockForgeTileBase;

public class SlotHeater extends Slot {

    private BlockForgeTileBase tf;

    public SlotHeater(IInventory te, int slotIndex, int xPosition, int yPosition) {
        super(te, slotIndex, xPosition, yPosition);
            this.tf = (BlockForgeTileBase) te;
    }

    /**
     * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
     */
    public boolean mayPlace(ItemStack stack) {
        return stack.getItem() instanceof ItemUpgrade && ((ItemUpgrade) stack.getItem()).upgradeType == 1;
    }


    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public void setChanged() {
            tf.onUpdateSent();
    }

}
