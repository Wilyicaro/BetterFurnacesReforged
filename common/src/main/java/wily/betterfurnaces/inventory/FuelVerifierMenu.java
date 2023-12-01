package wily.betterfurnaces.inventory;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import wily.betterfurnaces.blockentity.FuelVerifierBlockEntity;
import wily.betterfurnaces.init.ModObjects;

import java.util.Comparator;


public class FuelVerifierMenu extends AbstractInventoryMenu<FuelVerifierBlockEntity> {


    public FuelVerifierMenu(int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player) {
        this( windowId, world, pos, playerInventory, player, new SimpleContainerData(1));
    }

    public FuelVerifierMenu( int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player, ContainerData fields) {
        super(ModObjects.FUEL_VERIFIER_CONTAINER.get(), windowId, world, pos, playerInventory, player, fields);
        checkContainerDataCount(this.fields, 1);
    }


    public BlockPos getPos() {
        return this.be.getBlockPos();
    }

    static int maxFuelValue = FurnaceBlockEntity.getFuel().values().stream().max(Comparator.comparing(Integer::intValue)).orElse(20000);

    public int getBurnTimeScaled(int pixels) {
        return this.fields.get(0) * pixels / maxFuelValue;
    }
    public float getBurnTime() {
        return (float) this.fields.get(0) / 200;
    }


}
