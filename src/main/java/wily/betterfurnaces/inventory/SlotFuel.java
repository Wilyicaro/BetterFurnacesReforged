package wily.betterfurnaces.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import wily.betterfurnaces.blockentity.AbstractSmeltingBlockEntity;
import wily.betterfurnaces.init.Registration;

public class SlotFuel extends Slot {
    AbstractSmeltingBlockEntity be;
    public SlotFuel(Container be, int index, int x, int y) {
        super(be, index, x, y);
        if (be instanceof AbstractSmeltingBlockEntity)
            be = (AbstractSmeltingBlockEntity) be;
    }

    /**
     * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
     */
    public boolean mayPlace(ItemStack stack) {
        return AbstractSmeltingBlockEntity.isItemFuel(stack) || (stack.getCapability(ForgeCapabilities.ENERGY).isPresent() && be.hasUpgrade(Registration.ENERGY.get())) || isContainer(stack) ;
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return  isContainer(stack) ? 1 : super.getMaxStackSize(stack);
    }

    public static boolean isContainer(ItemStack stack) {
        return stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent();
    }
}
