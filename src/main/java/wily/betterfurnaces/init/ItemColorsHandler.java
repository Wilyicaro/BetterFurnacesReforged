package wily.betterfurnaces.init;

import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.items.ItemStackHandler;

public class ItemColorsHandler implements ItemColor {
    public static final ItemColor COLOR = new ItemColorsHandler();
    public static int colorFromTag(CompoundTag tag){
        return ((tag.getInt("red") & 0x0ff) << 16) | ((tag.getInt("green") & 0x0ff) << 8) | (tag.getInt("blue") & 0x0ff);
    }
    public static boolean itemContainsColor(CompoundTag tag){
        tag = setupInvColorTag(tag);
        return tag.contains("red") || tag.contains("green")|| tag.contains("blue");
    }
    public static CompoundTag setupInvColorTag(CompoundTag tag){
        if (tag.contains("BlockEntityTag")) {
            CompoundTag invTag =  tag.getCompound("BlockEntityTag").getCompound("inventory");
            ItemStackHandler handler = new ItemStackHandler();
            handler.deserializeNBT(invTag);
            for (int i = 0; i < handler.getSlots() ; i++) {
                ItemStack stack = handler.getStackInSlot(i);
                if (!stack.is(Registration.COLOR.get())) continue;
                return stack.getOrCreateTag();
            }
        }
        return tag;
    }
    public static void putColor(CompoundTag tag, int red, int green, int blue){
        tag.putInt("red", red);
        tag.putInt("green", green);
        tag.putInt("blue", blue);
    }
    @Override
    public int getColor(ItemStack stack, int i) {
        CompoundTag tag = setupInvColorTag(stack.getOrCreateTag());

            if ((stack.getTag() != null) && itemContainsColor(tag)) {
                return colorFromTag(tag);
            }else {
            return 0xFFFFFF;
        }
    }

}
