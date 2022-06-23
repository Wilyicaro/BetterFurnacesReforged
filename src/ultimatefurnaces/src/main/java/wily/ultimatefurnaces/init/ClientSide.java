package wily.ultimatefurnaces.init;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import wily.ultimatefurnaces.UltimateFurnaces;
import wily.ultimatefurnaces.gui.*;

@Mod.EventBusSubscriber(modid = UltimateFurnaces.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSide {

    public static void init(final FMLClientSetupEvent event) {
        ScreenManager.register(RegistrationUF.COPPER_FURNACE_CONTAINER.get(), BlockCopperFurnaceScreen::new);
        ScreenManager.register(RegistrationUF.STEEL_FURNACE_CONTAINER.get(), BlockSteelFurnaceScreen::new);
        ScreenManager.register(RegistrationUF.AMETHYST_FURNACE_CONTAINER.get(), BlockAmethystFurnaceScreen::new);
        ScreenManager.register(RegistrationUF.PLATINUM_FURNACE_CONTAINER.get(), BlockPlatinumFurnaceScreen::new);
        ScreenManager.register(RegistrationUF.ULTIMATE_FURNACE_CONTAINER.get(), BlockUltimateFurnaceScreen::new);
        ScreenManager.register(RegistrationUF.COPPER_FORGE_CONTAINER.get(), BlockCopperForgeScreen::new);
        ScreenManager.register(RegistrationUF.IRON_FORGE_CONTAINER.get(), BlockIronForgeScreen::new);
        ScreenManager.register(RegistrationUF.GOLD_FORGE_CONTAINER.get(), BlockGoldForgeScreen::new);
        ScreenManager.register(RegistrationUF.DIAMOND_FORGE_CONTAINER.get(), BlockDiamondForgeScreen::new);
        ScreenManager.register(RegistrationUF.NETHERHOT_FORGE_CONTAINER.get(), BlockNetherhotForgeScreen::new);
        ScreenManager.register(RegistrationUF.ULTIMATE_FORGE_CONTAINER.get(), BlockUltimateForgeScreen::new);
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