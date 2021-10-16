package wily.betterfurnaces.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import wily.betterfurnaces.init.Registration;

public class BlockIronFurnaceContainer extends BlockFurnaceContainerBase {

    public BlockIronFurnaceContainer(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
        super(Registration.IRON_FURNACE_CONTAINER.get(), windowId, world, pos, playerInventory, player);
    }

    public BlockIronFurnaceContainer(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player, IIntArray fields) {
        super(Registration.IRON_FURNACE_CONTAINER.get(), windowId, world, pos, playerInventory, player, fields);
    }
    @Override
    public boolean stillValid(PlayerEntity playerIn) {
        return stillValid(IWorldPosCallable.create(te.getLevel(), te.getBlockPos()), playerEntity, Registration.IRON_FURNACE.get());
    }

}
