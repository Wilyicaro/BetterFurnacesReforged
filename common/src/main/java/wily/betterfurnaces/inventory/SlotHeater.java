package wily.betterfurnaces.inventory;

import net.minecraft.world.item.ItemStack;
import wily.betterfurnaces.blockentity.SmeltingBlockEntity;
import wily.betterfurnaces.items.UpgradeItem;
import wily.factoryapi.base.FactoryItemSlot;
import wily.factoryapi.base.SlotsIdentifier;
import wily.factoryapi.base.TransportState;

public class SlotHeater extends FactoryItemSlot {

    private SmeltingBlockEntity tf;

    public SlotHeater(SmeltingBlockEntity be, int slotIndex, int xPosition, int yPosition) {
        super(be, SlotsIdentifier.GENERIC, TransportState.EXTRACT_INSERT, slotIndex, xPosition, yPosition);
        this.tf =  be;
    }

    public boolean mayPlace(ItemStack stack) {
        return stack.getItem() instanceof UpgradeItem && ((UpgradeItem) stack.getItem()).upgradeType == 1;
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
