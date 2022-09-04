package wily.betterfurnaces.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wily.betterfurnaces.init.Registration;

public class DiamondFurnaceContainer extends AbstractFurnaceContainer {

    public DiamondFurnaceContainer(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
        super(Registration.DIAMOND_FURNACE_CONTAINER.get(), windowId, world, pos, playerInventory, player);
    }

    public DiamondFurnaceContainer(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player, IIntArray fields) {
        super(Registration.DIAMOND_FURNACE_CONTAINER.get(), windowId, world, pos, playerInventory, player, fields);
    }


}
