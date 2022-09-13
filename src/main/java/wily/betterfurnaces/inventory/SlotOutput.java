package wily.betterfurnaces.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.ForgeEventFactory;
import wily.betterfurnaces.blockentity.AbstractSmeltingBlockEntity;

public class SlotOutput extends Slot {

    private final Player player;
    private int removeCount;
    private AbstractSmeltingBlockEntity be;

    public SlotOutput(Player player, Container be, int slotIndex, int xPosition, int yPosition) {
        super(be, slotIndex, xPosition, yPosition);
        this.player = player;
        if (be instanceof AbstractSmeltingBlockEntity) {
            this.be = (AbstractSmeltingBlockEntity) be;
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
        if (!this.player.level.isClientSide && this.be instanceof AbstractSmeltingBlockEntity) {
            (be).unlockRecipes(this.player);
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
