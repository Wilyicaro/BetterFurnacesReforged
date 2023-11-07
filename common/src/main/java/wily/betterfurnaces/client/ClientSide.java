package wily.betterfurnaces.client;

import com.google.common.collect.Lists;
import me.shedaniel.architectury.event.events.TickEvent;
import me.shedaniel.architectury.event.events.client.ClientTickEvent;
import me.shedaniel.architectury.registry.*;
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
import wily.betterfurnaces.init.ModObjects;
import wily.betterfurnaces.init.Registration;

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
                        new ResourceLocation(BetterFurnacesReforged.MOD_ID, "colored"), (stack, level, living) -> ItemColorsHandler.itemContainsColor(stack.getOrCreateTag()) ? 1.0F : 0.0F);
            }
            ColorHandlers.registerItemColors(ItemColorsHandler.COLOR,block.get().asItem());
        });
        Registration.BLOCK_ENTITIES.forEach((b)->{
            if(b.getId().getPath().contains("forge")) BlockEntityRenderers.registerRenderer((BlockEntityType<ForgeBlockEntity>) b.get(), ForgeRenderer::new);
            else if(b.getId().getPath().contains("furnace")) BlockEntityRenderers.registerRenderer((BlockEntityType<SmeltingBlockEntity>) b.get(), FurnaceRenderer::new);
        });
    }

    public static void registerExtraModels(Consumer<ResourceLocation> register){
        SmeltingBlock.TYPE.getPossibleValues().forEach((i)-> Lists.newArrayList(false,true).forEach((b)-> register.accept(FurnaceRenderer.getFront(i,b))));
        register.accept(new ModelResourceLocation( new ResourceLocation( "betterfurnacesreforged:colored_furnace"),""));
        register.accept(new ModelResourceLocation( new ResourceLocation( "betterfurnacesreforged:colored_forge"),""));
        register.accept(new ModelResourceLocation( new ResourceLocation( "betterfurnacesreforged:colored_forge_on"),""));
        register.accept(new ModelResourceLocation( new ResourceLocation( "betterfurnacesreforged:nsweud"),""));
    }

    public static void updateClientTick(){
        ClientTickEvent.ClientWorld listener = instance -> {
            Minecraft minecraft = Minecraft.getInstance();
            if(minecraft.player != null){
                Player player = minecraft.player;
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

        ClientTickEvent.CLIENT_WORLD_PRE.register(listener);
        ClientTickEvent.CLIENT_WORLD_POST.register((i)->{
            if(UpCheck.threadFinished && ClientTickEvent.CLIENT_WORLD_PRE.isRegistered(listener)) ClientTickEvent.CLIENT_WORLD_PRE.unregister(listener);
        });
    }
}