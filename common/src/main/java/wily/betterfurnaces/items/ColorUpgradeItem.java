package wily.betterfurnaces.items;

import dev.architectury.registry.menu.MenuRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import wily.betterfurnaces.init.Registration;
import wily.betterfurnaces.inventory.AbstractUpgradeMenu;

public class ColorUpgradeItem extends UpgradeItem {

    public ColorUpgradeItem(Properties properties, String tooltip) {
        super(properties,4, tooltip);
    }


    public InteractionResultHolder<ItemStack> use(Level world, Player entity, InteractionHand hand) {
        InteractionResultHolder<ItemStack> ar = super.use(world, entity, hand);
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();
        if (entity instanceof ServerPlayer) {
            ItemStack stack = entity.getItemInHand(InteractionHand.MAIN_HAND);
            MenuProvider ContainerProviderColorUpgrade = new ContainerProviderColorUpgrade(this, stack);
            MenuRegistry.openExtendedMenu((ServerPlayer) entity, ContainerProviderColorUpgrade, buf -> {
                buf.writeBlockPos(new BlockPos(x, y, z));
                buf.writeByte(hand == InteractionHand.MAIN_HAND ? 0 : 1);
            });
        }
        return ar;
    }
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        ItemStack itemStack = stack;
        CompoundTag nbt;
        nbt = itemStack.getOrCreateTag();

        if (!(nbt.contains("red") && nbt.contains("green") && nbt.contains("blue"))) {
            nbt.putInt("red", 255);
            nbt.putInt("green", 255);
            nbt.putInt("blue", 255);
            itemStack.setTag(nbt);
        }
    }
    private static class ContainerProviderColorUpgrade implements MenuProvider {
        public ContainerProviderColorUpgrade(ColorUpgradeItem item, ItemStack stack) {
            this.itemStackColorUpgrade = stack;
        }

        @Override
        public Component getDisplayName() {
            return Component.translatable("item.betterfurnacesreforged.color_upgrade");
        }

        @Override
        public ContainerColorUpgrade createMenu(int windowID, Inventory playerInventory, Player playerEntity) {
            ContainerColorUpgrade newContainerServerSide =
                    new ContainerColorUpgrade(windowID, playerInventory,
                            itemStackColorUpgrade);
            return newContainerServerSide;
        }

        private ItemStack itemStackColorUpgrade;
    }
    public static class ContainerColorUpgrade extends AbstractUpgradeMenu {

        public final ItemStack itemStackBeingHeld;

        public ContainerColorUpgrade(int windowId, Inventory playerInv,
                                     ItemStack itemStackBeingHeld) {
            super(Registration.COLOR_UPGRADE_CONTAINER.get(), windowId, playerInv, itemStackBeingHeld);
            this.itemStackBeingHeld = itemStackBeingHeld;

        }

        @Override
        public ItemStack quickMoveStack(Player p_38941_, int p_38942_) {
            return null;
        }
    }
    }


