package wily.ultimatefurnaces.init;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import wily.betterfurnaces.items.ItemUpgradeOreProcessing;
import wily.ultimatefurnaces.UltimateFurnaces;
import wily.ultimatefurnaces.blocks.*;
import wily.ultimatefurnaces.container.*;
import wily.ultimatefurnaces.items.ItemUpgradeCopper;
import wily.ultimatefurnaces.items.ItemUpgradeIron;
import wily.ultimatefurnaces.items.ItemUpgradeUltimate;
import wily.ultimatefurnaces.tileentity.*;

public class Registration {

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, UltimateFurnaces.MOD_ID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, UltimateFurnaces.MOD_ID);
    private static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, UltimateFurnaces.MOD_ID);
    private static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, UltimateFurnaces.MOD_ID);

    public static void init() {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        TILES.register(FMLJavaModLoadingContext.get().getModEventBus());
        CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<BlockCopperFurnace> COPPER_FURNACE = BLOCKS.register(BlockCopperFurnace.COPPER_FURNACE, () -> new BlockCopperFurnace(AbstractBlock.Properties.copy(Blocks.COAL_BLOCK).strength(10.0F, 20.0F)));
    public static final RegistryObject<Item> COPPER_FURNACE_ITEM = ITEMS.register(BlockCopperFurnace.COPPER_FURNACE, () -> new BlockItem(COPPER_FURNACE.get(), new Item.Properties().tab(wily.ultimatefurnaces.init.ModObjects.ITEM_GROUP)));
    public static final RegistryObject<TileEntityType<BlockCopperFurnaceTile>> COPPER_FURNACE_TILE = TILES.register(BlockCopperFurnace.COPPER_FURNACE, () -> TileEntityType.Builder.of(BlockCopperFurnaceTile::new, COPPER_FURNACE.get()).build(null));

    public static final RegistryObject<ContainerType<BlockCopperFurnaceContainer>> COPPER_FURNACE_CONTAINER = CONTAINERS.register(BlockCopperFurnace.COPPER_FURNACE, () -> IForgeContainerType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        World world = inv.player.getEntity().level;
        return new BlockCopperFurnaceContainer(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistryObject<BlockUltimateFurnace> ULTIMATE_FURNACE = BLOCKS.register(BlockUltimateFurnace.ULTIMATE_FURNACE, () -> new BlockUltimateFurnace(AbstractBlock.Properties.copy(Blocks.NETHERITE_BLOCK).strength(50.0F, 6000.0F)));
    public static final RegistryObject<Item> ULTIMATE_FURNACE_ITEM = ITEMS.register(BlockUltimateFurnace.ULTIMATE_FURNACE, () -> new BlockItem(ULTIMATE_FURNACE.get(), new Item.Properties().tab(ModObjects.ITEM_GROUP)));
    public static final RegistryObject<TileEntityType<BlockUltimateFurnaceTile>> ULTIMATE_FURNACE_TILE = TILES.register(BlockUltimateFurnace.ULTIMATE_FURNACE, () -> TileEntityType.Builder.of(BlockUltimateFurnaceTile::new, ULTIMATE_FURNACE.get()).build(null));

    public static final RegistryObject<ContainerType<BlockUltimateFurnaceContainer>> ULTIMATE_FURNACE_CONTAINER = CONTAINERS.register(BlockUltimateFurnace.ULTIMATE_FURNACE, () -> IForgeContainerType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        World world = inv.player.getEntity().level;
        return new BlockUltimateFurnaceContainer(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistryObject<BlockCopperForge> COPPER_FORGE = BLOCKS.register(BlockCopperForge.COPPER_FORGE, () -> new BlockCopperForge(AbstractBlock.Properties.copy(Blocks.COAL_BLOCK)));
    public static final RegistryObject<Item> COPPER_FORGE_ITEM = ITEMS.register(BlockCopperForge.COPPER_FORGE, () -> new BlockItem(COPPER_FORGE.get(), new Item.Properties().tab(ModObjects.ITEM_GROUP)));
    public static final RegistryObject<TileEntityType<BlockCopperForgeTile>> COPPER_FORGE_TILE = TILES.register(BlockCopperForge.COPPER_FORGE, () -> TileEntityType.Builder.of(BlockCopperForgeTile::new, COPPER_FORGE.get()).build(null));

    public static final RegistryObject<ContainerType<BlockCopperForgeContainer>> COPPER_FORGE_CONTAINER = CONTAINERS.register(BlockCopperForge.COPPER_FORGE, () -> IForgeContainerType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        World world = inv.player.getEntity().level;
        return new BlockCopperForgeContainer(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistryObject<BlockIronForge> IRON_FORGE = BLOCKS.register(BlockIronForge.IRON_FORGE, () -> new BlockIronForge(AbstractBlock.Properties.copy(Blocks.IRON_BLOCK)));
    public static final RegistryObject<Item> IRON_FORGE_ITEM = ITEMS.register(BlockIronForge.IRON_FORGE, () -> new BlockItem(IRON_FORGE.get(), new Item.Properties().tab(ModObjects.ITEM_GROUP)));
    public static final RegistryObject<TileEntityType<BlockIronForgeTile>> IRON_FORGE_TILE = TILES.register(BlockIronForge.IRON_FORGE, () -> TileEntityType.Builder.of(BlockIronForgeTile::new, IRON_FORGE.get()).build(null));

    public static final RegistryObject<ContainerType<BlockIronForgeContainer>> IRON_FORGE_CONTAINER = CONTAINERS.register(BlockIronForge.IRON_FORGE, () -> IForgeContainerType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        World world = inv.player.getEntity().level;
        return new BlockIronForgeContainer(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistryObject<BlockGoldForge> GOLD_FORGE = BLOCKS.register(BlockGoldForge.GOLD_FORGE, () -> new BlockGoldForge(AbstractBlock.Properties.copy(Blocks.GOLD_BLOCK)));
    public static final RegistryObject<Item> GOLD_FORGE_ITEM = ITEMS.register(BlockGoldForge.GOLD_FORGE, () -> new BlockItem(GOLD_FORGE.get(), new Item.Properties().tab(ModObjects.ITEM_GROUP)));
    public static final RegistryObject<TileEntityType<BlockGoldForgeTile>> GOLD_FORGE_TILE = TILES.register(BlockGoldForge.GOLD_FORGE, () -> TileEntityType.Builder.of(BlockGoldForgeTile::new, GOLD_FORGE.get()).build(null));

    public static final RegistryObject<ContainerType<BlockGoldForgeContainer>> GOLD_FORGE_CONTAINER = CONTAINERS.register(BlockGoldForge.GOLD_FORGE, () -> IForgeContainerType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        World world = inv.player.getEntity().level;
        return new BlockGoldForgeContainer(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistryObject<BlockDiamondForge> DIAMOND_FORGE = BLOCKS.register(BlockDiamondForge.DIAMOND_FORGE, () -> new BlockDiamondForge(AbstractBlock.Properties.copy(Blocks.DIAMOND_BLOCK)));
    public static final RegistryObject<Item> DIAMOND_FORGE_ITEM = ITEMS.register(BlockDiamondForge.DIAMOND_FORGE, () -> new BlockItem(DIAMOND_FORGE.get(), new Item.Properties().tab(ModObjects.ITEM_GROUP)));
    public static final RegistryObject<TileEntityType<BlockDiamondForgeTile>> DIAMOND_FORGE_TILE = TILES.register(BlockDiamondForge.DIAMOND_FORGE, () -> TileEntityType.Builder.of(BlockDiamondForgeTile::new, DIAMOND_FORGE.get()).build(null));

    public static final RegistryObject<ContainerType<BlockDiamondForgeContainer>> DIAMOND_FORGE_CONTAINER = CONTAINERS.register(BlockDiamondForge.DIAMOND_FORGE, () -> IForgeContainerType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        World world = inv.player.getEntity().level;
        return new BlockDiamondForgeContainer(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistryObject<BlockNetherhotForge> NETHERHOT_FORGE = BLOCKS.register(BlockNetherhotForge.NETHERHOT_FORGE, () -> new BlockNetherhotForge(AbstractBlock.Properties.copy(Blocks.DIAMOND_BLOCK)));
    public static final RegistryObject<Item> NETHERHOT_FORGE_ITEM = ITEMS.register(BlockNetherhotForge.NETHERHOT_FORGE, () -> new BlockItem(NETHERHOT_FORGE.get(), new Item.Properties().tab(ModObjects.ITEM_GROUP)));
    public static final RegistryObject<TileEntityType<BlockNetherhotForgeTile>> NETHERHOT_FORGE_TILE = TILES.register(BlockNetherhotForge.NETHERHOT_FORGE, () -> TileEntityType.Builder.of(BlockNetherhotForgeTile::new, NETHERHOT_FORGE.get()).build(null));

    public static final RegistryObject<ContainerType<BlockNetherhotForgeContainer>> NETHERHOT_FORGE_CONTAINER = CONTAINERS.register(BlockNetherhotForge.NETHERHOT_FORGE, () -> IForgeContainerType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        World world = inv.player.getEntity().level;
        return new BlockNetherhotForgeContainer(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistryObject<BlockUltimateForge> ULTIMATE_FORGE = BLOCKS.register(BlockUltimateForge.ULTIMATE_FORGE, () -> new BlockUltimateForge(AbstractBlock.Properties.copy(Blocks.NETHERITE_BLOCK).strength(50.0F, 6000.0F)));
    public static final RegistryObject<Item> ULTIMATE_FORGE_ITEM = ITEMS.register(BlockUltimateForge.ULTIMATE_FORGE, () -> new BlockItem(ULTIMATE_FORGE.get(), new Item.Properties().tab(ModObjects.ITEM_GROUP)));
    public static final RegistryObject<TileEntityType<BlockUltimateForgeTile>> ULTIMATE_FORGE_TILE = TILES.register(BlockUltimateForge.ULTIMATE_FORGE, () -> TileEntityType.Builder.of(BlockUltimateForgeTile::new, ULTIMATE_FORGE.get()).build(null));

    public static final RegistryObject<ContainerType<BlockUltimateForgeContainer>> ULTIMATE_FORGE_CONTAINER = CONTAINERS.register(BlockUltimateForge.ULTIMATE_FORGE, () -> IForgeContainerType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        World world = inv.player.getEntity().level;
        return new BlockUltimateForgeContainer(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistryObject<wily.ultimatefurnaces.items.ItemUpgradeCopper> COPPER_UPGRADE = ITEMS.register("copper_upgrade", () -> new ItemUpgradeCopper(new Item.Properties().tab(ModObjects.ITEM_GROUP)));

    public static final RegistryObject<wily.ultimatefurnaces.items.ItemUpgradeIron> IRON_UPGRADE = ITEMS.register("iron_upgrade", () -> new ItemUpgradeIron(new Item.Properties().tab(ModObjects.ITEM_GROUP)));

    public static final RegistryObject<wily.ultimatefurnaces.items.ItemUpgradeUltimate> ULTIMATE_UPGRADE = ITEMS.register("ultimate_upgrade", () -> new ItemUpgradeUltimate(new Item.Properties().tab(ModObjects.ITEM_GROUP)));

    public static final RegistryObject<ItemUpgradeOreProcessing> UORE = ITEMS.register("ultimate_ore_processing_upgrade", () -> new ItemUpgradeOreProcessing(new Item.Properties().tab(ModObjects.ITEM_GROUP),4));

    public static void registerBlocks(RegistryEvent.Register<Block> event)
    {
    }
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
    }

}
