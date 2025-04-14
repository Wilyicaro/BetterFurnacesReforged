package wily.betterfurnaces.inventory;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.level.Level;
import wily.betterfurnaces.blockentity.FuelVerifierBlockEntity;
import wily.betterfurnaces.init.ModObjects;
import wily.factoryapi.base.FuelManager;

import java.util.Comparator;


public class FuelVerifierMenu extends AbstractInventoryMenu<FuelVerifierBlockEntity> {
    static int maxFuelValue = FuelManager.getMap().values().stream().max(Comparator.comparing(Integer::intValue)).orElse(20000);

    public FuelVerifierMenu(int windowId, Level world, BlockPos pos, Inventory playerInventory) {
        this(windowId, world, pos, playerInventory, new SimpleContainerData(1));
    }

    public FuelVerifierMenu( int windowId, Level world, BlockPos pos, Inventory playerInventory, ContainerData fields) {
        super(ModObjects.FUEL_VERIFIER_CONTAINER.get(), windowId, world, pos, playerInventory, fields);
        checkContainerDataCount(this.data, 1);
    }

    public int getBurnTimeScaled(int pixels) {
        return this.data.get(0) * pixels / maxFuelValue;
    }

    public float getBurnTime() {
        return (float) this.data.get(0) / 200;
    }
}