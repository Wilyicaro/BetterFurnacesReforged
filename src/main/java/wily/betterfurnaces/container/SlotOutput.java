package wily.betterfurnaces.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import wily.betterfurnaces.tileentity.BlockForgeTileBase;
import wily.betterfurnaces.tileentity.BlockSmeltingTileBase;

public class SlotOutput extends Slot {

    private final PlayerEntity player;
    private int removeCount;
    private BlockSmeltingTileBase te;
    private BlockForgeTileBase tf;

    public SlotOutput(PlayerEntity player, IInventory te, int slotIndex, int xPosition, int yPosition) {
        super(te, slotIndex, xPosition, yPosition);
        this.player = player;
        if (te instanceof BlockSmeltingTileBase) {
            this.te = (BlockSmeltingTileBase) te;
        }else if (te instanceof BlockForgeTileBase) {
            this.tf = (BlockForgeTileBase) te;
        }
    }

    /**
     * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
     */
    public boolean mayPlace(ItemStack stack) {
        return false;
    }


    public ItemStack onTake(PlayerEntity thePlayer, ItemStack stack) {
        this.onCrafting(stack);
        super.onTake(thePlayer, stack);
        return stack;
    }

    /**
     * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not ore and wood. Typically increases an
     * internal count then calls onCrafting(item).
     */
    protected void onCrafting(ItemStack stack, int amount) {
        this.removeCount += amount;
        this.onCrafting(stack);
    }

    @Override
    protected void onQuickCraft(ItemStack stack, int p_75210_2_) {
        stack.onCraftedBy(this.player.level, this.player, this.removeCount);
        if (!this.player.level.isClientSide && this.te instanceof BlockSmeltingTileBase) {
            ((BlockSmeltingTileBase)this.te).unlockRecipes(this.player);
        }
        if (!this.player.level.isClientSide && this.tf instanceof BlockForgeTileBase) {
            ((BlockForgeTileBase)this.tf).unlockRecipes(this.player);
        }

        this.removeCount = 0;
        net.minecraftforge.fml.hooks.BasicEventHooks.firePlayerSmeltedEvent(this.player, stack);
    }

    /**
     * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not ore and wood.
     */
    protected void onCrafting(ItemStack stack) {

    }

}
