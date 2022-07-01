package wily.betterfurnaces.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import wily.betterfurnaces.init.Registration;
import wily.betterfurnaces.tileentity.AbstractSmeltingTileEntity;

public class SlotFuel extends Slot {

    AbstractSmeltingTileEntity be;

    public SlotFuel(IInventory te, int index, int x, int y) {
        super(te, index, x, y);
        if (te instanceof AbstractSmeltingTileEntity)
            be = (AbstractSmeltingTileEntity) te;
    }

    public static boolean isContainer(ItemStack stack) {
        return stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).isPresent();
    }

    /**
     * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
     */
    public boolean mayPlace(ItemStack stack) {
        return ((AbstractSmeltingTileEntity.isItemFuel(stack) || (stack.getCapability(CapabilityEnergy.ENERGY).isPresent() && be.hasUpgrade(Registration.ENERGY.get())) || isContainer(stack)));
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return isContainer(stack) ? 1 : super.getMaxStackSize(stack);
    }
}
