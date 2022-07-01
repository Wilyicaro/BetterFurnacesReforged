package wily.ultimatefurnaces.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wily.betterfurnaces.inventory.AbstractFurnaceContainer;
import wily.ultimatefurnaces.init.RegistrationUF;

public class CopperFurnaceContainer extends AbstractFurnaceContainer {

    public CopperFurnaceContainer(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
        super(RegistrationUF.COPPER_FURNACE_CONTAINER.get(), windowId, world, pos, playerInventory, player);
    }

    public CopperFurnaceContainer(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player, IIntArray fields) {
        super(RegistrationUF.COPPER_FURNACE_CONTAINER.get(), windowId, world, pos, playerInventory, player, fields);
    }


    @Override
    public boolean stillValid(PlayerEntity playerIn) {
        return stillValid(IWorldPosCallable.create(te.getLevel(), te.getBlockPos()), playerEntity, RegistrationUF.COPPER_FURNACE.get());
    }
}
