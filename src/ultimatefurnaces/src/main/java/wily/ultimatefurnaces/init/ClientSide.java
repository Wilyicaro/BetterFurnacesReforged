package wily.ultimatefurnaces.init;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import wily.ultimatefurnaces.UltimateFurnaces;
import wily.ultimatefurnaces.screens.*;

@Mod.EventBusSubscriber(modid = UltimateFurnaces.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSide {

    public static void init(final FMLClientSetupEvent event) {
        ScreenManager.register(RegistrationUF.COPPER_FURNACE_CONTAINER.get(), CopperFurnaceScreen::new);
        ScreenManager.register(RegistrationUF.STEEL_FURNACE_CONTAINER.get(), SteelFurnaceScreen::new);
        ScreenManager.register(RegistrationUF.AMETHYST_FURNACE_CONTAINER.get(), AmethystFurnaceScreen::new);
        ScreenManager.register(RegistrationUF.PLATINUM_FURNACE_CONTAINER.get(), PlatinumFurnaceScreen::new);
        ScreenManager.register(RegistrationUF.ULTIMATE_FURNACE_CONTAINER.get(), UltimateFurnaceScreen::new);
        ScreenManager.register(RegistrationUF.COPPER_FORGE_CONTAINER.get(), CopperForgeScreen::new);
        ScreenManager.register(RegistrationUF.IRON_FORGE_CONTAINER.get(), IronForgeScreen::new);
        ScreenManager.register(RegistrationUF.GOLD_FORGE_CONTAINER.get(), GoldForgeScreen::new);
        ScreenManager.register(RegistrationUF.DIAMOND_FORGE_CONTAINER.get(), DiamondForgeScreen::new);
        ScreenManager.register(RegistrationUF.NETHERHOT_FORGE_CONTAINER.get(), NetherhotForgeScreen::new);
        ScreenManager.register(RegistrationUF.ULTIMATE_FORGE_CONTAINER.get(), UltimateForgeScreen::new);
        RenderTypeLookup.setRenderLayer(RegistrationUF.COPPER_FURNACE.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(RegistrationUF.STEEL_FURNACE.get(), RenderType.cutoutMipped());
        RenderTypeLookup.setRenderLayer(RegistrationUF.AMETHYST_FURNACE.get(), RenderType.cutoutMipped());
        RenderTypeLookup.setRenderLayer(RegistrationUF.PLATINUM_FURNACE.get(), RenderType.cutoutMipped());
        RenderTypeLookup.setRenderLayer(RegistrationUF.ULTIMATE_FURNACE.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(RegistrationUF.COPPER_FORGE.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(RegistrationUF.IRON_FORGE.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(RegistrationUF.GOLD_FORGE.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(RegistrationUF.DIAMOND_FORGE.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(RegistrationUF.NETHERHOT_FORGE.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(RegistrationUF.ULTIMATE_FORGE.get(), RenderType.cutout());
        BlockColorsHandler.registerBlockColors();

    }

}