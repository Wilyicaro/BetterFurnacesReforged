package wily.betterfurnaces.init;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ItemColorsHandler implements IItemColor {
    public static final IItemColor COLOR = new ItemColorsHandler();

    @SubscribeEvent
    public static void registerItemColors() {
        System.out.println("Starting Better Furnaces ItemColorsHandler");
        Minecraft.getInstance().getItemColors().register(COLOR, Registration.EXTREME_FURNACE_ITEM.get());
        Minecraft.getInstance().getItemColors().register(COLOR, Registration.EXTREME_FORGE_ITEM.get());
    }

    @Override
    public int getColor(ItemStack stack, int i) {
        CompoundNBT nbt = stack.getOrCreateTag();
        if ((stack.getTag() != null) && (stack.getItem() == Registration.EXTREME_FURNACE_ITEM.get() || stack.getItem() == Registration.EXTREME_FORGE_ITEM.get()) && nbt.getBoolean("colored")) {
            return ((nbt.getInt("red") & 0x0ff) << 16) | ((nbt.getInt("green") & 0x0ff) << 8) | (nbt.getInt("blue") & 0x0ff);
        } else {
            return 0xFFFFFF;
        }
    }

}
