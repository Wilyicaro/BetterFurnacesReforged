package wily.betterfurnaces.init;

import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
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
        MenuScreens.register(Registration.FUEL_VERIFIER_CONTAINER.get(), AbstractFuelVerifierScreen.BlockFuelVerifierScreen::new);
        BetterFurnacesReforged.LOGGER.info("Initializing Better Furnaces Block Colors registering...");
        ItemColors itemColors = Minecraft.getInstance().getItemColors();
        BlockColors blockColors = Minecraft.getInstance().getBlockColors();
        for (Block block : ForgeRegistries.BLOCKS.getValues())
            if (block instanceof AbstractSmeltingBlock) {
                ItemBlockRenderTypes.setRenderLayer(block, RenderType.cutoutMipped());
                blockColors.register(BlockColorsHandler.COLOR, block);
                itemColors.register(ItemColorsHandler.COLOR, block.asItem());
                event.enqueueWork(() ->
                {
                    ItemProperties.register(block.asItem(),
                            new ResourceLocation(BetterFurnacesReforged.MOD_ID, "colored"), (stack, level, living, i) -> ItemColorsHandler.itemContainsColor(stack.getOrCreateTag()) ? 1.0F : 0.0F);
                });
            }
    }

}