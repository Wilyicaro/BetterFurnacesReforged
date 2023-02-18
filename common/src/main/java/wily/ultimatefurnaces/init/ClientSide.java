package wily.ultimatefurnaces.init;

import dev.architectury.registry.menu.MenuRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import wily.ultimatefurnaces.screens.*;


public class ClientSide {

    public static void init() {
        MenuRegistry.registerScreenFactory(RegistrationUF.COPPER_FURNACE_CONTAINER.get(), CopperFurnaceScreen::new);
        MenuRegistry.registerScreenFactory(RegistrationUF.STEEL_FURNACE_CONTAINER.get(), SteelFurnaceScreen::new);
        MenuRegistry.registerScreenFactory(RegistrationUF.AMETHYST_FURNACE_CONTAINER.get(), AmethystFurnaceScreen::new);
        MenuRegistry.registerScreenFactory(RegistrationUF.PLATINUM_FURNACE_CONTAINER.get(), PlatinumFurnaceScreen::new);
        MenuRegistry.registerScreenFactory(RegistrationUF.ULTIMATE_FURNACE_CONTAINER.get(), UltimateFurnaceScreen::new);
        MenuRegistry.registerScreenFactory(RegistrationUF.COPPER_FORGE_CONTAINER.get(), CopperForgeScreen::new);
        MenuRegistry.registerScreenFactory(RegistrationUF.IRON_FORGE_CONTAINER.get(), IronForgeScreen::new);
        MenuRegistry.registerScreenFactory(RegistrationUF.GOLD_FORGE_CONTAINER.get(), GoldForgeScreen::new);
        MenuRegistry.registerScreenFactory(RegistrationUF.DIAMOND_FORGE_CONTAINER.get(), DiamondForgeScreen::new);
        MenuRegistry.registerScreenFactory(RegistrationUF.NETHERHOT_FORGE_CONTAINER.get(), NetherhotForgeScreen::new);
        MenuRegistry.registerScreenFactory(RegistrationUF.ULTIMATE_FORGE_CONTAINER.get(), UltimateForgeScreen::new);
        wily.betterfurnaces.init.ClientSide.registerBetterFurnacesBlocksClientSide(RegistrationUF.BLOCK_ITEMS);
    }

}