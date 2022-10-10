package wily.betterfurnaces.init;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
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
        event.enqueueWork(() ->{
                for (Block block : ForgeRegistries.BLOCKS.getValues()) {
                    if (block instanceof AbstractSmeltingBlock)
                        ItemProperties.register(block.asItem(),
                            new ResourceLocation(BetterFurnacesReforged.MOD_ID, "colored"), (stack, level, living, id) -> ItemColorsHandler.itemContainsColor(stack.getOrCreateTag()) ? 1.0F : 0.0F);
                }
        });
    }

    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event){
        BetterFurnacesReforged.LOGGER.info("Initializing Better Furnaces Item Colors registering...");
        for (Block block : ForgeRegistries.BLOCKS.getValues())
            if (block instanceof AbstractSmeltingBlock)
                event.register(ItemColorsHandler.COLOR, block.asItem());
    }

    @SubscribeEvent
    public static void registerBlockColors(RegisterColorHandlersEvent.Block event){
        BetterFurnacesReforged.LOGGER.info("Initializing Better Furnaces Block Colors registering...");
        for (Block block : ForgeRegistries.BLOCKS.getValues())
            if (block instanceof AbstractSmeltingBlock)
                event.register(BlockColorsHandler.COLOR, block);
    }

}