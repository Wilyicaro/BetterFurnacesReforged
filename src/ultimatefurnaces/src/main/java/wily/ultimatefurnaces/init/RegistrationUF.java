package wily.ultimatefurnaces.init;

import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import wily.betterfurnaces.items.ItemUpgradeOreProcessing;
import wily.ultimatefurnaces.UltimateFurnaces;
import wily.ultimatefurnaces.blocks.*;
import wily.ultimatefurnaces.container.*;
import wily.ultimatefurnaces.items.*;
import wily.ultimatefurnaces.blockentity.*;

public class RegistrationUF {

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, UltimateFurnaces.MOD_ID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, UltimateFurnaces.MOD_ID);
    private static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, UltimateFurnaces.MOD_ID);
    private static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, UltimateFurnaces.MOD_ID);

    public static void init() {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        TILES.register(FMLJavaModLoadingContext.get().getModEventBus());
        CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<BlockCopperFurnace> COPPER_FURNACE = BLOCKS.register(BlockCopperFurnace.COPPER_FURNACE, () -> new BlockCopperFurnace(BlockBehaviour.Properties.copy(Blocks.COAL_BLOCK).strength(10.0F, 20.0F)));
    public static final RegistryObject<Item> COPPER_FURNACE_ITEM = ITEMS.register(BlockCopperFurnace.COPPER_FURNACE, () -> new BlockItem(COPPER_FURNACE.get(), new Item.Properties().tab(wily.ultimatefurnaces.init.ModObjects.ITEM_GROUP)));
    public static final RegistryObject<BlockEntityType<BlockEntityCopperFurnace>> COPPER_FURNACE_TILE = TILES.register(BlockCopperFurnace.COPPER_FURNACE, () -> BlockEntityType.Builder.of(BlockEntityCopperFurnace::new, COPPER_FURNACE.get()).build(null));

    public static final RegistryObject<MenuType<BlockCopperFurnaceContainer>> COPPER_FURNACE_CONTAINER = CONTAINERS.register(BlockCopperFurnace.COPPER_FURNACE, () -> IForgeMenuType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        Level world = inv.player.level;
        return new BlockCopperFurnaceContainer(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistryObject<BlockSteelFurnace> STEEL_FURNACE = BLOCKS.register(BlockSteelFurnace.STEEL_FURNACE, () -> new BlockSteelFurnace(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(20.0F, 60.0F)));
    public static final RegistryObject<Item> STEEL_FURNACE_ITEM = ITEMS.register(BlockSteelFurnace.STEEL_FURNACE, () -> new BlockItem(STEEL_FURNACE.get(), new Item.Properties().tab(wily.ultimatefurnaces.init.ModObjects.ITEM_GROUP)));
    public static final RegistryObject<BlockEntityType<BlockEntitySteelFurnace>> STEEL_FURNACE_TILE = TILES.register(BlockSteelFurnace.STEEL_FURNACE, () -> BlockEntityType.Builder.of(BlockEntitySteelFurnace::new, STEEL_FURNACE.get()).build(null));

    public static final RegistryObject<MenuType<BlockSteelFurnaceContainer>> STEEL_FURNACE_CONTAINER = CONTAINERS.register(BlockSteelFurnace.STEEL_FURNACE, () -> IForgeMenuType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        Level world = inv.player.level;
        return new BlockSteelFurnaceContainer(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistryObject<BlockAmethystFurnace> AMETHYST_FURNACE = BLOCKS.register(BlockAmethystFurnace.AMETHYST_FURNACE, () -> new BlockAmethystFurnace(BlockBehaviour.Properties.copy(Blocks.AMETHYST_BLOCK).strength(20.0F, 30.0F)));
    public static final RegistryObject<Item> AMETHYST_FURNACE_ITEM = ITEMS.register(BlockAmethystFurnace.AMETHYST_FURNACE, () -> new BlockItem(AMETHYST_FURNACE.get(), new Item.Properties().tab(wily.ultimatefurnaces.init.ModObjects.ITEM_GROUP)));
    public static final RegistryObject<BlockEntityType<BlockEntityAmethystFurnace>> AMETHYST_FURNACE_TILE = TILES.register(BlockAmethystFurnace.AMETHYST_FURNACE, () -> BlockEntityType.Builder.of(BlockEntityAmethystFurnace::new, AMETHYST_FURNACE.get()).build(null));

    public static final RegistryObject<MenuType<BlockAmethystFurnaceContainer>> AMETHYST_FURNACE_CONTAINER = CONTAINERS.register(BlockAmethystFurnace.AMETHYST_FURNACE, () -> IForgeMenuType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        Level world = inv.player.level;
        return new BlockAmethystFurnaceContainer(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistryObject<BlockPlatinumFurnace> PLATINUM_FURNACE = BLOCKS.register(BlockPlatinumFurnace.PLATINUM_FURNACE, () -> new BlockPlatinumFurnace(BlockBehaviour.Properties.copy(Blocks.RAW_GOLD_BLOCK).strength(30.0F, 60.0F)));
    public static final RegistryObject<Item> PLATINUM_FURNACE_ITEM = ITEMS.register(BlockPlatinumFurnace.PLATINUM_FURNACE, () -> new BlockItem(PLATINUM_FURNACE.get(), new Item.Properties().tab(wily.ultimatefurnaces.init.ModObjects.ITEM_GROUP)));
    public static final RegistryObject<BlockEntityType<BlockEntityPlatinumFurnace>> PLATINUM_FURNACE_TILE = TILES.register(BlockPlatinumFurnace.PLATINUM_FURNACE, () -> BlockEntityType.Builder.of(BlockEntityPlatinumFurnace::new, PLATINUM_FURNACE.get()).build(null));

    public static final RegistryObject<MenuType<BlockPlatinumFurnaceContainer>> PLATINUM_FURNACE_CONTAINER = CONTAINERS.register(BlockPlatinumFurnace.PLATINUM_FURNACE, () -> IForgeMenuType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        Level world = inv.player.level;
        return new BlockPlatinumFurnaceContainer(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistryObject<BlockUltimateFurnace> ULTIMATE_FURNACE = BLOCKS.register(BlockUltimateFurnace.ULTIMATE_FURNACE, () -> new BlockUltimateFurnace(BlockBehaviour.Properties.copy(Blocks.NETHERITE_BLOCK).strength(50.0F, 6000.0F)));
    public static final RegistryObject<Item> ULTIMATE_FURNACE_ITEM = ITEMS.register(BlockUltimateFurnace.ULTIMATE_FURNACE, () -> new BlockItem(ULTIMATE_FURNACE.get(), new Item.Properties().tab(ModObjects.ITEM_GROUP)));
    public static final RegistryObject<BlockEntityType<BlockEntityUltimateFurnace>> ULTIMATE_FURNACE_TILE = TILES.register(BlockUltimateFurnace.ULTIMATE_FURNACE, () -> BlockEntityType.Builder.of(BlockEntityUltimateFurnace::new, ULTIMATE_FURNACE.get()).build(null));

    public static final RegistryObject<MenuType<BlockUltimateFurnaceContainer>> ULTIMATE_FURNACE_CONTAINER = CONTAINERS.register(BlockUltimateFurnace.ULTIMATE_FURNACE, () -> IForgeMenuType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        Level world = inv.player.level;
        return new BlockUltimateFurnaceContainer(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistryObject<BlockCopperForge> COPPER_FORGE = BLOCKS.register(BlockCopperForge.COPPER_FORGE, () -> new BlockCopperForge(BlockBehaviour.Properties.copy(Blocks.COAL_BLOCK)));
    public static final RegistryObject<Item> COPPER_FORGE_ITEM = ITEMS.register(BlockCopperForge.COPPER_FORGE, () -> new BlockItem(COPPER_FORGE.get(), new Item.Properties().tab(ModObjects.ITEM_GROUP)));
    public static final RegistryObject<BlockEntityType<BlockEntityCopperForge>> COPPER_FORGE_TILE = TILES.register(BlockCopperForge.COPPER_FORGE, () -> BlockEntityType.Builder.of(BlockEntityCopperForge::new, COPPER_FORGE.get()).build(null));

    public static final RegistryObject<MenuType<BlockCopperForgeContainer>> COPPER_FORGE_CONTAINER = CONTAINERS.register(BlockCopperForge.COPPER_FORGE, () -> IForgeMenuType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        Level world = inv.player.level;
        return new BlockCopperForgeContainer(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistryObject<BlockIronForge> IRON_FORGE = BLOCKS.register(BlockIronForge.IRON_FORGE, () -> new BlockIronForge(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));
    public static final RegistryObject<Item> IRON_FORGE_ITEM = ITEMS.register(BlockIronForge.IRON_FORGE, () -> new BlockItem(IRON_FORGE.get(), new Item.Properties().tab(ModObjects.ITEM_GROUP)));
    public static final RegistryObject<BlockEntityType<BlockEntityIronForge>> IRON_FORGE_TILE = TILES.register(BlockIronForge.IRON_FORGE, () -> BlockEntityType.Builder.of(BlockEntityIronForge::new, IRON_FORGE.get()).build(null));

    public static final RegistryObject<MenuType<BlockIronForgeContainer>> IRON_FORGE_CONTAINER = CONTAINERS.register(BlockIronForge.IRON_FORGE, () -> IForgeMenuType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        Level world = inv.player.level;
        return new BlockIronForgeContainer(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistryObject<BlockGoldForge> GOLD_FORGE = BLOCKS.register(BlockGoldForge.GOLD_FORGE, () -> new BlockGoldForge(BlockBehaviour.Properties.copy(Blocks.GOLD_BLOCK)));
    public static final RegistryObject<Item> GOLD_FORGE_ITEM = ITEMS.register(BlockGoldForge.GOLD_FORGE, () -> new BlockItem(GOLD_FORGE.get(), new Item.Properties().tab(wily.ultimatefurnaces.init.ModObjects.ITEM_GROUP)));
    public static final RegistryObject<BlockEntityType<BlockEntityGoldForge>> GOLD_FORGE_TILE = TILES.register(BlockGoldForge.GOLD_FORGE, () -> BlockEntityType.Builder.of(BlockEntityGoldForge::new, GOLD_FORGE.get()).build(null));

    public static final RegistryObject<MenuType<BlockGoldForgeContainer>> GOLD_FORGE_CONTAINER = CONTAINERS.register(BlockGoldForge.GOLD_FORGE, () -> IForgeMenuType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        Level world = inv.player.level;
        return new BlockGoldForgeContainer(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistryObject<BlockDiamondForge> DIAMOND_FORGE = BLOCKS.register(BlockDiamondForge.DIAMOND_FORGE, () -> new BlockDiamondForge(BlockBehaviour.Properties.copy(Blocks.DIAMOND_BLOCK)));
    public static final RegistryObject<Item> DIAMOND_FORGE_ITEM = ITEMS.register(BlockDiamondForge.DIAMOND_FORGE, () -> new BlockItem(DIAMOND_FORGE.get(), new Item.Properties().tab(ModObjects.ITEM_GROUP)));
    public static final RegistryObject<BlockEntityType<BlockEntityDiamondForge>> DIAMOND_FORGE_TILE = TILES.register(BlockDiamondForge.DIAMOND_FORGE, () -> BlockEntityType.Builder.of(BlockEntityDiamondForge::new, DIAMOND_FORGE.get()).build(null));

    public static final RegistryObject<MenuType<BlockDiamondForgeContainer>> DIAMOND_FORGE_CONTAINER = CONTAINERS.register(BlockDiamondForge.DIAMOND_FORGE, () -> IForgeMenuType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        Level world = inv.player.level;
        return new BlockDiamondForgeContainer(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistryObject<BlockNetherhotForge> NETHERHOT_FORGE = BLOCKS.register(BlockNetherhotForge.NETHERHOT_FORGE, () -> new BlockNetherhotForge(BlockBehaviour.Properties.copy(Blocks.DIAMOND_BLOCK)));
    public static final RegistryObject<Item> NETHERHOT_FORGE_ITEM = ITEMS.register(BlockNetherhotForge.NETHERHOT_FORGE, () -> new BlockItem(NETHERHOT_FORGE.get(), new Item.Properties().tab(ModObjects.ITEM_GROUP)));
    public static final RegistryObject<BlockEntityType<BlockEntityNetherhotForge>> NETHERHOT_FORGE_TILE = TILES.register(BlockNetherhotForge.NETHERHOT_FORGE, () -> BlockEntityType.Builder.of(BlockEntityNetherhotForge::new, NETHERHOT_FORGE.get()).build(null));

    public static final RegistryObject<MenuType<BlockNetherhotForgeContainer>> NETHERHOT_FORGE_CONTAINER = CONTAINERS.register(BlockNetherhotForge.NETHERHOT_FORGE, () -> IForgeMenuType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        Level world = inv.player.level;
        return new BlockNetherhotForgeContainer(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistryObject<BlockUltimateForge> ULTIMATE_FORGE = BLOCKS.register(BlockUltimateForge.ULTIMATE_FORGE, () -> new BlockUltimateForge(BlockBehaviour.Properties.copy(Blocks.NETHERITE_BLOCK).strength(50.0F, 6000.0F)));
    public static final RegistryObject<Item> ULTIMATE_FORGE_ITEM = ITEMS.register(BlockUltimateForge.ULTIMATE_FORGE, () -> new BlockItem(ULTIMATE_FORGE.get(), new Item.Properties().tab(ModObjects.ITEM_GROUP)));
    public static final RegistryObject<BlockEntityType<BlockEntityUltimateForge>> ULTIMATE_FORGE_TILE = TILES.register(BlockUltimateForge.ULTIMATE_FORGE, () -> BlockEntityType.Builder.of(BlockEntityUltimateForge::new, ULTIMATE_FORGE.get()).build(null));

    public static final RegistryObject<MenuType<BlockUltimateForgeContainer>> ULTIMATE_FORGE_CONTAINER = CONTAINERS.register(BlockUltimateForge.ULTIMATE_FORGE, () -> IForgeMenuType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        Level world = inv.player.level;
        return new BlockUltimateForgeContainer(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistryObject<wily.ultimatefurnaces.items.ItemUpgradeCopper> COPPER_UPGRADE = ITEMS.register("copper_upgrade", () -> new ItemUpgradeCopper(new Item.Properties().tab(ModObjects.ITEM_GROUP)));

    public static final RegistryObject<wily.ultimatefurnaces.items.ItemUpgradeIron> IRON_UPGRADE = ITEMS.register("iron_upgrade", () -> new ItemUpgradeIron(new Item.Properties().tab(ModObjects.ITEM_GROUP)));

    public static final RegistryObject<ItemUpgradeSteel> STEEL_UPGRADE = ITEMS.register("steel_upgrade", () -> new ItemUpgradeSteel(new Item.Properties().tab(ModObjects.ITEM_GROUP)));

    public static final RegistryObject<wily.ultimatefurnaces.items.ItemUpgradeGold> GOLD_UPGRADE = ITEMS.register("gold_upgrade", () -> new ItemUpgradeGold(new Item.Properties().tab(ModObjects.ITEM_GROUP)));

    public static final RegistryObject<wily.ultimatefurnaces.items.ItemUpgradeAmethyst> AMETHYST_UPGRADE = ITEMS.register("amethyst_upgrade", () -> new ItemUpgradeAmethyst(new Item.Properties().tab(ModObjects.ITEM_GROUP)));

    public static final RegistryObject<wily.ultimatefurnaces.items.ItemUpgradeDiamond> DIAMOND_UPGRADE = ITEMS.register("diamond_upgrade", () -> new ItemUpgradeDiamond(new Item.Properties().tab(ModObjects.ITEM_GROUP)));

    public static final RegistryObject<wily.ultimatefurnaces.items.ItemUpgradePlatinum> PLATINUM_UPGRADE = ITEMS.register("platinum_upgrade", () -> new ItemUpgradePlatinum(new Item.Properties().tab(ModObjects.ITEM_GROUP)));

    public static final RegistryObject<wily.ultimatefurnaces.items.ItemUpgradeNetherhot> NETHERHOT_UPGRADE = ITEMS.register("netherhot_upgrade", () -> new ItemUpgradeNetherhot(new Item.Properties().tab(ModObjects.ITEM_GROUP)));

    public static final RegistryObject<wily.ultimatefurnaces.items.ItemUpgradeUltimate> ULTIMATE_UPGRADE = ITEMS.register("ultimate_upgrade", () -> new ItemUpgradeUltimate(new Item.Properties().tab(ModObjects.ITEM_GROUP)));

    public static final RegistryObject<ItemUpgradeOreProcessing> UORE_PROCESSING = ITEMS.register("ultimate_ore_processing_upgrade", () -> new ItemUpgradeOreProcessing(new Item.Properties().tab(ModObjects.ITEM_GROUP),4,true,true));

}
