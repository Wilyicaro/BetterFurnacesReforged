package wily.betterfurnaces.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

public abstract class UpgradeContainerBase extends Container{

    public final ItemStack itemStackBeingHeld;
    public PlayerInventory playerInv;

    public UpgradeContainerBase(ContainerType<?> containerType, int windowId, PlayerInventory playerInv, ItemStack itemStackBeingHeld) {
        super(containerType, windowId);
        this.itemStackBeingHeld = itemStackBeingHeld;
        this.playerInv = playerInv;
    }


    @Override
        public boolean stillValid(PlayerEntity player) {
            ItemStack main = player.getMainHandItem();
            ItemStack off = player.getItemInHand(Hand.OFF_HAND);
            return (!main.isEmpty() && main == itemStackBeingHeld) ||
                    (!off.isEmpty() && off == itemStackBeingHeld);
        }

}
