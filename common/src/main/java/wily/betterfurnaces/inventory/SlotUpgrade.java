package wily.betterfurnaces.inventory;

import net.minecraft.world.item.ItemStack;
import wily.betterfurnaces.blockentity.InventoryBlockEntity;
import wily.betterfurnaces.blockentity.SmeltingBlockEntity;
import wily.betterfurnaces.items.UpgradeItem;
import wily.factoryapi.base.FactoryItemSlot;
import wily.factoryapi.base.SlotsIdentifier;
import wily.factoryapi.base.TransportState;

public class SlotUpgrade extends FactoryItemSlot {

    private final InventoryBlockEntity be;

    public SlotUpgrade(InventoryBlockEntity be, int slotIndex, int xPosition, int yPosition) {
        super(be.inventory, SlotsIdentifier.GENERIC, TransportState.EXTRACT_INSERT, slotIndex, xPosition, yPosition);
        this.be =  be;

    }

    public boolean mayPlace(ItemStack stack) {
        return (be instanceof SmeltingBlockEntity s && stack.getItem() instanceof UpgradeItem upg && !s.hasUpgrade(upg)  && (!s.isForge() || upg.upgradeType != 1) && upg.isValid(s) && (!s.hasUpgradeType((UpgradeItem) stack.getItem()) || (be.inventory.getItem(index).getItem() instanceof UpgradeItem upg2 && upg2.isSameType(upg))));
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public void setChanged() {
        if (be instanceof SmeltingBlockEntity smeltingBe) {
            smeltingBe.onUpdateSent();
        }
    }

}
