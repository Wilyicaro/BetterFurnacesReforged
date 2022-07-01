package wily.betterfurnaces.tileentity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;

public interface IInventoryTileEntity {

    int[] IgetSlotsForFace(Direction side);

    boolean IcanExtractItem(int index, ItemStack stack, Direction direction);

    String IgetName();

    boolean IisItemValidForSlot(int index, ItemStack stack);

    Container IcreateMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity);


}
