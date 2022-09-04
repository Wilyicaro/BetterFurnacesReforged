package wily.ultimatefurnaces.inventory;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import wily.betterfurnaces.inventory.AbstractForgeMenu;
import wily.ultimatefurnaces.init.RegistrationUF;

public class UltimateForgeMenu extends AbstractForgeMenu {

    public UltimateForgeMenu(int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player) {
        super(RegistrationUF.ULTIMATE_FORGE_CONTAINER.get(), windowId, world, pos, playerInventory, player);
    }

    public UltimateForgeMenu(int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player, ContainerData fields) {
        super(RegistrationUF.ULTIMATE_FORGE_CONTAINER.get(), windowId, world, pos, playerInventory, player, fields);
    }


    @Override
    public boolean stillValid(Player playerIn) {
        return stillValid(ContainerLevelAccess.create(be.getLevel(), be.getBlockPos()), playerEntity, RegistrationUF.ULTIMATE_FORGE.get());
    }
}
