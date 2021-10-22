package wily.betterfurnaces.container;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

public abstract class ItemUpgradeContainerBase extends AbstractContainerMenu{

    public final ItemStack itemStackBeingHeld;
    public Inventory playerInv;

    public ItemUpgradeContainerBase(MenuType<?> containerType, int windowId, Inventory playerInv, ItemStack itemStackBeingHeld) {
        super(containerType, windowId);
        this.itemStackBeingHeld = itemStackBeingHeld;
        this.playerInv = playerInv;
    }


    @Override
        public boolean stillValid(Player player) {
            ItemStack main = player.getMainHandItem();
            ItemStack off = player.getItemInHand(InteractionHand.OFF_HAND);
            return (!main.isEmpty() && main == itemStackBeingHeld) ||
                    (!off.isEmpty() && off == itemStackBeingHeld);
        }

}
