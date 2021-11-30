package wily.ultimatefurnaces.init;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import wily.ultimatefurnaces.UltimateFurnaces;
import wily.ultimatefurnaces.gui.*;

@Mod.EventBusSubscriber(modid = UltimateFurnaces.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSide {

    public static void init(final FMLClientSetupEvent event) {
        MenuScreens.register(Registration.COPPER_FURNACE_CONTAINER.get(), BlockCopperFurnaceScreen::new);
        MenuScreens.register(Registration.ULTIMATE_FURNACE_CONTAINER.get(), BlockUltimateFurnaceScreen::new);
        MenuScreens.register(Registration.COPPER_FORGE_CONTAINER.get(), BlockCopperForgeScreen::new);
        MenuScreens.register(Registration.IRON_FORGE_CONTAINER.get(), BlockIronForgeScreen::new);
        MenuScreens.register(Registration.GOLD_FORGE_CONTAINER.get(), BlockGoldForgeScreen::new);
        MenuScreens.register(Registration.DIAMOND_FORGE_CONTAINER.get(), BlockDiamondForgeScreen::new);
        MenuScreens.register(Registration.NETHERHOT_FORGE_CONTAINER.get(), BlockNetherhotForgeScreen::new);
        MenuScreens.register(Registration.ULTIMATE_FORGE_CONTAINER.get(), BlockUltimateForgeScreen::new);
        ItemBlockRenderTypes.setRenderLayer(Registration.COPPER_FURNACE.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(Registration.ULTIMATE_FURNACE.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(Registration.COPPER_FORGE.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(Registration.IRON_FORGE.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(Registration.GOLD_FORGE.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(Registration.DIAMOND_FORGE.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(Registration.NETHERHOT_FORGE.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(Registration.ULTIMATE_FORGE.get(), RenderType.cutout());
        BlockColorsHandler.registerBlockColors();

    }

}