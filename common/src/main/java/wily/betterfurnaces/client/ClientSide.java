package wily.betterfurnaces.client;

import com.google.common.collect.Lists;
import me.shedaniel.architectury.registry.*;
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


public class ClientSide {

    public static void init() {
        SmeltingBlock.TYPE.getPossibleValues().forEach((i)-> Lists.newArrayList(false,true).forEach((b)-> BetterFurnacesPlatform.registerModel(FurnaceRenderer.getFront(i,b))));
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
            if(b.getId().getPath().contains("forge")) BlockEntityRenderers.registerRenderer((BlockEntityType<ForgeBlockEntity>) b.get(), ForgeRenderer::new);
            else if(b.getId().getPath().contains("furnace")) BlockEntityRenderers.registerRenderer((BlockEntityType<SmeltingBlockEntity>) b.get(), FurnaceRenderer::new);
        });
        if (Config.enableUltimateFurnaces.get())
            wily.ultimatefurnaces.init.ClientSide.init();


                

    }
    public static void registerBetterFurnacesBlocksClientSide(DeferredRegister<Block> blocks){
        blocks.forEach((block)->{
            if (block.get() instanceof SmeltingBlock) {
                ItemPropertiesRegistry.register(block.get().asItem(),
                        new ResourceLocation(BetterFurnacesReforged.MOD_ID, "colored"), (stack, level, living) -> ItemColorsHandler.itemContainsColor(stack.getOrCreateTag()) ? 1.0F : 0.0F);
            }
            ColorHandlers.registerItemColors(ItemColorsHandler.COLOR,block.get().asItem());
        });
    }

}