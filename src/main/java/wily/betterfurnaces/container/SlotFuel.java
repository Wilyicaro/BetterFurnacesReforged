package wily.betterfurnaces.container;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import wily.betterfurnaces.init.Registration;
import wily.betterfurnaces.tileentity.BlockSmeltingTileBase;

public class SlotFuel extends Slot {

    BlockSmeltingTileBase be;
    public SlotFuel(IInventory te, int index, int x, int y) {
        super(te, index, x, y);
        if (te instanceof  BlockSmeltingTileBase)
            be = (BlockSmeltingTileBase) te;
    }

    /**
     * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
     */
    public boolean mayPlace(ItemStack stack) {
        return BlockSmeltingTileBase.isItemFuel(stack) || (stack.getCapability(CapabilityEnergy.ENERGY).isPresent() && be.hasUpgrade(Registration.ENERGY.get())) || isEmptyContainer(stack) ;
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return  isEmptyContainer(stack) ? 1 : super.getMaxStackSize(stack);
    }

    public static boolean isEmptyContainer(ItemStack stack) {
        return stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).isPresent() && !FluidUtil.getFluidContained(stack).isPresent();
    }
}
