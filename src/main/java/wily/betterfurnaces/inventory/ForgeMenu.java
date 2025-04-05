package wily.betterfurnaces.inventory;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import wily.betterfurnaces.init.ModObjects;


public class ForgeMenu extends SmeltingMenu {

    public ForgeMenu(int windowId, Level world, BlockPos pos, Inventory playerInventory) {
        super(ModObjects.FORGE_CONTAINER.get(), windowId, world, pos, playerInventory);
    }

    public ForgeMenu(int windowId, Level world, BlockPos pos, Inventory playerInventory, ContainerData fields) {
        super(ModObjects.FORGE_CONTAINER.get(), windowId, world, pos, playerInventory, fields);
    }
    @Override
    public void addInventorySlots(){
        TOP_ROW = 126;
        super.addInventorySlots();
    }
}
