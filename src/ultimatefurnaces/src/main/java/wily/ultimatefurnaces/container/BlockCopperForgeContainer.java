package wily.ultimatefurnaces.container;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import wily.betterfurnaces.container.BlockForgeContainerBase;
import wily.ultimatefurnaces.init.RegistrationUF;

public class BlockCopperForgeContainer extends BlockForgeContainerBase {

    public BlockCopperForgeContainer(int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player) {
        super(RegistrationUF.COPPER_FORGE_CONTAINER.get(), windowId, world, pos, playerInventory, player);
    }

    public BlockCopperForgeContainer(int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player, ContainerData fields) {
        super(RegistrationUF.COPPER_FORGE_CONTAINER.get(), windowId, world, pos, playerInventory, player, fields);
    }


    @Override
    public boolean stillValid(Player playerIn) {
        return stillValid(ContainerLevelAccess.create(te.getLevel(), te.getBlockPos()), playerEntity, RegistrationUF.COPPER_FORGE.get());
    }
}
