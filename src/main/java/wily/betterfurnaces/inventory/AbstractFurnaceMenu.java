package wily.betterfurnaces.inventory;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.level.Level;


public abstract class AbstractFurnaceMenu extends AbstractSmeltingContainer {

    public AbstractFurnaceMenu(MenuType<?> containerType, int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player) {
        super(containerType, windowId, world, pos, playerInventory, player);
    }

    public AbstractFurnaceMenu(MenuType<?> containerType, int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player, ContainerData fields) {
        super(containerType, windowId, world, pos, playerInventory, player, fields);
    }
}
