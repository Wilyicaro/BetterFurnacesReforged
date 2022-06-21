package wily.betterfurnaces.container;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import wily.betterfurnaces.blockentity.BlockEntitySmeltingBase;
import wily.betterfurnaces.init.Registration;

public class SlotFuel extends Slot {
    BlockEntitySmeltingBase be;
    public SlotFuel(Container te, int index, int x, int y) {
        super(te, index, x, y);
        if (te instanceof  BlockEntitySmeltingBase)
            be = (BlockEntitySmeltingBase) te;
    }

    /**
     * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
     */
    public boolean mayPlace(ItemStack stack) {
        return BlockEntitySmeltingBase.isItemFuel(stack) || (stack.getCapability(CapabilityEnergy.ENERGY).isPresent() && be.hasUpgrade(Registration.ENERGY.get())) || isEmptyContainer(stack) ;
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return  isEmptyContainer(stack) ? 1 : super.getMaxStackSize(stack);
    }

    public static boolean isEmptyContainer(ItemStack stack) {
        return stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).isPresent() && FluidUtil.getFluidContained(stack).isEmpty();
    }
}
