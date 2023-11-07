package wily.betterfurnaces.client;

import net.minecraft.client.color.item.ItemColor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import wily.betterfurnaces.init.ModObjects;
import wily.factoryapi.FactoryAPIPlatform;
import wily.factoryapi.base.IPlatformItemHandler;

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
            IPlatformItemHandler handler = FactoryAPIPlatform.getItemHandlerApi(14,null);
            handler.deserializeTag(invTag);
            for (int i = 0; i < handler.getContainerSize() ; i++) {
                ItemStack stack = handler.getItem(i);
                if (stack.getItem() != ModObjects.COLOR.get()) continue;
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
