package wily.betterfurnaces.init;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.screens.*;

@Mod.EventBusSubscriber(modid = BetterFurnacesReforged.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSide {

    public static void init(final FMLClientSetupEvent event) {
        MenuScreens.register(Registration.IRON_FURNACE_CONTAINER.get(), IronFurnaceScreen::new);
        MenuScreens.register(Registration.GOLD_FURNACE_CONTAINER.get(), GoldFurnaceScreen::new);
        MenuScreens.register(Registration.DIAMOND_FURNACE_CONTAINER.get(), DiamondFurnaceScreen::new);
        MenuScreens.register(Registration.NETHERHOT_FURNACE_CONTAINER.get(), NetherhotFurnaceScreen::new);
        MenuScreens.register(Registration.EXTREME_FURNACE_CONTAINER.get(), ExtremeFurnaceScreen::new);
        MenuScreens.register(Registration.EXTREME_FORGE_CONTAINER.get(), ExtremeForgeScreen::new);
        MenuScreens.register(Registration.COLOR_UPGRADE_CONTAINER.get(), ColorUpgradeScreen::new);
        MenuScreens.register(Registration.COB_GENERATOR_CONTAINER.get(), AbstractCobblestoneGeneratorScreen.CobblestoneGeneratorScreen::new);
        MenuScreens.register(Registration.FUEL_VERIFIER_CONTAINER.get(), AbstractFuelVerifierScreen.FuelVerifierScreen::new);
        ItemBlockRenderTypes.setRenderLayer(Registration.IRON_FURNACE.get(), RenderType.cutoutMipped());
        ItemBlockRenderTypes.setRenderLayer(Registration.GOLD_FURNACE.get(), RenderType.cutoutMipped());
        ItemBlockRenderTypes.setRenderLayer(Registration.DIAMOND_FURNACE.get(), RenderType.cutoutMipped());
        ItemBlockRenderTypes.setRenderLayer(Registration.NETHERHOT_FURNACE.get(), RenderType.cutoutMipped());
        ItemBlockRenderTypes.setRenderLayer(Registration.EXTREME_FURNACE.get(), RenderType.cutoutMipped());
        ItemBlockRenderTypes.setRenderLayer(Registration.EXTREME_FORGE.get(), RenderType.cutoutMipped());
        BlockColorsHandler.registerBlockColors();
        ItemColorsHandler.registerItemColors();
        event.enqueueWork(() ->
        {
            ItemProperties.register(Registration.EXTREME_FURNACE_ITEM.get(),
                    new ResourceLocation(BetterFurnacesReforged.MOD_ID, "colored"), (stack, level, living, id) -> {
                        return stack.getOrCreateTag().getBoolean("colored") ? 1.0F : 0.0F;
                    });
            ItemProperties.register(Registration.EXTREME_FORGE_ITEM.get(),
                    new ResourceLocation(BetterFurnacesReforged.MOD_ID, "colored"), (stack, level, living, id) -> {
                        return stack.getOrCreateTag().getBoolean("colored") ? 1.0F : 0.0F;
                    });
        });
    }

}