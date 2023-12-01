package wily.betterfurnaces.init;

import dev.architectury.registry.registries.DeferredRegister;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import wily.betterfurnaces.BetterFurnacesReforged;
import wily.betterfurnaces.Config;
import wily.betterfurnaces.blocks.BFRBlock;
import wily.ultimatefurnaces.init.ModObjectsUF;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class Registration {
    public static final DeferredRegister<Block> BLOCK_ITEMS = DeferredRegister.create(BetterFurnacesReforged.MOD_ID, Registries.BLOCK);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BetterFurnacesReforged.MOD_ID, Registries.ITEM);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BetterFurnacesReforged.MOD_ID, Registries.BLOCK_ENTITY_TYPE);
    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(BetterFurnacesReforged.MOD_ID, Registries.MENU);
    static final DeferredRegister<RecipeSerializer<?>> RECIPES_SERIALIZERS = DeferredRegister.create( BetterFurnacesReforged.MOD_ID, Registries.RECIPE_SERIALIZER);
    static final DeferredRegister<RecipeType<?>> RECIPES = DeferredRegister.create(BetterFurnacesReforged.MOD_ID, Registries.RECIPE_TYPE);

    public static List<Supplier<Block>> FURNACES = new ArrayList<>();
    public static List<Supplier<Block>> FORGES = new ArrayList<>();
    public static void init() {
        ModObjects.init();
        if (Config.enableUltimateFurnaces.get()) ModObjectsUF.init();
        BLOCK_ITEMS.register();
        BLOCK_ITEMS.forEach((b)-> {
            if (b.getId().getPath().contains("forge")) FORGES.add(b);
            else if (b.getId().getPath().contains("furnace")) FURNACES.add(b);
            BetterFurnacesReforged.REGISTRIES.get().forRegistry(Registries.ITEM, i-> i.register(b.getId(),() -> new BlockItem(b.get(), ((BFRBlock)b.get()).itemProperties)));
        });
        ITEMS.register();
        BlockEntityTypes.init();
        BLOCK_ENTITIES.register();
        CONTAINERS.register();
        RECIPES_SERIALIZERS.register();
        RECIPES.register();
    }
}
