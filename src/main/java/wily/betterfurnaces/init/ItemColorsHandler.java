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
        Minecraft.getInstance().getItemColors().register(COLOR, Registration.EXTREME_FURNACE_ITEM.get());
        Minecraft.getInstance().getItemColors().register(COLOR, Registration.EXTREME_FORGE_ITEM.get());
    }

    @Override
    public int getColor(ItemStack stack, int i) {
        CompoundTag nbt = stack.getOrCreateTag();
        if ((stack.getTag() != null) && (stack.getItem() == Registration.EXTREME_FURNACE_ITEM.get() || stack.getItem() == Registration.EXTREME_FORGE_ITEM.get()) && nbt.getBoolean("colored")) {
            return ((nbt.getInt("red") & 0x0ff) << 16) | ((nbt.getInt("green") & 0x0ff) << 8) | (nbt.getInt("blue") & 0x0ff);
        }else {
            return 0xFFFFFF;
        }
    }


}
