package wily.betterfurnaces.init;

import dev.architectury.registry.client.rendering.ColorHandlerRegistry;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import dev.architectury.registry.item.ItemPropertiesRegistry;
import dev.architectury.registry.menu.MenuRegistry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.blocks.AbstractSmeltingBlock;
import wily.betterfurnaces.screens.*;


public class ClientSide {

    public static void init() {


        MenuRegistry.registerScreenFactory(Registration.IRON_FURNACE_CONTAINER.get(), IronFurnaceScreen::new);
        MenuRegistry.registerScreenFactory(Registration.GOLD_FURNACE_CONTAINER.get(), GoldFurnaceScreen::new);
        MenuRegistry.registerScreenFactory(Registration.DIAMOND_FURNACE_CONTAINER.get(), DiamondFurnaceScreen::new);
        MenuRegistry.registerScreenFactory(Registration.NETHERHOT_FURNACE_CONTAINER.get(), NetherhotFurnaceScreen::new);
        MenuRegistry.registerScreenFactory(Registration.EXTREME_FURNACE_CONTAINER.get(), ExtremeFurnaceScreen::new);
        MenuRegistry.registerScreenFactory(Registration.EXTREME_FORGE_CONTAINER.get(), ExtremeForgeScreen::new);
        MenuRegistry.registerScreenFactory(Registration.COLOR_UPGRADE_CONTAINER.get(), ColorUpgradeScreen::new);
        MenuRegistry.registerScreenFactory(Registration.COB_GENERATOR_CONTAINER.get(), AbstractCobblestoneGeneratorScreen.CobblestoneGeneratorScreen::new);
        MenuRegistry.registerScreenFactory(Registration.FUEL_VERIFIER_CONTAINER.get(), AbstractFuelVerifierScreen.FuelVerifierScreen::new);
        RenderTypeRegistry.register(RenderType.cutoutMipped(), Registration.IRON_FURNACE.get(),Registration.GOLD_FURNACE.get(),Registration.DIAMOND_FURNACE.get(),Registration.NETHERHOT_FURNACE.get(),Registration.EXTREME_FURNACE.get(),Registration.EXTREME_FORGE.get());
                Registration.BLOCK_ITEMS.forEach((block)->{
                    if (block.get() instanceof AbstractSmeltingBlock) {
                        ItemPropertiesRegistry.register(block.get().asItem(),
                                new ResourceLocation(BetterFurnacesReforged.MOD_ID, "colored"), (stack, level, living, id) -> ItemColorsHandler.itemContainsColor(stack.getOrCreateTag()) ? 1.0F : 0.0F);
                    }
                    ColorHandlerRegistry.registerBlockColors(BlockColorsHandler.COLOR, block.get());
                    ColorHandlerRegistry.registerItemColors(ItemColorsHandler.COLOR,block.get().asItem());
                });

                

    }
}