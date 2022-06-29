package wily.betterfurnaces.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
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
import wily.betterfurnaces.init.Registration;
import wily.betterfurnaces.tileentity.BlockForgeTileBase;
import wily.betterfurnaces.util.DirectionUtil;


public abstract class BlockForgeContainerBase extends AbstractSmeltingContainer {

    public BlockForgeContainerBase(ContainerType<?> containerType, int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
        super(containerType, windowId, world, pos, playerInventory, player);
    }

    public BlockForgeContainerBase(ContainerType<?> containerType, int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player, IIntArray fields) {
        super(containerType, windowId, world, pos, playerInventory, player, fields);
    }

    @Override
    public void addInventorySlots(){
        int i = 20;
        int y1 = 62 - i;
        int y2 = 100 - i;
        int y3 = 80 - i;
        int y4 = 5 - i;
        this.addSlot(new SlotInput(te, 0, 27, y1));
        this.addSlot(new SlotInput(te, 1, 45, y1));
        this.addSlot(new SlotInput(te, 2, 63, y1));
        this.addSlot(new SlotFuel(this.te, 3, 8, y2));
        this.addSlot(new SlotOutput(playerEntity, te, 4, 108, y3));
        this.addSlot(new SlotOutput(playerEntity, te, 5, 126, y3));
        this.addSlot(new SlotOutput(playerEntity, te, 6, 144, y3));
        this.addSlot(new SlotUpgrade(te, 7, 7, y4));
        this.addSlot(new SlotUpgrade(te, 8, 25, y4));
        this.addSlot(new SlotUpgrade(te, 9, 43, y4));
        this.addSlot(new SlotHeater(te, 10, 79, y4));
        this.addSlot(new SlotUpgrade(te, 11, 115, y4));
        this.addSlot(new SlotUpgrade(te, 12, 133, y4));
        this.addSlot(new SlotUpgrade(te, 13, 151, y4));
        layoutPlayerInventorySlots(8, 106);
    }
}
