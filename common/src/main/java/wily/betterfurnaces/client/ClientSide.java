package wily.betterfurnaces.client;

import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import dev.architectury.registry.client.rendering.ColorHandlerRegistry;
import dev.architectury.registry.item.ItemPropertiesRegistry;
import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.Config;
import wily.betterfurnaces.blockentity.ForgeBlockEntity;
import wily.betterfurnaces.blockentity.SmeltingBlockEntity;
import wily.betterfurnaces.blocks.SmeltingBlock;
import wily.betterfurnaces.client.renderer.ForgeRenderer;
import wily.betterfurnaces.client.renderer.FurnaceRenderer;
import wily.betterfurnaces.client.screen.*;
import wily.betterfurnaces.gitup.UpCheck;
import wily.betterfurnaces.init.BlockEntityTypes;
import wily.betterfurnaces.init.ModObjects;
import wily.betterfurnaces.init.Registration;

import java.util.List;
import java.util.function.Consumer;


public class ClientSide {

    public static void init() {
        MenuRegistry.registerScreenFactory(ModObjects.FURNACE_CONTAINER.get(), FurnaceScreen::new);
        MenuRegistry.registerScreenFactory(ModObjects.FORGE_CONTAINER.get(), ForgeScreen::new);
        MenuRegistry.registerScreenFactory(ModObjects.COLOR_UPGRADE_CONTAINER.get(), ColorUpgradeScreen::new);
        MenuRegistry.registerScreenFactory(ModObjects.FUEL_VERIFIER_CONTAINER.get(), FuelVerifierScreen::new);
        MenuRegistry.registerScreenFactory(ModObjects.COB_GENERATOR_CONTAINER.get(), CobblestoneGeneratorScreen::new);
        Registration.BLOCK_ITEMS.forEach((block)->{
            if (block.get() instanceof SmeltingBlock) {
                ItemPropertiesRegistry.register(block.get().asItem(),
                        new ResourceLocation(BetterFurnacesReforged.MOD_ID, "colored"), (stack, level, living, id) -> ItemColorsHandler.itemContainsColor(stack.getOrCreateTag()) ? 1.0F : 0.0F);
            }
            ColorHandlerRegistry.registerItemColors(ItemColorsHandler.COLOR,block.get().asItem());
        });
        BlockEntityRendererRegistry.register(BlockEntityTypes.BETTER_FURNACE_TILE.get(), FurnaceRenderer::new);
        BlockEntityRendererRegistry.register(BlockEntityTypes.FORGE_TILE.get(), ForgeRenderer::new);

    }
    public static void registerExtraModels(Consumer<ResourceLocation> register){
        SmeltingBlock.TYPE.getPossibleValues().forEach((i)-> List.of(false,true).forEach((b)-> register.accept(FurnaceRenderer.getFront(i,b))));
        register.accept(new ModelResourceLocation( new ResourceLocation( "betterfurnacesreforged:colored_furnace"),""));
        register.accept(new ModelResourceLocation( new ResourceLocation( "betterfurnacesreforged:colored_forge"),""));
        register.accept(new ModelResourceLocation( new ResourceLocation( "betterfurnacesreforged:colored_forge_on"),""));
        register.accept(new ModelResourceLocation( new ResourceLocation( "betterfurnacesreforged:nsweud"),""));
    }
    public static void updateClientTick(){
        ClientTickEvent.ClientLevel listener = level -> {
            if(level != null){
                Player player = Minecraft.getInstance().player;
                if(UpCheck.checkFailed){
                    player.displayClientMessage(Component.Serializer.fromJson(I18n.get(BetterFurnacesReforged.MOD_ID+".update.failed")), false);
                }
                else if(UpCheck.needsUpdateNotify){
                    player.displayClientMessage(Component.Serializer.fromJson(I18n.get(BetterFurnacesReforged.MOD_ID+".update.speech")), false);
                    player.displayClientMessage(Component.Serializer.fromJson(I18n.get(BetterFurnacesReforged.MOD_ID+".update.version",  BetterFurnacesReforged.MC_VERSION + "-" + BetterFurnacesReforged.VERSION.get(), UpCheck.updateVersionString)), false);
                    player.displayClientMessage(Component.Serializer.fromJson(I18n.get(BetterFurnacesReforged.MOD_ID+".update.buttons", UpCheck.DOWNLOAD_LINK)), false);
                }
            }
        };
        ClientTickEvent.CLIENT_LEVEL_PRE.register(listener);
        ClientTickEvent.CLIENT_LEVEL_POST.register((i)->{
            if(UpCheck.threadFinished && ClientTickEvent.CLIENT_LEVEL_PRE.isRegistered(listener)) ClientTickEvent.CLIENT_LEVEL_PRE.unregister(listener);
        });
    }




}