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
        Minecraft.getInstance().getBlockColors().register(COLOR, RegistrationUF.COPPER_FURNACE.get());
        Minecraft.getInstance().getBlockColors().register(COLOR, RegistrationUF.ULTIMATE_FURNACE.get());
        Minecraft.getInstance().getBlockColors().register(COLOR, RegistrationUF.COPPER_FORGE.get());
        Minecraft.getInstance().getBlockColors().register(COLOR, RegistrationUF.IRON_FORGE.get());
        Minecraft.getInstance().getBlockColors().register(COLOR, RegistrationUF.GOLD_FORGE.get());
        Minecraft.getInstance().getBlockColors().register(COLOR, RegistrationUF.DIAMOND_FORGE.get());
        Minecraft.getInstance().getBlockColors().register(COLOR, RegistrationUF.NETHERHOT_FORGE.get());
        Minecraft.getInstance().getBlockColors().register(COLOR, RegistrationUF.ULTIMATE_FORGE.get());
    }
}
