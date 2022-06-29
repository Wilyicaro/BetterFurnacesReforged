package wily.betterfurnaces.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.items.ItemUpgradeFuelEfficiency;
import wily.betterfurnaces.items.ItemUpgradeLiquidFuel;
import wily.betterfurnaces.items.ItemUpgradeOreProcessing;
import wily.betterfurnaces.items.ItemUpgrade;
import wily.betterfurnaces.tileentity.BlockSmeltingTileBase;
import wily.betterfurnaces.util.DirectionUtil;


public abstract class BlockFurnaceContainerBase extends AbstractSmeltingContainer {


    public BlockFurnaceContainerBase(ContainerType<?> containerType, int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
        super(containerType, windowId, world, pos, playerInventory, player);
    }

    public BlockFurnaceContainerBase(ContainerType<?> containerType, int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player, IIntArray fields) {
        super(containerType, windowId, world, pos, playerInventory, player, fields);
    }
}
