package wily.betterfurnaces.init;

//? if >=1.20.5 {
import net.minecraft.core.component.DataComponentType;
//?}
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.*;
//? if >=1.21.5 {
import net.minecraft.world.item.component.TooltipDisplay;
//?}
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.Nullable;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.BFRConfig;
import wily.betterfurnaces.blocks.BFRBlock;
import wily.betterfurnaces.blocks.SmeltingBlock;
import wily.factoryapi.FactoryAPIPlatform;
import wily.factoryapi.base.RegisterListing;
import wily.ultimatefurnaces.init.ModObjectsUF;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class Registration {
    public static final RegisterListing<BlockEntityType<?>> BLOCK_ENTITIES = FactoryAPIPlatform.createRegister(BetterFurnacesReforged.MOD_ID, BuiltInRegistries.BLOCK_ENTITY_TYPE);
    public static final RegisterListing<MenuType<?>> CONTAINERS = FactoryAPIPlatform.createRegister(BetterFurnacesReforged.MOD_ID, BuiltInRegistries.MENU);
    static final RegisterListing<RecipeSerializer<?>> RECIPES_SERIALIZERS = FactoryAPIPlatform.createRegister(BetterFurnacesReforged.MOD_ID, BuiltInRegistries.RECIPE_SERIALIZER);
    static final RegisterListing<RecipeType<?>> RECIPES = FactoryAPIPlatform.createRegister(BetterFurnacesReforged.MOD_ID, BuiltInRegistries.RECIPE_TYPE);
    //? if >=1.20.5 {
    static final RegisterListing<DataComponentType<?>> DATA_COMPONENTS = FactoryAPIPlatform.createRegister(BetterFurnacesReforged.MOD_ID, BuiltInRegistries.DATA_COMPONENT_TYPE);
    //?}

    public static final RegisterListing<CreativeModeTab> TABS = FactoryAPIPlatform.createRegister(BetterFurnacesReforged.MOD_ID, BuiltInRegistries.CREATIVE_MODE_TAB);


    public static void init() {
        ModObjects.init();
        //? if >=1.20.5 {
        DATA_COMPONENTS.register();
        //?}
        ModObjectsUF.init();
        BlockEntityTypes.init();
        BLOCK_ENTITIES.register();
        CONTAINERS.register();
        RECIPES_SERIALIZERS.register();
        RECIPES.register();
        TABS.register();
    }

    public static <T extends Block> RegisterListing.Holder<T> registerBlockItem(RegisterListing.Holder<T> holder, RegisterListing<Item> items){
        items.add(holder.getId().getPath(), ()-> new BlockItem(holder.get(), FactoryAPIPlatform.setupBlockItemProperties(new Item.Properties(), holder)){
            @Override
            public void appendHoverText(ItemStack itemStack, /*? if >=1.20.5 {*/Item.TooltipContext tooltipContext/*?} else {*//*@Nullable Level level*//*?}*/, /*? if >=1.21.5 {*/TooltipDisplay tooltipDisplay, Consumer<Component> tooltip/*?} else {*//*List<Component> tooltip*//*?}*/, TooltipFlag tooltipFlag) {
                if (getBlock() instanceof BFRBlock b) b.appendHoverText(itemStack, tooltip/*? if <1.21.5 {*//*::add*//*?}*/, tooltipFlag);
                super.appendHoverText(itemStack, /*? if >=1.20.5 {*/tooltipContext/*?} else {*//*level*//*?}*/, /*? if >=1.21.5 {*/tooltipDisplay, tooltip/*?} else {*//*tooltip*//*?}*/, tooltipFlag);
            }
        });
        return holder;
    }

    public static Stream<RegisterListing.Holder<Block>> getFurnaces(){
        return Stream.concat(ModObjects.FURNACES.stream(), ModObjectsUF.FURNACES.stream());
    }

    public static Stream<RegisterListing.Holder<? extends Block>> getForges(){
        return Stream.concat(Stream.of(ModObjects.EXTREME_FORGE), ModObjectsUF.FORGES.stream());
    }

    public static Stream<RegisterListing.Holder<? extends Block>> getSmeltingBlocks(){
        return Stream.concat(getFurnaces(), getForges());
    }

    public static Stream<RegisterListing.Holder<Item>> getItems(){
        return Stream.concat(ModObjects.ITEMS.stream(), ModObjectsUF.ITEMS.stream());
    }
}
