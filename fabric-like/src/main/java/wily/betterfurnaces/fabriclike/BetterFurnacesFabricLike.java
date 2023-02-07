package wily.betterfurnaces.fabriclike;

import dev.architectury.platform.Platform;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.world.level.block.entity.BlockEntityType;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.Config;
import wily.betterfurnaces.blockentity.AbstractSmeltingBlockEntity;
import wily.betterfurnaces.blockentity.InventoryBlockEntity;
import wily.betterfurnaces.init.Registration;
import wily.factoryapi.base.IFluidItem;
import wily.factoryapi.base.Storages;
import wily.factoryapi.fabriclike.base.FabricItemFluidStorage;
import wily.ultimatefurnaces.init.RegistrationUF;

import java.util.ArrayList;
import java.util.List;

public class BetterFurnacesFabricLike {
    public static void init() {
        BetterFurnacesReforged.init();
        FluidStorage.ITEM.registerForItems((s,c)-> s.getItem() instanceof IFluidItem<?> fluid ? new FabricItemFluidStorage(c,fluid.getFluidStorageBuilder()) : null, Registration.GENERATOR.get());
        Registration.BLOCK_ENTITIES.forEach(blockEntityTypeRegistrySupplier ->  ItemStorage.SIDED.registerForBlockEntity((storage, d) -> storage instanceof InventoryBlockEntity be ? (Storage<ItemVariant>) be.getStorage(Storages.ITEM,d).get().getHandler() : null, blockEntityTypeRegistrySupplier.get()));
        List<BlockEntityType<? extends AbstractSmeltingBlockEntity>> BEs =  new ArrayList<>(List.of(Registration.IRON_FURNACE_TILE.get(),Registration.GOLD_FURNACE_TILE.get(),Registration.DIAMOND_FURNACE_TILE.get(),Registration.NETHERHOT_FURNACE_TILE.get(),Registration.EXTREME_FURNACE_TILE.get(),Registration.EXTREME_FORGE_TILE.get()));
        if (Config.enableUltimateFurnaces) BEs.addAll(List.of(RegistrationUF.COPPER_FURNACE_TILE.get(),RegistrationUF.STEEL_FURNACE_TILE.get(),RegistrationUF.AMETHYST_FURNACE_TILE.get(), RegistrationUF.PLATINUM_FURNACE_TILE.get(),RegistrationUF.IRON_FORGE_TILE.get(),RegistrationUF.GOLD_FORGE_TILE.get(),RegistrationUF.DIAMOND_FORGE_TILE.get(),RegistrationUF.NETHERHOT_FORGE_TILE.get(),RegistrationUF.ULTIMATE_FORGE_TILE.get()));
        for (BlockEntityType<? extends AbstractSmeltingBlockEntity> blockEntityType: BEs) {
            FabricEnergyCompat.registerEnergyStorageSided(blockEntityType);
            FluidStorage.SIDED.registerForBlockEntity((storage, d) -> (Storage<FluidVariant>) storage.getStorage(Storages.FLUID, d).get().getHandler(), blockEntityType);
        }
        }
}
