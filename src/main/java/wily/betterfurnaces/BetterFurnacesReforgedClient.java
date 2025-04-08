package wily.betterfurnaces;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import wily.betterfurnaces.blocks.SmeltingBlock;
import wily.betterfurnaces.client.BFRClientConfig;
import wily.betterfurnaces.client.renderer.ForgeRenderer;
import wily.betterfurnaces.client.renderer.FurnaceRenderer;
import wily.betterfurnaces.client.screen.*;
import wily.betterfurnaces.gitup.UpCheck;
import wily.betterfurnaces.init.BlockEntityTypes;
import wily.betterfurnaces.init.ModObjects;
import wily.betterfurnaces.init.Registration;
import wily.betterfurnaces.items.ColorUpgradeItem;
import wily.factoryapi.*;
//? if >=1.21.4 {
import wily.factoryapi.FactoryAPIPlatform;
import wily.factoryapi.mixin.base.ItemTintSourcesAccessor;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperties;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
//?} else {
/*import net.minecraft.client.renderer.item.ItemProperties;
*///?}

import java.util.List;


public class BetterFurnacesReforgedClient {
    //? if >=1.21.4 {
    public static final ItemTintSource upgradeColorTint = new ItemTintSource() {
        final MapCodec<ItemTintSource> mapCodec = MapCodec.unit(this);
        @Override
        public int calculate(ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity) {
            return getFurnaceColor(itemStack, 0xFFFFFF);
        }

        @Override
        public MapCodec<? extends ItemTintSource> type() {
            return mapCodec;
        }
    };
    //?}

    public static void init() {
        FactoryAPIClient.setup(m->{
            BFRClientConfig.CLIENT_STORAGE.load();
            BlockEntityRenderers.register(BlockEntityTypes.BETTER_FURNACE_TILE.get(), FurnaceRenderer::new);
            BlockEntityRenderers.register(BlockEntityTypes.FORGE_TILE.get(), ForgeRenderer::new);
        });
        //? if <1.21.4 {
        /*Registration.getSmeltingBlocks().forEach((block)->{
            FactoryAPIClient.setup(m-> ItemProperties.register(block.get().asItem(), BetterFurnacesReforged.createModLocation("colored"), (stack, level, living, id) -> ColorUpgradeItem.itemContainsColor(stack) ? 1.0F : 0.0F));
            FactoryAPIClient.registerItemColor(r-> r.accept(BetterFurnacesReforgedClient::getFurnaceColor, block.get().asItem()));
        });
        *///?} else {
        ItemTintSourcesAccessor.getIdMapper().put(BetterFurnacesReforged.createModLocation("upgrade_color"), upgradeColorTint.type());
        //?}
        FactoryAPIClient.registerMenuScreen(menuScreenRegister -> {
            menuScreenRegister.register(ModObjects.FURNACE_CONTAINER.get(), FurnaceScreen::new);
            menuScreenRegister.register(ModObjects.FORGE_CONTAINER.get(), ForgeScreen::new);
            menuScreenRegister.register(ModObjects.COLOR_UPGRADE_CONTAINER.get(), ColorUpgradeScreen::new);
            menuScreenRegister.register(ModObjects.FUEL_VERIFIER_CONTAINER.get(), FuelVerifierScreen::new);
            menuScreenRegister.register(ModObjects.COB_GENERATOR_CONTAINER.get(), CobblestoneGeneratorScreen::new);
        });
        FactoryAPIClient.registerConfigScreen(FactoryAPIPlatform.getModInfo(BetterFurnacesReforged.MOD_ID), BFRConfigScreen::new);
        FactoryAPIClient.PlayerEvent.JOIN_EVENT.register(localPlayer -> {
            if (UpCheck.checkFailed){
                localPlayer.displayClientMessage(Component.Serializer.fromJson(I18n.get(BetterFurnacesReforged.MOD_ID+".update.failed")/*? if >=1.20.5 {*/, localPlayer.registryAccess()/*?}*/), false);
                UpCheck.checkFailed = false;
            } else if (UpCheck.needsUpdateNotify){
                localPlayer.displayClientMessage(Component.Serializer.fromJson(I18n.get(BetterFurnacesReforged.MOD_ID+".update.speech")/*? if >=1.20.5 {*/, localPlayer.registryAccess()/*?}*/), false);
                localPlayer.displayClientMessage(Component.Serializer.fromJson(I18n.get(BetterFurnacesReforged.MOD_ID+".update.version",  BetterFurnacesReforged.MC_VERSION + "-" + BetterFurnacesReforged.VERSION.get(), UpCheck.updateVersionString)/*? if >=1.20.5 {*/, localPlayer.registryAccess()/*?}*/), false);
                localPlayer.displayClientMessage(Component.Serializer.fromJson(I18n.get(BetterFurnacesReforged.MOD_ID+".update.buttons", UpCheck.DOWNLOAD_LINK)/*? if >=1.20.5 {*/, localPlayer.registryAccess()/*?}*/), false);
                UpCheck.needsUpdateNotify = false;
            }
        });
        FactoryAPIClient.registerExtraModels(register->{
            SmeltingBlock.TYPE.getPossibleValues().forEach((i)-> List.of(false,true).forEach((b)-> register.accept(FurnaceRenderer.getFront(i,b))));
            register.accept(BetterFurnacesReforged.createModLocation("colored_furnace"));
            register.accept(BetterFurnacesReforged.createModLocation("colored_forge"));
            register.accept(BetterFurnacesReforged.createModLocation("colored_forge_on"));
            register.accept(BetterFurnacesReforged.createModLocation("nsweud"));
        });
        //? if forge || neoforge {
        FactoryEvent.registerBuiltInPacks(registry->{
            registry.register("programmer_art", FactoryAPI.createLocation(BetterFurnacesReforged.MOD_ID, "programmer_art"), Component.translatable("builtin.betterfurnacesreforged.programmer_art"), Pack.Position.TOP, false);
        });
        //?}
    }

    public static int getFurnaceColor(ItemStack stack, int tint) {
        //? if <1.20.5 {
        /*return ColorUpgradeItem.itemContainsColor(stack) ? ColorUpgradeItem.colorFromTag(stack.getTag()) : 0xFFFFFF;
         *///?} else {
        return stack.getOrDefault(ModObjects.BLOCK_TINT.get(), ModObjects.BlockTint.WHITE).toRGB();
        //?}
    }
}