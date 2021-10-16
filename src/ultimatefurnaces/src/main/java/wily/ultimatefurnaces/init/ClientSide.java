package wily.ultimatefurnaces.init;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import wily.ultimatefurnaces.UltimateFurnaces;
import wily.betterfurnaces.gui.*;
import wily.ultimatefurnaces.gui.*;

@Mod.EventBusSubscriber(modid = UltimateFurnaces.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSide {

    public static void init(final FMLClientSetupEvent event) {
        ScreenManager.register(Registration.COPPER_FURNACE_CONTAINER.get(), BlockCopperFurnaceScreen::new);
        ScreenManager.register(Registration.ULTIMATE_FURNACE_CONTAINER.get(), BlockUltimateFurnaceScreen::new);
        ScreenManager.register(Registration.COPPER_FORGE_CONTAINER.get(), BlockCopperForgeScreen::new);
        ScreenManager.register(Registration.IRON_FORGE_CONTAINER.get(), BlockIronForgeScreen::new);
        ScreenManager.register(Registration.GOLD_FORGE_CONTAINER.get(), BlockGoldForgeScreen::new);
        ScreenManager.register(Registration.DIAMOND_FORGE_CONTAINER.get(), BlockDiamondForgeScreen::new);
        ScreenManager.register(Registration.NETHERHOT_FORGE_CONTAINER.get(), BlockNetherhotForgeScreen::new);
        ScreenManager.register(Registration.ULTIMATE_FORGE_CONTAINER.get(), BlockUltimateForgeScreen::new);
        RenderTypeLookup.setRenderLayer(Registration.COPPER_FURNACE.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(Registration.ULTIMATE_FURNACE.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(Registration.COPPER_FORGE.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(Registration.IRON_FORGE.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(Registration.GOLD_FORGE.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(Registration.DIAMOND_FORGE.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(Registration.NETHERHOT_FORGE.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(Registration.ULTIMATE_FORGE.get(), RenderType.cutout());
        BlockColorsHandler.registerBlockColors();

    }

}