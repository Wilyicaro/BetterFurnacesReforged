package wily.ultimatefurnaces.container;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import wily.betterfurnaces.container.BlockFurnaceContainerBase;
import wily.ultimatefurnaces.init.Registration;

public class BlockUltimateFurnaceContainer extends BlockFurnaceContainerBase {

    public BlockUltimateFurnaceContainer(int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player) {
        super(wily.ultimatefurnaces.init.Registration.ULTIMATE_FURNACE_CONTAINER.get(), windowId, world, pos, playerInventory, player);
    }

    public BlockUltimateFurnaceContainer(int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player, ContainerData fields) {
        super(wily.ultimatefurnaces.init.Registration.ULTIMATE_FURNACE_CONTAINER.get(), windowId, world, pos, playerInventory, player, fields);
    }


    @Override
    public boolean stillValid(Player playerIn) {
        return stillValid(ContainerLevelAccess.create(te.getLevel(), te.getBlockPos()), playerEntity, Registration.ULTIMATE_FURNACE.get());
    }
}
