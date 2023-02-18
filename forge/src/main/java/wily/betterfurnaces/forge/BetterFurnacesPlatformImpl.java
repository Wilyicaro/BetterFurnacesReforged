package wily.betterfurnaces.forge;

import dev.architectury.event.events.common.ChunkEvent;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.registries.ForgeRegistry;
import wily.betterfurnaces.BetterFurnacesPlatform;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.blockentity.AbstractCobblestoneGeneratorBlockEntity;
import wily.betterfurnaces.blockentity.AbstractSmeltingBlockEntity;
import wily.betterfurnaces.blockentity.InventoryBlockEntity;
import wily.factoryapi.base.Storages;

import java.nio.file.Path;

public class BetterFurnacesPlatformImpl {


    public static void smeltingAutoIO(AbstractSmeltingBlockEntity be) {
        for (Direction dir : Direction.values()) {
            BlockEntity tile = be.getLevel().getBlockEntity(be.getBlockPos().offset(dir.getNormal()));
            if (tile == null) {
                continue;
            }
            if (be.furnaceSettings.get(dir.ordinal()) == 1 || be.furnaceSettings.get(dir.ordinal()) == 2 || be.furnaceSettings.get(dir.ordinal()) == 3 || be.furnaceSettings.get(dir.ordinal()) == 4) {
                if (tile != null) {
                    IItemHandler other = tile.getCapability(ForgeCapabilities.ITEM_HANDLER, dir.getOpposite()).map(other1 -> other1).orElse(null);

                    if (other == null) {
                        continue;
                    }
                    if (other != null) {
                        if (be.getAutoInput() != 0 || be.getAutoOutput() != 0) {
                            if (be.getAutoInput() == 1) {
                                for (int INPUT : be.INPUTS())
                                    if (be.furnaceSettings.get(dir.ordinal()) == 1 || be.furnaceSettings.get(dir.ordinal()) == 3) {
                                        if (be.inventory.getItem(INPUT).getCount() >= be.inventory.getItem(INPUT).getMaxStackSize()) {
                                            continue;
                                        }
                                        for (int i = 0; i < other.getSlots(); i++) {
                                            if (other.getStackInSlot(i).isEmpty()) {
                                                continue;
                                            }
                                            ItemStack stack = other.extractItem(i, other.getStackInSlot(i).getMaxStackSize(), true);
                                            if (be.hasRecipe(stack) && be.inventory.getItem(INPUT).isEmpty() || ItemHandlerHelper.canItemStacksStack(be.inventory.getItem(INPUT), stack))
                                                be.inventory.insertItem(INPUT, other.extractItem(i, other.getStackInSlot(i).getMaxStackSize() - be.inventory.getItem(INPUT).getCount(), false), false);
                                        }
                                    }
                                if (be.furnaceSettings.get(dir.ordinal()) == 4) {
                                    if (be.inventory.getItem(be.FUEL()).getCount() >= be.inventory.getItem(be.FUEL()).getMaxStackSize()) {
                                        continue;
                                    }
                                    for (int i = 0; i < other.getSlots(); i++) {
                                        if (!be.isItemFuel(other.getStackInSlot(i))){
                                            continue;
                                        }
                                        if (other.getStackInSlot(i).isEmpty()) {
                                            continue;
                                        }
                                        ItemStack stack = other.extractItem(i, other.getStackInSlot(i).getMaxStackSize(), true);
                                        if (be.isItemFuel(stack) && be.inventory.getItem(be.FUEL()).isEmpty() || ItemHandlerHelper.canItemStacksStack(be.inventory.getItem(be.FUEL()), stack)) {
                                            be.inventory.insertItem(be.FUEL(), other.extractItem(i, other.getStackInSlot(i).getMaxStackSize() - be.inventory.getItem(be.FUEL()).getCount(), false), false);
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
                                    for (int i = 0; i < other.getSlots(); i++) {
                                        ItemStack stack = be.inventory.extractItem(be.FUEL(), be.inventory.getItem(be.FUEL()).getMaxStackSize() - other.getStackInSlot(i).getCount(), true);
                                        if (other.isItemValid(i, stack) && (other.getStackInSlot(i).isEmpty() || other.isItemValid(i, stack) && (ItemHandlerHelper.canItemStacksStack(other.getStackInSlot(i), stack) && other.getStackInSlot(i).getCount() + stack.getCount() <= other.getSlotLimit(i)))) {
                                            other.insertItem(i, be.inventory.extractItem(be.FUEL(), stack.getCount(), false), false);
                                        }
                                    }
                                }

                                for (int output : be.OUTPUTS()) {
                                    if (be.furnaceSettings.get(dir.ordinal()) == 2 || be.furnaceSettings.get(dir.ordinal()) == 3) {
                                        if (be.inventory.getItem(output).isEmpty()) {
                                            continue;
                                        }
                                        if (Registry.BLOCK.getKey(tile.getBlockState().getBlock()).toString().contains("storagedrawers:")) {
                                            continue;
                                        }
                                        for (int i = 0; i < other.getSlots(); i++) {
                                            ItemStack stack = be.inventory.extractItem(output, be.inventory.getItem(output).getMaxStackSize() - other.getStackInSlot(i).getCount(), true);
                                            if (other.isItemValid(i, stack) && (other.getStackInSlot(i).isEmpty() || other.isItemValid(i, stack) && (ItemHandlerHelper.canItemStacksStack(other.getStackInSlot(i), stack) && other.getStackInSlot(i).getCount() + stack.getCount() <= other.getSlotLimit(i)))) {
                                                other.insertItem(i, be.inventory.extractItem(output, stack.getCount(), false), false);
                                            }
                                        }

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
            IItemHandler other = tile.getCapability(ForgeCapabilities.ITEM_HANDLER, dir.getOpposite()).map(other1 -> other1).orElse(null);

            if (other == null) {
                continue;
            }

            if (other != null) {
                if (be.inventory.getItem(be.OUTPUT).isEmpty()) {
                    continue;
                }
                if (Registry.BLOCK.getKey(tile.getBlockState().getBlock()).toString().contains("storagedrawers:")) {
                    continue;
                }
                for (int i = 0; i < other.getSlots(); i++) {
                    ItemStack stack = be.inventory.extractItem(be.OUTPUT, be.inventory.getItem(be.OUTPUT).getMaxStackSize() - other.getStackInSlot(i).getCount(), true);
                    if (other.isItemValid(i, stack) && (other.getStackInSlot(i).isEmpty() || other.isItemValid(i, stack) && ItemHandlerHelper.canItemStacksStack(other.getStackInSlot(i), stack) && other.getStackInSlot(i).getCount() + stack.getCount() <= other.getSlotLimit(i))) {
                        other.insertItem(i, be.inventory.extractItem(be.OUTPUT, stack.getCount(), false), false);
                    }
                }
            }
        }
    }

    public static void transferEnergySides(AbstractSmeltingBlockEntity be) {
        for (Direction dir : Direction.values()) {
            BlockEntity sideBe = be.getLevel().getBlockEntity(be.getBlockPos().offset(dir.getNormal()));
            if (sideBe == null) {
                continue;
            }
            LazyOptional<IEnergyStorage> energyStorage = sideBe.getCapability(ForgeCapabilities.ENERGY,dir);
            energyStorage.ifPresent((e)-> { if (be.energyStorage.getEnergyStored() > 0)be.energyStorage.consumeEnergy(e.receiveEnergy(be.energyStorage.getEnergyStored(),false),false);});
        }
    }

    public static Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }

    public static TagKey<Item> getCommonItemTag(String commonTag) {
        return ItemTags.create(new ResourceLocation("forge", commonTag));
    }


}
