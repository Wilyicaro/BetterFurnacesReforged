package wily.ultimatefurnaces.init;

import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColor;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import wily.betterfurnaces.init.BlockColorsHandler;

public class BlockColorsHandlerUF extends BlockColorsHandler {
    public static final BlockColor COLOR = new BlockColorsHandler();

    @SubscribeEvent
    public static void registerBlockColors() {
        System.out.println("Registering block color handler");
        Minecraft.getInstance().getBlockColors().register(COLOR, Registration.COPPER_FURNACE.get());
        Minecraft.getInstance().getBlockColors().register(COLOR, Registration.ULTIMATE_FURNACE.get());
        Minecraft.getInstance().getBlockColors().register(COLOR, Registration.COPPER_FORGE.get());
        Minecraft.getInstance().getBlockColors().register(COLOR, Registration.IRON_FORGE.get());
        Minecraft.getInstance().getBlockColors().register(COLOR, Registration.GOLD_FORGE.get());
        Minecraft.getInstance().getBlockColors().register(COLOR, Registration.DIAMOND_FORGE.get());
        Minecraft.getInstance().getBlockColors().register(COLOR, Registration.NETHERHOT_FORGE.get());
        Minecraft.getInstance().getBlockColors().register(COLOR, Registration.ULTIMATE_FORGE.get());
    }
}
