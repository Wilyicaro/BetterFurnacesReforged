package wily.betterfurnaces.init;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class ItemColorsHandler implements IItemColor {
    public static final IItemColor COLOR = new ItemColorsHandler();
    public static int colorFromTag(CompoundNBT tag){
        return ((tag.getInt("red") & 0x0ff) << 16) | ((tag.getInt("green") & 0x0ff) << 8) | (tag.getInt("blue") & 0x0ff);
    }
    public static boolean itemContainsColor(CompoundNBT tag){
        tag = setupInvColorTag(tag);
        return tag.contains("red") || tag.contains("green")|| tag.contains("blue");
    }
    public static CompoundNBT setupInvColorTag(CompoundNBT tag){
        if (tag.contains("BlockEntityTag")) {
            CompoundNBT invTag =  tag.getCompound("BlockEntityTag");
            NonNullList<ItemStack> inventory = NonNullList.withSize(14,ItemStack.EMPTY);
            ItemStackHelper.loadAllItems(invTag, inventory);
            for (int i = 0; i < inventory.size(); i++) {
                ItemStack stack = inventory.get(i);
                if (!stack.getItem().equals(Registration.COLOR.get())) continue;
                return stack.getOrCreateTag();
            }
        }
        return tag;
    }
    public static void putColor(CompoundNBT tag, int red, int green, int blue){
        tag.putInt("red", red);
        tag.putInt("green", green);
        tag.putInt("blue", blue);
    }
    @Override
    public int getColor(ItemStack stack, int i) {
        CompoundNBT tag = setupInvColorTag(stack.getOrCreateTag());

        if ((stack.getTag() != null) && itemContainsColor(tag)) {
            return colorFromTag(tag);
        }else {
            return 0xFFFFFF;
        }
    }

}
