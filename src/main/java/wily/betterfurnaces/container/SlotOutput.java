package wily.betterfurnaces.container;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ForgeEventFactory;
import wily.betterfurnaces.tileentity.BlockForgeTileBase;
import wily.betterfurnaces.tileentity.BlockFurnaceTileBase;

public class SlotOutput extends Slot {

    private final Player player;
    private int removeCount;
    private BlockFurnaceTileBase te;
    private BlockForgeTileBase tf;

    public SlotOutput(Player player, Container te, int slotIndex, int xPosition, int yPosition) {
        super(te, slotIndex, xPosition, yPosition);
        this.player = player;
        if (te instanceof BlockFurnaceTileBase) {
            this.te = (BlockFurnaceTileBase) te;
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


    public void onTake(Player thePlayer, ItemStack stack) {
        this.checkTakeAchievements(stack);
        super.onTake(thePlayer, stack);
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
        if (!this.player.level.isClientSide && this.te instanceof BlockFurnaceTileBase) {
            ((BlockFurnaceTileBase)this.te).unlockRecipes(this.player);
        }
        if (!this.player.level.isClientSide && this.tf instanceof BlockForgeTileBase) {
            ((BlockForgeTileBase)this.tf).unlockRecipes(this.player);
        }

        this.removeCount = 0;
        ForgeEventFactory.firePlayerSmeltedEvent(this.player, stack);
    }

    /**
     * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not ore and wood.
     */
    protected void onCrafting(ItemStack stack) {

    }

}
