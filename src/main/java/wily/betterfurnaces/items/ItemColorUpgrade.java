package wily.betterfurnaces.items;

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
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
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.container.ItemUpgradeContainerBase;
import wily.betterfurnaces.gui.ItemColorScreen;
import wily.betterfurnaces.init.Registration;

import javax.annotation.Nullable;
import java.util.List;

public class ItemColorUpgrade extends ItemUpgradeMisc {

    public ItemColorUpgrade(Properties properties) {
        super(properties);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".upgrade_right_click").setStyle(Style.EMPTY.applyFormat(TextFormatting.GOLD).withItalic(true)));
        tooltip.add(new TranslationTextComponent("tooltip." + BetterFurnacesReforged.MOD_ID + ".upgrade.color").setStyle(Style.EMPTY.applyFormat((TextFormatting.GRAY))));
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
        if ((Minecraft.getInstance().screen) instanceof ItemColorScreen && player instanceof PlayerEntity && ((PlayerEntity) player).getMainHandItem() == stack) {
            ItemColorScreen color =  (ItemColorScreen) Minecraft.getInstance().screen;
            if (color.red != null) {
                int red = color.red.getValueInt();
                if (red != nbt.getInt("red") && color.red.isHovered())
                    nbt.putInt("red", red);
            }
            if (color.green != null) {
                int green = color.green.getValueInt();
                if (green != nbt.getInt("green") && color.green.isHovered())
                    nbt.putInt("green", green);
            }
            if (color.blue != null) {
                int blue = color.blue.getValueInt();
                if (blue != nbt.getInt("blue") && color.blue.isHovered())
                    nbt.putInt("blue", blue);
            }
            itemStack.setTag(nbt);
        }
    }
    private static class ContainerProviderColorUpgrade implements INamedContainerProvider {
        public ContainerProviderColorUpgrade(ItemColorUpgrade item, ItemStack stack) {
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

        private ItemStack itemStackColorUpgrade;
    }
    public static class ContainerColorUpgrade extends ItemUpgradeContainerBase {

        public final ItemStack itemStackBeingHeld;

        public ContainerColorUpgrade(int windowId, PlayerInventory playerInv,
                                     ItemStack itemStackBeingHeld) {
            super(Registration.COLOR_UPGRADE_CONTAINER.get(), windowId, playerInv, itemStackBeingHeld);
            this.itemStackBeingHeld = itemStackBeingHeld;

        }

        }
    }


