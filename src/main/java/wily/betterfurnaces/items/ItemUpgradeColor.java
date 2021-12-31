package wily.betterfurnaces.items;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.container.ItemUpgradeContainerBase;
import wily.betterfurnaces.gui.ItemColorScreen;
import wily.betterfurnaces.init.Registration;

import javax.annotation.Nullable;
import java.util.List;

public class ItemUpgradeColor extends ItemUpgrade {

    public ItemUpgradeColor(Properties properties) {
        super(properties);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TranslatableComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".upgrade.color").setStyle(Style.EMPTY.applyFormat((ChatFormatting.GRAY))));
    }

    public InteractionResultHolder<ItemStack> use(Level world, Player entity, InteractionHand hand) {
        InteractionResultHolder<ItemStack> ar = super.use(world, entity, hand);
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();
        if (entity instanceof ServerPlayer) {
            ItemStack stack = entity.getItemInHand(InteractionHand.MAIN_HAND);
            MenuProvider ContainerProviderColorUpgrade = new ContainerProviderColorUpgrade(this, stack);
            NetworkHooks.openGui((ServerPlayer) entity, ContainerProviderColorUpgrade, buf -> {
                buf.writeBlockPos(new BlockPos(x, y, z));
                buf.writeByte(hand == InteractionHand.MAIN_HAND ? 0 : 1);
            });
        }
        return ar;
    }
    public void inventoryTick(ItemStack stack, Level world, Entity player, int slot, boolean selected) {
        super.inventoryTick(stack, world, player, slot, selected);
        ItemStack itemStack = stack;
        CompoundTag nbt;
        nbt = itemStack.getOrCreateTag();

        if (!(nbt.contains("red") && nbt.contains("green") && nbt.contains("blue"))) {
            nbt.putInt("red", 255);
            nbt.putInt("green", 255);
            nbt.putInt("blue", 255);
            itemStack.setTag(nbt);
        }
        if ((Minecraft.getInstance().screen) instanceof ItemColorScreen && player instanceof Player && ((Player) player).getMainHandItem() == stack) {
            ItemColorScreen color =  (ItemColorScreen) Minecraft.getInstance().screen;
            if (color.red != null) {
                int red = color.red.getValueInt();
                if (red != nbt.getInt("red") && color.red.isHoveredOrFocused())
                nbt.putInt("red", red);
            }
            if (color.green != null) {
                int green = color.green.getValueInt();
                if (green != nbt.getInt("green") && color.green.isHoveredOrFocused())
                    nbt.putInt("green", green);
            }
            if (color.blue != null) {
                int blue = color.blue.getValueInt();
                if (blue != nbt.getInt("blue") && color.blue.isHoveredOrFocused())
                    nbt.putInt("blue", blue);
            }
        }
    }
    private static class ContainerProviderColorUpgrade implements MenuProvider {
        public ContainerProviderColorUpgrade(ItemUpgradeColor item, ItemStack stack) {
            this.itemStackColorUpgrade = stack;
        }

        @Override
        public Component getDisplayName() {
            return new TranslatableComponent("item.betterfurnacesreforged.color_upgrade");
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
    public static class ContainerColorUpgrade extends ItemUpgradeContainerBase {

        public final ItemStack itemStackBeingHeld;

        public ContainerColorUpgrade(int windowId, Inventory playerInv,
                                     ItemStack itemStackBeingHeld) {
            super(Registration.COLOR_UPGRADE_CONTAINER.get(), windowId, playerInv, itemStackBeingHeld);
            this.itemStackBeingHeld = itemStackBeingHeld;

        }

        }
    }


