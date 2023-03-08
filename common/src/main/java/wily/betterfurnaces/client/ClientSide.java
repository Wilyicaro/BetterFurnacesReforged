package wily.betterfurnaces.client;

import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import dev.architectury.registry.client.rendering.ColorHandlerRegistry;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import dev.architectury.registry.item.ItemPropertiesRegistry;
import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import wily.betterfurnaces.BetterFurnacesPlatform;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.Config;
import wily.betterfurnaces.blockentity.ForgeBlockEntity;
import wily.betterfurnaces.blockentity.SmeltingBlockEntity;
import wily.betterfurnaces.blocks.SmeltingBlock;
import wily.betterfurnaces.client.renderer.ForgeRenderer;
import wily.betterfurnaces.client.renderer.FurnaceRenderer;
import wily.betterfurnaces.client.screen.*;
import wily.betterfurnaces.init.Registration;

import java.util.List;


public class ClientSide {

    public static void init() {
        SmeltingBlock.TYPE.getPossibleValues().forEach((i)-> List.of(false,true).forEach((b)-> BetterFurnacesPlatform.registerModel(FurnaceRenderer.getFront(i,b))));
        BetterFurnacesPlatform.registerModel(new ModelResourceLocation( new ResourceLocation( "betterfurnacesreforged:colored_furnace"),""));
        BetterFurnacesPlatform.registerModel(new ModelResourceLocation( new ResourceLocation( "betterfurnacesreforged:colored_forge"),""));
        BetterFurnacesPlatform.registerModel(new ModelResourceLocation( new ResourceLocation( "betterfurnacesreforged:colored_forge_on"),""));
        BetterFurnacesPlatform.registerModel(new ModelResourceLocation( new ResourceLocation( "betterfurnacesreforged:nsweud"),""));
        MenuRegistry.registerScreenFactory(Registration.FURNACE_CONTAINER.get(), FurnaceScreen::new);
        MenuRegistry.registerScreenFactory(Registration.FORGE_CONTAINER.get(), ForgeScreen::new);
        MenuRegistry.registerScreenFactory(Registration.COLOR_UPGRADE_CONTAINER.get(), ColorUpgradeScreen::new);
        MenuRegistry.registerScreenFactory(Registration.FUEL_VERIFIER_CONTAINER.get(), FuelVerifierScreen::new);
        MenuRegistry.registerScreenFactory(Registration.COB_GENERATOR_CONTAINER.get(), CobblestoneGeneratorScreen::new);
        registerBetterFurnacesBlocksClientSide(Registration.BLOCK_ITEMS);
        Registration.BLOCK_ENTITIES.forEach((b)->{
            if(b.getId().getPath().contains("forge")) BlockEntityRendererRegistry.register((BlockEntityType<ForgeBlockEntity>) b.get(), ForgeRenderer::new);
            else if(b.getId().getPath().contains("furnace")) BlockEntityRendererRegistry.register((BlockEntityType<SmeltingBlockEntity>) b.get(), FurnaceRenderer::new);
        });
        if (Config.enableUltimateFurnaces.get())
            wily.ultimatefurnaces.init.ClientSide.init();


                

    }
    public static void registerBetterFurnacesBlocksClientSide(DeferredRegister<Block> blocks){
        blocks.forEach((block)->{
            if (block.get() instanceof SmeltingBlock) {
                RenderTypeRegistry.register(RenderType.cutoutMipped(),block.get());
                ItemPropertiesRegistry.register(block.get().asItem(),
                        new ResourceLocation(BetterFurnacesReforged.MOD_ID, "colored"), (stack, level, living, id) -> ItemColorsHandler.itemContainsColor(stack.getOrCreateTag()) ? 1.0F : 0.0F);
            }
            ColorHandlerRegistry.registerItemColors(ItemColorsHandler.COLOR,block.get().asItem());
        });
    }

}