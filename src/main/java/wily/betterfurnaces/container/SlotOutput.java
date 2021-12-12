package wily.betterfurnaces.container;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import wily.betterfurnaces.blockentity.BlockEntityForgeBase;
import wily.betterfurnaces.blockentity.BlockEntitySmeltingBase;

public class SlotOutput extends Slot {

    private final Player player;
    private int removeCount;
    private BlockEntitySmeltingBase te;
    private BlockEntityForgeBase tf;

    public SlotOutput(Player player, Container te, int slotIndex, int xPosition, int yPosition) {
        super(te, slotIndex, xPosition, yPosition);
        this.player = player;
        if (te instanceof BlockEntitySmeltingBase) {
            this.te = (BlockEntitySmeltingBase) te;
        }else if (te instanceof BlockEntityForgeBase) {
            this.tf = (BlockEntityForgeBase) te;
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
        if (!this.player.level.isClientSide && this.te instanceof BlockEntitySmeltingBase) {
            ((BlockEntitySmeltingBase)this.te).unlockRecipes(this.player);
        }
        if (!this.player.level.isClientSide && this.tf instanceof BlockEntityForgeBase) {
            ((BlockEntityForgeBase)this.tf).unlockRecipes(this.player);
        }

        this.removeCount = 0;
        net.minecraftforge.fmllegacy.hooks.BasicEventHooks.firePlayerSmeltedEvent(this.player, stack);
    }

    /**
     * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not ore and wood.
     */
    protected void onCrafting(ItemStack stack) {

    }

}
