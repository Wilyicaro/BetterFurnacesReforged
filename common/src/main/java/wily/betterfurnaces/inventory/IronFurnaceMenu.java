package wily.betterfurnaces.inventory;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import wily.betterfurnaces.init.Registration;

public class IronFurnaceMenu extends AbstractFurnaceMenu {

    public IronFurnaceMenu(int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player) {
        super(Registration.IRON_FURNACE_CONTAINER.get(), windowId, world, pos, playerInventory, player);
    }

    public IronFurnaceMenu(int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player, ContainerData fields) {
        super(Registration.IRON_FURNACE_CONTAINER.get(), windowId, world, pos, playerInventory, player, fields);
    }
    @Override
    public boolean stillValid(Player playerIn) {
        return stillValid(ContainerLevelAccess.create(be.getLevel(), be.getBlockPos()), playerEntity, Registration.IRON_FURNACE.get());
    }

}
