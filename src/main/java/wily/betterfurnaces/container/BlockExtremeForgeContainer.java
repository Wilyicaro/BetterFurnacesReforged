package wily.betterfurnaces.container;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import wily.betterfurnaces.init.Registration;

public class BlockExtremeForgeContainer extends BlockForgeContainerBase {

    public BlockExtremeForgeContainer(int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player) {
        super(Registration.EXTREME_FORGE_CONTAINER.get(), windowId, world, pos, playerInventory, player);
    }

    public BlockExtremeForgeContainer(int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player, ContainerData fields) {
        super(Registration.EXTREME_FORGE_CONTAINER.get(), windowId, world, pos, playerInventory, player, fields);
    }


    @Override
    public boolean stillValid(Player playerIn) {
        return stillValid(ContainerLevelAccess.create(te.getLevel(), te.getBlockPos()), playerEntity, Registration.EXTREME_FORGE.get());
    }
}
