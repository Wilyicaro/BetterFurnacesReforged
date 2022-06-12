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
        MenuScreens.register(RegistrationUF.COPPER_FURNACE_CONTAINER.get(), BlockCopperFurnaceScreen::new);
        MenuScreens.register(RegistrationUF.STEEL_FURNACE_CONTAINER.get(), BlockSteelFurnaceScreen::new);
        MenuScreens.register(RegistrationUF.AMETHYST_FURNACE_CONTAINER.get(), BlockAmethystFurnaceScreen::new);
        MenuScreens.register(RegistrationUF.PLATINUM_FURNACE_CONTAINER.get(), BlockPlatinumFurnaceScreen::new);
        MenuScreens.register(RegistrationUF.ULTIMATE_FURNACE_CONTAINER.get(), BlockUltimateFurnaceScreen::new);
        MenuScreens.register(RegistrationUF.COPPER_FORGE_CONTAINER.get(), BlockCopperForgeScreen::new);
        MenuScreens.register(RegistrationUF.IRON_FORGE_CONTAINER.get(), BlockIronForgeScreen::new);
        MenuScreens.register(RegistrationUF.GOLD_FORGE_CONTAINER.get(), BlockGoldForgeScreen::new);
        MenuScreens.register(RegistrationUF.DIAMOND_FORGE_CONTAINER.get(), BlockDiamondForgeScreen::new);
        MenuScreens.register(RegistrationUF.NETHERHOT_FORGE_CONTAINER.get(), BlockNetherhotForgeScreen::new);
        MenuScreens.register(RegistrationUF.ULTIMATE_FORGE_CONTAINER.get(), BlockUltimateForgeScreen::new);
        ItemBlockRenderTypes.setRenderLayer(RegistrationUF.COPPER_FURNACE.get(), RenderType.cutoutMipped());
        ItemBlockRenderTypes.setRenderLayer(RegistrationUF.STEEL_FURNACE.get(), RenderType.cutoutMipped());
        ItemBlockRenderTypes.setRenderLayer(RegistrationUF.AMETHYST_FURNACE.get(), RenderType.cutoutMipped());
        ItemBlockRenderTypes.setRenderLayer(RegistrationUF.PLATINUM_FURNACE.get(), RenderType.cutoutMipped());
        ItemBlockRenderTypes.setRenderLayer(RegistrationUF.ULTIMATE_FURNACE.get(), RenderType.cutoutMipped());
        ItemBlockRenderTypes.setRenderLayer(RegistrationUF.COPPER_FORGE.get(), RenderType.cutoutMipped());
        ItemBlockRenderTypes.setRenderLayer(RegistrationUF.IRON_FORGE.get(), RenderType.cutoutMipped());
        ItemBlockRenderTypes.setRenderLayer(RegistrationUF.GOLD_FORGE.get(), RenderType.cutoutMipped());
        ItemBlockRenderTypes.setRenderLayer(RegistrationUF.DIAMOND_FORGE.get(), RenderType.cutoutMipped());
        ItemBlockRenderTypes.setRenderLayer(RegistrationUF.NETHERHOT_FORGE.get(), RenderType.cutoutMipped());
        ItemBlockRenderTypes.setRenderLayer(RegistrationUF.ULTIMATE_FORGE.get(), RenderType.cutoutMipped());
        BlockColorsHandlerUF.registerBlockColors();

    }

}