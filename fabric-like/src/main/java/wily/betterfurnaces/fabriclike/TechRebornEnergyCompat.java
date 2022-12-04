package wily.betterfurnaces.fabriclike;

import net.minecraft.world.level.block.entity.BlockEntityType;
import team.reborn.energy.api.EnergyStorage;
import wily.betterfurnaces.blockentity.InventoryBlockEntity;
import wily.factoryapi.base.IPlatformEnergyStorage;
import wily.factoryapi.base.Storages;

import java.util.Optional;

public class TechRebornEnergyCompat {
    public static void registerEnergyStorageSided(BlockEntityType<? extends InventoryBlockEntity> blockEntityType){
        EnergyStorage.SIDED.registerForBlockEntity((storage, d) ->storage.getStorage(Storages.ENERGY,d).map(iPlatformEnergyStorage -> (EnergyStorage) iPlatformEnergyStorage.getHandler()).orElse(null), blockEntityType);
    }

}
