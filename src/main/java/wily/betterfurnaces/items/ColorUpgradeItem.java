package wily.betterfurnaces.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import wily.betterfurnaces.init.Registration;
import wily.betterfurnaces.inventory.UpgradeContainerBase;


public class ColorUpgradeItem extends UpgradeItem {

    public ColorUpgradeItem(Properties properties, String tooltip) {
        super(properties, 4, tooltip);
    }

    public ActionResult<ItemStack> use(World world, PlayerEntity entity, Hand hand) {
        ActionResult<ItemStack> ar = super.use(world, entity, hand);
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();
        if (entity instanceof ServerPlayerEntity) {
            ItemStack stack = entity.getItemInHand(Hand.MAIN_HAND);
            INamedContainerProvider ContainerProviderColorUpgrade = new ContainerProviderColorUpgrade(this, stack);
            NetworkHooks.openGui((ServerPlayerEntity) entity, ContainerProviderColorUpgrade, buf -> {
                buf.writeBlockPos(new BlockPos(x, y, z));
                buf.writeByte(hand == Hand.MAIN_HAND ? 0 : 1);
            });
        }
        return ar;
    }

    public void inventoryTick(ItemStack stack, World world, Entity player, int slot, boolean selected) {
        super.inventoryTick(stack, world, player, slot, selected);
        ItemStack itemStack = stack;
        CompoundNBT nbt;
        nbt = itemStack.getOrCreateTag();
        if (!(nbt.contains("red") && nbt.contains("green") && nbt.contains("blue"))) {
            nbt.putInt("red", 255);
            nbt.putInt("green", 255);
            nbt.putInt("blue", 255);
            itemStack.setTag(nbt);
        }
    }

    private static class ContainerProviderColorUpgrade implements INamedContainerProvider {
        private final ItemStack itemStackColorUpgrade;

        public ContainerProviderColorUpgrade(ColorUpgradeItem item, ItemStack stack) {
            this.itemStackColorUpgrade = stack;
        }

        @Override
        public ITextComponent getDisplayName() {
            return new TranslationTextComponent("item.betterfurnacesreforged.color_upgrade");
        }

        @Override
        public ContainerColorUpgrade createMenu(int windowID, PlayerInventory playerInventory, PlayerEntity playerEntity) {
            ContainerColorUpgrade newContainerServerSide =
                    new ContainerColorUpgrade(windowID, playerInventory,
                            itemStackColorUpgrade);
            return newContainerServerSide;
        }
    }

    public static class ContainerColorUpgrade extends UpgradeContainerBase {

        public final ItemStack itemStackBeingHeld;

        public ContainerColorUpgrade(int windowId, PlayerInventory playerInv,
                                     ItemStack itemStackBeingHeld) {
            super(Registration.COLOR_UPGRADE_CONTAINER.get(), windowId, playerInv, itemStackBeingHeld);
            this.itemStackBeingHeld = itemStackBeingHeld;

        }

    }
}


