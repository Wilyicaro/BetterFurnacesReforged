package wily.betterfurnaces.inventory;

import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import wily.betterfurnaces.blockentity.ForgeBlockEntity;
import wily.betterfurnaces.blockentity.InventoryBlockEntity;
import wily.betterfurnaces.blockentity.SmeltingBlockEntity;
import wily.betterfurnaces.items.UpgradeItem;

public class SlotUpgrade extends Slot {

    private final InventoryBlockEntity be;

    public SlotUpgrade(InventoryBlockEntity be, int slotIndex, int xPosition, int yPosition) {
        super(be.inventory, slotIndex, xPosition, yPosition);
        this.be =  be;

    }


    public boolean mayPlace(ItemStack stack) {
        return (be instanceof SmeltingBlockEntity && stack.getItem() instanceof UpgradeItem && (!(be instanceof ForgeBlockEntity) || ((UpgradeItem)stack.getItem()).upgradeType != 1) && !((SmeltingBlockEntity) be).hasUpgrade((UpgradeItem) stack.getItem()) && ((UpgradeItem) stack.getItem()).isValid((SmeltingBlockEntity) be) && (!((SmeltingBlockEntity) be).hasUpgradeType((UpgradeItem) stack.getItem()) || (be.inventory.getItem(index).getItem() instanceof UpgradeItem  && ((UpgradeItem) be.inventory.getItem(index).getItem()).isSameType((UpgradeItem) stack.getItem()))));
    }


    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public void setChanged() {
        if (be instanceof SmeltingBlockEntity) {
            ((SmeltingBlockEntity) be).onUpdateSent();
        }
    }

}
