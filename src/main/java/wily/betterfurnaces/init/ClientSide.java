package wily.betterfurnaces.init;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.gui.*;

@Mod.EventBusSubscriber(modid = BetterFurnacesReforged.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSide {

    public static void init(final FMLClientSetupEvent event) {
        ScreenManager.register(Registration.IRON_FURNACE_CONTAINER.get(), BlockIronFurnaceScreen::new);
        ScreenManager.register(Registration.GOLD_FURNACE_CONTAINER.get(), BlockGoldFurnaceScreen::new);
        ScreenManager.register(Registration.DIAMOND_FURNACE_CONTAINER.get(), BlockDiamondFurnaceScreen::new);
        ScreenManager.register(Registration.NETHERHOT_FURNACE_CONTAINER.get(), BlockNetherhotFurnaceScreen::new);
        ScreenManager.register(Registration.EXTREME_FURNACE_CONTAINER.get(), BlockExtremeFurnaceScreen::new);
        ScreenManager.register(Registration.EXTREME_FORGE_CONTAINER.get(), BlockExtremeForgeScreen::new);
        ScreenManager.register(Registration.COLOR_UPGRADE_CONTAINER.get(), ItemColorScreen::new);
        ScreenManager.register(Registration.COB_GENERATOR_CONTAINER.get(), BlockCobblestoneGeneratorScreen.BlockCobblestoneGeneratorScreenDefinition::new);
        ScreenManager.register(Registration.FUEL_VERIFIER_CONTAINER.get(), BlockFuelVerifierScreen.BlockFuelVerifierScreenDefiniton::new);
        RenderTypeLookup.setRenderLayer(Registration.IRON_FURNACE.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(Registration.GOLD_FURNACE.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(Registration.DIAMOND_FURNACE.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(Registration.NETHERHOT_FURNACE.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(Registration.EXTREME_FURNACE.get(), RenderType.cutout());
        RenderTypeLookup.setRenderLayer(Registration.EXTREME_FORGE.get(), RenderType.translucent());
        BlockColorsHandler.registerBlockColors();
        ItemColorsHandler.registerItemColors();
        event.enqueueWork(() ->
        {
            ItemModelsProperties.register(Registration.EXTREME_FURNACE_ITEM.get(),
                    new ResourceLocation(BetterFurnacesReforged.MOD_ID, "colored"), (stack, level, living) -> {
                        return stack.getOrCreateTag().getBoolean("colored") ? 1.0F : 0.0F;
                    });
            ItemModelsProperties.register(Registration.EXTREME_FORGE_ITEM.get(),
                    new ResourceLocation(BetterFurnacesReforged.MOD_ID, "colored"), (stack, level, living) -> {
                        return stack.getOrCreateTag().getBoolean("colored") ? 1.0F : 0.0F;
                    });
        });

    }

}