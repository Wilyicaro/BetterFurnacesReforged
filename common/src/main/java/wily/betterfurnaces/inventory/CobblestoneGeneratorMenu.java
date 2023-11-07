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



    public CobblestoneGeneratorMenu( int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player) {
        this( windowId, world, pos, playerInventory, player, new SimpleContainerData(3));
    }

    public CobblestoneGeneratorMenu(int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player, ContainerData fields) {
        super(ModObjects.COB_GENERATOR_CONTAINER.get(), windowId, world, pos, playerInventory, player, fields);
        checkContainerDataCount(this.fields, 3);
    }


    public BlockPos getPos() {
        return this.be.getBlockPos();
    }

    public int getCobTimeScaled(int pixels) {
        int i = this.fields.get(0);
        int j = this.fields.get(2);
        return j != 0 && i != 0 ? i * pixels / j : 0;
    }


}
