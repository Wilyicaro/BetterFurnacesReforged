package wily.betterfurnaces.inventory;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import wily.betterfurnaces.init.Registration;


public class ForgeMenu extends SmeltingMenu {

    public ForgeMenu( int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player) {
        super(Registration.FORGE_CONTAINER.get(), windowId, world, pos, playerInventory, player);
    }

    public ForgeMenu( int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player, ContainerData fields) {
        super(Registration.FORGE_CONTAINER.get(), windowId, world, pos, playerInventory, player, fields);
    }
    @Override
    public void addInventorySlots(){
        TOP_ROW = 126;
        super.addInventorySlots();
    }
}
