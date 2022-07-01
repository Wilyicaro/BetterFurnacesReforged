package wily.betterfurnaces.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.IIntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public abstract class AbstractFurnaceContainer extends AbstractSmeltingContainer {


    public AbstractFurnaceContainer(ContainerType<?> containerType, int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
        super(containerType, windowId, world, pos, playerInventory, player);
    }

    public AbstractFurnaceContainer(ContainerType<?> containerType, int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player, IIntArray fields) {
        super(containerType, windowId, world, pos, playerInventory, player, fields);
    }
}
