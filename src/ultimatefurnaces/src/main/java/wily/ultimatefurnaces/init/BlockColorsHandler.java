package wily.ultimatefurnaces.init;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class BlockColorsHandler extends wily.betterfurnaces.init.BlockColorsHandler {
    public static final IBlockColor COLOR = new wily.betterfurnaces.init.BlockColorsHandler();

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
