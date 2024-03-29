package wily.betterfurnaces.fabric;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.fabricmc.fabric.impl.tag.convention.TagRegistration;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import team.reborn.energy.api.EnergyStorage;
import wily.betterfurnaces.BetterFurnacesPlatform;
import wily.betterfurnaces.blockentity.AbstractCobblestoneGeneratorBlockEntity;
import wily.betterfurnaces.blockentity.AbstractSmeltingBlockEntity;
import wily.factoryapi.base.Storages;

import java.nio.file.Path;
import java.util.Iterator;

public class BetterFurnacesPlatformImpl {

    public static Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }

    public static void smeltingAutoIO(AbstractSmeltingBlockEntity be) {
        for (Direction dir : Direction.values()) {
            BlockEntity tile = be.getLevel().getBlockEntity(be.getBlockPos().offset(dir.getNormal()));
            if (tile == null) {
                continue;
            }
            if (be.furnaceSettings.get(dir.ordinal()) == 1 || be.furnaceSettings.get(dir.ordinal()) == 2 || be.furnaceSettings.get(dir.ordinal()) == 3 || be.furnaceSettings.get(dir.ordinal()) == 4) {
                if (tile != null) {
                    Storage<ItemVariant> other = ItemStorage.SIDED.find(be.getLevel(),be.getBlockPos().relative(dir),dir);
                    Iterator<StorageView<ItemVariant>> storageView = other.iterator();

                    if (other == null) {
                        continue;
                    }
                    if (other != null) {
                        if (be.getAutoInput() != 0 || be.getAutoOutput() != 0) {
                            if (be.getAutoInput() == 1) {
                                for (int INPUT : be.INPUTS())
                                    if (be.furnaceSettings.get(dir.ordinal()) == 1 || be.furnaceSettings.get(dir.ordinal()) == 3) {
                                        ItemStack input = be.inventory.getItem(INPUT);
                                        if (input.getCount() >= input.getMaxStackSize()) {
                                            continue;
                                        }
                                        while (storageView.hasNext()) {
                                            StorageView<ItemVariant> view = storageView.next();
                                            ItemVariant variant = view.getResource();
                                            if (view.isResourceBlank() || !be.IisItemValidForSlot(INPUT,variant.toStack())) {
                                                continue;
                                            }
                                            if (variant.isOf(input.getItem()) || input.isEmpty())
                                                try (Transaction transaction = Transaction.openOuter()) {
                                                    be.inventory.insertItem(INPUT, variant.toStack((int) other.extract(variant, view.getCapacity() - be.inventory.getItem(INPUT).getCount(), transaction)), false);
                                                    transaction.commit();
                                                }
                                        }
                                    }
                                if (be.furnaceSettings.get(dir.ordinal()) == 4) {
                                    ItemStack fuel = be.inventory.getItem(be.FUEL());
                                    if (fuel.getCount() >= fuel.getMaxStackSize()) {
                                        continue;
                                    }
                                    while (storageView.hasNext()) {
                                        StorageView<ItemVariant> view = storageView.next();
                                        ItemVariant variant = view.getResource();
                                        if (view.isResourceBlank() || !be.isItemFuel(view.getResource().toStack())) {
                                            continue;
                                        }
                                        if (variant.isOf(fuel.getItem()) || fuel.isEmpty())
                                        try (Transaction transaction = Transaction.openOuter()) {
                                            be.inventory.insertItem(be.FUEL(), variant.toStack((int) other.extract(variant, view.getCapacity() - be.inventory.getItem(be.FUEL()).getCount(), transaction)), false);
                                            transaction.commit();
                                        }
                                        }
                                    }
                                }
                            }

                            if (be.getAutoOutput() == 1) {

                                if (be.furnaceSettings.get(dir.ordinal()) == 4) {
                                    if (be.inventory.getItem(be.FUEL()).isEmpty()) {
                                        continue;
                                    }
                                    ItemStack fuel = be.inventory.getItem(be.FUEL());
                                    if (be.isItemFuel(fuel)) {
                                        continue;
                                    }
                                    try (Transaction transaction = Transaction.openOuter()) {
                                        be.inventory.extractItem(be.FUEL(), (int) other.insert(ItemVariant.of(fuel), fuel.getCount(), transaction), false);
                                        transaction.commit();
                                    }

                                }

                                for (int output : be.OUTPUTS()) {
                                    if (be.furnaceSettings.get(dir.ordinal()) == 2 || be.furnaceSettings.get(dir.ordinal()) == 3) {
                                        ItemStack o = be.inventory.getItem(output);
                                        if (o.isEmpty()) {
                                            continue;
                                        }
                                        if (BuiltInRegistries.BLOCK.getKey(tile.getBlockState().getBlock()).toString().contains("storagedrawers:")) {
                                            continue;
                                        }
                                        try (Transaction transaction = Transaction.openOuter()) {
                                            be.inventory.extractItem(output, (int) other.insert(ItemVariant.of(o), o.getCount(), transaction), false);
                                            transaction.commit();
                                        }

                                    }
                                }
                            }
                        }
                    }
                }

            }
        }

    public static void outputAutoIO(AbstractCobblestoneGeneratorBlockEntity be) {
        for (Direction dir : Direction.values()) {
            BlockEntity tile = be.getLevel().getBlockEntity(be.getBlockPos().offset(dir.getNormal()));
            if (tile == null) {
                continue;
            }
            if (tile != null) {
                Storage<ItemVariant> other = ItemStorage.SIDED.find(be.getLevel(),be.getBlockPos().relative(dir),dir);

                if (other == null) {
                    continue;
                }

                if (other != null) {
                    ItemStack o = be.inventory.getItem(be.OUTPUT);
                    if (be.inventory.getItem(be.OUTPUT).isEmpty()) {
                        continue;
                    }
                    if (BuiltInRegistries.BLOCK.getKey(tile.getBlockState().getBlock()).toString().contains("storagedrawers:")) {
                        continue;
                    }
                    try (Transaction transaction = Transaction.openOuter()) {
                        be.inventory.extractItem(be.OUTPUT, (int) other.insert(ItemVariant.of(o), o.getCount(), transaction), false);
                        transaction.commit();
                    }
                }
            }
        }
    }

    public static void transferEnergySides(AbstractSmeltingBlockEntity be) {
        for (Direction dir : Direction.values()) {
            EnergyStorage storage= EnergyStorage.SIDED.find(be.getLevel(),be.getBlockPos().relative(dir),dir);
            if (storage == null) continue;
            try (Transaction transaction = Transaction.openOuter()){
                int i = (int) storage.insert(be.energyStorage.getEnergyStored(),transaction);
                transaction.commit();
                be.energyStorage.consumeEnergy(i,false);
            }
        }
    }

    public static TagKey<Item> getCommonItemTag(String tag) {
        return TagRegistration.ITEM_TAG_REGISTRATION.registerCommon(tag);
    }

}
