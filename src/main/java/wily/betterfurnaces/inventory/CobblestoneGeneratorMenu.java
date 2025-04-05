package wily.betterfurnaces.inventory;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.level.Level;
import wily.betterfurnaces.blockentity.CobblestoneGeneratorBlockEntity;
import wily.betterfurnaces.init.ModObjects;


public class CobblestoneGeneratorMenu extends AbstractInventoryMenu<CobblestoneGeneratorBlockEntity> {
    public CobblestoneGeneratorMenu(int windowId, Level world, BlockPos pos, Inventory playerInventory) {
        this(windowId, world, pos, playerInventory, new SimpleContainerData(4));
    }

    public CobblestoneGeneratorMenu(int windowId, Level world, BlockPos pos, Inventory playerInventory, ContainerData fields) {
        super(ModObjects.COB_GENERATOR_CONTAINER.get(), windowId, world, pos, playerInventory, fields);
        checkContainerDataCount(this.data, 4);
    }

    public int getCobTimeScaled(int pixels) {
        int i = this.data.get(0);
        int j = this.data.get(2);
        return j != 0 && i != 0 ? i * pixels / j : 0;
    }


    public boolean hasAutoOutput(){
        return data.get(3) == 1;
    }

}
