package wily.ultimatefurnaces.init;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import wily.ultimatefurnaces.UltimateFurnaces;
import wily.ultimatefurnaces.screens.*;

@Mod.EventBusSubscriber(modid = UltimateFurnaces.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSide {

    public static void init(final FMLClientSetupEvent event) {
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

    }

}