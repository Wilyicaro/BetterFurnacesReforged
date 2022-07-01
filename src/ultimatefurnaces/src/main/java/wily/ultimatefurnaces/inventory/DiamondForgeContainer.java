package wily.ultimatefurnaces.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wily.betterfurnaces.inventory.AbstractForgeContainer;
import wily.ultimatefurnaces.init.RegistrationUF;

public class DiamondForgeContainer extends AbstractForgeContainer {

    public DiamondForgeContainer(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
        super(RegistrationUF.DIAMOND_FORGE_CONTAINER.get(), windowId, world, pos, playerInventory, player);
    }

    public DiamondForgeContainer(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player, IIntArray fields) {
        super(RegistrationUF.DIAMOND_FORGE_CONTAINER.get(), windowId, world, pos, playerInventory, player, fields);
    }


    @Override
    public boolean stillValid(PlayerEntity playerIn) {
        return stillValid(IWorldPosCallable.create(te.getLevel(), te.getBlockPos()), playerEntity, RegistrationUF.DIAMOND_FORGE.get());
    }
}
