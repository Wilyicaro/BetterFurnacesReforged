package wily.betterfurnaces.fabriclike;

import dev.architectury.platform.Platform;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.world.level.block.entity.BlockEntityType;
import team.reborn.energy.api.EnergyStorage;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.blockentity.AbstractSmeltingBlockEntity;
import wily.betterfurnaces.blockentity.InventoryBlockEntity;
import wily.betterfurnaces.init.Registration;
import wily.factoryapi.base.Storages;

import java.util.List;

public class BetterFurnacesFabricLike {
    public static void init() {
        BetterFurnacesReforged.init();
        for (BlockEntityType<?> blockEntityType : Registration.BLOCK_ENTITIES.getRegistrar()) ItemStorage.SIDED.registerForBlockEntity((storage, d) -> storage instanceof InventoryBlockEntity be ? (Storage<ItemVariant>) be.getStorage(Storages.ITEM,d).get().getHandler() : null, blockEntityType);

        for (BlockEntityType<? extends AbstractSmeltingBlockEntity> blockEntityType: List.of(Registration.IRON_FURNACE_TILE.get(),Registration.GOLD_FURNACE_TILE.get(),Registration.DIAMOND_FURNACE_TILE.get(),Registration.NETHERHOT_FURNACE_TILE.get(),Registration.EXTREME_FURNACE_TILE.get(),Registration.EXTREME_FORGE_TILE.get())) {
            if (Platform.isModLoaded("techreborn")) TechRebornEnergyCompat.registerEnergyStorageSided(blockEntityType);
            FluidStorage.SIDED.registerForBlockEntity((storage, d) -> (Storage<FluidVariant>) storage.getStorage(Storages.FLUID, d).get().getHandler(), blockEntityType);
        }
        }
}
