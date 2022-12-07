package wily.ultimatefurnaces.init;

import net.minecraft.client.gui.screens.MenuScreens;
import wily.ultimatefurnaces.screens.*;


public class ClientSide {

    public static void init() {
        MenuScreens.register(RegistrationUF.COPPER_FURNACE_CONTAINER.get(), CopperFurnaceScreen::new);
        MenuScreens.register(RegistrationUF.STEEL_FURNACE_CONTAINER.get(), SteelFurnaceScreen::new);
        MenuScreens.register(RegistrationUF.AMETHYST_FURNACE_CONTAINER.get(), AmethystFurnaceScreen::new);
        MenuScreens.register(RegistrationUF.PLATINUM_FURNACE_CONTAINER.get(), PlatinumFurnaceScreen::new);
        MenuScreens.register(RegistrationUF.ULTIMATE_FURNACE_CONTAINER.get(), UltimateFurnaceScreen::new);
        MenuScreens.register(RegistrationUF.COPPER_FORGE_CONTAINER.get(), CopperForgeScreen::new);
        MenuScreens.register(RegistrationUF.IRON_FORGE_CONTAINER.get(), IronForgeScreen::new);
        MenuScreens.register(RegistrationUF.GOLD_FORGE_CONTAINER.get(), GoldForgeScreen::new);
        MenuScreens.register(RegistrationUF.DIAMOND_FORGE_CONTAINER.get(), DiamondForgeScreen::new);
        MenuScreens.register(RegistrationUF.NETHERHOT_FORGE_CONTAINER.get(), NetherhotForgeScreen::new);
        MenuScreens.register(RegistrationUF.ULTIMATE_FORGE_CONTAINER.get(), UltimateForgeScreen::new);
        wily.betterfurnaces.init.ClientSide.registerBetterFurnacesBlocksClientSide(RegistrationUF.BLOCK_ITEMS);
    }

}