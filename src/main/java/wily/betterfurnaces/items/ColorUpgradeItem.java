package wily.betterfurnaces.items;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
//? <=1.21.1 {
/*import net.minecraft.world.InteractionResultHolder;
*///?}
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import wily.betterfurnaces.init.ModObjects;
import wily.betterfurnaces.inventory.AbstractUpgradeMenu;

public class ColorUpgradeItem extends UpgradeItem {

    public ColorUpgradeItem(Properties properties, String tooltip) {
        super(properties, Type.COLOR, tooltip);
    }

    public static boolean itemContainsColor(ItemStack stack){
        //? if <1.20.5 {
        /*CompoundTag tag = stack.getTag();
        return tag != null && (tag.contains("red") || tag.contains("green") || tag.contains("blue"));
        *///?} else {
        return stack.has(ModObjects.BLOCK_TINT.get());
        //?}
    }

    //? if <1.20.5 {
    /*public static int colorFromTag(CompoundTag tag){
        return ((tag.getInt("red") & 0x0ff) << 16) | ((tag.getInt("green") & 0x0ff) << 8) | (tag.getInt("blue") & 0x0ff);
    }

    public static void putColor(CompoundTag tag, int red, int green, int blue){
        tag.putInt("red", red);
        tag.putInt("green", green);
        tag.putInt("blue", blue);
    }
    *///?}

    public /*? if >=1.21.2 {*/ InteractionResult/*?} else {*//*InteractionResultHolder<ItemStack>*//*?}*/ use(Level world, Player entity, InteractionHand hand) {
        var ar = super.use(world, entity, hand);

        if (entity instanceof ServerPlayer) {
            ItemStack stack = entity.getItemInHand(InteractionHand.MAIN_HAND);
            entity.openMenu(new SimpleMenuProvider((window, inv, player)-> new ColorUpgradeMenu(window, inv), stack.getHoverName()));
        }
        return ar;
    }
    //? if <1.20.5 {
    /*public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        CompoundTag nbt = stack.getOrCreateTag();

        if (!(nbt.contains("red") && nbt.contains("green") && nbt.contains("blue"))) {
            nbt.putInt("red", 255);
            nbt.putInt("green", 255);
            nbt.putInt("blue", 255);
            stack.setTag(nbt);
        }
    }
    *///?}
    public static class ColorUpgradeMenu extends AbstractUpgradeMenu {
        public ColorUpgradeMenu(int windowId, Inventory playerInv) {
            super(ModObjects.COLOR_UPGRADE_CONTAINER.get(), windowId, playerInv, playerInv.player.getMainHandItem());
        }

        @Override
        public ItemStack quickMoveStack(Player p_38941_, int p_38942_) {
            return null;
        }
    }
}


