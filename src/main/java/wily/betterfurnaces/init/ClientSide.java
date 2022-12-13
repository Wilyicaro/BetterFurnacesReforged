package wily.betterfurnaces.init;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.blocks.AbstractSmeltingBlock;
import wily.betterfurnaces.screens.*;

@Mod.EventBusSubscriber(modid = BetterFurnacesReforged.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSide {

    public static void init(final FMLClientSetupEvent event) {
        ScreenManager.register(Registration.IRON_FURNACE_CONTAINER.get(), IronFurnaceScreen::new);
        ScreenManager.register(Registration.GOLD_FURNACE_CONTAINER.get(), GoldFurnaceScreen::new);
        ScreenManager.register(Registration.DIAMOND_FURNACE_CONTAINER.get(), DiamondFurnaceScreen::new);
        ScreenManager.register(Registration.NETHERHOT_FURNACE_CONTAINER.get(), NetherhotFurnaceScreen::new);
        ScreenManager.register(Registration.EXTREME_FURNACE_CONTAINER.get(), ExtremeFurnaceScreen::new);
        ScreenManager.register(Registration.EXTREME_FORGE_CONTAINER.get(), ExtremeForgeScreen::new);
        ScreenManager.register(Registration.COLOR_UPGRADE_CONTAINER.get(), ColorUpgradeScreen::new);
        ScreenManager.register(Registration.COB_GENERATOR_CONTAINER.get(), AbstractCobblestoneGeneratorScreen.CobblestoneGeneratorScreen::new);
        ScreenManager.register(Registration.FUEL_VERIFIER_CONTAINER.get(), AbstractFuelVerifierScreen.FuelVerifierScreen::new);
        BetterFurnacesReforged.LOGGER.info("Initializing Better Furnaces Block Colors registering...");
        ItemColors itemColors = event.getMinecraftSupplier().get().getItemColors();
        BlockColors blockColors = event.getMinecraftSupplier().get().getBlockColors();
        for (Block block : ForgeRegistries.BLOCKS.getValues())
            if (block instanceof AbstractSmeltingBlock) {
                RenderTypeLookup.setRenderLayer(block, RenderType.cutoutMipped());
                blockColors.register(BlockColorsHandler.COLOR, block);
                itemColors.register(ItemColorsHandler.COLOR, block.asItem());
                event.enqueueWork(() ->
                {
                    ItemModelsProperties.register(block.asItem(),
                            new ResourceLocation(BetterFurnacesReforged.MOD_ID, "colored"), (stack, level, living) -> ItemColorsHandler.itemContainsColor(stack.getOrCreateTag()) ? 1.0F : 0.0F);
                });
            }
    }



}