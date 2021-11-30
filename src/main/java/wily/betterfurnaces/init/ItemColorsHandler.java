package wily.betterfurnaces.init;

import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ItemColorsHandler implements ItemColor {
    public static final ItemColor COLOR = new ItemColorsHandler();

    @SubscribeEvent
    public static void registerItemColors() {
        System.out.println("Registering item color handler");
        Minecraft.getInstance().getItemColors().register(COLOR, Registration.COLOR_FURNACE.get());
        Minecraft.getInstance().getItemColors().register(COLOR, Registration.COLOR_FORGE.get());
    }

    @Override
    public int getColor(ItemStack stack, int i) {
        CompoundTag nbt = stack.getTag();
            if ((stack.getTag() != null) &&(stack.getItem() == Registration.COLOR_FURNACE.get() || stack.getItem() == Registration.COLOR_FORGE.get())) {
                return ((nbt.getInt("red") & 0x0ff) << 16) | ((nbt.getInt("green") & 0x0ff) << 8) | (nbt.getInt("blue") & 0x0ff);
            }else {
            return 0xFFFFFF;
        }
    }

}
