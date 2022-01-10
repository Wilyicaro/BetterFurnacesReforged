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
import wily.betterfurnaces.gui.*;

@Mod.EventBusSubscriber(modid = BetterFurnacesReforged.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSide {

    public static void init(final FMLClientSetupEvent event) {
        MenuScreens.register(Registration.IRON_FURNACE_CONTAINER.get(), BlockIronFurnaceScreen::new);
        MenuScreens.register(Registration.GOLD_FURNACE_CONTAINER.get(), BlockGoldFurnaceScreen::new);
        MenuScreens.register(Registration.DIAMOND_FURNACE_CONTAINER.get(), BlockDiamondFurnaceScreen::new);
        MenuScreens.register(Registration.NETHERHOT_FURNACE_CONTAINER.get(), BlockNetherhotFurnaceScreen::new);
        MenuScreens.register(Registration.EXTREME_FURNACE_CONTAINER.get(), BlockExtremeFurnaceScreen::new);
        MenuScreens.register(Registration.EXTREME_FORGE_CONTAINER.get(), BlockExtremeForgeScreen::new);
        MenuScreens.register(Registration.COLOR_UPGRADE_CONTAINER.get(), ItemUpgradeColorScreen::new);
        MenuScreens.register(Registration.COB_GENERATOR_CONTAINER.get(), BlockCobblestoneGeneratorScreen.BlockCobblestoneGeneratorScreenDefinition::new);
        MenuScreens.register(Registration.FUEL_VERIFIER_CONTAINER.get(), BlockFuelVerifierScreen.BlockFuelVerifierScreenDefiniton::new);
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