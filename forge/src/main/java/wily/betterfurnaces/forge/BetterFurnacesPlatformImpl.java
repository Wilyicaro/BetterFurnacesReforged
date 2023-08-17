package wily.betterfurnaces.forge;

import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.registries.ForgeRegistries;
import wily.betterfurnaces.blockentity.CobblestoneGeneratorBlockEntity;
import wily.betterfurnaces.blockentity.InventoryBlockEntity;
import wily.betterfurnaces.blockentity.SmeltingBlockEntity;

import java.nio.file.Path;

public class BetterFurnacesPlatformImpl {


    public static void smeltingAutoIO(SmeltingBlockEntity be) {
        for (Direction dir : Direction.values()) {
            BlockEntity tile = be.getLevel().getBlockEntity(be.getBlockPos().offset(dir.getNormal()));
            if (tile == null) {
                continue;
            }
            if (be.furnaceSettings.get(dir.ordinal()) == 1 || be.furnaceSettings.get(dir.ordinal()) == 2 || be.furnaceSettings.get(dir.ordinal()) == 3 || be.furnaceSettings.get(dir.ordinal()) == 4) {
                if (tile != null) {
                    IItemHandler other = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, dir.getOpposite()).map(other1 -> other1).orElse(null);

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
                                for (int FUEL : be.FUEL())
                                    if (be.furnaceSettings.get(dir.ordinal()) == 4) {
                                        if (be.inventory.getItem(FUEL).getCount() >= be.inventory.getItem(FUEL).getMaxStackSize()) {
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
                                            if (be.isItemFuel(stack) && be.inventory.getItem(FUEL).isEmpty() || ItemHandlerHelper.canItemStacksStack(be.inventory.getItem(FUEL), stack)) {
                                                be.inventory.insertItem(FUEL, other.extractItem(i, other.getStackInSlot(i).getMaxStackSize() - be.inventory.getItem(FUEL).getCount(), false), false);
                                            }
                                        }
                                    }
                            }

                            if (be.getAutoOutput() == 1) {
                                for (int FUEL : be.FUEL())
                                    if (be.furnaceSettings.get(dir.ordinal()) == 4) {
                                        if (be.inventory.getItem(FUEL).isEmpty()) {
                                            continue;
                                        }
                                        ItemStack fuel = be.inventory.getItem(FUEL);
                                        if (be.isItemFuel(fuel)) {
                                            continue;
                                        }
                                        for (int i = 0; i < other.getSlots(); i++) {
                                            ItemStack stack = be.inventory.extractItem(FUEL, be.inventory.getItem(FUEL).getMaxStackSize() - other.getStackInSlot(i).getCount(), true);
                                            if (other.isItemValid(i, stack) && (other.getStackInSlot(i).isEmpty() || other.isItemValid(i, stack) && (ItemHandlerHelper.canItemStacksStack(other.getStackInSlot(i), stack) && other.getStackInSlot(i).getCount() + stack.getCount() <= other.getSlotLimit(i)))) {
                                                other.insertItem(i, be.inventory.extractItem(FUEL, stack.getCount(), false), false);
                                            }
                                        }
                                    }
                                for (int output : be.OUTPUTS()) {
                                    if (be.furnaceSettings.get(dir.ordinal()) == 2 || be.furnaceSettings.get(dir.ordinal()) == 3) {
                                        if (be.inventory.getItem(output).isEmpty()) {
                                            continue;
                                        }
                                        if (ForgeRegistries.BLOCKS.getKey(tile.getBlockState().getBlock()).toString().contains("storagedrawers:")) {
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

    public static void autoOutput(InventoryBlockEntity be, int output) {
        for (Direction dir : Direction.values()) {
            BlockEntity tile = be.getLevel().getBlockEntity(be.getBlockPos().relative(dir));
            if (tile == null) {
                continue;
            }
            IItemHandler other = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, dir.getOpposite()).map(other1 -> other1).orElse(null);

            if (other == null) {
                continue;
            }

            if (be.inventory.getItem(output).isEmpty()) {
                continue;
            }
            if (ForgeRegistries.BLOCKS.getKey(tile.getBlockState().getBlock()).toString().contains("storagedrawers:")) {
                continue;
            }
            for (int i = 0; i < other.getSlots(); i++) {
                ItemStack stack = be.inventory.extractItem(output, be.inventory.getItem(output).getMaxStackSize() - other.getStackInSlot(i).getCount(), true);

                if (other.isItemValid(i, stack) && (other.getStackInSlot(i).isEmpty() || ItemHandlerHelper.canItemStacksStack(other.getStackInSlot(i), stack) && other.getStackInSlot(i).getCount() + stack.getCount() <= other.getSlotLimit(i))) {
                    be.inventory.extractItem(output,stack.getCount() - other.insertItem(i, stack, false).getCount(),false);
                }
            }
        }
    }

    public static void transferEnergySides(SmeltingBlockEntity be) {
        for (Direction dir : Direction.values()) {
            BlockEntity sideBe = be.getLevel().getBlockEntity(be.getBlockPos().offset(dir.getNormal()));
            if (sideBe == null) {
                continue;
            }
            LazyOptional<IEnergyStorage> energyStorage = sideBe.getCapability(CapabilityEnergy.ENERGY,dir);
            energyStorage.ifPresent((e)-> { if (be.energyStorage.getEnergyStored() > 0)be.energyStorage.consumeEnergy(e.receiveEnergy(be.energyStorage.getEnergyStored(),false),false);});
        }
    }

    public static Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }

    public static Tag<Item> getCommonItemTag(String commonTag) {
        return ItemTags.getAllTags().getTag(new ResourceLocation("forge", commonTag));
    }

}
